package codejam.year2020.worldFinal.hexacoinJam;

public class SplitToSumTest {

    private static int n = 5;
    private static int maxDigit = 16;

    private static int[] generateNumber() {
        int[] num = new int[n];
        for (int i = 0; i < n; i++) {
            num[i] = (int)(Math.random() * maxDigit);
        }
        return num;
    }

    private static void printNum(int[] num) {
        for (int i = 0; i < n; i++) {
            System.out.printf("%2d ", num[i]);
        }
        System.out.println();
    }

    private static long convertToDec(int[] num) {
        long res = 0;
        for (int i = 0; i < n; i++) {
            res *= maxDigit;
            res += num[i];
        }
        return res;
    }

    public static void main(String[] args) {
        while (true) {
            int[] num = generateNumber();
            printNum(num);
            long val1 = countWaysToSplit(num, 0);
            long val2 = convertToDec(num) + 1;
            System.out.println(val1);
            System.out.println(val2);
            System.out.println("==============");
            if (val1 != val2) {
                throw new RuntimeException();
            }
        }
    }

    private static long countWaysToSplit(int[] num, int pos) {
        if (pos == n) {
            return 1;
        }

        long res = 0;

        for (int sum = 0; sum <= num[pos]; sum++) {
            if (sum >= maxDigit || num[pos] - sum >= maxDigit) {
                continue;
            }
            res += countWaysToSplit(num, pos + 1);
        }

        if (pos < n - 1) {
            num[pos + 1] += maxDigit;
            for (int sum = 0; sum <= num[pos] - 1; sum++) {
                if (sum >= maxDigit || num[pos] - 1 - sum >= maxDigit) {
                    continue;
                }
                res += countWaysToSplit(num, pos + 1);
            }
            num[pos + 1] -= maxDigit;
        }

        return res;
    }
}
