package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class WeirdFunction {
    private static class Elmt {
        private long val;
        private long build1;
        private long build2;

        public Elmt(long build1, long build2) {
            this.build1 = build1;
            this.build2 = build2;
            this.val = (this.build1 * this.build2) / 2 ;
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

        List<Elmt> elements = new ArrayList<>();

        for (long i = 1; (i * (i + 1)) / 2 <= maxR; i++) {
            elements.add(new Elmt(i, i + 1));
        }

        int n = (int)elements.get(elements.size() - 1).build2;
        int[] isPrime = new int[n + 1];
        List<Integer>[] primes = new List[n + 1];
        Arrays.fill(isPrime, 1);

        for (int i = 0; i <= n; i++) {
            primes[i] = new ArrayList<>();
        }

        for (int i = 2; i <= n; i++) {
            if (isPrime[i] == 1) {
                for (int j = 2; i * j <= n; j++) {
                    primes[j * i].add(i);

                    int j_ = j;
                    while (j_ % i == 0) {
                        primes[j * i].add(i);
                        j_ /= i;
                    }
                    isPrime[j * i] = 0;
                }
            }
        }

        long[] eulers = new long[elements.size()];
        eulers[0] = 1;
        for (int i = 1; i < elements.size(); i++) {
            Elmt el = elements.get(i);
            List<Integer> singlePrimes = new ArrayList<>();

            if (el.build1 != 1) {
                if (isPrime[(int) el.build1] == 1) {
                    singlePrimes.add((int) el.build1);
                } else {
                    singlePrimes.addAll(primes[(int) el.build1]);
                }
            }

            if (el.build1 != 1) {
                if (isPrime[(int) el.build2] == 1) {
                    singlePrimes.add((int) el.build2);
                } else {
                    singlePrimes.addAll(primes[(int) el.build2]);
                }
            }

            eulers[i] = euler(el.val, singlePrimes);
        }

        long[] cumSum = new long[elements.size()];
        cumSum[0] = eulers[0];
        for (int i = 1; i < elements.size(); i++) {
            cumSum[i] = cumSum[i - 1] + eulers[i];
        }

        for (long[] request : requests) {
            int start = getGreatOrEqual(elements, request[0], 0, elements.size());
            int end = getLessOrEqual(elements, request[0], 0, elements.size());

            if (start == 0) {
                System.out.println(cumSum[end]);
            } else {
                System.out.println(cumSum[end] - cumSum[start - 1]);
            }
        }
    }

    private static int getGreatOrEqual(List<Elmt> vals, long searchVal, int start, int end) {
        if (start == end) {
            return start;
        }

        int mid = (start + end) / 2;
        if (vals.get(mid).val < searchVal) {
            return getGreatOrEqual(vals, searchVal, mid + 1, end);
        } else {
            return getGreatOrEqual(vals, searchVal, start, mid);
        }
    }

    private static int getLessOrEqual(List<Elmt> vals, long searchVal, int start, int end) {
        if (start == end) {
            return start;
        }

        int mid = (start + end) / 2;
        if (vals.get(mid).val > searchVal) {
            return getGreatOrEqual(vals, searchVal, start, mid - 1);
        } else {
            return getGreatOrEqual(vals, searchVal, mid, end);
        }
    }

    private static long euler(long n, List<Integer> primes) {
        if (primes.size() == 0) {
            return n - 1;
        }

        for (int prime : primes) {
            n -= n / (long) prime;
        }

        return n;
    }
}