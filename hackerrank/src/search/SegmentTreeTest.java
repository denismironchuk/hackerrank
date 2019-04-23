package search;

public class SegmentTreeTest {
    public static void main(String[] args) {
        int n = 50000;
        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = (int)(100 * Math.random());
        }

        int[] tree = new int[4 * n];
        int[] updates = new int[4 * n];

        buildSegTree(a, tree, 1, 0, n - 1);

        while (true) {
            int start = (int)(n * Math.random());
            int end = start + (int)((n - start) * Math.random());
            int updVal = (int)(100 * Math.random()) - 50;

            updateIntervalTrivial(a, start, end, updVal);
            updateInterval(tree, updates, 1,0, n - 1, start, end, updVal);

            int[] treeInit = new int[4 * n];
            int[] treeFromUpds = new int[4 * n];

            buildSegTree(a, treeInit, 1, 0, n - 1);
            createUpdatedTree(treeFromUpds, tree, updates, 1, 0, n - 1, 0);

            for (int i = 0; i < 4 * n; i++) {
                if (treeInit[i] != treeFromUpds[i]) {
                    throw new RuntimeException("diff!!!");
                }
            }

            int maxTriv = getMaxTrivial(a);

            System.out.printf("%s %s\n", maxTriv, tree[1]);

            if (maxTriv != tree[1]) {
                throw new RuntimeException("excp");
            }
        }
    }

    private static void updateIntervalTrivial(int[] a, int start, int end, int updVal) {
        for (int i = start; i <= end; i++) {
            a[i] += updVal;
        }
    }

    private static int getMaxTrivial(int[] a) {
        int res = Integer.MIN_VALUE;

        for (int i = 0; i < a.length; i++) {
            if (a[i] > res) {
                res = a[i];
            }
        }

        return res;
    }

    private static void updateInterval(int[] tree, int[] updates, int v, int l, int r, int intL, int intR, int incr) {
        if (intL > intR) {
            return;
        }

        if (l == intL && r == intR) {
            tree[v] += incr;
            updates[v] += incr;
        } else {
            int mid = (l + r) / 2;
            updateInterval(tree, updates, v * 2,        l,       mid, intL,                    Math.min(mid, intR), incr);
            updateInterval(tree, updates, v * 2 + 1, mid + 1, r,   Math.max(mid + 1, intL), intR,                incr);
            tree[v] = Math.max(tree[2 * v], tree[2 * v + 1]) + updates[v];
        }
    }

    private static void buildSegTree(int[] delays, int[] tree, int v, int left, int right) {
        if (left == right) {
            tree[v] = delays[left];
        } else {
            int mid = (left + right) / 2;
            buildSegTree(delays, tree, 2 * v, left, mid);
            buildSegTree(delays, tree, 2 * v + 1, mid + 1, right);
            tree[v] = Math.max(tree[2 * v], tree[2 * v + 1]);
        }
    }

    private static void createUpdatedTree(int[] newTree, int[] tree, int[] updates, int v, int l, int r, int incr) {
        if (l == r) {
            newTree[v] = tree[v] + incr;
        } else {
            int mid = (l + r) / 2;
            createUpdatedTree(newTree, tree, updates, 2 * v, l, mid, incr + updates[v]);
            createUpdatedTree(newTree, tree, updates, 2 * v + 1, mid + 1, r, incr + updates[v]);
            newTree[v] = Math.max(newTree[2 * v], newTree[2 * v + 1]);
        }
    }
}
