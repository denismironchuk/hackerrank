package codejam.pancakePyramid;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PyramidOptimal {
    private static final long MOD = 1000000007;

    private static int s;
    private static long[] p;

    private static class PArray {
        private Long val;
        private int pos;

        public PArray(Long val, int pos) {
            this.val = val;
            this.pos = pos;
        }

        public Long getVal() {
            return val;
        }

        public int getPos() {
            return pos;
        }
    }

    public static long count(long[] pInpt) {
            s = pInpt.length;
            p = pInpt;
            List<PArray> pArray = new ArrayList<>();
            for (int i = 0; i < s; i++) {
                pArray.add(new PArray(p[i], i));
            }
            pArray.sort(Comparator.comparingLong(PArray::getVal).thenComparingInt(PArray::getPos));

            long[] segTree = new long[s * 4];
            buildSegmentTree(segTree, p, 1, 0, s - 1);

            long[] sumLeftToRight = new long[s];
            sumLeftToRight[0] = p[0];
            for (int i = 1; i < s; i++) {
                sumLeftToRight[i] = sumLeftToRight[i - 1] + p[i];
            }

            long[] resLeft = new long[s];
            long[] resRight = new long[s];

            int[] leftLens = new int[s];
            int[] rightLens = new int[s];

            for (int i = 0; i < s; i++) {
                leftLens[i] = getLeftMaxIntervalLen(i, segTree, 0, i - 1);
                rightLens[i] = getRightMaxIntervalLen(i, segTree, i + 1, s - 1);
            }

            for (PArray pArr : pArray) {
                int lenLeft = leftLens[pArr.pos];
                int lenRight = rightLens[pArr.pos];

                int rightMaxIndex = pArr.pos + lenRight + 1;
                if (rightMaxIndex < s) {
                    resLeft[rightMaxIndex] += (resLeft[pArr.pos] + ((lenLeft + 1) * ((lenRight * pArr.val - (sumLeftToRight[rightMaxIndex - 1] - sumLeftToRight[pArr.pos])) % MOD) % MOD)) % MOD;
                    resLeft[rightMaxIndex] %= MOD;
                }
            }

            for (PArray pArr : pArray) {
                int lenLeft = leftLens[pArr.pos];
                int lenRight = rightLens[pArr.pos];

                int leftMaxIndex = pArr.pos - lenLeft - 1;

                if (leftMaxIndex > -1) {
                    resRight[leftMaxIndex] += (resRight[pArr.pos] + ((lenRight + 1) * ((lenLeft * pArr.val - (sumLeftToRight[pArr.pos - 1] - sumLeftToRight[leftMaxIndex])) % MOD) % MOD)) % MOD;
                    resRight[leftMaxIndex] %= MOD;
                }
            }

            long res = 0;

            for (int i = 0; i < s; i++) {
                int lenLeft = leftLens[i];
                int lenRight = rightLens[i];

                res += ((resRight[i] * (lenLeft + 1)) % MOD + (resLeft[i] * (lenRight + 1)) % MOD) % MOD;
                res %= MOD;
            }

            return res;
    }

    private static void buildSegmentTree(long[] tree, long[] data, int v, int start, int end) {
        if (start == end) {
            tree[v] = data[start];
            return;
        }

        int mid = (start + end) / 2;
        buildSegmentTree(tree, data, 2 * v, start, mid);
        buildSegmentTree(tree, data, 2 * v + 1, mid + 1, end);
        tree[v] = Math.max(tree[2 * v], tree[2 * v + 1]);
    }

    private static long getMaxValue(long[] tree, int v, int l, int r, int intervalL, int intervalR) {
        if (intervalL > intervalR) {
            return -1;
        }

        if (l == intervalL && r == intervalR) {
            return tree[v];
        }

        int mid = (l + r) / 2;

        return Math.max(getMaxValue(tree, 2 * v, l, mid, intervalL, Math.min(mid, intervalR)),
                getMaxValue(tree, 2 * v + 1, mid + 1, r, Math.max(mid + 1, intervalL), intervalR));
    }

    private static int getLeftMaxIntervalLen(int pos, long[] segTree, int start, int end) {
        if (pos == 0) {
            return 0;
        }

        if (start == end) {
            return p[start] <= p[pos] ? pos - start : pos - start - 1;
        }

        int mid = (start + end) / 2;
        if (getMaxValue(segTree, 1, 0, s - 1, mid + 1, end) > p[pos]) {
            return getLeftMaxIntervalLen(pos, segTree, mid + 1, end);
        } else {
            return getLeftMaxIntervalLen(pos, segTree, start, mid);
        }
    }

    private static int getRightMaxIntervalLen(int pos, long[] segTree, int start, int end) {
        if (pos == s - 1) {
            return 0;
        }

        if (start == end) {
            return p[start] < p[pos] ? start - pos : start - pos - 1;
        }

        int mid = (start + end) / 2;
        if (getMaxValue(segTree, 1, 0, s - 1, start, mid) >= p[pos]) {
            return getRightMaxIntervalLen(pos, segTree, start, mid);
        } else {
            return getRightMaxIntervalLen(pos, segTree, mid + 1, end);
        }
    }
}
