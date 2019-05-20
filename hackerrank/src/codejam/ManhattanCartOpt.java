package codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map.Entry;

public class ManhattanCartOpt {
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

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t <= T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int p = Integer.parseInt(tkn.nextToken());
            int q = Integer.parseInt(tkn.nextToken());

            List<Rect> rects = new ArrayList<>();
            List<Line> horizontals = new ArrayList<>();
            List<Line> verticals = new ArrayList<>();
            TreeSet<Integer> verticalCords = new TreeSet<>();

            for (int i = 0; i < p; i++) {
                StringTokenizer pTkn = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(pTkn.nextToken());
                int y = Integer.parseInt(pTkn.nextToken());
                char dir = pTkn.nextToken().charAt(0);
                Rect rect = null;

                switch (dir) {
                    case 'N':
                        rect = new Rect(new Point(0, q), new Point(q, y + 1));
                        break;
                    case 'S':
                        rect = new Rect(new Point(0, y - 1), new Point(q, 0));
                        break;
                    case 'E':
                        rect = new Rect(new Point(x + 1, q), new Point(q, 0));
                        break;
                    case 'W':
                        rect = new Rect(new Point(0, q), new Point(x - 1, 0));
                        break;
                    default:
                        break;
                }

                rects.add(rect);
                horizontals.addAll(rect.getHorizontalLines());
                List<Line> vLines = rect.getVerticalLines();
                verticals.addAll(vLines);
                verticalCords.add(vLines.get(0).end.x);
                verticalCords.add(vLines.get(1).end.x);
            }

            int[] cordPos = new int[q + 1];
            int verticalLinesCnt = verticalCords.size();
            List<Point>[] poinPositions = new List[verticalLinesCnt];
            int c = 0;
            for (int cord : verticalCords) {
                poinPositions[c] = new ArrayList<>();
                cordPos[cord] = c;
                c++;
            }

            Set<Point> processedPoints = new HashSet<>();
            for (Line vert : verticals) {
                List<Point> vertPoints = poinPositions[cordPos[vert.start.x]];
                for (Line hor : horizontals) {
                    Point intersectionPoint = Line.intersectVertHor(vert, hor);
                    if (null != intersectionPoint && !processedPoints.contains(intersectionPoint)) {
                        vertPoints.add(intersectionPoint);
                        processedPoints.add(intersectionPoint);
                    }
                }
            }

            List<int[]> updates = new ArrayList<>();
            for (int i = 0; i < verticalLinesCnt; i++) {
                int pointCnt = poinPositions[i].size();
                poinPositions[i].sort(Comparator.comparingInt(Point::getY));
                updates.add(new int[pointCnt * 4]);
            }

            for (Rect r : rects) {
                int startCol = cordPos[r.leftDown.x];
                int endCol = cordPos[r.rightDown.x];

                for (int i = startCol; i <= endCol; i++) {
                    int startRow = r.rightDown.y;
                    int endRow = r.rightUp.y;
                    int lastIndex = poinPositions[i].size() - 1;
                    int startIndex = findNearestGreaterEq(poinPositions[i], startRow, 0, lastIndex);
                    int endIndex = findNearestLowerEq(poinPositions[i], endRow, 0, lastIndex);
                    updateInterval(updates.get(i), 1, 0, lastIndex, startIndex, endIndex);
                }
            }

            List<Point> candidates0 = new ArrayList<>();
            int maxCnt = 0;
            for (int i = 0; i < verticalLinesCnt; i++) {
                List<Point> points = poinPositions[i];
                int[] update = updates.get(i);

                for (int j = 0; j < points.size(); j++) {
                    Point point = points.get(j);
                    point.rects = getVal(update, 1, 0, points.size() - 1, j);
                    candidates0.add(point);
                    if (maxCnt < point.rects) {
                        maxCnt = point.rects;
                    }
                }
            }

            List<Point> candidates = new ArrayList<>();
            for (Point cand : candidates0) {
                if (cand.rects == maxCnt) {
                    candidates.add(cand);
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

    private static int findNearestGreaterEq(List<Point> points, int y, int startPos, int endPos) {
        if  (startPos == endPos) {
            return points.get(startPos).getY() >= y ? startPos : -1;
        }

        int mid = (startPos + endPos) / 2;
        if (points.get(mid).getY() >= y) {
            return findNearestGreaterEq(points, y, startPos, mid);
        } else {
            return findNearestGreaterEq(points, y, mid + 1, endPos);
        }
    }

    private static int findNearestLowerEq(List<Point> points, int y, int startPos, int endPos) {
        if  (startPos == endPos) {
            return points.get(startPos).getY() <= y ? startPos : -1;
        }

        int mid = 1 + (startPos + endPos) / 2;
        if (points.get(mid).getY() <= y) {
            return findNearestLowerEq(points, y, mid, endPos);
        } else {
            return findNearestLowerEq(points, y, startPos, mid - 1);
        }
    }

    private static int getVal(int[] updates, int v, int l, int r, int pos) {
        if (l == r) {
            return updates[v];
        } else {
            int mid = (l + r) / 2;
            if (pos <= mid) {
                return getVal(updates, v * 2, l, mid, pos) + updates[v];
            } else {
                return getVal(updates, v * 2 + 1, mid + 1, r, pos) + updates[v];
            }
        }
    }

    private static void updateInterval(int[] updates, int v, int l, int r, int intL, int intR) {
        if (intL > intR) {
            return;
        }

        if (l == intL && r == intR) {
            updates[v]++;
        } else {
            int mid = (l + r) / 2;
            updateInterval(updates, v * 2,        l,       mid, intL,                    Math.min(mid, intR));
            updateInterval(updates, v * 2 + 1, mid + 1, r,   Math.max(mid + 1, intL), intR);
        }
    }
}
