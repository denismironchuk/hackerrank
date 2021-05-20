package codejam.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class ClosestPick {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                int k = Integer.parseInt(tkn1.nextToken());
                long[] p = new long[n];
                StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                for (int i = 0; i < n; i++) {
                    p[i] = Long.parseLong(tkn2.nextToken());
                }

                Arrays.sort(p);
                List<Long> probs = new ArrayList<>();
                probs.add(0l);
                probs.add(0l);

                for (int i = 0; i < n - 1; i++) {
                    probs.add((p[i + 1] - p[i]) / 2);
                }

                if (p[0] != 1) {
                    probs.add(p[0] - 1);
                }

                if (p[n - 1] != k) {
                    probs.add(k - p[n - 1]);
                }

                probs.sort((v1, v2) -> Long.compare(v2, v1));

                double prob1 = (double) (probs.get(0) + probs.get(1)) / (double) k;

                List<Long> probs2 = new ArrayList<>();
                probs2.add(0l);

                for (int i = 0; i < n - 1; i++) {
                    probs2.add(p[i + 1] - p[i] - 1);
                }

                probs2.sort((v1, v2) -> Long.compare(v2, v1));

                double prob2 = (double) (probs2.get(0)) / (double) k;

                System.out.printf("Case #%s: %.6f\n", t, Math.max(prob1, prob2));

            }
        }
    }
}
