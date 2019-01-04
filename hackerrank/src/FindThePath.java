import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class FindThePath {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int rows = Integer.parseInt(tkn1.nextToken());
        int cols = Integer.parseInt(tkn1.nextToken());

        int[][] rect = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            StringTokenizer rowTkn = new StringTokenizer(br.readLine());
            for (int j = 0; j < cols; j++) {
                rect[i][j] = Integer.parseInt(rowTkn.nextToken());
            }
        }

        long[][][] distsToLeft = calcDistsToLeft(rect, rows, cols);
        int[][] flippedRect = flipRect(rect, rows, cols);
        long[][][] distsToRight = calcDistsToLeft(flippedRect, rows, cols);
        long[][][] dists = combineDists(distsToLeft, distsToRight, rows, cols);

        StringBuilder result = new StringBuilder();

        if (cols == 1) {
            int Q = Integer.parseInt(br.readLine());
            for (int q = 0; q < Q; q++) {
                StringTokenizer qTkn = new StringTokenizer(br.readLine());
                int r1 = Integer.parseInt(qTkn.nextToken());
                int c1 = Integer.parseInt(qTkn.nextToken());

                int r2 = Integer.parseInt(qTkn.nextToken());
                int c2 = Integer.parseInt(qTkn.nextToken());

                result.append(dists[0][r1][r2] + rect[r1][0]).append("\n");
            }
        } else {
            long[][][] tree = buildSegmentTree(rows, cols, dists, rect);
            int Q = Integer.parseInt(br.readLine());
            for (int q = 0; q < Q; q++) {
                StringTokenizer qTkn = new StringTokenizer(br.readLine());
                int r1 = Integer.parseInt(qTkn.nextToken());
                int c1 = Integer.parseInt(qTkn.nextToken());

                int r2 = Integer.parseInt(qTkn.nextToken());
                int c2 = Integer.parseInt(qTkn.nextToken());

                if (c1 > c2) {
                    int temp = c1;
                    c1 = c2;
                    c2 = temp;

                    temp = r2;
                    r2 = r1;
                    r1 = temp;
                }

                if (c1 == c2) {
                    result.append(dists[c1][r1][r2] + rect[r1][c1]).append("\n");
                } else {
                    long[][] columnDist = countColumnsShortestsDists(c1, c2, 0, cols - 1, tree, 1, rows);
                    result.append(columnDist[r1][r2] + rect[r1][c1]).append("\n");
                }
            }
        }

        System.out.println(result.toString());
    }

    private static long[][] countColumnsShortestsDists(int col1, int col2, int leftLim, int rightLim, long[][][] tree, int v, int rows) {
        if (col2 <= col1) {
            return null;
        }

        long[][] res;

        if (col1 == leftLim && col2 == rightLim) {
            res = tree[v];
        } else {
            int middle = (leftLim + rightLim) / 2;
            long[][] dists1 = countColumnsShortestsDists(col1, Math.min(col2, middle), leftLim, middle, tree, 2 * v, rows);
            long[][] dists2 = countColumnsShortestsDists(Math.max(middle, col1), Math.max(col2, middle), middle, rightLim, tree, 2 * v + 1, rows);

            if (dists1 == null) {
                res = dists2;
            } else if (dists2 == null) {
                res = dists1;
            } else {
                long[][] dist = new long[rows][rows];

                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < rows; j++) {
                        dist[i][j] = Integer.MAX_VALUE;
                    }
                }

                for (int row1 = 0; row1 < rows; row1++) {
                    for (int row2 = 0; row2 < rows; row2++) {
                        for (int k = 0; k < rows; k++) {
                            long newDist = dists1[row1][k] + dists2[k][row2];
                            dist[row1][row2] = Math.min(dist[row1][row2], newDist);
                        }
                    }
                }

                res = dist;
            }
        }

        return res;
    }

    private static long[][][] buildSegmentTree(int rows, int cols, long[][][] dists, int[][] rect) {
        long[][][] tree = new long[cols * 4][rows][rows];
        for (int i = 0; i < cols * 4; i++) {
            for (int j = 0; j < rows; j++) {
                for (int k = 0; k < rows; k++) {
                    tree[i][j][k] = Integer.MAX_VALUE;
                }
            }
        }

        treeProcess(rows, dists, rect, tree, 0, cols - 1, 1);

        return tree;
    }

    private static void treeProcess(int rows, long[][][] dists, int[][] rect, long[][][] tree, int colStart, int colEnd, int v) {
        if (colStart + 1 == colEnd) {
            for (int row1 = 0; row1 < rows; row1++) {
                for (int row2 = 0; row2 < rows; row2++) {
                    for (int k = 0; k < rows; k++) {
                        long newDist = dists[colStart][row1][k] + rect[k][colEnd] + dists[colEnd][k][row2];
                        tree[v][row1][row2] = Math.min(tree[v][row1][row2], newDist);
                    }
                }
            }
        } else {
            int middle = (colStart + colEnd) / 2;
            treeProcess(rows, dists, rect, tree, colStart, middle, 2 * v);
            treeProcess(rows, dists, rect, tree, middle, colEnd, (2 * v) + 1);

            long[][] dst1 = tree[2 * v];
            long[][] dst2 = tree[(2 * v) + 1];

            for (int row1 = 0; row1 < rows; row1++) {
                for (int row2 = 0; row2 < rows; row2++) {
                    for (int k = 0; k < rows; k++) {
                        long newDist = dst1[row1][k] + dst2[k][row2];
                        tree[v][row1][row2] = Math.min(tree[v][row1][row2], newDist);
                    }
                }
            }
        }
    }

    private static long[][][] combineDists(long[][][] distsToLeft, long[][][] distsToRight, int rows, int cols) {
        long[][][] dists = new long[cols][rows][rows];

        for (int col = 0; col < cols; col++) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < rows; j++) {
                    dists[col][i][j] = Math.min(distsToLeft[col][i][j], distsToRight[cols - 1 - col][i][j]);
                }
            }
            floydWarshal(dists[col]);
        }

        return dists;
    }

    private static int[][] flipRect(int[][] rect, int rows, int cols) {
        int[][] flippedRect = new int[rows][cols];
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                flippedRect[row][cols - 1 - col] = rect[row][col];
            }
        }

        return flippedRect;
    }

    private static long[][][] calcDistsToLeft(int[][] rect, int rows, int cols) {
        long[][][] distsToLeft = new long[cols][rows * 2][rows * 2];

        for (int col = 0; col < cols; col++) {
            for (int i = 0; i < rows * 2; i++) {
                for (int j = 0; j < rows * 2; j++) {
                    if (i != j) {
                        distsToLeft[col][i][j] = Integer.MAX_VALUE;
                    }
                }
            }
        }

        for (int i = 0; i < rows - 1; i++) {
            distsToLeft[0][i][i + 1] = rect[i + 1][0];
            distsToLeft[0][i + 1][i] = rect[i][0];
        }

        floydWarshal(distsToLeft[0]);

        for (int col = 1; col < cols; col++) {
            for (int i = 0; i < rows - 1; i++) {
                distsToLeft[col][i][i + 1] = rect[i + 1][col];
                distsToLeft[col][i + 1][i] = rect[i][col];
            }

            for (int i = rows; i < rows * 2; i++) {
                for (int j = rows; j < rows * 2; j++) {
                    distsToLeft[col][i][j] = distsToLeft[col - 1][i - rows][j - rows];
                }
            }

            for (int i = 0; i < rows; i++) {
                distsToLeft[col][i][i + rows] = rect[i][col - 1];
                distsToLeft[col][i + rows][i] = rect[i][col];
            }

            floydWarshal(distsToLeft[col]);
        }

        return distsToLeft;
    }

    private static void floydWarshal(long[][] dists) {
        int side = dists.length;

        for (int k = 0; k < side; k++) {
            for (int i = 0; i < side; i++) {
                for (int j = 0; j < side; j++) {
                    if (dists[i][k] + dists[k][j] < dists[i][j]) {
                        dists[i][j] = dists[i][k] + dists[k][j];
                    }
                }
            }
        }
    }
}
