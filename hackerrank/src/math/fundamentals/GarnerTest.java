package math.fundamentals;

import java.util.*;

public class GarnerTest {
    private static final int PRIME_LIM = 50;
    private static final int CRT_CNT = 10;

    public static void main(String[] args) {
        while (true) {
            int[] processed = new int[PRIME_LIM];
            List<Integer> primes = new ArrayList<>();

            for (int i = 2; i < PRIME_LIM; i++) {
                if (processed[i] == 0) {
                    processed[i] = 1;
                    primes.add(i);
                    for (int j = 2; i * j < PRIME_LIM; j++) {
                        processed[i * j] = 1;
                    }
                }
            }

            Set<Integer> crtPrimes = new HashSet<>();

            while (crtPrimes.size() < CRT_CNT) {
                crtPrimes.add(primes.get((int) (Math.random() * primes.size())));
            }

        /*
        0 - prime
        1 - rem
         */
            int[][] pairs = new int[CRT_CNT][2];

            Iterator<Integer> crtPrimesItr = crtPrimes.iterator();

            for (int i = 0; i < CRT_CNT; i++) {
                int prime = crtPrimesItr.next();
                pairs[i][0] = prime;
                pairs[i][1] = (int) (Math.random() * prime);
            }

            printEquations(pairs);

            int[][] r = new int[CRT_CNT][CRT_CNT];

            for (int i = 0; i < CRT_CNT; i++) {
                for (int j = 0; j < CRT_CNT; j++) {
                    r[i][j] = fastPow(pairs[i][0], pairs[j][0] - 2, pairs[j][0]);
                }
            }

            int[] n = new int[CRT_CNT];

            for (int i = 0; i < CRT_CNT; i++) {
                n[i] = pairs[i][1];
                for (int j = 0; j < i; j++) {
                    n[i] -= n[j] % pairs[i][0];
                    n[i] += pairs[i][0];
                    n[i] %= pairs[i][0];

                    n[i] *= r[j][i];
                    n[i] %= pairs[i][0];
                }
            }

            long res = 0;
            long mul = 1;

            for (int i = 0; i < CRT_CNT; i++) {
                res += n[i] * mul;
                mul *= pairs[i][0];
            }

            System.out.println(res);

            if (res >= mul) {
                throw new RuntimeException();
            }

            for (int i = 0; i < CRT_CNT; i++) {
                if (res % pairs[i][0] != pairs[i][1]) {
                    throw new RuntimeException();
                }
            }
        }
    }

    private static void printEquations(int[][] pairs) {
        for (int[] eq : pairs) {
            System.out.printf("%s = x mod %s\n", eq[1], eq[0]);
        }
    }

    private static int fastPow(int n, int p, int mod) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return fastPow((n * n) % mod, p / 2, mod);
        } else {
            return (n * fastPow(n, p - 1, mod)) % mod;
        }
    }
}
