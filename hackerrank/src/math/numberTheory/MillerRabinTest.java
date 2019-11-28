package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MillerRabinTest {
    public static void main(String[] args) throws IOException {
        //System.out.println(isPrimeMillerRabin(4));

        int goodTests = 0;
        while (true) {
            if (goodTests % 10000 == 0) {
                System.out.println(goodTests);
            }
            long n = 2 * (2 + (int)(Math.random() * 500000000)) + 1;
            //long n = 2 + (int)(Math.random() * 1000000);
            //System.out.println(n);
            boolean isPrimeMillerRabin = isPrimeMillerRabin(n);
            boolean isPrimeTrivial = isPrimeTrivial(n);

            if (isPrimeMillerRabin != isPrimeTrivial) {
                System.out.println(n);
                System.out.println("Is isPrimeMillerRabin == " + isPrimeMillerRabin);
                System.out.println("Is isPrimeTrivial == " + isPrimeTrivial);

                throw new RuntimeException("Took " + goodTests + " good tests");
            }

            goodTests++;
        }
    }

    private static boolean isPrimeTrivial(long n) {
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
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
            //System.out.println(a);
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
            return fastPow((n * n) % mod, p / 2, mod);
        } else {
            return (n * fastPow(n, p - 1, mod)) % mod;
        }
    }
}
