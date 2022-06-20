package codejam.year2022.round1B;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LongSummaryStatistics;
import java.util.StringTokenizer;
import java.util.stream.LongStream;

public class ControlledInflation {
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                int p = Integer.parseInt(tkn1.nextToken());
                long[][] limits = new long[n + 1][2];
                limits[0][0] = 0;
                limits[0][1] = 0;
                for (int i = 1; i <= n; i++) {
                    StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                    LongSummaryStatistics stat = LongStream.range(0, p).map(j -> Long.parseLong(tkn2.nextToken())).sorted().summaryStatistics();
                    long min = stat.getMin();
                    long max = stat.getMax();
                    limits[i][0] = min;
                    limits[i][1] = max;
                }

                //0 - go down
                //1 - go up
                long[][] dynTable = new long[n + 1][2];
                dynTable[0][0] = 0;
                dynTable[0][1] = 0;
                for (int i = 1; i <= n; i++) {
                    long height = limits[i][1] - limits[i][0];
                    dynTable[i][1] = Math.min(dynTable[i - 1][1] + Math.abs(limits[i - 1][1] - limits[i][0]) + height,
                            dynTable[i - 1][0] + Math.abs(limits[i - 1][0] - limits[i][0]) + height);
                    dynTable[i][0] = Math.min(dynTable[i - 1][1] + Math.abs(limits[i - 1][1] - limits[i][1]) + height,
                            dynTable[i - 1][0] + Math.abs(limits[i - 1][0] - limits[i][1]) + height);
                }
                System.out.printf("Case #%s: %s\n", t, Math.min(dynTable[n][0], dynTable[n][1]));
            }
        }
    }
}
