package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class PrimitiveProblem {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        long p = Long.parseLong(br.readLine());

        long phiP = p - 1;
        List<Long> primes = primesFactor(phiP);

        boolean primeRootFound = false;
        long d = 2;

        while (!primeRootFound) {
            boolean has1Pow = false;
            for (long prime : primes) {
                long pow = phiP / prime;
                if (fastPow(d, pow, p) == 1) {
                    has1Pow = true;
                    break;
                }
            }

            if (!has1Pow) {
                primeRootFound = true;
            } else {
                d++;
            }
        }

        System.out.printf("%s %s\n", d, phi(phiP));
    }

    private static long phi(long n) {
        long res = n;

        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                res -= res / i;

                while (n % i == 0) {
                    n /= i;
                }
            }
        }

        if (n != 1) {
            res -= res / n;
        }

        return res;
    }

    private static long fastPow(long n, long pow, long mod) {
        if (pow == 0) {
            return 1;
        }

        if (pow % 2 == 0) {
            return fastPow((n * n) % mod, pow / 2, mod);
        } else {
            return (n * fastPow(n, pow - 1, mod)) % mod;
        }
    }

    private static List<Long> primesFactor(long p) {
        List<Long> res = new ArrayList<>();

        for (long i = 2; i * i <= p; i++) {
            if (p % i == 0) {
                res.add(i);
            }

            while (p % i == 0) {
                p /= i;
            }
        }

        if (p != 1) {
            res.add(p);
        }

        return res;
    }
}
