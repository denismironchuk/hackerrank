package search;

public class MatrixRotate {
    public static void main(String[] args) {
        int n = 10;
        int row = (int)(n * Math.random());
        int col = (int)(n * Math.random());

        int layer = getPointLayer(row, col, n);
        int[] rotated1 = rotate90ConterClockwise(row, col, layer, n);

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

        System.out.println(layer);

        if (row == layer) {
            System.out.println("Up");
        } else if (col == n - layer - 1) {
            System.out.println("Right");
        } else if (row == n - layer - 1) {
            System.out.println("Down");
        } else {
            System.out.println("Left");
        }


    }

    private static int[] rotate90Clockwise(int row, int col, int layer, int n) {
        if (row == layer) {
            //Up
            int rightLimit = n - layer - 1;
            return new int[]{row + (col - layer), rightLimit};
        } else if (col == n - layer - 1) {
            //Right
            int downLimit = n - layer - 1;
            return new int[]{downLimit, col - (row - layer)};
        } else if (row == n - layer - 1) {
            //Down
            int rightLimit = n - layer - 1;
            return new int[]{row - (rightLimit - col), layer};
        } else {
            //Left
            int downLimit = n - layer - 1;
            return new int[]{layer, col + (downLimit - row)};
        }
    }

    private static int[] rotate90ConterClockwise(int row, int col, int layer, int n) {
        if (row == layer) {
            //Up
            int rightLimit = n - layer - 1;
            return new int[]{row + (rightLimit - col), layer};
        } else if (col == n - layer - 1) {
            //Right
            int rightLimit = n - layer - 1;
            return new int[]{layer, rightLimit - (row - layer)};
        } else if (row == n - layer - 1) {
            //Down
            int rightLimit = n - layer - 1;
            return new int[]{row - (col - layer), rightLimit};
        } else {
            //Left
            int downLimit = n - layer - 1;
            return new int[]{downLimit, row};
        }
    }

    private static int getPointLayer(int row, int col, int n) {
        return Math.min(Math.min(row, col), Math.min(n - row - 1, n - col - 1));
    }
}
