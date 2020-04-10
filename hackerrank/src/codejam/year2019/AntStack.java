package codejam.year2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class AntStack {

    public static final int COLS = 150;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            int n = Integer.parseInt(br.readLine());
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            long[] w = new long[n];
            for (int i = 0; i < n; i++) {
                w[i] = Long.parseLong(tkn.nextToken());
            }

            long[][] dynTable = new long[n][COLS];
            for (int row = 0; row < n; row++) {
                for (int col = 1; col < COLS; col++) {
                    dynTable[row][col] = Long.MAX_VALUE;
                }
            }

            dynTable[0][1] = w[0];
            for (int row = 1; row < n; row++) {
                for (int col = 1; col < COLS; col++) {
                    if (w[row] * 6 >= dynTable[row - 1][col - 1]) {
                        dynTable[row][col] = Math.min(dynTable[row - 1][col], dynTable[row - 1][col - 1] + w[row]);
                    } else {
                        dynTable[row][col] = dynTable[row - 1][col];
                    }
                }
            }

            int res = 1;
            while(dynTable[n - 1][res] != Long.MAX_VALUE) {
                res++;
            }

            System.out.printf("Case #%s: %s\n", t + 1, res - 1);
        }
    }
}
