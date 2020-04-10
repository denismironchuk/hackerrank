package codejam.year2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class BacterialTactics {
    private static int r;
    private static int c;
    private static int[][] vertAccm;
    private static int[][] horAccm;
    private static int[][][][] rectMex;
    private static char[][] board;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t <= T; t++) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            r = Integer.parseInt(tkn1.nextToken());
            c = Integer.parseInt(tkn1.nextToken());
            board = new char[r][c];

            for (int i = 0; i < r; i++) {
                board[i] = br.readLine().toCharArray();
            }

            vertAccm = new int[r][c];

            for (int col = 0; col < c; col++) {
                vertAccm[0][col] = board[0][col] == '#' ? 1 : 0;
                for (int row = 1; row < r; row++) {
                    vertAccm[row][col] = vertAccm[row - 1][col] + (board[row][col] == '#' ? 1 : 0);
                }
            }

            horAccm = new int[r][c];

            for (int row = 0; row < r; row++) {
                horAccm[row][0] = board[row][0] == '#' ? 1 : 0;
                for (int col = 1; col < c; col++) {
                    horAccm[row][col] = horAccm[row][col - 1] + (board[row][col] == '#' ? 1 : 0);
                }
            }

            //rows, columns, lengths, heights
            rectMex = new int[c + 1][r + 1][r][c];
            int winMovesCnt = 0;

            for (int height = 1; height <= r; height++) {
                for (int len = 1; len <= c; len++) {

                    for (int row = 0; row < r; row++) {
                        for (int col = 0; col < c; col++) {
                            if (row + height <= r && col + len <= c) {
                                winMovesCnt = countMex(row, col, len, height);
                            }
                        }
                    }
                }
            }

            System.out.printf("Case #%s: %s\n", t, winMovesCnt);
        }
    }

    private static int countMex(int row, int col, int len, int height) {
        SortedSet<Integer> mexList = new TreeSet<>();
        int winMovesCnt = 0;

        for (int c = col; c < col + len; c++) {
            int metCnt = vertAccm[row + height - 1][c];
            if (row - 1 >= 0) {
                metCnt -= vertAccm[row - 1][c];
            }
            if (metCnt == 0) {
                int mex = 0;

                if (c - col != 0) {
                    mex ^= rectMex[c - col][height][row][col];
                }
                if (len - (c - col + 1) != 0) {
                    mex ^= rectMex[len - (c - col + 1)][height][row][c + 1];
                }

                winMovesCnt += (mex == 0 ? height : 0);
                mexList.add(mex);
            }
        }

        for (int r = row; r < row + height; r++) {
            int metCnt = horAccm[r][col + len - 1];
            if (col - 1 >= 0) {
                metCnt -= horAccm[r][col - 1];
            }
            if (metCnt == 0) {
                int mex = 0;
                if (r - row != 0) {
                    mex ^= rectMex[len][r - row][row][col];
                }
                if (height - (r - row + 1) != 0) {
                    mex ^= rectMex[len][height - (r - row + 1)][r + 1][col];
                }
                winMovesCnt += (mex == 0 ? len : 0);
                mexList.add(mex);
            }
        }

        rectMex[len][height][row][col] = countMex(mexList);
        return winMovesCnt;
    }

    private static int countMex(SortedSet<Integer> mexList) {
        int res = 0;

        for (Integer m : mexList) {
            if (m == res) {
                res++;
            } else {
                break;
            }
        }

        return res;
    }
}
