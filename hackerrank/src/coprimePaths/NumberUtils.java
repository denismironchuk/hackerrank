package coprimePaths;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denis_Mironchuk on 10/26/2018.
 */
public class NumberUtils {
    public static int factor(int n, int[] res) {
        int size = 0;

        int n_ = n;
        for (int i = 2; i * i <= n; i++) {
            if (n_ % i == 0) {
                res[size] = i;
                size++;
            }
            while (n_ % i == 0) {
                n_ /= i;
            }
        }

        if (n_ != 1) {
            res[size] = n_;
            size++;
        }

        return size;
    }

    public static int factor(int n, int[] res, int[] primes) {
        int size = 0;

        for (int prime : primes) {
            if (n % prime == 0) {
                res[size] = prime;
                size++;
            }

            while (n % prime == 0) {
                n /= prime;
            }

            if (n == 1) {
                break;
            }
        }

        if (n != 1) {
            res[size] = n;
            size++;
        }

        return size;
    }

    public static List<Integer> factorWithPrimes(int n, int[] primes) {
        List<Integer> factor = new ArrayList<>();

        for (int prime : primes) {
            if (n % prime == 0) {
                factor.add(prime);
            }

            while (n % prime == 0) {
                n /= prime;
            }

            if (n == 1) {
                break;
            }
        }

        if (n != 1) {
            factor.add(n);
        }

        return factor;
    }
}
