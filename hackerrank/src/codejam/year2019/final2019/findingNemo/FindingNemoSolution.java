package codejam.year2019.final2019.findingNemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class FindingNemoSolution {
    private static int MAX_VAL = 1000000000;
    private static int THRESHOLD = 800000000;

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
                    Arrays.fill(distsToNemo[i], MAX_VAL);
                }

                char[][] currentBoard = new char[rows][cols];
                char[][] intersectionResult = new char[rows][cols];
                char[][] tmp;
                int[][] proc = new int[rows][cols];
                int[][] distsToNemoNoLoop = new int[rows][cols];
                int[][] distsFromNemoToEnd = new int[rows][cols];
                int[][] distsFromStartToNemo = new int[rows][cols];

                for (int rowDisp = -nemo.row; nemo.row + rowDisp < rows; rowDisp++) {
                    for (int colDisp = -nemo.col; nemo.col + colDisp < cols; colDisp++) {
                        duplicateBoard(initialBoard, currentBoard);

                        if (rowDisp == 0 && colDisp == 0) {
                            countShortestPaths(currentBoard, nemo, rowDisp, colDisp, distsToNemoNoLoop, proc);
                            for (int row = 0; row < rows; row++) {
                                for (int col = 0; col < cols; col++) {
                                    if (distsToNemoNoLoop[row][col] < distsToNemo[row][col]) {
                                        distsToNemo[row][col] = distsToNemoNoLoop[row][col];
                                    }
                                }
                            }
                        } else {

                            int rowDisp_ = rowDisp;
                            int colDisp_ = colDisp;

                            int minRow = Math.max(0, rowDisp);
                            int maxRow = Math.min(rows, rows + rowDisp);
                            int minCol = Math.max(0, colDisp);
                            int maxCol = Math.min(cols, cols + colDisp);

                            while (nemo.row + rowDisp_ < rows && nemo.row + rowDisp_ >= 0
                                    && nemo.col + colDisp_ < cols && nemo.col + colDisp_ >= 0) {

                                int prevRowDisp = rowDisp_ - rowDisp;
                                int prevColDisp = colDisp_ - colDisp;

                                Point newNemoPos = new Point(nemo.row + rowDisp_, nemo.col + colDisp_);

                                if (currentBoard[newNemoPos.row][newNemoPos.col] == '#') {
                                    break;
                                }

                                countShortestPaths(currentBoard, newNemoPos, prevRowDisp, prevColDisp, distsFromNemoToEnd, proc);
                                intersectBoards(currentBoard, initialBoard, rowDisp, colDisp, rowDisp_, colDisp_, intersectionResult);

                                tmp = currentBoard;
                                currentBoard = intersectionResult;
                                intersectionResult = tmp;

                                if (currentBoard[newNemoPos.row][newNemoPos.col] == '#') {
                                    break;
                                }

                                countShortestPaths(currentBoard, newNemoPos, rowDisp_, colDisp_, distsFromStartToNemo, proc);

                                for(int row = minRow; row < maxRow; row++) {
                                    for (int col = minCol; col < maxCol; col++) {
                                        int distCandidate = distsFromStartToNemo[row][col] + distsFromNemoToEnd[row - rowDisp][col - colDisp] + 1;
                                        if (distCandidate < distsToNemo[row][col]) {
                                            distsToNemo[row][col] = distCandidate;
                                        }
                                    }
                                }

                                rowDisp_ += rowDisp;
                                colDisp_ += colDisp;
                            }
                        }
                    }
                }

                int[][]  distsFromMarlin = new int[rows][cols];
                countShortestPaths(initialBoard, marlin, 0, 0, distsFromMarlin, proc);
                int minDist = MAX_VAL;

                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        int candidate = distsFromMarlin[row][col] + distsToNemo[row][col];
                        if (candidate < minDist) {
                            minDist = candidate;
                        }
                    }
                }
                System.out.printf("Case #%s: %s\n", t, minDist > THRESHOLD ? "IMPOSSIBLE" : minDist);
            }
        }
    }

    private static int[][] countShortestPaths(char[][] board, Point nemoPos, int rowDisp, int colDisp, int[][] dists, int[][] proc) {
        int rows = board.length;
        int cols = board[0].length;

        for (int row = 0; row < rows; row++) {
            Arrays.fill(dists[row], MAX_VAL);
            Arrays.fill(proc[row], 0);
        }

        dists[nemoPos.row][nemoPos.col] = 0;
        proc[nemoPos.row][nemoPos.col] = 1;

        Queue<Point> q = new LinkedList<>();
        q.add(nemoPos);

        int minRow = Math.max(0, rowDisp);
        int maxRow = Math.min(rows, rows + rowDisp);
        int minCol = Math.max(0, colDisp);
        int maxCol = Math.min(cols, cols + colDisp);

        while (!q.isEmpty()) {
            Point p = q.poll();

            Point p1 = new Point(p.row, p.col + 1);
            Point p2 = new Point(p.row, p.col - 1);
            Point p3 = new Point(p.row + 1, p.col);
            Point p4 = new Point(p.row - 1, p.col);

            if (isPointInAvailbleArea(p1, minRow, maxRow, minCol, maxCol)
                    && proc[p1.row][p1.col] == 0
                    && board[p1.row][p1.col] != '#') {
                dists[p1.row][p1.col] = dists[p.row][p.col] + 1;
                proc[p1.row][p1.col] = 1;
                q.add(p1);
            }

            if (isPointInAvailbleArea(p2, minRow, maxRow, minCol, maxCol)
                    && proc[p2.row][p2.col] == 0
                    && board[p2.row][p2.col] != '#') {
                dists[p2.row][p2.col] = dists[p.row][p.col] + 1;
                proc[p2.row][p2.col] = 1;
                q.add(p2);
            }

            if (isPointInAvailbleArea(p3, minRow, maxRow, minCol, maxCol)
                    && proc[p3.row][p3.col] == 0
                    && board[p3.row][p3.col] != '#') {
                dists[p3.row][p3.col] = dists[p.row][p.col] + 1;
                proc[p3.row][p3.col] = 1;
                q.add(p3);
            }

            if (isPointInAvailbleArea(p4, minRow, maxRow, minCol, maxCol)
                    && proc[p4.row][p4.col] == 0
                    && board[p4.row][p4.col] != '#') {
                dists[p4.row][p4.col] = dists[p.row][p.col] + 1;
                proc[p4.row][p4.col] = 1;
                q.add(p4);
            }
        }

        return dists;
    }

    private static boolean isPointInAvailbleArea(Point p, int minRow, int maxRow, int minCol, int maxCol) {
        return p.row < maxRow && p.row >= minRow && p.col < maxCol && p.col >= minCol;
    }

    private static void duplicateBoard(char[][] board, char[][] duplicate) {
        int rows = board.length;
        int cols = board[0].length;

        for (int row = 0; row < rows; row++) {
            System.arraycopy(board[row], 0, duplicate[row], 0, cols);
        }
    }

    private static void intersectBoards(char[][] board, char[][] originalBoard, int rowDisp, int colDisp,
                                            int borderRow, int borderCol, char[][] intersectionResult) {
        int rows = board.length;
        int cols = board[0].length;

        int minRow = Math.max(0, borderRow);
        int maxRow = Math.min(rows, rows + borderRow);
        int minCol = Math.max(0, borderCol);
        int maxCol = Math.min(cols, cols + borderCol);

        for (int row = minRow; row < maxRow; row++) {
            for (int col = minCol; col < maxCol; col++) {
                intersectionResult[row][col] = mergeTwoBoardPositions(originalBoard[row][col], board[row - rowDisp][col - colDisp]);
            }
        }
    }

    private static char mergeTwoBoardPositions(char pos1, char pos2) {
        if (pos1 == '#' || pos2 == '#') {
            return '#';
        } else {
            return '.';
        }
    }
}
