package codejam.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class HiddenPancackes {

    private static int MAX_VAL = 100000;
    private static long MOD = 1000000000l + 7l;
    private static long[] FACTS = new long[MAX_VAL + 1];

    public static void main(String[] args) throws IOException {
        initFactorials();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int n = Integer.parseInt(br.readLine());
                int[] v = new int[n];
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                for (int i = 0; i < n; i++) {
                    v[i] = Integer.parseInt(tkn.nextToken());
                }
                long res = calculate(v, 0, n - 1, 1);
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }

    private static long calculate(int[] v, int start, int end, int min) {
        if (start > end) {
            return 0;
        }

        if (start == end) {
            return v[start] == min ? 1 : 0;
        }
        int maxPosition = -1;
        for (int i = end; maxPosition == -1 && i >= start; i--) {
            if (v[i] == min) {
                maxPosition = i;
            }
        }

        if (maxPosition == -1) {
            return 0;
        }

        if (maxPosition == start) {
            return calculate(v, start + 1, end, min + 1);
        }

        if (maxPosition == end) {
            return calculate(v, start, end - 1, min);
        }

        long leftCnt = calculate(v, start, maxPosition - 1, min);
        long rightCnt = calculate(v, maxPosition + 1, end, min + 1);

        return (((binom(maxPosition - start, end - start) * leftCnt) % MOD) * rightCnt) % MOD;
    }

    private static long binom(int k, int n) {
        return (FACTS[n] * fastPow((FACTS[k] * FACTS[n - k]) % MOD, MOD - 2)) % MOD;
    }

    private static void initFactorials() {
        FACTS[0] = 1;
        for (int i = 1; i <= MAX_VAL; i++) {
            FACTS[i] = (FACTS[i - 1] * i) % MOD;
        }
    }

    private static long fastPow(long n, long p) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return fastPow((n * n) % MOD, p / 2);
        } else {
            return (n * fastPow(n, p - 1)) % MOD;
        }
    }
}
