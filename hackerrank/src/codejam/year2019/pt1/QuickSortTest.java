package codejam.year2019.pt1;

import java.util.Arrays;
import java.util.Date;

public class QuickSortTest {
    public static void main(String[] args) {
        while (true) {
            System.out.println("=========");
            int n = 100000;
            int[] a = new int[n];
            int[] aOrigin = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = (int) (50 * Math.random());
                aOrigin[i] = a[i];
            }
            //print(a);

            Date d1 = new Date();
            sort(a, 0, n - 1);
            Date d2 = new Date();
            Arrays.sort(aOrigin);
            Date d3 = new Date();

            System.out.println(d2.getTime() - d1.getTime() + "ms");
            System.out.println(d3.getTime() - d2.getTime() + "ms");

            for (int i = 0; i < n; i++) {
                if (a[i] != aOrigin[i]) {
                    throw new RuntimeException();
                }
            }
            //print(a);
        }
    }

    private static void print(int[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.printf("%s ", a[i]);
        }
        System.out.println();
    }

    public static void sort(int[] a, int start, int end) {
        if (start >= end) {
            return;
        }

        int newPivot = start;

        for (int i = start; i < end; i++) {
            if (a[i] < a[end]) {
                int tmp = a[i];
                a[i] = a[newPivot];
                a[newPivot] = tmp;
                newPivot++;
            }
        }

        int tmp = a[end];
        a[end] = a[newPivot];
        a[newPivot] = tmp;

        sort(a, start, newPivot - 1);
        sort(a, newPivot + 1, end);
    }
}
