package codejam.year2018.round2;

import java.util.Map;
import java.util.TreeMap;

public class GracefullChainsawJuggling {
    public static void main(String[] args) {
        /*int a = 500;
        int b = 500;
        int[][] usage = new int[a + 1][b + 1];

        long start = System.currentTimeMillis();
        long res = getCombsCnt(a, b, usage);
        long end = System.currentTimeMillis();
        System.out.println(res);
        System.out.println(end - start + "ms");*/
        long cnt = 0;
        long cnt2 = 0;
        int maxLen = -1;
        Map<Integer, Integer> valsCnt = new TreeMap<>();

        long start = System.currentTimeMillis();
        for (int a = 0; a <= 500; a++) {
            for (int b = 0; b <= 500; b++) {
                for (int val = 0; val <= a; val++) {
                    valsCnt.merge(val, 1, (oldVal, newVal) -> oldVal + 1);
                    cnt2++;
                    for (int len = 1; val * len < a; len++) {
                        maxLen = Math.max(maxLen, len);
                        cnt++;
                        int nonZeroLen = len - 1;
                        int risingSum = nonZeroLen + ((nonZeroLen) * (nonZeroLen - 1) / 2);
                        if (risingSum > b) {
                            break;
                        }
                    }
                }
            }
        }
        System.out.println(valsCnt);
        long end = System.currentTimeMillis();
        System.out.println("cnt = " + cnt);
        System.out.println("cnt2 = " + cnt2);
        System.out.println("maxLen = " + maxLen);
        System.out.println(end - start + "ms");
    }

    private static long getCombsCnt(int a, int b, int[][] usageMap) {
        if (a == 0 && b == 0) {
            return 1;
        }

        if ((a == 0) ^ (b == 0)) {
            return 0;
        }
        if (a < 0 || b < 0) {
            return 0;
        }
        if (usageMap[a][b] != 0) {
            return 0;
        }

        long res = 0;
        for (int i = 1; i <= a; i++) {
            for (int len = 1; len * i <= a; len++) {
                if (a == len * i) {
                    int nonZeroLen = len - 1;
                    int risingSum = nonZeroLen + ((nonZeroLen) * (nonZeroLen - 1) / 2);
                    if (b >= risingSum) {
                        res++;
                    }
                } else {
                    int nonZeroLen = len - 1;
                    int risingSum = nonZeroLen + ((nonZeroLen) * (nonZeroLen - 1) / 2);
                    if (risingSum > b) {
                        break;
                    }

                    res += getCombsCnt(a - len * i, b - risingSum, usageMap);
                }
            }
        }
        usageMap[a][b] = 1;
        return res;
    }
}
