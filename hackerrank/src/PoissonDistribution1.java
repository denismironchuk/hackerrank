/**
 * Created by Denis_Mironchuk on 9/4/2018.
 */
public class PoissonDistribution1 {
    public static void main(String[] args) {
        double res = Math.pow(2.5, 5)* Math.pow(Math.E, -2.5) / (5.0 * 4.0 * 3.0 * 2.0);
        System.out.printf("%.3f\n", res);
    }
}
