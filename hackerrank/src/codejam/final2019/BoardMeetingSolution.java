package codejam.final2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class BoardMeetingSolution {
    private static final int BOARD_LIMS = 3;

    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private static class Point {
        private long x;
        private long y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class DiagPoint {
        private long x;
        private long y;

        private Diag posDiag;
        private Diag negDiag;

        public DiagPoint(long x, long y, Diag posDiag, Diag negDiag) {
            this.x = x;
            this.y = y;

            this.posDiag = posDiag;
            this.negDiag = negDiag;

            posDiag.addPoint(this);
            negDiag.addPoint(this);
        }
    }

    private static class Diag {
        private Point cord;
        private long pointCnt;
        /*
        -1 = /
         1 = \
         */
        private long dir;

        private List<DiagPoint> points = new ArrayList<>();

        private int usedPoints = 0;

        public Diag(Point cord, long pointCnt, long dir) {
            this.cord = cord;
            this.pointCnt = pointCnt;
            this.dir = dir;
        }

        public void addPoint(DiagPoint point) {
            points.add(point);
        }

        public boolean isShiftedDiag() {
            return Math.abs(cord.x) != Math.abs(cord.y);
        }
    }

    public static void main(String[] args) throws IOException {
        StringTokenizer initTkn = new StringTokenizer(br.readLine());

        int T = Integer.parseInt(initTkn.nextToken());
        int nMax = Integer.parseInt(initTkn.nextToken());
        int m = Integer.parseInt(initTkn.nextToken());
        int R = Integer.parseInt(initTkn.nextToken());

        for (int t = 1; t <= T; t++) {
            List<Diag> positiveDiags = getDiags(-1l);
            //System.out.println("Diag 1 done");
            List<Diag> negativeDiags = getDiags(1l);
            //System.out.println("Diag 2 done");

            initPossibleKingsPositions(positiveDiags, negativeDiags);
            int kingsCnt = getKingsCnt(positiveDiags);

            List<DiagPoint> comb = generateCombination(positiveDiags, 0, 0, new LinkedList<>(), kingsCnt);

            System.out.println("READY");

            for (int r = 0; r < R; r++) {
                String inpt = br.readLine();
                if (inpt.equals("ERROR")) {
                    throw new RuntimeException();
                }

                StringTokenizer reqTkn = new StringTokenizer(inpt);

                long x = Long.parseLong(reqTkn.nextToken());
                long y = Long.parseLong(reqTkn.nextToken());
                System.out.println(countTotalMovesToPoint(comb, x, y));
            }

            String inpt = br.readLine();
            if (!inpt.equals("DONE")) {
                throw new RuntimeException();
            }
        }
    }

    private static List<Diag> getDiags(long diagDir) throws IOException {
        List<Diag> locDiags = new ArrayList<>();
        long dist2 = countTotalMovesToPoint(-(BOARD_LIMS + 2), -diagDir * (BOARD_LIMS + 2));
        long dist1 = countTotalMovesToPoint(-(BOARD_LIMS + 1), -diagDir * (BOARD_LIMS + 1));

        long estimatedKingsAmount = dist2 - dist1;

        int foundKingsDiags = 0;

        long initPos = -(BOARD_LIMS + 1);
        long initMoves = dist1;
        long step = dist2 - dist1;

        while (foundKingsDiags != estimatedKingsAmount) {
            long changePos = findChangeStepPos(initPos, BOARD_LIMS + 1, initMoves, initPos, step, diagDir);
            long currentDist = countTotalMovesToPoint(changePos, changePos * diagDir);
            long prevDist = initMoves - (changePos - initPos - 1) * step;
            long currentDiff = prevDist - currentDist;

            long decr = step - currentDiff;
            long testDist1 = countTotalMovesToPoint(changePos - 2, (changePos - 1) * diagDir);
            long testDist2 = countTotalMovesToPoint(changePos - 1, changePos * diagDir);
            long testDiff = testDist1 - testDist2;

            if (testDiff == step) {
                locDiags.add(new Diag(new Point(changePos, (changePos - 1) * diagDir), decr, diagDir)); //touches diag
                foundKingsDiags += decr;
                currentDiff -= decr;
            } else {
                long amnt1 = step - testDiff;
                locDiags.add(new Diag(new Point(changePos - 1, (changePos - 1) * diagDir), amnt1, diagDir)); //crosses diag
                foundKingsDiags += amnt1;

                long amnt2 = decr - (amnt1 * 2);
                if (amnt2 != 0) {
                    locDiags.add(new Diag(new Point(changePos, (changePos - 1) * diagDir), amnt2, diagDir)); //touches diag
                    foundKingsDiags += amnt2;
                    currentDiff -= amnt2;
                }
            }

            initPos = changePos;
            initMoves = currentDist;
            step = currentDiff;
        }

        return locDiags;
    }

    private static long findChangeStepPos(long start, long end, long initMoves, long initPos, long step, long diagDir) throws IOException {
        if (start == end) {
            return start;
        }

        long mid = start + ((end - start) / 2);
        long moves = countTotalMovesToPoint(mid, mid * diagDir);
        long expectedMoves = initMoves - (mid - initPos) * step;
        if (moves == expectedMoves) {
            return findChangeStepPos(mid + 1, end, initMoves, initPos, step, diagDir);
        } else {
            return findChangeStepPos(start, mid, initMoves, initPos, step, diagDir);
        }
    }

    private static long countTotalMovesToPoint(long x, long y) throws IOException {
        System.out.println(x + " " + y);

        return Long.parseLong(br.readLine());
    }

    private static long countTotalMovesToPoint(List<DiagPoint> points, long x, long y) {
        long res = 0;

        for (DiagPoint king : points) {
            res += Math.max(Math.abs(king.x - x), Math.abs(king.y - y));
        }

        return res;
    }

    private static int getKingsCnt(List<Diag> diags) {
        int res = 0;

        for (Diag d : diags) {
            res += d.pointCnt;
        }

        return res;
    }

    private static void initPossibleKingsPositions(List<Diag> positiveDiags, List<Diag> negativeDiags) {
        for (Diag posDiag : positiveDiags) {
            for (Diag negDiag : negativeDiags) {
                if (!posDiag.isShiftedDiag() && !negDiag.isShiftedDiag()) {
                    DiagPoint cross = new DiagPoint(posDiag.cord.x + negDiag.cord.x, posDiag.cord.y + negDiag.cord.y, posDiag, negDiag);
                } else if (posDiag.isShiftedDiag() && negDiag.isShiftedDiag()) {
                    DiagPoint cross = new DiagPoint(posDiag.cord.x + negDiag.cord.x - 1, posDiag.cord.y + negDiag.cord.y, posDiag, negDiag);
                }
            }
        }
    }

    public static LinkedList<DiagPoint> generateCombination(List<Diag> diags, int diagIndex, int startPoint, LinkedList<DiagPoint> comb, int kingsCnt) {
        if (comb.size() == kingsCnt) {
            return comb;
        }

        Diag diag = diags.get(diagIndex);

        if (diag.usedPoints == diag.pointCnt) {
            LinkedList<DiagPoint> res = generateCombination(diags, diagIndex + 1, 0, comb, kingsCnt);
            if (null != res) {
                return res;
            }
        } else {
            for (int i = startPoint; i < diag.points.size(); i++) {
                DiagPoint p = diag.points.get(i);

                if (p.negDiag.usedPoints < p.negDiag.pointCnt && p.posDiag.usedPoints < p.posDiag.pointCnt) {
                    comb.add(p);
                    p.negDiag.usedPoints++;
                    p.posDiag.usedPoints++;

                    LinkedList<DiagPoint> res = generateCombination(diags, diagIndex, i + 1, comb, kingsCnt);

                    if (null != res) {
                        return res;
                    }

                    comb.removeLast();
                    p.negDiag.usedPoints--;
                    p.posDiag.usedPoints--;
                }
            }
        }

        return null;
    }
}
