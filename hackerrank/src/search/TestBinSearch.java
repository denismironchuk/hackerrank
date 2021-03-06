package search;

public class TestBinSearch {
    private static int getLatLowIndex(int[] arr, int tl, int tr, int lowerBound) {
        if (tr == tl) {
            return tr;
        } else {
            int tm = (tr + tl) / 2;
            if (arr[tm] < lowerBound) {
                return getLatLowIndex(arr, tm + 1, tr, lowerBound);
            } else {
                return getLatLowIndex(arr, tl, tm, lowerBound);
            }
        }
    }

    private static int getLatUpperIndex(int[] arr, int tl, int tr, int upperBound) {
        if (tr == tl) {
            return tr;
        } else {
            int tm = 1 + (tr + tl) / 2;
            if (arr[tm] > upperBound) {
                return getLatUpperIndex(arr, tl, tm - 1, upperBound);
            } else {
                return getLatUpperIndex(arr, tm, tr, upperBound);
            }
        }
    }

    private static int findCityPos(int[] cities, int tl, int tr, int city) {
        if (tr == tl) {
            return tr;
        } else {
            int tm = (tr + tl) / 2;
            if (cities[tm] >= city) {
                return findCityPos(cities, tl, tm, city);
            } else {
                return findCityPos(cities, tm + 1, tr, city);
            }
        }
    }

    public static void main(String[] args) {
        /*int[] arr = new int[] {1,4,7,9,13,24,24,24,25,26,46,46,57,57,57,678,4443};
        System.out.println(getLatLowIndex(arr, 0, arr.length - 1, -90));
        System.out.println(getLatUpperIndex(arr, 0, arr.length - 1, 57999));
        int[] arr2 = new int[] {1,4,7,9,13,24,25,26,46,57,678,4443};
        System.out.println(findCityPos(arr2, 0, arr2.length - 1, 4));*/
        int[] arr3 = new int[] {1,3,6,8,10};
        System.out.println(getLatUpperIndex(arr3, 0, arr3.length - 1, 1));
        System.out.println(getLatLowIndex(arr3, 0, arr3.length - 1, 9));
        System.out.println(findCityPos(arr3, 0, arr3.length - 1, 10));
    }
}
