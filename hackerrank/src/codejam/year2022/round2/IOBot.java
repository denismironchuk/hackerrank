package codejam.year2022.round2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class IOBot {

    private static class Ball {
        private long pos;
        private int val;

        public Ball(long pos, int val) {
            this.pos = pos;
            this.val = val;
        }
    }
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                long c = Integer.parseInt(tkn1.nextToken());
                List<Ball> balls = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                    long pos = Long.parseLong(tkn2.nextToken());
                    int val = Integer.parseInt(tkn2.nextToken());
                    balls.add(new Ball(pos, val));
                }

                Map<Boolean, List<Ball>> ballsMap = balls.stream()
                        .collect(Collectors.groupingBy(b -> b.pos > 0));

                long res = minCostAllPositive(ballsMap.get(Boolean.TRUE), c)
                        + minCostAllPositive(ballsMap.get(Boolean.FALSE), c);

                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }

    private static long minCostAllPositive(List<Ball> balls, long c) {
        if (balls == null || balls.isEmpty()) {
            return 0;
        }
        Map<Integer, List<Ball>> posMap = balls.stream().collect(Collectors.groupingBy(b -> b.val));
        List<Long> pos0 = posMap.get(0) == null ? new ArrayList<>() :
                posMap.get(0).stream().map(b -> Math.abs(b.pos)).collect(Collectors.toList());
        List<Long> pos1 = posMap.get(1) == null ? new ArrayList<>() :
                posMap.get(1).stream().map(b -> Math.abs(b.pos)).collect(Collectors.toList());

        pos0.sort(Long::compare);
        pos1.sort(Long::compare);

        long[][] dynTable = new long[pos0.size() + 1][pos1.size() + 1];
        if (!pos1.isEmpty()) {
            dynTable[0][1] = pos1.get(0) * 2;
            for (int i = 2; i <= pos1.size(); i++) {
                dynTable[0][i] = pos1.get(i - 1) * 2 + Math.min(dynTable[0][i - 1], c + dynTable[0][i - 2]);
            }
        }

        if (!pos0.isEmpty()) {
            dynTable[1][0] = pos0.get(0) * 2;
            for (int i = 2; i <= pos0.size(); i++) {
                dynTable[i][0] = pos0.get(i - 1) * 2 + Math.min(dynTable[i - 1][0], c + dynTable[i - 2][0]);
            }
        }

        for (int i = 1; i <= pos0.size(); i++) {
            for (int j = 1; j <= pos1.size(); j++) {
                if (pos0.get(i - 1) > pos1.get(j - 1)) {
                    //0 is most distant
                    if (i == 1) {
                        dynTable[i][j] = pos0.get(i - 1) * 2 + dynTable[i - 1][j - 1];
                    } else {
                        dynTable[i][j] = pos0.get(i - 1) * 2 +
                                Math.min(dynTable[i - 1][j - 1], c + dynTable[i - 2][j]);
                    }
                } else {
                    //1 is most distant
                    if (j == 1) {
                        dynTable[i][j] = pos1.get(j - 1) * 2 + dynTable[i - 1][j - 1];
                    } else {
                        dynTable[i][j] = pos1.get(j - 1) * 2 +
                                Math.min(dynTable[i - 1][j - 1], c + dynTable[i][j - 2]);
                    }
                }
            }
        }

        return dynTable[pos0.size()][pos1.size()];
    }
}
