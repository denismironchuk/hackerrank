package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SherlockAndPermutations {
    private static final long MOD = 1000000007;

    private static long fastPow(long a, long pow) {
        if (pow == 0) {
            return 1;
        }

        if (pow % 2 == 0) {
            return fastPow((a * a) % MOD, pow / 2);
        } else {
            return (a * fastPow(a, pow - 1)) % MOD;
        }
    }

    private static long fact(long a) {
        if (a == 0) {
            return 1;
        }

        return (a * fact(a - 1)) % MOD;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            long n = Integer.parseInt(tkn.nextToken());
            long m = Integer.parseInt(tkn.nextToken());

            long res = (fact(n + m - 1) * fastPow((fact(n) * fact(m - 1)) % MOD, MOD - 2)) % MOD;
            //long res = fact(n + m - 1)/ (fact(n - 1) * fact(m));

            System.out.println(res);
        }
    }
}
