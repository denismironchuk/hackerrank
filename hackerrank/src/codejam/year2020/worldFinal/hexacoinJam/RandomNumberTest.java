package codejam.year2020.worldFinal.hexacoinJam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RandomNumberTest {
    public static final int MAX_CNT = 450;
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

    public static long hashCode(int a[]) {
        if (a == null)
            return 0;

        long result = 1;
        for (int element : a)
            result = 31 * result + element;

        return result;
    }

    public static void main(String[] args) {
        /*List<int[]> nums = new ArrayList<>();
        for (int i = 0; i < MAX_CNT; i++) {
            nums.add(generateNumber());
        }

        Set<Long> hashes = new HashSet<>();

        for (int i = 0; i < MAX_CNT; i++) {
            for (int j = i; j < MAX_CNT; j++) {
                int[] normPair = normalizePair(nums.get(i), nums.get(j));
                hashes.add(hashCode(normPair));
            }
        }

        System.out.println(hashes.size());*/

        int[] num1 = generateNumber();
        int[] num2 = generateNumber();
        printNum(num1);
        printNum(num2);

        List<int[]> normPair = normalizePair(num1, num2);
        printNum(normPair.get(0));
        printNum(normPair.get(1));

        List<int[]> normPair2 = normalizePair(num2, num1);
        printNum(normPair2.get(0));
        printNum(normPair2.get(1));
    }

    //public static int[] normalizePair(int[] n1, int[] n2) {
    public static List<int[]> normalizePair(int[] n1, int[] n2) {
        int[] originalNums = new int[2 * n];
        for (int i = 0; i < 2 * n; i++) {
            if (i < n) {
                originalNums[i] = n1[i];
            } else {
                originalNums[i] = n2[i % n];
            }
        }
        int[] norm = new int[2 * n];
        int[] processed = new int[2 * n];

        int curVal = 1;

        for (int i = 0; i < 2 * n; i++) {
            if (processed[i] == 0) {
                for (int j = i; j < 2 * n; j++) {
                    if (originalNums[i] == originalNums[j]) {
                        norm[j] = curVal;
                        processed[j] = 1;
                    }
                }
                curVal++;
            }
        }

        //return norm;

        int[] norm1 = new int[n];
        int[] norm2 = new int[n];

        for (int i = 0; i < 2 * n; i++) {
            if (i < n) {
                norm1[i] = norm[i];
            } else {
                norm2[i % n] = norm[i];
            }
        }

        return Arrays.asList(norm1, norm2);
    }
}
