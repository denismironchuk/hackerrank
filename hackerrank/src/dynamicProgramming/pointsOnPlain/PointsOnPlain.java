package dynamicProgramming.pointsOnPlain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;

public class PointsOnPlain {
    private static int FINAL_STATE = -1;
    private static long MOD = 1000000000 + 7;
    private static long[] FACT = new long[17];
    static {
        FACT[0] = 1;
        for (int i = 1; i < 17; i++) {
            FACT[i] = (FACT[i - 1] * i) % MOD;
        }
    }

    private static int pow2(int pow) {
        int res = 1;
        for (int i = 0; i < pow; i++) {
            res *= 2;
        }
        return res;
    }

    private static class MyPoint {
        private int num;
        private int x;
        private int y;

        private int[] states = new int[pow2(n - 1)];
        private int statesCnt = 0;

        public MyPoint(int num, int x, int y) {
            this.num = pow2(num);
            this.x = x;
            this.y = y;
        }

        public void addLine(int line) {
            states[statesCnt] = line;
            statesCnt++;
        }
    }

    private static MyPoint[] points;
    private static int n;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 0; t < T; t++) {
                n = Integer.parseInt(br.readLine());
                FINAL_STATE = pow2(n) - 1;
                points = new MyPoint[n];
                for (int i = 0; i < n; i++) {
                    StringTokenizer pointTkn = new StringTokenizer(br.readLine());
                    int x = Integer.parseInt(pointTkn.nextToken());
                    int y = Integer.parseInt(pointTkn.nextToken());
                    points[i] = new MyPoint(i, x, y);
                }

                int[] colinearSubsets = new int[FINAL_STATE + 1];
                int subsetsCnt = getCollinearSubsets(0, 0, 1, colinearSubsets, 0, new LinkedList<>());
                int[] isCollinear = new int[FINAL_STATE + 1];
                for (int i = 0; i < subsetsCnt; i++) {
                    isCollinear[colinearSubsets[i]] = 1;
                }
                int[] minLinesCnt = new int[FINAL_STATE + 1];
                int[] minWaysCnt = new int[FINAL_STATE + 1];
                int[] processed = new int[FINAL_STATE + 1];
                count(FINAL_STATE, isCollinear, processed, minLinesCnt, minWaysCnt);

                System.out.printf("%s %s\n", minLinesCnt[FINAL_STATE], (minWaysCnt[FINAL_STATE] * FACT[minLinesCnt[FINAL_STATE]]) % MOD);
            }
        }
    }

    private static void count(int state, int[] isCollinear, int[] processed, int[] minLinesCnt, int[] minWaysCnt) {
        if (processed[state] == 1) {
            return;
        }

        if (isCollinear[state] == 1) {
            processed[state] = 1;
            minLinesCnt[state] = 1;
            minWaysCnt[state] = 1;
            return;
        }

        int stateTmp = state;
        int pointIndex = 0;

        while (stateTmp % 2 == 0) {
            pointIndex++;
            stateTmp /= 2;
        }

        MyPoint pivotPoint = points[pointIndex];
        minLinesCnt[state] = Integer.MAX_VALUE;
        Set<Integer> locallyProcessedStates = new HashSet<>();
        for (int i = 0; i < pivotPoint.statesCnt; i++) {
            int collinearSubset = pivotPoint.states[i];
            int nextState = state & ~collinearSubset;
            if (locallyProcessedStates.contains(nextState)) {
                continue;
            }

            count(nextState, isCollinear, processed, minLinesCnt, minWaysCnt);
            if (minLinesCnt[state] > minLinesCnt[nextState] + 1) {
                minLinesCnt[state] = minLinesCnt[nextState] + 1;
                minWaysCnt[state] = minWaysCnt[nextState];
            } else if (minLinesCnt[state] == minLinesCnt[nextState] + 1) {
                minWaysCnt[state] += minWaysCnt[nextState];
                minWaysCnt[state] %= MOD;
            }
            locallyProcessedStates.add(nextState);
        }

        processed[state] = 1;
    }

    private static int getCollinearSubsets(int index, int state, int mul, int[] colinearSubsets, int colinearSubsetsCnt, LinkedList<MyPoint> subsetPoints) {
        if (index == n) {
            if (state != 0 && isColinearSubset(subsetPoints)) {
                for (MyPoint p : subsetPoints) {
                    p.addLine(state);
                }
                colinearSubsets[colinearSubsetsCnt] = state;
                return colinearSubsetsCnt + 1;
            } else {
                return colinearSubsetsCnt;
            }
        }

        colinearSubsetsCnt = getCollinearSubsets(index + 1, state, mul * 2, colinearSubsets, colinearSubsetsCnt, subsetPoints);

        subsetPoints.add(points[index]);
        colinearSubsetsCnt = getCollinearSubsets(index + 1, state + mul, mul * 2, colinearSubsets, colinearSubsetsCnt, subsetPoints);
        subsetPoints.removeLast();

        return colinearSubsetsCnt;
    }

    private static boolean isColinearSubset(LinkedList<MyPoint> subsetPoints) {
        if (subsetPoints.size() <= 2) {
            return true;
        }

        Iterator<MyPoint> itr = subsetPoints.iterator();
        MyPoint p1 = itr.next();
        MyPoint p2 = itr.next();

        while (itr.hasNext()) {
            MyPoint p3 = itr.next();
            if (((p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x)) != 0) {
                return false;
            }
        }

        return true;
    }
}
