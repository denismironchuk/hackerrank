package ai.botbuilding.clickomania;

import java.util.Arrays;

public class GameResult {
    private int[] point;
    private char[][] board;
    private int result;

    public GameResult(int[] point, char[][] board, int result) {
        this.point = point;
        this.board = board;
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public int[] getPoint() {
        return point;
    }

    public char[][] getBoard() {
        return board;
    }

    private int hash = 0;

    @Override
    public int hashCode() {
        if (hash == 0) {
            for (char[] row : board) {
                hash = 31 * hash + Arrays.hashCode(row);
            }
        }
        return hash;
    }
}
