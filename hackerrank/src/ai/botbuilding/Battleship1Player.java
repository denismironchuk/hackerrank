package ai.botbuilding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Battleship1Player {
    private static int SIDE = 10;

    private static int[] POSSIBLE_SHIPS = new int[] {2, 2, 3, 4, 5};

    private static class Point {
        private int row;
        private int col;

        public Point(final int row, final int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(final Object o) {
            Point point = (Point) o;
            if (row != point.row) return false;
            return col == point.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    private static int[][] generateDistribution(char[][] board) {
        List<Integer> destrShips = getDestroyedShips(board);
        List<Integer> remainShips = new ArrayList<>();

        for (int ship : POSSIBLE_SHIPS) {
            if (!destrShips.contains(ship)) {
                remainShips.add(ship);
            } else {
                destrShips.remove(destrShips.indexOf(ship));
            }
        }

        int[][] distr = new int[SIDE][SIDE];

        for (int ship : remainShips) {
            for (int row = 0; row < SIDE; row++) {
                for (int col = 0; col < SIDE; col++) {
                    for (int offset = 0; offset < ship; offset++) {
                        distr[row][col]+=canContainShip(ship, row, col, board, true, offset);
                        distr[row][col]+=canContainShip(ship, row, col, board, false, offset);
                    }
                }
            }
        }

        return distr;
    }

    private static int canContainShip(int ship, int row, int col, char[][] board, boolean isVertical, int offset) {
        if (isVertical) {
            int startRow = row - offset;
            for (int i = 0; i < ship; i++) {
                if (startRow + i < 0 || startRow + i >= SIDE || (board[startRow + i][col] != '-' && board[startRow + i][col] != 'h')) {
                    return 0;
                }
            }
        } else {
            int startCol = col - offset;
            for (int i = 0; i < ship; i++) {
                if (startCol + i < 0 || startCol + i >= SIDE || (board[row][startCol + i] != '-' && board[row][startCol + i] != 'h')) {
                    return 0;
                }
            }
        }

        return 1;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int side = Integer.parseInt(br.readLine());
        char[][] board = new char[SIDE][SIDE];

        List<Point> hitPoints = new ArrayList<>();

        for (int row = 0; row < SIDE; row++) {
            String line = br.readLine();
            for (int col = 0; col < SIDE; col++) {
                board[row][col] = line.charAt(col);
                if (board[row][col] == 'h') {
                    hitPoints.add(new Point(row, col));
                }
            }
        }

        int[][] currentDistr = generateDistribution(board);
        List<Point> candidates = new ArrayList<>();

        if (!hitPoints.isEmpty()) {
            Set<Point> hitCandidates = new HashSet<>();
            for (Point p : hitPoints) {
                hitCandidates.addAll(getFreeNeighbours(p, board));
            }

            int maxDistr = -1;

            for (Point p : hitCandidates) {
                if (currentDistr[p.row][p.col] > maxDistr) {
                    maxDistr = currentDistr[p.row][p.col];
                    candidates.clear();
                    candidates.add(p);
                } else if (currentDistr[p.row][p.col] == maxDistr) {
                    candidates.add(p);
                }
            }

        } else {
            int maxDistr = -1;

            for (int i = 0; i < SIDE; i++) {
                for (int j = 0; j < SIDE; j++) {
                    if (board[i][j] == '-') {
                        if (currentDistr[i][j] > maxDistr) {
                            maxDistr = currentDistr[i][j];
                            candidates.clear();
                            candidates.add(new Point(i, j));
                        } else if (currentDistr[i][j] == maxDistr) {
                            candidates.add(new Point(i, j));
                        }
                    }
                }
            }
        }

        Point maxPoint = candidates.get((int) (Math.random() * candidates.size()));
        System.out.println(maxPoint.row + " " + maxPoint.col);
    }

    private static Set<Point> getFreeNeighbours(Point p, char[][] board) {
        Set<Point> neighs = new HashSet<>();

        if (p.row > 0 && board[p.row - 1][p.col] == '-') {
            neighs.add(new Point(p.row - 1, p.col));
        }

        if (p.row < SIDE - 1 && board[p.row + 1][p.col] == '-') {
            neighs.add(new Point(p.row + 1, p.col));
        }

        if (p.col > 0 && board[p.row][p.col - 1] == '-') {
            neighs.add(new Point(p.row, p.col - 1));
        }

        if (p.col < SIDE - 1 && board[p.row][p.col + 1] == '-') {
            neighs.add(new Point(p.row, p.col + 1));
        }

        return neighs;
    }

    private static List<Integer> getDestroyedShips(char[][] board) {
        List<Integer> ships = new ArrayList<>();
        boolean[][] processed = new boolean[SIDE][SIDE];

        for (int row = 0; row < SIDE; row++) {
            for (int col = 0; col < SIDE; col++){
                if (!processed[row][col] && board[row][col] == 'd') {
                    Point p = new Point(row, col);
                    int len1 = getShip(p, board, processed, true);
                    int len2 = getShip(p, board, processed, false);

                    if (len1 != 1 && len2 != 1) {
                        ships.add(len1);
                        ships.add(len2);
                    } else if (len1 == 1 && len2 == 1) {
                        ships.add(len1);
                    } else {
                        ships.add(Math.max(len1, len2));
                    }
                }
                processed[row][col] = true;
            }
        }

        return ships;
    }

    private static int getShip(Point p, char[][] board, boolean[][] processed, boolean isVert) {
        int len = 1;
        int col = p.col;
        int row = p.row;

        if (isVert) {
            row++;
            while (row < SIDE && board[row][col] == 'd') {
                processed[row][col] = true;
                len++;
                row++;
            }
        } else {
            col++;
            while (col < SIDE && board[row][col] == 'd') {
                processed[row][col] = true;
                len++;
                col++;
            }
        }

        return len;
    }
}
