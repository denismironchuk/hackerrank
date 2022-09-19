package kickstart.year2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class PalindromicDeletions {
    public static final long MOD = 1000000000 + 7;

    public static void main(String[] args) throws IOException {
        Random rnd1 = new Random();
        long a = rnd1.nextInt((int)MOD) % MOD;
        long b = rnd1.nextInt((int)MOD) % MOD;
        long c = rnd1.nextInt((int)MOD) % MOD;
        System.out.println((a * fastPow(b, MOD - 2)) % MOD);
        System.out.println((((a * c) % MOD) * (fastPow((b * c) % MOD, MOD - 2))) % MOD);


        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                /*int n = Integer.parseInt(br.readLine());
                String s = br.readLine();*/

                int n = 20;
                StringBuilder sBuild = new StringBuilder();
                Random rnd = new Random();
                for (int i = 0; i < n; i++) {
                    sBuild.append((char)(rnd.nextInt(26) + 'A'));
                }
                String s = sBuild.toString();
                System.out.println(s);
                /*
                index 0 - palindrom len
                index 1 - left index
                index 2 - right index
                 */
                long[][][] dynTable = new long[n + 1][n][n];
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

                                    if (len == 2) {
                                        dynTable[len][left][right] = (dynTable[len][left][right] + 1) % MOD;
                                    }
                                }
                            }
                        }
                    }
                }
                //System.out.println();
                long[] trivial = countPalindromsTrivial(s);
                String optRes = Arrays.stream(dynTable).mapToLong(ar -> ar[0][n - 1]).mapToObj(String::valueOf).collect(Collectors.joining(" "));
                String trivRes = Arrays.stream(trivial).mapToObj(String::valueOf).collect(Collectors.joining(" "));
                System.out.println(optRes);
                System.out.println(trivRes);
                if (!optRes.equals(trivRes)) {
                    throw new RuntimeException("!!!!!!!!!!!!!!!!!!");
                }
            }
        }
    }

    public static long[] countPalindromsTrivial(String s) {
        int n = s.length();
        long[] res = new long[n + 1];

        for (int i = 1; i < fastPow(2, n); i++) {
            StringBuilder subS = new StringBuilder();
            int tmp = i;
            int index = 0;
            while (tmp != 0) {
                if (tmp % 2 == 1) {
                    subS.append(s.charAt(index));
                }
                index++;
                tmp /= 2;
            }
            if (isPalindrom(subS.toString())) {
                res[subS.length()]++;
            }
        }

        return res;
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

    public static boolean isPalindrom(String s) {
        boolean isPlndrm = true;
        for (int i = 0; isPlndrm && i < s.length() / 2; i++) {
            isPlndrm = s.charAt(i) == s.charAt(s.length() - 1 - i);
        }
        return isPlndrm;
    }
}
