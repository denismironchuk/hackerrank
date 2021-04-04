package codejam.year2020.worldFinal.hexacoinJam;

public class HexacoinJamTest {
    private static int n = 2;
    private static int maxDigit = 16;

    public static void main(String[] args) {
        //int[] num1 = generateNumber();
        //int[] num2 = generateNumber();

        //int[] lowerLimit = generateNumber();
        //int[] upperLimit = generateNumberGreaterOrEqualThan(lowerLimit);

        int[] lowerLimit = new int[] {15, 9};
        int[] upperLimit = new int[] {15, 10};

        printNum(lowerLimit);
        printNum(upperLimit);

        long res = 0;

        for (int sum = lowerLimit[0]; sum <= upperLimit[0]; sum++) {
            res += countSums(0, sum, lowerLimit, upperLimit);
        }

        System.out.println(res);

        int resTrivial = 0;

        long lowerDec = convertToDec(lowerLimit);
        long upperDec = convertToDec(upperLimit);

        for (long i = lowerDec; i <= upperDec; i++) {
            resTrivial += i + 1;
        }
        System.out.println(resTrivial);
    }

    private static long convertToDec(int[] num) {
        long res = 0;
        for (int i = 0; i < n; i++) {
            res *= maxDigit;
            res += num[i];
        }
        return res;
    }

    /*private static long countSums(int position, int sum, int[] lowerLimit, int[] upperLimit) {
        if (position == n - 1) {
            if (sum < maxDigit) {
                return sum + 1;
            } else {
                return 2 * maxDigit - sum - 1;
            }
        }

        long res = 0;

        if (sum == upperLimit[position]) {
            int start = upperLimit[position] == lowerLimit[position] ? lowerLimit[position + 1] : 0;
            for (int j = 0; j <= sum; j++) {
                for (int k = start; k <= upperLimit[position + 1]; k++) {
                    res += countSums(position + 1, k, lowerLimit, upperLimit);
                }
            }
        } else if (sum == upperLimit[position] - 1) {
            int start = upperLimit[position] - 1 == lowerLimit[position] ? lowerLimit[position + 1] : 0;
            for (int j = 0; j <= sum; j++) {
                for (int k = start; k <= upperLimit[position + 1] + maxDigit; k++) {
                    res += countSums(position + 1, k, lowerLimit, upperLimit);
                }
            }
        } else if (sum == lowerLimit[position]) {
            for (int j = 0; j <= sum; j++) {
                for (int k = lowerLimit[position + 1]; k <= 2 * maxDigit - 1; k++) {
                    res += countSums(position + 1, k, lowerLimit, upperLimit);
                }
            }
        } else if (sum == lowerLimit[position] - 1) {
            for (int j = 0; j <= sum; j++) {
                for (int k = lowerLimit[position + 1] + maxDigit; k <= 2 * maxDigit - 1; k++) {
                    res += countSums(position + 1, k, lowerLimit, upperLimit);
                }
            }
        } else {
            for (int j = 0; j <= sum; j++) {
                for (int k = 0; k <= 2 * maxDigit - 1; k++) {
                    res += countSums(position + 1, k, lowerLimit, upperLimit);
                }
            }
        }

        return res;
    }*/

    private static long countSums(int position, int sum, int[] lowerLimit, int[] upperLimit) {
        long res = 0;

        if (sum == upperLimit[position]) {
            for (int j = 0; j <= sum; j++) {
                if (j >= maxDigit || sum - j >= maxDigit) {
                    continue;
                }

                for (int k = 0; k <= upperLimit[position + 1]; k++) {
                    res += countSums(position + 1, k, lowerLimit, upperLimit);
                }
            }

            for (int j = 0; j <= sum - 1; j++) {
                if (j >= maxDigit || sum - 1 - j >= maxDigit) {
                    continue;
                }

                for (int k = maxDigit; k <= upperLimit[position + 1] + maxDigit; k++) {
                    res += countSums(position + 1, k, lowerLimit, upperLimit);
                }
            }
        } else if (sum == lowerLimit[position]) {
            for (int j = 0; j <= sum; j++) {
                if (j >= maxDigit || sum - j >= maxDigit) {
                    continue;
                }

                for (int k = lowerLimit[position + 1]; k < maxDigit; k++) {
                    res += countSums(position + 1, k, lowerLimit, upperLimit);
                }
            }

            for (int j = 0; j <= sum - 1; j++) {
                if (j >= maxDigit || sum - 1 - j >= maxDigit) {
                    continue;
                }

                for (int k = lowerLimit[position + 1] + maxDigit; k <= 2 * (maxDigit - 1); k++) {
                    res += countSums(position + 1, k, lowerLimit, upperLimit);
                }
            }
        } else {
            for (int j = 0; j <= sum; j++) {
                if (j >= maxDigit || sum - j >= maxDigit) {
                    continue;
                }

                for (int k = 0; k < maxDigit; k++) {
                    res += countSums(position + 1, k, lowerLimit, upperLimit);
                }
            }

            for (int j = 0; j <= sum - 1; j++) {
                if (j >= maxDigit || sum - 1 - j >= maxDigit) {
                    continue;
                }

                for (int k = maxDigit; k <= 2 * (maxDigit - 1); k++) {
                    res += countSums(position + 1, k, lowerLimit, upperLimit);
                }
            }
        }

        return res;
    }

    private static long fastPow(int n, int p) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return fastPow(n * n, p / 2);
        } else {
            return n * fastPow(n, p - 1);
        }
    }

    private static int[] generateNumber() {
        int[] num = new int[n];
        for (int i = 0; i < n; i++) {
            num[i] = (int)(Math.random() * maxDigit);
        }
        return num;
    }

    private static int[] generateNumberGreaterOrEqualThan(int[] num) {
        int[] greaterNum = new int[n];
        boolean isGreater = false;
        for (int i = 0; i < n; i++) {
            if (isGreater) {
                greaterNum[i] = (int)(Math.random() * maxDigit);
            } else {
                greaterNum[i] = num[i] + (int)(Math.random() * (maxDigit - num[i]));
                isGreater = isGreater || greaterNum[i] > num[i];
            }
        }

        return greaterNum;
    }

    private static void printNum(int[] num) {
        for (int i = 0; i < n; i++) {
            System.out.printf("%2d ", num[i]);
        }
        System.out.println();
    }
}
