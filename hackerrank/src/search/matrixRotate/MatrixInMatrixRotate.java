package search.matrixRotate;

public class MatrixInMatrixRotate {
    public static void main(String[] args) {
        int n = 20;
        int innerMatrixRow = (int)(Math.random() * (n / 2));
        int innerMatrixCol = (int)(Math.random() * (n / 2));
        int innerMatrixSide = 1 + (int)((n - 2 - Math.max(innerMatrixRow, innerMatrixCol)) * Math.random());

        System.out.println("innerMatrixRow=" + innerMatrixRow);
        System.out.println("innerMatrixCol=" + innerMatrixCol);
        System.out.println("innerMatrixSide=" + innerMatrixSide);

        int innerPointRow = innerMatrixRow + (int)(innerMatrixSide * Math.random());
        int innerPointCol = innerMatrixCol + (int)(innerMatrixSide * Math.random());

        int innerLayer = getPointLayer(innerPointRow - innerMatrixRow, innerPointCol - innerMatrixCol, innerMatrixSide);

        int[] rotate1 = rotate90Clockwise(innerPointRow - innerMatrixRow, innerPointCol - innerMatrixCol, innerMatrixSide);
        int[] rotate2 = rotate180Clockwise(innerPointRow - innerMatrixRow, innerPointCol - innerMatrixCol, innerMatrixSide);
        int[] rotate3 = rotate270Clockwise(innerPointRow - innerMatrixRow, innerPointCol - innerMatrixCol, innerMatrixSide);

        rotate1[0] += innerMatrixRow;
        rotate1[1] += innerMatrixCol;

        rotate2[0] += innerMatrixRow;
        rotate2[1] += innerMatrixCol;

        rotate3[0] += innerMatrixRow;
        rotate3[1] += innerMatrixCol;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (isPointInSquare(i, j, innerMatrixRow, innerMatrixCol, innerMatrixSide)) {
                    int innerPointLayer = getPointLayer(i - innerMatrixRow, j - innerMatrixCol, innerMatrixSide);

                    if ((innerPointRow == i && innerPointCol == j)
                            || (rotate1[0] == i && rotate1[1] == j)
                            || (rotate2[0] == i && rotate2[1] == j)
                            || (rotate3[0] == i && rotate3[1] == j)) {
                        System.out.printf("X ");
                    } else if (isPointOnContour(i, j, innerMatrixRow, innerMatrixCol, innerMatrixSide)) {
                        System.out.printf(". ");
                    } else if (innerPointLayer == innerLayer) {
                        System.out.printf("* ");
                    } else {
                        System.out.printf("_ ");
                    }
                } else {
                    System.out.printf("_ ");
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

    private static boolean isPointOnContour(int row, int col, int innerMatrixRow, int innerMatrixCol, int innerMatrixSide) {
        if (row == innerMatrixRow || row == innerMatrixRow + innerMatrixSide - 1 || col == innerMatrixCol || col == innerMatrixCol + innerMatrixSide - 1) {
            return isPointInSquare(row, col, innerMatrixRow, innerMatrixCol, innerMatrixSide);
        }

        return false;
    }

    private static boolean isPointInSquare(int row, int col, int innerMatrixRow, int innerMatrixCol, int innerMatrixSide) {
        return col >= innerMatrixCol && col <= innerMatrixCol + innerMatrixSide - 1 && row >= innerMatrixRow && row <= innerMatrixRow + innerMatrixSide - 1;
    }

    private static int getPointLayer(int row, int col, int n) {
        return Math.min(Math.min(row, col), Math.min(n - row - 1, n - col - 1));
    }
}
