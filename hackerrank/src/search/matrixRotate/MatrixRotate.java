package search.matrixRotate;

public class MatrixRotate {
    public static void main(String[] args) {
        int n = 20;
        int row = (int)(n * Math.random());
        int col = (int)(n * Math.random());

        int layer = getPointLayer(row, col, n);
        int[] rotated1 = rotate270Clockwise(row, col, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((i == row && j == col) || (i == rotated1[0] && j == rotated1[1])) {
                    System.out.print("x ");
                } else {
                    if (getPointLayer(i, j, n) == layer) {
                        System.out.print(". ");
                    } else {
                        System.out.print("_ ");
                    }
                }
            }
            System.out.println();
        }
    }

    private static int[] rotate90Clockwise(int row, int col, int n) {
        return new int[]{col, n - 1 - row};
    }

    private static int[] rotate180Clockwise(int row, int col, int n) {
        return new int[]{n - 1 - row, n - 1 - col};
    }

    private static int[] rotate270Clockwise(int row, int col, int n) {
        return new int[]{n - 1 - col, row};
    }

    private static int getPointLayer(int row, int col, int n) {
        return Math.min(Math.min(row, col), Math.min(n - row - 1, n - col - 1));
    }
}
