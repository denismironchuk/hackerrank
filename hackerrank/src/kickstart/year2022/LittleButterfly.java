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
        private long leftMaxEnergy;
        private long rightMaxEnergy;
        public Flower(int x, int y, long c) {
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
                    int height = lf.get(0).getY();
                    lf.add(new Flower(Integer.MIN_VALUE, height, 0));
                    lf.add(new Flower(Integer.MAX_VALUE, height, 0));
                    lf.sort(Comparator.comparingInt(Flower::getX));
                }
                List<Flower> processed = new ArrayList<>();
                while (!heightFlowers.isEmpty()) {
                    Map.Entry<Integer, List<Flower>> row = heightFlowers.pollFirstEntry();
                    List<Flower> rowFlowers = row.getValue();

                    //Calculate to right
                    for (int i = rowFlowers.size() - 1; i > -1; i--) {
                        Flower candidate = rowFlowers.get(i);
                        for (Flower proc : processed) {
                            if (proc.x >= candidate.x) {
                                candidate.rightMaxEnergy = Math.max(
                                        candidate.rightMaxEnergy,
                                        candidate.c + Math.max(
                                                proc.rightMaxEnergy, proc.leftMaxEnergy - e
                                        )
                                );
                            } else if (proc.x < candidate.x) {
                                candidate.rightMaxEnergy = Math.max(
                                        candidate.rightMaxEnergy,
                                        candidate.c + Math.max(
                                                proc.leftMaxEnergy - e, proc.rightMaxEnergy - 2 * e
                                        )
                                );
                            }
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
                        for (Flower proc : processed) {
                            if (proc.x <= candidate.x) {
                                candidate.leftMaxEnergy = Math.max(
                                        candidate.leftMaxEnergy,
                                        candidate.c + Math.max(
                                                proc.leftMaxEnergy, proc.rightMaxEnergy - e
                                        )
                                );
                            } else if (proc.x > candidate.x) {
                                candidate.leftMaxEnergy = Math.max(
                                        candidate.leftMaxEnergy,
                                        candidate.c + Math.max(
                                                proc.leftMaxEnergy - 2 * e, proc.rightMaxEnergy - e
                                        )
                                );
                            }
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

                    //rowFlowers.forEach(System.out::println);
                    //System.out.println("===============");
                    processed.addAll(rowFlowers);
                }

                long res = Long.MIN_VALUE;
                for (Flower f : processed) {
                    res = Math.max(res, Math.max(f.rightMaxEnergy, f.leftMaxEnergy - e));
                }
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }
}
