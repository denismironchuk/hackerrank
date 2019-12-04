package codejam.juggleStrugle.pt1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class JuggleStruggle1Sol {
    private static class Point {
        private int num;
        private int x;
        private int y;

        public Point(int num, int x, int y) {
            this.num = num;
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        Comparator<Point> xComparator = Comparator.comparingInt(Point::getX);
        Comparator<Point> yComparator = Comparator.comparingInt(Point::getY);

        for (int t = 1; t <= T; t++) {
            int n = Integer.parseInt(br.readLine());
            List<Point> points = new ArrayList<>();
            for (int i = 0; i < 2 * n; i++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(tkn.nextToken());
                int y = Integer.parseInt(tkn.nextToken());
                points.add(new Point(i, x, y));
            }
            points.sort(xComparator);

            List<Point> startPoints = new ArrayList<>();
            List<Point> endPoints = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                startPoints.add(points.get(i));
                endPoints.add(points.get(n + i));
            }

            startPoints.sort(yComparator);
            endPoints.sort(yComparator.reversed());

            int[] res = new int[2 * n];

            for (int i = 0; i < n; i++) {
                res[startPoints.get(i).num] = endPoints.get(i).num;
                res[endPoints.get(i).num] = startPoints.get(i).num;
            }

            System.out.printf("Case #%s: ", t);
            for (int i = 0; i < 2 * n; i++) {
                System.out.printf("%s ", res[i] + 1);
            }
            System.out.println();
        }
    }
}
