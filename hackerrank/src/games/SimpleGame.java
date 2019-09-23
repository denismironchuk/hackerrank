package games;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SimpleGame {
    private static final int XOR_LIMIT = 1024;
    private static final long MOD = 1000000007;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(tkn.nextToken());
        int M = Integer.parseInt(tkn.nextToken());
        int K = Integer.parseInt(tkn.nextToken());

        Date start = new Date();
        int[] grund = generateGrundies(K, N);

        long[][][] dyn = new long[M + 1][N + 1][XOR_LIMIT];

        for (int n = 1; n <= N; n++) {
            dyn[1][n][grund[n]]++;
        }

        for (int m = 2; m <= M; m++) {
            for (int n = m; n <= N; n++) {
                for (int xor = 0; xor < XOR_LIMIT; xor++) {
                    for (int n2 = m - 1; n2 < n; n2++) {
                        dyn[m][n][xor] += dyn[m - 1][n2][xor ^ grund[n - n2]];
                        dyn[m][n][xor] %= MOD;
                    }
                }
            }
        }

        long res = 0;
        for (int xor = 1; xor < XOR_LIMIT; xor++) {
            res += dyn[M][N][xor];
            res %= MOD;
        }

        System.out.println(res);
        Date end = new Date();

        System.out.println(end.getTime() - start .getTime() + "ms");
    }

    private static int[] generateGrundies(int k, int n) {
        int[] grund = new int[n + 1];
        grund[1] = 0;
        if (k == 2) {
            for (int i = 2; i <= n; i++) {
                grund[i] = (i - 1) % 2;
            }
        } else if (k ==3) {
            for (int i = 2; i <= n; i++) {
                List<List<Integer>> sums = new ArrayList<>();
                for (int j = 2; j <= k; j++) {
                    makeAsSum(i, j, new LinkedList<>(), 1, sums);
                }
                TreeSet<Integer> nextMoves = new TreeSet<>();
                for (List<Integer> sum : sums) {
                    int xor = 0;
                    for (Integer s : sum) {
                        xor ^= grund[s];
                    }
                    nextMoves.add(xor);
                }
                grund[i] = mex(nextMoves);
            }
        } else {
            for (int i = 2; i <= n; i++) {
                grund[i] = i - 1;
            }
        }

        return grund;
    }

    private static int mex(TreeSet<Integer> set) {
        int res = 0;

        for (int i : set) {
            if (i == res) {
                res++;
            }
        }

        return res;
    }

    private static void makeAsSum(int n, int k, LinkedList<Integer> sum, int last, List<List<Integer>> res) {
        if (n <= 0) {
            return;
        }

        if (k == 1) {
            sum.add(n);
            res.add(new ArrayList<>(sum));
            sum.removeLast();
            return;
        }

        for (int i = last; i <= n / 2; i++) {
            sum.add(i);
            makeAsSum(n - i, k - 1, sum, i, res);
            sum.removeLast();
        }
    }
}
