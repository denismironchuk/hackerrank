package codejam.year2021;

import java.math.BigInteger;

public class HackedExam {
    private static final int Q = 120;
    private static final int N = 3;

    public static void main(String[] args) {
        int[] ans1 = new int[Q];
        int[] ans2 = new int[Q];
        int[] ans3 = new int[Q];

        int rightAnswers1 = -1;
        int rightAnswers2 = -1;
        int rightAnswers3 = -1;

        String a1Str = "TFFFTFFFTTTTTTTFTFFFFFFTTTFTFFFTFTFFTTFTFFTFFTTTFTFTFFTFTFTTFFFFTFTFFFFTFTTFTTFTTTTFFFTTFFFFFTTFFTFFTFFTTTFFFFTTFFTFTTFF";
        String a2Str = "FTFFTTFFFFTFTFFTFFFTTTTTTFFFTTTFTTTTFFTFTTTFTTFFTTTFTFFFFTFFTTFFTTFTTFFTFFTFFTFTTFTFTFFTTTFFTFTFTTFFTFTFTFTTFFTFFFTFTFTF";
        String a3Str = "FTFTTFFFFFTFTFTTTTTTFFTTFTFFFTFFTTTTTTFFFTTTFFFTTFTFFFFFFTFTTFFTFTTTFTTTFTFTTFFFFTFFTTFTFFTTTTTTFTFFFFFTTFFTFTFTFFTTTTTF";

        for (int i = 0; i < Q; i++) {
            //ans1[i] = Math.random() > 0.5 ? 1 : 0;
            //ans2[i] = Math.random() > 0.5 ? 1 : 0;
            //ans3[i] = Math.random() > 0.5 ? 1 : 0;

            ans1[i] = a1Str.charAt(i) == 'T' ? 1 : 0;
            ans2[i] = a2Str.charAt(i) == 'T'  ? 1 : 0;
            ans3[i] = a3Str.charAt(i) == 'T'  ? 1 : 0;
        }

        //rightAnswers1 = (int)((Q + 1) * Math.random());
        //rightAnswers2 = (int)((Q + 1) * Math.random());
        //rightAnswers3 = (int)((Q + 1) * Math.random());

        rightAnswers1 = 55;
        rightAnswers2 = 62;
        rightAnswers3 = 64;

        long start = System.currentTimeMillis();
        BigInteger[][][][] calcRes = new BigInteger[Q][Q + 1][Q + 1][Q + 1];
        BigInteger allPossibleSolutions = countValidPositions(0, ans1, ans2, ans3, rightAnswers1, rightAnswers2, rightAnswers3, calcRes, Q);

        BigInteger chisl = BigInteger.ZERO;

        BigInteger[] cache = new BigInteger[8];

        for (int i = 0; i < 120; i++) {
            int key = ans1[i] * 4 + ans2[i] * 2 + ans3[i];
            if (cache[key] != null) {
                chisl = chisl.add(cache[key]);
                continue;
            }
            //calcRes = new BigInteger[Q][Q + 1][Q + 1][Q + 1];

            int tmp = ans1[0];
            ans1[0] = ans1[i];
            ans1[i] = tmp;

            tmp = ans2[0];
            ans2[0] = ans2[i];
            ans2[i] = tmp;

            tmp = ans3[0];
            ans3[0] = ans3[i];
            ans3[i] = tmp;

            //Suppose answer is True
            int nextRightAnswers1 = ans1[0] == 0 ? rightAnswers1 : rightAnswers1 - 1;
            int nextRightAnswers2 = ans2[0] == 0 ? rightAnswers2 : rightAnswers2 - 1;
            int nextRightAnswers3 = ans3[0] == 0 ? rightAnswers3 : rightAnswers3 - 1;

            BigInteger candidate1 = countValidPositions(1, ans1, ans2, ans3, nextRightAnswers1, nextRightAnswers2, nextRightAnswers3, calcRes, Q);

            //Suppose answer is False
            nextRightAnswers1 = ans1[0] == 1 ? rightAnswers1 : rightAnswers1 - 1;
            nextRightAnswers2 = ans2[0] == 1 ? rightAnswers2 : rightAnswers2 - 1;
            nextRightAnswers3 = ans3[0] == 1 ? rightAnswers3 : rightAnswers3 - 1;

            BigInteger candidate2 = countValidPositions(1, ans1, ans2, ans3, nextRightAnswers1, nextRightAnswers2, nextRightAnswers3, calcRes, Q);

            if (candidate1.compareTo(candidate2) == 1) {
                chisl = chisl.add(candidate1);
                //System.out.print("T");
                cache[key] = candidate1;
            } else {
                chisl = chisl.add(candidate2);
                //System.out.print("F");
                cache[key] = candidate2;
            }

            tmp = ans1[0];
            ans1[0] = ans1[i];
            ans1[i] = tmp;

            tmp = ans2[0];
            ans2[0] = ans2[i];
            ans2[i] = tmp;

            tmp = ans3[0];
            ans3[0] = ans3[i];
            ans3[i] = tmp;
        }

        if (allPossibleSolutions.compareTo(BigInteger.ZERO) == 0) {
            System.out.println("0/0");
        } else {
            BigInteger gcd = chisl.gcd(allPossibleSolutions);
            System.out.println(chisl.divide(gcd));
            System.out.println(allPossibleSolutions.divide(gcd));
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start + "ms");
    }

