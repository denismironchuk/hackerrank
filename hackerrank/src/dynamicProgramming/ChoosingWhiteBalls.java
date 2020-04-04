package dynamicProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ChoosingWhiteBalls {
    private static final int ARR_LIM = 21;

    public static int binToDec(int[] bin) {
        int mul = 1;
        int dec = 0;

        for (int i = bin.length - 1; i >= 0; i--) {
            dec += mul * bin[i];
            mul *= 2;
        }

        return dec;
    }

    private static void countExpectationArray(int state, int len, int remainAttempts, double[][] expectations) {
        if (remainAttempts == 0) {
            expectations[len][state] = 0;
            return;
        }

        int mul = 1;
        double[] localExps = new double[len];
        for (int pos = 0; pos < len; pos++) {
            double digit = (state >> pos) & 1;
            int nextState = ((state >> (pos + 1)) << pos) + (state & (mul - 1));

            if (expectations[len - 1][nextState] == -1) {
                countExpectationArray(nextState, len - 1, remainAttempts - 1, expectations);
            }

            localExps[pos] = (digit + expectations[len - 1][nextState]) / ((double)len);

            mul <<= 1;
        }

        double resExp = len % 2 == 0 ? 0d : localExps[len / 2];

        for (int i = 0; i < len / 2; i++) {
            resExp += Math.max(localExps[i], localExps[len - i - 1]) * 2;
        }

        expectations[len][state] = resExp;
    }

    private static void countExpectation(int state, int len, int remainAttempts, Map<Integer, Map<Integer, Double>> expectations, double[][] expectationsArr) {
        Map<Integer, Double> l1Map = expectations.get(len - 1);
        if (l1Map == null) {
            l1Map = new HashMap<>();
            expectations.put(len - 1, l1Map);
        }

        if (remainAttempts == 0) {
            expectations.get(len).put(state, 0d);
            return;
        }

        int mul = 1;
        double[] localExps = new double[len];
        for (int pos = 0; pos < len; pos++) {
            long digit = (state >> pos) & 1;
            int nextState = ((state >> (pos + 1)) << pos) + (state & (mul - 1));

            if (len == ARR_LIM) {
                if (expectationsArr[len - 1][nextState] == -1) {
                    countExpectationArray(nextState, len - 1, remainAttempts - 1, expectationsArr);
                }

                localExps[pos] = (digit + expectationsArr[len - 1][nextState]) / ((double)len);
            } else {
                if (!l1Map.containsKey(nextState)) {
                    countExpectation(nextState, len - 1, remainAttempts - 1, expectations, expectationsArr);
                }

                localExps[pos] = ((double) digit + l1Map.get(nextState)) / ((double) len);
            }

            mul <<= 1;
        }

        double resExp = len % 2 == 0 ? 0d : localExps[len / 2];

        for (int i = 0; i < len / 2; i++) {
            resExp += Math.max(localExps[i], localExps[len - i - 1]) * 2;
        }

        expectations.get(len).put(state, resExp);
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(tkn1.nextToken());
            int k = Integer.parseInt(tkn1.nextToken());
            int[] balls = new int[n];
            String ballsStr = br.readLine();
            for (int i = 0; i < n; i++) {
                balls[i] = ballsStr.charAt(i) == 'W' ? 1 : 0;
            }

            Date start = new Date();
            int initState = binToDec(balls);
            if (n < ARR_LIM) {
                double[][] stateArr = new double[n + 1][(int)Math.pow(2, n + 1)];
                for (int i = 0; i < stateArr.length; i++) {
                   Arrays.fill(stateArr[i], -1);
                }
                countExpectationArray(initState, n, k, stateArr);
                System.out.printf("%.6f\n", stateArr[n][initState]);
            } else {
                double[][] stateArr = new double[ARR_LIM][(int)Math.pow(2, ARR_LIM)];
                for (int i = 0; i < stateArr.length; i++) {
                    Arrays.fill(stateArr[i], -1);
                }

                Map<Integer, Map<Integer, Double>> stateExp = new HashMap<>();
                Map<Integer, Double> initLen = new HashMap<>();
                stateExp.put(n, initLen);
                countExpectation(initState, n, k, stateExp, stateArr);
                System.out.printf("%.6f\n", initLen.get(initState));
            }
            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + "ms");
        }
    }
}
