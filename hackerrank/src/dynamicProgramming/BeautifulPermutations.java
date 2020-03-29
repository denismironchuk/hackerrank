package dynamicProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BeautifulPermutations {
    private static final long MOD = 1000000007;
    private static final long TWO = (MOD + 1) / 2;

    private static long fact(long n) {
        long f = 1;
        for (long i = 2; i <= n; i++) {
            f = (f * i) % MOD;
        }
        return f;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());
        for (int q = 0; q < Q; q++) {
            int n = Integer.parseInt(br.readLine());
            long[] a = new long[n];
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                a[i] = Long.parseLong(tkn.nextToken());
            }
            Arrays.sort(a);
            int doubleLen = 0;
            for (int i = 0; i < n - 1; i++) {
                if (a[i] == a[i + 1]) {
                    doubleLen++;
                }
            }
            int elmnLen = n - doubleLen;
            long[] res = new long[doubleLen + 2];
            res[0] = fact(elmnLen);

            for (int i = 0; i < doubleLen; i++) {
                long prev = 0;
                for (int j = 0; j < i + 2; j++) {
                    long newVal = (((res[j + 1] * (j + 1)) % MOD) + ((res[j] * (elmnLen - j - 1))) % MOD) % MOD;
                    newVal *= TWO;
                    newVal %= MOD;

                    newVal += prev;
                    newVal %= MOD;

                    prev = res[j];
                    res[j] = newVal;
                }
                elmnLen += 1;
            }
            System.out.println(res[0]);
        }
    }
}
