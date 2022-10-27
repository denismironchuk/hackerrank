package codejam.year2022.round2;

public class PixelatedCircle {

    static int R = 9;
    static int[][] boardFull = new int[2 * R + 1][2 * R + 1];
    static int[][] boardGaps = new int[2 * R + 1][2 * R + 1];

    public static void main(String[] args) {
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
            boardGaps[x + R][-y + R] = 1;
            boardGaps[y + R][x + R] = 1;
            boardGaps[-y + R][x + R] = 1;
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
