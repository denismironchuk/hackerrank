package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class HyperrectGCD {
    public static final long MOD = 1000000007;
    public static final int MAX = 100000;

    public static void main(String[] args) throws IOException {
        long[] phi = new long[MAX + 1];
        for (int i = 1; i <= MAX; i++) {
            phi[i] = i;
        }
        int[] isPrime = new int[MAX + 1];
        Arrays.fill(isPrime, 1);
        for (int i = 2; i <= MAX; i++) {
            if (isPrime[i] == 1) {
                for (int j = 2; j * i <= MAX; j++) {
                    isPrime[i * j] = 0;
                    phi[i * j] -= phi[i * j] / i;
                }
            }
        }

        for (int i = 2; i <= MAX; i++) {
            if (isPrime[i] == 1) {
                phi[i] = i - 1;
            }
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 0; t < T; t++) {
                int k = Integer.parseInt(br.readLine());
                StringTokenizer nTkn = new StringTokenizer(br.readLine());
                long[] n = new long[k];
                long minN = Long.MAX_VALUE;

                for (int i = 0; i < k; i++) {
                    n[i] = Long.parseLong(nTkn.nextToken());
                    if (n[i] < minN) {
                        minN = n[i];
                    }
                }

                long res = 0;

                for (int div = 1; div <= minN; div++) {
                    long  cnt = 1;
                    for (int j = 0; j < k; j++) {
                        cnt *= n[j] / (long)div;
                        cnt %= MOD;
                    }
                    res += (cnt * phi[div]) % MOD;
                    res %= MOD;
                }

                System.out.println(res);
            }
        }
    }
}
