package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Twins {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(tkn.nextToken());
        int m = Integer.parseInt(tkn.nextToken());

        int mSqrt = 1 + (int)Math.sqrt(m);
        int[] isNotPrime = new int[mSqrt + 1];

        for (int i = 2; i * i <= mSqrt; i++) {
            if (isNotPrime[i] == 0) {
                for (int j = 2; (j * i) <= mSqrt; j++) {
                    isNotPrime[j * i] = 1;
                }
            }
        }

        int primeCnt = 0;

        for (int i = 2; i <= mSqrt; i++) {
            if (isNotPrime[i] == 0) {
                primeCnt++;
            }
        }

        int[] primes = new int[primeCnt];
        int index = 0;

        for (int i = 2; i <= mSqrt; i++) {
            if (isNotPrime[i] == 0) {
                primes[index] = i;
                index++;
            }
        }

        int[] isPrime = new int[m - n + 1];

        for (int i = n; i <=m; i++) {
            boolean prime = i != 1;
            for (int j = 0; j < primeCnt && prime; j++) {
                prime = i == primes[j] || i % primes[j] != 0;
            }
            if (prime) {
                isPrime[i - n] = 1;
            }
        }

        int res = 0;

        for (int i = 0; i < m - n - 1; i++) {
            if (isPrime[i] == 1 && isPrime[i + 2] == 1) {
                res++;
            }
        }

        System.out.println(res);
    }
}
