package codejam.year2022.round3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class DuckDuckGeese {

    private static class Interval {
        private long startPos;
        private long stopPos;
        private boolean canBeGoose;

        public Interval(long startPos, long stopPos, boolean canBeGoose) {
            this.startPos = startPos;
            this.stopPos = stopPos;
            this.canBeGoose = canBeGoose;
        }
    }
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                int c = Integer.parseInt(tkn1.nextToken());
                int[] colorLowLimit = new int[c];
                int[] colorUpLimit = new int[c];
                for (int i = 0; i < c; i++) {
                    StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                    int low = Integer.parseInt(tkn2.nextToken());
                    int up = Integer.parseInt(tkn2.nextToken());
                    colorLowLimit[i] = low;
                    colorUpLimit[i] = up;
                }
                int[] hatColors = new int[n];
                StringTokenizer tkn3 = new StringTokenizer(br.readLine());
                for (int i = 0; i < n; i++) {
                    hatColors[i] = Integer.parseInt(tkn3.nextToken()) - 1;
                }
                long res = 0;
                for (int startPos = 0; startPos < n; startPos++) {
                    List<Interval> intervals = new ArrayList<>();
                    Interval currentInterval = new Interval(startPos, startPos, false);
                    intervals.add(currentInterval);

                    Set<Integer> reachedMin = new HashSet<>();
                    Map<Integer, Integer> remainToMin = new HashMap<>();
                    Map<Integer, Integer> remainToMax = new HashMap<>();

                    int curRemainToMin = colorLowLimit[hatColors[startPos]] - 1;
                    if (curRemainToMin <= 0) {
                        reachedMin.add(hatColors[startPos]);
                    } else {
                        remainToMin.put(hatColors[startPos], curRemainToMin);
                    }

                    boolean canStart = true;
                    int curRemainToMax = colorUpLimit[hatColors[startPos]] - 1;
                    if (curRemainToMax == -1) {
                        canStart = false;
                    } else {
                        remainToMax.put(hatColors[startPos], curRemainToMax);
                    }

                    if (canStart) {
                        for (int pos = (startPos + 1) % n; pos != (startPos - 1 + n) % n; pos = (pos + 1) % n) {
                            if (!reachedMin.contains(hatColors[pos])) {
                                curRemainToMin = remainToMin.containsKey(hatColors[pos]) ?
                                        remainToMin.get(hatColors[pos]) - 1 : colorLowLimit[hatColors[pos]] - 1;
                                if (curRemainToMin <= 0) {
                                    remainToMin.remove(hatColors[pos]);
                                    reachedMin.add(hatColors[pos]);
                                } else {
                                    remainToMin.put(hatColors[pos], curRemainToMin);
                                }
                            }

                            curRemainToMax = remainToMax.containsKey(hatColors[pos]) ?
                                    remainToMax.get(hatColors[pos]) - 1 : colorUpLimit[hatColors[pos]] - 1;
                            if (curRemainToMax == -1) {
                                break;
                            } else {
                                remainToMax.put(hatColors[pos], curRemainToMax);
                            }

                            if (remainToMin.isEmpty()) {
                                if (currentInterval.canBeGoose == false) {
                                    currentInterval = new Interval(pos, pos, true);
                                    intervals.add(currentInterval);
                                } else {
                                    currentInterval.stopPos++;
                                }
                            } else {
                                if (currentInterval.canBeGoose == true) {
                                    currentInterval = new Interval(pos, pos, false);
                                    intervals.add(currentInterval);
                                } else {
                                    currentInterval.stopPos++;
                                }
                            }
                        }
                    }

                    res += intervals.stream().filter(interval -> interval.canBeGoose)
                            .map(interval -> interval.stopPos - interval.startPos + 1)
                            .reduce(0l, (a, b) -> a + b);
                }
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }
}
