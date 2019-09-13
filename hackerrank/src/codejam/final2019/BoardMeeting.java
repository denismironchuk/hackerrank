package codejam.final2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BoardMeeting {
    private static final int N_MAX = 10;
    private static final int BOARD_LIMS = 100;
    private static int n;
    private static Point[] kings;
    private static List<Diag> diags;

    private static class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
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
        private int pointCnt;

        public Diag(Point cord, int pointCnt) {
            this.cord = cord;
            this.pointCnt = pointCnt;
        }

        @Override
        public String toString() {
            return String.format("%s %s", cord, pointCnt);
        }
    }

    private static boolean isOccupiedCell(int x, int y) {
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

    private static int generateRandomBoardCord() {
        return (int)(2 * BOARD_LIMS * Math.random()) - BOARD_LIMS;
    }

    private static int countTotalMovesToPoint(int x, int y) {
        int res = 0;

        for (Point king : kings) {
            res += Math.max(Math.abs(king.x - x), Math.abs(king.y - y));
        }

        return res;
    }

    private static void generateRandomPoints() {
        for (int i = 0; i < n; i++) {
            int x = generateRandomBoardCord();
            int y = generateRandomBoardCord();

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

            int x = Integer.parseInt(pointTkn.nextToken());
            int y = Integer.parseInt(pointTkn.nextToken());

            kings[i] = new Point(x, y);
        }
    }

    private static Diag findDiagWithPoint(int x, int y) {
        for (Diag diag : diags) {
            if (diag.cord.x == x && diag.cord.y == y) {
                return diag;
            }
        }

        return null;
    }

    private static void printBoard() {
        for (int row = BOARD_LIMS; row > -BOARD_LIMS - 1; row--) {
            System.out.printf("%3d | ", row);
            for (int col = -BOARD_LIMS; col < BOARD_LIMS + 1; col++) {
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

    private static boolean isPoint(int x, int y) {
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
            n = (int) (Math.random() * N_MAX) + 1;
            //n = Integer.parseInt(br.readLine());
            kings = new Point[n];

            //inputPoints(br);
            generateRandomPoints();
            //printBoard();

            //System.out.println("================");
            int dist102 = countTotalMovesToPoint(-(BOARD_LIMS + 2), BOARD_LIMS + 2);
            int dist101 = countTotalMovesToPoint(-(BOARD_LIMS + 1), BOARD_LIMS + 1);
            //int prevDiff = dist102 - dist101;

            int estimatedKingsAmount = dist102 - dist101;

            //System.out.printf("Kings amnt = %s\n", estimatedKingsAmount);

            diags = new ArrayList<>();

            int foundKingsDiags = 0;

            int initPos = -(BOARD_LIMS + 1);
            int initMoves = dist101;
            int step = dist102 - dist101;

            while (foundKingsDiags != estimatedKingsAmount) {
                int changePos = findChangeStepPos(initPos, BOARD_LIMS + 1, initMoves, initPos, step);
                int currentDist = countTotalMovesToPoint(changePos, -changePos);
                int prevDist = initMoves - (changePos - initPos - 1) * step;
                int currentDiff = prevDist - currentDist;

                int decr = step - currentDiff;
                int testDist1 = countTotalMovesToPoint(changePos - 2, -changePos + 1);
                int testDist2 = countTotalMovesToPoint(changePos - 1, -changePos);
                int testDiff = testDist1 - testDist2;

                if (testDiff == step) {
                    diags.add(new Diag(new Point(changePos, -changePos + 1), decr)); //touches diag
                    foundKingsDiags += decr;
                    currentDiff -= decr;
                } else {
                    int amnt1 = step - testDiff;
                    diags.add(new Diag(new Point(changePos - 1, -changePos + 1), amnt1)); //crosses diag
                    foundKingsDiags += amnt1;

                    int amnt2 = decr - (amnt1 * 2);
                    if (amnt2 != 0) {
                        diags.add(new Diag(new Point(changePos, -changePos + 1), amnt2)); //touches diag
                        foundKingsDiags += amnt2;
                        currentDiff -= amnt2;
                    }
                }

                initPos = changePos;
                initMoves = currentDist;
                step = currentDiff;
            }

            //diags.forEach(System.out::println);
            validateDiags();
        }
    }

    private static int findChangeStepPos(int start, int end, int initMoves, int initPos, int step) {
        if (start == end) {
            return start;
        }

        int mid = start + ((end - start) / 2);
        int moves = countTotalMovesToPoint(mid, -mid);
        int expectedMoves = initMoves - (mid - initPos) * step;
        if (moves == expectedMoves) {
            return findChangeStepPos(mid + 1, end, initMoves, initPos, step);
        } else {
            return findChangeStepPos(start, mid, initMoves, initPos, step);
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
                if (d.cord.x - king.x == d.cord.y - king.y) {
                    validDiags++;
                }
            }
            if (validDiags != 1) {
                throw new RuntimeException("Invalid!!!");
            }
        }
    }
}
