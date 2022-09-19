package kickstart.year2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PalindromicDeletions {
    public static final long MOD = 1000000000 + 7;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int n = Integer.parseInt(br.readLine());
                String s = br.readLine();
                /*
                index 0 - palindrom len
                index 1 - left index
                index 2 - right index
                 */
                long[][][] dynTable = new long[n + 1][n][n];
                //palindrom len = 0
                for (int left = 0; left < n; left++) {
                    for (int right = left; right < n; right++) {
                        dynTable[0][left][right] = 1;
                    }
                }
                //palindrom len = 1
                for (int left = 0; left < n; left++) {
                    for (int right = left; right < n; right++) {
                        dynTable[1][left][right] = right - left + 1;
                    }
                }
                for (int len = 2; len <= n; len++) {
                    for (int left = n - 1; left >= 0; left--) {
                        for (int right = left + len - 1; right < n; right++) {
                            if (len == 2 && left + 1 == right) {
                                dynTable[len][left][right] = s.charAt(left) == s.charAt(right) ? 1 : 0;
                            } else {
                                dynTable[len][left][right] = ((dynTable[len][left + 1][right] + dynTable[len][left][right - 1]) % MOD
                                            - dynTable[len][left + 1][right - 1] + MOD) % MOD;

                                if (s.charAt(left) == s.charAt(right)) {
                                    dynTable[len][left][right] = (dynTable[len][left][right]
                                            + dynTable[len - 2][left + 1][right - 1]) % MOD;
                                }
                            }
                        }
                    }
                }
                long res = 0;
                for (int len = 0; len < n; len++) {
                    long prob = dynTable[len][0][n - 1];
                    long n_ = n;
                    for (long j = n - len; j > 0; j--) {
                        prob = (((prob * j) % MOD) * fastPow(n_, MOD - 2)) % MOD;
                        n_--;
                    }
                    res += prob;
                    res %= MOD;
                }
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }

    public static long fastPow(long n, long pow) {
        if (pow == 0) {
            return 1;
        }

        if (pow % 2 == 0) {
            return fastPow((n * n) % MOD, pow / 2);
        } else {
            return (n * fastPow(n, pow - 1)) % MOD;
        }
    }
}
