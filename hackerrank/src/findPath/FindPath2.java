package findPath;

import java.util.Date;

public class FindPath2 {
    public static void main(String[] args) {
        while(true) {
            int rows = 7;
            int cols = 10;

            int[][] rect = new int[rows][cols];

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    rect[i][j] = (int) (10 * Math.random());
                }
            }

            Date start = new Date();
            long[][][] distsToLeft = calcDistsToLeft(rect, rows, cols);
            int[][] flippedRect = flipRect(rect, rows, cols);
            long[][][] distsToRight = calcDistsToLeft(flippedRect, rows, cols);
            long[][][] dists = combineDists(distsToLeft, distsToRight, rows, cols);

            long[][][] tree = buildSegmentTree(rows, cols, dists, rect);
            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + "ms");

            if (!DistanceChecker.checkSegmentTreeDists(rows, cols, rect, tree)) {
                throw new RuntimeException("!!!!!!!!!!!!");
            }
        }
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
