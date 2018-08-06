package cardsPermutations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class CardsPermutationFinal {
    private final static long MOD = 1000000007;
    private final static long INV_TWO = inverseElmnt(2);
    private static final long Y_DISP = 10000000000l;
    private static final Set<Long> USED_Y = new HashSet<>();

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

    private static long fact(int n) {
        long res = 1;
        for(int i = 1; i <= n; i++) {
            res = (res * i) % MOD;
        }
        return res;
    }

    private static long generateY() {
        long y;
        do {
            y = (long)(Y_DISP * Math.random());
        } while (USED_Y.contains(y));
        USED_Y.add(y);
        return y;
    }

    private long run(int n, int[] perm) {
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

        int[] lessAmntLeft = new int[n + 1];

        cell = n - 1;
        while (cell >= 0 && perm[cell] == 0) {
            cell--;
        }

        Treap t = null;
        if (cell >= 0) {
            t = new Treap(perm[cell], generateY(), null, null);
        }

        for (int i = cell - 1; i >= 0; i--) {
            if (perm[i] != 0) {
                Treap res = new Treap(perm[i], generateY(), null, null);

                Treap[] splitRes = t.split(perm[i]);
                lessAmntLeft[perm[i]] = splitRes[0] == null ? 0 : splitRes[0].size;

                if (null != splitRes[0]) {
                    res = merge(splitRes[0], res);
                }

                if (null != splitRes[1]) {
                    res = merge(res, splitRes[1]);
                }
                t = res;
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

        return result;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader br = new BufferedReader(new FileReader("D:\\cards44.txt"));
        //BufferedReader br = new BufferedReader(new FileReader("D:\\cards41.txt"));
        int n = Integer.parseInt(br.readLine());
        int[] perm = new int[n];
        StringTokenizer permTkn = new StringTokenizer(br.readLine());

        for (int i = 0; i < n; i++) {
            perm[i] = Integer.parseInt(permTkn.nextToken());
        }

        //Date start = new Date();
        long res = new CardsPermutationFinal().run(n, perm);
        //Date end = new Date();
        //System.out.println(end.getTime() - start.getTime() + "ms");
        System.out.println(res);
    }

    private static void recalculateSize(Treap t) {
        if (null != t) {
            t.recalculateSize();
        }
    }

    public Treap merge(Treap l, Treap r) {
        if (null == l) {
            return r;
        }

        if (null == r) {
            return l;
        }

        Treap res;

        if (l.y > r.y) {
            Treap newTreap = merge(l.right, r);
            recalculateSize(newTreap);
            res = new Treap(l.x, l.y, l.left, newTreap);
        } else {
            Treap newTreap = merge(l, r.left);
            recalculateSize(newTreap);
            res = new Treap(r.x, r.y, newTreap, r.right);
        }

        recalculateSize(res);
        return res;
    }

    private class Treap {
        private int x;
        private long y;

        private Treap left;
        private Treap right;

        private int size;

        public Treap(final int x, final long y, final Treap left, final Treap right) {
            this.x = x;
            this.y = y;
            this.right = right;
            this.left = left;
        }

        private void recalculateSize() {
            size = (null == left ? 0 : left.size) + (null == right ? 0 : right.size) + 1;
        }


        public Treap[] split(int x) {
            Treap newLeft = null;
            Treap newRight = null;

            if (x < this.x) {

                if (this.left == null) {
                    newRight = new Treap(this.x, this.y, this.left, this.right);
                } else {
                    Treap[] splitResult = this.left.split(x);
                    newLeft = splitResult[0];
                    newRight = new Treap(this.x, this.y, splitResult[1], this.right);
                }
            } else {
                if (this.right == null) {
                    newLeft = new Treap(this.x, this.y, this.left, this.right);
                } else {
                    Treap[] splitResult = this.right.split(x);
                    newLeft = new Treap(this.x, this.y, this.left, splitResult[0]);
                    newRight = splitResult[1];
                }
            }

            CardsPermutationFinal.recalculateSize(newLeft);
            CardsPermutationFinal.recalculateSize(newRight);

            return new Treap[]{newLeft, newRight};
        }
    }
}

