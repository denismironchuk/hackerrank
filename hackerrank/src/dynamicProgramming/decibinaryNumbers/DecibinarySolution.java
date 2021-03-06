package dynamicProgramming.decibinaryNumbers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class DecibinarySolution {
    public static void main(String[] args) throws IOException {
        //try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\dmiro\\Downloads\\input08.txt"))) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int Q = Integer.parseInt(br.readLine());
            long[] queries = new long[Q];
            long max = -1;
            for (int q = 0; q < Q; q++) {
                long x = Long.parseLong(br.readLine());
                queries[q] = x;
                if (x > max) {
                    max = x;
                }
            }
            Date start = new Date();
            int n = getMaxDecimalVal(max - 1);

            int maxBinaryLen = getBinaryLength(n) + 1;
            long[][] dyn = buildDynamicTable(n, maxBinaryLen);

            long[] counts = new long[n + 1];
            counts[0] = dyn[0][maxBinaryLen - 1];

            for (int i = 1; i <= n; i++) {
                counts[i] = counts[i - 1] + dyn[i][maxBinaryLen - 1];
            }

            StringBuilder res = new StringBuilder();
            for (long x : queries) {
                res.append(findNthDecibinaryVal(x, counts, dyn)).append("\n");
            }

            System.out.println(res);

            Date end = new Date();
            //System.out.println(end.getTime() - start.getTime() + "ms");
        }
    }

    private static long[][] buildDynamicTable(int n, int maxBinaryLen) {
        long[][] dyn = new long[n + 1][maxBinaryLen];
        for (int i = 0; i < maxBinaryLen; i++) {
            dyn[0][i] = 1;
        }

        int binaryLen = 1;
        int increaseThreshold = 2;
        for (int decimalVal = 1; decimalVal <= n; decimalVal++) {
            if (decimalVal == increaseThreshold) {
                binaryLen++;
                increaseThreshold <<= 1;
            }
            long positionSum = decimalVal < 10 ? 1 : 0;
            for (int decibinaryPosition = 1; decibinaryPosition < binaryLen; decibinaryPosition++) {
                dyn[decimalVal][decibinaryPosition] = positionSum;
                for (int positionDigit = 1; positionDigit < 10 && decimalVal - (positionDigit << decibinaryPosition) >= 0; positionDigit++) {
                    positionSum += dyn[decimalVal - (positionDigit << decibinaryPosition)][decibinaryPosition];
                }
            }

            for (int decibinaryPosition = binaryLen; decibinaryPosition < maxBinaryLen; decibinaryPosition++) {
                dyn[decimalVal][decibinaryPosition] += positionSum;
            }
        }
        return dyn;
    }

    private static long findNthDecibinaryVal(long n, long[] counts, long[][] dyn) {
        int decimalVal = findDecimalVal(counts, n, 0, counts.length - 1);

        if (decimalVal == 0) {
            return buildNthDecibinaryNumber(n, decimalVal, dyn);
        } else {
            return buildNthDecibinaryNumber(n - counts[decimalVal - 1], decimalVal, dyn);
        }
    }

    private static int findClosestSmallerDecimalPos(long position, long[] dyn, int start, int end) {
        if (start == end) {
            return start;
        }

        int middle = 1 + (start + end) / 2;
        if (dyn[middle] >= position) {
            return findClosestSmallerDecimalPos(position, dyn, start, middle - 1);
        } else {
            return findClosestSmallerDecimalPos(position, dyn, middle, end);
        }
    }

    private static long buildNthDecibinaryNumber(long position, int decimalVal, long[][] dyn) {
        if (position == 1 && decimalVal < 10) {
            return decimalVal;
        }

        int decimalPos = findClosestSmallerDecimalPos(position, dyn[decimalVal], 0, dyn[decimalVal].length - 1);
        long mul10 = fastPowLong(10, decimalPos);

        int digit = 0;
        long sum = 0;
        while (digit < 10 && sum + dyn[decimalVal - (digit << decimalPos)][decimalPos] < position) {
            sum += dyn[decimalVal - (digit << decimalPos)][decimalPos];
            digit++;
        }

        return mul10 * (long) digit + buildNthDecibinaryNumber(position - sum, decimalVal - (digit << decimalPos), dyn);
    }

    private static long fastPowLong(long n, int pow) {
        if (pow == 0) {
            return 1;
        }

        if (pow % 2 == 0) {
            return fastPowLong(n * n, pow / 2);
        } else {
            return n * fastPowLong(n, pow - 1);
        }
    }

    private static int findDecimalVal(long[] counts, long decibinaryPosition, int start, int end) {
        if (start == end) {
            return start;
        }

        int middle = (start + end) / 2;
        if (counts[middle] < decibinaryPosition) {
            return findDecimalVal(counts, decibinaryPosition, middle + 1, end);
        } else {
            return findDecimalVal(counts, decibinaryPosition, start, middle);
        }
    }

    private static int getMaxDecimalVal(long pos) {
        long[] cnt = new long[300000];
        long sum = 0;
        cnt[0] = 1;

        if (pos == 0) {
            return 0;
        }

        int res = 1;
        for (; sum < pos; res++) {
            for (int i = res % 2; (res - i) / 2 >= 0 && i < 10; i += 2) {
                cnt[res] += cnt[(res - i) / 2];
            }
            sum += cnt[res];
        }

        res--;

        return res;
    }

    private static int getBinaryLength(int val) {
        int len = 0;
        while (val != 0) {
            val /= 2;
            len++;
        }

        return len;
    }
}
