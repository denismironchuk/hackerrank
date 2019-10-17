package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class ZeroXorSubsets {

    public static final BigInteger TWO = BigInteger.valueOf(2);
    private static final long MOD = 1000000000 + 7;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        StringBuilder build = new StringBuilder();
        for(int t = 0; t < T; t++) {
            long n = Long.parseLong(br.readLine());
            long pow = (fastPow(2, n, MOD - 1) - (n % (MOD - 1)) + MOD - 1) % (MOD - 1);
            build.append(fastPow(2, pow, MOD)).append("\n");
        }
        System.out.println(build);
    }

    public static void main2(String[] args) {
        for (int n = 1; n < 13; n++) {
            int maxVal = fastPow(2, n);

            BigInteger[][] dynTable = new BigInteger[maxVal][maxVal];
            for (int i = 0; i < maxVal; i++) {
                for (int j = 0; j < maxVal; j++) {
                    dynTable[i][j] = BigInteger.valueOf(0);
                }
            }

            dynTable[0][0] = BigInteger.valueOf(2);

            for (int i = 1; i < maxVal; i++) {
                for (int j = 0; j < maxVal; j++) {
                    dynTable[i][j] = dynTable[i - 1][j];
                }

                for (int j = 0; j < maxVal; j++) {
                    dynTable[i][j] = dynTable[i][j].add(dynTable[i - 1][j ^ i]);
                }
            }

            System.out.println(n);
            System.out.println(dynTable[maxVal - 1][0]);
            System.out.printf("2^%s\n", getPow2(dynTable[maxVal - 1][0]));
            System.out.println("================");
        }
    }

    private static long getPow2(BigInteger n) {
        long res = 0;
        while (!n.equals(BigInteger.ONE)) {
            if (n.mod(TWO).equals(BigInteger.ZERO)) {
                n = n.divide(TWO);
                res++;
            } else {
                return -1;
            }
        }

        return res;
    }

    private static int fastPow(int n, int pow) {
        if (pow == 0) {
            return 1;
        }

        if (pow % 2 == 0) {
            return fastPow(n * n, pow / 2);
        } else {
            return n * fastPow(n, pow - 1);
        }
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
}
