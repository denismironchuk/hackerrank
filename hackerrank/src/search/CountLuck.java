package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class CountLuck {
    private static int n;
    private static int m;
    private static char[][] map;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        for (int t = 0; t < T; t++) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            n = Integer.parseInt(tkn1.nextToken());
            m = Integer.parseInt(tkn1.nextToken());
            map = new char[n][m];

            int startRow = -1;
            int startCol = -1;

            for (int i = 0; i < n; i++) {
                String line = br.readLine();
                map[i] = line.toCharArray();
                for (int j = 0; j < m; j++) {
                    if (map[i][j] == 'M') {
                        startRow = i;
                        startCol = j;
                    }
                }
            }

            int l = Integer.parseInt(br.readLine());
            int actualForks = deepSearch(startRow, startCol, new int[n][m])[1];
            System.out.println(l == actualForks ? "Impressed" : "Oops!");
        }
    }

    private static int[] deepSearch(int row, int col, int[][] processed) {
        processed[row][col] = 1;
        if (map[row][col] == '*') {
            return new int[] {1,0};
        }

        int localForks = 0;
        int globalForks = 0;
        int hasGoalPath = 0;

        if (row > 0 && processed[row - 1][col] == 0 && map[row - 1][col] != 'X') {
            localForks++;
            int[] res = deepSearch(row - 1, col, processed);
            hasGoalPath += res[0];
            globalForks += res[1];
        }

        if (col > 0 && processed[row][col -1] == 0 && map[row][col - 1] != 'X') {
            localForks++;
            int[] res = deepSearch(row, col - 1, processed);
            hasGoalPath += res[0];
            globalForks += res[1];
        }

        if (row < n - 1 && processed[row + 1][col] == 0 && map[row + 1][col] != 'X') {
            localForks++;
            int[] res = deepSearch(row + 1, col, processed);
            hasGoalPath += res[0];
            globalForks += res[1];
        }

        if (col < m - 1 && processed[row][col +1] == 0 && map[row][col + 1] != 'X') {
            localForks++;
            int[] res = deepSearch(row, col + 1, processed);
            hasGoalPath += res[0];
            globalForks += res[1];
        }

        return hasGoalPath > 0 ? new int[] {1, globalForks + (localForks > 1 ? 1 : 0)} : new int[] {0, 0};
    }
}
