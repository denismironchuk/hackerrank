package codejam.year2020.worldFinal.hexacoinJam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SplitToSumInterval {

    private static int n = 5;
    private static int maxDigit = 16;

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

    private static void printPerm(int[] perm) {
        for (int i = 0; i < maxDigit; i++) {
            System.out.printf("%2d ", perm[i]);
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

    private static int[] applyPermutationToNumber(int[] perm, int[] num) {
        int[] newNum = new int[n];

        for (int i = 0; i < n; i++) {
            newNum[i] = perm[num[i]];
        }

        return newNum;
    }

    private static void buildAllPermutations(int pos, Set<Integer> usedVals, int[] permutation, List<int[]> allPerms) {
        if (pos == maxDigit) {
            allPerms.add(Arrays.copyOf(permutation, maxDigit));
            return;
        }

        for (int i = 0; i < maxDigit; i++) {
            if (usedVals.contains(i)) {
                continue;
            }

            permutation[pos] = i;
            usedVals.add(i);
            buildAllPermutations(pos + 1, usedVals, permutation, allPerms);
            usedVals.remove(i);
        }
    }

    public static void main(String[] args) {
        List<int[]> allPerms = new ArrayList<>();
        //buildAllPermutations(0, new HashSet<>(), new int[maxDigit], allPerms);

        int cnt = 0;
        while (true) {
            cnt++;
            if (cnt % 1000 == 0) {
                System.out.println(cnt);
            }
            int[] start = generateNumber();
            int[] end = generateNumberGreaterOrEqualThan(start);

            int[] num1 = generateNumber();
            int[] num2 = generateNumber();

            //int[] start = new int[] {4, 13, 7, 8, 7};
            //int[] end = new int[] {9, 0, 15, 13, 9};

            //int[] num1 = new int[] {1, 2, 3, 4, 5};
            //int[] num2 = new int[] {1, 6, 7, 8, 9};

            /*printNum(start);
            printNum(end);
            System.out.println("=====Numbers=====");
            printNum(num1);
            printNum(num2);*/

            int[] permutation = new int[maxDigit];
            Arrays.fill(permutation, -1);
            int[] reversePermutation = new int[maxDigit];
            Arrays.fill(reversePermutation, -1);
            //long startTime = System.currentTimeMillis();
            long res = countPermutations(0, start, end, num1, num2, permutation, reversePermutation, false);
            //long endTime = System.currentTimeMillis();

            //System.out.println(endTime - startTime + "ms");

            /*System.out.println(res);

            long resTrivial = countTrivial(num1, num2, start, end, allPerms);

            System.out.println(resTrivial);

            if (res != resTrivial) {
                throw new RuntimeException();
            }*/
        }
    }

    private static long countTrivial(int[] num1, int[] num2, int[] start, int[] end, List<int[]> allPerms) {
        long res = 0;
        for (int[] perm : allPerms) {
            long permedNum1 = convertToDec(applyPermutationToNumber(perm, num1));
            long permedNum2 = convertToDec(applyPermutationToNumber(perm, num2));

            long startDec = convertToDec(start);
            long endDec = convertToDec(end);

            if (permedNum1 + permedNum2 >= startDec && permedNum1 + permedNum2 <= endDec) {
                res++;
            }
        }
        return res;
    }

    private static long getFreeValsFactorial(int[] permutation) {
        int freeCnt = 0;
        for (int p : permutation) {
            if (p == -1) {
                freeCnt++;
            }
        }
        long fact = 1;
        for (int i = 1; i <= freeCnt; i++) {
            fact *= i;
        }
        return fact;
    }

    private static boolean isValidPair(int val1, int val2, int substVal1, int substVal2, int[] permutation, int[] reversePermutation) {
        if (reversePermutation[substVal1] != -1 && reversePermutation[substVal1] != val1) {
            return false;
        }

        if (reversePermutation[substVal2] != -1 && reversePermutation[substVal2] != val2) {
            return false;
        }

        if (val1 == val2 && substVal1 != substVal2) {
            return false;
        }

        if (val1 != val2 && substVal1 == substVal2) {
            return false;
        }

        if (permutation[val1] != -1 && substVal1 != permutation[val1]) {
            return false;
        }

        if (permutation[val2] != -1 && substVal2 != permutation[val2]) {
            return false;
        }

        if (permutation[val1] != -1 && permutation[val2] != -1
                && !(substVal1 == permutation[val1] && substVal2 == permutation[val2])) {
            return false;
        }

        return true;
    }

    public static long countPermutations(int pos, int[] start, int[] end, int[] num1, int[] num2, int[] permutation, int[] reversePermutation, boolean setAllStartToZero) {
        int tmpStartGlobal = start[pos];

        if (setAllStartToZero) {
            start[pos] = 0;
        }
        long res = 0;

        if (pos == n - 1) {
            for (int limit = start[pos]; limit <= end[pos]; limit++) {
                for (int sum = 0; sum <= limit; sum++) {
                    if (sum >= maxDigit || limit - sum >= maxDigit) {
                        continue;
                    }

                    if (!isValidPair(num1[pos], num2[pos], sum, limit - sum, permutation, reversePermutation)) {
                        continue;
                    }

                    int perm1Tmp = permutation[num1[pos]];
                    int perm2Tmp = permutation[num2[pos]];

                    int revPerm1Tmp = reversePermutation[sum];
                    int revPerm2Tmp = reversePermutation[limit - sum];

                    permutation[num1[pos]] = sum;
                    permutation[num2[pos]] = limit - sum;

                    reversePermutation[sum] = num1[pos];
                    reversePermutation[limit - sum] = num2[pos];

                    res+= getFreeValsFactorial(permutation);

                    permutation[num1[pos]] = perm1Tmp;
                    permutation[num2[pos]] = perm2Tmp;

                    reversePermutation[sum] = revPerm1Tmp;
                    reversePermutation[limit - sum] = revPerm2Tmp;
                }
            }
        } else {
            if (start[pos] > 0) {
                for (int sum = 0; sum <= start[pos] - 1; sum++) {
                    if (sum >= maxDigit || start[pos] - 1 - sum >= maxDigit) {
                        continue;
                    }

                    if (!isValidPair(num1[pos], num2[pos], sum, start[pos] - 1 - sum, permutation, reversePermutation)) {
                        continue;
                    }

                    int startTmp = start[pos + 1];
                    int endTmp = end[pos + 1];

                    start[pos + 1] += maxDigit;

                    if (start[pos] == end[pos]) {
                        end[pos + 1] += maxDigit;
                    } else {
                        end[pos + 1] = 2 * maxDigit;
                    }

                    int perm1Tmp = permutation[num1[pos]];
                    int perm2Tmp = permutation[num2[pos]];

                    int revPerm1Tmp = reversePermutation[sum];
                    int revPerm2Tmp = reversePermutation[start[pos] - 1 - sum];

                    permutation[num1[pos]] = sum;
                    permutation[num2[pos]] = start[pos] - 1 - sum;

                    reversePermutation[sum] = num1[pos];
                    reversePermutation[start[pos] - 1 - sum] = num2[pos];

                    res += countPermutations(pos + 1, start, end, num1, num2, permutation, reversePermutation, setAllStartToZero);

                    permutation[num1[pos]] = perm1Tmp;
                    permutation[num2[pos]] = perm2Tmp;

                    reversePermutation[sum] = revPerm1Tmp;
                    reversePermutation[start[pos] - 1 - sum] = revPerm2Tmp;

                    start[pos + 1] = startTmp;
                    end[pos + 1] = endTmp;
                }
            }

            for (int sum = 0; sum <= start[pos]; sum++) {
                if (sum >= maxDigit || start[pos] - sum >= maxDigit) {
                    continue;
                }

                if (!isValidPair(num1[pos], num2[pos], sum, start[pos] - sum, permutation, reversePermutation)) {
                    continue;
                }

                int startTmp = start[pos + 1];
                int endTmp = end[pos + 1];

                if (start[pos] == end[pos]) {
                    //Do nothing
                } else if (start[pos] + 1 == end[pos]) {
                    end[pos + 1] += maxDigit;
                } else {
                    end[pos + 1] = 2 * maxDigit;
                }

                int perm1Tmp = permutation[num1[pos]];
                int perm2Tmp = permutation[num2[pos]];

                int revPerm1Tmp = reversePermutation[sum];
                int revPerm2Tmp = reversePermutation[start[pos] - sum];

                permutation[num1[pos]] = sum;
                permutation[num2[pos]] = start[pos] - sum;

                reversePermutation[sum] = num1[pos];
                reversePermutation[start[pos] - sum] = num2[pos];

                res += countPermutations(pos + 1, start, end, num1, num2, permutation, reversePermutation, setAllStartToZero);

                permutation[num1[pos]] = perm1Tmp;
                permutation[num2[pos]] = perm2Tmp;

                reversePermutation[sum] = revPerm1Tmp;
                reversePermutation[start[pos] - sum] = revPerm2Tmp;

                start[pos + 1] = startTmp;
                end[pos + 1] = endTmp;
            }

            for (int limit = start[pos] + 1; limit < end[pos] - 1; limit++) {
                for (int sum = 0; sum <= limit; sum++) {
                    if (sum >= maxDigit || limit - sum >= maxDigit) {
                        continue;
                    }

                    if (!isValidPair(num1[pos], num2[pos], sum, limit - sum, permutation, reversePermutation)) {
                        continue;
                    }

                    int startTmp = start[pos + 1];
                    int endTmp = end[pos + 1];

                    end[pos + 1] = 2 * maxDigit;

                    int perm1Tmp = permutation[num1[pos]];
                    int perm2Tmp = permutation[num2[pos]];

                    int revPerm1Tmp = reversePermutation[sum];
                    int revPerm2Tmp = reversePermutation[limit - sum];

                    permutation[num1[pos]] = sum;
                    permutation[num2[pos]] = limit - sum;

                    reversePermutation[sum] = num1[pos];
                    reversePermutation[limit - sum] = num2[pos];

                    res+= getFreeValsFactorial(permutation);
                    //res += countPermutations(pos + 1, start, end, num1, num2, permutation, reversePermutation, true);

                    permutation[num1[pos]] = perm1Tmp;
                    permutation[num2[pos]] = perm2Tmp;

                    reversePermutation[sum] = revPerm1Tmp;
                    reversePermutation[limit - sum] = revPerm2Tmp;

                    start[pos + 1] = startTmp;
                    end[pos + 1] = endTmp;
                }
            }

            if (start[pos] < end[pos] - 1) {
                for (int sum = 0; sum <= end[pos] - 1; sum++) {
                    if (sum >= maxDigit || end[pos] - 1 - sum >= maxDigit) {
                        continue;
                    }

                    if (!isValidPair(num1[pos], num2[pos], sum, end[pos] - 1 - sum, permutation, reversePermutation)) {
                        continue;
                    }

                    int startTmp = start[pos + 1];
                    int endTmp = end[pos + 1];

                    end[pos + 1] += maxDigit;

                    int perm1Tmp = permutation[num1[pos]];
                    int perm2Tmp = permutation[num2[pos]];

                    int revPerm1Tmp = reversePermutation[sum];
                    int revPerm2Tmp = reversePermutation[end[pos] - 1 - sum];

                    permutation[num1[pos]] = sum;
                    permutation[num2[pos]] = end[pos] - 1 - sum;

                    reversePermutation[sum] = num1[pos];
                    reversePermutation[end[pos] - 1 - sum] = num2[pos];

                    res += countPermutations(pos + 1, start, end, num1, num2, permutation, reversePermutation, true);

                    permutation[num1[pos]] = perm1Tmp;
                    permutation[num2[pos]] = perm2Tmp;

                    reversePermutation[sum] = revPerm1Tmp;
                    reversePermutation[end[pos] - 1 - sum] = revPerm2Tmp;

                    start[pos + 1] = startTmp;
                    end[pos + 1] = endTmp;
                }
            }

            if (start[pos] < end[pos]) {
                for (int sum = 0; sum <= end[pos]; sum++) {
                    if (sum >= maxDigit || end[pos] - sum >= maxDigit) {
                        continue;
                    }

                    if (!isValidPair(num1[pos], num2[pos], sum, end[pos] - sum, permutation, reversePermutation)) {
                        continue;
                    }

                    int startTmp = start[pos + 1];
                    int endTmp = end[pos + 1];

                    int perm1Tmp = permutation[num1[pos]];
                    int perm2Tmp = permutation[num2[pos]];

                    int revPerm1Tmp = reversePermutation[sum];
                    int revPerm2Tmp = reversePermutation[end[pos] - sum];

                    reversePermutation[sum] = num1[pos];
                    reversePermutation[end[pos] - sum] = num2[pos];

                    permutation[num1[pos]] = sum;
                    permutation[num2[pos]] = end[pos] - sum;

                    res += countPermutations(pos + 1, start, end, num1, num2, permutation, reversePermutation, true);

                    permutation[num1[pos]] = perm1Tmp;
                    permutation[num2[pos]] = perm2Tmp;

                    reversePermutation[sum] = revPerm1Tmp;
                    reversePermutation[end[pos] - sum] = revPerm2Tmp;

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
