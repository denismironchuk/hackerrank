package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoprimeConundrum {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        long n = Long.parseLong(br.readLine());

        System.out.println(findCoprimePairsOpt(n));

        /*for (long n = 1; n < 100000; n++) {
            if (n % 1000 == 0) {
                System.out.println(n);
            }
            long resOpt = findCoprimePairsOpt(n);
            long resTriv = coprimePairsTriv(n);
            if (resOpt != resTriv) {
                throw new RuntimeException("fsdfsdfsdf");
            }*/
    }

    private static long coprimePairsTriv(long n) {
        long res = 0;
        for (int i = 2; i * i <= n; i++) {
            for (int j = i + 1; j * i <= n; j++) {
                if (gcd(i, j) == 1) {
                    res++;
                }
            }
        }
        return res;
    }

    private static long gcd(long a, long b) {
        return a >= b ? gcdInner(a, b) : gcdInner(b, a);
    }

    private static long gcdInner(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return gcdInner(b, a % b);
        }
    }

    private static long findCoprimePairsOpt(long n ) {
        int lm = (int)Math.sqrt(n) + 1;
        int[] isPrime = new int[lm + 1];
        List<Integer>[] factors = new List[lm + 1];
        for (int i = 0; i <= lm; i++) {
            factors[i] = new ArrayList<>();
        }

        Arrays.fill(isPrime, 1);

        for (int i = 2; i <= lm; i++) {
            if (isPrime[i] == 1) {
                factors[i].add(i);
                for (int j = 2; j * i <= lm; j++) {
                    isPrime[j * i] = 0;
                    factors[i * j].add(i);
                }
            }
        }

        long res = 0;

        for (int i = 2; i <= lm; i++) {
            long start = i + 1;
            long end = n / (long)i;
            long len = end - start + 1;

            if (end < start) {
                continue;
            }

            List<Integer>[] combinations = findPrimesCobinations(factors[i]);

            long coPrime = len;
            int mult = -1;

            for (int j = 0; j < combinations.length; j++) {
                List<Integer> combs = combinations[j];

                for (Integer comb : combs) {
                    coPrime += mult * (len / comb);
                }
                mult *= -1;
            }

            res += coPrime;
        }

        return res;
    }

    private static List<Integer>[] findPrimesCobinations(List<Integer> factors) {
        List<Integer>[] allcombs = new List[factors.size()];

        for (int i = 0; i < factors.size(); i++) {
            allcombs[i] = new ArrayList<>();
        }

        for (int i = 0; i < factors.size(); i++) {
            int prime = factors.get(i);
            for (int j = i - 1; j >= 0; j--) {
                for (Integer comb : allcombs[j]) {
                    allcombs[j + 1].add(comb * prime);
                }
            }
            allcombs[0].add(prime);
        }

        return allcombs;
    }
}
