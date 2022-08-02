package kickstart.year2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class InterestingIntegers {

    private static final int MAX_SIZE = 12;
    private static final int MAX_DIGIT = 9;

    public static void main(String[] args) throws IOException {
        List<List<Integer>> interestingIntegers = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            splitToSum(i, MAX_DIGIT, MAX_SIZE).forEach(s -> {
                long sum = s.stream().map(val -> (long)val).reduce((a, b) -> a + b).get();
                long prod = s.stream().map(val -> (long)val).reduce((a, b) -> a * b).get();
                if (prod % sum == 0) {
                    interestingIntegers.add(s);
                }
            });
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer inpt = new StringTokenizer(br.readLine());
                long a = Long.parseLong(inpt.nextToken());
                long b = Long.parseLong(inpt.nextToken());
                if (a < b) {
                    long tmp = a;
                    a = b;
                    b = tmp;
                }
                long res = a - countNoZero(a) + 1 - (b - 1 - countNoZero(b - 1) + 1);
                for (List<Integer> interestingInteger : interestingIntegers) {
                    res += countPermutationsLessOrEqual(a, interestingInteger) - countPermutationsLessOrEqual(b - 1, interestingInteger);
                }
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }

    private static int[] convertDigitToArray(long val) {
        String valStr = String.valueOf(val);
        char[] digitsChar = valStr.toCharArray();
        int[] digits = new int[digitsChar.length];
        for (int i = 0; i < digits.length; i++) {
            digits[i] = digitsChar[i] - '0';
        }
        return digits;
    }

    private static long countNoZero(long n) {
        int[] nDigs = convertDigitToArray(n);
        int digsCnt = nDigs.length;
        long res = countNoZero(nDigs, 0);

        for (int i = digsCnt - 1; i > 0; i--) {
            res += fastPow(9, i);
        }
        return res;
    }

    private static long countNoZero(int[] n, int position) {
        if (n[position] == 0) {
            return 0;
        }

        if (position == n.length - 1) {
            return n[position];
        }

        long res = (n[position] - 1) * fastPow(9, n.length - position - 1);
        res += countNoZero(n, position + 1);

        return res;
    }

    private static long countPermutationsLessOrEqual(long maxVal, List<Integer> digits) {
        if (digits.size() == 1) {
            return maxVal >= digits.get(0) ? 1 : 0;
        }

        String valStr = String.valueOf(maxVal);
        int digitsCnt = valStr.length();
        if (digitsCnt > digits.size()) {
            return countPermutations(digits);
        } else if (digitsCnt < digits.size()) {
            return 0;
        } else {
            long res = 0;
            int firstDigit = (int)(maxVal / fastPow(10, digitsCnt - 1));
            for (int i = 1; i < firstDigit; i++) {
                if (digits.contains(i)) {
                    digits.remove((Integer) i);
                    res += countPermutations(digits);
                    digits.add(i);
                }
            }
            if (digits.contains(firstDigit)) {
                digits.remove((Integer) firstDigit);
                res += countPermutationsLessOrEqual(maxVal % fastPow(10, digitsCnt - 1), digits);
                digits.add(firstDigit);
            }
            return res;
        }
    }

    private static long fastPow(long n, int p) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return fastPow(n * n, p / 2);
        } else {
            return n * fastPow(n, p - 1);
        }
    }

    private static long countPermutations(List<Integer> digits) {
        Map<Integer, Integer> digCnt = new HashMap<>();
        for (int digit : digits) {
            digCnt.merge(digit, 1, (oldVal, newVal) -> oldVal + newVal);
        }
        return fact(digits.size()) / digCnt.values().stream().map(InterestingIntegers::fact).reduce((a, b) -> a * b).get();
    }

    private static long fact(long n) {
        if (n == 0) {
            return 1;
        }
        return n * fact(n - 1);
    }

    private static List<List<Integer>> splitToSum(int n, int maxVal, int maxCnt) {
        List<List<Integer>> result = new ArrayList<>();

        if (maxCnt == 1) {
            if (n <= maxVal) {
                List<Integer> subResult = new ArrayList<>();
                subResult.add(n);
                result.add(subResult);
            }
            return result;
        }

        for (int i = maxVal; i > 0; i--) {
            if (n - i > 0) {
                List<List<Integer>> subSums = splitToSum(n - i, i, maxCnt - 1);
                for (List<Integer> subSum : subSums) {
                    subSum.add(i);
                    result.add(subSum);
                }
            } else if (n - i == 0) {
                List<Integer> subResult = new ArrayList<>();
                subResult.add(i);
                result.add(subResult);
            }
        }

        return result;
    }
}
