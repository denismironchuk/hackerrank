package projectEuler;

import java.math.BigInteger;

public class Problem254Research2 {
    private static final BigInteger[] BIG_DIGITS = {BigInteger.valueOf(0), BigInteger.valueOf(1), BigInteger.valueOf(2),
            BigInteger.valueOf(3), BigInteger.valueOf(4), BigInteger.valueOf(5), BigInteger.valueOf(6),
            BigInteger.valueOf(7), BigInteger.valueOf(8), BigInteger.valueOf(9)};

    public static void main(String[] args) {
        BigInteger n = BigInteger.valueOf(19999999);
        BigInteger tempSum = BigInteger.ZERO;
        for (int i = 0; i < 100; i++) {
            BigInteger tmp = new BigInteger(n.toString());
            for (int dig = 2; dig < 10; dig++) {
                BigInteger[] divRem = tmp.divideAndRemainder(BIG_DIGITS[dig]);
                System.out.printf("%s ", divRem[1]);
                tmp = divRem[0];
            }
            System.out.printf("- %s - %s\n", tmp, n);
            //tempSum = tempSum.add(tmp);
            //System.out.println(tmp);
            //System.out.println(tempSum);

            n = n.multiply(BigInteger.TEN).add(BIG_DIGITS[9]);
        }
    }
}
