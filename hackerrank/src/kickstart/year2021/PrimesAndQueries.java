package kickstart.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.StringTokenizer;

public class PrimesAndQueries {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                int q = Integer.parseInt(tkn1.nextToken());
                long p = Long.parseLong(tkn1.nextToken());

                long[] dividedByPrime = new long[n];
                long[] notDividedByPrime = new long[n];
                long[] notDividedByPrimeNonZeroVal = new long[n];
                long[] notDividedByPrimeSquare = new long[n];

                long[] dividedByPrimeTree = new long[4 * n];
                long[] notDividedByPrimeTree = new long[4 * n];
                long[] notDividedByPrimeNonZeroValTree = new long[4 * n];
                long[] notDividedByPrimeSquareTree = new long[4 * n];

                StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                for (int i = 0; i < n; i++) {
                    long val = Long.parseLong(tkn2.nextToken());
                    if (val % p == 0) {
                        dividedByPrime[i] = getPrimePow(val, p);
                    } else {
                        notDividedByPrime[i] = getPrimePow(val - val % p, p);
                        if (notDividedByPrime[i] != 0) {
                            notDividedByPrimeNonZeroVal[i] = 1;

                            BigInteger bigVal = BigInteger.valueOf(val);
                            notDividedByPrimeSquare[i] = bigVal.multiply(bigVal).subtract(BigInteger.ONE).getLowestSetBit();
                        }
                    }
                }

                buildTree(dividedByPrime, dividedByPrimeTree, 1, 0, n - 1);
                buildTree(notDividedByPrime, notDividedByPrimeTree, 1, 0, n - 1);
                buildTree(notDividedByPrimeNonZeroVal, notDividedByPrimeNonZeroValTree, 1, 0, n - 1);
                buildTree(notDividedByPrimeSquare, notDividedByPrimeSquareTree, 1, 0, n - 1);

                StringBuilder resBuilder = new StringBuilder();
                for (int i = 0; i < q; i++) {
                    StringTokenizer qTkn = new StringTokenizer(br.readLine());
                    int type = Integer.parseInt(qTkn.nextToken());
                    if (type == 1) {
                        int pos = Integer.parseInt(qTkn.nextToken()) - 1;
                        long val = Long.parseLong(qTkn.nextToken());
                        update(dividedByPrimeTree, 1, pos, 0, 0, n - 1);
                        update(notDividedByPrimeTree, 1, pos, 0, 0, n - 1);
                        update(notDividedByPrimeNonZeroValTree, 1, pos, 0, 0, n - 1);
                        update(notDividedByPrimeSquareTree, 1, pos, 0, 0, n - 1);

                        if (val % p == 0) {
                            long pow = getPrimePow(val, p);
                            update(dividedByPrimeTree, 1, pos, pow, 0, n - 1);
                        } else {
                            long pow = getPrimePow(val - val % p, p);
                            update(notDividedByPrimeTree, 1, pos, pow, 0, n - 1);
                            if (pow != 0) {
                                update(notDividedByPrimeNonZeroValTree, 1, pos, 1, 0, n - 1);

                                BigInteger bigVal = BigInteger.valueOf(val);
                                int sqrPow = bigVal.multiply(bigVal).subtract(BigInteger.ONE).getLowestSetBit();
                                update(notDividedByPrimeSquareTree, 1, pos, sqrPow, 0, n - 1);
                            }
                        }
                    } else {
                        long s = Long.parseLong(qTkn.nextToken());
                        int l = Integer.parseInt(qTkn.nextToken()) - 1;
                        int r = Integer.parseInt(qTkn.nextToken()) - 1;
                        if (p != 2 || s % 2 == 1) {
                            long res = s * getSum(dividedByPrimeTree, 1, l, r, 0, n - 1) +
                                    getSum(notDividedByPrimeTree, 1, l, r, 0, n - 1) +
                                    getSum(notDividedByPrimeNonZeroValTree, 1, l, r, 0, n - 1) * getPrimePow(s, p);
                            resBuilder.append(res).append(" ");
                        } else {
                            long res = s * getSum(dividedByPrimeTree, 1, l, r, 0, n - 1);

                            long pow = 0;
                            while (s % 4 == 0) {
                                pow++;
                                s /= 2;
                            }

                            res += getSum(notDividedByPrimeSquareTree, 1, l, r, 0, n - 1) +
                                    getSum(notDividedByPrimeNonZeroValTree, 1, l, r, 0, n - 1) * pow;

                            resBuilder.append(res).append(" ");
                        }
                    }
                }
                System.out.printf("Case #%s: %s\n", t, resBuilder);
            }
        }
    }

    private static long getPrimePow(long a, long p) {
        if (a < p) {
            return 0;
        }

        long pow = 0;
        while (a % p == 0) {
            pow++;
            a /= p;
        }
        return pow;
    }

    private static long getSum(long[] tree, int p, int intrStart, int intrEnd, int start, int end) {
        if (intrStart > intrEnd) {
            return 0;
        }

        if (intrStart == start && intrEnd == end) {
            return tree[p];
        }

        int middle = (start + end) / 2;

        return getSum(tree, 2 * p, intrStart, Math.min(middle, intrEnd), start, middle) +
                getSum(tree, 2 * p + 1, Math.max(middle + 1, intrStart), intrEnd, middle + 1, end);
    }

    private static void update(long[] tree, int p, int pos, long newVal, int start, int end) {
        if (start == end) {
            tree[p] = newVal;
            return;
        }

        int middle = (start + end) / 2;
        if  (pos <= middle) {
            update(tree, 2 * p, pos, newVal, start, middle);
        } else {
            update(tree, 2 * p + 1, pos, newVal, middle + 1, end);
        }

        tree[p] = tree[2 * p] + tree[2 * p + 1];
    }

    private static void buildTree(long[] val, long[] tree, int p, int start, int end) {
        if (start == end) {
            tree[p] = val[start];
            return;
        }

        int middle = (start + end) / 2;
        buildTree(val, tree, 2 * p, start, middle);
        buildTree(val, tree, 2 * p + 1, middle + 1, end);
        tree[p] = tree[2 * p] + tree[2 * p + 1];
    }
}
