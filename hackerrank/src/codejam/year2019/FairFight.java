package codejam.year2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class FairFight {
    public static int n = 100000;
    public static int k = 20;

    public static void fillRandomArray(int[] array) {
        for (int i = 0; i < n; i++) {
            array[i] = (int)(Math.random() * 100);
        }
    }

    public static void fillArrayFromStdIn(int[] array, BufferedReader br) throws IOException {
        StringTokenizer tkn = new StringTokenizer(br.readLine());

        for (int i = 0; i < n; i++) {
            array[i] = Integer.parseInt(tkn.nextToken());
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t <= T; t++) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            n = Integer.parseInt(tkn1.nextToken());
            k = Integer.parseInt(tkn1.nextToken());

            int[] c = new int[n];
            int[] d = new int[n];

            //fillRandomArray(c);
            //fillRandomArray(d);
            fillArrayFromStdIn(c, br);
            fillArrayFromStdIn(d, br);

            int[] cSegTree = new int[n * 4];
            int[] dSegTree = new int[n * 4];

            buildSegTree(cSegTree, c, 1, 0, n - 1);
            buildSegTree(dSegTree, d, 1, 0, n - 1);

            long intervals = 0;

            Map<Integer, Integer> lastElemsPositions = new HashMap<>();

            for (int searchPos = 0; searchPos < n; searchPos++) {
                int maxVal = c[searchPos];
                int lastPosition = lastElemsPositions.getOrDefault(maxVal, -1);
                int cMaxIntervalLeft = Math.max(lastPosition + 1, getMaxIntervalLeft(cSegTree, searchPos, maxVal, 0, searchPos, n));
                int cMaxIntervalRight = getMaxIntervalRight(cSegTree, searchPos, maxVal, searchPos, n - 1, n);

                lastElemsPositions.put(maxVal, searchPos);

                int upLimit = c[searchPos] + k;
                int downLimit = c[searchPos] - k - 1;

                if (d[searchPos] <= upLimit) {
                    long dUpLimitIntervalLeft = getMaxIntervalLeft(dSegTree, searchPos, upLimit, cMaxIntervalLeft, searchPos, n);
                    long dUpLimitIntervalRight = getMaxIntervalRight(dSegTree, searchPos, upLimit, searchPos, cMaxIntervalRight, n);

                    intervals += (searchPos - dUpLimitIntervalLeft + 1) * (dUpLimitIntervalRight - searchPos + 1);

                    if (d[searchPos] <= downLimit) {
                        long dDownLimitIntervalLeft = getMaxIntervalLeft(dSegTree, searchPos, downLimit, cMaxIntervalLeft, searchPos, n);
                        long dDownLimitIntervalRight = getMaxIntervalRight(dSegTree, searchPos, downLimit, searchPos, cMaxIntervalRight, n);

                        intervals -= (searchPos - dDownLimitIntervalLeft + 1) * (dDownLimitIntervalRight - searchPos + 1);
                    }
                }
            }

            /*long intrsTriv = countIntervalsTrivial(c, d);
            if (intervals != intrsTriv) {
                throw new RuntimeException("my excp!!!");
            }*/
            System.out.printf("Case #%s: %s\n", t, intervals);
        }
    }

    private static long countIntervalsTrivial(int[] c, int[] d) {
        long intrs = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int maxC = countMaxValOnInterval(c, i, j);
                int maxD = countMaxValOnInterval(d, i, j);
                if (Math.abs(maxC - maxD) <= k) {
                    intrs++;
                }
            }
        }

        return intrs;
    }

    private static int countMaxValOnInterval(int[] arr, int intrStart, int intrEnd) {
        int max = arr[intrStart];

        for (int i = intrStart; i <= intrEnd; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }

        return max;
    }

    private static int getMaxIntervalLeft(int[] segTree, int pos, int maxVal, int reqL, int reqR, int dataLen) {
        if (reqL == reqR) {
            return reqL;
        }

        int mid = (reqL + reqR) / 2;
        int max = getMax(segTree, 1, 0, dataLen - 1, mid, pos);

        if (max <= maxVal) {
            return getMaxIntervalLeft(segTree, pos, maxVal, reqL, mid, dataLen);
        } else {
            return getMaxIntervalLeft(segTree, pos, maxVal, mid + 1, reqR, dataLen);
        }
    }

    private static int getMaxIntervalRight(int[] segTree, int pos, int maxVal, int reqL, int reqR, int dataLen) {
        if (reqL == reqR) {
            return reqL;
        }

        int mid = 1 + (reqL + reqR) / 2;
        int max = getMax(segTree, 1, 0, dataLen - 1, pos, mid);

        if (max <= maxVal) {
            return getMaxIntervalRight(segTree, pos, maxVal, mid, reqR, dataLen);
        } else {
            return getMaxIntervalRight(segTree, pos, maxVal, reqL, mid - 1, dataLen);
        }
    }

    private static void buildSegTree(int[] tree, int[] data, int v, int l, int r) {
        if (l == r) {
            tree[v] = data[l];
        } else {
            int mid = (l + r) / 2;
            buildSegTree(tree, data, 2 * v, l, mid);
            buildSegTree(tree, data, 2 * v + 1, mid + 1, r);
            tree[v] = Math.max(tree[2 * v], tree[2 * v + 1]);
        }
    }

    private static int getMax(int[] tree, int v, int l, int r, int reqL, int reqR) {
        if (reqL > reqR) {
            return -1;
        }

        if (l == reqL && r == reqR) {
            return tree[v];
        } else {
            int mid = (l + r) / 2;
            return Math.max(getMax(tree, 2 * v, l, mid, reqL, Math.min(mid, reqR)),
                    getMax(tree, 2 * v + 1, mid + 1, r, Math.max(mid + 1, reqL), reqR));
        }
    }
}
