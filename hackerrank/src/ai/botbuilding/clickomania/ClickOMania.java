package ai.botbuilding.clickomania;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;

public class ClickOMania {
    public static final int DEPTH_LIMIT = 20;
    public static int MAX_DESC = 1400;
    public static int MAX_ISLE_TO_PROCESS = 5;

    public static final int SIZE_FACT = 7;
    public static final int HEIGHT_FACT = 1;

    private static int rows;
    private static int cols;

    private static char[][] generateBoard(int rows, int cols, int colours) {
        char[][] board = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = (char)('A' + (int)(Math.random() * colours));
            }
        }

        return board;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("D:\\clickomania1.txt"));
        StringTokenizer line1Tkn = new StringTokenizer(br.readLine());
        rows = Integer.parseInt(line1Tkn.nextToken());
        cols = Integer.parseInt(line1Tkn.nextToken());

        char[][] board = new char[rows][cols];

        for (int row = 0; row < rows; row++) {
            String line = br.readLine();
            board[row] = line.toCharArray();
        }

        //while (true) {
            /*rows = 20;
            cols = 10;
            int colours = 6;
            char[][] board = generateBoard(rows, cols, colours);*/

            char[][] nextState = board;

            while (true) {
                board = nextState;
                BoardUtils.printBoard(board, rows);
                NextMoveGenerator nextMoveGen = new NextMoveGenerator(board, rows, cols);

                Date start = new Date();
                int[] move = nextMoveGen.generateNextMove();
                Date end = new Date();
                System.out.println(end.getTime() - start.getTime() + "ms");

                if (null == move) {
                    break;
                }
                nextState = BoardUtils.makeMove(board, move, rows, cols);
            }

            //BoardUtils.printBoard(board, rows);
        }
    //}
}
