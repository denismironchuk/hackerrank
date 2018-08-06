import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class LegoBlocks {
    public static final long MOD = 1000000007;

    private static long pow(long n, long p) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return pow((n * n) % MOD, p / 2) % MOD;
        } else {
            return (n * pow(n, p - 1)) % MOD;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            String input = br.readLine();
            StringTokenizer inputTkn = new StringTokenizer(input, " ");
            int height = Integer.parseInt(inputTkn.nextToken());
            int len = Integer.parseInt(inputTkn.nextToken());

            long[] all = new long[len + 1];
            long[] oneLine = new long[len + 1];
            oneLine[0] = 1;

            for (int i = 1; i <= len; i++) {
                for (int blocLen = 4; blocLen > 0; blocLen--) {
                    if (i - blocLen < 0) {
                        continue;
                    }

                    oneLine[i] = (oneLine[i] + oneLine[i - blocLen]) % MOD;
                }
                all[i] = pow(oneLine[i], height);
            }

            long[] solid = new long[len + 1];
            long[] notSolid = new long[len + 1];

            solid[1] = 1;
            notSolid[1] = 0;

            for (int i = 2; i <= len; i++) {
                for (int j = 1; j < i; j++) {
                    notSolid[i] = (notSolid[i] + ((all[j] * solid[i - j]) % MOD)) % MOD;
                }
                solid[i] = (all[i] - notSolid[i] + MOD) % MOD;
            }

            System.out.println(solid[len]);
        }
    }
}
