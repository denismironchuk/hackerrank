package kickstart.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class DependentEvents {

    private static final long MOD = 1000000000 + 7;
    private static final long MIL_INVERSE = fastPow(1000000, MOD - 2);

    private static long fastPow(long v, long p) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return fastPow((v * v) % MOD, p / 2);
        } else {
            return (v * fastPow(v, p - 1)) % MOD;
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                int q = Integer.parseInt(tkn1.nextToken());
                int[] parents = new int[n];
                //prob[i][0] - parent event happened
                //prob[i][1] - parent event didn't happen
                long[][] probs = new long[n][2];
                long k = Long.parseLong(br.readLine());
                probs[0][0] = (k * MIL_INVERSE) % MOD;
                for (int i = 1; i < n; i++) {
                    StringTokenizer tkn = new StringTokenizer(br.readLine());
                    int parent = Integer.parseInt(tkn.nextToken()) - 1;
                    parents[i] = parent;
                    probs[i][0] = (Long.parseLong(tkn.nextToken()) * MIL_INVERSE) % MOD;
                    probs[i][1] = (Long.parseLong(tkn.nextToken()) * MIL_INVERSE) % MOD;
                }

                StringBuilder results = new StringBuilder();
                for (int i = 0; i < q; i++) {
                    StringTokenizer tkn = new StringTokenizer(br.readLine());
                    int u = Integer.parseInt(tkn.nextToken()) - 1;
                    int v = Integer.parseInt(tkn.nextToken()) - 1;
                    Set<Integer> pathToRoot = new HashSet<>();
                    int currentNode = u;
                    while (currentNode != 0) {
                        pathToRoot.add(currentNode);
                        currentNode = parents[currentNode];
                    }
                    pathToRoot.add(currentNode);

                    int lca = v;
                    while (!pathToRoot.contains(lca)) {
                        lca = parents[lca];
                    }

                    if (lca == u || lca == v) {
                        int top = lca;
                        int bottom = lca == u ? v : u;

                        //matr[0][0] - parent occurred, current occurred (+ +)
                        //matr[0][1] - parent occurred, current not occurred (+ -)
                        //matr[1][0] - parent not occurred, current occurred (- +)
                        //matr[1][1] - parent not occurred, current not occurred (- -)
                        long[][] matr1 = new long[][] {{1, 0}, {0, 1}};
                        int currState = bottom;

                        while (parents[currState] != top) {
                            long[][] prevMatr = new long[][] {{probs[currState][0], (MOD + 1 - probs[currState][0]) % MOD}, {probs[currState][1], (MOD + 1 - probs[currState][1]) % MOD}};
                            matr1 = matrMul(prevMatr, matr1);
                            currState = parents[currState];
                        }

                        long[][] afterLca = new long[][] {{probs[currState][0], (MOD + 1 - probs[currState][0]) % MOD}};

                        currState = top;
                        long[][] matr2 = new long[][] {{1, 0}, {0, 1}};
                        while (currState != 0) {
                            long[][] prevMatr = new long[][] {{probs[currState][0], (MOD + 1 - probs[currState][0]) % MOD}, {probs[currState][1], (MOD + 1 - probs[currState][1]) % MOD}};
                            matr2 = matrMul(prevMatr, matr2);
                            currState = parents[currState];
                        }

                        long[][] res1 = matrMul(new long[][] {{probs[0][0], (MOD + 1 - probs[0][0]) % MOD}}, matr2);
                        long[][] res2 = matrMul(afterLca, matr1);

                        results.append((res1[0][0] * res2[0][0]) % MOD).append(" ");
                    } else {
                        long[][] matrU = new long[][] {{1, 0}, {0, 1}};
                        int currState = u;

                        while (parents[currState] != lca) {
                            long[][] prevMatr = new long[][] {{probs[currState][0], (MOD + 1 - probs[currState][0]) % MOD}, {probs[currState][1], (MOD + 1 - probs[currState][1]) % MOD}};
                            matrU = matrMul(prevMatr, matrU);
                            currState = parents[currState];
                        }

                        long[][] afterLca1U = new long[][] {{probs[currState][0], (MOD + 1 - probs[currState][0]) % MOD}};
                        long[][] afterLca0U = new long[][] {{probs[currState][1], (MOD + 1 - probs[currState][1]) % MOD}};

                        long[][] matrV = new long[][] {{1, 0}, {0, 1}};
                        currState = v;

                        while (parents[currState] != lca) {
                            long[][] prevMatr = new long[][] {{probs[currState][0], (MOD + 1 - probs[currState][0]) % MOD}, {probs[currState][1], (MOD + 1 - probs[currState][1]) % MOD}};
                            matrV = matrMul(prevMatr, matrV);
                            currState = parents[currState];
                        }

                        long[][] afterLca1V = new long[][] {{probs[currState][0], (MOD + 1 - probs[currState][0]) % MOD}};
                        long[][] afterLca0V = new long[][] {{probs[currState][1], (MOD + 1 - probs[currState][1]) % MOD}};

                        currState = lca;
                        long[][] commonMatr = new long[][] {{1, 0}, {0, 1}};
                        while (currState != 0) {
                            long[][] prevMatr = new long[][] {{probs[currState][0], (MOD + 1 - probs[currState][0]) % MOD}, {probs[currState][1], (MOD + 1 - probs[currState][1]) % MOD}};
                            commonMatr = matrMul(prevMatr, commonMatr);
                            currState = parents[currState];
                        }

                        long[][] res1 = matrMul(new long[][] {{probs[0][0], (MOD + 1 - probs[0][0]) % MOD}}, commonMatr);
                        long[][] res2 = matrMul(afterLca1U, matrU);
                        long[][] res3 = matrMul(afterLca1V, matrV);

                        long[][] res4 = matrMul(afterLca0U, matrU);
                        long[][] res5 = matrMul(afterLca0V, matrV);

                        long res = (((((res1[0][0] * res2[0][0]) % MOD) * res3[0][0]) % MOD) + ((((res1[0][1] * res4[0][0]) % MOD) * res5[0][0]) % MOD)) % MOD;

                        results.append(res).append(" ");
                    }
                }
                System.out.printf("Case #%s: %s\n", t, results);
            }
        }
    }

    private static long[][] matrMul(long[][] m1, long[][] m2) {
        long[][] res = new long[m1.length][m2[0].length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m2[0].length; j++) {
                for (int k = 0; k < m2.length; k++) {
                    res[i][j] += (m1[i][k] * m2[k][j]) % MOD;
                    res[i][j] %= MOD;
                }
            }
        }
        return res;
    }
}