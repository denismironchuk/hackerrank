package kickstart.year2021;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PrimesAndQueriesTest {

    public static void main(String[] args) {
        int n = 10000;

        List<Long> primes = new ArrayList<>();
        int[] isNotPrime = new int[n];
        for (int i = 2; i < n; i++) {
            if (isNotPrime[i] == 0) {
                primes.add((long)i);
                for (int j = i * i; j < n; j += i) {
                    isNotPrime[j] = 1;
                }
            }
        }
        while (true) {
            long p = primes.get((int) (Math.random() * (primes.size() - 1)));
            System.out.println("p = " + p);
            long maxA = 1000;
            long a = 1 + (long) (maxA * Math.random());
            System.out.println("a = " + a);
            int maxPow = 6;
            int s = 1 + (int) (maxPow * Math.random());
            System.out.println("s = " + s);

            long val = fastPow(a, s) - fastPow(a % p, s);
            long resTrivial = val == 0 ? 0 : getPrimePow(val, p);
            System.out.println("==========");

            long resOpt = 0;

            if (a % p == 0) {
                long pow = getPrimePow(a, p);
                resOpt = pow * s;
            } else {
                if (p == 2 && s % 2 == 0) {
                    if (a != 1) {
                        long pow = 0;
                        while (s % 4 == 0) {
                            pow++;
                            s /= 2;
                        }

                        BigInteger bigA = BigInteger.valueOf(a);
                        bigA = bigA.multiply(bigA).subtract(BigInteger.ONE);
                        pow += bigA.getLowestSetBit();

                        resOpt = pow;
                    }
                } else {
                    long divisionRes = a / p;
                    long pow = 0;
                    if (divisionRes != 0) {
                        pow = 1 + getPrimePow(divisionRes, p);
                        pow += getPrimePow(s, p);
                    }
                    resOpt = pow;
                }
            }

            if (resTrivial != resOpt) {
                System.out.println(resTrivial + " != " + resOpt);
                throw new RuntimeException("!!!!!!!!!");
            }
        }
    }

    private static long getPrimePow(long a, long p) {
        if (a < p) {
            return 0;
        }

        long pow = 0;
        while (a % p == 0) {
            pow++;
            a /= p;
        }
        return pow;
    }

    private static long fastPow(long a, int p) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return fastPow(a * a, p / 2);
        } else {
            return a * fastPow(a, p - 1);
        }
    }
}
