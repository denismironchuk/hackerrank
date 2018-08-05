import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class GridWalking {
    private static final long MOD = 1000000007;

    private static long[][] binomials(int n) {
        long[][] binomials = new long[n + 1][n + 1];
        binomials[0][0] = 1;

        for (int i = 1; i <= n; i++) {
            binomials[i][0] = 1;
            for (int j = 1; j <= i; j++) {
                binomials[i][j] = (binomials[i - 1][j] + binomials[i-1][j - 1]) % MOD;
            }
        }

        return binomials;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            String line1 = br.readLine();
            StringTokenizer tkn1 = new StringTokenizer(line1, " ");
            int n = Integer.parseInt(tkn1.nextToken());
            int m = Integer.parseInt(tkn1.nextToken());
            int[] x = new int[n];
            StringTokenizer tkn2 = new StringTokenizer(br.readLine(), " ");
            for (int i = 0; i < n; i++) {
                x[i] = Integer.parseInt(tkn2.nextToken());
            }
            StringTokenizer tkn3 = new StringTokenizer(br.readLine(), " ");
            int[] d = new int[n];
            for (int i = 0; i < n; i++) {
                d[i] = Integer.parseInt(tkn3.nextToken());
            }

            long[][] oneDimensionMoves = new long[n][m + 1];
            for (int i = 0; i < n; i++) {
                long[] moves = new long[d[i]];
                moves[x[i] - 1] = 1;
                oneDimensionMoves[i][0] = 1;
                for (int j = 1; j <= m; j++) {
                    long[] newMoves = new long[d[i]];
                    for (int k = 0; k < d[i]; k++) {
                        if (k > 0) {
                            newMoves[k] = (newMoves[k] + moves[k - 1]) % MOD;
                        }

                        if (k < d[i] - 1) {
                            newMoves[k] = (newMoves[k] + moves[k + 1]) % MOD;
                        }

                        oneDimensionMoves[i][j] = (oneDimensionMoves[i][j] + newMoves[k]) % MOD;
                    }
                    moves = newMoves;
                }
            }

            long[][] binomials = binomials(m);

            long[][] result = new long[n][m + 1];
            for (int i = 0; i <= m; i++) {
                result[0][i] = oneDimensionMoves[0][i];
            }

            for (int dim = 1; dim < n; dim++) {
                for (int moves = 0; moves <= m; moves++) {
                    for (int k = 0; k <= moves; k++) {
                        result[dim][moves] = (result[dim][moves] +
                                (((result[dim - 1][k] * oneDimensionMoves[dim][moves - k]) % MOD) * binomials[moves][k]) % MOD) % MOD;
                    }
                }
            }
            System.out.println(result[n - 1][m]);
        }
    }
}
