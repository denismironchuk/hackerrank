package codejam.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.StringTokenizer;

public class HackedExam {

    private static class Result {
        private StringBuilder answers = new StringBuilder();
        private BigInteger chisl = BigInteger.ZERO;
        private BigInteger znam = BigInteger.ZERO;
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn.nextToken());
                int q = Integer.parseInt(tkn.nextToken());
                if (n == 1) {
                    StringTokenizer tkn1 = new StringTokenizer(br.readLine());

                    String a1Str = tkn1.nextToken();

                    int rightAnswers1 = Integer.parseInt(tkn1.nextToken());

                    boolean revert1 = rightAnswers1 > q / 2;

                    if (revert1) {
                        rightAnswers1 = q - rightAnswers1;
                    }

                    int[] ans1 = new int[q];

                    for (int i = 0; i < q; i++) {
                        ans1[i] = a1Str.charAt(i) == (revert1 ? 'F' : 'T') ? 1 : 0;
                    }

                    //long start = System.currentTimeMillis();
                    Result res = calculate(ans1, rightAnswers1, q);
                    //long end = System.currentTimeMillis();
                    System.out.printf("Case #%s: %s %s/%s\n", t, res.answers.toString(), res.chisl, res.znam);
                    //System.out.println(end - start + "ms");
                } else if (n == 2) {
                    StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                    StringTokenizer tkn2 = new StringTokenizer(br.readLine());

                    String a1Str = tkn1.nextToken();
                    String a2Str = tkn2.nextToken();

                    int rightAnswers1 = Integer.parseInt(tkn1.nextToken());
                    int rightAnswers2 = Integer.parseInt(tkn2.nextToken());

                    boolean revert1 = rightAnswers1 > q / 2;
                    boolean revert2 = rightAnswers2 > q / 2;

                    if (revert1) {
                        rightAnswers1 = q - rightAnswers1;
                    }

                    if (revert2) {
                        rightAnswers2 = q - rightAnswers2;
                    }

                    int[] ans1 = new int[q];
                    int[] ans2 = new int[q];

                    for (int i = 0; i < q; i++) {
                        ans1[i] = a1Str.charAt(i) == (revert1 ? 'F' : 'T') ? 1 : 0;
                        ans2[i] = a2Str.charAt(i) == (revert2 ? 'F' : 'T') ? 1 : 0;
                    }

                    //long start = System.currentTimeMillis();
                    Result res = calculate(ans1, ans2, rightAnswers1, rightAnswers2, q);
                    //long end = System.currentTimeMillis();
                    System.out.printf("Case #%s: %s %s/%s\n", t, res.answers.toString(), res.chisl, res.znam);
                    //System.out.println(end - start + "ms");
                } else {
                    StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                    StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                    StringTokenizer tkn3 = new StringTokenizer(br.readLine());

                    String a1Str = tkn1.nextToken();
                    String a2Str = tkn2.nextToken();
                    String a3Str = tkn3.nextToken();

                    int rightAnswers1 = Integer.parseInt(tkn1.nextToken());
                    int rightAnswers2 = Integer.parseInt(tkn2.nextToken());
                    int rightAnswers3 = Integer.parseInt(tkn3.nextToken());

                    boolean revert1 = rightAnswers1 > q / 2;
                    boolean revert2 = rightAnswers2 > q / 2;
                    boolean revert3 = rightAnswers3 > q / 2;

                    if (revert1) {
                        rightAnswers1 = q - rightAnswers1;
                    }

                    if (revert2) {
                        rightAnswers2 = q - rightAnswers2;
                    }

                    if (revert3) {
                        rightAnswers3 = q - rightAnswers3;
                    }

                    int[] ans1 = new int[q];
                    int[] ans2 = new int[q];
                    int[] ans3 = new int[q];

                    for (int i = 0; i < q; i++) {
                        ans1[i] = a1Str.charAt(i) == (revert1 ? 'F' : 'T') ? 1 : 0;
                        ans2[i] = a2Str.charAt(i) == (revert2 ? 'F' : 'T') ? 1 : 0;
                        ans3[i] = a3Str.charAt(i) == (revert3 ? 'F' : 'T') ? 1 : 0;
                    }

                    long start = System.currentTimeMillis();
                    Result res = calculate(ans1, ans2, ans3, rightAnswers1, rightAnswers2, rightAnswers3, q);
                    long end = System.currentTimeMillis();
                    System.out.printf("Case #%s: %s %s/%s\n", t, res.answers.toString(), res.chisl, res.znam);
                    System.out.println(end - start + "ms");
                }
            }
        }
    }

    private static Result calculate(int[] ans1, int[] ans2, int[] ans3, int rightAnswers1, int rightAnswers2, int rightAnswers3, int questionsCnt) {
        Result output = new Result();
        BigInteger[][][][] calcRes2 = new BigInteger[questionsCnt][rightAnswers1 + 1][rightAnswers2 + 1][rightAnswers3 + 1];
        BigInteger allPossibleSolutions = countValidPositions(0, ans1, ans2, ans3, rightAnswers1, rightAnswers2, rightAnswers3, calcRes2, questionsCnt);

        BigInteger chisl = BigInteger.ZERO;

        BigInteger[] cache = new BigInteger[8];
        char[] cache2 = new char[8];

        for (int i = 0; i < questionsCnt; i++) {
            int key = ans1[i] * 4 + ans2[i] * 2 + ans3[i];
            if (cache[key] != null) {
                chisl = chisl.add(cache[key]);
                output.answers.append(cache2[key]);
                continue;
            }

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

            BigInteger candidate1 = countValidPositions(1, ans1, ans2, ans3, nextRightAnswers1, nextRightAnswers2, nextRightAnswers3, calcRes2, questionsCnt);

            //Suppose answer is False
            nextRightAnswers1 = ans1[0] == 1 ? rightAnswers1 : rightAnswers1 - 1;
            nextRightAnswers2 = ans2[0] == 1 ? rightAnswers2 : rightAnswers2 - 1;
            nextRightAnswers3 = ans3[0] == 1 ? rightAnswers3 : rightAnswers3 - 1;

            BigInteger candidate2 = countValidPositions(1, ans1, ans2, ans3, nextRightAnswers1, nextRightAnswers2, nextRightAnswers3, calcRes2, questionsCnt);

            if (candidate1.compareTo(candidate2) == 1) {
                chisl = chisl.add(candidate1);
                output.answers.append("T");
                cache[key] = candidate1;
                cache2[key] = 'T';
            } else {
                chisl = chisl.add(candidate2);
                output.answers.append("F");
                cache[key] = candidate2;
                cache2[key] = 'F';
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

        if (allPossibleSolutions.compareTo(BigInteger.ZERO) != 0) {
            BigInteger gcd = chisl.gcd(allPossibleSolutions);
            output.chisl = chisl.divide(gcd);
            output.znam = allPossibleSolutions.divide(gcd);
        }

        return output;
    }

    private static Result calculate(int[] ans1, int[] ans2, int rightAnswers1, int rightAnswers2, int questionsCnt) {
        Result output = new Result();
        BigInteger[][][] calcRes2 = new BigInteger[questionsCnt][rightAnswers1 + 1][rightAnswers2 + 1];
        BigInteger allPossibleSolutions = countValidPositions(0, ans1, ans2, rightAnswers1, rightAnswers2, calcRes2, questionsCnt);

        BigInteger chisl = BigInteger.ZERO;

        BigInteger[] cache = new BigInteger[4];
        char[] cache2 = new char[4];

        for (int i = 0; i < questionsCnt; i++) {
            int key = ans1[i] * 2 + ans2[i];
            if (cache[key] != null) {
                chisl = chisl.add(cache[key]);
                output.answers.append(cache2[key]);
                continue;
            }

            int tmp = ans1[0];
            ans1[0] = ans1[i];
            ans1[i] = tmp;

            tmp = ans2[0];
            ans2[0] = ans2[i];
            ans2[i] = tmp;

            //Suppose answer is True
            int nextRightAnswers1 = ans1[0] == 0 ? rightAnswers1 : rightAnswers1 - 1;
            int nextRightAnswers2 = ans2[0] == 0 ? rightAnswers2 : rightAnswers2 - 1;

            BigInteger candidate1 = countValidPositions(1, ans1, ans2, nextRightAnswers1, nextRightAnswers2, calcRes2, questionsCnt);

            //Suppose answer is False
            nextRightAnswers1 = ans1[0] == 1 ? rightAnswers1 : rightAnswers1 - 1;
            nextRightAnswers2 = ans2[0] == 1 ? rightAnswers2 : rightAnswers2 - 1;

            BigInteger candidate2 = countValidPositions(1, ans1, ans2, nextRightAnswers1, nextRightAnswers2, calcRes2, questionsCnt);

            if (candidate1.compareTo(candidate2) == 1) {
                chisl = chisl.add(candidate1);
                output.answers.append("T");
                cache[key] = candidate1;
                cache2[key] = 'T';
            } else {
                chisl = chisl.add(candidate2);
                output.answers.append("F");
                cache[key] = candidate2;
                cache2[key] = 'F';
            }

            tmp = ans1[0];
            ans1[0] = ans1[i];
            ans1[i] = tmp;

            tmp = ans2[0];
            ans2[0] = ans2[i];
            ans2[i] = tmp;
        }

        if (allPossibleSolutions.compareTo(BigInteger.ZERO) != 0) {
            BigInteger gcd = chisl.gcd(allPossibleSolutions);
            output.chisl = chisl.divide(gcd);
            output.znam = allPossibleSolutions.divide(gcd);
        }
        return output;
    }

    private static Result calculate(int[] ans1, int rightAnswers1, int questionsCnt) {
        Result output = new Result();
        BigInteger[][] calcRes2 = new BigInteger[questionsCnt][rightAnswers1 + 1];
        BigInteger allPossibleSolutions = countValidPositions(0, ans1, rightAnswers1, calcRes2, questionsCnt);

        BigInteger chisl = BigInteger.ZERO;

        BigInteger[] cache = new BigInteger[2];
        char[] cache2 = new char[2];

        for (int i = 0; i < questionsCnt; i++) {
            int key = ans1[i];
            if (cache[key] != null) {
                chisl = chisl.add(cache[key]);
                output.answers.append(cache2[key]);
                continue;
            }

            int tmp = ans1[0];
            ans1[0] = ans1[i];
            ans1[i] = tmp;

            //Suppose answer is True
            int nextRightAnswers1 = ans1[0] == 0 ? rightAnswers1 : rightAnswers1 - 1;

            BigInteger candidate1 = countValidPositions(1, ans1, nextRightAnswers1, calcRes2, questionsCnt);

            //Suppose answer is False
            nextRightAnswers1 = ans1[0] == 1 ? rightAnswers1 : rightAnswers1 - 1;

            BigInteger candidate2 = countValidPositions(1, ans1, nextRightAnswers1, calcRes2, questionsCnt);

            if (candidate1.compareTo(candidate2) == 1) {
                chisl = chisl.add(candidate1);
                output.answers.append("T");
                cache[key] = candidate1;
                cache2[key] = 'T';
            } else {
                chisl = chisl.add(candidate2);
                output.answers.append("F");
                cache[key] = candidate2;
                cache2[key] = 'F';
            }

            tmp = ans1[0];
            ans1[0] = ans1[i];
            ans1[i] = tmp;
        }

        if (allPossibleSolutions.compareTo(BigInteger.ZERO) != 0) {
            BigInteger gcd = chisl.gcd(allPossibleSolutions);
            output.chisl = chisl.divide(gcd);
            output.znam = allPossibleSolutions.divide(gcd);
        }
        return output;
    }

    private static BigInteger countValidPositions(int pos, int[] ans1, int[] ans2, int[] ans3, int rightAnswers1,
                                                  int rightAnswers2, int rightAnswers3, BigInteger[][][][] calcRes, int questionsCnt) {
        if (pos > questionsCnt - 1) {
            return rightAnswers1 == 0 && rightAnswers2 == 0 && rightAnswers3 == 0 ? BigInteger.ONE : BigInteger.ZERO;
            //return BigInteger.ONE;
        }

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
                    && (ans2[pos] == 0 && rightAnswers2 == 0) || (ans2[pos] == 1 && rightAnswers2 == 1)
                    && (ans3[pos] == 0 && rightAnswers3 == 0) || (ans3[pos] == 1 && rightAnswers3 == 1)) {
                res = res.add(BigInteger.ONE);
            }
            //Suppose answer is False
            if (((ans1[pos] == 1 && rightAnswers1 == 0) || (ans1[pos] == 0 && rightAnswers1 == 1))
                    && ((ans2[pos] == 1 && rightAnswers2 == 0) || (ans2[pos] == 0 && rightAnswers2 == 1))
                    && ((ans3[pos] == 1 && rightAnswers3 == 0) || (ans3[pos] == 0 && rightAnswers3 == 1))) {
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

    private static BigInteger countValidPositions(int pos, int[] ans1, int[] ans2, int rightAnswers1,
                                                  int rightAnswers2, BigInteger[][][] calcRes, int questionsCnt) {
        if (pos > questionsCnt - 1) {
            return rightAnswers1 == 0 && rightAnswers2 == 0 ? BigInteger.ONE : BigInteger.ZERO;
        }

        if (rightAnswers1 > questionsCnt - pos || rightAnswers2 > questionsCnt - pos) {
            return BigInteger.ZERO;
        }

        if (rightAnswers1 < 0 || rightAnswers2 < 0) {
            return BigInteger.ZERO;
        }

        if (calcRes[pos][rightAnswers1][rightAnswers2] != null) {
            return calcRes[pos][rightAnswers1][rightAnswers2];
        }

        BigInteger res = BigInteger.ZERO;

        if (pos == questionsCnt - 1) {

            //Suppose answer is True
            if (((ans1[pos] == 0 && rightAnswers1 == 0) || (ans1[pos] == 1 && rightAnswers1 == 1))
                    && ((ans2[pos] == 0 && rightAnswers2 == 0) || (ans2[pos] == 1 && rightAnswers2 == 1))) {
                res = res.add(BigInteger.ONE);
            }
            //Suppose answer is False
            if (((ans1[pos] == 1 && rightAnswers1 == 0) || (ans1[pos] == 0 && rightAnswers1 == 1))
                    && ((ans2[pos] == 1 && rightAnswers2 == 0) || (ans2[pos] == 0 && rightAnswers2 == 1))) {
                res = res.add(BigInteger.ONE);
            }
        } else {
            //Suppose answer is True
            int nextRightAnswers1 = ans1[pos] == 0 ? rightAnswers1 : rightAnswers1 - 1;
            int nextRightAnswers2 = ans2[pos] == 0 ? rightAnswers2 : rightAnswers2 - 1;

            res = res.add(countValidPositions(pos + 1, ans1, ans2, nextRightAnswers1, nextRightAnswers2, calcRes, questionsCnt));

            //Suppose answer is False
            nextRightAnswers1 = ans1[pos] == 1 ? rightAnswers1 : rightAnswers1 - 1;
            nextRightAnswers2 = ans2[pos] == 1 ? rightAnswers2 : rightAnswers2 - 1;

            res = res.add(countValidPositions(pos + 1, ans1, ans2, nextRightAnswers1, nextRightAnswers2, calcRes, questionsCnt));
        }

        calcRes[pos][rightAnswers1][rightAnswers2] = res;
        return res;
    }

    private static BigInteger countValidPositions(int pos, int[] ans1, int rightAnswers1,
                                                  BigInteger[][] calcRes, int questionsCnt) {
        if (pos > questionsCnt - 1) {
            return rightAnswers1 == 0 ? BigInteger.ONE : BigInteger.ZERO;
            //return BigInteger.ONE;
        }

        if (rightAnswers1 > questionsCnt - pos) {
            return BigInteger.ZERO;
        }

        if (rightAnswers1 < 0) {
            return BigInteger.ZERO;
        }

        if (calcRes[pos][rightAnswers1] != null) {
            return calcRes[pos][rightAnswers1];
        }

        BigInteger res = BigInteger.ZERO;

        if (pos == questionsCnt - 1) {

            //Suppose answer is True
            if ((ans1[pos] == 0 && rightAnswers1 == 0) || (ans1[pos] == 1 && rightAnswers1 == 1)) {
                res = res.add(BigInteger.ONE);
            }
            //Suppose answer is False
            if ((ans1[pos] == 1 && rightAnswers1 == 0) || (ans1[pos] == 0 && rightAnswers1 == 1)) {
                res = res.add(BigInteger.ONE);
            }
        } else {
            //Suppose answer is True
            int nextRightAnswers1 = ans1[pos] == 0 ? rightAnswers1 : rightAnswers1 - 1;

            res = res.add(countValidPositions(pos + 1, ans1, nextRightAnswers1, calcRes, questionsCnt));

            //Suppose answer is False
            nextRightAnswers1 = ans1[pos] == 1 ? rightAnswers1 : rightAnswers1 - 1;

            res = res.add(countValidPositions(pos + 1, ans1, nextRightAnswers1, calcRes, questionsCnt));
        }

        calcRes[pos][rightAnswers1] = res;
        return res;
    }
}
