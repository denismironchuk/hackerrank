package codejam.final2019.findingNemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class FindingNemoSolution {
    private static int MAX_VAL = 1000000000;

    private static class Point {
        private int row;
        private int col;

        public Point(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    private static long CNT = 0;

    public static void main(String[] args) throws IOException {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer sizeTkn = new StringTokenizer(br.readLine());
                int rows = Integer.parseInt(sizeTkn.nextToken());
                int cols = Integer.parseInt(sizeTkn.nextToken());
                char[][] initialBoard = new char[rows][cols];

                /*for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        initialBoard[row][col] = '.';
                    }
                }

                initialBoard[rows / 2][cols / 2] = 'N';
                initialBoard[0][0] = 'M';*/

                for (int row = 0; row < rows; row++) {
                    initialBoard[row] = br.readLine().toCharArray();
                }

                Date start = new Date();

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
                                    CNT++;
                                    if (distsToNemoNoLoop[row][col] != MAX_VAL) {
                                        if (distsToNemoNoLoop[row][col] < distsToNemo[row][col]) {
                                            distsToNemo[row][col] = distsToNemoNoLoop[row][col];
                                        }
                                    }
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

                                for(int row = Math.max(0, rowDisp); row < Math.min(rows, rows + rowDisp); row++) {
                                    for (int col = Math.max(0, colDisp); col < Math.min(cols, cols + colDisp); col++) {
                                        CNT++;
                                        if (distsFromStartToNemo[row][col] != MAX_VAL && distsFromNemoToEnd[row - rowDisp][col - colDisp] != MAX_VAL) {
                                            int distCandidate = distsFromStartToNemo[row][col] + distsFromNemoToEnd[row - rowDisp][col - colDisp] + 1;
                                            if (distCandidate < distsToNemo[row][col]) {
                                                distsToNemo[row][col] = distCandidate;
                                            }
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
                        CNT++;
                        if (distsFromMarlin[row][col] != MAX_VAL && distsToNemo[row][col] != MAX_VAL) {
                            int candidate = distsFromMarlin[row][col] + distsToNemo[row][col];
                            if (candidate < minDist) {
                                minDist = candidate;
                            }
                        }
                    }
                }

                Date end = new Date();
                System.out.printf("Case #%s: %s\n", t, minDist == MAX_VAL ? "IMPOSSIBLE" : minDist);
                //System.out.println(end.getTime() - start.getTime() + "ms");
                //System.out.println(CNT);
            }
        }
    }

    private static int[][] countShortestPaths(char[][] board, Point nemoPos, int rowDisp, int colDisp, int[][] dists, int[][] proc) {
        int rows = board.length;
        int cols = board[0].length;

        /*int[][] dists = new int[rows][cols];
        int[][] proc = new int[rows][cols];*/

        for (int row = 0; row < rows; row++) {
            Arrays.fill(dists[row], MAX_VAL);
            Arrays.fill(proc[row], 0);
        }

        dists[nemoPos.row][nemoPos.col] = 0;
        proc[nemoPos.row][nemoPos.col] = 1;

        Queue<Point> q = new LinkedList<>();
        q.add(nemoPos);

        while (!q.isEmpty()) {
            CNT++;
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

    private static void duplicateBoard(char[][] board, char[][] duplicate) {
        int rows = board.length;
        int cols = board[0].length;

        //char[][] duplicate = new char[rows][cols];

        for (int row = 0; row < rows; row++) {
            CNT++;
            System.arraycopy(board[row], 0, duplicate[row], 0, cols);
        }

        //return duplicate;
    }

    private static void intersectBoards(char[][] board, char[][] originalBoard, int rowDisp, int colDisp,
                                            int borderRow, int borderCol, char[][] intersectionResult) {
        int rows = board.length;
        int cols = board[0].length;

        //char[][] resultBoard = new char[rows][cols];

        for (int row = Math.max(0, borderRow); row < Math.min(rows, rows + borderRow); row++) {
            for (int col = Math.max(0, borderCol); col < Math.min(cols, cols + borderCol); col++) {
                CNT++;
                intersectionResult[row][col] = mergeTwoBoardPositions(originalBoard[row][col], board[row - rowDisp][col - colDisp]);
            }
        }

        //return resultBoard;
    }

    private static char mergeTwoBoardPositions(char pos1, char pos2) {
        if (pos1 == '#' || pos2 == '#') {
            return '#';
        } else {
            return '.';
        }
    }
}
