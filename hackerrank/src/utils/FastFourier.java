package utils;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class FastFourier {
    public Complex[] fft(Complex[] input) {
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

        Complex[] fft0 = fft(a0);
        Complex[] fft1 = fft(a1);

        Complex[] result = new Complex[n];

        Complex principal = new Complex(cos(2 * PI / n), sin(2 * PI / n));
        Complex w = new Complex(1);
        for (int i = 0; i < n / 2; i++) {
            result[i] = fft0[i].plus(w.mul(fft1[i]));
            result[i + (n / 2)] = fft0[i].minus(w.mul(fft1[i]));
            w = w.mul(principal);
        }

        return result;
    }

    public Complex[] ifft(Complex[] input) {
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

        Complex[] fft0 = ifft(a0);
        Complex[] fft1 = ifft(a1);

        Complex[] result = new Complex[n];

        Complex principal = new Complex(cos(-2 * PI / n), sin(-2 * PI / n));
        Complex w = new Complex(1);

        for (int i = 0; i < n / 2; i++) {
            result[i] = fft0[i].plus(w.mul(fft1[i])).div(2);
            result[i + (n / 2)] = fft0[i].minus(w.mul(fft1[i])).div(2);
            w = w.mul(principal);
        }

        return result;
    }

    public static void main(String[] args) {
        int len1 = 262144;
        Complex[] input1 = new Complex[len1];

        for (int i = 0; i < len1; i++) {
            if (i < len1 / 2) {
                input1[i] = new Complex(1);
            } else {
                input1[i] = new Complex(0);
            }
        }

        Complex[] fft1 = new FastFourier().fft(input1);

        int len2 = 131072;
        Complex[] input2 = new Complex[len1];

        for (int i = 0; i < len1; i++) {
            if (i < len2 / 2) {
                input2[i] = new Complex(1);
            } else {
                input2[i] = new Complex(0);
            }
        }

        Complex[] fft2 = new FastFourier().fft(input2);

        Complex[] prod = new Complex[input1.length];
        for (int i = 0; i < input1.length; i++) {
            prod[i] = fft1[i].mul(fft2[i]);
        }

        Complex[] res = new FastFourier().ifft(prod);

        for (int i = 0; i < len1; i++) {
            if (i % 1000 == 0) {
                System.out.println(Math.round(res[i].getRe()));
            }
        }

        System.out.println();
    }
}
