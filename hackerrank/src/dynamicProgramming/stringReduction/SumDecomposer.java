package dynamicProgramming.stringReduction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SumDecomposer {
    public static void main(String[] args) {
        int n = 100;
        List<int[]> sums = new ArrayList<>();
        Date start = new Date();
        decompose(n, n, 3, new int[3], sums);
        Date end = new Date();

        System.out.println(end.getTime() - start.getTime() + "ms");
    }

    private static void decompose(int n, int prev, int cnt, int[] sum, List<int[]> sums) {
        if (cnt == 1) {
            if (n <= prev) {
                sums.add(new int[]{sum[2], sum[1], n});
            }
            return;
        }

        for (int i = 0; i <= n && i <= prev; i++) {
            sum[cnt - 1] = i;
            decompose(n - i, sum[cnt - 1], cnt - 1, sum, sums);
        }
    }
}
