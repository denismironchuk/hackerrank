package codejam.juggleStrugle.pt1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class JuggleStruggleSimple {

    private static class Point {
        private int num;
        private long x;
        private long y;

        private int sector;

        public Point(int num, long x, long y) {
            this.num = num;
            this.x = x;
            this.y = y;
        }

        public long getX() {
            return x;
        }

        public long getY() {
            return y;
        }

        public double getDist(Point p) {
            return Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
        }

        public int getSector() {
            return sector;
        }

        public void setSector(int sector) {
            this.sector = sector;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "num=" + num +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    private static class Line {
        private Point p1;
        private Point p2;

        private long A;
        private long B;
        private long C;

        public Line(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
            calcCoeficients();
        }

        private void calcCoeficients() {
            this.A = p2.y - p1.y;
            this.B = p1.x - p2.x;
            this.C = (p1.y * p2.x) - (p1.x * p2.y);
        }

        public int getPointSide(Point p) {
            return Long.compare(A * p.x + B * p.y, -C);
        }

        public Vector getVector() {
            return new Vector(this);
        }

        @Override
        public String toString() {
            return "Line{" +
                    "p1=" + p1 +
                    ", p2=" + p2 +
                    '}';
        }
    }

    public static class Vector {
        long x;
        long y;

        public Vector(Line line) {
            this.x = line.p2.x - line.p1.x;
            this.y = line.p2.y - line.p1.y;
        }

        public double scalarMul(Vector v) {
            return x * v.x + y * v.y;
        }

        public double area(Vector v) {
            return x * v.y - y * v.x;
        }

        public double getLen() {
            return Math.sqrt(x * x + y * y);
        }
    }

    private static class PointAndAngle implements Comparable<PointAndAngle> {
        private Point p;
        private double cos;
        private double sin;
        private boolean clockWise;
        private int heightSign;

        public PointAndAngle(Point p, double cos, double sin, int heightSign) {
            this.p = p;
            this.cos = cos;
            this.sin = sin;
            this.heightSign = heightSign;
        }

        @Override
        public int compareTo(PointAndAngle o) {
            int sideCompare = Integer.compare(o.heightSign, heightSign);
            if (sideCompare != 0) {
                return sideCompare;
            }

            int cosCompare = Double.compare(o.cos, cos);
            if (cosCompare != 0) {
                return cosCompare;
            }

            if (heightSign == 1) {
                return Double.compare(o.sin, sin);
            } else {
                return Double.compare(sin, o.sin);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t <= T; t++) {
            int n = Integer.parseInt(br.readLine());
            Set<Point> points = new HashSet<>();
            for (int i = 0; i < 2 * n; i++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                long x = Long.parseLong(tkn.nextToken());
                long y = Long.parseLong(tkn.nextToken());
                points.add(new Point(i, x, y));
            }

            List<Line> divideLines = new ArrayList<>();

            while (points.size() != 0) {
                Line divideLine = getDividingLineInitial(points);
                divideLines.add(divideLine);

                if (!validate(points, divideLine)) {
                    throw new RuntimeException();
                }

                points.remove(divideLine.p1);
                points.remove(divideLine.p2);
            }

            int[] pairs = new int[2 * n];

            for (Line line : divideLines) {
                pairs[line.p1.num] = line.p2.num;
                pairs[line.p2.num] = line.p1.num;
            }

            System.out.printf("Case #%s: ", t);
            for (int i = 0; i < 2 * n; i++) {
                System.out.printf("%s ", pairs[i] + 1);
            }
            System.out.println();
        }
    }

    private static boolean validate(Set<Point> points, Line line) {
        int leftCnt = 0;
        int rightCount = 0;

        for (Point p : points) {
            int side = line.getPointSide(p);

            if (side == -1) {
                leftCnt++;
            } else if (side == 1) {
                rightCount++;
            }
        }

        return leftCnt == rightCount;
    }

    /*private static Line getDividingLineInitial(Set<Point> points) {
        List<Point> pointsSorted = points.stream().sorted(Comparator.comparingLong(Point::getX)).collect(Collectors.toList());
        Point center = pointsSorted.get(0);
        Line line = null;

        for (int i = 1; i < pointsSorted.size(); i++) {
            Point p = pointsSorted.get(i);
            line = new Line(center, p);

            if (validate(points, line)) {
                break;
            }
        }

        return line;
    }*/

    private static Line getDividingLineInitial(Set<Point> points) {
        List<Point> pointsSorted = points.stream().sorted(Comparator.comparingLong(Point::getX)).collect(Collectors.toList());
        Point center = pointsSorted.get(0);
        List<PointAndAngle> pointAndAngles = new ArrayList<>();

        Line line = new Line(center, new Point(-1, center.x, center.y + 1));
        Vector v1 = line.getVector();

        for (int i = 1; i < pointsSorted.size(); i++) {
            Point p = pointsSorted.get(i);
            Vector v2 = new Line(center, p).getVector();
            pointAndAngles.add(new PointAndAngle(p, getAngleCos(v1, v2), getAngleSin(v1, v2), p.y < center.y ? -1 : 1));
        }

        Collections.sort(pointAndAngles);

        Point end = pointAndAngles.get(pointAndAngles.size() / 2).p;

        return new Line(center, end);
    }

    private static double getAngleCos(Vector v1, Vector v2) {
        return v1.scalarMul(v2) / v1.getLen() / v2.getLen();
    }

    private static double getAngleSin(Vector v1, Vector v2) {
        return v1.area(v2) / v1.getLen() / v2.getLen();
    }
}
