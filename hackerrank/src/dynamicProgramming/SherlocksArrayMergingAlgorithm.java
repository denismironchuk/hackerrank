package dynamicProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class SherlocksArrayMergingAlgorithm {
    private static long MOD = 1000000007;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int n = Integer.parseInt(br.readLine());
            int[] m = new int[n];
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                m[i] = Integer.parseInt(tkn.nextToken());
            }

            Date start = new Date();

            Map<Integer, Map<Integer, Long>> currMap = new HashMap<>();
            Map<Integer, Long> tmp = new HashMap<>();
            tmp.put(0, 1l);
            currMap.put(1, tmp);

            Map<Integer, Map<Integer, Long>> nextMap = new HashMap<>();

            int prevVal = m[0];

            for (int i = 1; i < n; i++) {
                int val = m[i];
                nextMap.put(1, new HashMap<>());

                for (Map.Entry<Integer, Map<Integer, Long>> entry1 : currMap.entrySet()) {
                    Integer last = entry1.getKey();
                    Map<Integer, Long> dyn = entry1.getValue();
                    nextMap.get(1).put(last, 0l);

                    for (Map.Entry<Integer, Long> entry2: dyn.entrySet()) {
                        int beforeLast = entry2.getKey();
                        long cnt = entry2.getValue();

                        if (val > prevVal) {
                            if (beforeLast == 0) {
                                Map<Integer, Long> map = new HashMap<>();
                                map.put(0, 1l);
                                nextMap.put(last + 1, map);
                            } else if (beforeLast > last) {
                                if (!nextMap.containsKey(last + 1)) {
                                    nextMap.put(last + 1, new HashMap<>());
                                }

                                nextMap.get(last + 1).put(beforeLast, ((cnt * (beforeLast - last)) % MOD));
                            }
                        }

                        Map<Integer, Long> tmpMap = nextMap.get(1);
                        Long tmpCnt = tmpMap.get(last);
                        tmpCnt += (cnt * last) % MOD;
                        tmpCnt %= MOD;
                        tmpMap.put(last, tmpCnt);
                    }
                }

                currMap = nextMap;
                nextMap = new HashMap<>();
                prevVal = val;
            }

            Long res = 0l;

            for (Map.Entry<Integer, Map<Integer, Long>> entry1 : currMap.entrySet()) {
                Map<Integer, Long> dyn = entry1.getValue();

                for (Map.Entry<Integer, Long> entry2 : dyn.entrySet()) {
                    Long cnt = entry2.getValue();
                    res += cnt;
                    res %= MOD;
                }
            }

            Date end = new Date();

            System.out.println(res);
            System.out.println(end.getTime() - start.getTime() + "ms");
        }
    }
}
