package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BestDivisor {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int bestDivisor = -1;
        int bestDivDigSum = -1;

        for (int div = n; div > 0; div--) {
            if (n % div == 0) {
                int sum = 0;
                int divTmp = div;

                while (divTmp != 0) {
                    sum += divTmp % 10;
                    divTmp /= 10;
                }

                if (sum >= bestDivDigSum) {
                    bestDivDigSum = sum;
                    bestDivisor = div;
                }
            }
        }

        System.out.println(bestDivisor);
    }
}
