package sorting;

import java.util.Arrays;

public class QuickSort {
    private static int partition(int[] arr, int start, int end) {
        int pivot = arr[end];
        int lessEnd = start - 1;
        for (int i = start; i <= end; i++) {
            if (arr[i] <= pivot) {
                lessEnd++;
                int tmp = arr[i];
                arr[i] = arr[lessEnd];
                arr[lessEnd] = tmp;
            }
        }

        return lessEnd == end ? lessEnd - 1 : lessEnd;
    }

    private static void sort(int[] arr, int start, int end) {
        if (start >= end) {
            return;
        }

        int middle = partition(arr, start, end);
        sort(arr, start, middle);
        sort(arr, middle + 1, end);
    }

    private static void print(int[] arr) {
        for (int a : arr) {
            System.out.printf("%3d", a);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        while (true) {
            int n = 20;
            int max = 10;
            int[] arr = new int[n];
            int[] arrEtl = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = (int) (max * Math.random());
                arrEtl[i] = arr[i];
            }

            print(arr);
            sort(arr, 0, n - 1);
            print(arr);

            Arrays.sort(arrEtl);
            print(arrEtl);
            System.out.println("================");
            if (!Arrays.equals(arr, arrEtl)) {
                throw new RuntimeException();
            }

        }
    }
}
