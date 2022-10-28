package codejam.year2022.round2;

public class PixelatedCircle {

    static int R = 4;
    static int[][] boardFull = new int[2 * R + 1][2 * R + 1];
    static int[][] boardGaps = new int[2 * R + 1][2 * R + 1];

    public static void main(String[] args) {
        for (int x = 1; x <= R; x++) {
            System.out.printf("%s - ", x);
            double xD = x;
            for (int r = x; r <= R; r++) {
                double rD = r;
                System.out.printf("%s ", Math.round(Math.sqrt(rD * rD - xD * xD)));
            }
            double rD = R;
            int maxHeight = (int)Math.round(Math.sqrt(rD * rD - xD * xD));
            double maxHeightD = maxHeight;
            System.out.println();
            //System.out.println(Math.round(Math.sqrt(maxHeight * maxHeight + xD * xD)));
        }

        for (int r = 0; r <= R; r++) {
            draw_circle_perimeter(r);
        }
        printBoard(boardGaps);
    }

    private static void draw_circle_perimeter(int radius) {
        for (int x = -radius; x <= radius; x++) {
            double fRadius = radius;
            double fX = x;
            int y = (int) Math.round(Math.sqrt(fRadius * fRadius - fX * fX));
            boardGaps[x + R][y + R] = 1;
            //boardGaps[x + R][-y + R] = 1;
            //boardGaps[y + R][x + R] = 1;
            //boardGaps[-y + R][x + R] = 1;
        }
    }

    private static void printBoard(int[][] board) {
        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[j][i] == 0 ? ". " : "* ");
                //System.out.print(board[j][i]);
            }
            System.out.println();
        }
    }
}