    private static BigInteger countValidPositions(int pos, int[] ans1, int[] ans2, int[] ans3, int rightAnswers1,
                                                  int rightAnswers2, int rightAnswers3, BigInteger[][][][] calcRes, int questionsCnt) {
        if (rightAnswers1 > questionsCnt - pos || rightAnswers2 > questionsCnt - pos || rightAnswers3 > questionsCnt - pos) {
            return BigInteger.ZERO;
        }

        if (rightAnswers1 < 0 || rightAnswers2 < 0 || rightAnswers3 < 0) {
            return BigInteger.ZERO;
        }

        if (calcRes[pos][rightAnswers1][rightAnswers2][rightAnswers3] != null) {
            return calcRes[pos][rightAnswers1][rightAnswers2][rightAnswers3];
        }

        BigInteger res = BigInteger.ZERO;

        if (pos == questionsCnt - 1) {

            //Suppose answer is True
            if ((ans1[pos] == 0 && rightAnswers1 == 0) || (ans1[pos] == 1 && rightAnswers1 == 1)
                    || (ans2[pos] == 0 && rightAnswers2 == 0) || (ans2[pos] == 1 && rightAnswers2 == 1)
                    || (ans3[pos] == 0 && rightAnswers3 == 0) || (ans3[pos] == 1 && rightAnswers3 == 1)) {
                res = res.add(BigInteger.ONE);
            }
            //Suppose answer is False
            if ((ans1[pos] == 1 && rightAnswers1 == 0) || (ans1[pos] == 0 && rightAnswers1 == 1)
                    || (ans2[pos] == 1 && rightAnswers2 == 0) || (ans2[pos] == 0 && rightAnswers2 == 1)
                    || (ans3[pos] == 1 && rightAnswers3 == 0) || (ans3[pos] == 0 && rightAnswers3 == 1)) {
                res = res.add(BigInteger.ONE);
            }
        } else {
            //Suppose answer is True
            int nextRightAnswers1 = ans1[pos] == 0 ? rightAnswers1 : rightAnswers1 - 1;
            int nextRightAnswers2 = ans2[pos] == 0 ? rightAnswers2 : rightAnswers2 - 1;
            int nextRightAnswers3 = ans3[pos] == 0 ? rightAnswers3 : rightAnswers3 - 1;

            res = res.add(countValidPositions(pos + 1, ans1, ans2, ans3, nextRightAnswers1, nextRightAnswers2, nextRightAnswers3, calcRes, questionsCnt));

            //Suppose answer is False
            nextRightAnswers1 = ans1[pos] == 1 ? rightAnswers1 : rightAnswers1 - 1;
            nextRightAnswers2 = ans2[pos] == 1 ? rightAnswers2 : rightAnswers2 - 1;
            nextRightAnswers3 = ans3[pos] == 1 ? rightAnswers3 : rightAnswers3 - 1;

            res = res.add(countValidPositions(pos + 1, ans1, ans2, ans3, nextRightAnswers1, nextRightAnswers2, nextRightAnswers3, calcRes, questionsCnt));
        }

        calcRes[pos][rightAnswers1][rightAnswers2][rightAnswers3] = res;
        return res;
    }
}
