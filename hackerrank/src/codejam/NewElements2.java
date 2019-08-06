package codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class NewElements2 {
    private static class Rational implements Comparable<Rational> {
        private long chisl;
        private long znam;

        public Rational(long chisl, long znam) {
            this(chisl, znam, true);
        }

        public Rational(long chisl, long znam, boolean gcdCount) {
            long gcd = 1;
            if (gcdCount) {
                gcd = gcd(chisl, znam);
            }

            this.chisl = chisl / gcd;
            this.znam = znam / gcd;
        }

        public long getIntVal(long mult) {
            return chisl * mult / znam;
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

        @Override
        public int compareTo(Rational r) {
            long r1 = chisl * r.znam;
            long r2 = r.chisl * znam;
            return Long.compare(r1, r2);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        for (int t = 1; t <= T; t++) {
            int n = Integer.parseInt(br.readLine());

            long[][] moleculs = new long[n][2];
            long[][] forSorting = new long[n][2];

            for (int i = 0; i < n; i++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                moleculs[i][0] = Long.parseLong(tkn.nextToken());
                moleculs[i][1] = Long.parseLong(tkn.nextToken());

                forSorting[i][0] = moleculs[i][0];
                forSorting[i][1] = moleculs[i][1];
            }

            Set<Rational> drobs = new TreeSet<>();
            drobs.add(new Rational(0, 1, false));
            drobs.add(new Rational(1, 0, false));

            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (Long.compare(moleculs[i][0], moleculs[j][0]) * Long.compare(moleculs[i][1], moleculs[j][1]) < 0) {
                        long chisl = Math.abs(moleculs[i][0] - moleculs[j][0]); //jamarium
                        long znam = Math.abs(moleculs[i][1] - moleculs[j][1]); //codium
                        drobs.add(new Rational(chisl, znam));
                    }
                }
            }

            Iterator<Rational> itr = drobs.iterator();
            Rational start = itr.next();
            Rational end = null;
            boolean foundAppropriate = false;

            while (!foundAppropriate && itr.hasNext()) {
                end = itr.next();
                final Rational weights2 = new Rational(start.chisl + end.chisl, start.znam + end.znam);
                Arrays.sort(forSorting, (molecul1, molecul2) -> getCompare(molecul1, molecul2, weights2));

                if (areArraysEqual(moleculs, forSorting)) {
                    foundAppropriate = true;
                } else {
                    start = end;
                }
            }

            if (foundAppropriate) {
                long smallestZnam = getSmallestZnamOptimal(start, end);
                long smallestChisl = 1 + (start.chisl * smallestZnam) / start.znam;
                System.out.printf("Case #%s: %s %s\n", t, smallestZnam, smallestChisl);
            } else {
                System.out.printf("Case #%s: %s\n", t, "IMPOSSIBLE");
            }
        }
    }

    private static boolean areArraysEqual(long[][] arr1, long[][] arr2) {
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i][0] != arr2[i][0] || arr1[i][1] != arr2[i][1]) {
                return false;
            }
        }
        return true;
    }

    private static int getCompare(long[] molecul1, long[] molecul2, Rational weights) {
        return Long.compare(molecul1[0] * weights.znam + molecul1[1] * weights.chisl,
                molecul2[0] * weights.znam + molecul2[1] * weights.chisl);
    }

    private static long getSmallestZnamOptimal(Rational r1, Rational r2) {
        long res = Long.MAX_VALUE;

        long chisl = r1.chisl + r2.chisl;
        long znam = r1.znam + r2.znam;

        List<Long> A = new ArrayList<>();
        while (znam > 1) {
            A.add(chisl/znam);
            long ost = chisl % znam;
            chisl = znam;
            znam = ost;
        }

        A.add(chisl);

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
            long a = findIncreasing(r1, H[i - 1], H[i - 2], K[i - 1], K[i - 2], 0, A.get(i - 2));
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
            long a = findDecreasing(r2, H[i - 1], H[i - 2], K[i - 1], K[i - 2], 0, A.get(i - 2));
            res = Math.min(res, a * K[i - 1] + K[i - 2]);
        }

        return res;
    }

    private static long findIncreasing(Rational limit, long H1, long H2, long K1, long K2, long start, long end) {
        if (start == end) {
            return start;
        }

        long mid = (start + end) / 2;
        Rational r = new Rational(mid * H1 + H2, mid * K1 + K2, false);

        if (r.compareTo(limit) != 1) {
            return findIncreasing(limit, H1, H2, K1, K2, mid + 1, end);
        } else {
            return findIncreasing(limit, H1, H2, K1, K2, start, mid);
        }
    }

    private static long findDecreasing(Rational limit, long H1, long H2, long K1, long K2, long start, long end) {
        if (start == end) {
            return start;
        }

        long mid = (start + end) / 2;
        Rational r = new Rational(mid * H1 + H2, mid * K1 + K2, false);

        if (r.compareTo(limit) != -1) {
            return findDecreasing(limit, H1, H2, K1, K2, mid + 1, end);
        } else {
            return findDecreasing(limit, H1, H2, K1, K2, start, mid);
        }
    }
}
