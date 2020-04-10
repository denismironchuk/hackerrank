package codejam.year2019.sortingPermutationUnit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Solution {
    private static int[] a;
    private static int[] pos;
    private static List<int[][]> perms;
    private static int N;
    private static int S;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t <= T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int p = Integer.parseInt(tkn.nextToken());
            S = Integer.parseInt(tkn.nextToken());
            int K = Integer.parseInt(tkn.nextToken());
            N = Integer.parseInt(tkn.nextToken());

            System.out.printf("Case #%s:\n", t);

            generateAllowablePermutations();
            System.out.println(perms.size());
            for (int[][] perm : perms) {
                printPermutation(perm, N);
            }

            for (int k = 0; k < K; k++) {
                a = new int[N];
                int[] aSort = new int[N];
                pos = new int[N];

                StringTokenizer arrTkn = new StringTokenizer(br.readLine());
                for (int i = 0; i < N; i++) {
                    a[i] = Integer.parseInt(arrTkn.nextToken());
                    aSort[i] = a[i];
                }
                Arrays.sort(aSort);
                pos = calculateCorrectPositions(a, aSort, N);
                generatePermSeq();
            }
        }
    }

    private static void generatePermSeq() {
        List<Integer> seq = new ArrayList<>();

        int seqShuffle = 0;
        while (true) {
            if (pos[0] == 0) {
                int disorderedPos = 1;
                for (; disorderedPos < N && pos[disorderedPos] == 1 + (N - 2 - seqShuffle + disorderedPos) % (N - 1); disorderedPos++)
                    ;

                if (disorderedPos == N) {
                    applySdvig(N - 1 - seqShuffle, seq);
                    break;
                }

                int locShuffle = N - disorderedPos;

                seqShuffle += locShuffle;
                seqShuffle %= N - 1;

                applySdvig(locShuffle, seq);
                shuffleFirstElems(seq);
            }

            int newShuff = (N - pos[0]) % (N - 1);

            applySdvig((newShuff - seqShuffle + (N - 1)) % (N - 1), seq);

            seqShuffle = newShuff;
            shuffleFirstElems(seq);
        }

        if (seq.size() >= S) {
            throw new RuntimeException();
        }

        System.out.printf("%s ", seq.size());
        for (int permNum : seq) {
            System.out.printf("%s ", permNum);
        }

        System.out.println();
    }

    private static void applySdvig(int sdvig, List<Integer> seq) {
        if (sdvig == N - 1) {
            return;
        }

        List<Integer> sdvigsCnt = createSdvigSeq(sdvig);

        int move = 1;

        for (int sdvigCnt : sdvigsCnt) {
            for (int i = 0; i < sdvigCnt; i++) {
                a = applyPerm(a, perms.get(move));
                pos = applyPerm(pos, perms.get(move));

                seq.add(move + 1);
            }
            move++;
        }
    }

    private static void shuffleFirstElems(List<Integer> seq) {
        a = applyPerm(a, perms.get(0));
        pos = applyPerm(pos, perms.get(0));
        seq.add(1);
    }

    private static List<Integer> createSdvigSeq(int expectedSdvig) {
        List<Integer> res = new ArrayList<>();
        while (expectedSdvig != 0) {
            res.add(expectedSdvig % 3);
            expectedSdvig /= 3;
        }

        return res;
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

    private static void printPermutation(int[][] perm, int N) {
        for (int col = 0; col < N; col++) {
            for (int row = 0; row < N; row++) {
                if (perm[row][col] == 1) {
                    System.out.printf("%s ", row + 1);
                    break;
                }
            }
        }
        System.out.println();
    }

    private static int[] calculateCorrectPositions(int[] a, int[] aSort, int N) {
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

        return pos;
    }

    private static void generateAllowablePermutations() {
        perms = new ArrayList<>();

        int[][] perm1 = new int[N][N];

        perm1[0][1] = 1;
        perm1[1][0] = 1;

        for (int i = 2; i < N; i++) {
            perm1[i][i] = 1;
        }

        perms.add(perm1);

        List<Integer> sdvigs = new ArrayList<>();
        int n = N - 1;
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
}