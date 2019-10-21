package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Equations {
    private static final long MOD = 1000007;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        int[] isPrime = new int[n + 1];
        Arrays.fill(isPrime, 1);
        for (int i = 2; i * i <= n; i++) {
            if (isPrime[i] == 1) {
                for (int j = i * i; j <= n; j += i) {
                    isPrime[j] = 0;
                }
            }
        }

        List<Long> primes = new ArrayList<>();

        for (int i = 2; i <= n; i++) {
            if (isPrime[i] == 1) {
                primes.add(Long.valueOf(i));
            }
        }

        int primesCnt = primes.size();

        long[] powers = new long[primesCnt];
        for (int i = 0; i < primesCnt; i++) {
            long prime = primes.get(i);
            long divide = prime;
            long cnt = n / divide;
            while (cnt != 0) {
                powers[i] += cnt;
                divide *= prime;
                cnt = n / divide;
            }
        }

        for (int i = 0; i < primesCnt; i++) {
            powers[i] *= 2;
        }

        long res = powers[0];

        for (int i = 1; i < primesCnt; i++) {
            res = ((res + 1) * (powers[i] + 1) - 1 + MOD) % MOD;
        }

        System.out.println(res + 1);
    }
}
