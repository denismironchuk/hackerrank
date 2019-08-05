package codejam;

import java.util.ArrayList;
import java.util.List;

public class RationalsIntervalTest {
    private static final int MAX_VAL = 50000;
    private static final Rational ONE = new Rational(1,1);

    private static class Rational implements Comparable<Rational> {
        private int chisl;
        private int znam;

        public Rational(int chisl, int znam) {
            this(chisl, znam, true);
        }

        public Rational(int chisl, int znam, boolean gcdCount) {
            int gcd = 1;
            if (gcdCount) {
                gcd = gcd(chisl, znam);
            }

            this.chisl = chisl / gcd;
            this.znam = znam / gcd;
        }

        public float getFloatValue() {
            return (float)chisl/(float)znam;
        }

        public int getIntVal(int mult) {
            return chisl * mult / znam;
        }

        private static int gcd(int a, int b) {
            return a >= b ? gcdInternal(a, b) : gcdInternal(b, a);
        }

        private static int gcdInternal(int a, int b) {
            int ost = a % b;
            if (ost == 0) {
                return b;
            } else {
                return gcdInternal(b, ost);
            }
        }

        @Override
        public int compareTo(Rational r) {
            long r1 = (long)chisl * (long)r.znam;
            long r2 = (long)r.chisl * (long)znam;
            return Long.compare(r1, r2);
        }

        @Override
        public String toString() {
            return String.format("%d/%d (%.3f)", chisl, znam, getFloatValue());
        }
    }

    private static Rational[] generateInterval() {
        Rational r1 = new Rational(1, 1);
        Rational r2 = new Rational(1, 1);

        while (r1.compareTo(r2) == 0 || r1.chisl >= r1.znam || r2.chisl >= r2.znam) {
            r1 = new Rational(1 + (int) (MAX_VAL * Math.random()), 1 + (int) (MAX_VAL * Math.random()));
            r2 = new Rational(1 + (int) (MAX_VAL * Math.random()), 1 + (int) (MAX_VAL * Math.random()));
        }

        if (r1.compareTo(r2) == 1) {
            Rational temp = r1;
            r1 = r2;
            r2 = temp;
        }

        return new Rational[]{r1, r2};
    }

    public static void main(String[] args) {
        while(true) {
            Rational[] intervalBorders = generateInterval();
            Rational r1 = intervalBorders[0];
            Rational r2 = intervalBorders[1];

            //System.out.printf("%s %s\n", r1, r2);

            Rational res = getInnerTrivial(r1, r2);
            int smallestZnam = getSmallestZnamOptimal(r1, r2);
            int smallestChisl = 1 + (r1.chisl * smallestZnam) / r1.znam;
            Rational resOpt = new Rational(smallestChisl, smallestZnam);

            System.out.println(resOpt);

            if (res.compareTo(resOpt) != 0) {
                throw new RuntimeException("!!!!!!!!");
            }

        }
    }

    private static Rational getInnerTrivial(Rational r1, Rational r2) {
        int znam = 0;

        Rational res;

        do {
            znam++;
            while (r1.getIntVal(znam) == r2.getIntVal(znam)) {
                znam++;
            }

            res = new Rational(r1.getIntVal(znam) + 1, znam);
        } while (res.compareTo(r2) == 0 || res.compareTo(r1) == 0);

        return res;
    }

    private static int getSmallestZnamOptimal(Rational r1, Rational r2) {
        int res = Integer.MAX_VALUE;

        int chisl = r1.chisl + r2.chisl;
        int znam = r1.znam + r2.znam;

        List<Integer> A = new ArrayList<>();
        while (znam > 1) {
            A.add(chisl/znam);
            int ost = chisl % znam;
            chisl = znam;
            znam = ost;
        }

        A.add(chisl);

        int[] H = new int[A.size() + 2];
        H[0] = 0;
        H[1] = 1;
        int[] K = new int[A.size() + 2];
        K[0] = 1;
        K[1] = 0;

        int i = 2;
        for (int a : A) {
            H[i] = a * H[i - 1] + H[i - 2];
            K[i] = a * K[i - 1] + K[i - 2];
            i++;
        }

        boolean foundInInterval = false;
        for (i = 2; i < A.size() + 2; i += 2) {
            Rational r = new Rational(H[i], K[i], false);
            if (r.compareTo(r1) == 1) {
                foundInInterval = true;
                break;
            }
        }

        if (foundInInterval) {
            int a = findIncreasing(r1, H[i - 1], H[i - 2], K[i - 1], K[i - 2], 0, A.get(i - 2));
            res = Math.min(res, a * K[i - 1] + K[i - 2]);
        }

        foundInInterval = false;
        for (i = 3; i < A.size() + 2; i += 2) {
            Rational r = new Rational(H[i], K[i], false);
            if (r.compareTo(r2) == -1) {
                foundInInterval = true;
                break;
            }
        }

        if (foundInInterval) {
            int a = findDecreasing(r2, H[i - 1], H[i - 2], K[i - 1], K[i - 2], 0, A.get(i - 2));
            res = Math.min(res, a * K[i - 1] + K[i - 2]);
        }

        return res;
    }

    private static int findIncreasing(Rational limit, int H1, int H2, int K1, int K2, int start, int end) {
        if (start == end) {
            return start;
        }

        int mid = (start + end) / 2;
        Rational r = new Rational(mid * H1 + H2, mid * K1 + K2, false);

        if (r.compareTo(limit) != 1) {
            return findIncreasing(limit, H1, H2, K1, K2, mid + 1, end);
        } else {
            return findIncreasing(limit, H1, H2, K1, K2, start, mid);
        }
    }

    private static int findDecreasing(Rational limit, int H1, int H2, int K1, int K2, int start, int end) {
        if (start == end) {
            return start;
        }

        int mid = (start + end) / 2;
        Rational r = new Rational(mid * H1 + H2, mid * K1 + K2, false);

        if (r.compareTo(limit) != -1) {
            return findDecreasing(limit, H1, H2, K1, K2, mid + 1, end);
        } else {
            return findDecreasing(limit, H1, H2, K1, K2, start, mid);
        }
    }
}
