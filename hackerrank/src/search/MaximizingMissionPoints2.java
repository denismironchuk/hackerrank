package search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MaximizingMissionPoints2 {
    private static int dLat;
    private static int dLong;
    private static City[] latCities;
    private static int[][] segmentLevels;
    private static int[][] segments;

    private static class City {
        private int latitude;
        private int longitude;
        private int height;
        private int points;

        public City(final int latitude, final int longitude, final int height, final int points) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.height = height;
            this.points = points;
        }

        public int getHeight() {
            return height;
        }

        public int getLatitude() {
            return latitude;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("D:\\maxMisPoints.txt"));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        dLat = Integer.parseInt(tkn1.nextToken());
        dLong = Integer.parseInt(tkn1.nextToken());
        int maxLat = -1;
        List<City> cities = new ArrayList<>();

        Date start = new Date();
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
        cities.sort(Comparator.comparingInt(City::getLatitude));
        latCities = new City[n];
        for (int i = 0; i < n; i++) {
            latCities[i] = cities.get(i);
        }

        segmentLevels = new int[20][n];
        segments = new int[4 * n][3];

        fillLevels(1, 0, 0, n - 1);
        Date end = new Date();



        System.out.println(end.getTime() - start.getTime() + "ms");
    }

    private static void fillLevels(int v, int level, int tl, int tr) {
        segments[v][0] = level;
        segments[v][1] = tl;
        segments[v][2] = tr;

        if (tr == tl) {
            segmentLevels[level][tr] = latCities[tr].longitude;
        } else {
            int tm = (tr + tl) / 2;
            fillLevels(2*v, level + 1, tl, tm);
            fillLevels(2*v+1,level + 1, tm + 1, tr);

            int ptr1 = tl;
            int ptr2 = tm + 1;
            int index = tl;

            for (; ptr1 < tm + 1 && ptr2 < tr + 1; index++) {
                if (segmentLevels[level + 1][ptr1] < segmentLevels[level + 1][ptr2]) {
                    segmentLevels[level][index] = segmentLevels[level + 1][ptr1];
                    ptr1++;
                } else {
                    segmentLevels[level][index] = segmentLevels[level + 1][ptr2];
                    ptr2++;
                }
            }

            for (; ptr1 < tm + 1; index++) {
                segmentLevels[level][index] = segmentLevels[level + 1][ptr1];
                ptr1++;
            }

            for (; ptr2 < tr + 1; index++) {
                segmentLevels[level][index] = segmentLevels[level + 1][ptr2];
                ptr2++;
            }
        }
    }
}
