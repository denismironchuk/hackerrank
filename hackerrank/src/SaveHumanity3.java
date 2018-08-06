import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.StringTokenizer;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class SaveHumanity3 {
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

    public void fft(double[] inpRe, double[] inpIm, boolean inverse, double[] outpRe, double[] outpIm) {
        int n = inpRe.length;

        if (n == 1) {
            outpRe = inpRe;
            outpIm = inpIm;
            return;
        }

        double[] inpRe0 = new double[n / 2];
        double[] inpIm0 = new double[n / 2];
        double[] inpRe1 = new double[n / 2];
        double[] inpIm1 = new double[n / 2];

        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                inpRe0[i / 2] = inpRe[i];
                inpIm0[i / 2] = inpIm[i];
            } else {
                inpRe1[i / 2] = inpRe[i];
                inpIm1[i / 2] = inpIm[i];
            }
        }

        double[] fftRe0 = new double[n / 2];
        double[] fftIm0 = new double[n / 2];
        double[] fftRe1 = new double[n / 2];
        double[] fftIm1 = new double[n / 2];

        fft(inpRe0, inpIm0, inverse, fftRe0, fftIm0);
        fft(inpRe1, inpIm1, inverse, fftRe1, fftIm1);

        outpRe = new double[n];
        outpIm = new double[n];

        int sign = inverse ? -1: 1;
        double principalRe = cos(sign * 2 * PI / n);
        double principalIm = sin(sign * 2 * PI / n);

        double wRe = 1;
        double wIm = 0;

        for (int i = 0; i < n / 2; i++) {
            outpRe[i] = wRe * fftRe1[i] - wIm * fftIm1[i] + fftRe0[i];
            outpIm[i] = wIm * fftRe1[i] + wRe * fftRe1[i] + fftIm0[i];

            outpRe[i + (n / 2)] = wRe * fftRe1[i] - wIm * fftIm1[i] - fftRe0[i];
            outpIm[i + (n / 2)] = wIm * fftRe1[i] + wRe * fftRe1[i] - fftIm0[i];

            if (inverse) {
                outpRe[i] /= 2;
                outpIm[i] /= 2;

                outpRe[i + (n / 2)] /= 2;
                outpIm[i + (n / 2)] /= 2;
            }

            double wRe_ = wRe * principalRe - wIm * principalIm;
            double wIm_ = wIm * principalRe + wRe * principalIm;

            wRe = wRe_;
            wIm = wIm_;
        }
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

            double[] pMatchRe = new double[len];
            double[] pMatchIm = new double[len];

            for (int i = 0; i < pMatch.length; i++) {
                pMatchRe[i] = pMatch[pMatch.length - 1 - i];
            }

            double[] vMatchRe = new double[len];
            double[] vMatchIm = new double[len];

            for (int i = 0; i < vMatch.length; i++) {
                vMatchRe[i] = vMatch[i];
            }

            Date d1 = new Date();
            double[] pMatchFftRe = new double[len];
            double[] pMatchFftIm = new double[len];
            fft(pMatchRe, pMatchIm, false, pMatchFftRe, pMatchFftIm);

            Date d2 = new Date();
            double[] vMatchFftRe = new double[len];
            double[] vMatchFftIm = new double[len];
            fft(vMatchRe, vMatchIm, false, vMatchFftRe, vMatchFftIm);
            Date d3 = new Date();

            //System.out.println(d3.getTime() - d2.getTime() + "ms");
            //System.out.println(d2.getTime() - d1.getTime() + "ms");

            double[] productRe = new double[len];
            double[] productIm = new double[len];

            for (int i = 0; i < len; i++) {
                productRe[i] = pMatchFftRe[i] * vMatchFftRe[i] - pMatchFftIm[i] * vMatchFftIm[i];
                productIm[i] = pMatchFftRe[i] * vMatchFftIm[i] + pMatchFftIm[i] * vMatchFftRe[i];
            }

            double[] resultRe = new double[len];
            double[] resultIm = new double[len];
            fft(productRe, productIm, true, resultRe, resultIm);

            for (int i = vStr.length() - 1; i < pStr.length(); i++) {
                mismatches[pStr.length() - i - 1] += Math.round(resultRe[i]);
            }
        }

        return mismatches;
    }

    public static void main(String[] args) throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("D:/savehumanity.txt")));
        int T = Integer.parseInt(br.readLine());
        SaveHumanity3 app = new SaveHumanity3();
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
