package codejam.zillionim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ZillionimGame {
    private static final long INIT_COINTS = 1000000000000l;
    private static final long MOVE_COINTS = 10000000000l;

    //private static final long INIT_COINTS = 40l;
    //private static final long MOVE_COINTS = 2l;

    private static class Interval {
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
    }

    public static void main(String[] args) throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            long coins = INIT_COINTS;
            List<Interval> gameState = new ArrayList<>();
            gameState.add(new Interval(1, INIT_COINTS));
            int player = 0;

            while (hasNextMove(gameState)) {
                //br.readLine();
                makeRandomMove(gameState);
                player++;
                player %= 2;
            }

            System.out.printf("Player %s win\n", (player + 1) % 2);
        }
    }

    private static boolean hasNextMove(List<Interval> gameState) {
        return !gameState.isEmpty();
    }

    private static void makeRandomMove(List<Interval> gameState) {
        int intervalToSplitIndex = (int)(Math.random() * gameState.size());
        Interval intervalToSplit = gameState.get(intervalToSplitIndex);
        gameState.remove(intervalToSplit);

        long posToSplit = intervalToSplit.start + (long)(Math.random() * (intervalToSplit.getIntervalLength() - MOVE_COINTS + 1));
        gameState.addAll(intervalToSplit.splitInterval(posToSplit));
    }
}
