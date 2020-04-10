package codejam.year2019.zillionim;

import java.util.ArrayList;
import java.util.List;

public class ZillionimGame {
    //private static final long INIT_COINTS = 1000000000000l;
    //private static final long MOVE_COINTS = 10000000000l;
    private static final long INIT_COINTS = 5000l;
    private static final long MOVE_COINTS = 50l;

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
            for (long[] interval: GRANDY_INTERVALS) {
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

        public long getStart() {
            return start;
        }

        public long getEnd() {
            return end;
        }
    }

    public static void main(String[] args) {
        int firstWin = 0;
        for (int i = 0; i < 500; i++) {
        //while (true) {
            List<Interval> gameState = new ArrayList<>();
            gameState.add(new Interval(1, INIT_COINTS));
            int player = 0;

            while (hasNextMove(gameState)) {
                if (player == 0) {
                    makeRandomMove(gameState);
                } else {
                    boolean isCountable = gameState.stream().allMatch(Interval::isCountable);
                    if (isCountable) {
                        if (!makeCleverMove(gameState)) {
                            makeRandomMove(gameState);
                        }
                    } else {
                        makeSplitMove(gameState);
                    }
                }
                player++;
                player %= 2;
            }

            if ((player + 1) % 2 == 1) {
                firstWin++;
            }
        }
        System.out.printf("%s", firstWin);
    }

    private static boolean hasNextMove(List<Interval> gameState) {
        return !gameState.isEmpty();
    }

    private static void makeRandomMove(List<Interval> gameState) {
        int intervalToSplitIndex = (int)(Math.random() * gameState.size());
        Interval intervalToSplit = gameState.get(intervalToSplitIndex);
        gameState.remove(intervalToSplit);

        long posToSplit = intervalToSplit.start + (long)(Math.random() * (intervalToSplit.getIntervalLength() - MOVE_COINTS + 1));

        if (posToSplit < intervalToSplit.start || posToSplit > intervalToSplit.end - MOVE_COINTS + 1) {
            throw new IllegalStateException();
        }

        gameState.addAll(intervalToSplit.splitInterval(posToSplit));
    }

    private static void makeSplitMove(List<Interval> gameState) {
        Interval longest = null;
        long size = -1;

        for (Interval intr : gameState) {
            if (intr.getIntervalLength() > size) {
                longest = intr;
                size = intr.getIntervalLength();
            }
        }

        gameState.remove(longest);
        long posToSplit = longest.start + ((size - MOVE_COINTS) / 2);

        if (posToSplit < longest.start || posToSplit > longest.end - MOVE_COINTS + 1) {
            throw new IllegalStateException();
        }

        gameState.addAll(longest.splitInterval(posToSplit));
    }

    private static boolean makeCleverMove(List<Interval> gameState) {
        long stateGrand = gameState.stream().map(Interval::getGrandyValue).reduce((a, b) -> a ^ b).orElse(0l);
        if (stateGrand == 0) {
            return false;
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
            if (pos < toSplit.start || pos > toSplit.end - MOVE_COINTS + 1) {
                throw new IllegalStateException();
            }

            gameState.remove(toSplit);
            gameState.addAll(toSplit.splitInterval(pos));
            return true;
        } else {
            return false;
        }
    }
}
