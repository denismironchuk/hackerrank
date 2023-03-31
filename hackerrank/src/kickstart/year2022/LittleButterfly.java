package kickstart.year2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class LittleButterfly {

    private static class Flower {

        private int x;
        private int y;
        private long c;
        private long leftMaxEnergy = Long.MIN_VALUE;
        private long rightMaxEnergy = Long.MIN_VALUE;
        public Flower(int x, int y, long c) {
            this.x = x;
            this.y = y;
            this.c = c;
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
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                long e = Long.parseLong(tkn1.nextToken());
                List<Flower> flowers = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    StringTokenizer tkn3 = new StringTokenizer(br.readLine());
                    int x = Integer.parseInt(tkn3.nextToken());
                    int y = Integer.parseInt(tkn3.nextToken());
                    long c = Long.parseLong(tkn3.nextToken());
                    flowers.add(new Flower(x, y, c));
                }
                TreeMap<Integer, List<Flower>> heightFlowers = new TreeMap<>(Integer::compare);
                for (Flower f : flowers) {
                    if (!heightFlowers.containsKey(f.y)) {
                        heightFlowers.put(f.y, new ArrayList<>());
                    }
                    heightFlowers.get(f.y).add(f);
                }
                for (List<Flower> lf : heightFlowers.values()) {
                    lf.sort(Comparator.comparingInt(Flower::getX));
                }
                List<Flower> processed = new ArrayList<>();
                while (!heightFlowers.isEmpty()) {
                    Map.Entry<Integer, List<Flower>> row = heightFlowers.pollFirstEntry();
                    List<Flower> rowFlowers = row.getValue();
                    for (int i = 0; i < rowFlowers.size(); i++) {
                        Flower testFlower = rowFlowers.get(i);
                        long rowEnergy = testFlower.c;

                        testFlower.leftMaxEnergy = Math.max(testFlower.leftMaxEnergy, rowEnergy);
                        testFlower.rightMaxEnergy = Math.max(testFlower.rightMaxEnergy, rowEnergy);

                        recalculateEnergy(testFlower, testFlower, processed, rowEnergy, e);

                        for (int j = i + 1; j < rowFlowers.size(); j++) {
                            Flower next = rowFlowers.get(j);
                            rowEnergy = rowEnergy + next.c;

                            testFlower.leftMaxEnergy = Math.max(testFlower.leftMaxEnergy, rowEnergy - e);
                            testFlower.rightMaxEnergy = Math.max(testFlower.rightMaxEnergy, rowEnergy);

                            recalculateEnergy(testFlower, next, processed, rowEnergy, e);
                        }

                        processed.add(testFlower);
                    }
                }
            }
        }
    }

    private static void recalculateEnergy(Flower testFlower, Flower nextFlower, List<Flower> processed, long rowEnergy, long e) {
        for (Flower procF : processed) {
            if (procF.x > nextFlower.x) {
                testFlower.leftMaxEnergy = Math.max(testFlower.leftMaxEnergy,
                        Math.max(rowEnergy - e + procF.rightMaxEnergy,
                                rowEnergy - 2 * e + procF.leftMaxEnergy));
                testFlower.rightMaxEnergy = Math.max(testFlower.rightMaxEnergy,
                        Math.max(rowEnergy + procF.rightMaxEnergy,
                                rowEnergy - e + procF.leftMaxEnergy));
            } else if (procF.x < nextFlower.x) {
                testFlower.leftMaxEnergy = Math.max(testFlower.leftMaxEnergy,
                        Math.max(rowEnergy + procF.leftMaxEnergy,
                                rowEnergy - e + procF.rightMaxEnergy));
                testFlower.rightMaxEnergy = Math.max(testFlower.rightMaxEnergy,
                        Math.max(rowEnergy - e + procF.leftMaxEnergy,
                                rowEnergy - 2 * e + procF.rightMaxEnergy));
            } else if (procF.x == nextFlower.x) {
                testFlower.leftMaxEnergy = Math.max(testFlower.leftMaxEnergy,
                        Math.max(rowEnergy + procF.leftMaxEnergy,
                                rowEnergy - e + procF.rightMaxEnergy));
                testFlower.rightMaxEnergy = Math.max(testFlower.rightMaxEnergy,
                        Math.max(rowEnergy + procF.rightMaxEnergy,
                                rowEnergy - e + procF.leftMaxEnergy));
            }
        }
    }
}
