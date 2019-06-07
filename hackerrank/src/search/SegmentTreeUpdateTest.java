package search;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class SegmentTreeUpdateTest {
    /*public static void main(String[] args) {
        int n = 10;
        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = Math.random() > 0.3 ? 1 : 0;
            System.out.printf("%s ", a[i]);
        }

        System.out.println();

        int[] segTree = new int[4 * n];
        int[] setValues = new int[4 * n];
        buildTree(segTree, a, 1, 0, n - 1);
        Arrays.fill(setValues, -1);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            int startCandidate = (int)(Math.random() * n);
            int endCandidate = (int)(Math.random() * n);

            int start = Math.min(startCandidate, endCandidate);
            int end = Math.max(startCandidate, endCandidate);

            int valToSet = Math.random() > 0.5 ? 0 : 1;

            System.out.printf("Update info - (start - %s, end - %s, val - %s)\n", start, end, valToSet);

            setValToInterval(segTree, 1, 0, n - 1, start, end, valToSet, setValues);
            int[] updated = getUpdatedTree(segTree, setValues, n);
            updateIntervalTrivial(start, end, valToSet, a);

            startCandidate = (int)(Math.random() * n);
            endCandidate = (int)(Math.random() * n);

            start = Math.min(startCandidate, endCandidate);
            end = Math.max(startCandidate, endCandidate);

            int onesOpt = countOnes(segTree, setValues, 1, 0, n - 1, start, end, -1);
            int onesTrivial = countOnesTrivial(start, end, a);

            for (int i = 0; i < n; i++) {
                System.out.printf("%s ", updated[i]);
            }

            System.out.println();

            for (int i = 0; i < n; i++) {
                System.out.printf("%s ", a[i]);
            }

            System.out.println("\n==============");

            if (onesOpt != onesTrivial) {
                System.out.println(start + " " + end);
                throw new RuntimeException("My excp!!!!");
            }

            for (int i = 0; i < n; i++) {
                if (updated[i] != a[i]) {
                    throw new RuntimeException("my excp!!!");
                }
            }
        }
    }*/

    public static void main(String[] args) {
        int n = 10;
        int[] a = new int[]{1, 1, 0, 1, 1, 1, 0, 1, 0, 1};
        int[] segTree = new int[4 * n];
        int[] setValues = new int[4 * n];
        buildTree(segTree, a, 1, 0, n - 1);
        Arrays.fill(setValues, -1);

        int start = 0;
        int end = 2;
        int valToSet = 0;

        setValToInterval(segTree, 1, 0, n - 1, start, end, valToSet, setValues);
        int[] updated = getUpdatedTree(segTree, setValues, n);
        updateIntervalTrivial(start, end, valToSet, a);

        start = 1;
        end = 3;
        valToSet = 0;

        setValToInterval(segTree, 1, 0, n - 1, start, end, valToSet, setValues);
        updated = getUpdatedTree(segTree, setValues, n);
        updateIntervalTrivial(start, end, valToSet, a);

        start = 0;
        end = 9;

        int onesOpt = countOnes(segTree, setValues, 1, 0, n - 1, start, end, -1);
        int onesTrivial = countOnesTrivial(start, end, a);

        System.out.println(onesOpt);
        System.out.println(onesTrivial);
    }

    private static int countOnesTrivial(int start, int end, int[] a) {
        int res = 0;
        for (int i = start; i <= end; i++) {
            res += a[i];
        }
        return res;
    }

    private static void updateIntervalTrivial(int start, int end, int valToSet, int[] a) {
        for (int i = start; i <= end; i++) {
            a[i] = valToSet;
        }
    }

    private static int[] getUpdatedTree(int[] segTree, int[] setVals, int n) {
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = countOnes(segTree, setVals, 1, 0, n - 1, i, i, setVals[1]);
        }
        return res;
    }

    private static void buildTree(int[] segTree, int[] data, int v, int l, int r) {
        if (l == r) {
            segTree[v] = data[l];
        } else {
            int mid = (l + r) / 2;
            buildTree(segTree, data, 2 * v, l, mid);
            buildTree(segTree, data, 2 * v + 1, mid + 1, r);
            segTree[v] = segTree[2 * v] + segTree[2 * v + 1];
        }
    }

    private static int countOnes(int[] segTree, int[] setValues, int v, int l, int r, int searchL, int searchR, int setVal) {
        if (searchL > searchR) {
            return 0;
        }

        setVal = setVal != -1 ? setVal : setValues[v];

        if (l == searchL && r == searchR) {
            return setVal == -1 ? segTree[v] : setVal * (r - l + 1);
        } else {
            int mid = (l + r) / 2;

            return countOnes(segTree, setValues, 2 * v, l, mid, searchL, Math.min(mid, searchR), setVal) +
                    countOnes(segTree, setValues, 2 * v + 1, mid + 1, r, Math.max(mid + 1, searchL), searchR, setVal);
        }
    }

    public static void setValToInterval(int[] segTree, int v, int l, int r, int updL, int updR, int setVal, int[] setValues) {
        if (updL > updR) {
            return;
        }

        if (l == updL && r == updR) {
            setValues[v] = setVal;
            segTree[v] = (r - l + 1) * setVal;
        } else {
            push(v, setValues);
            int mid = (l + r) / 2;

            setValToInterval(segTree, 2 * v, l, mid, updL, Math.min(mid, updR), setVal, setValues);
            setValToInterval(segTree, 2 * v + 1, mid + 1, r, Math.max(updL, mid + 1), updR, setVal, setValues);

            segTree[v] = segTree[2 * v] + segTree[2 * v + 1];
        }
    }

    private static void push(int v, int[] setValues) {
        if (setValues[v] != -1) {
            setValues[2 * v] = setValues[v];
            setValues[2 * v + 1] = setValues[v];
            setValues[v] = -1;
        }
    }
}