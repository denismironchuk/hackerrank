package codejam.year2020.round2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class WormholeInOne {
    private static long countOrientedArea(long[] p1, long[] p2, long[] p3) {
        return (p2[0] - p1[0]) * (p3[1] - p1[1]) - (p2[1] - p1[1]) * (p3[0] - p1[0]);
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int n = Integer.parseInt(br.readLine());
                long[][] points = new long[n][2];
                for (int i = 0; i < n; i++) {
                    StringTokenizer pointTkn = new StringTokenizer(br.readLine());
                    points[i][0] = Long.parseLong(pointTkn.nextToken());
                    points[i][1] = Long.parseLong(pointTkn.nextToken());
                }

                long res = 1;

                for (int p1 = 0; p1 < n - 1; p1++) {
                    for (int p2 = p1 + 1; p2 < n; p2++) {
                        Map<Long, Integer> lineCount = new HashMap<>();
                        for (int k = 0; k < n; k++) {
                            long area = countOrientedArea(points[p1], points[p2], points[k]);
                            lineCount.merge(area, 1, (oldVal, newVal) -> oldVal + newVal);
                        }

                        long onesCount = lineCount.values().stream().filter(cnt -> cnt == 1).count();
                        List<Integer> moreThanOnes = lineCount.values().stream().filter(cnt -> cnt > 1).collect(Collectors.toList());
                        long oddLineCount = moreThanOnes.stream().filter(cnt -> cnt % 2 == 1).count();
                        long oddLinePointAmount = moreThanOnes.stream().filter(cnt -> cnt % 2 == 1).reduce(0, Integer::sum);
                        long evenLinePointAmount = moreThanOnes.stream().filter(cnt -> cnt % 2 == 0).reduce(0, Integer::sum);

                        long localRes = oddLinePointAmount + evenLinePointAmount;

                        if (onesCount > 0) {
                            localRes++;
                            onesCount--;
                        }

                        if (onesCount > 0 && oddLineCount % 2 == 0) {
                            localRes++;
                        }

                        if (localRes > res) {
                            res = localRes;
                        }
                    }
                }

                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }
}
