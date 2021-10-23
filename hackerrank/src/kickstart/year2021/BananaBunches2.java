package kickstart.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BananaBunches2 {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                int k = Integer.parseInt(tkn1.nextToken());
                long[] b = new long[n];

                StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                for (int i = 0; i < n; i++) {
                    b[i] = Long.parseLong(tkn2.nextToken());
                }

                long[] aggrCnt = new long[n];
                aggrCnt[0] = b[0];
                for (int i = 1; i < n; i++) {
                    aggrCnt[i] = aggrCnt[i - 1] + b[i];
                }

                int[] minLen = new int[k + 1];
                Arrays.fill(minLen, Integer.MAX_VALUE);

                int res = Integer.MAX_VALUE;
                for (int i = n - 1 ; i >= 0; i--) {
                    for (int j = 0; j <= i; j++) {
                        long cnt = j == 0 ? aggrCnt[i] : aggrCnt[i] - aggrCnt[j - 1];
                        if (cnt == k) {
                            res = Math.min(res, i - j + 1);
                        } else if (cnt < k) {
                            int cnt2 = (int)(k - cnt);
                            if (minLen[cnt2] != Integer.MAX_VALUE) {
                                res = Math.min(res, i - j + 1 + minLen[cnt2]);
                            }
                        }
                    }

                    for (int j = i; j < n; j++) {
                        long cnt = i == 0 ? aggrCnt[j] : aggrCnt[j] - aggrCnt[i - 1];
                        if (cnt <= k) {
                            int cnt2 = (int)cnt;
                            int len = j - i + 1;
                            minLen[cnt2] = Math.min(minLen[cnt2], len);
                        }
                    }
                }

                if (res == Integer.MAX_VALUE) {
                    res = -1;
                }

                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }
}
