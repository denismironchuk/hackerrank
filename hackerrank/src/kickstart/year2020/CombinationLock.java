package kickstart.year2020;

import java.util.Arrays;

public class CombinationLock {
    public static void main(String[] args) {
        //Test only even wings amount (2, 4, 6, ...)
        int w = 500;
        int n = 10000;
        while (true) {
            int[] vals = new int[w];
            for (int i = 0; i < w; i++) {
                vals[i] = (int) (Math.random() * n);
            }

            //int[] vals = new int[] {1, 1, 9, 9, 6, 4};
            //Arrays.stream(vals).forEach(System.out::println);
            //int resOptimal = countOptimalOdd(vals, w, n);
            int resOptimal = countOptimal(vals, w, n);
            int resTrivial = countTrivial(vals, w, n);
            System.out.println("==============");
            System.out.println(resOptimal);
            System.out.println(resTrivial);
            if (resOptimal != resTrivial) {
                throw new RuntimeException();
            }
        }
    }

    private static int countTrivial(int[] vals, int w, int n) {
        int res = Integer.MAX_VALUE;
        for (int commonVal = 0; commonVal < n; commonVal++) {
        //for (int commonVal : vals) {
            int candidate = 0;
            for (int val : vals) {
                candidate += Math.min(Math.abs(commonVal - val), n - Math.abs(commonVal - val));
            }
            res = Math.min(res, candidate);
        }
        return res;
    }

    private static int countOptimal(int[] vals, int w, int n) {
        Arrays.sort(vals);
        int[] segLen = new int[w];
        for (int i = 0; i < w; i++) {
            segLen[i] = (vals[(i + 1) % w] - vals[i] + n) % n;
        }
        int segsToSum = (w / 2);
        int[] clockwiseSum = new int[w];
        for (int i = 0; i < segsToSum; i++) {
            clockwiseSum[0] += segLen[i];
        }
        for (int i = 1; i < w; i++) {
            clockwiseSum[i] = clockwiseSum[i - 1] - segLen[(i - 1 + w) % w] + segLen[(i + segsToSum - 1 + w) % w];
        }
        int[] conClockwiseSum = new int[w];
        for (int i = 0; i < segsToSum; i++) {
            conClockwiseSum[0] += segLen[(w - i) % w];
        }
        for (int i = 1; i < w; i++) {
            conClockwiseSum[i] = conClockwiseSum[i - 1] + segLen[i] - segLen[(i - segsToSum + w) % w];
        }

        //calculate min moves. Start from 0 interval
        int minMoves = segsToSum * segLen[0];
        for (int i = 1; i < segsToSum; i++) {
            minMoves += (segsToSum - i) * (segLen[(w - i) % w] + segLen[i]);
        }
        int prevMoves = minMoves;
        for (int i = 1; i < w; i++) {
            int nextMoves = prevMoves - conClockwiseSum[(w + i - 1) % w] + clockwiseSum[i];
            if (nextMoves < minMoves) {
                minMoves = nextMoves;
            }
            prevMoves = nextMoves;
        }
        return minMoves;
    }

    private static int countOptimalOdd(int[] vals, int w, int n) {
        Arrays.sort(vals);
        int[] segLen = new int[w];
        for (int i = 0; i < w; i++) {
            segLen[i] = (vals[(i + 1) % w] - vals[i] + n) % n;
        }
        int segsToSum = (w / 2);
        int[] clockwiseSum = new int[w];
        for (int i = 0; i < segsToSum; i++) {
            clockwiseSum[0] += segLen[i];
        }
        for (int i = 1; i < w; i++) {
            clockwiseSum[i] = clockwiseSum[i - 1] - segLen[(i - 1 + w) % w] + segLen[(i + segsToSum - 1 + w) % w];
        }
        int[] conClockwiseSum = new int[w];
        for (int i = 0; i < segsToSum; i++) {
            conClockwiseSum[0] += segLen[(w - i) % w];
        }
        for (int i = 1; i < w; i++) {
            conClockwiseSum[i] = conClockwiseSum[i - 1] + segLen[i] - segLen[(i - segsToSum + w) % w];
        }

        //calculate min moves. Start from 0 interval
        int minMoves = segsToSum * (segLen[0] + segLen[1]);
        for (int i = 1; i < segsToSum; i++) {
            minMoves += (segsToSum - i) * (segLen[(w - i) % w] + segLen[(i + 1) % w]);
        }
        int prevMoves = minMoves;
        for (int i = 1; i < w; i++) {
            int nextMoves = prevMoves - conClockwiseSum[(w + i - 1) % w] + clockwiseSum[(i + 1) % w];
            if (nextMoves < minMoves) {
                minMoves = nextMoves;
            }
            prevMoves = nextMoves;
        }
        return minMoves;
    }
}
