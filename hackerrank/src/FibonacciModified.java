import java.util.*;
import java.math.*;

public class FibonacciModified {
    static BigInteger fibonacciModified(BigInteger t1, BigInteger t2, int n) {
        for (int i = 3; i <= n; i++) {
            BigInteger result = t1.add(t2.multiply(t2));
            t1 = t2;
            t2 = result;
        }
        return t2;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        BigInteger t1 = BigInteger.valueOf(in.nextInt());
        BigInteger t2 = BigInteger.valueOf(in.nextInt());
        int n = in.nextInt();
        BigInteger result = fibonacciModified(t1, t2, n);
        System.out.println(result);
        in.close();
    }
}
