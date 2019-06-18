package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class SortedSubsegments {
    private static long MIN = -1000000000;
    private static int n;
    private static int q;
    private static int k;
    private static long[] a;
    private static int[] aRel;
    private static int[][] intervals;
    private static int[] segTree;
    private static int[] setValues;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        n = Integer.parseInt(tkn1.nextToken());
        q = Integer.parseInt(tkn1.nextToken());
        k = Integer.parseInt(tkn1.nextToken());

        a = new long[n];
        aRel = new int[n];
        segTree = new int[4 * n];
        setValues = new int[4 * n];

        StringTokenizer tkn2 = new StringTokenizer(br.readLine());

        long min = Integer.MAX_VALUE;
        long max = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(tkn2.nextToken()) - MIN;
            if (a[i] > max) {
                max = a[i];
            }

            if (a[i] < min) {
                min = a[i];
            }
        }

        intervals = new int[q][2];

        for (int i = 0; i < q; i++) {
            StringTokenizer tkn3 = new StringTokenizer(br.readLine());
            intervals[i][0] = Integer.parseInt(tkn3.nextToken());
            intervals[i][1] = Integer.parseInt(tkn3.nextToken());
        }

        System.out.println(getValue(min, max) + MIN);
    }

    private static long getValue(long min, long max) {
        if (min == max) {
            return min;
        }

        long mid = (min + max) / 2;

        if (isGreater(mid)) {
            return getValue(mid + 1, max);
        } else {
            return getValue(min, mid);
        }
    }

    private static boolean isGreater(long mid) {
        for (int i = 0; i < n; i++) {
            aRel[i] = a[i] > mid ? 1 : 0;
        }

        buildTree(1, 0, n - 1);
        Arrays.fill(setValues, -1);

        for (int i = 0; i < q; i++) {
            int start = intervals[i][0];
            int end = intervals[i][1];

            int onesCnt = countOnes(1, 0, n - 1, start, end, -1);
            setValToInterval(1, 0, n - 1, start, end - onesCnt, 0);
            setValToInterval(1, 0, n - 1, end - onesCnt + 1, end, 1);
            //printSorterRelArray();
        }

        return countOnes(1, 0, n - 1, k, k, -1) == 1;
    }

    private static void printSorterRelArray() {
        for (int i = 0; i < n; i++) {
            int val = countOnes(1, 0, n - 1, i, i, -1);
            System.out.printf("%s ", val);
        }
        System.out.println();
    }

    private static void buildTree(int v, int l, int r) {
        if (l == r) {
            segTree[v] = aRel[l];
        } else {
            int mid = (l + r) / 2;
            buildTree(2 * v, l, mid);
            buildTree(2 * v + 1, mid + 1, r);
            segTree[v] = segTree[2 * v] + segTree[2 * v + 1];
        }
    }

    private static int countOnes(int v, int l, int r, int searchL, int searchR, int setVal) {
        if (searchL > searchR) {
            return 0;
        }

        setVal = setVal == -1 ? setValues[v] : setVal;

        if (l == searchL && r == searchR) {
            return setVal == -1 ? segTree[v] : setVal * (r - l + 1);
        } else {
            int mid = (l + r) / 2;

            return countOnes(2 * v, l, mid, searchL, Math.min(mid, searchR), setVal) +
                    countOnes(2 * v + 1, mid + 1, r, Math.max(mid + 1, searchL), searchR, setVal);
        }
    }

    public static void setValToInterval(int v, int l, int r, int updL, int updR, int setVal) {
        if (updL > updR) {
            return;
        }

        if (l == updL && r == updR) {
            setValues[v] = setVal;
            segTree[v] = (r - l + 1) * setVal;
        } else {
            int mid = (l + r) / 2;
            push(v, l, mid, r);

            setValToInterval(2 * v, l, mid, updL, Math.min(mid, updR), setVal);
            setValToInterval(2 * v + 1, mid + 1, r, Math.max(updL, mid + 1), updR, setVal);

            segTree[v] = segTree[2 * v] + segTree[2 * v + 1];
        }
    }

    private static void push(int v, int l, int mid, int r) {
        if (setValues[v] != -1) {
            setValues[2 * v] = setValues[v];
            segTree[2 * v] = (mid - l + 1) * setValues[v];

            setValues[2 * v + 1] = setValues[v];
            segTree[2 * v + 1] = (r - mid) * setValues[v];

            setValues[v] = -1;
        }
    }
}
