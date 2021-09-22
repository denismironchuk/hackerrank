package codejam.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

public class SlideCircuits {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int b = Integer.parseInt(tkn1.nextToken());
                int s = Integer.parseInt(tkn1.nextToken());
                int n = Integer.parseInt(tkn1.nextToken());
                int[] slideStarts = new int[s + 1];
                int[] slideEnds = new int[s + 1];
                for (int i = 1; i <= s; i++) {
                    StringTokenizer slideTkn = new StringTokenizer(br.readLine());
                    int startBuilding = Integer.parseInt(slideTkn.nextToken());
                    int endBuilding = Integer.parseInt(slideTkn.nextToken());
                    slideStarts[i] = startBuilding;
                    slideEnds[i] = endBuilding;
                }

                Random rnd = new Random();
                long[] buildInRands = new long[b + 1];
                long[] buildOutRands = new long[b + 1];
                long fullCircuitHash = 0;
                for (int i = 1; i <= b; i++) {
                    buildInRands[i] = rnd.nextLong();
                    buildOutRands[i] = rnd.nextLong();
                    fullCircuitHash += buildInRands[i] + buildOutRands[i];
                }

                long[] edgeRands = new long[s + 1];
                Map<Long, Integer> hashesToEdges = new HashMap<>();
                for (int i = 1; i <= s; i++) {
                    edgeRands[i] = buildOutRands[slideStarts[i]] + buildInRands[slideEnds[i]];
                    hashesToEdges.put(fullCircuitHash - edgeRands[i], i);
                }

                List<long[]> intervalSums = new ArrayList<>();
                intervalSums.add(null);
                for (int i = 1; i <= s; i++) {
                    long[] sums = new long[s / i + 1];
                    intervalSums.add(sums);
                    int index = 1;
                    for (int j = i; j <= s; j += i) {
                        sums[index] = sums[index - 1] + edgeRands[j];
                        index++;
                    }
                }

                StringBuilder result = new StringBuilder();
                long currentHash = 0;
                for (int i = 0; i < n; i++) {
                    StringTokenizer reqTkn = new StringTokenizer(br.readLine());
                    String action = reqTkn.nextToken();
                    int l = Integer.parseInt(reqTkn.nextToken());
                    int r = Integer.parseInt(reqTkn.nextToken());
                    int m = Integer.parseInt(reqTkn.nextToken());

                    long[] sums = intervalSums.get(m);
                    long sumOfHashes = sums[r / m];
                    if (l >= m) {
                        sumOfHashes -= ((l % m == 0) ? sums[l / m - 1] : sums[l / m]);
                    }
                    if (action.equals("E")) {
                        currentHash += sumOfHashes;
                    } else {
                        currentHash -= sumOfHashes;
                    }

                    if (hashesToEdges.containsKey(currentHash)) {
                        result.append(hashesToEdges.get(currentHash)).append(" ");
                    } else {
                        result.append("X").append(" ");
                    }
                }
                System.out.printf("Case #%s: %s\n", t, result);
            }
        }
    }
}
