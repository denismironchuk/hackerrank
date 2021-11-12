package codejam.year2018.round2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class GracefullChainsawJugglingFinal {
    public static void main(String[] args) throws IOException {
        int aMax = 500;
        int bMax = 500;
        int[][][] maxLen = new int[aMax + 1][bMax + 1][Math.max(aMax, bMax) + 2];

        for (int a = 0; a <= aMax; a++) {
            for (int b = 0; b <= bMax; b++) {
                //val = 0
                int len = 1;
                while (true) {
                    int risingSum = len + ((len * (len - 1)) / 2);
                    if (risingSum > b) {
                        break;
                    }
                    maxLen[a][b][0] = Math.max(maxLen[a][b - risingSum][1] + len, maxLen[a][b][0]);
                    len++;
                }
                //val > 0
                for (int val = 1; val <= a; val++) {
                    for (len = 1; val * len <= a; len++) {
                        int nonZeroLen = len - 1;
                        int risingSum = nonZeroLen + ((nonZeroLen) * (nonZeroLen - 1) / 2);
                        if (risingSum > b) {
                            break;
                        }
                        if (a == val * len) {
                            risingSum = b;
                        }

                        maxLen[a][b][val] = Math.max(maxLen[a - val * len][b - risingSum][val + 1] + len, maxLen[a][b][val]);
                    }
                }

                for (int i = maxLen[0][0].length - 2; i >= 0; i--) {
                    maxLen[a][b][i] = Math.max(maxLen[a][b][i], maxLen[a][b][i + 1]);
                }
            }
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                int a = Integer.parseInt(tkn.nextToken());
                int b = Integer.parseInt(tkn.nextToken());
                System.out.printf("Case #%s: %s\n", t, maxLen[a][b][0]);
            }
        }
    }
}
