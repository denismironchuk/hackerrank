package statistics;

import org.apache.commons.math3.special.Erf;

public class NormalDistribution1 {
    private static double f(double x) {
        return (1 + x) / 2;
    }

    private static double erf(double m, double s, double x) {
        return Erf.erf((x - m) / (s * Math.sqrt(2)));
    }

    public static void main(String[] args) {
        double m = 20;
        double s = 2;

        double res1 = f(erf(m, s, 19.5));
        System.out.printf("%.3f\n", res1);

        double res2 = f(erf(m, s, 22)) - f(erf(m, s, 20));
        System.out.printf("%.3f\n", res2);
    }
}
