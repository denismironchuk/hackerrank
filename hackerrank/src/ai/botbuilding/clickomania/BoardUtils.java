package ai.botbuilding.clickomania;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Denis_Mironchuk on 3/29/2019.
 */
public class BoardUtils {
    public static int convertToIsles(char[][] board, char[] islesColor, int[][] isles, int[][] islePoint,
                                     List<int[]> sizes, int rows, int cols) {
        int islesNumber = 0;

        for (int row = 0; row < rows; row++) {
            Arrays.fill(isles[row], -1);
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (board[row][col] != '-' && isles[row][col] == -1) {
                    islePoint[islesNumber][0] = row;
                    islePoint[islesNumber][1] = col;
                    islesColor[islesNumber] = board[row][col];
                    int size = fillIsles(row, col, board, isles, islesNumber, rows, cols);

                    if (null != sizes) {
                        sizes.add(new int[]{islesNumber, size, rows - row});
                    }

                    islesNumber++;
                }
            }
        }

        return islesNumber;
    }

    public static int convertToIsles(char[][] board, char[] islesColor, int[][] isles, int[][] islePoint, int rows, int cols) {
        return convertToIsles(board, islesColor, isles, islePoint, null, rows, cols);
    }

    private static int fillIsles(int row, int col, char[][] board, int[][] isles, int number, int rows, int cols) {
        int size = 1;
        isles[row][col] = number;

        if (row > 0 && isles[row - 1][col] == -1 && board[row - 1][col] == board[row][col]) {
            size += fillIsles(row - 1, col, board, isles, number, rows, cols);
        }

        if (row < rows - 1 && isles[row + 1][col] == -1 && board[row + 1][col] == board[row][col]) {
            size += fillIsles(row + 1, col, board, isles, number, rows, cols);
        }

        if (col > 0 && isles[row][col - 1] == -1 && board[row][col - 1] == board[row][col]) {
            size += fillIsles(row, col - 1, board, isles, number, rows, cols);
        }

        if (col < cols - 1 && isles[row][col + 1] == -1 && board[row][col + 1] == board[row][col]) {
            size += fillIsles(row, col + 1, board, isles, number, rows, cols);
        }

        return size;
    }

    public static int[][] removeIsle(int color, int[][] isles, int rows, int cols) {
        int[][] islesNew = new int[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                islesNew[row][col] = isles[row][col] == color ? -1 : isles[row][col];
            }
        }

        moveIsles(islesNew, rows, cols);

        return islesNew;
    }

    private static void moveIsles(int[][] isles, int rows, int cols) {
        int emptyCols = 0;
        for (int col = 0; col < cols; col++) {
            int offset = 0;
            for (int row = rows - 1; row >= 0; row--) {
                if (isles[row][col] == -1) {
                    offset++;
                } else if (offset != 0) {
                    isles[row + offset][col] = isles[row][col];
                    isles[row][col] = -1;
                }
            }

            if (offset == rows) {
                emptyCols++;
            } else if (emptyCols != 0) {
                for (int row = 0; row < rows; row++) {
                    isles[row][col - emptyCols] = isles[row][col];
                    isles[row][col] = -1;
                }
            }
        }
    }

    public static char[][] convertToColors(int[][] isles, char[] colorsMap, int rows, int cols) {
        char[][] colors = new char[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                colors[row][col] = isles[row][col] == -1 ? '-': colorsMap[isles[row][col]];
            }
        }
        return colors;
    }

    public static char[][] makeMove(char[][] board, int[] point, int rows, int cols) {
        int[][] isles = new int[rows][cols];
        char[] islesColor = new char[rows * cols];
        int[] islesSize = new int[rows * cols];
        int[][] islePoint = new int[rows * cols][2];

        BoardUtils.convertToIsles(board, islesColor, isles, islePoint, rows, cols);
        return BoardUtils.convertToColors(BoardUtils.removeIsle(isles[point[0]][point[1]], isles, rows, cols), islesColor, rows, cols);
    }

    public static void printBoard(char[][] board, int rows) {
        for (int row = 0; row < rows; row++) {
            System.out.println(new String(board[row]));
        }
        System.out.println("=============");
    }

    public static boolean equalBoards(char[][] board1, char[][] board2, int rows, int cols) {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board1[i][j] != board2[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }
}
