package codejam;

import java.util.ArrayList;
import java.util.List;

public class ContinuedFraction {
    public static void main(String[] args) {
        long chisl = (long)(Long.MAX_VALUE * Math.random());
        long znam = (long)(Long.MAX_VALUE * Math.random());
        long gcd = gcd(chisl, znam);
        chisl /= gcd;
        znam /= gcd;

        System.out.printf("%d/%d\n", chisl, znam);

        List<Long> A = new ArrayList<>();
        while (znam != 1) {
            A.add(chisl/znam);
            long ost = chisl % znam;
            chisl = znam;
            znam = ost;
        }

        A.add(chisl);

        System.out.println(A);

        long[] H = new long[A.size() + 2];
        H[0] = 0;
        H[1] = 1;
        long[] K = new long[A.size() + 2];
        K[0] = 1;
        K[1] = 0;

        int i = 2;
        for (long a : A) {
            H[i] = a * H[i - 1] + H[i - 2];
            K[i] = a * K[i - 1] + K[i - 2];
            System.out.printf("%d/%d\n", H[i], K[i]);
            i++;
        }
    }

    private static long gcd(long a, long b) {
        return a >= b ? gcdInternal(a, b) : gcdInternal(b, a);
    }

    private static long gcdInternal(long a, long b) {
        long ost = a % b;
        if (ost == 0) {
            return b;
        } else {
            return gcdInternal(b, ost);
        }
    }
}
