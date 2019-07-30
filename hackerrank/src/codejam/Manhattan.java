package codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Manhattan {
    private static class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x &&
                    y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
    private static class Rect {
        private Point upLeft;
        private Point upRight;
        private Point downLeft;
        private Point downRight;
        private Set<Integer> parents = new HashSet<>();

        public Rect(int upLeftX, int upLeftY, int downRightX, int downRightY) {
            this.upLeft = new Point(upLeftX, upLeftY);
            this.upRight = new Point(downRightX, upLeftY);
            this.downLeft = new Point(upLeftX, downRightY);
            this.downRight = new Point(downRightX, downRightY);
        }

        public Rect(Point upLeft, Point downRight) {
            this.upLeft = upLeft;
            this.upRight = new Point(downRight.x, upLeft.y);
            this.downLeft = new Point(upLeft.x, downRight.y);
            this.downRight = downRight;
        }

        public boolean isPointInRect(Point p) {
            return p.x >= upLeft.x && p.x <= upRight.x && p.y >= downLeft.y && p.y <= upLeft.y;
        }

        public Rect intersect(Rect rect) {
            int[] newUpLeftX = new int[3];
            newUpLeftX[0] = upLeft.x;
            newUpLeftX[1] = rect.upLeft.x;
            newUpLeftX[2] = upRight.x;

            int[] newUpLeftY = new int[3];
            newUpLeftY[0] = upLeft.y;
            newUpLeftY[1] = rect.upLeft.y;
            newUpLeftY[2] = downLeft.y;

            Arrays.sort(newUpLeftX);
            Arrays.sort(newUpLeftY);

            Point newUpLeft = new Point(newUpLeftX[1], newUpLeftY[1]);

            int[] newDownRightX = new int[3];
            newDownRightX[0] = downLeft.x;
            newDownRightX[1] = rect.downRight.x;
            newDownRightX[2] = downRight.x;

            int[] newDownRightY = new int[3];
            newDownRightY[0] = upRight.y;
            newDownRightY[1] = rect.downRight.y;
            newDownRightY[2] = downRight.y;

            Arrays.sort(newDownRightX);
            Arrays.sort(newDownRightY);

            Point newDownRight = new Point(newDownRightX[1], newDownRightY[1]);

            if (isPointInRect(newUpLeft)
                    && isPointInRect(newDownRight) && rect.isPointInRect(newUpLeft) && rect.isPointInRect(newDownRight)) {
                return new Rect(newUpLeft, newDownRight);
            }

            return null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Rect rect = (Rect) o;
            return Objects.equals(upLeft, rect.upLeft) &&
                    Objects.equals(upRight, rect.upRight) &&
                    Objects.equals(downLeft, rect.downLeft) &&
                    Objects.equals(downRight, rect.downRight);
        }

        @Override
        public int hashCode() {
            return Objects.hash(upLeft, upRight, downLeft, downRight);
        }

        @Override
        public String toString() {
            return "Rect{" +
                    "upLeft=" + upLeft +
                    ", downRight=" + downRight +
                    '}';
        }
    }

    /*public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            StringTokenizer r1Tkn = new StringTokenizer(br.readLine());
            int leftUpX = Integer.parseInt(r1Tkn.nextToken());
            int leftUpY = Integer.parseInt(r1Tkn.nextToken());
            int rightDpwnX = Integer.parseInt(r1Tkn.nextToken());
            int rightDpwnY = Integer.parseInt(r1Tkn.nextToken());
            Rect r1 = new Rect(leftUpX, leftUpY, rightDpwnX, rightDpwnY);

            StringTokenizer r2Tkn = new StringTokenizer(br.readLine());
            leftUpX = Integer.parseInt(r2Tkn.nextToken());
            leftUpY = Integer.parseInt(r2Tkn.nextToken());
            rightDpwnX = Integer.parseInt(r2Tkn.nextToken());
            rightDpwnY = Integer.parseInt(r2Tkn.nextToken());
            Rect r2 = new Rect(leftUpX, leftUpY, rightDpwnX, rightDpwnY);

            Rect intr = r1.intersect(r2);
            System.out.println(intr);
        }
    }*/

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int p = Integer.parseInt(tkn.nextToken());
            int q = Integer.parseInt(tkn.nextToken());
            List<Rect> rects = new ArrayList<>();
            Map<Rect, Integer> rectsCnt = new HashMap<>();

            for (int i = 0; i < p; i++) {
                StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(tkn2.nextToken());
                int y = Integer.parseInt(tkn2.nextToken());
                char dir = tkn2.nextToken().charAt(0);

                Rect r = null;
                switch (dir) {
                    case 'N':
                        r = new Rect(0, q, q, y + 1);
                        break;
                    case 'S':
                        r = new Rect(0, y - 1, q, 0);
                        break;
                    case 'E':
                        r = new Rect(x + 1, q, q, 0);
                        break;
                    case 'W':
                        r = new Rect(0, q, x - 1, 0);
                        break;
                }

                r.parents.add(i);
                if (rectsCnt.containsKey(r)) {
                    rectsCnt.put(r, rectsCnt.get(r) + 1);
                } else {
                    rectsCnt.put(r, 1);
                    rects.add(r);
                }
            }

            int rectsSize = rects.size();

            for (int i = 0; i < rectsSize; i++) {
                Rect r = rects.get(i);
                List<Rect> newRects = new ArrayList<>();
                for (int j = i + 1; j < rects.size(); j++) {
                    Rect r2 = rects.get(j);
                    if (r2.parents.contains(i)) {
                        continue;
                    }

                    int r2Cnt = rectsCnt.get(r2);
                    Rect intr = r.intersect(r2);
                    if (intr != null) {
                        intr.parents.addAll(r2.parents);
                        intr.parents.add(i);
                        if (rectsCnt.containsKey(intr)) {
                            rectsCnt.put(intr, rectsCnt.get(intr) + r2Cnt + 1);
                        } else {
                            rectsCnt.put(intr, r2Cnt + 1);
                            newRects.add(intr);
                        }
                    }
                }

                rects.addAll(newRects);
            }

            int max = 0;
            for (Map.Entry<Rect,Integer> entry : rectsCnt.entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                }
            }
            List<Rect> res = new ArrayList<>();
            for (Map.Entry<Rect,Integer> entry : rectsCnt.entrySet()) {
                if (entry.getValue() == max) {
                    res.add(entry.getKey());
                }
            }

            res.sort((r1, r2) -> Integer.compare(r1.upLeft.x, r2.upLeft.x));
            int x = res.get(0).upLeft.x;
            List<Rect> res2 = new ArrayList<>();
            for (Rect r : res) {
                if (r.upLeft.x == x) {
                    res2.add(r);
                }
            }

            res2.sort((r1, r2) -> Integer.compare(r1.downLeft.y, r2.downLeft.y));
            Point finalRes = res2.get(0).downLeft;
            System.out.printf("Case %s: %s %s\n", t + 1, finalRes.x, finalRes.y);
        }
    }
}
