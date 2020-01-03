package math.numberTheory;

public class GCDTest {
    public static void main(String[] args) {
        int n = 50000;
        long res = 0;

        for (int i = 1; i <= n; i++) {
            res += n / i;
        }

        System.out.println(res);
    }
}
