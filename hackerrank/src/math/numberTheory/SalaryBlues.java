package math.numberTheory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SalaryBlues {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader br = new BufferedReader(new FileReader("/Users/denis_mironchuk/input14.txt"));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        int Q = Integer.parseInt(tkn1.nextToken());

        long[] s = new long[n];
        StringTokenizer sTkn = new StringTokenizer(br.readLine());

        boolean allEquals = true;

        for (int i = 0; i < n; i++) {
            s[i] = Long.parseLong(sTkn.nextToken());
            allEquals = allEquals && s[i] == s[0];
        }

        long gcd = s[0];

        for (int i = 1; i < n; i++) {
            gcd = gcd(gcd, s[i]);
        }

        StringBuilder res = new StringBuilder();

        for (int q = 0; q < Q; q++) {
            long k = Long.parseLong(br.readLine());
            if (allEquals) {
                res.append(gcd + k).append("\n");
            } else {
                res.append(gcd(gcd, k)).append("\n");
            }
        }

        System.out.println(res);
    }

    private static long gcd(long a, long b) {
        return a > b ? gcdInner(a, b) : gcdInner(b, a);
    }

    private static long gcdInner(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return gcd(b, a % b);
        }
    }
}
