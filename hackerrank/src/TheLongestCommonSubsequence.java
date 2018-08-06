import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class TheLongestCommonSubsequence {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn = new StringTokenizer(br.readLine(), " ");
        int n = Integer.parseInt(tkn.nextToken());
        int m = Integer.parseInt(tkn.nextToken());

        int[] a = new int[n];
        StringTokenizer aTkn = new StringTokenizer(br.readLine(), " ");

        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(aTkn.nextToken());
        }

        int[] b = new int[m];
        StringTokenizer bTkn = new StringTokenizer(br.readLine(), " ");

        for (int i = 0; i < m; i++) {
            b[i] = Integer.parseInt(bTkn.nextToken());
        }

        int[][] dyn = new int[n][m];

        dyn[0][0] = a[0] == b[0] ? 1 : 0;

        for (int i = 1; i < m ; i++) {
            if (a[0] == b[i]) {
                dyn[0][i] = 1;
            } else {
                dyn[0][i] = dyn[0][i - 1];
            }
        }

        for (int i = 1; i < n; i++) {
            if (b[0] == a[i]) {
                dyn[i][0] = 1;
            } else {
                dyn[i][0] = dyn[i - 1][0];
            }
        }

        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                if (a[i] == b[j]) {
                    dyn[i][j] = dyn[i - 1][j - 1] + 1;
                } else {
                    dyn[i][j] = Math.max(dyn[i - 1][j], dyn[i][j - 1]);
                }
            }
        }

        int row = n - 1;
        int col = m - 1;

        StringBuilder result = new StringBuilder();

        while (row >= 0 && col >= 0) {
            int len = dyn[row][col];

            while (col >= 0 && dyn[row][col] == len) {
                col--;
            }

            col++;

            while (row >= 0 && dyn[row][col] == len) {
                row--;
            }

            row++;

            result.insert(0, a[row] + " ");

            row--;
            col--;
        }

        System.out.println(result.toString());
    }
}
