package kickstart.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

public class Festival {

    static class Point implements Comparable<Point> {
        private int index;
        private int day;
        private long happiness;
        private int ending;

        public Point(int index, int day, long happiness, int ending) {
            this.index = index;
            this.day = day;
            this.happiness = happiness;
            this.ending = ending;
        }

        @Override
        public int compareTo(Point o) {
            int cmp1 = Long.compare(happiness, o.happiness);
            if (cmp1 == 0) {
                return Integer.compare(index, o.index);
            } else {
                return cmp1;
            }
        }

        @Override
        public String toString() {
            return "Point{" +
                    "index=" + index +
                    ", day=" + day +
                    ", happiness=" + happiness +
                    ", ending=" + ending +
                    '}';
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int d = Integer.parseInt(tkn1.nextToken());
                int n = Integer.parseInt(tkn1.nextToken());
                int k = Integer.parseInt(tkn1.nextToken());
                List<Point> points = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                    long h = Long.parseLong(tkn2.nextToken());
                    int s = Integer.parseInt(tkn2.nextToken());
                    int e = Integer.parseInt(tkn2.nextToken());
                    points.add(new Point(i, s, h,0));
                    points.add(new Point(i, e, h,1));
                }
                points.sort(new Comparator<Point>() {
                    @Override
                    public int compare(Point o1, Point o2) {
                        int cmp1 = Integer.compare(o1.day, o2.day);
                        if (cmp1 != 0) {
                            return cmp1;
                        }
                        return Integer.compare(o1.ending, o2.ending);
                    }
                });
                TreeSet<Point> tree = new TreeSet<>();
                TreeSet<Point> available = new TreeSet<>();
                long res = -1;
                long currentHappiness = 0;
                for (Point p : points) {
                    if (p.ending == 0) {
                        if (tree.size() < k) {
                            currentHappiness += p.happiness;
                            tree.add(p);
                        } else {
                            Point smallest = tree.first();
                            if (smallest.happiness < p.happiness) {
                                currentHappiness += p.happiness - smallest.happiness;
                                tree.remove(smallest);
                                available.add(smallest);

                                tree.add(p);
                            } else {
                                available.add(p);
                            }
                        }
                    } else {
                        if (available.contains(p)) {
                            available.remove(p);
                        } else {
                            currentHappiness -= p.happiness;
                            tree.remove(p);

                            if (available.size() > 0) {
                                Point toAdd = available.last();
                                currentHappiness += toAdd.happiness;
                                tree.add(toAdd);
                                available.remove(toAdd);
                            }
                        }
                    }

                    if (currentHappiness > res) {
                        res = currentHappiness;
                    }
                }
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }
}
