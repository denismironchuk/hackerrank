package codejam.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class BinarySearchGame {

    private static final long MOD = 1000000000 + 7;
    private static long n;
    private static long m;
    private static long l;
    private static int[] a;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer line1 = new StringTokenizer(br.readLine());
                n = Long.parseLong(line1.nextToken());
                m = Long.parseLong(line1.nextToken());
                l = Long.parseLong(line1.nextToken());

                StringTokenizer line2 = new StringTokenizer(br.readLine());
                int len = (int) fastPow(2, l);
                a = new int[len];
                for (int i = 0; i < len; i++) {
                    a[i] = Integer.parseInt(line2.nextToken());
                }

                Map<Integer, Integer> repeatedCards = new HashMap<>();
                int notPresentCnt = 0;
                int repeatedCnt = 0;

                for (int card = 1; card <= n; card++) {
                    int cnt = 0;
                    for (int i = 0; i < len; i++) {
                        if (a[i] == card) {
                            cnt++;
                        }
                    }
                    if (cnt > 1) {
                        repeatedCards.put(card, repeatedCnt);
                        repeatedCnt++;
                    } else if (cnt == 0) {
                        notPresentCnt++;
                    }
                }

                long res = 0;
                for (int i = 0; i < fastPow(2, repeatedCnt); i++) {
                    int[] mask = new int[repeatedCnt];
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

                    for (int k = 1; k <= m; k++) {
                        long combCnt = greaterResultCombCnt(mask, repeatedCards, k);
                        res += (((combCnt * fastPowMod(k - 1, repeatedCnt - greaterCnt)) % MOD) * fastPowMod(m - k + 1, greaterCnt)) % MOD;
                        res %= MOD;
                    }
                }
                res *= fastPowMod(m, notPresentCnt);
                res %= MOD;

                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }

    private static long greaterResultCombCnt(int[] mask, Map<Integer, Integer> repeatedCards, int k) {
        int[] assigns = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            if (repeatedCards.containsKey(a[i])) {
                assigns[i] = mask[repeatedCards.get(a[i])];
            } else {
                assigns[i] = 2;
            }
        }

        return countCombinations(assigns, 0, assigns.length - 1, true, k)[0];
    }

    private static long[] countCombinations(int[] assigns, int start, int end, boolean max, int k) {
        if (start + 1 == end) {
            if (assigns[start] != 2 && assigns[end] != 2) {
                return new long[] {max ? Math.max(assigns[start], assigns[end]) : Math.min(assigns[start], assigns[end]) , 0};
            } else {
                if (assigns[start] == 2 && assigns[end] == 2) {
                    return new long[] {
                            max ? (((((m * (m - k + 1)) % MOD)) % MOD) + ((k - 1) * (m - k + 1)) % MOD) % MOD : (((m - k + 1) * (m - k + 1)) % MOD),
                            2
                    };
                } else if (assigns[start] * assigns[end] == 2) {
                    return new long[] {
                            max ? m : m - k + 1,
                            1
                    };
                } else {
                    return new long[] {
                            max ? m - k + 1 : 0,
                            1
                    };
                }
            }
        } else {
            long[] leftRes = countCombinations(assigns, start, start + (end - start) / 2, !max, k);
            long[] rightRes = countCombinations(assigns, start + 1 + (end - start) / 2, end, !max, k);

            return new long[] {
                    max ?
                            ((((leftRes[0] * fastPowMod(m, rightRes[1])) % MOD) + ((rightRes[0] * fastPowMod(m, leftRes[1])) % MOD)) % MOD + MOD - ((leftRes[0] * rightRes[0]) % MOD)) % MOD :
                            (leftRes[0] * rightRes[0]) % MOD,
                    rightRes[1] + leftRes[1]
            };

        }
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
