package statistics;

import org.apache.commons.math3.special.Erf;

public class NormalDistribution2 {
    private static double f(double x) {
        return (1 + x) / 2;
    }

    private static double erf(double m, double s, double x) {
        return Erf.erf((x - m) / (s * Math.sqrt(2)));
    }

    public static void main(String[] args) {
        double m = 70;
        double s = 10;

        double res80 = (1 - f(erf(m, s, 80))) * 100;

        double failed = f(erf(m, s, 60)) * 100;
        double passed = 100 - failed;

        System.out.printf("%.2f\n", res80);
        System.out.printf("%.2f\n", passed);
        System.out.printf("%.2f\n", failed);
    }
}
