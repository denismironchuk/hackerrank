package codejam.year2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map.Entry;

public class ManhattanCartRand2 {
    private static class Point {
        private int x;
        private int y;

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

    public static void main(String[] args) throws IOException {
        int T = 100;

        Date start = new Date();
        for (int t = 1; t <= T; t++) {
            int p = 500;
            int q = 100000;

            List<Rect> rects = new ArrayList<>();
            List<Line> horizontals = new ArrayList<>();
            List<Line> verticals = new ArrayList<>();

            for (int i = 0; i < p; i++) {
                int x = (int)(q * Math.random());
                int y = (int)(q * Math.random());
                int dirInt = (int)(4 * Math.random());

                Rect rect = null;

                switch (dirInt) {
                    case 0:
                        rect = new Rect(new Point(0, q), new Point(q, y + 1));
                        break;
                    case 1:
                        rect = new Rect(new Point(0, y - 1), new Point(q, 0));
                        break;
                    case 2:
                        rect = new Rect(new Point(x + 1, q), new Point(q, 0));
                        break;
                    case 3:
                        rect = new Rect(new Point(0, q), new Point(x - 1, 0));
                        break;
                    default:
                        break;
                }

                rects.add(rect);
                horizontals.addAll(rect.getHorizontalLines());
                verticals.addAll(rect.getVerticalLines());
            }

            Set<Point> intersectionPoints = new HashSet<>();
            for (Line vert : verticals) {
                for (Line hor : horizontals) {
                    Point intersectionPoint = Line.intersectVertHor(vert, hor);
                    if (null != intersectionPoint) {
                        intersectionPoints.add(intersectionPoint);
                    }
                }
            }

            Map<Point, Integer> pointCnt = new HashMap<>();
            int maxCnt = 0;
            for (Point point : intersectionPoints) {
                int cnt = 0;
                for (Rect r : rects) {
                    if (r.isPointInRect(point)) {
                        cnt++;
                    }
                }
                pointCnt.put(point, cnt);
                if (cnt > maxCnt) {
                    maxCnt = cnt;
                }
            }

            List<Point> candidates = new ArrayList<>();
            for (Entry<Point, Integer> entry : pointCnt.entrySet()) {
                if (entry.getValue() == maxCnt) {
                    candidates.add(entry.getKey());
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
        Date end = new Date();
        System.out.println(end.getTime() - start.getTime() + "ms");
    }
}
