package kickstart.year2022;

import java.util.Random;

public class ZeroCount {

    public static void main(String[] args) {
        Random rnd = new Random();
        while (true) {
            int n = rnd.nextInt(100000);
            int cntOpt = n - countNoZero(n) + 1;
            int cntTriv = countZeroContainingTrivial(n);
            System.out.printf("%s %s %s\n", n, cntOpt, cntTriv);

            if (cntOpt != cntTriv) {
                System.out.printf("%s %s %s\n", n, cntOpt, cntTriv);
                throw new RuntimeException();
            }
        }
    }

    private static int[] convertDigitToArray(long val) {
        String valStr = String.valueOf(val);
        char[] digitsChar = valStr.toCharArray();
        int[] digits = new int[digitsChar.length];
        for (int i = 0; i < digits.length; i++) {
            digits[i] = digitsChar[i] - '0';
        }
        return digits;
    }

    private static int countNoZero(int n) {
        /*int digsCnt = String.valueOf(n).length();
        int res = countNoZero(n, digsCnt);*/
        int[] nDigs = convertDigitToArray(n);
        int digsCnt = nDigs.length;
        int res = countNoZero(nDigs, 0);

        for (int i = digsCnt - 1; i > 0; i--) {
            res += fastPow(9, i);
        }
        return res;
    }

    private static int countNoZero(int[] n, int position) {
        if (n[position] == 0) {
            return 0;
        }

        if (position == n.length - 1) {
            return n[position];
        }

        int res = (n[position] - 1) * fastPow(9, n.length - position - 1);
        res += countNoZero(n, position + 1);

        return res;
    }

    private static int countNoZero(int n, int digsCnt) {
        if (digsCnt == 1) {
            return n;
        }

        int firstDig = n / fastPow(10, digsCnt - 1);

        if (firstDig == 0) {
            return 0;
        }

        int res = (firstDig - 1) * fastPow(9, digsCnt - 1);
        res += countNoZero(n % fastPow(10, digsCnt - 1), digsCnt - 1);

        return res;
    }

    private static int fastPow(int n, int p) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return fastPow(n * n, p / 2);
        } else {
            return n * fastPow(n, p - 1);
        }
    }

    private static int countZeroContainingTrivial(int n) {
        int res = 0;
        for (int i = 0; i <= n; i++) {
            String iStr = String.valueOf(i);
            if (iStr.contains("0")) {
                res++;
            }
        }
        return res;
    }
}
