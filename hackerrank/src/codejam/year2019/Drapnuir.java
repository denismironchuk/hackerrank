package codejam.year2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Drapnuir {
    public static final long pow1 = fastPow(2l, 55);
    public static final long pow2 = fastPow(2l, 27);
    public static final long pow3 = fastPow(2l, 18);
    public static final long pow4 = fastPow(2l, 56);
    public static final long pow5 = fastPow(2l, 44);
    public static final long pow6 = fastPow(2l, 37);

    public static final long pow4_2 = fastPow(2l, 13);
    public static final long pow5_2 = fastPow(2l, 11);
    public static final long pow6_2 = fastPow(2l, 9);

    private static long fastPow(long val, int pow) {
        if (pow == 0) {
            return 1;
        }

        if (pow % 2 == 0) {
            return fastPow((val * val), pow / 2);
        } else {
            return (val * fastPow(val, pow - 1));
        }
    }

    public static void main(String[] agrs) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());

        int T = Integer.parseInt(tkn1.nextToken());
        int w = Integer.parseInt(tkn1.nextToken());

        for (int t = 0; t < T; t++) {
            System.out.println(224);
            long x1 = 0;
            long x2 = 0;
            long x3 = 0;
            long x4 = 0;
            long x5 = 0;
            long x6 = 0;

            long n1 = Long.parseLong(br.readLine());
            boolean found = false;
            for (x4 = 0; !found && x4 <= 100; x4++) {
                for (x5 = 0; !found && x5 <= 100; x5++) {
                    for (x6 = 0; !found && x6 <= 100; x6++) {
                        if (pow4 * x4 + pow5 * x5 + pow6 * x6 == n1) {
                            found = true;
                        }
                    }
                }
            }

            x4--;
            x5--;
            x6--;

            System.out.println(55);
            long n2 = Long.parseLong(br.readLine()) - (pow4_2 * x4 + pow5_2 * x5 + pow6_2 * x6);
            found = false;
            for (x1 = 0; !found && x1 <= 100; x1++) {
                for (x2 = 0; !found && x2 <= 100; x2++) {
                    for (x3 = 0; !found && x3 <= 100; x3++) {
                        if (pow1 * x1 + pow2 * x2 + pow3 * x3 == n2) {
                            found = true;
                        }
                    }
                }
            }

            x1--;
            x2--;
            x3--;

            System.out.printf("%s %s %s %s %s %s\n", x1, x2, x3, x4, x5, x6);
            int res = Integer.parseInt(br.readLine());
            if (res == -1) {
                return;
            }
        }
    }
}
