package kickstart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class CombinationLockSolution {
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int w = Integer.parseInt(tkn1.nextToken());
                long n = Long.parseLong(tkn1.nextToken());
                long[] vals = new long[w];
                StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                for (int i = 0; i < w; i++) {
                    vals[i] = Long.parseLong(tkn2.nextToken()) - 1;
                }
                if (w == 1) {
                    System.out.printf("Case #%s: %s\n", t, 0);
                } else {
                    long res = w % 2 == 0 ? countEven(vals, w, n) : countOdd(vals, w, n);
                    System.out.printf("Case #%s: %s\n", t, res);
                }
            }
        }
    }

    private static long countEven(long[] vals, int w, long n) {
        Arrays.sort(vals);
        long[] segLen = new long[w];
        for (int i = 0; i < w; i++) {
            segLen[i] = (vals[(i + 1) % w] - vals[i] + n) % n;
        }
        int segsToSum = (w / 2);
        long[] clockwiseSum = new long[w];
        for (int i = 0; i < segsToSum; i++) {
            clockwiseSum[0] += segLen[i];
        }
        for (int i = 1; i < w; i++) {
            clockwiseSum[i] = clockwiseSum[i - 1] - segLen[(i - 1 + w) % w] + segLen[(i + segsToSum - 1 + w) % w];
        }
        long[] conClockwiseSum = new long[w];
        for (int i = 0; i < segsToSum; i++) {
            conClockwiseSum[0] += segLen[(w - i) % w];
        }
        for (int i = 1; i < w; i++) {
            conClockwiseSum[i] = conClockwiseSum[i - 1] + segLen[i] - segLen[(i - segsToSum + w) % w];
        }

        //calculate min moves. Start from 0 interval
        long minMoves = segsToSum * segLen[0];
        for (int i = 1; i < segsToSum; i++) {
            minMoves += (segsToSum - i) * (segLen[(w - i) % w] + segLen[i]);
        }
        long prevMoves = minMoves;
        for (int i = 1; i < w; i++) {
            long nextMoves = prevMoves - conClockwiseSum[(w + i - 1) % w] + clockwiseSum[i];
            if (nextMoves < minMoves) {
                minMoves = nextMoves;
            }
            prevMoves = nextMoves;
        }
        return minMoves;
    }

    private static long countOdd(long[] vals, int w, long n) {
        Arrays.sort(vals);
        long[] segLen = new long[w];
        for (int i = 0; i < w; i++) {
            segLen[i] = (vals[(i + 1) % w] - vals[i] + n) % n;
        }
        int segsToSum = (w / 2);
        long[] clockwiseSum = new long[w];
        for (int i = 0; i < segsToSum; i++) {
            clockwiseSum[0] += segLen[i];
        }
        for (int i = 1; i < w; i++) {
            clockwiseSum[i] = clockwiseSum[i - 1] - segLen[(i - 1 + w) % w] + segLen[(i + segsToSum - 1 + w) % w];
        }
        long[] conClockwiseSum = new long[w];
        for (int i = 0; i < segsToSum; i++) {
            conClockwiseSum[0] += segLen[(w - i) % w];
        }
        for (int i = 1; i < w; i++) {
            conClockwiseSum[i] = conClockwiseSum[i - 1] + segLen[i] - segLen[(i - segsToSum + w) % w];
        }

        //calculate min moves. Start from 0 interval
        long minMoves = segsToSum * (segLen[0] + segLen[1]);
        for (int i = 1; i < segsToSum; i++) {
            minMoves += (segsToSum - i) * (segLen[(w - i) % w] + segLen[(i + 1) % w]);
        }
        long prevMoves = minMoves;
        for (int i = 1; i < w; i++) {
            long nextMoves = prevMoves - conClockwiseSum[(w + i - 1) % w] + clockwiseSum[(i + 1) % w];
            if (nextMoves < minMoves) {
                minMoves = nextMoves;
            }
            prevMoves = nextMoves;
        }
        return minMoves;
    }
}
