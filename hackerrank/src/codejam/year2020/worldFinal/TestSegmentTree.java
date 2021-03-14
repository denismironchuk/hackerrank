package codejam.year2020.worldFinal;

import java.util.Arrays;

public class TestSegmentTree {
    private static int n = 100;
    private static long[] a = new long[n];
    private static long[] segmentTree = new long[4 * n];
    private static long[] updates = new long[4 * n];

    public static void main(String[] args) {
        for (int i = 0; i < n; i++) {
            a[i] = (long)(100 * Math.random());
        }
        Arrays.fill(segmentTree, Long.MAX_VALUE);
        initSegmentTree(1, 0, n - 1);

        int itr = 0;

        while (true) {
            int intervalStart = (int)(Math.random() * n);
            int intervalEnd = intervalStart + (int)(Math.random() * (n - intervalStart));
            long updVal = (long)(Math.random() * 20);

            System.out.printf("Start: %s\nEnd: %s\nUpdVal: %s\n\n", intervalStart, intervalEnd, updVal);

            update(updVal, intervalStart, intervalEnd);
            updateTrivial(updVal, intervalStart, intervalEnd);

            int intervalStartForMin = (int)(Math.random() * n);
            int intervalEndForMin = intervalStart + (int)(Math.random() * (n - intervalStart));

            long min = getMinValue(intervalStartForMin, intervalEndForMin);
            long minTrivial = getMinValueTrivial(intervalStartForMin, intervalEndForMin);

            if (min != minTrivial) {
                throw new RuntimeException(String.valueOf(itr));
            }
            itr++;
        }
    }

    private static void initSegmentTree(int p, int start, int end) {
        if (start == end) {
            segmentTree[p] = a[start];
        } else {
            int middle = (start + end) / 2;
            initSegmentTree(2 * p, start, middle);
            initSegmentTree(2 * p + 1, middle + 1, end);
            segmentTree[p] = Math.min(segmentTree[2 * p], segmentTree[2 * p + 1]);
        }
    }

    public static void updateTrivial(long updVal, int intrStart, int intrEnd) {
        for (int i = intrStart; i <= intrEnd; i++) {
            a[i] -= updVal;
        }
    }

    public static void update(long updVal, int intrStart, int intrEnd) {
        update(1, updVal, 0, n - 1, intrStart, intrEnd);
    }

    private static void update(int p, long updVal, int start, int end, int intrStart, int intrEnd) {
        if (intrStart > intrEnd) {
            return;
        }

        if (start == intrStart && end == intrEnd) {
            updates[p] += updVal;
            segmentTree[p] -= updVal;
        } else {
            int middle = (start + end) / 2;
            update(2 * p, updVal, start, middle, intrStart, Math.min(intrEnd, middle));
            update(2 * p + 1, updVal, middle + 1, end, Math.max(middle + 1, intrStart), intrEnd);
            segmentTree[p] = Math.min(segmentTree[2 * p], segmentTree[2 * p + 1]) - updates[p];
        }
    }

    public static long getMinValue(int intrStart, int intrEnd) {
        return getMinValue(1, 0, n - 1, intrStart, intrEnd);
    }

    public static long getMinValueTrivial(int intrStart, int intrEnd) {
        long res = Long.MAX_VALUE;
        for (int i = intrStart; i <= intrEnd; i++) {
            res = Math.min(res, a[i]);
        }
        return res;
    }

    private static long getMinValue(int p, int start, int end, int intrStart, int intrEnd) {
        if (intrStart > intrEnd) {
            return Long.MAX_VALUE;
        }

        if (start == intrStart && end == intrEnd) {
            return segmentTree[p];
        } else {
            int middle = (start + end) / 2;
            return Math.min(getMinValue(2 * p, start, middle, intrStart, Math.min(intrEnd, middle)),
                    getMinValue(2 * p + 1, middle + 1, end, Math.max(middle + 1, intrStart), intrEnd)) - updates[p];
        }
    }
}
