package ai.botbuilding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class ClickOMania {
    private static final int MAX_DESC = 1000;
    private static int rows;
    private static int cols;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line1Tkn = new StringTokenizer(br.readLine());
        rows = Integer.parseInt(line1Tkn.nextToken());
        cols = Integer.parseInt(line1Tkn.nextToken());
        int colors = Integer.parseInt(line1Tkn.nextToken());

        char[][] board = new char[rows][cols];

        for (int row = 0; row < rows; row++) {
            String line = br.readLine();
            board[row] = line.toCharArray();
        }

        int[] point = getNextPoint(board);
        System.out.println(point[0] + " " + point[1]);
    }

    private static int[] getNextPoint(char[][] board) {
        int[][] isles = new int[rows][cols];
        char[] islesColor = new char[rows * cols];
        int[] islesSize = new int[rows * cols];
        int[][] islePoint = new int[rows * cols][2];

        int islesNumber = convertToIsles(board, islesColor, isles, islePoint,islesSize);

        int minNumber = Integer.MAX_VALUE;
        List<int[]> pointsCandidates = new ArrayList<>();
        for (int isle = 0; isle < islesNumber; isle++) {
            if (islesSize[isle] > 1) {
                int newNumbers = convertToIsles(convertToColors(removeIsle(isle, isles), islesColor),
                        new char[rows * cols], new int[rows][cols], new int[rows * cols][2], new int[rows * cols]);

                if (newNumbers < minNumber) {
                    pointsCandidates.clear();
                    minNumber = newNumbers;
                    pointsCandidates.add(islePoint[isle]);
                } else if (newNumbers == minNumber) {
                    pointsCandidates.add(islePoint[isle]);
                }
            }
        }

        return pointsCandidates.get((int)(Math.random() * pointsCandidates.size()));
    }

    private static int[][] removeIsle(int color, int[][] isles) {
        int[][] islesNew = new int[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                islesNew[row][col] = isles[row][col] == color ? -1 : isles[row][col];
            }
        }

        moveIsles(islesNew);

        return islesNew;
    }

    private static void moveIsles(int[][] isles) {
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

    private static int convertToIsles(char[][] board, char[] islesColor, int[][] isles, int[][] islePoint, int[] isleSize) {
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
                    isleSize[islesNumber] = fillIsles(row, col, board, isles, islesNumber);
                    islesNumber++;
                }
            }
        }

        return islesNumber;
    }

    private static char[][] convertToColors(int[][] isles, char[] colorsMap) {
        char[][] colors = new char[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                colors[row][col] = isles[row][col] == -1 ? '-': colorsMap[isles[row][col]];
            }
        }
        return colors;
    }

    private static int fillIsles(int row, int col, char[][] board, int[][] isles, int number) {
        int size = 1;
        isles[row][col] = number;

        if (row > 0 && isles[row - 1][col] == -1 && board[row - 1][col] == board[row][col]) {
            size += fillIsles(row - 1, col, board, isles, number);
        }

        if (row < rows - 1 && isles[row + 1][col] == -1 && board[row + 1][col] == board[row][col]) {
            size += fillIsles(row + 1, col, board, isles, number);
        }

        if (col > 0 && isles[row][col - 1] == -1 && board[row][col - 1] == board[row][col]) {
            size += fillIsles(row, col - 1, board, isles, number);
        }

        if (col < cols - 1 && isles[row][col + 1] == -1 && board[row][col + 1] == board[row][col]) {
            size += fillIsles(row, col + 1, board, isles, number);
        }

        return size;
    }
}
