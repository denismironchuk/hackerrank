package kickstart.year2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class LittleButterfly {

    private static final Random rnd = new Random();

    private static class Treap {
        private Flower x;
        private long y;

        private Treap left;
        private Treap right;

        private long maxLeftEnergy;
        private long maxRightEnergy;

        public Treap(Flower x) {
            this.x = x;
            this.y = rnd.nextLong();
            this.maxLeftEnergy = x.leftMaxEnergy;
            this.maxRightEnergy = x.rightMaxEnergy;
        }

        public int getSize() {
            int size = 1;
            if (left != null) {
                size += left.getSize();
            }

            if (right != null) {
                size += right.getSize();
            }

            return size;
        }

        private void recalculateMinLen() {
            this.maxLeftEnergy = Math.max(x.leftMaxEnergy,
                    Math.max(left == null ? Integer.MIN_VALUE / 2 : left.maxLeftEnergy,
                            right == null ? Integer.MIN_VALUE / 2 : right.maxLeftEnergy));

            this.maxRightEnergy = Math.max(x.rightMaxEnergy,
                    Math.max(left == null ? Integer.MIN_VALUE / 2 : left.maxRightEnergy,
                            right == null ? Integer.MIN_VALUE / 2 : right.maxRightEnergy));
        }

        public Treap[] split(Flower s) {
            Treap[] res;
            if (x.compareTo(s) < 0) {
                if (right == null) {
                    return new Treap[] {this, null};
                }

                Treap[] splitRes = right.split(s);
                right = splitRes[0];
                res = new Treap[] {this, splitRes[1]};
            } else {
                if (left == null) {
                    return new Treap[]{null, this};
                }

                Treap[] splitRes = left.split(s);
                left = splitRes[1];
                res = new Treap[]{splitRes[0], this};
            }
            recalculateMinLen();
            return res;
        }

        public static Treap merge(Treap lower, Treap higher) {
            if (lower == null) {
                return higher;
            }

            if (higher == null) {
                return lower;
            }

            Treap res;
            if (lower.y > higher.y) {
                lower.right = merge(lower.right, higher);
                res = lower;
            } else {
                higher.left = merge(lower, higher.left);
                res = higher;
            }
            res.recalculateMinLen();
            return res;
        }

        public Treap addNode(Flower x) {
            Treap[] split = this.split(x);
            return Treap.merge(Treap.merge(split[0], new Treap(x)), split[1]);
        }

        @Override
        public String toString() {
            return String.valueOf(getSize());
        }
    }

    private static class Flower implements Comparable<Flower> {

        private int index;
        private int x;
        private int y;
        private long c;
        private long leftMaxEnergy;
        private long rightMaxEnergy;
        public Flower(int index, int x, int y, long c) {
            this.index = index;
            this.x = x;
            this.y = y;
            this.c = c;
            this.leftMaxEnergy = c;
            this.rightMaxEnergy = c;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public long getC() {
            return c;
        }

        @Override
        public String toString() {
            return "<-" + leftMaxEnergy +
                    ", ->" + rightMaxEnergy;
        }

        @Override
        public int compareTo(Flower o) {
            int cmp1 = Integer.compare(this.x, o.x);
            if (cmp1 != 0) {
                return cmp1;
            }
            return Integer.compare(this.index, o.index);
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                long e = Long.parseLong(tkn1.nextToken());
                List<Flower> flowers = new ArrayList<>();
                int index = 1;
                for (int i = 0; i < n; i++) {
                    StringTokenizer tkn3 = new StringTokenizer(br.readLine());
                    int x = Integer.parseInt(tkn3.nextToken());
                    int y = Integer.parseInt(tkn3.nextToken());
                    long c = Long.parseLong(tkn3.nextToken());
                    flowers.add(new Flower(index++, x, y, c));
                }
                TreeMap<Integer, List<Flower>> heightFlowers = new TreeMap<>(Integer::compare);
                for (Flower f : flowers) {
                    if (!heightFlowers.containsKey(f.y)) {
                        heightFlowers.put(f.y, new ArrayList<>());
                    }
                    heightFlowers.get(f.y).add(f);
                }
                for (List<Flower> lf : heightFlowers.values()) {
                    int height = lf.get(0).getY();
                    lf.add(new Flower(index++, Integer.MIN_VALUE, height, 0));
                    lf.add(new Flower(index++, Integer.MAX_VALUE, height, 0));
                    lf.sort(Comparator.comparingInt(Flower::getX));
                }
                Treap processedOpt = null;
                while (!heightFlowers.isEmpty()) {
                    Map.Entry<Integer, List<Flower>> row = heightFlowers.pollFirstEntry();
                    List<Flower> rowFlowers = row.getValue();

                    //Calculate to right
                    for (int i = rowFlowers.size() - 1; i > -1; i--) {
                        Flower candidate = rowFlowers.get(i);
                        if (processedOpt != null) {
                            Treap[] split = processedOpt.split(new Flower(-1, candidate.x, 0, 0));

                            long leftEnergyLeftPoints = split[0] == null ? Long.MIN_VALUE / 2 : split[0].maxLeftEnergy;
                            long rightEnergyLeftPoints = split[0] == null ? Long.MIN_VALUE / 2 : split[0].maxRightEnergy;

                            candidate.rightMaxEnergy = Math.max(
                                    candidate.rightMaxEnergy,
                                    candidate.c + Math.max(
                                            leftEnergyLeftPoints - e, rightEnergyLeftPoints - 2 * e
                                    )
                            );

                            long leftEnergyRightPoints = split[1] == null ? Long.MIN_VALUE / 2 : split[1].maxLeftEnergy;
                            long rightEnergyRightPoints = split[1] == null ? Long.MIN_VALUE / 2 : split[1].maxRightEnergy;

                            candidate.rightMaxEnergy = Math.max(
                                    candidate.rightMaxEnergy,
                                    candidate.c + Math.max(
                                            rightEnergyRightPoints, leftEnergyRightPoints - e
                                    )
                            );
                            processedOpt = Treap.merge(split[0], split[1]);
                        }
                        if (i + 1 < rowFlowers.size()) {
                            candidate.rightMaxEnergy = Math.max(
                                    candidate.rightMaxEnergy,
                                    candidate.c + rowFlowers.get(i + 1).rightMaxEnergy
                            );
                        }
                    }

                    //Calculate to left
                    for (int i = 0; i < rowFlowers.size(); i++) {
                        Flower candidate = rowFlowers.get(i);
                        if (processedOpt != null) {
                            Treap[] split = processedOpt.split(new Flower(Integer.MAX_VALUE, candidate.x, 0, 0));

                            long leftEnergyLeftPoints = split[0] == null ? Long.MIN_VALUE / 2 : split[0].maxLeftEnergy;
                            long rightEnergyLeftPoints = split[0] == null ? Long.MIN_VALUE / 2 : split[0].maxRightEnergy;

                            candidate.leftMaxEnergy = Math.max(
                                    candidate.leftMaxEnergy,
                                    candidate.c + Math.max(
                                            leftEnergyLeftPoints, rightEnergyLeftPoints - e
                                    )
                            );

                            long leftEnergyRightPoints = split[1] == null ? Long.MIN_VALUE / 2 : split[1].maxLeftEnergy;
                            long rightEnergyRightPoints = split[1] == null ? Long.MIN_VALUE / 2 : split[1].maxRightEnergy;

                            candidate.leftMaxEnergy = Math.max(
                                    candidate.leftMaxEnergy,
                                    candidate.c + Math.max(
                                            leftEnergyRightPoints - 2 * e, rightEnergyRightPoints - e
                                    )
                            );

                            processedOpt = Treap.merge(split[0], split[1]);
                        }
                        if (i > 0) {
                            candidate.leftMaxEnergy = Math.max(
                                    candidate.leftMaxEnergy,
                                    candidate.c + rowFlowers.get(i - 1).leftMaxEnergy
                            );
                        }
                    }

                    long[] toRightAccumulatedSum = new long[rowFlowers.size()];
                    for (int i = rowFlowers.size() - 2; i > -1; i--) {
                        toRightAccumulatedSum[i] = rowFlowers.get(i).c + toRightAccumulatedSum[i + 1];
                    }
                    long[] toLeftAccumulatedSum = new long[rowFlowers.size()];
                    for (int i = 1; i < rowFlowers.size(); i++) {
                        toLeftAccumulatedSum[i] = rowFlowers.get(i).c + toLeftAccumulatedSum[i - 1];
                    }

                    for (int i = 0; i < rowFlowers.size(); i++) {
                        Flower fl = rowFlowers.get(i);
                        long newLeft = Math.max(fl.leftMaxEnergy, toLeftAccumulatedSum[i] - e + fl.rightMaxEnergy - fl.c);
                        long newRight = Math.max(fl.rightMaxEnergy, toRightAccumulatedSum[i] - e + fl.leftMaxEnergy - fl.c);
                        fl.leftMaxEnergy = newLeft;
                        fl.rightMaxEnergy = newRight;
                    }

                    for (Flower f : rowFlowers) {
                        if (processedOpt == null) {
                            processedOpt = new Treap(f);
                        } else {
                            processedOpt = processedOpt.addNode(f);
                        }
                    }
                }

                long res = Long.MIN_VALUE;
                Treap[] split = processedOpt.split(new Flower(-1, -1, -1, -1));
                res = Math.max(res, Math.max(split[1].maxRightEnergy, split[1].maxLeftEnergy - e));
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }
}
