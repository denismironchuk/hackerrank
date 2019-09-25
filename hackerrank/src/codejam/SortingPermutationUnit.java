package codejam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortingPermutationUnit {
    private static final int N = 10;
    private static final int MAX = 10;

    public static void main(String[] args) {
        /*int[] a = new int[N];
        int[] aSort = new int[N];

        for (int i = 0; i < N; i++) {
            a[i] = (int)(MAX * Math.random());
            aSort[i] = a[i];
        }*/

        int[] a = new int[] {1, 2, 6, 5, 4, 10, 5, 5, 4, 3};
        int[] aSort = new int[] {1, 2, 6, 5, 4, 10, 5, 5, 4, 3};

        printArray(a);

        Arrays.sort(aSort);

        int[] pos = new int[N];
        int[] isOccup = new int[N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (aSort[j] == a[i] && isOccup[j] == 0) {
                    pos[i] = j;
                    isOccup[j] = 1;
                    break;
                }
            }
        }

        printArray(pos);

        System.out.println("==============");

        int startIndex = 0;

        for (;startIndex < N && pos[startIndex] == startIndex; startIndex++);

        List<int[][]> perms = new ArrayList<>();
        int[][] perm1 = new int[N][N];
        for (int i = 0; i < startIndex; i++) {
            perm1[i][i] = 1;
        }

        if (startIndex == N) {
            System.out.println("Array is already sorted");
            return;
        }

        perm1[startIndex][startIndex + 1] = 1;
        perm1[startIndex + 1][startIndex] = 1;

        for (int i = startIndex + 2; i < N; i++) {
            perm1[i][i] = 1;
        }

        perms.add(perm1);

        List<Integer> sdvigs = new ArrayList<>();
        int n = N - startIndex;
        int sdvig = 1;

        while (n != 0) {
            sdvigs.add(sdvig);
            n /= 3;
            sdvig *= 3;
        }

        n = N - startIndex - 1;

        for (int s : sdvigs) {
            int[][] perm = new int[N][N];

            for (int i = 0; i <= startIndex; i++) {
                perm[i][i] = 1;
            }

            for (int col = startIndex + 1; col < N; col++) {
                perm[startIndex + 1 + ((col - startIndex - 1 - s + n) % (n))][col] = 1;
            }

            perms.add(perm);
        }

        printMatrix(perms.get(0));
        a = applyPerm(a, perms.get(0));
        pos = applyPerm(pos, perms.get(0));

        printArray(a);
        printArray(pos);
        System.out.println("==============");
        int posToMove = pos[startIndex + 1];
        List<Integer> sdvigsCnt = createSdvigSeq(posToMove - startIndex - 1);

        int move = 1;

        for (int sdvigCnt : sdvigsCnt) {
            for (int i = 0; i < sdvigCnt; i++) {
                printMatrix(perms.get(move));
                System.out.println("==============");
                a = applyPerm(a, perms.get(move));
                pos = applyPerm(pos, perms.get(move));
                System.out.println("==============");
                printArray(a);
                printArray(pos);
            }
            move++;
        }
    }

    private static List<Integer> createSdvigSeq(int expectedSdvig) {
        List<Integer> res = new ArrayList<>();
        while (expectedSdvig != 0) {
            res.add(expectedSdvig % 3);
            expectedSdvig /= 3;
        }

        return res;
    }

    private static void printArray(int[] a) {
        for (int i = 0; i < N; i++) {
            System.out.printf("%2s ", a[i]);
        }

        System.out.println();
    }

    private static int[] applyPerm(int[] a, int[][] perm) {
        int[] res = new int[a.length];

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                res[i] += a[j] * perm[j][i];
            }
        }

        return res;
    }

    private static void printMatrix(int[][] matr) {
        for (int row = 0; row < matr.length; row++) {
            for (int col = 0; col < matr[row].length; col++) {
                System.out.printf("%s ", matr[row][col]);
            }
            System.out.println();
        }
        System.out.println("=============");
    }
}
