/**
 * Created by Denis_Mironchuk on 9/3/2018.
 */
public class BinomialDistribution2 {
    public static void main(String[] args) {
        double p0 = 0.12;
        double p1 = 1 - p0;

        double res1 = 0;

        for (int i = 0; i < 3; i++) {
            res1 += (double)(c(10, i)) * pow(p1, 10 - i) * pow(p0, i);
        }

        double res2 = 0;

        for (int i = 2; i < 11; i++) {
            res2 += (double)(c(10, i)) * pow(p1, 10 - i) * pow(p0, i);
        }

        System.out.printf("%.3f\n", res1);
        System.out.printf("%.3f\n", res2);
    }

    private static double pow(double x, int n) {
        double res = 1;
        for (int i = 0; i < n; i++) {
           res *= x;
        }
        return res;
    }

    private static int c(int n, int x) {
        return fact(n) / (fact(x) * fact(n - x));
    }

    private static int fact(int n) {
        if (n == 0) {
            return 1;
        }

        return n * fact(n - 1);
    }
}