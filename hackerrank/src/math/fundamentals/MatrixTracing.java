package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MatrixTracing {
    private static final long MOD = 1000000007;

    private static long fastPow(long a, long p) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return fastPow((a * a) % MOD, p / 2);
        } else {
            return (a * fastPow(a, p - 1)) % MOD;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            long m = Integer.parseInt(tkn.nextToken());
            long n = Integer.parseInt(tkn.nextToken());

            long chisl = 1;

            for (long i = n + m - 2; i >= m; i--) {
                chisl *= i;
                chisl %= MOD;
            }

            long znam = 1;

            for (long i = 1; i < n; i++) {
                znam *= i;
                znam %= MOD;
            }

            long res = (chisl * fastPow(znam, MOD - 2)) % MOD;

            System.out.println(res);
        }
    }
}
