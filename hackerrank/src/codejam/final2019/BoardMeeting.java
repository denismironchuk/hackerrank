package codejam.final2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BoardMeeting {
    private static final int N_MAX = 5;
    private static final int BOARD_LIMS = 5;
    private static int n;
    private static Point[] kings;
    private static List<Diag> diags;

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

    private static class Diag {
        private Point cord;
        private long pointCnt;
        /*
         1 = \
        -1 = /
         */
        private long dir;

        public Diag(Point cord, long pointCnt, long dir) {
            this.cord = cord;
            this.pointCnt = pointCnt;
            this.dir = dir;
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

    private static Diag findDiagWithPoint(long x, long y) {
        for (Diag diag : diags) {
            if (diag.cord.x == x && diag.cord.y == y) {
                return diag;
            }
        }

        return null;
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
        //while(true) {
            REQ_CNT = 0;
            n = (int) (Math.random() * N_MAX) + 1;
            //n = Integer.parseInt(br.readLine());
            //n = 10;
            kings = new Point[n];

            //inputPoints(br);
            generateRandomPoints();
            printBoard();

            diags = getDiags(1l);

            diags.forEach(System.out::println);
            validateDiags();
            System.out.println(REQ_CNT);
            System.out.println("================");
        //}
    }

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

    private static void validateDiags() {
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
