package dynamicProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DecibinaryNumbers {
    public static void main(String [] args) throws IOException {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int Q = Integer.parseInt(br.readLine());
            long[] xArr = new long[Q];
            long maxX = -1;
            for (int q = 0; q < Q; q++) {
                xArr[q] = Long.parseLong(br.readLine());
                if (xArr[q] > maxX) {
                    maxX = xArr[q];
                }
            }

            List<Long> decimalCounts = buildDecibinaryCountList(maxX);
            System.out.println(decimalCounts);

            int lastDecimalValue = decimalCounts.size();
            int maxBinaryLen = getBinaryLength(lastDecimalValue);
            long[][][] dyn = new long[lastDecimalValue + 1][maxBinaryLen][10];
            dyn[0][0][0] = 1;

            for (int decimalVal = 1; decimalVal <= lastDecimalValue; decimalVal++) {
                int binaryLen = getBinaryLength(decimalVal);
                int mul = 1;
                if (decimalVal < 10) {
                    dyn[decimalVal][0][decimalVal] = 1;
                }
                for (int decibinaryPosition = 1; decibinaryPosition < binaryLen; decibinaryPosition++) {
                    for (int positionDigit = 1; positionDigit < 10; positionDigit++) {
                        for (int d = 0; d < 10; d++) {
                            if (decimalVal - mul * positionDigit >= 0 && decibinaryPosition - 1 >= 0) {
                                dyn[decimalVal][decibinaryPosition][positionDigit] += dyn[decimalVal - mul * positionDigit][decibinaryPosition - 1][d];
                            }
                        }
                    }
                    mul *= 2;
                }
            }
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
