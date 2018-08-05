package cardsPermutations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 7/2/2018.
 */
public class BinomialCalculator {
    private static long factorial(long n) {
        if (n == 0) {
            return 1;
        }

        return n * factorial(n - 1);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            StringTokenizer inpt = new StringTokenizer(br.readLine());
            long m = Long.parseLong(inpt.nextToken());
            long n = Long.parseLong(inpt.nextToken());

            long bin = factorial(n) / (factorial(m) * factorial(n - m));
            System.out.println("C(m,n) = " + bin);
        }
    }
}
