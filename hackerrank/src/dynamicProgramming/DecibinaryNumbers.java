package dynamicProgramming;

import java.util.Date;

public class DecibinaryNumbers {
    private static int CNT = 0;

    public static void main(String [] args) {
        Date start = new Date();
        int n = 10;
        int maxBinaryLen = getBinaryLength(n) + 1;
        long[][][] dyn = new long[n + 1][maxBinaryLen][10];
        for (int i = 0; i < maxBinaryLen; i++) {
            dyn[0][i][0] = 1;
        }

        System.out.println(0);
        print(dyn[0]);
        System.out.println("=================");

        for (int i = 1; i < 10; i++) {
            dyn[i][0][i] = 1;
        }

        for (int decimalVal = 1; decimalVal <= n; decimalVal++) {
            int binaryLen = getBinaryLength(decimalVal);
            int mul = 2;
            long positionSum = decimalVal < 10 ? 1 : 0;
            for (int decibinaryPosition = 1; decibinaryPosition < binaryLen; decibinaryPosition++) {
                dyn[decimalVal][decibinaryPosition][0] = positionSum;
                for (int positionDigit = 1; positionDigit < 10 && decimalVal - mul * positionDigit >= 0; positionDigit++) {
                    dyn[decimalVal][decibinaryPosition][positionDigit] = dyn[decimalVal - mul * positionDigit][decibinaryPosition][0];
                    positionSum += dyn[decimalVal][decibinaryPosition][positionDigit];
                    CNT++;
                }
                mul *= 2;
            }

            for (int decibinaryPosition = binaryLen; decibinaryPosition < maxBinaryLen; decibinaryPosition++) {
                dyn[decimalVal][decibinaryPosition][0] += positionSum;
                CNT++;
            }

            System.out.println(decimalVal);
            print(dyn[decimalVal]);
            System.out.println("=================");
        }

        long[] counts = new long[n + 1];

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j < 10; j++) {
                counts[i] += dyn[i][maxBinaryLen - 1][j];
            }
        }

        for (int i = 1; i <= n; i++) {
            counts[i] += counts[i - 1];
        }


        for (long i = 1; i < 60; i++) {
            System.out.println(findNthDecibinaryVal(i, counts, dyn));
        }

        //System.out.println(findNthDecibinaryVal(36, counts, dyn));

        System.out.println("==============");
        Date end = new Date();
        System.out.println(end.getTime() - start.getTime() + "ms");
        System.out.println(CNT);
    }

    private static long findNthDecibinaryVal(long n, long[] counts, long[][][] dyn) {
        int decimalVal = findDecimalVal(counts, n, 0, counts.length);

        if (decimalVal == 0) {
            return buildNthDecibinaryNumber(n, decimalVal, dyn);
        } else {
            return buildNthDecibinaryNumber(n - counts[decimalVal - 1], decimalVal, dyn);
        }
    }

    private static long buildNthDecibinaryNumber(long position, int decimalVal, long[][][] dyn) {
        if (position == 1 && decimalVal < 10) {
            return decimalVal;
        }

        int decimalPos = 0;
        int mul2 = 1;
        long mul10 = 1;
        while (dyn[decimalVal][decimalPos][0] < position) {
            decimalPos++;
            mul2 *= 2;
            mul10 *= 10;
        }

        decimalPos--;
        mul2 /= 2;
        mul10 /= 10;

        int digit = 0;
        long sum = 0;
        while (digit < 10 && sum + dyn[decimalVal][decimalPos][digit] < position) {
            sum += dyn[decimalVal][decimalPos][digit];
            digit++;
        }

        return mul10 * (long) digit + buildNthDecibinaryNumber(position - sum, decimalVal - digit * mul2, dyn);
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

    private static void print(long[][] arr) {
        int rows = arr.length;
        int cols = arr[0].length;

        for (int i = 0; i < cols; i++) {
            for (int j = rows - 1; j >= 0; j--) {
                System.out.printf("%5d ", arr[j][i]);
            }
            System.out.println();
        }
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
