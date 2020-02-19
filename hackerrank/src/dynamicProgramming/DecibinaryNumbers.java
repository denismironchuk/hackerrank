package dynamicProgramming;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DecibinaryNumbers {
    public static void main(String [] args) throws IOException {
        //long maxX = 100;
        //List<Long> decimalCounts = buildDecibinaryCountList(maxX);
        //System.out.println(decimalCounts);
        //int lastDecimalValue = decimalCounts.size();
        int lastDecimalValue = 10;
        int maxBinaryLen = getBinaryLength(lastDecimalValue);
        long[][][] dyn = new long[lastDecimalValue + 1][maxBinaryLen][10];
        for (int i = 0; i < maxBinaryLen; i++) {
            dyn[0][i][0] = 1;
        }

        System.out.println(0);
        print(dyn[0]);
        System.out.println("=================");

        for (int i = 1; i < 10; i++) {
            dyn[i][0][i] = 1;
        }
        for (int decimalVal = 1; decimalVal <= lastDecimalValue; decimalVal++) {
            int binaryLen = getBinaryLength(decimalVal);
            int mul = 2;
            for (int decibinaryPosition = 1; decibinaryPosition < binaryLen; decibinaryPosition++) {
                for (int positionDigit = 0; positionDigit < 10 && decimalVal - mul * positionDigit >= 0; positionDigit++) {
                    for (int d = 0; d < 10; d++) {
                        dyn[decimalVal][decibinaryPosition][positionDigit] += dyn[decimalVal - mul * positionDigit][decibinaryPosition - 1][d];
                    }
                }
                mul *= 2;
            }

            for (int decibinaryPosition = binaryLen; decibinaryPosition < maxBinaryLen; decibinaryPosition++) {
                for (int d = 0; d < 10; d++) {
                    dyn[decimalVal][decibinaryPosition][0] += dyn[decimalVal][binaryLen - 1][d];
                }
            }

            System.out.println(decimalVal);
            print(dyn[decimalVal]);
            System.out.println("=================");
        }

        System.out.println();
    }

    private static void print(long[][] arr) {
        int rows = arr.length;
        int cols = arr[0].length;

        for (int i = 0; i < cols; i++) {
            for (int j = rows - 1; j >= 0; j--) {
                System.out.printf("%3d ", arr[j][i]);
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

    private static List<Long> buildDecibinaryCountList(long lastVal) {
        List<Long> res = new ArrayList<>();
        long cnt = 1;
        res.add(1l);

        int currVal = 1;
        while (cnt < lastVal) {
            long currLen = 0;
            for (int i = currVal % 2; (currVal - i) / 2 >= 0 && i < 10; i += 2) {
                currLen += res.get((currVal - i) / 2);
            }
            res.add(currLen);
            cnt += currLen;
            currVal++;
        }

        return res;
    }
}
