package projectEuler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class Problem254Research {
    private static final int[] FACTORIALS = {1,1,2,6,24,120,720,5040,40320,362880};
    private static final BigInteger[] BIG_DIGITS = {BigInteger.valueOf(0),BigInteger.valueOf(1),BigInteger.valueOf(2),BigInteger.valueOf(3),
            BigInteger.valueOf(4),BigInteger.valueOf(5),BigInteger.valueOf(6),BigInteger.valueOf(7),BigInteger.valueOf(8),BigInteger.valueOf(9)};

    private static int[] digitsCnt(int n, int maxDiv) {
        int[] res = new int[10];
        while (n != 0) {
            res[maxDiv] = n / FACTORIALS[maxDiv];
            n %= FACTORIALS[maxDiv];
            maxDiv--;
        }
        return res;
    }

    private static BigInteger buildNumber(int[] digitsCnt) {
        BigInteger res = BigInteger.ZERO;

        for (int i = 0; i < 10; i++) {
            BigInteger bigI = BigInteger.valueOf(i);
            for (int j = 0; j < digitsCnt[i]; j++) {
                res = res.multiply(BigInteger.TEN).add(bigI);
            }
        }

        return res;
    }

    private static long factorialDigitsSum(BigInteger num) {
        long res = 0;
        while (!num.equals(BigInteger.ZERO)) {
            BigInteger[] divRem = num.divideAndRemainder(BigInteger.TEN);
            res += FACTORIALS[divRem[1].intValue()];
            num = divRem[0];
        }
        return res;
    }

    private static long digitsSum(long num) {
        long res = 0;
        while (num != 0) {
            res += num % 10;
            num /= 10;
        }
        return res;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\dmiro\\Documents\\forTest.txt"));
        int i = 1;
        while (true) {
            String inpt = br.readLine();
            if (null == inpt) {
                break;
            }
            long res = 0;
            for (char digit : inpt.toCharArray()) {
                res += FACTORIALS[digit - '0'];
            }
            long nines = res;
            for (int j = 2; j < 10; j++) {
                nines /= j;
            }
            //System.out.printf("%s - %s - %s\n", i, res, nines);
            System.out.printf("%s\n", res);
            i++;
        }
    }

    public static void main2(String[] args) throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //int n = Integer.parseInt(br.readLine());
        for (int n = 91; n < 100; n++) {
            BigInteger res = null;
            int rem = -1;
            int nines = -1;
            for (int d = 9; d < 10; d++) {
                for (int i = 0; i < FACTORIALS[d]; i++) {
                    //System.out.println(i);
                    int[] digitsCnt = digitsCnt(i, d - 1);
                    BigInteger candidate = buildNumber(digitsCnt);
                    long factDigsSum = factorialDigitsSum(candidate);
                    for (int k = 1; k <= n * 1000; k++) {
                        factDigsSum += FACTORIALS[d];
                        if (digitsSum(factDigsSum) == n) {
                            for (int j = 0; j < k; j++) {
                                candidate = candidate.multiply(BigInteger.TEN).add(BIG_DIGITS[d]);
                            }
                            if (res == null || candidate.compareTo(res) == -1) {
                                res = candidate;
                                rem = i;
                                nines = k;
                            }
                            break;
                        }
                    }
                }
            }
            System.out.printf("g(%s) = %s\nrem = %s\nnines = %s\n\n", n, res, rem, nines);
        }
    }
}
