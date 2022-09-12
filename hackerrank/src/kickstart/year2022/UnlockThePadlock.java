package kickstart.year2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class UnlockThePadlock {

    private static int n;
    private static int d;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                n = Integer.parseInt(tkn1.nextToken());
                d = Integer.parseInt(tkn1.nextToken());
                int[] v = new int[n];
                StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                for (int i = 0; i < n; i++) {
                    v[i] = Integer.parseInt(tkn2.nextToken());
                }

                Map<Integer, Long>[][] dynTable = new Map[n][n];
                long res = count(v, 0, n - 1, 0, dynTable);
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }

    public static long count(int[] v, int left, int right, int valueNeeded, Map<Integer, Long>[][] dynTable) {
        if (left == right) {
            long res = Math.min(Math.abs(v[left] - valueNeeded), d - Math.abs(v[left] - valueNeeded));
            return res;
        }

        if (dynTable[left][right] != null && dynTable[left][right].containsKey(valueNeeded)) {
            return dynTable[left][right].get(valueNeeded);
        }

        long res;
        if (v[left] == valueNeeded) {
            res = count(v, left + 1, right, valueNeeded, dynTable);
        } else if (v[right] == valueNeeded) {
            res = count(v, left, right - 1, valueNeeded, dynTable);
        } else {
            long dist1 = Math.abs(v[left] - valueNeeded);
            long res1 = count(v, left + 1, right, v[left], dynTable) + Math.min(dist1, d - dist1);
            long dist2 = Math.abs(v[right] - valueNeeded);
            long res2 = count(v, left, right - 1, v[right], dynTable) + Math.min(dist2, d - dist2);
            res = Math.min(res1, res2);
        }
        putToMap(left, right, valueNeeded, res, dynTable);
        return res;
    }

    private static void putToMap(int left, int right, int valueNeeded, long valToPut, Map<Integer, Long>[][] dynTable) {
        if (dynTable[left][right] == null) {
            dynTable[left][right] = new HashMap<>();
        }
        dynTable[left][right].put(valueNeeded, valToPut);
    }
}
