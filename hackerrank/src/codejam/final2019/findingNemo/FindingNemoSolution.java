package codejam.final2019.findingNemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class FindingNemoSolution {
    private static class Point {
        private int row;
        private int col;
        private String direction;

        public Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public Point(int row, int col, String direction) {
            this.row = row;
            this.col = col;
            this.direction = direction;
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
                List[][] commandsToNemo = new List[rows][cols];

                for (int i = 0; i < rows; i++) {
                    Arrays.fill(distsToNemo[i], Integer.MAX_VALUE);
                }

                for (int rowDisp = -nemo.row; nemo.row + rowDisp < rows; rowDisp++) {
                    for (int colDisp = -nemo.col; nemo.col + colDisp < cols; colDisp++) {
                        char[][] currentBoard = duplicateBoard(initialBoard);

                        if (rowDisp == 0 && colDisp == 0) {
                            List[][] commandsToNoLoop = new List[rows][cols];
                            int[][]  distsToNemoNoLoop = countShortestPaths(currentBoard, nemo, rowDisp, colDisp, commandsToNoLoop, true);
                            for (int row = 0; row < rows; row++) {
                                for (int col = 0; col < cols; col++) {
                                    if (distsToNemoNoLoop[row][col] != Integer.MAX_VALUE) {
                                        if (distsToNemoNoLoop[row][col] < distsToNemo[row][col]) {
                                            distsToNemo[row][col] = distsToNemoNoLoop[row][col];
                                            commandsToNemo[row][col] = commandsToNoLoop[row][col];
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

                                List[][] commandsFromNemoToEnd = new List[rows][cols];
                                int[][]  distsFromNemoToEnd = countShortestPaths(currentBoard, newNemoPos, prevRowDisp, prevColDisp, commandsFromNemoToEnd, false);
                                currentBoard = intersectBoards(currentBoard, initialBoard, rowDisp, colDisp, rowDisp_, colDisp_);
                                //printBoard(currentBoard);
                                List[][] commandsFromStartToNemo = new List[rows][cols];
                                int[][] distsFromStartToNemo = countShortestPaths(currentBoard, newNemoPos, rowDisp_, colDisp_, commandsFromStartToNemo, true);

                                for(int row = 0; row < rows; row++) {
                                    for (int col = 0; col < cols; col++) {
                                        if (row - rowDisp >= 0 && col - colDisp >= 0 && row - rowDisp < rows && col - colDisp < cols) {
                                            if (distsFromStartToNemo[row][col] != Integer.MAX_VALUE && distsFromNemoToEnd[row - rowDisp][col - colDisp] != Integer.MAX_VALUE) {
                                                int distCandidate = distsFromStartToNemo[row][col] + distsFromNemoToEnd[row - rowDisp][col - colDisp] + 1;
                                                if (distCandidate < distsToNemo[row][col]) {
                                                    distsToNemo[row][col] = distCandidate;
                                                    commandsToNemo[row][col] = new ArrayList();
                                                    commandsToNemo[row][col].addAll(commandsFromStartToNemo[row][col]);
                                                    commandsToNemo[row][col].addAll(commandsFromNemoToEnd[row - rowDisp][col - colDisp]);
                                                    commandsToNemo[row][col].add("goto");
                                                }
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

                List[][] commandsFromMarlin = new List[rows][cols];
                int[][]  distsFromMarlin = countShortestPaths(initialBoard, marlin, 0, 0, commandsFromMarlin, false);
                int minDist = Integer.MAX_VALUE;
                List<String> commands = null;

                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        if (distsFromMarlin[row][col] != Integer.MAX_VALUE && distsToNemo[row][col] != Integer.MAX_VALUE) {
                            int candidate = distsFromMarlin[row][col] + distsToNemo[row][col];
                            if (candidate < minDist) {
                                minDist = candidate;
                                if (commandsToNemo[row][col] != null && commandsToNemo[row][col].size() != 0
                                        && commandsToNemo[row][col].get(commandsToNemo[row][col].size() - 1).equals("goto")) {
                                    String gotoStep = "goto " + (commandsFromMarlin[row][col].size() + 1);
                                    commandsToNemo[row][col].set(commandsToNemo[row][col].size() - 1, gotoStep);
                                }

                                commands = commandsFromMarlin[row][col];
                                commands.addAll(commandsToNemo[row][col]);

                            }
                        }
                    }
                }

                System.out.printf("Case #%s: %s\n", t, minDist == Integer.MAX_VALUE ? "IMPOSSIBLE" : minDist);
                commands.forEach(System.out::println);
            }
        }
    }

    private static int[][] countShortestPaths(char[][] board, Point nemoPos, int rowDisp, int colDisp, List[][] commands, boolean invert) {
        int rows = board.length;
        int cols = board[0].length;

        int[][] dists = new int[rows][cols];
        int[][] proc = new int[rows][cols];

        for (int row = 0; row < rows; row++) {
            Arrays.fill(dists[row], Integer.MAX_VALUE);
            Arrays.fill(proc[row], 0);
            for (int col = 0; col < cols; col++) {
                commands[row][col] = new ArrayList();
            }
        }

        dists[nemoPos.row][nemoPos.col] = 0;
        proc[nemoPos.row][nemoPos.col] = 1;

        Queue<Point> q = new LinkedList<>();
        q.add(nemoPos);

        while (!q.isEmpty()) {
            Point p = q.poll();

            List<Point> nextPoints = Arrays.asList(new Point(p.row, p.col + 1, invert ? "W" : "E"),
                    new Point(p.row, p.col - 1, invert ? "E" : "W"),
                    new Point(p.row + 1, p.col, invert ? "N" : "S"),
                    new Point(p.row - 1, p.col, invert ? "S" : "N"));

            for (Point nextPoint : nextPoints) {
                if (isPointInAvailbleArea(nextPoint, rowDisp, colDisp, rows, cols)
                        && proc[nextPoint.row][nextPoint.col] == 0
                        && board[nextPoint.row][nextPoint.col] != '#') {
                    dists[nextPoint.row][nextPoint.col] = dists[p.row][p.col] + 1;
                    proc[nextPoint.row][nextPoint.col] = 1;
                    q.add(nextPoint);
                    if (invert) {
                        commands[nextPoint.row][nextPoint.col].add(nextPoint.direction);
                        commands[nextPoint.row][nextPoint.col].addAll(commands[p.row][p.col]);
                    } else {
                        commands[nextPoint.row][nextPoint.col].addAll(commands[p.row][p.col]);
                        commands[nextPoint.row][nextPoint.col].add(nextPoint.direction);
                    }
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

    private static char[][] intersectBoards(char[][] board, char[][] originalBoard, int rowDisp, int colDisp, int borderRow, int borderCol) {
        int rows = board.length;
        int cols = board[0].length;

        char[][] resultBoard = new char[rows][cols];

        for (int row = Math.max(0, borderRow); row < Math.min(rows, rows + borderRow); row++) {
            for (int col = Math.max(0, borderCol); col < Math.min(cols, cols + borderCol); col++) {
                resultBoard[row][col] = mergeTwoBoardPositions(originalBoard[row][col], board[row - rowDisp][col - colDisp]);
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
