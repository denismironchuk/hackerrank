package codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.StringTokenizer;

public class FairFight {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t <= T; t++) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(tkn1.nextToken());
            int k = Integer.parseInt(tkn1.nextToken());

            StringTokenizer cTkn = new StringTokenizer(br.readLine());
            StringTokenizer dTkn = new StringTokenizer(br.readLine());
            int[] c = new int[n];
            int[] d = new int[n];

            for (int i = 0; i < n; i++) {
                c[i] = Integer.parseInt(cTkn.nextToken());
                d[i] = Integer.parseInt(dTkn.nextToken());
            }

            int[] cSegTree = new int[n * 4];
            int[] dSegTree = new int[n * 4];

            buildSegTree(cSegTree, c, 1, 0, n - 1);
            buildSegTree(dSegTree, d, 1, 0, n - 1);

            long intervals = 0;

            for (int searchPos = 0; searchPos < n; searchPos++) {
                int maxVal = c[searchPos];
                int cMaxIntervalLeft = getMaxIntervalLeft(cSegTree, searchPos, maxVal, 0, searchPos, n);
                int cMaxIntervalRight = getMaxIntervalRight(cSegTree, searchPos, maxVal, searchPos, n - 1, n);

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

            System.out.printf("Case #%s: %s\n", t, intervals);
        }
    }

    /*public static void main(String[] args) {
        int n = 100;

        while (true) {
            int[] c = new int[n];

            for (int i = 0; i < n; i++) {
                c[i] = (int) (Math.random() * 100);
                System.out.printf("%s ", c[i]);
            }

            System.out.println();

            int[] cSegTree = new int[n * 4];
            buildSegTree(cSegTree, c, 1, 0, n - 1);

            for (int searchPos = 0; searchPos < n; searchPos++) {
                int maxVal = c[searchPos];
                System.out.printf("Pos = %s; val = %s\n", searchPos, maxVal);
                System.out.println("Left");
                System.out.println(getMaxIntervalLeft(cSegTree, searchPos, maxVal, 0, searchPos, n));
                System.out.println(getMaxIntervalLeftTrivial(c, searchPos));

                System.out.println("Right");
                System.out.println(getMaxIntervalRight(cSegTree, searchPos, maxVal, searchPos, n - 1, n));
                System.out.println(getMaxIntervalRightTrivial(c, searchPos));

                int leftBound = getMaxIntervalLeft(cSegTree, searchPos, maxVal, 0, searchPos, n);
                int leftBoundTriv = getMaxIntervalLeftTrivial(c, searchPos);

                int rightBound =  getMaxIntervalRight(cSegTree, searchPos, maxVal, searchPos, n - 1, n);
                int rightBoundTriv = getMaxIntervalRightTrivial(c, searchPos);

                if (leftBound != leftBoundTriv) {
                    throw new RuntimeException("My excp!!!");
                }

                if (rightBound != rightBoundTriv) {
                    throw new RuntimeException("My excp!!!");
                }
            }
        }
    }*/

    private static int getMaxIntervalLeftTrivial(int[] data, int pos) {
        int max = data[pos];

        while(pos >= 0 && data[pos] <= max) {
            pos--;
        }

        return pos + 1;
    }

    private static int getMaxIntervalRightTrivial(int[] data, int pos) {
        int max = data[pos];

        while(pos < data.length && data[pos] <= max) {
            pos++;
        }

        return pos - 1;
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
