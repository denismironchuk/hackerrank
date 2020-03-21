package dynamicProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class AlienLnguage {
    private static final long MOD = 100000007;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());

            for (int t = 0; t < T; t++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn.nextToken());
                int m = Integer.parseInt(tkn.nextToken());

                long[] step = new long[n + 1];
                for (int i = 1; i <= n; i++) {
                    step[i] = 2 * i > n ? 1 : 0;
                }

                LinkedList<long[]> stepsCumSum = new LinkedList<>();
                boolean haveNonZero = true;

                while (haveNonZero) {
                    long[] stepCumSum = new long[n + 1];
                    stepCumSum[n] = step[n];
                    for (int i = n - 1; i > 0; i--) {
                        stepCumSum[i] = (stepCumSum[i + 1] + step[i]) % MOD;
                    }
                    stepsCumSum.add(stepCumSum);
                    haveNonZero = false;
                    for (int i =1; i <= n; i++) {
                        if (2 * i > n) {
                            step[i] = 0;
                        } else {
                            long newVal = stepsCumSum.getLast()[i * 2];
                            step[i] = newVal;
                            haveNonZero = haveNonZero || newVal > 0;
                        }
                    }
                }

                long[] sums = new long[stepsCumSum.size()];

                int i = 0;
                for (long[] cumSum : stepsCumSum) {
                    sums[i] = cumSum[1];
                    i++;
                }

                int add = 20;
                long[] res = new long[m + add + 1];
                res[add] = 1;

                for (i = 21; i < res.length; i++) {
                    for (int j = 0; j < sums.length; j++) {
                        res[i] += (res[i - 1 - j] * sums[j]) % MOD;
                        res[i] %= MOD;
                    }
                }

                System.out.println(res[res.length - 1]);
            }
        }
    }
}
