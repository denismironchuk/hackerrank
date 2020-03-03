package dynamicProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.StringTokenizer;

public class SherlockAndMergingAlgorithm2 {
    private static long MOD = 1000000007;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int n = Integer.parseInt(br.readLine());
            int[] m = new int[n];
            //StringTokenizer tkn = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                m[i] = i;//Integer.parseInt(tkn.nextToken());
            }

            Date start = new Date();
            long[][] currMap = new long[n + 1][n + 1];
            long[][] nextMap = new long[n + 1][n + 1];

            int[][] currPoints = new int[(n + 2) * (n + 2)][2];
            int[][] nextPoints = new int[(n + 2) * (n + 2)][2];

            int currPointsCnt = 0;
            int nextPointsCnt = 0;

            currMap[1][0] = 1;
            currPoints[0][0] = 1;
            currPoints[0][1] = 0;
            currPointsCnt = 1;

            int prevVal = m[0];

            for (int i = 1; i < n; i++) {
                int val = m[i];

                for (int j = 0; j < nextPointsCnt; j++) {
                    nextMap[nextPoints[j][0]][nextPoints[j][1]] = 0;
                }

                nextPointsCnt = 0;

                for (int j = 0; j < currPointsCnt; j++) {
                    int last = currPoints[j][0];
                    int beforeLast = currPoints[j][1];
                    long cnt = currMap[last][beforeLast];

                    if (val > prevVal) {
                        if (beforeLast == 0) {
                            nextMap[last + 1][0] = 1;
                            nextPoints[nextPointsCnt][0] = last + 1;
                            nextPoints[nextPointsCnt][1] = 0;
                            nextPointsCnt++;
                        } else if (beforeLast > last) {
                            nextMap[last + 1][beforeLast] = ((cnt * (beforeLast - last)) % MOD);
                            nextPoints[nextPointsCnt][0] = last + 1;
                            nextPoints[nextPointsCnt][1] = beforeLast;
                            nextPointsCnt++;
                        }
                    }

                    if (nextMap[1][last] == 0) {
                        nextPoints[nextPointsCnt][0] = 1;
                        nextPoints[nextPointsCnt][1] = last;
                        nextPointsCnt++;
                    }

                    nextMap[1][last] += (cnt * last) % MOD;
                    nextMap[1][last] %= MOD;
                }

                long[][] tmp1 = currMap;
                currMap = nextMap;
                nextMap = tmp1;

                int[][] tmp2 = currPoints;
                currPoints = nextPoints;
                nextPoints = tmp2;

                int tmp3 = currPointsCnt;
                currPointsCnt = nextPointsCnt;
                nextPointsCnt = tmp3;

                prevVal = val;
            }

            long res = 0;

            for (int i = 0; i < currPointsCnt; i++) {
                res += currMap[currPoints[i][0]][currPoints[i][1]];
                res %= MOD;
            }

            Date end = new Date();

            System.out.println(res);
            System.out.println(end.getTime() - start.getTime() + "ms");
        }
    }
}
