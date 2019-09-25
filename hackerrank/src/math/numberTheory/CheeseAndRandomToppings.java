package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CheeseAndRandomToppings {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            long n = Long.parseLong(tkn.nextToken());
            long r = Long.parseLong(tkn.nextToken());
            long m = Long.parseLong(tkn.nextToken());

            List<Long> primes = factor(m);

            long[][] pairs = new long[primes.size()][2];

            for (int i = 0; i < primes.size(); i++) {
                long prime = primes.get(i);
                pairs[i][0] = prime;
                pairs[i][1] = 1;

                long mTemp = r;
                long nTemp = n;

                while (nTemp != 0) {
                    pairs[i][1] *= binom(mTemp % prime, nTemp % prime, prime);
                    pairs[i][1] %= prime;

                    mTemp /= prime;
                    nTemp /= prime;
                }
            }

            long[][] rev = new long[primes.size()][primes.size()];

            for (int i = 0; i < primes.size(); i++) {
                for (int j = 0; j < primes.size(); j++) {
                    rev[i][j] = fastPow(pairs[i][0], pairs[j][0] - 2, pairs[j][0]);
                }
            }

            long[] x = new long[primes.size()];

            for (int i = 0; i < primes.size(); i++) {
                x[i] = pairs[i][1];
                for (int j = 0; j < i; j++) {
                    x[i] -= x[j] % pairs[i][0];
                    x[i] += pairs[i][0];
                    x[i] %= pairs[i][0];

                    x[i] *= rev[j][i];
                    x[i] %= pairs[i][0];
                }
            }

            long res = 0;
            long mul = 1;

            for (int i = 0; i < primes.size(); i++) {
                res += x[i] * mul;
                mul *= pairs[i][0];
            }

            System.out.println(res);
        }
    }

    private static long binom(long m, long n, long mod) {
        if (m > n) {
            return 0;
        }

        long chisl = 1;

        for (long i = n; i > m; i--) {
            chisl *= i;
            chisl %= mod;
        }

        long znam = 1;

        for (long i = 1; i <= n - m; i++) {
            znam *= i;
            znam %= mod;
        }

        return (chisl * fastPow(znam, mod - 2, mod)) % mod;
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

    private static List<Long> factor(long n) {
        List<Long> primes = new ArrayList<>();

        for (long i = 2; i * i < n; i++) {
            if (n % i == 0) {
                primes.add(i);
                n /= i;
            }
        }

        if (n != 1) {
            primes.add(n);
        }

        return primes;
    }
}
