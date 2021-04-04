package codejam.year2020.worldFinal.hexacoinJam;

public class SplitToSumInterval {

    private static int n = 5;
    private static long maxDigit = 16;

    private static long[] generateNumber() {
        long[] num = new long[n];
        for (int i = 0; i < n; i++) {
            num[i] = (long)(Math.random() * maxDigit);
        }
        return num;
    }

    private static long[] generateNumberGreaterOrEqualThan(long[] num) {
        long[] greaterNum = new long[n];
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

    private static void printNum(long[] num) {
        for (int i = 0; i < n; i++) {
            System.out.printf("%2d ", num[i]);
        }
        System.out.println();
    }

    private static long convertToDec(long[] num) {
        long res = 0;
        for (int i = 0; i < n; i++) {
            res *= maxDigit;
            res += num[i];
        }
        return res;
    }

    public static void main(String[] args) {
        while (true) {
            long[] start = generateNumber();
            long[] end = generateNumberGreaterOrEqualThan(start);

            //long[] start = new long[] {5, 15, 2, 12, 2};
            //long[] end = new long[] {6, 15, 2, 12, 2};

            //int[] start = new int[] {1, 0, 15};
            //int[] end = new int[] {3, 15, 15};

            printNum(start);
            printNum(end);

            long res = countSplits(0, start, end, false);

            System.out.println(res);

            long resTrivial = 0;

            long lowerDec = convertToDec(start);
            long upperDec = convertToDec(end);

            for (long i = lowerDec; i <= upperDec; i++) {
                resTrivial += i + 1;
            }
            System.out.println(resTrivial);

            if (res != resTrivial) {
                throw new RuntimeException();
            }
        }
    }

    public static long countSplits(int pos, long[] start, long[] end, boolean setAllStartToZero) {
        long tmpStartGlobal = start[pos];
        if (setAllStartToZero) {
            start[pos] = 0;
        }
        long res = 0;

        if (pos == n - 1) {
            for (long limit = start[pos]; limit <= end[pos]; limit++) {
                for (int sum = 0; sum <= limit; sum++) {
                    if (sum >= maxDigit || limit - sum >= maxDigit) {
                        continue;
                    }

                    res++;
                }
            }
        } else {
            if (start[pos] > 0) {
                for (int sum = 0; sum <= start[pos] - 1; sum++) {
                    if (sum >= maxDigit || start[pos] - 1 - sum >= maxDigit) {
                        continue;
                    }

                    long startTmp = start[pos + 1];
                    long endTmp = end[pos + 1];

                    start[pos + 1] += maxDigit;

                    if (start[pos] == end[pos]) {
                        end[pos + 1] += maxDigit;
                    } else {
                        end[pos + 1] = 2 * maxDigit;
                    }

                    res += countSplits(pos + 1, start, end, setAllStartToZero);

                    start[pos + 1] = startTmp;
                    end[pos + 1] = endTmp;
                }
            }

            for (int sum = 0; sum <= start[pos]; sum++) {
                if (sum >= maxDigit || start[pos] - sum >= maxDigit) {
                    continue;
                }

                long startTmp = start[pos + 1];
                long endTmp = end[pos + 1];

                if (start[pos] == end[pos]) {
                    //Do nothing
                } else if (start[pos] + 1 == end[pos]) {
                    end[pos + 1] += maxDigit;
                } else {
                    end[pos + 1] = 2 * maxDigit;
                }

                res += countSplits(pos + 1, start, end, setAllStartToZero);

                start[pos + 1] = startTmp;
                end[pos + 1] = endTmp;
            }

            for (long limit = start[pos] + 1; limit < end[pos] - 1; limit++) {
                for (int sum = 0; sum <= limit; sum++) {
                    if (sum >= maxDigit || limit - sum >= maxDigit) {
                        continue;
                    }

                    long startTmp = start[pos + 1];
                    long endTmp = end[pos + 1];

                    //start[pos + 1] = 0;
                    end[pos + 1] = 2 * maxDigit;

                    res += countSplits(pos + 1, start, end, true);

                    start[pos + 1] = startTmp;
                    end[pos + 1] = endTmp;
                }
            }

            if (start[pos] < end[pos] - 1) {
                for (int sum = 0; sum <= end[pos] - 1; sum++) {
                    if (sum >= maxDigit || end[pos] - 1 - sum >= maxDigit) {
                        continue;
                    }

                    long startTmp = start[pos + 1];
                    long endTmp = end[pos + 1];

                    //start[pos + 1] = 0;
                    end[pos + 1] += maxDigit;

                    res += countSplits(pos + 1, start, end, true);

                    start[pos + 1] = startTmp;
                    end[pos + 1] = endTmp;
                }
            }

            if (start[pos] < end[pos]) {
                for (int sum = 0; sum <= end[pos]; sum++) {
                    if (sum >= maxDigit || end[pos] - sum >= maxDigit) {
                        continue;
                    }

                    long startTmp = start[pos + 1];
                    long endTmp = end[pos + 1];

                    //start[pos + 1] = 0;

                    res += countSplits(pos + 1, start, end, true);

                    start[pos + 1] = startTmp;
                    end[pos + 1] = endTmp;
                }
            }
        }


        if (setAllStartToZero) {
            start[pos] = tmpStartGlobal;
        }

        return res;
    }
}
