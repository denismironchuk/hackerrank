package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class MegaPrimeNumbers {

    private static final int[] PRIME_DIGITS = new int[] {0,0,1,1,0,1,0,1,0,0};
    private static final int[] NEXT_PRIME = new int[] {2,2,3,5,5,7,7,2,2,2};

    public static final int MAX_DIGITS = 20;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());

            long first = Long.parseLong(tkn.nextToken());
            long last = Long.parseLong(tkn.nextToken());

            buildMegaprimes(first, last);

            int intervalLen = (int) (Math.sqrt(last) + 1);
            int[] isNotPrime = new int[intervalLen + 1];

            for (int i = 2; i * i <= intervalLen; i++) {
                if (isNotPrime[i] == 0) {
                    for (int j = i * i; j <= intervalLen; j += i) {
                        isNotPrime[j] = 1;
                    }
                }
            }

            List<Integer> primes = new ArrayList<>();
            for (int i = 2; i < intervalLen; i++) {
                if (isNotPrime[i] == 0) {
                    primes.add(i);
                }
            }
        }
    }

    private static List<int[]> buildMegaprimes(long first, long last) {
        List<int[]> megaprimes = new ArrayList<>();

        int[] lastDigits = new int[MAX_DIGITS];
        int lastIndex = convertToDigitsArray(last, lastDigits);

        int[] prev = new int[MAX_DIGITS];
        convertToDigitsArray(first, prev);
        while(true) {
            int[] next = Arrays.copyOf(prev, MAX_DIGITS);
            int lastUpdatedPos = buildNextMegaprime(next);
            if ((lastUpdatedPos == lastIndex && next[lastUpdatedPos] > lastDigits[lastIndex]) || lastUpdatedPos > lastIndex) {
                break;
            }
            printMegaprime(next);
            System.out.println(lastUpdatedPos);

            prev = next;
        }

        return megaprimes;
    }

    private static int convertToDigitsArray(long n, int[] digits) {
        int ind = 0;

        while (n != 0) {
            int digit = (int)(n % 10);
            digits[ind] = digit;
            ind++;
            n /= 10;
        }

        return --ind;
    }

    private static void printMegaprime(int[] megaprime) {
        for (int i = megaprime.length - 1; i >= 0; i--) {
            if (megaprime[i] != 0) {
                System.out.print(megaprime[i]);
            }
        }
        System.out.println();
    }

    private static int buildNextMegaprime(int[] next) {
        return updatePosition(next, 0);
    }

    private static int updatePosition(int[] megaprime, int pos) {
        if (megaprime[pos] == 0) {
            megaprime[pos] = 2;
            return pos;
        }

        megaprime[pos] = NEXT_PRIME[megaprime[pos]];

        if (megaprime[pos] == 2) {
            return updatePosition(megaprime, pos + 1);
        }

        return pos;
    }
}
