package dynamicProgramming.pointsOnPlain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;

public class PointsOnPlainCnt {
    private static long MOD = 1000000000 + 7;
    private static int FINAL_STATE = -1;

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

                int[] states = new int[pow2(16) + 1];
                int statesCnt = 0;

                states[statesCnt] = 0;
                statesCnt++;

                int[] processedStates = new int[pow2(16) + 1];
                processedStates[0] = 1;

                int[] statesMinLinesCnt = new int[pow2(16) + 1];
                statesMinLinesCnt[0] = 0;


                for (Line line : lines) {
                    int statesCnt_ = statesCnt;

                    for (int i = 0; i < statesCnt_; i++) {
                        int state = states[i];
                        int minLinesCnt = statesMinLinesCnt[state];
                        int newState = line.getBinaryState() | state;

                        if (processedStates[newState] == 1) {
                            statesMinLinesCnt[newState] = Math.min(statesMinLinesCnt[newState], minLinesCnt + 1);
                        } else {
                            statesMinLinesCnt[newState] = minLinesCnt + 1;
                            processedStates[newState] = 1;
                            states[statesCnt] = newState;
                            statesCnt++;
                        }
                    }
                }

                int minLines = statesMinLinesCnt[FINAL_STATE];
                System.out.printf("%s\n", minLines);
            }
        }
    }
}
