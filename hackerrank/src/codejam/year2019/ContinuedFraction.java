package codejam.year2019;

import java.util.ArrayList;
import java.util.List;

public class ContinuedFraction {
    public static void main(String[] args) {
        long chisl = 2;//(long)(100 * Math.random());
        long znam = (long)(100 * Math.random());
        /*long chisl = 1;
        long znam = 217;*/
        long gcd = gcd(chisl, znam);
        chisl /= gcd;
        znam /= gcd;

        double val = (double)chisl/(double)znam;

        System.out.printf("%d/%d - ", chisl, znam);
        System.out.printf("%.15f\n", val);

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
            for (int s = 0; s < a; s++) {
                long semiChist = s * H[i - 1] + H[i - 2];
                long semiZnam = s * K[i - 1] + K[i - 2];
                System.out.printf("%d/%d - ", semiChist, semiZnam);
                System.out.printf("%.15f - ", (double)semiChist/(double)semiZnam);
                System.out.printf("%.15f\n", Math.abs(val - (double)semiChist/(double)semiZnam));
            }
            H[i] = a * H[i - 1] + H[i - 2];
            K[i] = a * K[i - 1] + K[i - 2];
            System.out.printf("%d/%d - ", H[i], K[i]);
            System.out.printf("%.15f - ", (double)H[i]/(double)K[i]);
            System.out.printf("%.15f\n", Math.abs(val - (double)H[i]/(double)K[i]));
            i++;
            System.out.println("============");
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
