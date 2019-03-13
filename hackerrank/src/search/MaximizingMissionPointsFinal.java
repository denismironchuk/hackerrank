package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class MaximizingMissionPointsFinal {
    private static int dLat;
    private static int dLong;
    private static City[] latCities;
    private static City[][] latSegmentLevels;
    private static LatSegm[] latSegments;

    private static class City {
        private int latitude;
        private int longitude;
        private int height;
        private long points;
        private List<LatSegm> segments = new ArrayList<>();

        public City(final int latitude, final int longitude, final int height, final long points) {
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

    private static class LatSegm {
        private int level;
        private int tl;
        private int tr;
        private long[] segmentTree;

        public LatSegm(int level, int tl, int tr) {
            this.level = level;
            this.tl = tl;
            this.tr = tr;
            this.segmentTree = new long[4 * (tr - tl + 1)];
            Arrays.fill(segmentTree, Long.MIN_VALUE);
        }
    }

    private static List<City> generateCities(int n, int maxLat, int maxLong, int maxHeight) {
        int[] availableLats = new int[maxLat + 2];
        int[] availableLongs = new int[maxLong + 2];
        int[] availableHeights = new int[maxHeight + 2];

        List<City> cities = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int lat = (int)(Math.random() * maxLat) + 1;
            int longit = (int)(Math.random() * maxLong) + 1;
            int height = (int)(Math.random() * maxHeight) + 1;

            while (availableLats[lat] == 1 || availableLongs[longit] == 1 || availableHeights[height] == 1) {
                lat = (int)(Math.random() * maxLat) + 1;
                longit = (int)(Math.random() * maxLong) + 1;
                height = (int)(Math.random() * maxHeight) + 1;
            }

            availableLats[lat] = 1;
            availableLongs[longit] = 1;
            availableHeights[height] = 1;

            cities.add(new City(lat, longit, height, (int)(Math.random() * 20)));
        }

        return cities;
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
            long points = Long.parseLong(cityTkn.nextToken());
            cities.add(new City(latitude, longitude, height, points));
        }

        cities.sort(Comparator.comparingInt(City::getLatitude));
        latCities = new City[n];
        for (int i = 0; i < n; i++) {
            latCities[i] = cities.get(i);
        }

        latSegmentLevels = new City[20][n];
        latSegments = new LatSegm[4 * n];

        fillLevels(1, 0, 0, n - 1);
        cities.sort(Comparator.comparingInt(City::getHeight).reversed());

        for (City city : cities) {
            int lowerBound = city.latitude - dLat;
            int upperBound = city.latitude + dLat;
            int lowIndex = getLatLowIndex(latCities, 0, n - 1, lowerBound);
            int upperIndex = getLatUpperIndex(latCities, 0, n - 1, upperBound);

            long newPoints = processLat(city, 1, 0, n - 1, lowIndex, upperIndex);
            if (newPoints == Long.MIN_VALUE) {
                newPoints = city.points;
            } else {
                newPoints += city.points;
            }


            updateTrees(city, newPoints);
        }

        System.out.println(latSegments[1].segmentTree[1]);
    }

    private static void updateTrees(City city, long newPoints) {
        for (LatSegm segment : city.segments) {
            int pos = findCityPos(latSegmentLevels[segment.level], segment.tl, segment.tr, city);
            updateTree(segment.segmentTree, 1, segment.tl, segment.tr, newPoints, pos);
        }
    }

    private static void updateTree(long[] segmentTree, int v, int tl, int tr, long newPoints, int pos) {
        if (tr == tl && tr == pos) {
            segmentTree[v] = Math.max(segmentTree[v], newPoints);
        } else {
            int tm = (tr + tl) / 2;
            if (pos <= tm) {
                updateTree(segmentTree, 2 * v, tl, tm, newPoints, pos);
            } else {
                updateTree(segmentTree, 2 * v + 1, tm + 1, tr, newPoints, pos);
            }
            segmentTree[v] = Math.max(segmentTree[2 * v], segmentTree[2 * v + 1]);
        }
    }

    private static long processLat(City city, int v, int tl, int tr, int l, int r) {
        if (l > r) {
            return Long.MIN_VALUE;
        }

        if (tl == l && tr == r) {
            int lowerBound = city.longitude - dLong;
            int upperBound = city.longitude + dLong;

            int lowIndex = getLongLowIndex(latSegmentLevels[latSegments[v].level], latSegments[v].tl, latSegments[v].tr, lowerBound);
            int upperIndex = getLongUpperIndex(latSegmentLevels[latSegments[v].level], latSegments[v].tl, latSegments[v].tr, upperBound);

            return lowIndex == -1 || upperIndex == -1 ? Long.MIN_VALUE : processLong(latSegments[v], 1, latSegments[v].tl, latSegments[v].tr, lowIndex, upperIndex);
        } else {
            int tm = (tl + tr) / 2;
            return Math.max(processLat(city, 2 * v, tl, tm, l, Math.min(r, tm)),
                    processLat(city, 2 * v + 1, tm + 1, tr, Math.max(l,tm+1), r));
        }
    }

    private static long processLong(LatSegm latSegm, int v, int tl, int tr, int l, int r) {
        if (l > r) {
            return Long.MIN_VALUE;
        }

        if (tl == l && tr == r) {
            return latSegm.segmentTree[v];
        } else {
            int tm = (tl + tr) / 2;
            return Math.max(processLong(latSegm, 2 * v, tl, tm, l, Math.min(r, tm)),
                    processLong(latSegm, 2 * v + 1, tm + 1, tr, Math.max(l,tm+1), r));
        }
    }

    private static void fillLevels(int v, int level, int tl, int tr) {
        latSegments[v] = new LatSegm(level, tl, tr);

        if (tr == tl) {
            latSegmentLevels[level][tr] = latCities[tr];
            latCities[tr].segments.add(latSegments[v]);
        } else {
            int tm = (tr + tl) / 2;
            fillLevels(2*v, level + 1, tl, tm);
            fillLevels(2*v+1,level + 1, tm + 1, tr);

            int ptr1 = tl;
            int ptr2 = tm + 1;
            int index = tl;

            for (; ptr1 < tm + 1 && ptr2 < tr + 1; index++) {
                if (latSegmentLevels[level + 1][ptr1].longitude < latSegmentLevels[level + 1][ptr2].longitude) {
                    latSegmentLevels[level][index] = latSegmentLevels[level + 1][ptr1];
                    ptr1++;
                } else {
                    latSegmentLevels[level][index] = latSegmentLevels[level + 1][ptr2];
                    ptr2++;
                }
                latSegmentLevels[level][index].segments.add(latSegments[v]);
            }

            for (; ptr1 < tm + 1; index++) {
                latSegmentLevels[level][index] = latSegmentLevels[level + 1][ptr1];
                latSegmentLevels[level][index].segments.add(latSegments[v]);
                ptr1++;
            }

            for (; ptr2 < tr + 1; index++) {
                latSegmentLevels[level][index] = latSegmentLevels[level + 1][ptr2];
                latSegmentLevels[level][index].segments.add(latSegments[v]);
                ptr2++;
            }
        }
    }

    private static int findCityPos(City[] cities, int tl, int tr, City city) {
        if (tr == tl) {
            return tr;
        } else {
            int tm = (tr + tl) / 2;
            if (cities[tm].longitude >= city.longitude) {
                return findCityPos(cities, tl, tm, city);
            } else {
                return findCityPos(cities, tm + 1, tr, city);
            }
        }
    }

    private static int getLatLowIndex(City[] arr, int tl, int tr, int lowerBound) {
        if (tr == tl) {
            return tr;
        } else {
            int tm = (tr + tl) / 2;
            if (arr[tm].latitude < lowerBound) {
                return getLatLowIndex(arr,tm + 1, tr, lowerBound);
            } else {
                return getLatLowIndex(arr,tl, tm, lowerBound);
            }
        }
    }

    private static int getLatUpperIndex(City[] arr, int tl, int tr, int upperBound) {
        if (tr == tl) {
            return tr;
        } else {
            int tm = 1 + (tr + tl) / 2;
            if (arr[tm].latitude > upperBound) {
                return getLatUpperIndex(arr, tl, tm - 1, upperBound);
            } else {
                return getLatUpperIndex(arr, tm, tr, upperBound);
            }
        }
    }

    private static int getLongLowIndex(City[] arr, int tl, int tr, int lowerBound) {
        if (tr == tl) {
            return arr[tr].longitude < lowerBound ? -1 : tr;
        } else {
            int tm = (tr + tl) / 2;
            if (arr[tm].longitude < lowerBound) {
                return getLongLowIndex(arr,tm + 1, tr, lowerBound);
            } else {
                return getLongLowIndex(arr,tl, tm, lowerBound);
            }
        }
    }

    private static int getLongUpperIndex(City[] arr, int tl, int tr, int upperBound) {
        if (tr == tl) {
            return arr[tr].longitude > upperBound ? -1 : tr;
        } else {
            int tm = 1 + (tr + tl) / 2;
            if (arr[tm].longitude > upperBound) {
                return getLongUpperIndex(arr, tl, tm - 1, upperBound);
            } else {
                return getLongUpperIndex(arr, tm, tr, upperBound);
            }
        }
    }
}
