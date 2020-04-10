package codejam.year2019.zillionim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ZillionimFinal {
    private static final long INIT_COINTS = 1000000000000l;
    private static final long MOVE_COINTS = 10000000000l;
    //private static final long INIT_COINTS = 20l;
    //private static final long MOVE_COINTS = 2l;

    private static final long CONTABLE_LEN = 9 * MOVE_COINTS - 3;

    private static final long[][] GRANDY_INTERVALS = new long[][] {
            {0, 0,                   MOVE_COINTS - 1},
            {1, MOVE_COINTS,         2 * MOVE_COINTS - 1},
            {2, 2 * MOVE_COINTS,     3 * MOVE_COINTS - 2},
            {0, 3 * MOVE_COINTS - 1, 3 * MOVE_COINTS - 1},
            {3, 3 * MOVE_COINTS,     4 * MOVE_COINTS - 2},
            {1, 4 * MOVE_COINTS - 1, 5 * MOVE_COINTS - 2},
            {0, 5 * MOVE_COINTS - 1, 5 * MOVE_COINTS - 1},
            {4, 5 * MOVE_COINTS, 6 * MOVE_COINTS - 3},
            {3, 6 * MOVE_COINTS - 2, 7 * MOVE_COINTS - 3},
            {2, 7 * MOVE_COINTS - 2, 8 * MOVE_COINTS - 3},
            {4, 8 * MOVE_COINTS - 2, 9 * MOVE_COINTS - 4},
            {0, 9 * MOVE_COINTS - 3, 9 * MOVE_COINTS - 3},
    };

    private static final int GRAND_INTERVALS_CNT = GRANDY_INTERVALS.length;

    public static class Interval {

        private long start;
        private long end;

        public Interval(long start, long end) {
            this.start = start;
            this.end = end;
        }

        public long getIntervalLength() {
            return end - start + 1;
        }

        public List<Interval> splitInterval(long pos) {
            if (pos == start) {
                List<Interval> res = new ArrayList<>();
                Interval intr1 = new Interval(pos + MOVE_COINTS, end);
                if (intr1.getIntervalLength() >= MOVE_COINTS) {
                    res.add(intr1);
                }
                return res;
            } else {
                List<Interval> res = new ArrayList<>();

                Interval intr1 = new Interval(start, pos - 1);
                if (intr1.getIntervalLength() >= MOVE_COINTS) {
                    res.add(intr1);
                }

                Interval intr2 = new Interval(pos + MOVE_COINTS, end);
                if (intr2.getIntervalLength() >= MOVE_COINTS) {
                    res.add(intr2);
                }

                return res;
            }
        }

        public boolean isCountable() {
            return getIntervalLength() <= CONTABLE_LEN;
        }

        public long getGrandyValue() {
            return getGrandyValue(getIntervalLength());
        }

        public static long getGrandyValue(long len) {
            for (long[] interval : GRANDY_INTERVALS) {
                if (len >= interval[1] && len <= interval[2]) {
                    return interval[0];
                }
            }

            return 0;
        }

        public long getPositionToSplit(long expectedGrandyVal) {
            long len = getIntervalLength() - MOVE_COINTS;

            for (int i = 0; i < GRAND_INTERVALS_CNT; i++) {
                long[] interval1 = GRANDY_INTERVALS[i];
                long maxLen1 = interval1[2];
                long minLen1 = interval1[1];

                for (int j = i; j < GRAND_INTERVALS_CNT; j++) {
                    long[] interval2 = GRANDY_INTERVALS[j];
                    long maxLen2 = interval2[2];
                    long minLen2 = interval2[1];

                    if ((interval1[0] ^ interval2[0]) == expectedGrandyVal
                            && (minLen1 + minLen2 <= len && maxLen1 + maxLen2 >= len)) {
                        if (minLen2 + maxLen1 < len) {
                            return start + maxLen1;
                        }

                        if (minLen1 + maxLen2 < len) {
                            return start + maxLen2;
                        }

                        return start + minLen1;
                    }
                }
            }
            return -1;
        }
    }

    private static long makeRandomMove(List<Interval> gameState) {
        int intervalToSplitIndex = (int)(Math.random() * gameState.size());
        Interval intervalToSplit = gameState.get(intervalToSplitIndex);
        gameState.remove(intervalToSplit);

        long posToSplit = intervalToSplit.start + (long)(Math.random() * (intervalToSplit.getIntervalLength() - MOVE_COINTS + 1));
        gameState.addAll(intervalToSplit.splitInterval(posToSplit));
        return posToSplit;
    }

    private static long makeSplitMove(List<Interval> gameState) {
        Interval longest = null;
        long size = -1;

        for (Interval intr : gameState) {
            if (intr.getIntervalLength() > size) {
                longest = intr;
                size = intr.getIntervalLength();
            }
        }

        gameState.remove(longest);
        long posToSplit = longest.start + (size - MOVE_COINTS) / 2;

        gameState.addAll(longest.splitInterval(posToSplit));
        return posToSplit;
    }

    private static long makeCleverMove(List<Interval> gameState) {
        long stateGrand = gameState.stream().map(Interval::getGrandyValue).reduce((a, b) -> a ^ b).orElse(0l);
        if (stateGrand == 0) {
            return -1;
        }
        Interval toSplit = null;
        long pos = -1;
        for (Interval intr : gameState) {
            pos = intr.getPositionToSplit(stateGrand ^ intr.getGrandyValue());
            if (pos != -1) {
                toSplit =  intr;
                break;
            }
        }

        if (pos != -1) {
            gameState.remove(toSplit);
            gameState.addAll(toSplit.splitInterval(pos));
            return pos;
        } else {
            return -1;
        }
    }

    private static Interval findInterval(List<Interval> gameState, long p) {
        for (Interval intr : gameState) {
            if (intr.start <= p && intr.end >= p) {
                return intr;
            }
        }
        throw new IllegalStateException();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line1Tkn = new StringTokenizer(br.readLine());
        int T = Integer.parseInt(line1Tkn.nextToken());
        for (int t = 0; t < T; t++) {
            List<Interval> gameState = new ArrayList<>();
            gameState.add(new Interval(1, INIT_COINTS));

            while (true) {
                long p = Long.parseLong(br.readLine());
                if (p == -2 || p == -3) {
                    break;
                }

                if (p == -1) {
                    return;
                }

                Interval toSplit = findInterval(gameState, p);
                gameState.remove(toSplit);
                gameState.addAll(toSplit.splitInterval(p));

                long pos = -1;
                boolean isCountable = gameState.stream().allMatch(Interval::isCountable);
                if (isCountable) {
                    pos = makeCleverMove(gameState);
                    if (pos == -1) {
                        pos = makeRandomMove(gameState);
                    }
                } else {
                    pos = makeSplitMove(gameState);
                }
                System.out.println(pos);
            }
        }
    }
}
