package codejam.year2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrimeTime {
    private static final int MAX_PRIME = 500;

    public static void main(String[] args) {
        int[] isPrime = new int[MAX_PRIME + 1];
        Arrays.fill(isPrime, 1);
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= MAX_PRIME; i++) {
            if (isPrime[i] == 1) {
                primes.add(i);
                for (int j = 2; i * j <= MAX_PRIME; j++) {
                    isPrime[i * j] = 0;
                }
            }
        }

        long count = fastPow(10, 15);
        long[] counts = new long[MAX_PRIME];
        long primesCnt = primes.size();
        long maxSum = 0;
        for (int prime : primes) {
            counts[prime] = count / primesCnt;
            maxSum += prime * counts[prime];
        }

        System.out.println(maxSum);
        buildSum(maxSum, 1, 0, primes, counts);
        //buildSum(35, 1, 0, Arrays.asList(2,3,5,7,11), new long[] {0,0,2,1,0,2,0,1,0,0,0,1});

        double res = 1;
        for (int prime: primes) {
            res *= Math.log(maxSum) / Math.log(prime);
            //System.out.println(Math.log(maxSum) / Math.log(prime));
        }
        System.out.println(res);
    }

    private static void buildSum(long sum, long mul, int primePos, List<Integer> primes, long[] primesCnt) {
        //System.out.println(primePos);
        if (primePos == primes.size()) {
            return;
        }

        int prime = primes.get(primePos);

        /*if (sum == mul) {
            System.out.println(mul);
            return;
        }*/
        //buildSum(sum, mul, primePos + 1, primes, primesCnt);

        long originalCnt = primesCnt[prime];
        long newMul = mul;
        long newSum = sum;
        for (int cnt = 1; cnt <= originalCnt; cnt++) {
            //System.out.println(primePos + " " + cnt);
            newSum = newSum - prime;
            newMul = newMul * prime;
            if (newMul == newSum) {
                System.out.println(newMul);
            } else if (newMul > newSum) {
                break;
            } else {
                buildSum(newSum, newMul, primePos + 1, primes, primesCnt);
            }
        }
    }

    private static long fastPow(long n, long p) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return fastPow(n * n, p / 2);
        } else {
            return n * fastPow(n, p - 1);
        }
    }
}
