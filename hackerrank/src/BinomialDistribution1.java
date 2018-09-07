/**
 * Created by Denis_Mironchuk on 9/3/2018.
 */
public class BinomialDistribution1 {
    public static void main(String[] args) {
        double p2 = 1 / 2.09;
        double p1 = 1.09 / 2.09;
        double res = p1*p1*p1*p2*p2*p2*20 + p1*p1*p1*p1*p2*p2*15 + p1*p1*p1*p1*p1*p2*6 + p1*p1*p1*p1*p1*p1*1;
        System.out.printf("%.3f", res);
    }
}