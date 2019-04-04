package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class BikeRacers {
    private static class Distance implements Comparable<Distance> {
        private int racerNum;
        private int bikeNum;
        private long dist;

        public Distance(int racerNum, int bikeNum, long dist) {
            this.racerNum = racerNum;
            this.bikeNum = bikeNum;
            this.dist = dist;
        }

        @Override
        public int compareTo(Distance o) {
            int distCompare = Long.compare(dist, o.dist);
            if (distCompare == 0) {
                int racerCompare = Integer.compare(racerNum, o.racerNum);
                if (racerNum == 0) {
                    return Integer.compare(bikeNum, o.bikeNum);
                } else {
                    return racerCompare;
                }
            } else {
                return distCompare;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        int m = Integer.parseInt(tkn1.nextToken());
        int k = Integer.parseInt(tkn1.nextToken());

        long[][] racers = new long[n][2];
        for (int i = 0; i < n; i++) {
            StringTokenizer racerTkn = new StringTokenizer(br.readLine());
            racers[i][0] = Long.parseLong(racerTkn.nextToken());
            racers[i][1] = Long.parseLong(racerTkn.nextToken());
        }

        long[][] bikes = new long[m][2];
        for (int i = 0; i < m; i++) {
            StringTokenizer bikeTkn = new StringTokenizer(br.readLine());
            bikes[i][0] = Long.parseLong(bikeTkn.nextToken());
            bikes[i][1] = Long.parseLong(bikeTkn.nextToken());
        }

        Distance[][] dists = new Distance[n][m];
        TreeSet<Distance> distsTree = new TreeSet<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                long dist = (racers[i][0] - bikes[j][0]) * (racers[i][0] - bikes[j][0]) +
                        (racers[i][1] - bikes[j][1]) * (racers[i][1] - bikes[j][1]);
                dists[i][j] = new Distance(i, j, dist);
                distsTree.add(dists[i][j]);
            }
        }

        Distance dist = null;
        for (int i = 0; i < k; i++) {
            dist = distsTree.first();
            for (Distance d : dists[dist.racerNum]) {
                distsTree.remove(d);
            }
            for (int j = 0; j < m; j++) {
                distsTree.remove(dists[j][dist.bikeNum]);
            }
        }

        System.out.println(dist.dist);
    }
}
