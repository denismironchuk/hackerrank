package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Restaurant {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        for (int t = 0; t < T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int l = Integer.parseInt(tkn.nextToken());
            int b = Integer.parseInt(tkn.nextToken());

            int gcd = gcd(l, b);

            System.out.println((l * b) / (gcd * gcd));
        }
    }

    private static int gcd(int a, int b) {
        return a >= b ? gcdInner(a, b) : gcdInner(b, a);
    }

    private static int gcdInner(int a, int b) {
        int ost = a % b;
        if (ost == 0) {
            return b;
        } else {
            return gcdInner(b, ost);
        }
    }
}
