package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class FibonacchiFindings {
    private static final long MOD = 1000000007;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            long a = Long.parseLong(tkn.nextToken());
            long b = Long.parseLong(tkn.nextToken());
            long n = Long.parseLong(tkn.nextToken());


            long[][] mult = fastPow(new long[][]{{1, 1},{1, 0}}, n - 1);
            long[][] data = mull(new long[][]{{b, a}}, mult);

            System.out.println(data[0][0]);
        }
    }

    private static long[][] fastPow(long[][] matr, long pow) {
        if (pow == 0) {
            return unity(matr.length);
        }

        if (pow % 2 == 0) {
            return fastPow(mull(matr, matr), pow / 2);
        } else {
            return mull(matr, fastPow(matr, pow - 1));
        }
    }

    private static long[][] unity(int n) {
        long[][] res = new long[n][n];

        for (int i = 0; i < n; i++) {
            res[i][i] = 1;
        }

        return res;
    }

    private static long[][] mull(long[][] m1, long[][] m2) {
        long[][] res = new long[m1.length][m2[0].length];

        for (int row1 = 0; row1 < m1.length; row1++) {
            for (int col2 = 0; col2 < m2[0].length; col2++) {
                for (int col1 = 0; col1 < m1[0].length; col1++) {
                    res[row1][col2] += (m1[row1][col1] * m2[col1][col2]) % MOD;
                    res[row1][col2] %= MOD;
                }
            }
        }

        return res;
    }
}
