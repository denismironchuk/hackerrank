import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class PrimeXOR {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());

        for (int q = 0; q < Q; q++) {
            /*int n = Integer.parseInt(br.readLine());
            String aStr = br.readLine();
            StringTokenizer tkn = new StringTokenizer(aStr, " ");
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = Integer.parseInt(tkn.nextToken());
            }*/
            int n = 100000;
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = (int)(3500 + (Math.random() * 1000));
            }

            Date start = new Date();
            int[] uniqueA = new int[n];
            int[] cnts = new int[n];

            Arrays.sort(a);

            uniqueA[0] = a[0];
            cnts[0] = 1;

            int uniqueIndex = 0;

            for (int i = 1; i < n; i++) {
                if (a[i] == a[i - 1]) {
                    cnts[uniqueIndex]++;
                } else {
                    uniqueIndex++;
                    uniqueA[uniqueIndex] = a[i];
                    cnts[uniqueIndex] = 1;
                }
            }

            Map<Integer, Long> prev = new HashMap<>();
            prev.put(0, 1l);

            for (int i = 0; i < uniqueIndex + 1; i++) {
                Map<Integer, Long> beforePrev = new HashMap<>();
                beforePrev.putAll(prev);
                prev.clear();

                for (Map.Entry<Integer, Long> beforePrevEntry : beforePrev.entrySet()) {
                    Integer newVal = beforePrevEntry.getKey() ^ uniqueA[i];
                    prev.put(newVal, beforePrevEntry.getValue());
                }

                if (cnts[i] > 1) {
                    int mult1 = 1 + (cnts[i] / 2);

                    for (Map.Entry<Integer, Long> setBeforePrevEntry : beforePrev.entrySet()) {
                        Long cnt = setBeforePrevEntry.getValue();
                        setBeforePrevEntry.setValue(cnt * mult1);
                    }

                    int mult2 = 1 + ((cnts[i] - 1) / 2);

                    for (Map.Entry<Integer, Long> setPrevEntry : prev.entrySet()) {
                        Long cnt = setPrevEntry.getValue();
                        setPrevEntry.setValue(cnt * mult2);
                    }
                }

                for (Map.Entry<Integer, Long> beforePrevEntry : beforePrev.entrySet()) {
                    Integer key = beforePrevEntry.getKey();
                    Long val = beforePrevEntry.getValue();
                    prev.merge(key, val, (oldCnt, newCnt) -> oldCnt + newCnt);
                }
            }

            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + "ms");
        }
    }
}
