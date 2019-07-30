package codejam;

import java.util.*;
import java.util.Map.Entry;

public class NewElements2 {
    private static class Drob implements Comparable<Drob> {
        private long chisl;
        private long znam;

        public Drob(long chisl, long znam) {
            this.chisl = chisl;
            this.znam = znam;
        }

        @Override
        public int compareTo(Drob d) {
            return Long.compare(chisl * d.znam, d.chisl * znam);
        }

        @Override
        public String toString() {
            return String.format("%d/%d", chisl, znam);
        }
    }
    public static void main(String[] args) {
        int n = 5;
        long[][] moleculs = new long[n][2];

        for (int i = 0; i < n; i++) {
            moleculs[i][0] = (long)(20 * Math.random());
            moleculs[i][1] = (long)(20 * Math.random());
        }

        Map<Long, Set<Long>> drobsSet = new HashMap<>();

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (Long.compare(moleculs[i][0], moleculs[j][0]) * Long.compare(moleculs[i][1], moleculs[j][1]) < 0) {
                    long chisl = Math.abs(moleculs[i][1] - moleculs[j][1]); //codium
                    long znam = Math.abs(moleculs[i][0] - moleculs[j][0]); //jamarium

                    long gcd = gcd(chisl, znam);
                    chisl /= gcd;
                    znam /= gcd;

                    Set<Long> znams = drobsSet.get(chisl);

                    if (znams == null) {
                        znams = new HashSet<>();
                        znams.add(znam);
                        drobsSet.put(chisl, znams);
                    } else {
                        if (!znams.contains(znam)) {
                            znams.add(znam);
                        }
                    }
                }
            }
        }

        List<Drob> drobs = new ArrayList<>();
        for (Entry<Long, Set<Long>> entry : drobsSet.entrySet()) {
            long chisl = entry.getKey();
            Set<Long> znams = entry.getValue();
            for (Long znam : znams) {
                drobs.add(new Drob(chisl, znam));
            }
        }

        Collections.sort(drobs);

        Drob first = drobs.get(0);
        final Drob weights1 = getMinusInfIntervalMin(first);
        System.out.printf("(-inf - %s) - %s\n", first, weights1);

        Arrays.sort(moleculs, (molecul1, molecul2) -> getCompare(molecul1, molecul2, weights1));
        printMoleculs(moleculs);
        for (int i = 1; i < drobs.size() - 1; i++) {
            Drob start = drobs.get(i - 1);
            Drob end = drobs.get(i);
            final Drob weights2 = getIntervalMin(start, end);
            System.out.printf("(%s - %s) - %s\n", start, end, weights2);
            Arrays.sort(moleculs, (molecul1, molecul2) -> getCompare(molecul1, molecul2, weights2));
            printMoleculs(moleculs);
        }

        Drob last = drobs.get(drobs.size() - 1);
        Drob weights3 = getPlusInfIntervalMin(last);
        System.out.printf("(%s - inf) - %s\n", last, weights3);
        Arrays.sort(moleculs, (molecul1, molecul2) -> getCompare(molecul1, molecul2, weights3));
        printMoleculs(moleculs);
    }

    private static int getCompare(long[] molecul1, long[] molecul2, Drob weights) {
        return Long.compare(molecul1[0] * weights.chisl + molecul1[1] * weights.znam,
                molecul2[0] * weights.chisl + molecul2[1] * weights.znam);
    }

    private static void printMoleculs(long[][] moleculs) {
        for (long[] mol : moleculs) {
            System.out.printf("(%s, %s) ", mol[0], mol[1]);
        }
        System.out.println();
    }

    private static Drob getMinusInfIntervalMin(Drob d) {
        if (d.chisl != 1) {
            return new Drob(1, d.znam);
        } else {
            return new Drob(1, d.znam * 2);
        }
    }

    private static Drob getPlusInfIntervalMin(Drob d) {
        long chisl = d.chisl + 1;
        long znam = d.znam;

        long gcd = gcd(chisl, znam);

        return new Drob(chisl / gcd, znam / gcd);
    }

    private static Drob getIntervalMin(Drob start, Drob end) {
        long znamGcd = gcd(start.znam, end.znam);
        long commonZnam = start.znam * (end.znam / znamGcd);

        long startChisl = start.chisl * end.znam / znamGcd;
        long endChisl = end.chisl * start.znam / znamGcd;

        if (startChisl + 1 != endChisl) {
            long chisl = startChisl + 1;
            long znam = commonZnam;

            long gcd = gcd(chisl, znam);
            return new Drob(chisl / gcd, znam / gcd);
        } else {
            long chisl = 2 * startChisl + 1;
            long znam = 2 * commonZnam;

            long gcd = gcd(chisl, znam);
            return new Drob(chisl / gcd, znam / gcd);
        }
    }

    private static long gcd(long a, long b) {
        return a < b ? gcdInner(b, a) : gcdInner(a, b);
    }

    private static long gcdInner(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return gcdInner(b, a % b);
        }
    }
}
