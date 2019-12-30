package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class MegaPrimeNumbers2 {
    private static final int[] PRIME_DIGITS = new int[]     {0,0,1,1,0,1,0,1,0,0};
    private static final long[] NOT_PRIME_MODIFY = new long[] {2,1,1,2,1,2,1,-5,-6,-7};

    public static void main(String[] args) throws IOException {
        Date start = new Date();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());

            long first = Long.parseLong(tkn.nextToken());
            long last = Long.parseLong(tkn.nextToken());

            List<Long> megaprimeCandidates = new ArrayList<>();
            long prevMeg = getBiggerClosestMegaprime(first);
            while (prevMeg <= last) {
                megaprimeCandidates.add(prevMeg);
                long nextMeg = getNextMegaprime(prevMeg);
                prevMeg = nextMeg;
            }

            int intervalLen = (int) (Math.sqrt(last) + 1);
            int[] isNotPrime = new int[intervalLen + 1];

            for (int i = 2; i * i <= intervalLen; i++) {
                if (isNotPrime[i] == 0) {
                    for (int j = i * i; j <= intervalLen; j += i) {
                        isNotPrime[j] = 1;
                    }
                }
            }

            List<Integer> primes = new ArrayList<>();
            for (int i = 2; i < intervalLen; i++) {
                if (isNotPrime[i] == 0) {
                    primes.add(i);
                }
            }

            long res = 0;
           for (Long meg : megaprimeCandidates) {
                boolean isPrime = true;

                for (int i = 0; isPrime && i < 1000 && i < primes.size(); i++) {
                    long prime = primes.get(i);
                    if (meg == prime) {
                        break;
                    }
                    isPrime = (meg % prime) != 0;
                }

                if (isPrime && isPrimeMillerRabin(meg)) {
                    res++;
                }
            }

            System.out.println(res);
        }
        Date end = new Date();

        System.out.println(end.getTime() - start.getTime() + "ms");
    }

    private static boolean isPrimeMillerRabin(long n) {
        long d = n - 1;
        while (d % 2 == 0) {
            d /= 2;
        }

        int k = 4;
        boolean isPrime = true;

        for (int i = 0; isPrime && i < k; i++) {
            long a = 1 + (long) (Math.random() * (n - 2));
            long pow = fastPow(a, d, n);

            if (pow != 1 && pow != n - 1) {
                boolean notPrime = true;
                long d_ = d;
                while (notPrime && 2 * d_ < n - 1) {
                    pow = fastPow(pow, 2, n);
                    d_ *= 2;
                    if (pow == n - 1) {
                        notPrime = false;
                    } else if (pow == 1) {
                        break;
                    }
                }
                isPrime = !notPrime;
            }
        }

        return isPrime;
    }

    private static long fastPow(long n, long p, long mod) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return fastPow(fastMul(n, n, mod), p / 2, mod);
        } else {
            return fastMul(n, fastPow(n, p - 1, mod), mod);
            //return (n * fastPow(n, p - 1, mod)) % mod;
        }
    }

    private static long fastMul(long n, long p, long mod) {
        if (p == 0) {
            return 0;
        }

        if (p % 2 == 0) {
            return fastMul((n + n) % mod, p / 2, mod);
        } else {
            return (n + fastMul(n, p - 1, mod)) % mod;
        }
    }

    private static long getNextMegaprime(long n) {
        long res = n;
        long n_ = n;
        long mul = 1;

        while (n_ != 0) {
            int digit = (int)(n_ % 10);
            res += mul * NOT_PRIME_MODIFY[digit];

            if (NOT_PRIME_MODIFY[digit] > 0) {
                return res;
            }

            mul *= 10;
            n_ /= 10;
        }

        if (res < n) {
            res += mul * 2;
        }

        return res;
    }

    private static long getBiggerClosestMegaprime(long n) {
        long n_ = n;
        long res = n;
        long mul = 1;
        long lastNotPrimePos = -1;

        while (n_ != 0) {
            int digit = (int) (n_ % 10);

            if (PRIME_DIGITS[digit] == 0) {
                lastNotPrimePos = mul;
            }

            mul *= 10;
            n_ /= 10;
        }

        if (lastNotPrimePos == -1) {
            return res;
        }

        n_ = n;
        int m = 10;
        while (m <= lastNotPrimePos) {
            int digit = (int) (n_ % 10);
            res -= (digit - 2) * (m / 10);

            m *= 10;
            n_ /= 10;
        }

        boolean perenos = true;
        n_ = n / lastNotPrimePos;
        while (perenos && n_ != 0) {
            int lastNotPrimeDigit = (int) (n_ % 10);
            res += NOT_PRIME_MODIFY[lastNotPrimeDigit] * lastNotPrimePos;
            perenos = NOT_PRIME_MODIFY[lastNotPrimeDigit] < 0;
            lastNotPrimePos *= 10;
            n_ /= 10;
        }

        if (perenos) {
            res += 2 * lastNotPrimePos;
        }

        return res;
    }
}
