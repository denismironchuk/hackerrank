package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BigErathosphen {
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int n = Integer.parseInt(br.readLine());
            long[] d = new long[n];
            /*StringTokenizer nTkn = new StringTokenizer(br.readLine());
            long maxD = -1;
            for (int i = 0; i < n; i++) {
                d[i] = Long.parseLong(nTkn.nextToken());
                if (d[i] > maxD) {
                    maxD = d[i];
                }
            }*/

            long maxD = -1;
            for (int i = 0; i < n; i++) {
                d[i] = 1000000000000000l;
                if (d[i] > maxD) {
                    maxD = d[i];
                }
            }

            Date d1 = new Date();
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

            Date d2 = new Date();


            List<Long> primes = new ArrayList<>();
            for (int i = 2; i <= nn; i++) {
                if (isPrime[i] == 1) {
                    primes.add((long)i);
                }
            }

            Date d3 = new Date();

            int[] parity1 = new int[n];
            int par1Cnt = 0;

            int[] parity2 = new int[n];
            int par2Cnt = 0;

            long itrCnt = 0;

            for (int i = 0; i < n; i++) {
                long di = d[i];
                int primePow;
                int distPrimes = 0;
                parity2[i] = 1;

                for (Long prime : primes) {
                    itrCnt++;
                    primePow = 0;
                    boolean isDiv = di % prime == 0;
                    if (isDiv) {
                        distPrimes++;
                    }

                    while (di % prime == 0) {
                        primePow++;
                        di /= prime;
                    }

                    if (isDiv && prime != 2 && primePow % 2 == 1) {
                        parity2[i] = 0;
                    }
                }

                if (di != 1) {
                    distPrimes++;
                    if (di != 2) {
                        parity2[i] = 0;
                    }
                }

                parity1[i] = distPrimes % 2;
                par1Cnt += parity1[i];
                par2Cnt += parity2[i];
            }

            int[] resCands = new int[] {par1Cnt, n - par1Cnt, par2Cnt, n - par2Cnt};
            Arrays.sort(resCands);
            Date d4 = new Date();
            System.out.println(resCands[3]);
            System.out.println(d2.getTime() - d1.getTime() + "ms");
            System.out.println(d3.getTime() - d2.getTime() + "ms");
            System.out.println(d4.getTime() - d3.getTime() + "ms");
            System.out.println("itrCnt = " + itrCnt);
        }
    }
}
