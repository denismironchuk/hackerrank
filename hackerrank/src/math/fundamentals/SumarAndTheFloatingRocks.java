package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SumarAndTheFloatingRocks {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int x1 = Integer.parseInt(tkn.nextToken());
            int y1 = Integer.parseInt(tkn.nextToken());
            int x2 = Integer.parseInt(tkn.nextToken());
            int y2 = Integer.parseInt(tkn.nextToken());

            int len1 = Math.abs(x1 - x2);
            int len2 = Math.abs(y1 - y2);

            if (len1 == 0 && len2 == 0) {
                System.out.println(0);
            } else if (len1 == 0) {
                System.out.println(len2 - 1);
            } else if (len2 == 0) {
                System.out.println(len1 - 1);
            } else {
                System.out.println(gcd(len1, len2) - 1);
            }
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
