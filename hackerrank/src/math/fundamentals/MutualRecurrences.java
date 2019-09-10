package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MutualRecurrences {
    private static final long MOD = 1000000000;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());

            int a = Integer.parseInt(tkn.nextToken());
            int b = Integer.parseInt(tkn.nextToken());
            int c = Integer.parseInt(tkn.nextToken());

            int d = Integer.parseInt(tkn.nextToken());

            int e = Integer.parseInt(tkn.nextToken());
            int f = Integer.parseInt(tkn.nextToken());
            int g = Integer.parseInt(tkn.nextToken());

            int h = Integer.parseInt(tkn.nextToken());

            long n = Long.parseLong(tkn.nextToken());

            long[][] oper = new long[][] {
                    {0, 1, 0, 0, 0, 0, 0, 0, 0,   0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0,   0, 0},
                    {0, 0, 1, 0, 0, 0, 0, 0, 0,   0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0,   0, 0},
                    {0, 0, 0, 1, 0, 0, 0, 0, 0,   0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0,   0, 0},
                    {0, 0, 0, 0, 1, 0, 0, 0, 0,   0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0,   0, 0},
                    {0, 0, 0, 0, 0, 1, 0, 0, 0,   0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0,   0, 0},
                    {0, 0, 0, 0, 0, 0, 1, 0, 0,   0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0,   0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 1, 0,   0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0,   0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 1,   0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0,   0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0,   0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 1, 0, 0, 0, 0, 0, 0, 0,   0, 0,   0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0, 1, 0, 0, 0, 0, 0, 0,   0, 0,   0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0, 0, 1, 0, 0, 0, 0, 0,   0, 0,   0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0, 0, 0, 1, 0, 0, 0, 0,   0, 0,   0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0, 0, 0, 0, 1, 0, 0, 0,   0, 0,   0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0, 0, 0, 0, 0, 1, 0, 0,   0, 0,   0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0, 0, 0, 0, 0, 0, 1, 0,   0, 0,   0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0, 0, 0, 0, 0, 0, 0, 1,   0, 0,   0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0,   0, 0},
                    {d, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0, 0, 0, 0, 0, 0, 0, 0,   d, 0,   0, 0},
                    {d, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0, 0, 0, 0, 0, 0, 0, 0,   d, d,   0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0,   h, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0,   h, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0,   h, 0, 0, 0, 0, 0, 0, 0, 0,   0, 0,   h, h}};

            long[][] res = new long[][] {
                  //n = 0
                   //0 -1 -2 -3 -4 -5 -6 -7 -8    0 -1 -2 -3 -4 -5 -6 -7 -8   0*d^0 d^0  0*h^0 h^0
                    {3, 1, 1, 1, 1, 1, 1, 1, 1,   3, 1, 1, 1, 1, 1, 1, 1, 1,      0,  1,     0,  1}};

            oper[a - 1][0]++;
            oper[9 + b - 1][0]++;
            oper[9 + c - 1][0]++;

            oper[9 + e - 1][9]++;
            oper[f - 1][9]++;
            oper[g - 1][9]++;

            res = matrixMul(res, fastPow(oper, n));

            System.out.println(res[0][0] + " " + res[0][9]);
        }
    }

    private static void printMatrix(long[][] a) {
        for (int row = 0; row < a.length; row++) {
            for (int col = 0; col < a[0].length; col++) {
                System.out.printf("%2d", a[row][col]);
            }
            System.out.println();
        }
    }

    private static long[][] fastPow(long[][] a, long p) {
        if (p == 1) {
            return a;
        }

        if (p % 2 == 0) {
            return fastPow(matrixMul(a, a), p / 2);
        } else {
            return matrixMul(a, fastPow(a, p - 1));
        }
    }

    private static long[][] matrixMul(long[][] a, long[][] b) {
        long[][] res = new long[a.length][b[0].length];

        for (int rowA = 0; rowA < a.length; rowA++) {
            for (int colA = 0; colA < a[0].length; colA++) {
                for (int colB = 0; colB < b[0].length; colB++) {
                    res[rowA][colB] += (a[rowA][colA] * b[colA][colB]) % MOD;
                    res[rowA][colB] %= MOD;
                }
            }
        }

        return res;
    }
}
