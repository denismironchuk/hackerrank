package kickstart.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class PerfectSubarray {
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int n = Integer.parseInt(br.readLine());
                int[] a = new int[n];
                int min = 0;
                int max = 0;
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                for (int i = 0; i < n; i++) {
                    a[i] = Integer.parseInt(tkn.nextToken());
                    if (a[i] < 0) {
                        min += a[i];
                    } else {
                        max += a[i];
                    }
                }
                int maxSqr = 0;
                while (maxSqr * maxSqr <= max) {
                    maxSqr++;
                }
                long[] counts = new long[max - min + 1];
                counts[-min] = 1;
                int cumSum = 0;
                long res = 0;
                for (int i = 0; i < n; i++) {
                    cumSum += a[i];
                    for (int sqr = 0; sqr < maxSqr; sqr++) {
                        if (cumSum - sqr * sqr - min >= 0) {
                            res += counts[cumSum - sqr * sqr - min];
                        }
                    }
                    counts[cumSum - min]++;
                }
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }
}
