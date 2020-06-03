package projectEuler;

import java.math.BigInteger;

public class Problem254SimpleCount {
    private static final BigInteger[] BIG_DIGITS = {BigInteger.valueOf(0), BigInteger.valueOf(1), BigInteger.valueOf(2),
            BigInteger.valueOf(3), BigInteger.valueOf(4), BigInteger.valueOf(5), BigInteger.valueOf(6),
            BigInteger.valueOf(7), BigInteger.valueOf(8), BigInteger.valueOf(9)};
    private static long COUNT_START = 64;

    private static final long[] FIRST_RES = {-1, 1, 2, 120, 121, 122, 6, 7, 8, 720,
            721, 722, 48, 49, 842, 726, 727, 728, 5760, 5761,
            5762, 362910, 362911, 362912, 362904, 362905, 362906, 362880, 362881, 362882,
            362883, 362884, 362885, 362886, 362887, 362888, 362889, 362899, 725888, 367968,
            367969, 368888, 367998, 367999, 488888, 488889, 488899, 887888, 887889, 887899,
            897989, 889998, 889999, 2988989, 2988999, 3998899, 3999989, 3999999, 6899899, 7989989,
            7989999, 7999999, 9999989, 9999999, 19999999, 29999999, 39999999, 49999999, 59999999, 69999999,
            79999999, 89999999, 99999999, 199999999, 299999999, 399999999, 499999999, 599999999, 699999999, 799999999,
            899999999, 999999999, 1999999999, 2999999999l, 3999999999l, 4999999999l, 5999999999l, 6999999999l, 7999999999l, 8999999999l,
            9999999999l, 19999999999l, 29999999999l};

    public static void main(String[] args) {
        long res = 0;

        for (int i = 1; i < COUNT_START; i++) {
            long temp = FIRST_RES[i];
            for (int dig = 2; dig < 10; dig++) {
                res += (dig - 1) * (temp % dig);
                temp /= dig;
            }
            res += 9 * temp;
            System.out.println(res);
        }

        /*for (int i = 64; i <= 150; i++) {
            BigInteger sum = BigInteger.ZERO;
            BigInteger mul = BigInteger.ONE;
            int temp = i;
            while (temp > 9) {
                sum = sum.add(BIG_DIGITS[9].multiply(mul));
                temp -= 9;
                mul = mul.multiply(BigInteger.TEN);
            }
            sum = sum.add(BIG_DIGITS[temp].multiply(mul));
            //System.out.println(sum);

            for (int dig = 2; dig < 10; dig++) {
                BigInteger[] divRem = sum.divideAndRemainder(BIG_DIGITS[dig]);
                res += (dig - 1) * (divRem[1].longValue());
                sum = divRem[0];
            }
            res += 9 * sum.longValue();
            System.out.println(res);
        }
        System.out.println(res);*/
    }
}
