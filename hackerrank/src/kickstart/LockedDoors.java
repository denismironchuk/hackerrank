package kickstart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class LockedDoors {
    private static int n;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                n = Integer.parseInt(tkn1.nextToken());
                int q = Integer.parseInt(tkn1.nextToken());
                int[] locks = new int[n + 1];
                locks[0] = Integer.MAX_VALUE;
                locks[n] = Integer.MAX_VALUE;
                StringTokenizer locksTkn = new StringTokenizer(br.readLine());
                for (int i = 1; i < n; i++) {
                    locks[i] = Integer.parseInt(locksTkn.nextToken());
                }
                int[] maxTree = new int[4 * (n + 1)];
                buildTree(maxTree, locks, 1, 0, n);
                //for index i - index of first element to left greater than value on i + 1 position
                int[] maxToLeftIndex = new int[n + 1];
                //for index i - index of first element to right greater than value on i - 1 position
                int[] maxToRightIndex = new int[n + 1];

                for (int i = 1; i < n; i++) {
                    maxToLeftIndex[i] = getFirstLeftGreaterIndex(maxTree, locks[i + 1], 0, i);
                    maxToRightIndex[i] = getFirstRightGreaterIndex(maxTree, locks[i - 1], i, n);
                }
                System.out.println();
                int[] roomsTransitions = new int[n];
                for (int i = 0; i < n; i++) {
                    if (locks[i] < locks[i + 1]) {
                        roomsTransitions[i] = maxToLeftIndex[i];
                    } else {
                        roomsTransitions[i] = maxToRightIndex[i + 1] - 1;
                    }
                }
            }
        }
    }

    private static int getFirstLeftGreaterIndex(int[] tree, int topLimitValue, int start, int end) {
        if (start == end) {
            return start;
        }

        int middle = 1 + (start + end) / 2;
        int maxVal = getMaxValue(tree, 1, 0, n, middle, end);
        if (maxVal < topLimitValue) {
            return getFirstLeftGreaterIndex(tree, topLimitValue, start, middle - 1);
        } else {
            return getFirstLeftGreaterIndex(tree, topLimitValue,  middle, end);
        }
    }

    private static int getFirstRightGreaterIndex(int[] tree, int topLimitValue, int start, int end) {
        if (start == end) {
            return start;
        }

        int middle = (start + end) / 2;
        int maxVal = getMaxValue(tree, 1, 0, n, start, middle);
        if (maxVal < topLimitValue) {
            return getFirstRightGreaterIndex(tree, topLimitValue, middle + 1, end);
        } else {
            return getFirstRightGreaterIndex(tree, topLimitValue,  start, middle);
        }
    }

    private static void buildTree(int[] tree, int[] locks, int p, int left, int right) {
        if (left == right) {
            tree[p] = locks[left];
        } else {
            int middle = (left + right) / 2;
            buildTree(tree, locks, 2 * p, left, middle);
            buildTree(tree, locks, 2 * p + 1, middle + 1, right);
            tree[p] = Math.max(tree[2 * p], tree[2 * p + 1]);
        }
    }

    private static int getMaxValue(int[] tree, int p, int start, int end, int queryStart, int queryEnd) {
        if (queryStart > queryEnd) {
            return Integer.MIN_VALUE;
        }

        if (queryStart == start && queryEnd == end) {
            return tree[p];
        } else {
            int middle = (start + end) / 2;
            return Math.max(getMaxValue(tree, 2 * p, start, middle, queryStart, Math.min(middle, queryEnd)),
                    getMaxValue(tree, 2 * p + 1, middle + 1, end, Math.max(middle + 1, queryStart), queryEnd));
        }
    }
}
