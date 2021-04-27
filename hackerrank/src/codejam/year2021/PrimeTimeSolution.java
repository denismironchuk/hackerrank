package codejam.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class PrimeTimeSolution {

    private static Map<Integer, Long> factorize(long n, int[] primes, long[] counts) {
        Map<Integer, Long> res = new HashMap<>();

        for (int i = 0; i < primes.length; i++) {
            if (n % primes[i] == 0) {
                long cnt = 0;
                while (n % primes[i] == 0) {
                    n /= primes[i];
                    cnt++;
                }

                if (cnt > counts[i]) {
                    return Collections.EMPTY_MAP;
                }

                res.put(primes[i], cnt);
            }
        }

        if (n == 1) {
            return res;
        } else {
            return Collections.EMPTY_MAP;
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int m = Integer.parseInt(br.readLine());
                int[] primes = new int[m];
                long[] counts = new long[m];
                long maxSum = 0;
                for (int i = 0; i < m; i++) {
                    StringTokenizer tkn = new StringTokenizer(br.readLine());
                    primes[i] = Integer.parseInt(tkn.nextToken());
                    counts[i] = Long.parseLong(tkn.nextToken());
                    maxSum += primes[i] * counts[i];
                }

                long maxLim = 0;
                for (int i = 0; i < m; i++) {
                    maxLim += primes[i] * Math.ceil(Math.log(maxSum) / Math.log(primes[i]));
                }

                boolean hasSolution = false;
                for (long s = 2; s <= maxLim; s++) {
                    if (maxSum - s < 2) {
                        break;
                    }
                    Map<Integer, Long> fact = factorize(maxSum - s, primes, counts);
                    if (!fact.isEmpty()) {
                        long primesSum = 0;
                        for (Map.Entry<Integer, Long> entry : fact.entrySet()) {
                            long p = entry.getKey();
                            long cnt = entry.getValue();
                            primesSum += p * cnt;
                        }

                        if (primesSum == s) {
                            System.out.printf("Case #%s: %s\n", t, maxSum - s);
                            hasSolution = true;
                            break;
                        }
                    }
                }

                if (!hasSolution) {
                    System.out.printf("Case #%s: %s\n", t, 0);
                }
            }
        }
    }
}
