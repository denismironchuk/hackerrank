package codejam.datacenterDuplex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DatacenterDuplexTest {
    private static int rows = 7;
    private static int cols = 7;
    private static char[][] board = new char[rows][cols];
    /*private static char[][] board = new char[][] {
            {'B', 'B', 'B', 'B', 'B', 'B', 'B'},
            {'B', 'B', 'B', 'A', 'A', 'B', 'B'},
            {'B', 'A', 'B', 'A', 'A', 'B', 'B'},
            {'B', 'B', 'A', 'B', 'A', 'B', 'A'},
            {'B', 'B', 'B', 'B', 'A', 'A', 'A'},
            {'B', 'B', 'B', 'B', 'A', 'A', 'A'}
    };*/
    private static char[][] corridors = new char[rows - 1][cols - 1];

    public static void main(String[] args) {
        while(true) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (Math.random() > 0.5) {
                        board[i][j] = 'A';
                    } else {
                        board[i][j] = 'B';
                    }
                }
            }

            for (int i = 0; i < rows - 1; i++) {
                for (int j = 0; j < cols - 1; j++) {
                    corridors[i][j] = '.';
                }
            }

            List<int[]> coridorsPos = new ArrayList<>();

            for (int i = 0; i < rows - 1; i++) {
                for (int j = 0; j < cols - 1; j++) {
                    if (board[i][j] == board[i + 1][j + 1] && board[i][j + 1] == board[i + 1][j] && board[i][j] != board[i][j + 1]) {
                        coridorsPos.add(new int[]{i, j});
                    }
                }
            }

            long corridorsCnt = coridorsPos.size();

            int aCnt = countIslands('A');
            int bCnt = countIslands('B');

            boolean isPossibleOptimal = ((aCnt + bCnt - corridorsCnt) == 2);
            boolean isPossibleTriv = false;

            long compbinationsCnt = pow(2, corridorsCnt);

            for (int i = 0; !isPossibleTriv && i < compbinationsCnt; i++) {
                int combs = i;
                for (int[] corridor : coridorsPos) {
                    int row = corridor[0];
                    int col = corridor[1];
                    if (combs % 2 == 0) {
                        corridors[row][col] = '\\';
                    } else {
                        corridors[row][col] = '/';
                    }
                    combs /= 2;
                }

                int islandsA = countIslands('A');
                int islandsB = countIslands('B');

                //printCoridors();
                //System.out.println("=============");
                if (islandsA == 1 && islandsB == 1) {
                    System.out.println("POSSIBLE!!!!");
                    System.out.println("Corridors cnt = " + corridorsCnt);
                    printBoard();
                    printCoridors();
                    isPossibleTriv = true;
                    break;
                }
            }

            if (isPossibleOptimal != isPossibleTriv) {
                System.out.println("A islands = " + aCnt);
                System.out.println("B islands = " + bCnt);
                System.out.println("Corridors cnt = " + corridorsCnt);
                printBoard();
                printCoridors();

                throw new RuntimeException();
            }
        }
    }

    private static void printBoard() {
        for (int row = 0 ; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                System.out.printf("%s ", board[row][col]);
            }
            System.out.printf("\n");
        }
    }

    private static void printCoridors() {
        for (int row = 0 ; row < rows - 1; row++) {
            for (int col = 0; col < cols - 1; col++) {
                System.out.printf("%s ", corridors[row][col]);
            }
            System.out.printf("\n");
        }
    }

    private static long pow(long n, long pow) {
        if (pow == 0) {
            return 1;
        }

        if (pow % 2 == 0) {
            return pow(n*n, pow / 2);
        } else {
            return n * pow(n, pow - 1);
        }
    }

    private static int countIslands(char c) {
        int res = 0;
        int[][] proc = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (proc[i][j] == 0 && board[i][j] == c) {
                    res++;
                    bfs(i, j, proc);
                }
            }
        }

        return res;
    }

    private static void bfs(int startRow, int startCol, int[][] proc) {
        char c = board[startRow][startCol];

        Queue<int[]> q = new LinkedList<>();
        q.add(new int[] {startRow, startCol});

        while (!q.isEmpty()) {
            int[] pos = q.poll();
            int row = pos[0];
            int col = pos[1];

            proc[row][col] = 1;

            if (row < rows - 1 && proc[row + 1][col] == 0 && board[row + 1][col] == c) {
                q.add(new int[] {row + 1, col});
            }

            if (row > 0 && proc[row - 1][col] == 0 && board[row - 1][col] == c) {
                q.add(new int[] {row - 1, col});
            }

            if (col < cols - 1 && proc[row][col + 1] == 0 && board[row][col + 1] == c) {
                q.add(new int[] {row, col + 1});
            }

            if (col > 0 && proc[row][col - 1] == 0 && board[row][col - 1] == c) {
                q.add(new int[]{row, col - 1});
            }

            //corridors

            if (row < rows - 1 && col < cols - 1 && corridors[row][col] == '\\' && proc[row + 1][col + 1] == 0 && board[row + 1][col + 1] == c) {
                q.add(new int[]{row + 1, col + 1});
            }

            if (row > 0 && col > 0 && corridors[row - 1][col - 1] == '\\' && proc[row - 1][col - 1] == 0 && board[row - 1][col - 1] == c) {
                q.add(new int[]{row - 1, col - 1});
            }

            if (row < rows - 1 && col > 0 && corridors[row][col - 1] == '/' && proc[row + 1][col - 1] == 0 && board[row + 1][col - 1] == c) {
                q.add(new int[]{row + 1, col - 1});
            }

            if (row > 0 && col < cols - 1 && corridors[row - 1][col] == '/' && proc[row - 1][col + 1] == 0 && board[row - 1][col + 1] == c) {
                q.add(new int[]{row - 1, col + 1});
            }
        }
    }
}
