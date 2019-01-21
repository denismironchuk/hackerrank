package bitManipulation.medium;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WinningLotteryTicket {
    private static final int[] BITS = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512};

    private static int convertString(String str) {
        int res = 0;
        int[] bitsSet = new int[10];

        for (char c : str.toCharArray()) {
            int dig = c - '0';
            if (bitsSet[dig] == 0) {
                res += BITS[dig];
                bitsSet[dig] = 1;
            }
        }
        return res;
    }

    public static void main(String[] args) throws IOException {
        //BufferedReader br = new BufferedReader(new FileReader("D:/test.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        int[] tikets = new int[n];
        long[] tickCnt = new long[1024];

        for (int i = 0; i < n; i++) {
            tikets[i] = convertString(br.readLine());
            tickCnt[tikets[i]]++;
        }

        long resCnt = 0;

        for (int i = 0; i < 1023; i++) {
            for (int j = i; j < 1024; j++) {
                if ((i | j) == 1023) {
                    resCnt += tickCnt[i] * tickCnt[j];
                }
            }
        }
        resCnt += (tickCnt[1023] * (tickCnt[1023] - 1)) / 2;
        System.out.println(resCnt);
    }
}
