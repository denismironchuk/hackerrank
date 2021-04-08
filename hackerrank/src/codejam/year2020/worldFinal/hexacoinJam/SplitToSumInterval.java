package codejam.year2020.worldFinal.hexacoinJam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SplitToSumInterval {

    private static int n = 5;
    private static int maxDigit = 16;
    private static long[] FACTS = new long[] {
            1l,
            1l,
            1l * 2l,
            1l * 2l * 3l,
            1l * 2l * 3l * 4l,
            1l * 2l * 3l * 4l * 5l,
            1l * 2l * 3l * 4l * 5l * 6l,
            1l * 2l * 3l * 4l * 5l * 6l * 7l,
            1l * 2l * 3l * 4l * 5l * 6l * 7l * 8l,
            1l * 2l * 3l * 4l * 5l * 6l * 7l * 8l * 9l,
            1l * 2l * 3l * 4l * 5l * 6l * 7l * 8l * 9l * 10l,
            1l * 2l * 3l * 4l * 5l * 6l * 7l * 8l * 9l * 10l * 11l,
            1l * 2l * 3l * 4l * 5l * 6l * 7l * 8l * 9l * 10l * 11l * 12l,
            1l * 2l * 3l * 4l * 5l * 6l * 7l * 8l * 9l * 10l * 11l * 12l * 13l,
            1l * 2l * 3l * 4l * 5l * 6l * 7l * 8l * 9l * 10l * 11l * 12l * 13l * 14l,
            1l * 2l * 3l * 4l * 5l * 6l * 7l * 8l * 9l * 10l * 11l * 12l * 13l * 14l * 15l,
            1l * 2l * 3l * 4l * 5l * 6l * 7l * 8l * 9l * 10l * 11l * 12l * 13l * 14l * 15l * 16l,
    };
    private static long itrCnt = 0;

    private static int[] generateNumber() {
        int[] num = new int[n];
        for (int i = 0; i < n; i++) {
            num[i] = (int)(Math.random() * maxDigit);
        }
        return num;
    }

    private static int[] generateZeroNumber() {
        int[] num = new int[n];
        for (int i = 0; i < n; i++) {
            num[i] = 0;
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
        long startTime = System.currentTimeMillis();
        while (true) {
            cnt++;
            if (cnt % 1000 == 0) {
                long now = System.currentTimeMillis();
                System.out.println(now - startTime + "ms");
                startTime = System.currentTimeMillis();
            }

            int[] zero = generateZeroNumber();
            int[] start = generateNumber();
            int[] end = generateNumberGreaterOrEqualThan(start);

            int[] num1 = generateNumber();
            int[] num2 = generateNumber();

            int[] permutation = new int[maxDigit];
            int[] reversePermutation = new int[maxDigit];

            Arrays.fill(permutation, -1);
            Arrays.fill(reversePermutation, -1);
            /*long res1 = countPermutations(0, start, end, num1, num2, permutation, reversePermutation, false);
            start[0] += maxDigit;
            end[0] += maxDigit;
            Arrays.fill(permutation, -1);
            Arrays.fill(reversePermutation, -1);
            long res2 = countPermutations(0, start, end, num1, num2, permutation, reversePermutation, false);*/

            long res1 = countPermutations(0, start, num1, num2, permutation, reversePermutation, maxDigit);
            Arrays.fill(permutation, -1);
            Arrays.fill(reversePermutation, -1);
            long res2 = countPermutations(0, end, num1, num2, permutation, reversePermutation, maxDigit);

            start[0] += maxDigit;
            end[0] += maxDigit;

            Arrays.fill(permutation, -1);
            Arrays.fill(reversePermutation, -1);
            long res3 = countPermutations(0, start, num1, num2, permutation, reversePermutation, maxDigit);
            Arrays.fill(permutation, -1);
            Arrays.fill(reversePermutation, -1);
            long res4 = countPermutations(0, end, num1, num2, permutation, reversePermutation, maxDigit);

            /*long res1 = countPermutations(0, zero, end, num1, num2, permutation, reversePermutation, false);
            Arrays.fill(permutation, -1);
            Arrays.fill(reversePermutation, -1);
            long res2 = countPermutations(0, end, num1, num2, permutation, reversePermutation, maxDigit);

            if (res1 != res2) {
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

    private static long getFreeValsFactorial(int[] permutation, int freeCnt) {
        return FACTS[freeCnt];
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
        //return 1;
    }

    private static boolean isValidSubst(int val, int substVal, int[] permutation, int[] reversePermutation) {
        if (reversePermutation[substVal] != -1 && reversePermutation[substVal] != val) {
            return false;
        }

        if (permutation[val] != -1 && substVal != permutation[val]) {
            return false;
        }

        return true;
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
                for (int sum = Math.max(0, limit - maxDigit + 1); sum <= Math.min(maxDigit - 1, limit); sum++) {
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

                    res += getFreeValsFactorial(permutation);

                    permutation[num1[pos]] = perm1Tmp;
                    permutation[num2[pos]] = perm2Tmp;

                    reversePermutation[sum] = revPerm1Tmp;
                    reversePermutation[limit - sum] = revPerm2Tmp;
                }
            }
        } else {
            if (start[pos] > 0) {
                for (int sum = Math.max(0, start[pos] - maxDigit); sum <= Math.min(maxDigit - 1, start[pos] - 1); sum++) {
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

            for (int sum = Math.max(0, start[pos] - maxDigit + 1); sum <= Math.min(maxDigit - 1, start[pos]); sum++) {
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
                for (int sum = Math.max(0, limit - maxDigit + 1); sum <= Math.min(maxDigit - 1, limit); sum++) {

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

            if (start[pos] < end[pos] - 1) {
                for (int sum = Math.max(0, end[pos] - maxDigit); sum <= Math.min(maxDigit - 1, end[pos] - 1); sum++) {

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
                for (int sum = Math.max(0, end[pos] - maxDigit + 1); sum <= Math.min(maxDigit - 1, end[pos]); sum++) {

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

    public static long countPermutations(int pos, int[] end, int[] num1, int[] num2, int[] permutation, int[] reversePermutation, int freeCnt) {
        long res = 0;

        if (pos == n - 1) {
            int subst1Start = 0;
            int subst1End = Math.min(maxDigit - 1, end[pos]);

            if (permutation[num1[pos]] != -1) {
                if (permutation[num1[pos]] <= subst1End) {
                    subst1Start = permutation[num1[pos]];
                    subst1End = permutation[num1[pos]];
                } else {
                    subst1Start = 0;
                    subst1End = -1;
                }
            }

            for (int subst1 = subst1Start; subst1 <= subst1End; subst1++) {
                if (!isValidSubst(num1[pos], subst1, permutation, reversePermutation)) {
                    continue;
                }

                int subst2Start = 0;
                int subst2End = Math.min(maxDigit - 1, end[pos] - subst1);

                if (num1[pos] == num2[pos]) {
                    if (subst1 <= subst2End) {
                        subst2Start = subst1;
                        subst2End = subst1;
                    } else {
                        continue;
                    }
                }

                for (int subst2 = subst2Start; subst2 <= subst2End; subst2++) {
                    if (!isValidPair(num1[pos], num2[pos], subst1, subst2, permutation, reversePermutation)) {
                        continue;
                    }

                    if (permutation[num1[pos]] == -1) {
                        freeCnt--;
                    }
                    if (permutation[num2[pos]] == -1 && num1[pos] != num2[pos]) {
                        freeCnt--;
                    }

                    int perm1Tmp = permutation[num1[pos]];
                    int perm2Tmp = permutation[num2[pos]];

                    int revPerm1Tmp = reversePermutation[subst1];
                    int revPerm2Tmp = reversePermutation[subst2];

                    permutation[num1[pos]] = subst1;
                    permutation[num2[pos]] = subst2;

                    reversePermutation[subst1] = num1[pos];
                    reversePermutation[subst2] = num2[pos];

                    res+= FACTS[freeCnt];

                    permutation[num1[pos]] = perm1Tmp;
                    permutation[num2[pos]] = perm2Tmp;

                    reversePermutation[subst1] = revPerm1Tmp;
                    reversePermutation[subst2] = revPerm2Tmp;

                    if (permutation[num1[pos]] == -1) {
                        freeCnt++;
                    }
                    if (permutation[num2[pos]] == -1 && num1[pos] != num2[pos]) {
                        freeCnt++;
                    }
                }
            }
        } else if (end[pos] == 0) {
            if (isValidPair(num1[pos], num2[pos], 0, 0, permutation, reversePermutation)) {
                if (permutation[num1[pos]] == -1) {
                    freeCnt--;
                }
                if (permutation[num2[pos]] == -1 && num1[pos] != num2[pos]) {
                    freeCnt--;
                }

                int perm1Tmp = permutation[num1[pos]];
                int perm2Tmp = permutation[num2[pos]];

                int revPerm1Tmp = reversePermutation[0];
                int revPerm2Tmp = reversePermutation[0];

                reversePermutation[0] = num1[pos];
                reversePermutation[0] = num2[pos];

                permutation[num1[pos]] = 0;
                permutation[num2[pos]] = 0;

                res += countPermutations(pos + 1, end, num1, num2, permutation, reversePermutation, freeCnt);

                permutation[num1[pos]] = perm1Tmp;
                permutation[num2[pos]] = perm2Tmp;

                reversePermutation[0] = revPerm1Tmp;
                reversePermutation[0] = revPerm2Tmp;

                if (permutation[num1[pos]] == -1) {
                    freeCnt++;
                }
                if (permutation[num2[pos]] == -1 && num1[pos] != num2[pos]) {
                    freeCnt++;
                }
            }
        } else {
            int subst1Start = 0;
            int subst1End = Math.min(maxDigit - 1, end[pos] - 2);

            if (permutation[num1[pos]] != -1) {
                if (permutation[num1[pos]] <= subst1End) {
                    subst1Start = permutation[num1[pos]];
                    subst1End = permutation[num1[pos]];
                } else {
                    subst1Start = 0;
                    subst1End = -1;
                }
            }

            for (int subst1 = subst1Start; subst1 <= subst1End; subst1++) {
                if (!isValidSubst(num1[pos], subst1, permutation, reversePermutation)) {
                    continue;
                }

                int subst2Start = 0;
                int subst2End = Math.min(maxDigit - 1, end[pos] - 2 - subst1);

                if (num1[pos] == num2[pos]) {
                    if (subst1 <= subst2End) {
                        subst2Start = subst1;
                        subst2End = subst1;
                    } else {
                        continue;
                    }
                }

                for (int subst2 = subst2Start; subst2 <= subst2End; subst2++) {
                    if (!isValidPair(num1[pos], num2[pos], subst1, subst2, permutation, reversePermutation)) {
                        continue;
                    }

                    if (permutation[num1[pos]] == -1) {
                        freeCnt--;
                    }
                    if (permutation[num2[pos]] == -1 && num1[pos] != num2[pos]) {
                        freeCnt--;
                    }

                    int perm1Tmp = permutation[num1[pos]];
                    int perm2Tmp = permutation[num2[pos]];

                    int revPerm1Tmp = reversePermutation[subst1];
                    int revPerm2Tmp = reversePermutation[subst2];

                    permutation[num1[pos]] = subst1;
                    permutation[num2[pos]] = subst2;

                    reversePermutation[subst1] = num1[pos];
                    reversePermutation[subst2] = num2[pos];

                    res += FACTS[freeCnt];

                    permutation[num1[pos]] = perm1Tmp;
                    permutation[num2[pos]] = perm2Tmp;

                    reversePermutation[subst1] = revPerm1Tmp;
                    reversePermutation[subst2] = revPerm2Tmp;

                    if (permutation[num1[pos]] == -1) {
                        freeCnt++;
                    }
                    if (permutation[num2[pos]] == -1 && num1[pos] != num2[pos]) {
                        freeCnt++;
                    }
                }
            }

            if (end[pos] >= 1) {
                int substStart = Math.max(0, end[pos] - maxDigit);
                int substEnd = Math.min(maxDigit - 1, end[pos] - 1);

                if (num1[pos] == num2[pos]) {
                    if ((end[pos] - 1) % 2 == 0) {
                        substStart = (end[pos] - 1) / 2;
                        substEnd = (end[pos] - 1) / 2;
                    } else {
                        substStart = 0;
                        substEnd = -1;
                    }
                }

                for (int sum = substStart; sum <= substEnd; sum++) {
                    if (!isValidPair(num1[pos], num2[pos], sum, end[pos] - 1 - sum, permutation, reversePermutation)) {
                        continue;
                    }

                    if (permutation[num1[pos]] == -1) {
                        freeCnt--;
                    }
                    if (permutation[num2[pos]] == -1 && num1[pos] != num2[pos]) {
                        freeCnt--;
                    }

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

                    res += countPermutations(pos + 1, end, num1, num2, permutation, reversePermutation, freeCnt);

                    permutation[num1[pos]] = perm1Tmp;
                    permutation[num2[pos]] = perm2Tmp;

                    reversePermutation[sum] = revPerm1Tmp;
                    reversePermutation[end[pos] - 1 - sum] = revPerm2Tmp;

                    end[pos + 1] = endTmp;

                    if (permutation[num1[pos]] == -1) {
                        freeCnt++;
                    }
                    if (permutation[num2[pos]] == -1 && num1[pos] != num2[pos]) {
                        freeCnt++;
                    }
                }
            }

            int substStart = Math.max(0, end[pos] - maxDigit + 1);
            int substEnd = Math.min(maxDigit - 1, end[pos]);

            if (num1[pos] == num2[pos]) {
                if (end[pos] % 2 == 0) {
                    substStart = end[pos] / 2;
                    substEnd = end[pos] / 2;
                } else {
                    substStart = 0;
                    substEnd = -1;
                }
            }

            for (int sum = substStart; sum <= substEnd; sum++) {
                if (!isValidPair(num1[pos], num2[pos], sum, end[pos] - sum, permutation, reversePermutation)) {
                    continue;
                }

                if (permutation[num1[pos]] == -1) {
                    freeCnt--;
                }
                if (permutation[num2[pos]] == -1 && num1[pos] != num2[pos]) {
                    freeCnt--;
                }

                int perm1Tmp = permutation[num1[pos]];
                int perm2Tmp = permutation[num2[pos]];

                int revPerm1Tmp = reversePermutation[sum];
                int revPerm2Tmp = reversePermutation[end[pos] - sum];

                reversePermutation[sum] = num1[pos];
                reversePermutation[end[pos] - sum] = num2[pos];

                permutation[num1[pos]] = sum;
                permutation[num2[pos]] = end[pos] - sum;

                res += countPermutations(pos + 1, end, num1, num2, permutation, reversePermutation, freeCnt);

                permutation[num1[pos]] = perm1Tmp;
                permutation[num2[pos]] = perm2Tmp;

                reversePermutation[sum] = revPerm1Tmp;
                reversePermutation[end[pos] - sum] = revPerm2Tmp;

                if (permutation[num1[pos]] == -1) {
                    freeCnt++;
                }
                if (permutation[num2[pos]] == -1 && num1[pos] != num2[pos]) {
                    freeCnt++;
                }
            }
        }

        return res;
    }
}
