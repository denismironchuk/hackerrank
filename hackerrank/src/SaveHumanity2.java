import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.StringTokenizer;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class SaveHumanity2 {
    private class Complex {
        private double re = 0;
        private double im = 0;

        public Complex() {
        }

        public Complex(final double re, final double im) {
            this.re = re;
            this.im = im;
        }

        public Complex(final double re) {
            this.re = re;
        }

        public Complex plus(Complex c) {
            return new Complex(re + c.re, im + c.im);
        }

        public Complex minus(Complex c) {
            return new Complex(re - c.re, im - c.im);
        }

        public Complex mul(Complex c) {
            return new Complex(re * c.re - im * c.im, re * c.im + im * c.re);
        }

        public Complex div(int n) {
            return new Complex(re / n, im / n);
        }

        public Complex clone() {
            return new Complex(re, im);
        }
    }

    private long calls = 0;

    public Complex[] fft(Complex[] input, boolean inverse) {
        int n = input.length;

        if (n == 1) {
            return input;
        }

        Complex[] a0 = new Complex[n / 2];
        Complex[] a1 = new Complex[n / 2];

        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                a0[i / 2] = input[i];
            } else {
                a1[i / 2] = input[i];
            }
        }

        Complex[] fft0 = fft(a0, inverse);
        Complex[] fft1 = fft(a1, inverse);

        Complex[] result = new Complex[n];

        int sign = inverse ? -1: 1;
        Complex principal = new Complex(cos(sign * 2 * PI / n), sin(sign * 2 * PI / n));
        Complex w = new Complex(1);

        for (int i = 0; i < n / 2; i++) {
            result[i] = fft0[i].plus(w.mul(fft1[i]));
            result[i + (n / 2)] = fft0[i].minus(w.mul(fft1[i]));

            if (inverse) {
                result[i] = result[i].div(2);
                result[i + (n / 2)] = result[i + (n / 2)].div(2);
            }
            w = w.mul(principal);
        }

        return result;
    }

    private int[] run(String pStr, String vStr) {
        int[] mismatches = new int[pStr.length()];

        for (char c = 'a'; c <= 'z'; c++) {
            int[] vMatch = new int[vStr.length()];

            for (int i = 0; i < vStr.length(); i++) {
                if (c == vStr.charAt(i)) {
                    vMatch[i] = 1;
                }
            }

            int[] pMatch = new int[pStr.length()];

            for (int i = 0; i < pStr.length(); i++) {
                if (c != pStr.charAt(i)) {
                    pMatch[i] = 1;
                }
            }

            int maxLen = Math.max(pStr.length(), vStr.length());

            int len = 1;

            while (len < maxLen) {
                len *= 2;
            }

            len *= 2;

            Complex[] pMatchCompl = new Complex[len];
            for (int i = 0; i < len; i++) {
                pMatchCompl[i] = new Complex();
            }
            for (int i = 0; i < pMatch.length; i++) {
                pMatchCompl[i] = new Complex(pMatch[pMatch.length - 1 - i]);
            }

            Complex[] vMatchCompl = new Complex[len];
            for (int i = 0; i < len; i++) {
                vMatchCompl[i] = new Complex();
            }
            for (int i = 0; i < vMatch.length; i++) {
                vMatchCompl[i] = new Complex(vMatch[i]);
            }

            Date d1 = new Date();
            Complex[] pMatchFft = fft(pMatchCompl, false);
            Date d2 = new Date();
            Complex[] vMatchFft = fft(vMatchCompl, false);
            Date d3 = new Date();

            System.out.println(d3.getTime() - d2.getTime() + "ms");
            System.out.println(d2.getTime() - d1.getTime() + "ms");

            Complex[] product = new Complex[len];

            for (int i = 0; i < len; i++) {
                product[i] = pMatchFft[i].mul(vMatchFft[i]);
            }

            Complex[] result = fft(product, true);

            for (int i = vStr.length() - 1; i < pStr.length(); i++) {
                mismatches[pStr.length() - i - 1] += Math.round(result[i].re);
            }
        }

        return mismatches;
    }

    public static void main(String[] args) throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("D:/savehumanity.txt")));
        int T = Integer.parseInt(br.readLine());
        SaveHumanity2 app = new SaveHumanity2();
        for (int t = 0; t < T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine(), " ");
            String p = tkn.nextToken();
            String v = tkn.nextToken();

            int[] mismatches = app.run(p, v);
            boolean wasMatches = false;

            for (int i = 0; i <= mismatches.length - v.length(); i++) {
                if (mismatches[i] <= 1) {
                    System.out.print(i + " ");
                    wasMatches = true;
                }
            }

            if (wasMatches) {
                System.out.println();
            } else {
                System.out.println("No Match!");
            }
        }
    }
}
