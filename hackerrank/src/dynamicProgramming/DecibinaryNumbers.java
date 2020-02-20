package dynamicProgramming;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DecibinaryNumbers {
    private static int CNT = 0;

    public static void main(String [] args) {
        Date start = new Date();
        int lastDecimalValue = 300000;
        int maxBinaryLen = getBinaryLength(lastDecimalValue);
        long[][][] dyn = new long[lastDecimalValue + 1][maxBinaryLen][10];
        for (int i = 0; i < maxBinaryLen; i++) {
            dyn[0][i][0] = 1;
        }

        /*System.out.println(0);
        print(dyn[0]);
        System.out.println("=================");*/

        for (int i = 1; i < 10; i++) {
            dyn[i][0][i] = 1;
        }

        for (int decimalVal = 1; decimalVal <= lastDecimalValue; decimalVal++) {
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

            /*System.out.println(decimalVal);
            print(dyn[decimalVal]);
            System.out.println("=================");*/
        }
        Date end = new Date();
        System.out.println(end.getTime() - start.getTime() + "ms");
        System.out.println(CNT);
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
