package sorting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CountRadixSort {
    private static final int n = 20;
    private static final int digits = 20;
    private static final int max = 100;

    private static int[] countSort(int[] arr) {
        int[] counts = new int[max];

        for (int i = 0; i < n; i++) {
            counts[arr[i]]++;
        }

        for (int i = 1; i < max; i++) {
            counts[i] += counts[i - 1];
        }

        int[] res = new int[n];

        for (int i = n - 1; i > -1; i--) {
            res[counts[arr[i]] - 1] = arr[i];
            counts[arr[i]]--;
        }

        return res;
    }

    private static int[][] countSort(int[][] arr, int pos) {
        int[] counts = new int[max];

        for (int i = 0; i < n; i++) {
            counts[arr[i][pos]]++;
        }

        for (int i = 1; i < max; i++) {
            counts[i] += counts[i - 1];
        }

        int[][] res = new int[n][digits];

        for (int i = n - 1; i > -1; i--) {
            res[counts[arr[i][pos]] - 1] = arr[i];
            counts[arr[i][pos]]--;
        }

        return res;
    }

    private static void test() {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int)(max * Math.random());
            System.out.printf("%3d ", arr[i]);
        }
        System.out.println();

        int[] res = countSort(arr);
        for (int i = 0; i < n; i++) {
            System.out.printf("%3d ", res[i]);
        }
        System.out.println("\n==============");

        int[][] unsorted = new int[n][digits];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < digits; j++) {
                unsorted[i][j] = (int)(10 * Math.random());
                System.out.print(unsorted[i][j]);
            }
            System.out.println();
        }
        System.out.println("==============");

        for (int pos = digits - 1; pos > -1; pos--) {
            unsorted = countSort(unsorted, pos);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < digits; j++) {
                System.out.print(unsorted[i][j]);
            }
            System.out.println();
        }
        System.out.println("==============");
    }

    public static void main(String[] args) throws IOException {
        test();
    }
}
