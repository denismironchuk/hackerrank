package codejam.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
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
                Map<Integer, Map<Integer, Integer>> edgesMap = new HashMap<>();
                for (int i = 1; i <= s; i++) {
                    StringTokenizer slideTkn = new StringTokenizer(br.readLine());
                    int startBuilding = Integer.parseInt(slideTkn.nextToken());
                    int endBuilding = Integer.parseInt(slideTkn.nextToken());
                    slideStarts[i] = startBuilding;
                    slideEnds[i] = endBuilding;

                    if (!edgesMap.containsKey(startBuilding)) {
                        edgesMap.put(startBuilding, new HashMap<>());
                    }

                    edgesMap.get(startBuilding).put(endBuilding, i);
                }

                int[] outCnt = new int[b + 1];
                int[] inCnt = new int[b + 1];
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < n; i++) {
                    StringTokenizer reqTkn = new StringTokenizer(br.readLine());
                    String action = reqTkn.nextToken();
                    int l = Integer.parseInt(reqTkn.nextToken());
                    int r = Integer.parseInt(reqTkn.nextToken());
                    int m = Integer.parseInt(reqTkn.nextToken());
                    int start = l % m == 0 ? l : ((l / m) + 1) * m;
                    if (action.equals("E")) {
                        for (int j = start; j <= r; j += m) {
                            outCnt[slideStarts[j]]++;
                            inCnt[slideEnds[j]]++;
                        }
                    } else {
                        for (int j = start; j <= r; j += m) {
                            outCnt[slideStarts[j]]--;
                            inCnt[slideEnds[j]]--;
                        }
                    }

                    int edgeToAdd = getEdgeToAdd(outCnt, inCnt, edgesMap);
                    if (edgeToAdd == 0) {
                        result.append("X").append(" ");
                    } else {
                        result.append(edgeToAdd).append(" ");
                    }
                }
                System.out.printf("Case #%s: %s\n", t, result);
            }
        }
    }

    private static int getEdgeToAdd(int[] outCnt, int[] inCnt, Map<Integer, Map<Integer, Integer>> edgesMap) {
        int outZeroCnt = 0;
        int outZeroBuilding = -1;
        int inZeroCnt = 0;
        int inZeroBuilding = -1;

        boolean correct = true;

        for (int i = 1; i < outCnt.length; i++) {
            if (outCnt[i] > 1 || inCnt[i] > 1) {
                correct = false;
                break;
            }

            if (outCnt[i] == 0) {
                outZeroCnt++;
                outZeroBuilding = i;
            }

            if (inCnt[i] == 0) {
                inZeroCnt++;
                inZeroBuilding = i;
            }
        }

        if (!correct) {
            return 0;
        }

        if (outZeroCnt != 1 || inZeroCnt != 1) {
            return 0;
        }

        if (!edgesMap.get(outZeroBuilding).containsKey(inZeroBuilding)) {
            return 0;
        }

        return edgesMap.get(outZeroBuilding).get(inZeroBuilding);
    }
}
