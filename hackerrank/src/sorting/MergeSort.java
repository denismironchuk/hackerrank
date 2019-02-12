package sorting;

public class MergeSort {
    private static void sort(int[] arr, int start, int end, int[] res) {
        if (start == end) {
            res[start] = arr[start];
            return;
        }

        int middle = (start + end) / 2;
        sort(arr, start, middle, res);
        sort(arr, middle + 1, end, res);

        for (int i = start; i <= middle; i++) {
            arr[i] = res[i];
        }

        for (int i = middle + 1; i <= end; i++) {
            arr[i] = res[i];
        }

        merge(arr, start, middle, middle + 1, end, res);
    }

    private static void merge(int[] arr, int start1, int end1, int start2, int end2, int[] res) {
        int index1 = start1;
        int index2 = start2;
        int resIndex = start1;

        for (; index1 <= end1 && index2 <= end2 && resIndex <= end2; resIndex++){
            res[resIndex] = Math.min(arr[index1], arr[index2]);
            if (res[resIndex] == arr[index1]) {
                index1++;
            } else {
                index2++;
            }
        }

        for (; index1 <= end1; resIndex++) {
            res[resIndex] = arr[index1];
            index1++;
        }

        for (; index2 <= end2; resIndex++) {
            res[resIndex] = arr[index2];
            index2++;
        }
    }

    private static void print(int[] arr) {
        for (int a : arr) {
            System.out.printf("%3d", a);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int n = 100;
        int max = 100;
        int[] arr = new int[n];

        for (int i = 0; i < n; i++) {
            arr[i] = (int) (max * Math.random());
        }

        print(arr);

        int[] res = new int[n];
        sort(arr, 0, n - 1, res);
        print(res);
    }
}
