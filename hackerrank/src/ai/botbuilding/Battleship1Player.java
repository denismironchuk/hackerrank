package ai.botbuilding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Battleship1Player {
    private static int SIDE = 10;

    private static int[] POSSIBLE_SHIPS = new int[] {1, 1, 2, 2, 3, 4, 5};

    private static class Point {
        private int row;
        private int col;

        public Point(final int row, final int col) {
            this.row = row;
            this.col = col;
        }

        public int manhDist(Point p) {
            return Math.abs(row - p.row) + Math.abs(col - p.col);
        }

        @Override
        public boolean equals(final Object o) {
            Point point = (Point) o;
            if (row != point.row) return false;
            return col == point.col;
        }
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

        for (Point p : hitPoints) {
            if (countHitNeighbours(p, board) < 2) {
                Point toHit = getPointToHit(p, board);
                if (toHit != null) {
                    System.out.println(toHit.row + " " + toHit.col);
                    return;
                }
            }
        }

        List<Integer> destrShips = getDestroyedShips(board);
        List<Integer> remainShips = new ArrayList<>();

        for (int ship : POSSIBLE_SHIPS) {
            if (!destrShips.contains(ship)) {
                remainShips.add(ship);
            } else {
                destrShips.remove(destrShips.indexOf(ship));
            }
        }

        int rowToHit = (int)(SIDE * Math.random());
        int colToHit = (int)(SIDE * Math.random());

        while (board[rowToHit][colToHit] != '-' || !pointCanContainShips(remainShips, rowToHit, colToHit, board)) {
            rowToHit = (int)(SIDE * Math.random());
            colToHit = (int)(SIDE * Math.random());
        }

        System.out.println(rowToHit + " " + colToHit);
    }

    private static boolean pointCanContainShips(List<Integer> remainShips, int row, int col, char[][] board) {
        int vert = 1;

        int row1 = row + 1;
        int col1 = col;

        while (row1 < SIDE && board[row1][col1] == '-') {
            vert++;
            row1++;
        }

        row1 = row - 1;
        col1 = col;

        while (row1 >= 0 && board[row1][col1] == '-') {
            vert++;
            row1--;
        }

        int horiz = 1;

        int row2 = row;
        int col2 = col + 1;

        while (col2 < SIDE && board[row2][col2] == '-') {
            horiz++;
            col2++;
        }

        row2 = row;
        col2 = col - 1;

        while (col2 >= 0 && board[row2][col2] == '-') {
            horiz++;
            col2--;
        }

        boolean canContain = false;

        for (int ship : remainShips) {
            canContain = ship <= vert || ship <= horiz || canContain;
        }

        return canContain;
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

    private static Point getPointToHit(Point p, char[][] board) {
        Point neighbour = getPointNeighbour(p, board);

        if (null == neighbour) {
            if (p.row > 0 && board[p.row - 1][p.col] == '-') {
                return new Point(p.row - 1, p.col);
            }

            if (p.row < SIDE - 1 && board[p.row + 1][p.col] == '-') {
                return new Point(p.row + 1, p.col);
            }

            if (p.col > 0 && board[p.row][p.col - 1] == '-') {
                return new Point(p.row, p.col - 1);
            }

            if (p.col < SIDE - 1 && board[p.row][p.col + 1] == '-') {
                return new Point(p.row, p.col + 1);
            }
        } else if (neighbour.row == p.row) {
            if (p.col > 0 && board[p.row][p.col - 1] == '-') {
                return new Point(p.row, p.col - 1);
            }

            if (p.col < SIDE - 1 && board[p.row][p.col + 1] == '-') {
                return new Point(p.row, p.col + 1);
            }
        } else {
            if (p.row > 0 && board[p.row - 1][p.col] == '-') {
                return new Point(p.row - 1, p.col);
            }

            if (p.row < SIDE - 1 && board[p.row + 1][p.col] == '-') {
                return new Point(p.row + 1, p.col);
            }
        }

        return null;
    }

    private static Point getPointNeighbour(Point p, char[][] board) {
        if (p.row > 0 && board[p.row - 1][p.col] == 'h') {
            return new Point(p.row - 1, p.col);
        }

        if (p.row < SIDE - 1 && board[p.row + 1][p.col] == 'h') {
            return new Point(p.row + 1, p.col);
        }

        if (p.col > 0 && board[p.row][p.col - 1] == 'h') {
            return new Point(p.row, p.col - 1);
        }

        if (p.col < SIDE - 1 && board[p.row][p.col + 1] == 'h') {
            return new Point(p.row, p.col + 1);
        }

        return null;
    }

    private static int countHitNeighbours(Point p, char[][] board) {
        int res = 0;

        if (p.row > 0 && board[p.row - 1][p.col] == 'h') {
            res++;
        }

        if (p.row < SIDE - 1 && board[p.row + 1][p.col] == 'h') {
            res++;
        }

        if (p.col > 0 && board[p.row][p.col - 1] == 'h') {
            res++;
        }

        if (p.col < SIDE - 1 && board[p.row][p.col + 1] == 'h') {
            res++;
        }

        return res;
    }
}
