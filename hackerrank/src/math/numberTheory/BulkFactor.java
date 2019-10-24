package math.numberTheory;

import java.util.*;

public class BulkFactor {
    public static void main(String[] args) {
        Date start = new Date();
        int n = 1000000;
        int[] isPrime = new int[n + 1];
        List<Integer>[] primes = new List[n + 1];
        Arrays.fill(isPrime, 1);

        for (int i = 0; i <= n; i++) {
            primes[i] = new ArrayList<>();
        }

        for (int i = 2; i * i <= n; i++) {
            if (isPrime[i] == 1) {
                for (int j = i * i; j <= n; j += i) {
                    /*primes[j * i].add(i);

                    int j_ = j;
                    while (j_ % i == 0) {
                        primes[j * i].add(i);
                        j_ /= i;
                    }
                    isPrime[j * i] = 0;*/
                    primes[j].add(i);
                    isPrime[j] = 0;
                }
            }
        }

        for (int i = 2; i <= n; i++) {
            int i_ = i;
            for (int prime : primes[i]) {
                while (i_ % prime == 0) {
                    i_ /= prime;
                }
            }
            if (i_ != 1) {
                primes[i].add(i_);
            }
        }

        for (int i = 2; i <= n; i++) {
            Set<Integer> primesSet = new HashSet<>(primes[i]);
            if (primes[i].size() != primesSet.size()) {
                throw new RuntimeException("Duplicate primes");
            }

            int i_ = i;
            for (int prime : primes[i]) {
                while (i_ % prime == 0) {
                    i_ /= prime;
                }
            }

            if (i_ != 1) {
                throw new RuntimeException("Lost prime");
            }
        }

        Date end = new Date();

        System.out.println(end.getTime() - start.getTime() + "ms");
    }
}
