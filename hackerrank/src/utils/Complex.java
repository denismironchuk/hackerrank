package utils;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.PI;

public class Complex {
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

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
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

    public static Complex w(int n, int k) {
        return new Complex(cos(2 * PI * k / n), sin(2 * PI * k / n));
    }

    public static Complex invW(int n, int k) {
        return new Complex(cos(-2 * PI * k / n), sin(-2 * PI * k / n));
    }

    public Complex div(int n) {
        return new Complex(re / n, im / n);
    }

    public Complex clone() {
        return new Complex(re, im);
    }

    @Override
    public String toString() {
        return re + "+i*" + im;
    }
}
