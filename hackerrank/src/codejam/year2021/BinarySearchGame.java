package codejam.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BinarySearchGame {

    private static final long MOD = 1000000000 + 7;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer line1 = new StringTokenizer(br.readLine());
                long n = Long.parseLong(line1.nextToken());
                long m = Long.parseLong(line1.nextToken());
                long l = Long.parseLong(line1.nextToken());

                StringTokenizer line2 = new StringTokenizer(br.readLine());
                int len = (int) fastPow(2, l);
                int[] a = new int[len];
                for (int i = 0; i < len; i++) {
                    a[i] = Integer.parseInt(line2.nextToken());
                }

                long[] greates = new long[(int)n + 1];
                for (int i = 1; i < fastPow(2, n); i++) {
                    int[] mask = new int[(int)n];
                    int greaterCnt = 0;
                    int i_ = i;
                    int pos = 0;
                    while (i_ != 0) {
                        mask[pos] = i_ % 2;
                        i_ /= 2;
                        if (mask[pos] == 1) {
                            greaterCnt++;
                        }
                        pos++;
                    }

                    boolean isGreater = isResultGreater(a, mask);
                    if (isGreater) {
                        greates[greaterCnt]++;
                    }
                }
                long res = 0;
                for (int k = 1; k <= m; k++) {
                    for (int greaterCnt = 0; greaterCnt <= n; greaterCnt++) {
                        res += (((greates[greaterCnt] * fastPowMod(k - 1, n - greaterCnt)) % MOD) * fastPowMod(m - k + 1, greaterCnt)) % MOD;
                        res %= MOD;
                    }
                }
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }

    private static boolean isResultGreater(int[] a, int[] mask) {
        int[] assigns = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            assigns[i] = mask[a[i] - 1];
        }

        return 1 == processMinMaxTree(assigns, 0, assigns.length - 1, true);
    }

    private static int processMinMaxTree(int[] a, int start, int end, boolean max) {
        if (start + 1 == end) {
            return max ? Math.max(a[start], a[end]) : Math.min(a[start], a[end]);
        }

        int leftRes = processMinMaxTree(a, start, start + (end - start) / 2, !max);
        int rightRes = processMinMaxTree(a, start + 1 + (end - start) / 2, end, !max);

        return max ? Math.max(leftRes, rightRes) : Math.min(leftRes, rightRes);
    }

    private static long fastPow(long n, long p) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return fastPow(n * n, p / 2);
        } else {
            return n * fastPow(n, p - 1);
        }
    }

    private static long fastPowMod(long n, long p) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return fastPowMod((n * n) % MOD, p / 2);
        } else {
            return (n * fastPowMod(n, p - 1)) % MOD;
        }
    }
}
