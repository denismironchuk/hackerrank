package findPath;

public class FindPath2 {
    public static void main(String[] args) {
        while (true) {
            System.out.println("Iteration started");
            int rows = 7;
            int cols = 100;

            int[][] rect = new int[rows][cols];

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    rect[i][j] = (int) (10 * Math.random());
                }
            }

            long[][][] distsToLeft = calcDistsToLeft(rect, rows, cols);
            int[][] flippedRect = flipRect(rect, rows, cols);
            long[][][] distsToRight = calcDistsToLeft(flippedRect, rows, cols);
            long[][][] dists = combineDists(distsToLeft, distsToRight, rows, cols);

            if (!DistanceChecker.checkDists(rows, cols, rect, dists)) {
                throw new RuntimeException("Mismatch!!!");
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

    private static void printRectangle(int rows, int cols, int[][] rect) {
        System.out.println("Rectangle");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("%2d", rect[i][j]);
            }
            System.out.println();
        }
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
