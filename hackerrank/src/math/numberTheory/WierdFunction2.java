package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class WierdFunction2 {
    private static class Elmt {
        private long val;
        private int build1;
        private int build2;

        public Elmt(int build1, int build2) {
            this.build1 = build1 % 2 == 0 ? build1 / 2 : build1;
            this.build2 = build2 % 2 == 0 ? build2 / 2 : build2;
            this.val = (long) this.build1 * (long) this.build2;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        long[][] requests = new long[T][2];

        long maxR = -1;

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            long l = Long.parseLong(tkn.nextToken());
            long r = Long.parseLong(tkn.nextToken());
            requests[t][0] = l;
            requests[t][1] = r;

            if (r > maxR) {
                maxR = r;
            }
        }

        Date start = new Date();

        List<Elmt> elements = new ArrayList<>();

        long maxBuild = 1;

        for (; (maxBuild * (maxBuild + 1)) / 2 <= maxR; maxBuild++) {
            elements.add(new Elmt((int) maxBuild, (int)maxBuild + 1));
        }

        int n = (int)maxBuild + 1;

        int[] primesCnt = new int[n + 1];
        int[][] primes = new int[n + 1][10];

        for (int i = 2; i <= n; i++) {
            if (primesCnt[i] == 0) {
                for (int j = 2; i * j <= n; j++) {
                    primes[j * i][primesCnt[j * i]] = i;
                    primesCnt[j * i]++;
                }
            }
        }

        for (int i = 2; i <= n; i++) {
            if (primesCnt[i] == 0) {
                primes[i][0] = i;
                primesCnt[i]++;
            }
        }

        int elementsCnt = elements.size();
        long[] euler = new long[elementsCnt];
        for (int i = 0; i < elementsCnt; i++) {
            Elmt el = elements.get(i);
            euler[i] = euler(el.val, merge(primes[el.build1], primesCnt[el.build1], primes[el.build2], primesCnt[el.build2]));
        }

        long[] cumSum = new long[elementsCnt];
        cumSum[0] = euler[0];
        for (int i = 1; i < elementsCnt; i++) {
            cumSum[i] = cumSum[i - 1] + euler[i];
        }

        StringBuilder output = new StringBuilder();

        for (long[] req : requests) {
            int posStart = getPositionGreaterOrEqual(elements, req[0], 0, elementsCnt - 1);
            int posEnd = getPositionLessOrEqual(elements, req[1], 0, elementsCnt - 1);

            if (posStart == 0) {
                output.append(cumSum[posEnd]).append("\n");
            } else {
                long res = cumSum[posEnd] - cumSum[posStart - 1];
                output.append(res < 0 ? 0 : res).append("\n");
            }
        }

        System.out.println(output);

        Date end = new Date();
        System.out.println(end.getTime() - start.getTime() + "ms");
        br.readLine();
    }

    private static int getPositionGreaterOrEqual(List<Elmt> vals, long search, int start, int end) {
        if (start == end) {
            return start;
        }

        int mid = (start + end) / 2;
        if (vals.get(mid).val < search) {
            return getPositionGreaterOrEqual(vals, search, mid + 1, end);
        } else {
            return getPositionGreaterOrEqual(vals, search, start, mid);
        }
    }

    private static int getPositionLessOrEqual(List<Elmt> vals, long search, int start, int end) {
        if (start == end) {
            return start;
        }

        int mid = 1+ (start + end) / 2;
        if (vals.get(mid).val > search) {
            return getPositionLessOrEqual(vals, search, start, mid - 1);
        } else {
            return getPositionLessOrEqual(vals, search, mid, end);
        }
    }

    private static long euler(long n, int[] primes) {
        long res = n;

        for (int i = 0; i < primes.length && primes[i] != 0; i++) {
            res -= res / (long)primes[i];
        }

        return res;
    }

    private static int[] merge(int[] a1, int len1, int[] a2, int len2) {
        int[] res = new int[len1 + len2];
        int index = 0;

        int index1 = 0;
        int index2 = 0;

        int lastAdded = -1;

        while (index1 < len1 && index2 < len2) {
            if (a1[index1] < a2[index2]) {
                if (lastAdded != a1[index1]) {
                    res[index] = a1[index1];
                    lastAdded = a1[index1];
                    index++;
                }
                index1++;
            } else {
                if (lastAdded != a2[index2]) {
                    res[index] = a2[index2];
                    lastAdded = a2[index2];
                    index++;
                }
                index2++;
            }
        }

        for (int i = index1; i < len1; i++) {
            if (lastAdded != a1[i]) {
                res[index] = a1[i];
                lastAdded = a1[i];
                index++;
            }
        }

        for (int i = index2; i < len2; i++) {
            if (lastAdded != a2[i]) {
                res[index] = a2[i];
                lastAdded = a2[i];
                index++;
            }
        }

        return res;
    }
}
