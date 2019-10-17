package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class TheChosenOne {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        long[] a = new long[n];

        StringTokenizer tkn = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            a[i] = Long.parseLong(tkn.nextToken());
        }

        if (n == 1) {
            System.out.println(a[0] + 1);
            return;
        }

        long[] toLeftGcd = new long[n];
        toLeftGcd[0] = a[0];
        for (int i = 1; i < n; i++) {
            toLeftGcd[i] = gcd(toLeftGcd[i - 1], a[i]);
        }

        long[] toRightGcd = new long[n];
        toRightGcd[n - 1] = a[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            toRightGcd[i] = gcd(toRightGcd[i + 1], a[i]);
        }

        //Check first element
        if (toRightGcd[1] > a[0] || a[0] % toRightGcd[1] != 0) {
            System.out.println(toRightGcd[1]);
            return;
        }

        for (int i = 1; i < n - 2; i++) {
            long gcd = gcd(toLeftGcd[i - 1], toRightGcd[i + 1]);
            if (gcd > a[i] || a[i] % gcd != 0) {
                System.out.println(gcd);
                return;
            }
        }

        if (toLeftGcd[n - 2] > a[n - 1] || a[n - 1] % toLeftGcd[n - 2] != 0) {
            System.out.println(toLeftGcd[n - 2]);
            return;
        }
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
