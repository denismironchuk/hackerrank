package codejam.year2019;

public class DrapNuirCalc {
    private static final long MOD = fastPow(2, 63);

    private static long fastPow(long val, int pow) {
        if (pow == 0) {
            return 1;
        }

        if (pow % 2 == 0) {
            return fastPow((val * val), pow / 2);
        } else {
            return (val * fastPow(val, pow - 1));
        }
    }

    private static long fastPowMod(long val, int pow) {
        if (pow == 0) {
            return 1;
        }

        if (pow % 2 == 0) {
            return fastPow((val * val) % MOD, pow / 2) % MOD;
        } else {
            return (val * fastPow(val, pow - 1)) % MOD;
        }
    }

    public static void main(String[] args) {
        long x1 = 45;
        long x2 = 65;
        long x3 = 26;
        long x4 = 84;
        long x5 = 33;
        long x6 = 7;

        int day = 55;

        int pow1 = day;
        int pow2 = (day / 2);
        int pow3 = (day / 3);
        int pow4 = (day / 4);
        int pow5 = (day / 5);
        int pow6 = (day / 6);

        long xd1 = fastPowMod(2, pow1);
        long xd2 = fastPowMod(2, pow2);
        long xd3 = fastPowMod(2, pow3);
        long xd4 = fastPowMod(2, pow4);
        long xd5 = fastPowMod(2, pow5);
        long xd6 = fastPowMod(2, pow6);

        long res = ((x1 * xd1) + (x2 * xd2) + (x3 * xd3) + (x4 * xd4) + (x5 * xd5) + (x6 * xd6)) % MOD;
        System.out.println(res);
        System.out.println(Long.toBinaryString(res));
        //224 - 6053419403398086656
        //55 - 1621295874585105920
    }
}
