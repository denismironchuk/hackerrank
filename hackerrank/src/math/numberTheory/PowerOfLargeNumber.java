package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.StringTokenizer;

public class PowerOfLargeNumber {
    public static final long MOD = 1000000000 + 7;
    public static  final BigInteger MOD_BIG = BigInteger.valueOf(MOD);
    private static final BigInteger TWO = BigInteger.valueOf(2);

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            BigInteger a = new BigInteger(tkn.nextToken());
            BigInteger b = new BigInteger(tkn.nextToken());

            long aLong = a.mod(MOD_BIG).longValue();
            long bLong = b.mod(MOD_BIG.subtract(BigInteger.ONE)).longValue();

            System.out.println(fastPowNoRecursion(aLong, bLong));
        }
    }

    private static long fastPow(long n, BigInteger p) {
        if (p.equals(BigInteger.ZERO)) {
            return 1;
        }

        if (p.mod(TWO).equals(BigInteger.ZERO)) {
            return fastPow((n * n) % MOD, p.divide(TWO));
        } else {
            return (n * fastPow(n, p.subtract(BigInteger.ONE))) % MOD;
        }
    }

    private static long fastPowNoRecursion(long n, long p) {
        long res = 1;
        long mul = n;

        while (p != 0) {
            if (p % 2 == 1) {
                res *= mul;
                res %= MOD;
            }
            mul *= mul;
            mul %= MOD;
            p /= 2;
        }

        return res;
    }
}
