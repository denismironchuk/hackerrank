package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class BreakingSticks {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        long[] a = new long[n];
        StringTokenizer tkn = new StringTokenizer(br.readLine());
        long res = 0;
        for (int i = 0; i < n; i++) {
            a[i] = Long.parseLong(tkn.nextToken());
            List<Long> primes = factor(a[i]);
            res += 1;
            long mul = 1;
            for (Long prime : primes) {
                mul *= prime;
                res += mul;
            }
        }

        System.out.println(res);
    }

    private static List<Long> factor(long n) {
        List<Long> res = new ArrayList<>();

        for (long i = 2; i * i <= n; i++) {
            while (n % i == 0) {
                res.add(i);
                n /= i;
            }
        }

        if (n != 1) {
            res.add(n);
        }

        Comparator<Long> comp = Comparator.comparingLong(l -> l);
        res.sort(comp.reversed());

        return res;
    }
}
