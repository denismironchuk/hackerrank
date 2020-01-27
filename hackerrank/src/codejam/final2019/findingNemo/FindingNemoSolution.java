package codejam.final2019.findingNemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class FindingNemoSolution {
    private static class Point {
        private int row;
        private int col;

        public Point(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    public static void main(String[] args) throws IOException {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer sizeTkn = new StringTokenizer(br.readLine());
                int rows = Integer.parseInt(sizeTkn.nextToken());
                int cols = Integer.parseInt(sizeTkn.nextToken());
                char[][] initialBoard = new char[rows][cols];
                for (int row = 0; row < rows; row++) {
                    initialBoard[row] = br.readLine().toCharArray();
                }

                Point nemo = null;
                Point marlin = null;

                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        if (initialBoard[row][col] == 'N') {
                            nemo = new Point(row, col);
                        } else if (initialBoard[row][col] == 'M') {
                            marlin = new Point(row, col);
                        }
                    }
                }

                for (int rowDisp = -nemo.row; nemo.row + rowDisp < rows; rowDisp++) {
                    for (int colDisp = -nemo.col; nemo.col + colDisp < cols; colDisp++) {
                        char[][] currentBoard = duplicateBoard(initialBoard);

                        if (rowDisp == 0 && colDisp == 0) {

                        } else {
                            int rowDisp_ = rowDisp;
                            int colDisp_ = colDisp;

                            while (nemo.row + rowDisp_ < rows && nemo.row + rowDisp >= 0 && nemo.col + colDisp_ < cols && nemo.col + colDisp_ >= 0) {
                                Point newNemoPos = new Point(nemo.row + rowDisp_, nemo.col + colDisp_);

                                currentBoard = intersectBoards(currentBoard, initialBoard, rowDisp_, colDisp_);
                                rowDisp_ += rowDisp;
                                colDisp_ += colDisp;
                            }
                        }

                    }
                }
            }
        }
    }

    private static char[][] duplicateBoard(char[][] board) {
        int rows = board.length;
        int cols = board[0].length;

        char[][] duplicate = new char[rows][cols];

        for (int row = 0; row < rows; row++) {
            System.arraycopy(board[row], 0, duplicate[row], 0, cols);
        }

        return duplicate;
    }

    private static char[][] intersectBoards(char[][] board, char[][] originalBoard, int rowDisp, int colDisp) {
        int rows = board.length;
        int cols = board[0].length;

        char[][] resultBoard = new char[rows][cols];

        for (int row = Math.max(0, -rowDisp); row < Math.min(rows, rows - rowDisp); row++) {
            for (int col = Math.max(0, -colDisp); col < Math.min(cols, cols - colDisp); col++) {
                resultBoard[row][col] = mergeTwoBoardPositions(board[row][col], originalBoard[row - rowDisp][col - colDisp]);
            }
        }

        return resultBoard;
    }

    private static char mergeTwoBoardPositions(char pos1, char pos2) {
        if (pos1 == '#' || pos2 == '#') {
            return '#';
        } else {
            return '.';
        }
    }
}
