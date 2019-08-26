package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class PossiblePath {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            long a = Long.parseLong(tkn.nextToken());
            long b = Long.parseLong(tkn.nextToken());
            long x = Long.parseLong(tkn.nextToken());
            long y = Long.parseLong(tkn.nextToken());

            if (gcd(a,b) == gcd(x,y)) {
                System.out.println("YES");
            } else {
                System.out.println("NO");
            }
        }
    }

    private static long gcd(long a, long b) {
        return a >= b ? gcdInner(a, b) : gcdInner(b, a);
    }

    private static long gcdInner(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return gcdInner(b, a % b);
        }
    }
}
