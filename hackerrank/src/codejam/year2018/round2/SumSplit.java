package codejam.year2018.round2;

import java.util.ArrayList;
import java.util.List;

public class SumSplit {

    public static int countMaxLen(int a, int b) {
        List<List<Integer>> sums1 = new ArrayList<>();
        split(a, new ArrayList<>(), sums1, a + 1);

        List<List<Integer>> sums2 = new ArrayList<>();
        split(b, new ArrayList<>(), sums2, b + 1);

        int maxLen = 0;
        for (List<Integer> sum1 : sums1) {
            for (List<Integer> sum2 : sums2) {
                int[][] map = new int[a + 1][b + 1];
                boolean valid = true;
                for (int i = 0; valid && i < Math.max(sum1.size(), sum2.size()); i++) {
                    int v1 = i < sum1.size() ? sum1.get(i) : 0;
                    int v2 = i < sum2.size() ? sum2.get(i) : 0;
                    if (map[v1][v2] == 1 || (v1 == 0 && v2 == 0)) {
                        valid = false;
                    } else {
                        map[v1][v2] = 1;
                    }
                }
                if (valid && maxLen < Math.max(sum1.size(), sum2.size())) {
                    maxLen = Math.max(sum1.size(), sum2.size());
                }
            }
        }
        return maxLen;
    }

    public static void main(String[] args) {
        int n = 1;
        List<List<Integer>> sums = new ArrayList<>();
        split(n, new ArrayList<>(), sums, n + 1);

        System.out.println(sums.size());
        int maxLen = 0;
        List<Integer> maxSum1 = null;
        List<Integer> maxSum2 = null;
        for (List<Integer> sum1 : sums) {
            for (List<Integer> sum2 : sums) {
                int[][] map = new int[n + 1][n + 1];
                boolean valid = true;
                for (int i = 0; valid && i < Math.max(sum1.size(), sum2.size()); i++) {
                    int v1 = i < sum1.size() ? sum1.get(i) : 0;
                    int v2 = i < sum2.size() ? sum2.get(i) : 0;
                    if (map[v1][v2] == 1 || (v1 == 0 && v2 == 0)) {
                        valid = false;
                    } else {
                        map[v1][v2] = 1;
                    }
                }
                if (valid && maxLen < Math.max(sum1.size(), sum2.size())) {
                    maxLen = Math.max(sum1.size(), sum2.size());
                    maxSum1 = sum1;
                    maxSum2 = sum2;
                }
            }
        }
        System.out.println(maxLen);
        System.out.println(maxSum1);
        System.out.println(maxSum2);
    }

    private static void split(int n, List<Integer> curSum, List<List<Integer>> sums, int maxLen) {
        if (n == 0) {
            sums.add(new ArrayList<>(curSum));
            return;
        }

        if (curSum.size() + 1 == maxLen) {
            curSum.add(n);
            sums.add(new ArrayList<>(curSum));
            curSum.remove(curSum.size() - 1);
            return;
        }

        for (int i = 0; i <= n; i++) {
            curSum.add(i);
            split(n - i, curSum, sums, maxLen);
            curSum.remove(curSum.size() - 1);
        }
    }
}
