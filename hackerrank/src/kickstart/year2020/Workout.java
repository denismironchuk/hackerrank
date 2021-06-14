package kickstart.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Workout {
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                int k = Integer.parseInt(tkn1.nextToken());
                StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                int[] m = new int[n];
                m[0] = Integer.parseInt(tkn2.nextToken());
                int[] diffs = new int[n - 1];
                for (int i = 1; i < n; i++) {
                    m[i] = Integer.parseInt(tkn2.nextToken());
                    diffs[i - 1] = m[i] - m[i - 1];
                }
                Arrays.sort(diffs);
                int res = binSearch(diffs, 1, diffs[n - 2], k);
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }

    private static boolean test(int[] diffs, int minLen, int k) {
        int neededAdd = 0;
        for (int diff : diffs) {
            if (diff > minLen) {
                neededAdd += diff / minLen;
                if (diff % minLen == 0) {
                    neededAdd--;
                }
            }
        }
        return neededAdd <= k;
    }

    public static int binSearch(int[] diffs, int minLen, int maxLen, int k) {
        if (minLen == maxLen) {
            return minLen;
        }

        int middle = (minLen + maxLen) / 2;
        if (test(diffs, middle, k)) {
            return binSearch(diffs, minLen, middle, k);
        } else {
            return binSearch(diffs, middle + 1, maxLen, k);
        }
    }
}
