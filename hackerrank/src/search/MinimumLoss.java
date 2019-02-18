package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class MinimumLoss {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        long[] prices = new long[n];
        StringTokenizer pricesTkn = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            prices[i] = Long.parseLong(pricesTkn.nextToken());
        }

        TreeSet<Long> prSet = new TreeSet<>();
        prSet.add(prices[n - 1]);
        long res = Long.MAX_VALUE;
        for (int i = n - 2; i > -1; i--) {
            Long lower = prSet.lower(prices[i]);
            if (null != lower && prices[i] - lower < res) {
                res = prices[i] - lower;
            }
            prSet.add(prices[i]);
        }

        System.out.println(res);
    }
}
