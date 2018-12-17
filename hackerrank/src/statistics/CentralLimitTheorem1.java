package statistics;

import org.apache.commons.math3.special.Erf;

/**
 * Created by Denis_Mironchuk on 12/14/2018.
 */
public class CentralLimitTheorem1 {
    private static double f(double x) {
        return (1 + x) / 2;
    }

    private static double erf(double m, double s, double x) {
        return Erf.erf((x - m) / (s * Math.sqrt(2)));
    }

    public static void main(String[] args) {
        double x = 9800;
        double n = 49;
        double m0 = 205;
        double s0 = 15;

        double m1 = n * m0;
        double s1 = Math.sqrt(n) * s0;

        double res = f(erf(m1, s1, x));

        System.out.printf("%.4f\n", res);
    }
}
