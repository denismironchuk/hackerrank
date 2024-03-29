package kickstart.year2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class HappySubarrays {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int n = Integer.parseInt(br.readLine());
                long[] a = new long[n];
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                for (int i = 0; i < n; i++) {
                    a[i] = Long.parseLong(tkn.nextToken());
                }
                long[] maxLenHappy = new long[n];
                long[] sums = new long[n];
                long[] happySubArraySums = new long[n];

                long res = 0;

                for (int i = n - 1; i >= 0; i--) {
                    int j = i;
                    while (true) {
                        for (; j < n && sums[i] + a[j] >= 0 && maxLenHappy[j] == 0; j++) {
                            sums[i] += a[j];
                            maxLenHappy[i]++;
                            happySubArraySums[i] += sums[i];
                        }
                        if (j < n && maxLenHappy[j] != 0) {
                            maxLenHappy[i] += maxLenHappy[j];
                            happySubArraySums[i] += sums[i] * maxLenHappy[j] + happySubArraySums[j];
                            sums[i] += sums[j];
                            j += maxLenHappy[j];
                        } else {
                            break;
                        }
                    }
                    res += happySubArraySums[i];
                }
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }
}
