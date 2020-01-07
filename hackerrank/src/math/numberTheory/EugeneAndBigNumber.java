package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class EugeneAndBigNumber {
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());

            for (int t = 0; t < T; t++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                long a = Long.parseLong(tkn.nextToken());
                long n = Long.parseLong(tkn.nextToken());
                long m = Long.parseLong(tkn.nextToken());

                long[][] mulMatr = new long[2][2];

                mulMatr[0][1] = 0;
                mulMatr[1][0] = a;
                mulMatr[1][1] = 1;

                if (a < 10) {
                    mulMatr[0][0] = 10;
                } else if (a < 100) {
                    mulMatr[0][0] = 100;
                } else if (a < 1000) {
                    mulMatr[0][0] = 1000;
                } else {
                    mulMatr[0][0] = 10000;
                }

                mulMatr = fastPow(mulMatr, n - 1, m);

                long[][] dest = new long[1][2];
                dest[0][0] = a;
                dest[0][1] = 1;

                dest = mul(dest, mulMatr, m);


                System.out.println(dest[0][0]);
            }
        }
    }

    private static long[][] mul(long[][] m1, long[][] m2, long mod) {
        long[][] res = new long[m1.length][m2[0].length];

        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m2[0].length; j++) {
                for (int k = 0; k < m1[0].length; k++) {
                    res[i][j] += (m1[i][k] * m2[k][j]) % mod;
                    res[i][j] %= mod;
                }
            }
        }

        return res;
    }

    private static long[][] fastPow(long[][] m, long p, long mod) {
        if (p == 0) {
            return new long[][] {{1, 0}, {0, 1}};
        }

        if (p % 2 == 0) {
            return fastPow(mul(m, m, mod), p / 2, mod);
        } else {
            return mul(m, fastPow(m, p - 1, mod), mod);
        }
    }
}
