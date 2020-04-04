package dynamicProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ChoosingWhiteBalls {
    public static long binToDec(int[] bin) {
        long mul = 1;
        long dec = 0;

        for (int i = bin.length - 1; i >= 0; i--) {
            dec += mul * bin[i];
            mul *= 2;
        }

        return dec;
    }

    private static void countExpectation(Long state, int len, int remainAttempts, Map<Integer, Map<Long, Double>> expectations) {
        Map<Long, Double> l1Map = expectations.get(len - 1);
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
            long nextState = ((state >> (pos + 1)) << pos) + (state & (mul - 1));

            if (!l1Map.containsKey(nextState)) {
                countExpectation(nextState, len - 1, remainAttempts - 1, expectations);
            }

            localExps[pos] = ((double)digit + l1Map.get(nextState)) / ((double)len);

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
            Map<Integer, Map<Long, Double>> stateExp = new HashMap<>();
            Map<Long, Double> initLen = new HashMap<>();
            stateExp.put(n, initLen);
            long initState = binToDec(balls);
            countExpectation(initState, n, k, stateExp);
            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + "ms");
            System.out.printf("%.6f", initLen.get(initState));
        }
    }
}
