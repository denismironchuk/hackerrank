package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class RussianPeasantExponentiation {
    private static class Complex {
        private long a;
        private long b;

        public Complex(long a, long b) {
            this.a = a;
            this.b = b;
        }

        public Complex mul(Complex c, long mod) {
            return new Complex((((a * c.a) % mod) - ((b * c.b) % mod) + mod) % mod, (((a * c.b) % mod) + ((b * c.a) % mod)) % mod);
        }

        @Override
        public String toString() {
            return String.format("%d %d", a, b);
        }
    }

    private static Complex pow(Complex c, long p, long m) {
        if (p == 0) {
            return new Complex(1, 0);
        }

        if (p % 2 == 0) {
            return pow(c.mul(c, m), p / 2, m);
        } else {
            return c.mul(pow(c, p - 1, m), m);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());
        for (int q = 0; q < Q; q++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            long a = Long.parseLong(tkn.nextToken());
            long b = Long.parseLong(tkn.nextToken());
            long k = Long.parseLong(tkn.nextToken());
            long m = Long.parseLong(tkn.nextToken());

            Complex res = pow(new Complex(a, b), k, m);
            System.out.println(res);
        }
    }
}
