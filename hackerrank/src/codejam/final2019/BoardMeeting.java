package codejam.final2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BoardMeeting {
    private static final int N_MAX = 4;
    private static final int BOARD_LIMS = 1000000;
    private static int n;
    private static Point[] kings;

    private static int REQ_CNT = 0;

    private static class Point {
        private long x;
        private long y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return String.format("(%s %s)", x, y);
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

        @Override
        public String toString() {
            return String.format("(%s %s)", x, y);
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

        @Override
        public String toString() {
            return String.format("%s %s %s", cord, pointCnt, dir == 1 ? "\\" : "/");
        }
    }

    private static boolean isOccupiedCell(long x, long y) {
        for (Point king : kings) {
            if (king == null) {
                return false;
            }
            if (king.x == x && king.y == y) {
                return true;
            }
        }

        return false;
    }

    private static long generateRandomBoardCord() {
        return (long)(2 * BOARD_LIMS * Math.random()) - BOARD_LIMS;
    }

    private static long countTotalMovesToPoint(long x, long y) {
        REQ_CNT++;
        long res = 0;

        for (Point king : kings) {
            res += Math.max(Math.abs(king.x - x), Math.abs(king.y - y));
        }

        return res;
    }

    private static void generateRandomPoints() {
        for (int i = 0; i < n; i++) {
            long x = generateRandomBoardCord();
            long y = generateRandomBoardCord();

            while (isOccupiedCell(x, y)) {
                x = generateRandomBoardCord();
                y = generateRandomBoardCord();
            }

            kings[i] = new Point(x, y);
        }
    }

    private static void inputPoints(BufferedReader br) throws IOException {
        for (int i = 0; i < n; i++) {
            StringTokenizer pointTkn = new StringTokenizer(br.readLine());

            long x = Long.parseLong(pointTkn.nextToken());
            long y = Long.parseLong(pointTkn.nextToken());

            kings[i] = new Point(x, y);
        }
    }

    private static void printBoard() {
        for (long row = BOARD_LIMS; row > -BOARD_LIMS - 1; row--) {
            System.out.printf("%3d | ", row);
            for (long col = -BOARD_LIMS; col < BOARD_LIMS + 1; col++) {
                if (isPoint(col, row)) {
                    System.out.print("*  ");
                } else {
                    System.out.print("-  ");
                }
            }
            System.out.println();
        }

        System.out.printf("    ");
        for (int col = -BOARD_LIMS; col < BOARD_LIMS + 1; col++) {
            System.out.printf("%3d", col);
        }
        System.out.println();
    }

    private static boolean isPoint(long x, long y) {
        for (Point king : kings) {
            if (king.x == x && king.y == y) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            REQ_CNT = 0;
            //n = (int) (Math.random() * N_MAX) + 1;
            //n = Integer.parseInt(br.readLine());
            n = 10;
            kings = new Point[n];

            //inputPoints(br);
            generateRandomPoints();
            //printBoard();

            List<Diag> positiveDiags = getDiags(-1l);
            List<Diag> negativeDiags = getDiags(1l);

            /*System.out.println("Positive diags /");
            positiveDiags.forEach(System.out::println);
            System.out.println("Negative diags \\");
            negativeDiags.forEach(System.out::println);*/

            validateDiags(positiveDiags);
            validateDiags(negativeDiags);

            List<DiagPoint> possiblePositions = getPossibleKingsPositions(positiveDiags, negativeDiags);

            validatePossiblePositions(possiblePositions);

            int kingsCnt = getKingsCnt(positiveDiags);

            Date start = new Date();

            //long combsCnt = generateCombinations(positiveDiags, 0, 0, new LinkedList<>(), kingsCnt, 0);
            boolean realPosIsPresent = generateCombinations2(positiveDiags, 0, 0, new LinkedList<>(), kingsCnt, 0);

            Date end = new Date();

            if (!realPosIsPresent) {
                throw new RuntimeException("No real pos among candidates");
            }

            //System.out.println("Request = " + REQ_CNT);
            long execTime = end.getTime() - start.getTime();
            //if (execTime > 400) {
                System.out.println("Combinations generation took " + execTime + "ms");
                //System.out.println("Combinations = " + combsCnt);
            //}
            System.out.println("================");
        }
    }

    public static boolean generateCombinations2(List<Diag> diags, int diagIndex, int startPoint, LinkedList<DiagPoint> comb, int kingsCnt, long combCnt) {
        if (comb.size() == kingsCnt) {

            for (Point king : kings) {
                boolean isPresent = false;
                for (DiagPoint p : comb) {
                    if (king.x == p.x && king.y == p.y) {
                        isPresent = true;
                        break;
                    }
                }
                if (!isPresent) {
                    return false;
                }
            }

            return true;
        }

        Diag diag = diags.get(diagIndex);

        if (diag.usedPoints == diag.pointCnt) {
            if (generateCombinations2(diags, diagIndex + 1, 0, comb, kingsCnt, combCnt)) {
                return true;
            }
        } else {
            for (int i = startPoint; i < diag.points.size(); i++) {
                DiagPoint p = diag.points.get(i);

                if (p.negDiag.usedPoints < p.negDiag.pointCnt && p.posDiag.usedPoints < p.posDiag.pointCnt) {
                    comb.add(p);
                    p.negDiag.usedPoints++;
                    p.posDiag.usedPoints++;

                    if (generateCombinations2(diags, diagIndex, i + 1, comb, kingsCnt, combCnt)) {
                        return true;
                    }

                    comb.removeLast();
                    p.negDiag.usedPoints--;
                    p.posDiag.usedPoints--;
                }
            }
        }

        return false;
    }

    private static int getKingsCnt(List<Diag> diags) {
        int res = 0;

        for (Diag d : diags) {
            res += d.pointCnt;
        }

        return res;
    }

    private static List<DiagPoint> getPossibleKingsPositions(List<Diag> positiveDiags, List<Diag> negativeDiags) {
        List<DiagPoint> res = new ArrayList<>();

        for (Diag posDiag : positiveDiags) {
            for (Diag negDiag : negativeDiags) {
                if (!posDiag.isShiftedDiag() && !negDiag.isShiftedDiag()) {
                    DiagPoint cross = new DiagPoint(posDiag.cord.x + negDiag.cord.x, posDiag.cord.y + negDiag.cord.y, posDiag, negDiag);
                    res.add(cross);
                } else if (posDiag.isShiftedDiag() && negDiag.isShiftedDiag()) {
                    DiagPoint cross = new DiagPoint(posDiag.cord.x + negDiag.cord.x - 1, posDiag.cord.y + negDiag.cord.y, posDiag, negDiag);
                    res.add(cross);
                }
            }
        }

        return res;
    }

    /*
    -1 - /
     1 - \
     */
    private static List<Diag> getDiags(long diagDir) {
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

    private static long findChangeStepPos(long start, long end, long initMoves, long initPos, long step, long diagDir) {
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

    private static void validatePossiblePositions(List<DiagPoint> possiblePositions) {
        for (Point king : kings) {
            boolean isPresent = false;
            for (DiagPoint pos : possiblePositions) {
                if (king.x == pos.x && king.y == pos.y) {
                    isPresent = true;
                    break;
                }
            }

            if (!isPresent) {
                throw new RuntimeException("Invalid possible positions!!!");
            }
        }
    }

    private static void validateDiags(List<Diag> diags) {
        int kingsCnt = 0;
        for (Diag d : diags) {
            kingsCnt+=d.pointCnt;
        }

        if (kingsCnt != n) {
            throw new RuntimeException("Invalid!!!");
        }

        for (Point king : kings) {
            int validDiags = 0;
            for (Diag d : diags) {
                if (d.cord.x - king.x == -d.dir * (d.cord.y - king.y)) {
                    validDiags++;
                }
            }
            if (validDiags != 1) {
                throw new RuntimeException("Invalid!!!");
            }
        }
    }
}
