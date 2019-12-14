package codejam.juggleStrugle.pt1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class JuggleStruggleFinal {

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

        public double getLen() {
            return Math.sqrt(x * x + y * y);
        }
    }

    private static class PointAndAngle implements Comparable<PointAndAngle> {
        private Point p;
        private double angle;
        private boolean clockWise;

        public PointAndAngle(Point p, double angle) {
            this.p = p;
            this.angle = angle;
        }

        public PointAndAngle(Point p, double angle, boolean clockWise) {
            this.p = p;
            this.angle = angle;
            this.clockWise = clockWise;
        }

        @Override
        public int compareTo(PointAndAngle o) {
            return Double.compare(o.angle, angle);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t <= T; t++) {
            int n = Integer.parseInt(br.readLine());
            List<Point> points = new ArrayList<>();
            for (int i = 0; i < 2 * n; i++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                long x = Long.parseLong(tkn.nextToken());
                long y = Long.parseLong(tkn.nextToken());
                points.add(new Point(i, x, y));
            }

            Line divideLine = getDividingLineInitial(points);
            List<Point> sector1 = new ArrayList<>();
            List<Point> sector2 = new ArrayList<>();

            for (Point p : points) {
                int side = divideLine.getPointSide(p);
                if (side == 1) {
                    sector1.add(p);
                    p.sector = 1;
                } else if (side == -1) {
                    sector2.add(p);
                    p.sector = 2;
                }
            }

            List<Line> divideLines = new ArrayList<>();
            divideLines.add(divideLine);

            dividePoints(sector1, sector2, divideLines);

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

    private static void dividePoints(List<Point> sector1, List<Point> sector2, List<Line> divideLines) {
        List<Point> points = new ArrayList<>();

        points.addAll(sector1);
        points.addAll(sector2);

        Line divideLine = getDividingLine(points);
        divideLines.add(divideLine);

        List<Point> sector11 = new ArrayList<>();
        List<Point> sector21 = new ArrayList<>();

        List<Point> sector12 = new ArrayList<>();
        List<Point> sector22 = new ArrayList<>();

        for (Point p : sector1) {
            int side = divideLine.getPointSide(p);

            if (side == 1) {
                sector11.add(p);
                p.sector = 1;
            } else if (side == -1) {
                sector21.add(p);
                p.sector = 1;
            }
        }

        for (Point p : sector2) {
            int side = divideLine.getPointSide(p);

            if (side == 1) {
                sector22.add(p);
                p.sector = 2;
            } else if (side == -1) {
                sector12.add(p);
                p.sector = 2;
            }
        }

        if (sector11.size() != 0) {
            dividePoints(sector11, sector12, divideLines);
        }

        if (sector21.size() != 0) {
            dividePoints(sector21, sector22, divideLines);
        }
    }

    private static Line getDividingLineInitial(List<Point> points) {
        points.sort(Comparator.comparingLong(Point::getX));
        Point center = points.get(0);
        List<PointAndAngle> pointAndAngles = new ArrayList<>();

        Line line = new Line(center, new Point(-1, center.x, center.y + 1));
        Vector v1 = line.getVector();

        for (int i = 1; i < points.size(); i++) {
            Point p = points.get(i);
            Vector v2 = new Line(center, p).getVector();
            pointAndAngles.add(new PointAndAngle(p, getAngleCos(v1, v2)));
        }

        Collections.sort(pointAndAngles);

        Point end = pointAndAngles.get(pointAndAngles.size() / 2).p;

        return new Line(center, end);
    }

    /*private static Line getDividingLine(List<Point> points) {
        Point center = points.get((int)(Math.random() * points.size()));
        Point end = center;

        while (center.num == end.num) {
            int ind = (int)(Math.random() * points.size());
            end = points.get(ind);
        }

        Line line = new Line(center, end);
        Vector v1 = line.getVector();

        List<PointAndAngle> pointAndAngles = new ArrayList<>();

        int clockWiseCnt = 0;
        int contClockWiseCnt = 0;

        for (Point p : points) {
            if (line.getPointSide(p) == -1) {
                Vector v2 = new Line(p, center).getVector();
                pointAndAngles.add(new PointAndAngle(p, getAngleCos(v1, v2), false));
                contClockWiseCnt++;
            } else if (line.getPointSide(p) == 1) {
                Vector v2 = new Line(center, p).getVector();
                pointAndAngles.add(new PointAndAngle(p, getAngleCos(v1, v2), true));
                clockWiseCnt++;
            }
        }

        Collections.sort(pointAndAngles);
        int index = 0;
        boolean isClockWise = true;

        while (clockWiseCnt != contClockWiseCnt || center.sector == end.sector) {
            PointAndAngle curr = pointAndAngles.get(index);
            index++;
            end = curr.p;

            if (curr.clockWise) {
                if (isClockWise) {
                    clockWiseCnt--;
                    contClockWiseCnt++;
                } else {
                    isClockWise = !isClockWise;
                }
            } else {
                if (!isClockWise) {
                    contClockWiseCnt--;
                    clockWiseCnt++;
                } else {
                    isClockWise = !isClockWise;
                }
            }
        }

        return new Line(center, end);
    }*/

    private static Line getDividingLine(List<Point> points) {
        for (Point center : points) {
            for (Point end : points) {
                if (center.sector != end.sector) {
                    int leftCnt = 0;
                    int rightCnt = 0;

                    Line line = new Line(center, end);

                    for (Point p : points) {
                        int side = line.getPointSide(p);

                        if (side == 1) {
                            leftCnt++;
                        } else if (side == -1) {
                            rightCnt++;
                        }
                    }

                    if (leftCnt == rightCnt) {
                        return line;
                    }
                }
            }
        }

        throw new RuntimeException();
    }

    private static double getAngleCos(Vector v1, Vector v2) {
        return v1.scalarMul(v2) / v1.getLen() / v2.getLen();
    }
}
