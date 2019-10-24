package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LucyAndFlowers {
    private static final long MOD = 1000000009;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        int[] ns = new int[T];
        int maxN = -1;

        for (int t = 0; t < T; t++) {
            ns[t] = Integer.parseInt(br.readLine());
            if (ns[t] > maxN) {
                maxN = ns[t];
            }
        }

        long[][] pascal = new long[maxN + 1][maxN + 2];
        for (int i = 0; i <= maxN; i++) {
            pascal[i][0] = 1;
            for (int j = 1; j <= i; j++) {
                pascal[i][j] = (pascal[i - 1][j] + pascal[i - 1][j - 1]) % MOD;
            }
        }

        long[] catalans = new long[maxN + 1];
        catalans[0] = 1;
        for (int i = 1; i <= maxN; i++) {
            for (int j = 0; j < i; j++) {
                catalans[i] = (catalans[i] + (catalans[j] * catalans[i - j - 1]) % MOD) % MOD;
            }
        }

        for (int n : ns) {
            long res = 0;
            for (int i = 1; i <= n; i++) {
                res = (res + (pascal[n][i] * catalans[i]) % MOD) % MOD;
            }
            System.out.println(res);
        }
    }
}
