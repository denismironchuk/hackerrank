package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;

public class KCandyStore {
    private static final long MOD = 1000000000;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            int n = Integer.parseInt(br.readLine());
            int k = Integer.parseInt(br.readLine());

            BigInteger res = BigInteger.ONE;

            for (int i = n + k - 1; i >= n; i--) {
                res = res.multiply(BigInteger.valueOf(i));
            }

            for (long i = 1; i <= k; i++) {
                res = res.divide(BigInteger.valueOf(i));
            }

            res = res.mod(BigInteger.valueOf(MOD));

            System.out.println(res);
        }
    }
}
