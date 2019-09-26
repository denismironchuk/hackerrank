package codejam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortingPermutationUnit {
    private static final int N = 50;
    private static final int MAX = 1000;

    private static int[] a;
    private static int[] pos;
    private static List<int[][]> perms = new ArrayList<>();;

    private static int CNT = 0;

    public static void main(String[] args) {
        while (true) {
            CNT = 0;
            a = new int[N];
            int[] aSort = new int[N];

            for (int i = 0; i < N; i++) {
                a[i] = (int) (MAX * Math.random());
                aSort[i] = a[i];
            }

            //printArray(a);
            Arrays.sort(aSort);
            calculateCorrectPositions(aSort);
            //printArray(pos);
            generateAllowablePermutations();

            int seqShuffle = 0;
            while (true) {
                if (pos[0] == 0) {
                    int disorderedPos = 1;
                    for (; disorderedPos < N && pos[disorderedPos] == 1 + (N - 2 - seqShuffle + disorderedPos) % (N - 1); disorderedPos++)
                        ;

                    if (disorderedPos == N) {
                        applySdvig(N - 1 - seqShuffle);
                        break;
                    }

                    int locShuffle = N - disorderedPos;

                    seqShuffle += locShuffle;
                    seqShuffle %= N - 1;

                    applySdvig(locShuffle);
                    shuffleFirstElems();
                }

                int newShuff = (N - pos[0]) % (N - 1);

                applySdvig((newShuff - seqShuffle + (N - 1)) % (N - 1));

                seqShuffle = newShuff;
                shuffleFirstElems();
            }
            //System.out.println("================");
            //printArray(a);
            //printArray(pos);

            validate(a, aSort);
            System.out.println(CNT);
        }
    }

    private static void validate(int[] a, int[] aSort) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] != aSort[i]) {
                throw new RuntimeException("!!!!!!!!!");
            }
        }
    }

    private static void calculateCorrectPositions(int[] aSort) {
        pos = new int[N];
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
    }

    private static void generateAllowablePermutations() {
        int[][] perm1 = new int[N][N];

        perm1[0][1] = 1;
        perm1[1][0] = 1;

        for (int i = 2; i < N; i++) {
            perm1[i][i] = 1;
        }

        perms.add(perm1);

        List<Integer> sdvigs = new ArrayList<>();
        int n = N;
        int sdvig = 1;

        while (n != 0) {
            sdvigs.add(sdvig);
            n /= 3;
            sdvig *= 3;
        }

        for (int s : sdvigs) {
            int[][] perm = new int[N][N];
            perm[0][0] = 1;

            for (int col = 1; col < N; col++) {
                perm[1 + ((col - s + N - 2) % (N - 1))][col] = 1;
            }

            perms.add(perm);
        }
    }

    private static void applySdvig(int sdvig) {
        List<Integer> sdvigsCnt = createSdvigSeq(sdvig);

        int move = 1;

        for (int sdvigCnt : sdvigsCnt) {
            for (int i = 0; i < sdvigCnt; i++) {
                //printMatrix(perms.get(move));
                a = applyPerm(a, perms.get(move), 1);
                pos = applyPerm(pos, perms.get(move), 0);
                //printArray(a);
                //printArray(pos);
            }
            move++;
        }
    }

    private static void shuffleFirstElems() {
        //printMatrix(perms.get(0));
        a = applyPerm(a, perms.get(0), 1);
        pos = applyPerm(pos, perms.get(0), 0);
        //printArray(a);
        //printArray(pos);
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
            System.out.printf("%3s ", a[i]);
        }

        System.out.println();
    }

    private static int[] applyPerm(int[] a, int[][] perm, int incr) {
        CNT += incr;

        int[] res = new int[a.length];

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                res[i] += a[j] * perm[j][i];
            }
        }

        return res;
    }

    private static void printMatrix(int[][] matr) {
        System.out.println("==============");
        for (int row = 0; row < matr.length; row++) {
            for (int col = 0; col < matr[row].length; col++) {
                System.out.printf("%s ", matr[row][col]);
            }
            System.out.println();
        }
        System.out.println("=============");
    }
}
