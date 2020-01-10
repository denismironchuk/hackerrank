package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map.Entry;

public class BigErathosphen {
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int n = Integer.parseInt(br.readLine());
            long[] d = new long[n];
            StringTokenizer nTkn = new StringTokenizer(br.readLine());
            long maxD = -1;
            for (int i = 0; i < n; i++) {
                d[i] = Long.parseLong(nTkn.nextToken());
                if (d[i] > maxD) {
                    maxD = d[i];
                }
            }

            //Date start = new Date();
            int nn = (int)Math.sqrt(maxD) + 1;
            int[] isPrime = new int[nn + 1];
            Arrays.fill(isPrime, 1);
            for (int i = 2; i * i <= nn; i++) {
                if (isPrime[i] == 1) {
                    for (int j = i * i; j <= nn; j+=i) {
                        isPrime[j] = 0;
                    }
                }
            }

            List<Long> primes = new ArrayList<>();
            for (int i = 2; i <= nn; i++) {
                if (isPrime[i] == 1) {
                    primes.add((long)i);
                }
            }

            Map<Long, Integer>[] factor = new Map[n];

            for (int i = 0; i < n; i++) {
                factor[i] = new HashMap<>();
                long di = d[i];
                for (Long prime : primes) {
                    while (di % prime == 0) {
                        factor[i].merge(prime, 1, (oldVal, nevVal) -> oldVal + 1);
                        di /= prime;
                    }
                }
                if (di != 1) {
                    factor[i].put(di, 1);
                }
            }

            int[] parity1 = new int[n];

            int par1Cnt = 0;
            for (int i = 0; i < n; i++) {
                Map<Long, Integer> map = factor[i];
                parity1[i] = map.keySet().size() % 2;
                par1Cnt += parity1[i];
            }

            int[] parity2 = new int[n];
            int par2Cnt = 0;

            for (int i = 0; i < n; i++) {
                Map<Long, Integer> map = factor[i];
                parity2[i] = 1;
                for (Entry<Long, Integer> entry : map.entrySet()) {
                    if (entry.getKey() != 2 && entry.getValue() % 2 == 1) {
                        parity2[i] = 0;
                        break;
                    }
                }
                par2Cnt+=parity2[i];
            }

            int[] resCands = new int[] {par1Cnt, n - par1Cnt, par2Cnt, n - par2Cnt};
            Arrays.sort(resCands);

            //Date end = new Date();
            //System.out.println(end.getTime() - start.getTime() + "ms");
            System.out.println(resCands[3]);
        }
    }
}
