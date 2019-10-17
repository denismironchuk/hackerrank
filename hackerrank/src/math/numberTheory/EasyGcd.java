package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class EasyGcd {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        long k = Long.parseLong(tkn1.nextToken());

        StringTokenizer tkn2 = new StringTokenizer(br.readLine());
        long[] a = new long[n];

        for (int i = 0; i < n; i++) {
            a[i] = Long.parseLong(tkn2.nextToken());
        }

        long gcd = a[0];

        for (int i = 1; i < n; i++) {
            gcd = gcd(gcd, a[i]);
        }

        long res = 0;

        List<Long> dividors = getDividors(gcd);

        for (Long dividor : dividors) {
            long candidate = dividor * (k / dividor);
            if (candidate > res) {
                res = candidate;
            }
        }

        System.out.println(res);
    }

    private static List<Long> getDividors(long n) {
        List<Long> dividors = new ArrayList<>();

        for (long i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                dividors.add(i);
                if (i * i != n) {
                    dividors.add(n / i);
                }
            }
        }

        dividors.add(n);

        return dividors;
    }

    private static long gcd(long a, long b) {
        return a > b ? gcdInner(a, b) : gcdInner(b, a);
    }

    private static long gcdInner(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return gcdInner(b, a % b);
        }
    }
}
