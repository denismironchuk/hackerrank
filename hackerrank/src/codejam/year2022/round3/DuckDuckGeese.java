package codejam.year2022.round3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class DuckDuckGeese {

    private static int n;
    private static int c;
    private static int[] colorLowLimit;
    private static int[] colorUpLimit;
    private static int[] hatColors;
    private static Set<Integer> reachedMin;
    private static Map<Integer, Integer> remainToMin;
    private static Map<Integer, Integer> remainToMax;
    private static int[] canBeGoose;
    private static int canBeGooseCnt;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                n = Integer.parseInt(tkn1.nextToken());
                c = Integer.parseInt(tkn1.nextToken());
                colorLowLimit = new int[c];
                colorUpLimit = new int[c];
                for (int i = 0; i < c; i++) {
                    StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                    int low = Integer.parseInt(tkn2.nextToken());
                    int up = Integer.parseInt(tkn2.nextToken());
                    colorLowLimit[i] = low;
                    colorUpLimit[i] = up;
                }
                hatColors = new int[n];
                StringTokenizer tkn3 = new StringTokenizer(br.readLine());
                for (int i = 0; i < n; i++) {
                    hatColors[i] = Integer.parseInt(tkn3.nextToken()) - 1;
                }

                Map<Integer, List<Integer>> colorPositions = new HashMap<>();
                for (int i = 0; i < c; i++) {
                    colorPositions.put(i, new ArrayList<>());
                }

                for (int i = 0; i < n; i++) {
                    colorPositions.get(hatColors[i]).add(i);
                }

                int[] nextColorPosition = new int[n];
                for (Map.Entry<Integer, List<Integer>> entry : colorPositions.entrySet()) {
                    List<Integer> positions = entry.getValue();
                    for (int i = 0; i < positions.size(); i++) {
                        nextColorPosition[positions.get(i)] = positions.get((i + 1) % positions.size());
                    }
                }

                long res = 0;

                for (int pos = 0; pos < n; pos++) {
                    canBeGoose = new int[n];
                    reachedMin = new HashSet<>();
                    remainToMin = new HashMap<>();
                    remainToMax = new HashMap<>();
                    canBeGooseCnt = 0;
                    proceed(pos, true);
                    res += canBeGooseCnt;
                }
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }

    private static int proceed(int startPos, boolean treatFirst) {
        int pos = startPos;
        if (treatFirst) {
            pos = (startPos + 1) % n;

            if (colorUpLimit[hatColors[startPos]] == 0) {
                return pos;
            }

            if (colorLowLimit[hatColors[startPos]] <= 1) {
                reachedMin.add(hatColors[startPos]);
            } else {
                remainToMin.put(hatColors[startPos], colorLowLimit[hatColors[startPos]] - 1);
            }
            remainToMax.put(hatColors[startPos], colorUpLimit[hatColors[startPos]] - 1);
        }

        for (; pos != (startPos - 1 + n) % n; pos = (pos + 1) % n) {
            if (!reachedMin.contains(hatColors[pos])) {
                int curRemainToMin = remainToMin.containsKey(hatColors[pos]) ?
                        remainToMin.get(hatColors[pos]) - 1 : colorLowLimit[hatColors[pos]] - 1;
                if (curRemainToMin <= 0) {
                    remainToMin.remove(hatColors[pos]);
                    reachedMin.add(hatColors[pos]);
                } else {
                    remainToMin.put(hatColors[pos], curRemainToMin);
                }
            }

           int curRemainToMax = remainToMax.containsKey(hatColors[pos]) ?
                    remainToMax.get(hatColors[pos]) - 1 : colorUpLimit[hatColors[pos]] - 1;
            if (curRemainToMax == -1) {
                break;
            } else {
                remainToMax.put(hatColors[pos], curRemainToMax);
            }

            if (remainToMin.isEmpty()) {
                canBeGoose[pos] = 1;
                canBeGooseCnt++;
            } else {
                canBeGoose[pos] = 0;
            }
        }
        return pos;
    }
}
