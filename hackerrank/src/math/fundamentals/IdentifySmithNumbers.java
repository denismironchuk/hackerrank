package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IdentifySmithNumbers {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        long n = Long.parseLong(br.readLine());

        System.out.println(getPrimesDigitsSum(n) == getDigitsSum(n) ? 1 : 0);
    }

    private static int getPrimesDigitsSum(long n) {
        int res = 0;

        for (int i = 2; i * i <= n; i++) {
            while (n % i == 0) {
                res += getDigitsSum(i);
                n /= i;
            }
        }

        if (n != 1) {
            res += getDigitsSum(n);
        }

        return res;
    }

    private static int getDigitsSum(long n) {
        int res = 0;

        while (n != 0) {
            res += n % 10;
            n /= 10;
        }

        return res;
    }
}
