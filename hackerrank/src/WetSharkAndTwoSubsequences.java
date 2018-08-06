import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.StringTokenizer;

public class WetSharkAndTwoSubsequences {
    public static long MOD = 1000000007;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line1 = br.readLine();
        StringTokenizer line1Tkn = new StringTokenizer(line1, " ");
        int m = Integer.parseInt(line1Tkn.nextToken());
        int r = Integer.parseInt(line1Tkn.nextToken());
        int s = Integer.parseInt(line1Tkn.nextToken());

        String line2 = br.readLine();
        StringTokenizer line2Tkn = new StringTokenizer(line2, " ");
        int[] x = new int[m];
        for (int i = 0; i < m; i++) {
            x[i] = Integer.parseInt(line2Tkn.nextToken());
        }

        if (r < s || (r + s) % 2 != 0 || (r - s) % 2 != 0) {
            System.out.println(0);
        } else {
            int a = (r + s) / 2;
            int b = (r - s) / 2;

            long[][] dynA = new long[a+1][m + 1];
            dynA[0][0] = 1;
            long[][] dynB = new long[b+1][m + 1];
            dynB[0][0] = 1;

            for (int i = 0; i < m; i++) {
                for (int j = a; j >= x[i]; j--) {
                    for (int k = 1; k <= m; k++) {
                        dynA[j][k] = (dynA[j][k] + dynA[j - x[i]][k-1]) % MOD;
                    }
                }

                for (int j = b; j >= x[i]; j--) {
                    for (int k = 1; k <= m; k++) {
                        dynB[j][k] = (dynB[j][k] + dynB[j - x[i]][k-1]) % MOD;
                    }
                }
            }

            long res = 0;

            for (int k = 1; k <= m; k++) {
                res = (res + (dynA[a][k] * dynB[b][k]) % MOD) % MOD;
            }

            System.out.println(res);
        }
    }
}
