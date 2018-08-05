package cardsPermutations;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class CardsPermutationFinal {
    private final static long MOD = 1000000007;
    private final static long INV_TWO = inverseElmnt(2);

    private static long pow(long n, long p) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return pow((n * n) % MOD, p / 2) % MOD;
        } else {
            return (n * pow(n, p - 1)) % MOD;
        }
    }

    private static long inverseElmnt(long n) {
        return pow(n, MOD - 2);
    }

    public static void main(String[] args) throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new FileReader("D:\\cardPermsInpt2.txt"));
        int n = Integer.parseInt(br.readLine());
        int[] perm = new int[n];
        StringTokenizer permTkn = new StringTokenizer(br.readLine());

        for (int i = 0; i < n; i++) {
            perm[i] = Integer.parseInt(permTkn.nextToken());
        }

        Date start = new Date();
        int[] undefinedAmnt = new int[n];
        undefinedAmnt[n - 1] = 0;

        for (int i = n - 2; i >= 0; i--) {
            undefinedAmnt[i] = undefinedAmnt[i + 1] + (perm[i + 1] == 0 ? 1 : 0);
        }

        int totalUndef = undefinedAmnt[0] + (perm[0] == 0 ? 1 : 0);

        long[] bin = new long[n];
        bin[n - 1] = 1;
        long chisl = totalUndef;
        long znam = 1;

        long[] incr = new long[n];
        incr[n - 1] = 0;

        long currentIncr = perm[n - 1] == 0 ? 1 : 0;

        int chislForIncr = totalUndef - 1;
        int znamForIncr = 1;

        for (int i = n - 2; i >= 0; i--) {
            if (undefinedAmnt[i] == undefinedAmnt[i + 1]) {
                bin[i] = bin[i + 1];
            } else {
                bin[i] = (((bin[i + 1] * chisl) % MOD) * inverseElmnt(znam))% MOD;
                chisl--;
                znam++;
            }

            if (perm[i] != 0) {
                incr[i] = perm[i + 1] != 0 ? incr[i + 1] : currentIncr;
            } else {
                if (0 == currentIncr) {
                    currentIncr = 1;
                } else {
                    currentIncr = (((currentIncr * chislForIncr) % MOD) * inverseElmnt(znamForIncr)) % MOD;
                    chislForIncr--;
                    znamForIncr++;
                }
            }
        }

        long[] colSum = new long[n];
        long[] rowSum = new long[n];

        int cell = n - 1;
        while (cell >= 0 && perm[cell] != 0) {
            cell--;
        }

        if (cell >= 0) {
            colSum[cell] = 1;
            rowSum[cell] = totalUndef;
        }

        int chislColSum = totalUndef - 1;
        int znamColSum = 1;

        int chislRowSum = totalUndef - 1;
        int znamRowSum = 2;

        for (int i = cell - 1; i >= 0; i--) {
            if (perm[i] == 0) {
                colSum[i] = (((colSum[i + 1] * chislColSum) % MOD) * inverseElmnt(znamColSum)) % MOD;
                chislColSum--;
                znamColSum++;

                rowSum[i] = (((rowSum[i + 1] * chislRowSum) % MOD) * inverseElmnt(znamRowSum)) % MOD;
                chislRowSum--;
                znamRowSum++;
            } else {
                colSum[i] = colSum[i + 1];
                rowSum[i] = rowSum[i + 1];
            }
        }

        int[] defVals = new int[n - totalUndef];
        int defValsSize = 0;

        for (int i = 0; i < n; i++) {
            if (perm[i] != 0) {
                defVals[defValsSize] = perm[i];
                defValsSize++;
            }
        }

        int[] lessAmntLeft = new int[n + 1];
        int[] prevSmallerPos = new int[n - totalUndef];
        prevSmallerPos[n - totalUndef - 1] = -1;
        lessAmntLeft[defVals[n - totalUndef - 1]] = 0;
        for (int i = n - totalUndef - 2; i >= 0; i--) {
            int smalPos = i + 1;
            while (smalPos != -1 && defVals[i] < defVals[smalPos]) {
                smalPos = prevSmallerPos[smalPos];
            }
            prevSmallerPos[i] = smalPos;
            if (prevSmallerPos[i] == -1) {
                lessAmntLeft[defVals[i]] = 0;
            } else {
                lessAmntLeft[defVals[i]] = lessAmntLeft[defVals[prevSmallerPos[i]]] + 1;
            }
        }

        Arrays.sort(defVals);

        long[] greaterUndef = new long[n + 1];
        long[] smallerDefined = new long[n + 1];
        long totalSum = 0;

        for (int i = 0; i < defValsSize; i++) {
            int definedValue = defVals[i];
            int greaterCnt = n - definedValue - (defValsSize - i - 1);
            greaterUndef[definedValue] = greaterCnt;
            totalSum = (totalSum + greaterCnt) % MOD;

            smallerDefined[definedValue] = i;
        }

        long[] resultInpt = new long[n];

        for (int i = n - 1; i >= 0; i--) {
            if (perm[i] != 0) {
                 resultInpt[i] = (((incr[i] * (perm[i] - 1 - smallerDefined[perm[i]])) % MOD) +
                        (lessAmntLeft[perm[i]] * bin[i]) % MOD) % MOD;
            }
        }

        int undef = totalUndef;
        for (int i = 0; i < n; i++) {
            if (perm[i] == 0) {
                resultInpt[i] = (((((rowSum[i] * undef) % MOD) * (undef - 1)) % MOD) * INV_TWO) % MOD;
                resultInpt[i] = (resultInpt[i] + (colSum[i] * totalSum) % MOD) % MOD;
                undef--;
            } else {
                totalSum = (totalSum - greaterUndef[perm[i]] + MOD) % MOD;
            }
        }

        int undefRight = undefinedAmnt[0];
        int undefLeft = 0;

        long rightFact = fact(undefRight);
        long leftFact = 1;
        resultInpt[0] = (resultInpt[0] * rightFact) % MOD;

        for (int i = 1; i < n; i++) {
            if (perm[i] == 0) {
                rightFact = (rightFact * inverseElmnt(undefRight)) % MOD;
                undefRight--;
            }

            if (perm[i - 1] == 0) {
                undefLeft++;
                leftFact = (leftFact * undefLeft) % MOD;
            }

            resultInpt[i] = (resultInpt[i] * ((rightFact * leftFact) % MOD)) % MOD;
        }

        long fact = 1;
        int cnt = 1;

        for (int i = n - 2; i >= 0; i--) {
            resultInpt[i] = (resultInpt[i] * fact) % MOD;
            cnt++;
            fact = (fact * cnt) % MOD;
        }

        long result = fact(totalUndef);
        for (int i = 0; i < n; i++) {
            result = (result + resultInpt[i]) % MOD;
        }
        Date end = new Date();
        System.out.println(result);
        System.out.println(end.getTime() - start.getTime() + "ms");
    }

    private static long fact(int n) {
        long res = 1;
        for(int i = 1; i <= n; i++) {
           res = (res * i) % MOD;
        }
        return res;
    }

    public static int findFirstGreater(List<Integer> list, int val, int start, int end) {
        if (val > list.get(list.size() - 1)) {
            return list.size();
        }

        if (val < list.get(0)) {
            return 0;
        }

        return findFirstGreaterInner(list, val, start, end);
    }

    public static int findFirstGreaterInner(List<Integer> list, int val, int start, int end) {
        while (start != end) {
            int middle = (start + end) / 2;
            int middleElmnt = list.get(middle);
            if (val < middleElmnt) {
                end = middle;
            } else {
                start = middle + 1;
            }
        }

        return start;
    }

    public static int findFirstGreater(int[] list, int val, int start, int end) {
        if (val > list[end]) {
            return end + 1;
        }

        if (val < list[0]) {
            return 0;
        }

        return findFirstGreaterInner(list, val, start, end);
    }

    public static int findFirstGreaterInner(int[] list, int val, int start, int end) {
        while (start != end) {
            int middle = (start + end) / 2;
            int middleElmnt = list[middle];
            if (val < middleElmnt) {
                end = middle;
            } else {
                start = middle + 1;
            }
        }

        return start;
    }

    public static void insertIntoList(int[] list, int val, int posToInsert, int listSize) {
        System.arraycopy(list, posToInsert, list, posToInsert + 1,
                listSize - posToInsert);

        list[posToInsert] = val;
    }
}
