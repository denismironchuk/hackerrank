package dynamicProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;

public class PointsOnPlain {
    private static long MOD = 1000000000 + 7;
    private static int FINAL_STATE = -1;
    private static Set<Integer> TWO_POW = new HashSet<>();
    private static long[] FACT = new long[17];
    static {
        for (int i = 0; i < 17; i++) {
            TWO_POW.add(pow2(i));
        }

        FACT[0] = 1;
        for (int i = 1; i < 17; i++) {
            FACT[i] = (FACT[i - 1] * i) % MOD;
        }
    }

    private static class MyPoint {
        private int num;
        private int x;
        private int y;

        public MyPoint(int num, int x, int y) {
            this.num = pow2(num);
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyPoint myPoint = (MyPoint) o;
            return x == myPoint.x &&
                    y == myPoint.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private static int pow2(int pow) {
        int res = 1;
        for (int i = 0; i < pow; i++) {
            res *= 2;
        }
        return res;
    }

    private static class Line {
        private Set<MyPoint> points = new HashSet<>();
        private int binState = -1;

        public void addPoint(MyPoint point) {
            points.add(point);
        }

        private int getBinaryState() {
            if (binState == -1) {
                binState = 0;
                for (MyPoint point : points) {
                    binState += point.num;
                }
            }
            return binState;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Line line = (Line) o;
            return Objects.equals(points, line.points);
        }

        @Override
        public int hashCode() {
            return Objects.hash(points);
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 0; t < T; t++) {
                int n = Integer.parseInt(br.readLine());
                FINAL_STATE = pow2(n) - 1;
                MyPoint[] points = new MyPoint[n];
                for (int i = 0; i < n; i++) {
                    StringTokenizer pointTkn = new StringTokenizer(br.readLine());
                    int x = Integer.parseInt(pointTkn.nextToken());
                    int y = Integer.parseInt(pointTkn.nextToken());
                    points[i] = new MyPoint(i, x, y);
                }

                Set<Line> lines = new HashSet<>();

                for (int i = 0; i < n - 1; i++) {
                    MyPoint p1 = points[i];
                    for (int j = i + 1; j < n; j++) {
                        MyPoint p2 = points[j];
                        Line line = new Line();

                        line.addPoint(p1);
                        line.addPoint(p2);

                        for (int k = 0; k < n; k++) {
                            MyPoint p3 = points[k];

                            if (!line.points.contains(p3)) {
                                if (((p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x)) == 0) {
                                    line.addPoint(p3);
                                }
                            }
                        }

                        lines.add(line);
                    }
                }

                for (MyPoint p : points) {
                    Line line = new Line();
                    line.addPoint(p);
                    lines.add(line);
                }

                Set<Integer> states = new HashSet<>();
                states.add(0);

                Map<Integer, Integer> statesMinLinesCnt = new HashMap<>();
                statesMinLinesCnt.put(0, 0);

                Map<Integer, Long> statesWaysCnt = new HashMap<>();
                statesWaysCnt.put(0, 1l);

                Set<Integer> newStates = new HashSet<>();
                Map<Integer, Integer> newStatesMinLinesCnt = new HashMap<>();
                Map<Integer, Long> newStatesWaysCnt = new HashMap<>();

                for (Line line : lines) {
                    newStates.addAll(states);
                    newStatesMinLinesCnt.putAll(statesMinLinesCnt);
                    newStatesWaysCnt.putAll(statesWaysCnt);

                    boolean nonSinglePointLine = line.points.size() > 1;

                    for (Integer state : states) {
                        if (nonSinglePointLine) {
                            if (TWO_POW.contains((state & line.getBinaryState()) ^ line.getBinaryState())) {
                                continue;
                            }
                        }

                        int minLinesCnt = statesMinLinesCnt.get(state);
                        long waysCnt  = statesWaysCnt.get(state);

                        int newState = line.getBinaryState() | state;

                        if (newStates.contains(newState)) {
                            int existingCnt = newStatesMinLinesCnt.get(newState);
                            if (existingCnt < minLinesCnt + 1) {
                                newStatesMinLinesCnt.put(newState, existingCnt);
                                newStatesWaysCnt.put(newState, newStatesWaysCnt.get(newState));
                            } else if (existingCnt > minLinesCnt + 1) {
                                newStatesMinLinesCnt.put(newState, minLinesCnt + 1);
                                newStatesWaysCnt.put(newState, waysCnt);
                            } else {
                                newStatesMinLinesCnt.put(newState, minLinesCnt + 1);
                                newStatesWaysCnt.put(newState, (newStatesWaysCnt.get(newState) + waysCnt) % MOD);
                            }
                        } else {
                            newStatesMinLinesCnt.put(newState, minLinesCnt + 1);
                            newStatesWaysCnt.put(newState, waysCnt);
                        }

                        newStates.add(newState);
                    }

                    Set<Integer> tmpStates = states;
                    Map<Integer, Integer> tmpStatesMinLinesCnt = statesMinLinesCnt;
                    Map<Integer, Long> tmpStatesWaysCnt = statesWaysCnt;

                    states = newStates;
                    statesMinLinesCnt = newStatesMinLinesCnt;
                    statesWaysCnt = newStatesWaysCnt;

                    newStates = tmpStates;
                    newStatesMinLinesCnt = tmpStatesMinLinesCnt;
                    newStatesWaysCnt = tmpStatesWaysCnt;
                }

                int minLines = statesMinLinesCnt.get(FINAL_STATE);
                long waysCnt = statesWaysCnt.get(FINAL_STATE);
                System.out.printf("%s %s\n", minLines, (waysCnt * FACT[minLines]) % MOD);
            }
        }
    }
}
