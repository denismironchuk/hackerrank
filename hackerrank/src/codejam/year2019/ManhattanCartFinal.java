package codejam.year2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ManhattanCartFinal {
    private static class Point {
        private int x;
        private int y;
        private int rects = 0;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Point point = (Point) o;
            return x == point.x &&
                    y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private static class Line {
        private Point start;
        private Point end;

        public Line(Point start, Point end) {
            if (isHorizontal(start, end)) {
                if (start.x < end.x) {
                    this.start = start;
                    this.end = end;
                } else {
                    this.start = end;
                    this.end = start;
                }
            } else {
                if (start.y < end.y) {
                    this.start = start;
                    this.end = end;
                } else {
                    this.start = end;
                    this.end = start;
                }
            }
        }

        public boolean isHorizontal(Point start, Point end) {
            return start.y == end.y;
        }

        public static Point intersectVertHor(Line vertical, Line horizontal) {
            if (vertical.start.x >= horizontal.start.x && vertical.start.x <= horizontal.end.x) {
                if (horizontal.start.y >= vertical.start.y && horizontal.start.y <= vertical.end.y) {
                    return new Point(vertical.start.x, horizontal.start.y);
                }
            }

            return null;
        }
    }

    private static class Rect {
        private Point leftUp;
        private Point rightUp;
        private Point leftDown;
        private Point rightDown;

        public Rect(Point leftUp, Point rightDown) {
            this.leftUp = leftUp;
            this.rightUp = new Point(rightDown.x, leftUp.y);
            this.leftDown = new Point(leftUp.x, rightDown.y);
            this.rightDown = rightDown;
        }

        public List<Line> getHorizontalLines() {
            return Arrays.asList(new Line(leftUp, rightUp), new Line(leftDown, rightDown));
        }

        public List<Line> getVerticalLines() {
            return Arrays.asList(new Line(leftUp, leftDown), new Line(rightUp, rightDown));
        }

        public boolean isPointInRect(Point p) {
            return p.x >= leftUp.x && p.x <= rightUp.x && p.y >= leftDown.y && p.y <= leftUp.y;
        }
    }

    private static class VertPoint implements Comparable<VertPoint> {
        private int yCord;
        private int rectCnt;

        public VertPoint(int yCord) {
            this.yCord = yCord;
        }

        @Override
        public int compareTo(VertPoint o) {
            return Integer.compare(yCord, o.yCord);
        }
    }

    private static class HorizPoint implements Comparable<HorizPoint> {
        private int xCord;
        private int rectCnt;

        public HorizPoint(int xCord) {
            this.xCord = xCord;
        }

        @Override
        public int compareTo(HorizPoint o) {
            return Integer.compare(xCord, o.xCord);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t <= T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int p = Integer.parseInt(tkn.nextToken());
            int q = Integer.parseInt(tkn.nextToken());

            List<Rect> northSouthRects = new ArrayList<>();
            List<Rect> eastWestRects = new ArrayList<>();
            TreeSet<Integer> verticalCords = new TreeSet<>();
            verticalCords.add(0);
            verticalCords.add(q);
            TreeSet<Integer> horizontalCords = new TreeSet<>();
            horizontalCords.add(0);
            horizontalCords.add(q);

            for (int i = 0; i < p; i++) {
                StringTokenizer pTkn = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(pTkn.nextToken());
                int y = Integer.parseInt(pTkn.nextToken());
                char dir = pTkn.nextToken().charAt(0);
                Rect rect = null;

                switch (dir) {
                    case 'N':
                        rect = new Rect(new Point(0, q), new Point(q, y + 1));
                        northSouthRects.add(rect);
                        verticalCords.add(q);
                        verticalCords.add(y + 1);
                        break;
                    case 'S':
                        rect = new Rect(new Point(0, y - 1), new Point(q, 0));
                        northSouthRects.add(rect);
                        verticalCords.add(y - 1);
                        verticalCords.add(0);
                        break;
                    case 'E':
                        rect = new Rect(new Point(x + 1, q), new Point(q, 0));
                        eastWestRects.add(rect);
                        horizontalCords.add(x + 1);
                        horizontalCords.add(q);
                        break;
                    case 'W':
                        rect = new Rect(new Point(0, q), new Point(x - 1, 0));
                        eastWestRects.add(rect);
                        horizontalCords.add(0);
                        horizontalCords.add(x - 1);
                        break;
                    default:
                        break;
                }
            }

            TreeSet<VertPoint> vertPointRects = new TreeSet<>();
            for (int yCord : verticalCords) {
                VertPoint vp = new VertPoint(yCord);
                Point point = new Point(0, yCord);
                for (Rect r : northSouthRects) {
                    if (r.isPointInRect(point)) {
                        vp.rectCnt++;
                    }
                }
                vertPointRects.add(vp);
            }

            TreeSet<HorizPoint> horizPointRects = new TreeSet<>();
            for (int xCord : horizontalCords) {
                HorizPoint hp = new HorizPoint(xCord);
                Point point = new Point(xCord, 0);
                for (Rect r : eastWestRects) {
                    if (r.isPointInRect(point)) {
                        hp.rectCnt++;
                    }
                }
                horizPointRects.add(hp);
            }

            List<Point> points = new ArrayList<>();
            int maxCnt = 0;
            for (VertPoint vp : vertPointRects) {
                for (HorizPoint hp : horizPointRects) {
                    Point point = new Point(hp.xCord, vp.yCord);
                    points.add(point);
                    point.rects = hp.rectCnt + vp.rectCnt;
                    if (point.rects > maxCnt) {
                        maxCnt = point.rects;
                    }
                }
            }

            List<Point> candidates = new ArrayList<>();
            for (Point point : points) {
                if (point.rects == maxCnt) {
                    candidates.add(point);
                }
            }

            candidates.sort(Comparator.comparing(Point::getX));
            Point minXPoint = candidates.get(0);
            List<Point> candidates2 = new ArrayList<>();
            for (Point point : candidates) {
                if (point.x == minXPoint.x) {
                    candidates2.add(point);
                } else {
                    break;
                }
            }

            candidates2.sort(Comparator.comparing(Point::getY));
            Point minYPoint = candidates2.get(0);
            System.out.printf("Case #%s: %s %s\n", t, minYPoint.getX(), minYPoint.getY());
        }
    }
}
