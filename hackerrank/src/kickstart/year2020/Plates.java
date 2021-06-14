package kickstart.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Plates {
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());

            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                int k = Integer.parseInt(tkn1.nextToken());
                int p = Integer.parseInt(tkn1.nextToken());

                int[][] platesStacks = new int[n][k];
                for (int i = 0; i < n; i++) {
                    StringTokenizer stackTkn = new StringTokenizer(br.readLine());
                    for (int j = 0; j < k; j++) {
                        platesStacks[i][j] = Integer.parseInt(stackTkn.nextToken());
                    }
                }

                int[][] beautyCnt = new int[n][k];
                for (int i = 0; i < n; i++) {
                    beautyCnt[i][0] = platesStacks[i][0];
                    for (int j = 1; j < k; j++) {
                        beautyCnt[i][j] = platesStacks[i][j] + beautyCnt[i][j - 1];
                    }
                }

                int[][] dyn = new int[n][p + 1];

                for (int i = 1; i <= p; i++) {
                    if (i <= k) {
                        dyn[0][i] = beautyCnt[0][i - 1];
                    } else {
                        dyn[0][i] = beautyCnt[0][k - 1];
                    }
                }

                for (int i = 1; i < n; i++) {
                    for (int j = 1; j <= p; j++) {
                        for (int c = 0; c < k; c++) {
                            if ((j - (c + 1)) < 0) {
                                dyn[i][j] = Math.max(dyn[i][j], dyn[i - 1][j]);
                            } else {
                                dyn[i][j] = Math.max(Math.max(dyn[i][j], dyn[i - 1][j]), dyn[i - 1][j - (c + 1)] + beautyCnt[i][c]);
                            }
                        }
                    }
                }

                System.out.printf("Case #%s: %s\n", t, dyn[n - 1][p]);
            }
        }
    }
}
