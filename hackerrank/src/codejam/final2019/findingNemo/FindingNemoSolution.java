package codejam.final2019.findingNemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class FindingNemoSolution {
    private static class Point {
        private int row;
        private int col;

        public Point(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    public static void main(String[] args) throws IOException {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer sizeTkn = new StringTokenizer(br.readLine());
                int rows = Integer.parseInt(sizeTkn.nextToken());
                int cols = Integer.parseInt(sizeTkn.nextToken());
                char[][] initialBoard = new char[rows][cols];
                for (int row = 0; row < rows; row++) {
                    initialBoard[row] = br.readLine().toCharArray();
                }

                Point nemo = null;
                Point marlin = null;

                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        if (initialBoard[row][col] == 'N') {
                            nemo = new Point(row, col);
                        } else if (initialBoard[row][col] == 'M') {
                            marlin = new Point(row, col);
                        }
                    }
                }

                int[][] distsToNemo = new int[rows][cols];

                for (int i = 0; i < rows; i++) {
                    Arrays.fill(distsToNemo[i], Integer.MAX_VALUE);
                }

                for (int rowDisp = -nemo.row; nemo.row + rowDisp < rows; rowDisp++) {
                    for (int colDisp = -nemo.col; nemo.col + colDisp < cols; colDisp++) {
                        char[][] currentBoard = duplicateBoard(initialBoard);

                        if (rowDisp == 0 && colDisp == 0) {
                            int[][]  distsToNemoNoLoop = countShortestPaths(currentBoard, nemo, rowDisp, colDisp);
                            for (int row = 0; row < rows; row++) {
                                for (int col = 0; col < cols; col++) {
                                    distsToNemo[row][col] = Math.min(distsToNemoNoLoop[row][col], distsToNemo[row][col]);
                                }
                            }
                        } else {
                            int rowDisp_ = rowDisp;
                            int colDisp_ = colDisp;

                            while (nemo.row + rowDisp_ < rows && nemo.row + rowDisp_ >= 0
                                    && nemo.col + colDisp_ < cols && nemo.col + colDisp_ >= 0) {

                                int prevRowDisp = rowDisp_ - rowDisp;
                                int prevColDisp = colDisp_ - colDisp;

                                Point newNemoPos = new Point(nemo.row + rowDisp_, nemo.col + colDisp_);
                                int[][]  distsFromNemoToEnd = countShortestPaths(currentBoard, newNemoPos, prevRowDisp, prevColDisp);
                                currentBoard = intersectBoards(currentBoard, initialBoard, rowDisp_, colDisp_);
                                int[][] distsFromStartToNemo = countShortestPaths(currentBoard, newNemoPos, rowDisp_, colDisp_);

                                printBoard(currentBoard);

                                for(int row = 0; row < rows; row++) {
                                    for (int col = 0; col < cols; col++) {
                                        if (row - rowDisp_ >= 0 && col - colDisp_ >= 0 && row - rowDisp_ < rows && col - colDisp_ < cols) {
                                            int distCandidate = distsFromStartToNemo[row][col] + distsFromNemoToEnd[row - rowDisp_][col - colDisp_] + 1;
                                            distsToNemo[row][col] = Math.min(distCandidate, distsToNemo[row][col]);
                                        }
                                    }
                                }

                                rowDisp_ += rowDisp;
                                colDisp_ += colDisp;
                            }
                        }
                    }
                }

                int[][]  distsFromMarlin = countShortestPaths(initialBoard, marlin, 0, 0);
                int minDist = Integer.MAX_VALUE;

                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        int candidate = distsFromMarlin[row][col] + distsToNemo[row][col];
                        if (candidate < minDist) {
                            minDist = candidate;
                        }
                    }
                }

                System.out.printf("Case #%s: %s", t, minDist);
            }
        }
    }

    private static int[][] countShortestPaths(char[][] board, Point nemoPos, int rowDisp, int colDisp) {
        int rows = board.length;
        int cols = board[0].length;

        int[][] dists = new int[rows][cols];
        int[][] proc = new int[rows][cols];

        for (int row = 0; row < rows; row++) {
            Arrays.fill(dists[row], Integer.MAX_VALUE);
            Arrays.fill(proc[row], 0);
        }

        dists[nemoPos.row][nemoPos.col] = 0;
        proc[nemoPos.row][nemoPos.col] = 1;

        Queue<Point> q = new LinkedList<>();
        q.add(nemoPos);

        while (!q.isEmpty()) {
            Point p = q.poll();

            List<Point> nextPoints = Arrays.asList(new Point(p.row, p.col + 1),
                    new Point(p.row, p.col - 1),
                    new Point(p.row + 1, p.col),
                    new Point(p.row - 1, p.col));

            for (Point nextPoint : nextPoints) {
                if (isPointInAvailbleArea(nextPoint, rowDisp, colDisp, rows, cols)
                        && proc[nextPoint.row][nextPoint.col] == 0
                        && board[nextPoint.row][nextPoint.col] != '#') {
                    dists[nextPoint.row][nextPoint.col] = dists[p.row][p.col] + 1;
                    proc[nextPoint.row][nextPoint.col] = 1;
                    q.add(nextPoint);
                }
            }
        }

        return dists;
    }

    private static boolean isPointInAvailbleArea(Point p, int rowDisp, int colDisp, int rows, int cols) {
        return p.row < Math.min(rows, rows + rowDisp) && p.row >= Math.max(0, rowDisp) &&
                p.col < Math.min(cols, cols + colDisp) && p.col >= Math.max(0, colDisp);
    }

    private static void printBoard(char[][] board) {
        int rows = board.length;
        int cols = board[0].length;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                System.out.print(board[row][col]);
            }
            System.out.println();
        }
        System.out.println("==================");
    }

    private static char[][] duplicateBoard(char[][] board) {
        int rows = board.length;
        int cols = board[0].length;

        char[][] duplicate = new char[rows][cols];

        for (int row = 0; row < rows; row++) {
            System.arraycopy(board[row], 0, duplicate[row], 0, cols);
        }

        return duplicate;
    }

    private static char[][] intersectBoards(char[][] board, char[][] originalBoard, int rowDisp, int colDisp) {
        int rows = board.length;
        int cols = board[0].length;

        char[][] resultBoard = new char[rows][cols];

        for (int row = Math.max(0, rowDisp); row < Math.min(rows, rows + rowDisp); row++) {
            for (int col = Math.max(0, colDisp); col < Math.min(cols, cols + colDisp); col++) {
                resultBoard[row][col] = mergeTwoBoardPositions(board[row][col], originalBoard[row - rowDisp][col - colDisp]);
            }
        }

        return resultBoard;
    }

    private static char mergeTwoBoardPositions(char pos1, char pos2) {
        if (pos1 == '#' || pos2 == '#') {
            return '#';
        } else {
            return '.';
        }
    }
}
