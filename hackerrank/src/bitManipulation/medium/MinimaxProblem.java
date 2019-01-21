package bitManipulation.medium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MinimaxProblem {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        long[] a = new long[n];
        long[] aRem = new long[n];

        StringTokenizer aTkn = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            a[i] = Long.parseLong(aTkn.nextToken());
            aRem[i] = a[i];
        }

        boolean hasNonZeros = true;
        long lastDiffBit = -1;
        long bitCnt = 1;

        while (hasNonZeros) {
            boolean hasZeros = false;
            boolean hasOnes = false;
            hasNonZeros = false;

            for (int i = 0; i < n; i++) {
                long bit = aRem[i] % 2;
                hasZeros = hasZeros || bit == 0;
                hasOnes = hasOnes || bit == 1;

                aRem[i] /= 2;
                hasNonZeros = hasNonZeros || aRem[i] != 0;
            }

            if (hasZeros && hasOnes) {
                lastDiffBit = bitCnt;
            }

            bitCnt *= 2;
        }

        if (lastDiffBit == -1) {
            System.out.println(0);
        } else {
            List<Long> zeroBits = new ArrayList<>();
            List<Long> oneBits = new ArrayList<>();

            for (long ai : a) {
                if ((ai & lastDiffBit) == 0) {
                    zeroBits.add(ai);
                } else {
                    oneBits.add(ai);
                }
            }

            long minVal = Long.MAX_VALUE;

            for (long zeroBit : zeroBits) {
                for (long oneBit : oneBits) {
                    long xor = zeroBit ^ oneBit;
                    if (xor < minVal) {
                        minVal = xor;
                    }
                }
            }

            System.out.println(minVal);
        }
    }
}
