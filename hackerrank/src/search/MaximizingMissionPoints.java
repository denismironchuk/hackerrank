package search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class MaximizingMissionPoints {
    public static final int DISP = 1000000000;
    private static int dLat;
    private static int dLong;
    private static TreapNode[] treapSegments;
    private static int cloneCnt = 0;

    public static class TreapNode {
        private int x;
        private int y;
        private TreapNode left;
        private TreapNode right;
        private TreapNode parent;
        private long maxPoints = Long.MIN_VALUE;

        private City city;

        public TreapNode(final City city) {
            this.x = city.longitude;
            this.y = (int)(DISP * Math.random());
            this.city = city;
            city.treaps.add(this);
        }

        public TreapNode(final int x, final int y, final City city) {
            this.x = x;
            this.y = y;
            this.city = city;
            city.treaps.add(this);
        }

        public TreapNode clone() {
            cloneCnt++;
            TreapNode clone = new TreapNode(x, y, city);
            if (left != null) {
                clone.setLeft(left.clone());
            }
            if (right != null) {
                clone.setRight(right.clone());
            }

            return clone;
        }

        public void setLeft(final TreapNode left) {
            this.left = left;
            if (null != left) {
                left.parent = this;
            }
        }

        public void setRight(final TreapNode right) {
            this.right = right;
            if (null != right) {
                right.parent = this;
            }
        }

        public void updateMaxPoint(long newMaxPoint) {
            this.maxPoints = Math.max(this.maxPoints, newMaxPoint);
            if (parent != null) {
                parent.updateMaxPoint(this.maxPoints);
            }
        }

        @Override
        public String toString() {
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"x\":").append(x);
            json.append(",\"y\":").append(y);
            if (null != left) {
                json.append(",\"left\":").append(left);
            }
            if (null != right) {
                json.append(",\"right\":").append(right);
            }
            json.append("}");
            return json.toString();
        }
    }

    private static TreapNode[] split(TreapNode nd, int val) {
        if (nd == null) {
            return new TreapNode[2];
        }

        if (nd.x < val) {
            TreapNode[] splitRes = split(nd.right, val);
            nd.setRight(splitRes[0]);
            nd.maxPoints = Math.max(nd.maxPoints, Math.max(nd.left == null ? Long.MIN_VALUE : nd.left.maxPoints, nd.right == null ? Long.MIN_VALUE : nd.right.maxPoints));
            return new TreapNode[]{nd, splitRes[1]};
        } else {
            TreapNode[] splitRes = split(nd.left, val);
            nd.setLeft(splitRes[1]);
            nd.maxPoints = Math.max(nd.maxPoints, Math.max(nd.left == null ? Long.MIN_VALUE : nd.left.maxPoints, nd.right == null ? Long.MIN_VALUE : nd.right.maxPoints));
            return new TreapNode[] {splitRes[0], nd};
        }
    }

    private static TreapNode merge(TreapNode nd1, TreapNode nd2) {
        if (nd1 == null) {
            return nd2;
        }

        if (nd2 == null) {
            return nd1;
        }

        if (nd1.y > nd2.y) {
            nd1.setRight(merge(nd1.right, nd2));
            nd1.maxPoints = Math.max(nd1.maxPoints, Math.max(nd1.left == null ? Long.MIN_VALUE : nd1.left.maxPoints, nd1.right == null ? Long.MIN_VALUE : nd1.right.maxPoints));
            return nd1;
        } else {
            nd2.setLeft(merge(nd2.left, nd1));
            nd2.maxPoints = Math.max(nd2.maxPoints, Math.max(nd2.left == null ? Long.MIN_VALUE : nd2.left.maxPoints, nd2.right == null ? Long.MIN_VALUE : nd2.right.maxPoints));
            return nd2;
        }
    }

    private static TreapNode union(TreapNode nd1, TreapNode nd2) {
        if (nd1 == null) {
            return nd2;
        }
        if (nd2 == null) {
            return nd1;
        }

        if (nd1.y > nd2.y) {
            TreapNode[] splitRes = split(nd2, nd1.x);
            nd1.setLeft(union(nd1.left, splitRes[0]));
            nd1.setRight(union(nd1.right, splitRes[1]));
            return nd1;
        } else {
            TreapNode[] splitRes = split(nd1, nd2.x);
            nd2.setLeft(union(nd2.left, splitRes[0]));
            nd2.setRight(union(nd2.right, splitRes[1]));
            return nd2;
        }
    }

    private static class City {
        private int latitude;
        private int longitude;
        private int height;
        private int points;

        private List<TreapNode> treaps = new ArrayList<>();

        public City(final int latitude, final int longitude, final int height, final int points) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.height = height;
            this.points = points;
        }

        public int getHeight() {
            return height;
        }
    }

    public static void main(String[] args) throws IOException {
        List<TreapNode> test = new ArrayList<>();
        Date testStart = new Date();
        City testCity = new City(1,2,3,4);
        for (int i = 0; i < 4000000; i++) {
            test.add(new TreapNode(2, 2, testCity));
        }
        Date testEnd = new Date();

        System.out.println("Test took " + (testEnd.getTime() - testStart.getTime()) + "ms");

        Date d1 = new Date();
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new FileReader("D:\\maxMisPoints.txt"));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        dLat = Integer.parseInt(tkn1.nextToken());
        dLong = Integer.parseInt(tkn1.nextToken());
        int maxLat = -1;

        List<City> cities = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            StringTokenizer cityTkn = new StringTokenizer(br.readLine());
            int latitude = Integer.parseInt(cityTkn.nextToken());
            int longitude = Integer.parseInt(cityTkn.nextToken());
            int height = Integer.parseInt(cityTkn.nextToken());
            int points = Integer.parseInt(cityTkn.nextToken());
            cities.add(new City(latitude, longitude, height, points));
            if (latitude > maxLat) {
                maxLat = latitude;
            }
        }

        Date d2 = new Date();

        TreapNode[] treaps = new TreapNode[maxLat + 1];
        for (City city : cities) {
            treaps[city.latitude] = new TreapNode(city);
        }

        Date d3 = new Date();
        treapSegments = new TreapNode[(maxLat + 1) * 4];
        buildSegmentTree(treaps, 1, 0, maxLat);


        int insts = 0;
        for (City c : cities) {
            insts += c.treaps.size();
        }

        System.out.println("Treap insts = " + insts);

        Date d4 = new Date();
        cities.sort(Comparator.comparingInt(City::getHeight).reversed());
        Date d5 = new Date();
        for (City city : cities) {
            int l = Math.max(1, city.latitude - dLat);
            int r = Math.min(maxLat, city.latitude + dLat);
            long points = maxPoint(city, 1, 0, maxLat, l, r);
            long maxPoint = (points == Long.MIN_VALUE ? 0 : points) + city.points;
            for (TreapNode node : city.treaps) {
                node.updateMaxPoint(maxPoint);
            }
        }
        Date d6 = new Date();
        System.out.println(d2.getTime() - d1.getTime() + "ms");
        System.out.println(d3.getTime() - d2.getTime() + "ms");
        System.out.println(d4.getTime() - d3.getTime() + "ms");
        System.out.println(d5.getTime() - d4.getTime() + "ms");
        System.out.println(d6.getTime() - d5.getTime() + "ms");
        System.out.println("Total time " + (d6.getTime() - d1.getTime()) + "ms");
        System.out.println("CLone cnt = " + cloneCnt);
        System.out.println(treapSegments[1].maxPoints);
    }

    private static long maxPoint(City city, int v, int tl, int tr, int l, int r) {
        if (l > r) {
            return Long.MIN_VALUE;
        }

        if (l == tl && r == tr) {
            TreapNode nd = treapSegments[v];
            int longLow = city.longitude - dLong;
            int longUp = city.longitude + dLong + 1;
            TreapNode[] splitRes1 = split(nd, longLow);
            TreapNode[] splitRes2 = split(splitRes1[1], longUp);
            long maxPoint = null == splitRes2[0] ? Long.MIN_VALUE : splitRes2[0].maxPoints;
            merge(splitRes1[0], merge(splitRes2[0], splitRes2[1]));
            return maxPoint;
        }

        int tm = (tl + tr) / 2;
        return Math.max(maxPoint(city, 2 * v,  tl, tm, l, Math.min(r, tm)), maxPoint(city, 2 * v + 1,  tm + 1, tr, Math.max(l,tm+1), r));
    }

    private static void buildSegmentTree(TreapNode[] treaps, int v, int tl, int tr) {
        if (tl == tr) {
            treapSegments[v] = treaps[tl];
        } else {
            int tm = (tl + tr) / 2;
            buildSegmentTree(treaps, v*2, tl, tm);
            buildSegmentTree(treaps, v*2 + 1, tm + 1, tr);
            TreapNode nd1 = treapSegments[v*2];
            TreapNode nd2 = treapSegments[v*2 + 1];
            treapSegments[v] = union(null == nd1 ? null : nd1.clone(), null == nd2 ? null : nd2.clone());
            //treapSegments[v] = union(nd1, nd2);
        }
    }
}
