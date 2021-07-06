package codejam.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class BuildAPair {

    public static void main(String[] args) throws IOException {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                String d = br.readLine();
                int n = d.length();
                int[] digits = new int[n];
                for (int i = 0; i < n; i++) {
                    digits[i] = d.charAt(i) - '0';
                }
                Arrays.sort(digits);
                if (n % 2 == 1) {
                    long res = processOdd(digits);
                    System.out.printf("Case #%s: %s\n", t, res);
                } else {
                    long res = processEven(digits);
                    System.out.printf("Case #%s: %s\n", t, res);
                }
            }
        }
    }

    private static long processEven(int[] digitsSorted) {
        int n = digitsSorted.length;
        int[] pairsCnt = new int[10];
        int[] digitsCnt = new int[10];
        for (int i = 0; i < n; i++) {
            digitsCnt[digitsSorted[i]]++;
        }

        for (int i = 0; i < n - 1; i++) {
            if (digitsSorted[i] == digitsSorted[i + 1]) {
                pairsCnt[digitsSorted[i]]++;
                i++;
            }
        }

        long res = getMinDiff(digitsCnt, n, false);

        for (int d = 9; d > 0; d--) {
            if (pairsCnt[d] > 0) {
                pairsCnt[d]--;
                digitsCnt[d] -= 2;
                res = Math.min(res, buildPairs(pairsCnt, digitsCnt, d, n - 2));
                digitsCnt[d] += 2;
                pairsCnt[d]++;
            }
        }

        return res;
    }

    private static long getMinDiff(int[] digitsCnt, int len, boolean useZero) {
        long res = Long.MAX_VALUE;
        for (int d1 = useZero ? 0 : 1; d1 < 9; d1++) {
            for (int d2 = d1 + 1; d2 < 10; d2++) {
                if (digitsCnt[d1] == 0 || digitsCnt[d2] == 0) {
                    continue;
                }

                int biggerFirstDig = d2;
                int smallerFirstDig = d1;

                long bigger = biggerFirstDig;
                long smaller = smallerFirstDig;

                digitsCnt[biggerFirstDig]--;
                digitsCnt[smallerFirstDig]--;

                int d = 0;
                int usedCnt = 0;
                int buildLen = 1;
                while (buildLen < len / 2) {
                    if (digitsCnt[d] - usedCnt == 0) {
                        usedCnt = 0;
                        d++;
                    } else {
                        bigger *= 10;
                        bigger += d;
                        usedCnt++;
                        buildLen++;
                    }
                }

                d = 9;
                usedCnt = 0;
                buildLen = 1;

                while (buildLen < len / 2) {
                    if (digitsCnt[d] - usedCnt == 0) {
                        usedCnt = 0;
                        d--;
                    } else {
                        smaller *= 10;
                        smaller += d;
                        usedCnt++;
                        buildLen++;
                    }
                }

                digitsCnt[biggerFirstDig]++;
                digitsCnt[smallerFirstDig]++;

                if (bigger - smaller < res) {
                    res = bigger - smaller;
                }

                break;
            }
        }

        return res;
    }

    private static long buildPairs(int[] pairsCnt, int[] digitsCnt, int startIndex, int len) {
        if (len == 0) {
            return 0;
        }

        long res = getMinDiff(digitsCnt, len, true);

        for (int d = startIndex; d >= 0; d--) {
            if (pairsCnt[d] > 0) {
                pairsCnt[d]--;
                digitsCnt[d] -= 2;
                res = Math.min(res, buildPairs(pairsCnt, digitsCnt, d, len - 2));
                digitsCnt[d] += 2;
                pairsCnt[d]++;
            }
        }

        return res;
    }


    private static long processOdd(int[] digitsSorted) {
        int firstPos = 9;
        for (int d : digitsSorted) {
            if (d != 0) {
                firstPos = d;
                break;
            }
        }
        int numberLen = (digitsSorted.length - 1) / 2;
        long biggerNum = firstPos;
        boolean isUsed = false;
        int digLen = 1;
        int index = 0;
        while (digLen < numberLen + 1) {
            if (digitsSorted[index] == firstPos && !isUsed) {
                isUsed = true;
            } else {
                biggerNum *= 10;
                biggerNum += digitsSorted[index];
                digLen++;
            }
            index++;
        }
        long smallerNum = 0;
        digLen = 0;
        index = digitsSorted.length - 1;
        while (digLen < numberLen) {
            if (digitsSorted[index] == firstPos && !isUsed) {
                isUsed = true;
            } else {
                smallerNum *= 10;
                smallerNum += digitsSorted[index];
                digLen++;
            }
            index--;
        }

        return biggerNum - smallerNum;
    }
}
