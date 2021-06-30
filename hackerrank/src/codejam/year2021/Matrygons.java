package codejam.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Matrygons {
    private static final int MAX_VAL = 1000000;
    private static final int[] results = new int[MAX_VAL + 1];
    private static final Set<Integer>[] divs = new Set[MAX_VAL + 1];

    public static void main(String[] args) throws IOException {
        Arrays.fill(results, -1);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int n = Integer.parseInt(br.readLine());
                System.out.printf("Case #%s: %s\n", t, countRes(n, true));
            }
        }
    }

    public static int countRes(int n, boolean first) {
        if (n == 1) {
            return 0;
        }

        if (results[n] != -1 && !first) {
            return results[n];
        }

        int maxVal = 1;
        for (Integer div : buildDivisors(n)) {
            if (div == n || (div == 2 && first)) {
                continue;
            }

            int candidate = countRes((n / div) - 1, false) + 1;
            if (candidate > maxVal) {
                maxVal = candidate;
            }
        }

        if (!first) {
            results[n] = maxVal;
        }
        return maxVal;
    }

    public static Set<Integer> buildDivisors(int n) {
        if (divs[n] != null) {
            return divs[n];
        }
        Set<Integer> divisors = new HashSet<>();
        divisors.add(n);
        int nOrig = n;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                divisors.add(i);
                n /= i;
                Set<Integer> nextDivisors = buildDivisors(n);
                for (Integer divisor : nextDivisors) {
                    divisors.add(divisor);
                    divisors.add(i * divisor);
                }
            }
        }
        divs[nOrig] = divisors;
        return divisors;
    }

    /*public static void main(String[] args) {
        int[] isPrime = new int[MAX_VAL + 1];
        int[] values = new int[MAX_VAL + 1];
        for (int i = 0; i <= MAX_VAL; i++) {
            values[i]
             = i;
            isPrime[i] = 1;
        }

        int[] divisorsCnt = new int[MAX_VAL + 1];
        Arrays.fill(divisorsCnt, 1);

        for (int i = 2; i <= MAX_VAL; i++) {
            if (isPrime[i] == 1) {
                divisorsCnt[i] = 2;
                if (1l * i * i <= MAX_VAL) {
                    for (int j = i * i; j <= MAX_VAL; j += i) {
                        isPrime[j] = 0;
                        int pow = 0;
                        while (values[j] % i == 0) {
                            values[j] /= i;
                            pow++;
                        }
                        divisorsCnt[j] *= pow + 1;
                    }
                }
            }
        }

        for (int i = 1; i <= MAX_VAL; i++) {
            if (isPrime[i] == 0 && values[i] != 1) {
                divisorsCnt[i] *= 2;
            }
            if (isPrime[values[i]] == 0) {
                throw new RuntimeException();
            }
        }

        long res = 0;

        for (int i = 2; i <= MAX_VAL; i++) {
            if (isPrime[i] == 0) {
                res += divisorsCnt[i];
            }
        }

        System.out.println(res);
    }*/
}
