package kickstart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Candies {
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                int Q = Integer.parseInt(tkn1.nextToken());
                long[] a = new long[n];
                StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                long mul = 1;
                for (int i = 0; i < n; i++) {
                    a[i] = mul * Integer.parseInt(tkn2.nextToken());
                    mul *= -1;
                }
                long[] tree1 = new long[n * 4];
                fillTree(tree1, a, 1, 0, n - 1);
                for (int i = 0; i < n; i++) {
                    a[i] = a[i] * (i + 1);
                }
                long[] tree2 = new long[n * 4];
                fillTree(tree2, a, 1, 0, n - 1);
                long res = 0;
                for (int q = 0; q < Q; q++) {
                    StringTokenizer qTkn = new StringTokenizer(br.readLine());
                    String type = qTkn.nextToken();
                    if (type.equals("U")) {
                        int pos = Integer.parseInt(qTkn.nextToken());
                        long newVal = Long.parseLong(qTkn.nextToken());
                        mul = pos % 2 == 1 ? 1 : -1;
                        updateTree(tree1, 1, pos - 1, newVal * mul, 0, n - 1);
                        updateTree(tree2, 1, pos - 1, newVal * mul * pos, 0, n - 1);
                    } else {
                        int left = Integer.parseInt(qTkn.nextToken());
                        int right = Integer.parseInt(qTkn.nextToken());
                        mul = left % 2 == 1 ? 1 : -1;
                        long sum1 = mul * getSum(tree2, 1, 0, n - 1, left - 1, right - 1);
                        long sum2 = mul * (left - 1) * getSum(tree1, 1, 0, n - 1, left - 1, right - 1);
                        res += sum1 - sum2;
                    }
                }
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }

    private static void fillTree(long[] tree, long[] vals, int p, int left, int right) {
        if (left == right) {
            tree[p] = vals[left];
        } else {
            int middle = (left + right) / 2;
            fillTree(tree, vals, 2 * p, left, middle);
            fillTree(tree, vals, 2 * p + 1, middle + 1, right);
            tree[p] = tree[2 * p] + tree[2 * p + 1];
        }
    }

    private static void updateTree(long[] tree, int p, int updPos, long newVal, int left, int right) {
        if (left == right) {
            tree[p] = newVal;
        } else {
            int middle = (left + right) / 2;
            if (updPos <= middle) {
                updateTree(tree, 2 * p, updPos, newVal, left, middle);
            } else {
                updateTree(tree, 2 * p + 1, updPos, newVal, middle + 1, right);
            }
            tree[p] = tree[2 * p] + tree[2 * p + 1];
        }
    }

    private static long getSum(long[] tree, int p, int left, int right, int startIndex, int endIndex) {
        if (startIndex > endIndex) {
            return 0;
        }

        if (left == startIndex && right == endIndex) {
            return tree[p];
        } else {
            int middle = (left + right) / 2;
            return getSum(tree, 2 * p, left, middle, startIndex, Math.min(endIndex, middle)) +
                    getSum(tree, 2 * p + 1, middle + 1, right, Math.max(middle + 1, startIndex), endIndex);
        }
    }
}
