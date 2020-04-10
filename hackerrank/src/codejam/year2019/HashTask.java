package codejam.year2019;

import java.util.HashSet;
import java.util.Set;

public class HashTask {
    private static final long MOD = 0x7fffffffffffffffl;
    //private static final long MOD = 0xffffffffffffffffl;

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

    public static void main(String[] args) {
        Set<Long> hashes1 = new HashSet<>();
        for (long i = 0; i <= 100; i++) {
            for (long j = 0; j <= 100; j++) {
                for (long k = 0; k <= 100; k++) {
                    long pow4 = fastPow(2l, 56);
                    long pow5 = fastPow(2l, 44);
                    long pow6 = fastPow(2l, 37);
                    long hash = pow4 * i + pow5 * j + pow6 * k;
                    hashes1.add(hash);
                }
            }
        }
        System.out.println(hashes1.size());

        Set<Long> hashes2 = new HashSet<>();
        for (long i = 0; i <= 100; i++) {
            for (long j = 0; j <= 100; j++) {
                for (long k = 0; k <= 100; k++) {
                    long pow1 = fastPow(2l, 55);
                    long pow2 = fastPow(2l, 27);
                    long pow3 = fastPow(2l, 18);
                    long hash = pow1 * i + pow2 * j + pow3 * k;
                    hashes2.add(hash);
                }
            }
        }
        System.out.println(hashes2.size());
    }
}
