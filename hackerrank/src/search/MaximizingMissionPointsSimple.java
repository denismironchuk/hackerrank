package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class MaximizingMissionPointsSimple {
    private static int dLat;
    private static int dLong;

    private static class City {
        private int latitude;
        private int longitude;
        private int height;
        private int points;
        private long accumulatedPoints = Long.MIN_VALUE;

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
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        dLat = Integer.parseInt(tkn1.nextToken());
        dLong = Integer.parseInt(tkn1.nextToken());
        List<City> cities = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            StringTokenizer cityTkn = new StringTokenizer(br.readLine());
            int latitude = Integer.parseInt(cityTkn.nextToken());
            int longitude = Integer.parseInt(cityTkn.nextToken());
            int height = Integer.parseInt(cityTkn.nextToken());
            int points = Integer.parseInt(cityTkn.nextToken());
            cities.add(new City(latitude, longitude, height, points));
        }

        cities.sort(Comparator.comparingInt(City::getHeight).reversed());
        long result = Long.MIN_VALUE;
        for (City city : cities) {
            int latLowerBound = city.latitude - dLat;
            int latUpperBound = city.latitude + dLat;

            int longLowerBound = city.longitude - dLong;
            int longUpperBound = city.longitude + dLong;

            long max = Long.MIN_VALUE;
            for (City c2 : cities) {
                if (!c2.equals(city) && c2.latitude >= latLowerBound
                        && c2.latitude <= latUpperBound && c2.longitude >= longLowerBound && c2.longitude <= longUpperBound) {
                    max = Math.max(max, c2.accumulatedPoints);
                }
            }

            if (max == Long.MIN_VALUE) {
                city.accumulatedPoints = city.points;
            } else {
                city.accumulatedPoints = max + city.points;
            }

            System.out.println(city.accumulatedPoints);

            if (city.accumulatedPoints > result) {
                result = city.accumulatedPoints;
            }
        }

        System.out.println(result);
    }
}
