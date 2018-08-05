package cardsPermutations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class CardsPermutationTrivial {
    private static long factorial(long n) {
        if (n == 0) {
            return 1;
        }

        return n * factorial(n - 1);
    }

    public static long bin(long m, long n) {
        return factorial(n) / (factorial(m) * factorial(n - m));
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[] perm = new int[n];
        StringTokenizer permTkn = new StringTokenizer(br.readLine());

        for (int i = 0; i < n; i++) {
            perm[i] = Integer.parseInt(permTkn.nextToken());
        }

        int[] greaterAmnt = new int[n + 1];
        int[] smallerAmnt = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < n; j++) {
                if (perm[j] != 0 && i != perm[j]) {
                    if (perm[j] < i) {
                        smallerAmnt[i]++;
                    } else {
                        greaterAmnt[i]++;
                    }
                }
            }
        }

        int[] definedValues = new int[n + 1];

        for (int i = 0; i < n; i++) {
            if (perm[i] != 0) {
                definedValues[perm[i]] = 1;
            }
        }

        int[] undefinedAmnt = new int[n];
        undefinedAmnt[n - 1] = 0;

        for (int i = n-2; i >= 0; i--) {
            undefinedAmnt[i] = undefinedAmnt[i + 1] + (perm[i + 1] == 0 ? 1 : 0);
        }

        int[][] lessValues = new int[n][n + 1];
        int[][] greatValues = new int[n][n + 1];

        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= n; j++) {
                for (int k = i + 1; k < n; k++) {
                    if (perm[k] != 0) {
                        if (perm[k] < j) {
                            lessValues[i][j]++;
                        } else if (perm[k] > j) {
                            greatValues[i][j]++;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            long[][] table = new long[n - i][n + 1];

            for (int col = 1; col <= n; col++) {
                if ((perm[i] == col || definedValues[col] == 0)) {
                    int n1 = col - 1 - ((perm[i] != 0 && perm[i] < col) ? smallerAmnt[col] - 1 : smallerAmnt[col]);
                    int n2 = n - col - ((perm[i] != 0 && perm[i] > col) ? greaterAmnt[col] - 1 : greaterAmnt[col]);

                    if (n1 >= 0 && n2 >= 0) {
                        for (int row = 0; row <= n - i - 1; row++) {
                            int m1 = row - lessValues[i][col];
                            int m2 = undefinedAmnt[i] - m1;

                            if (m1 >= 0 && m2 >= 0 && m1 <= n1 && m2 <= n2){
                                table[row][col] = bin(m1, n1) * bin(m2, n2);
                            }
                        }
                    }
                }
            }

            for (int row = table.length - 1; row >= 0; row--) {
                for (int col = 1; col < table[0].length; col++) {
                    System.out.printf("%5d", table[row][col]);
                }
                System.out.println();
            }

            System.out.println("-----------------");
            for (int col = 1; col < table[0].length; col++) {
                long res = 0;

                for (int row = table.length - 1; row >= 0; row--) {
                    res += table[row][col] * row;
                }

                System.out.printf("%5d", res);
            }

            System.out.println("\n====================");
        }

        System.out.println();
    }

    public static long getValue(int[] perm, int n, int cell) {
        int[] greaterAmnt = new int[n + 1];
        int[] smallerAmnt = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < n; j++) {
                if (perm[j] != 0 && i != perm[j]) {
                    if (perm[j] < i) {
                        smallerAmnt[i]++;
                    } else {
                        greaterAmnt[i]++;
                    }
                }
            }
        }

        int[] definedValues = new int[n + 1];

        for (int i = 0; i < n; i++) {
            if (perm[i] != 0) {
                definedValues[perm[i]] = 1;
            }
        }

        int[] undefinedAmnt = new int[n];
        undefinedAmnt[n - 1] = 0;

        for (int i = n-2; i >= 0; i--) {
            undefinedAmnt[i] = undefinedAmnt[i + 1] + (perm[i + 1] == 0 ? 1 : 0);
        }

        int[][] lessValues = new int[n][n + 1];
        int[][] greatValues = new int[n][n + 1];

        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= n; j++) {
                for (int k = i + 1; k < n; k++) {
                    if (perm[k] != 0) {
                        if (perm[k] < j) {
                            lessValues[i][j]++;
                        } else if (perm[k] > j) {
                            greatValues[i][j]++;
                        }
                    }
                }
            }
        }

        long[][] table = new long[n - cell][n + 1];

        for (int col = 1; col <= n; col++) {
            if ((perm[cell] == col || definedValues[col] == 0)) {
                int n1 = col - 1 - ((perm[cell] != 0 && perm[cell] < col) ? smallerAmnt[col] - 1 : smallerAmnt[col]);
                int n2 = n - col - ((perm[cell] != 0 && perm[cell] > col) ? greaterAmnt[col] - 1 : greaterAmnt[col]);

                if (n1 >= 0 && n2 >= 0) {
                    for (int row = 0; row <= n - cell - 1; row++) {
                        int m1 = row - lessValues[cell][col];
                        int m2 = undefinedAmnt[cell] - m1;

                        if (m1 >= 0 && m2 >= 0 && m1 <= n1 && m2 <= n2){
                            table[row][col] = bin(m1, n1) * bin(m2, n2);
                        }
                    }
                }
            }
        }

        long res = 0;

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < n - cell; j++) {
                res += table[j][i] * j;
            }
        }

        return res;
    }

    public static long getValue(int[] perm, int n, int cell, int value) {
        int[] greaterAmnt = new int[n + 1];
        int[] smallerAmnt = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < n; j++) {
                if (perm[j] != 0 && i != perm[j]) {
                    if (perm[j] < i) {
                        smallerAmnt[i]++;
                    } else {
                        greaterAmnt[i]++;
                    }
                }
            }
        }

        int[] definedValues = new int[n + 1];

        for (int i = 0; i < n; i++) {
            if (perm[i] != 0) {
                definedValues[perm[i]] = 1;
            }
        }

        int[] undefinedAmnt = new int[n];
        undefinedAmnt[n - 1] = 0;

        for (int i = n-2; i >= 0; i--) {
            undefinedAmnt[i] = undefinedAmnt[i + 1] + (perm[i + 1] == 0 ? 1 : 0);
        }

        int[][] lessValues = new int[n][n + 1];
        int[][] greatValues = new int[n][n + 1];

        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= n; j++) {
                for (int k = i + 1; k < n; k++) {
                    if (perm[k] != 0) {
                        if (perm[k] < j) {
                            lessValues[i][j]++;
                        } else if (perm[k] > j) {
                            greatValues[i][j]++;
                        }
                    }
                }
            }
        }

        long[] resArr = new long[n - cell];

        if ((perm[cell] == value || definedValues[value] == 0)) {
            int n1 = value - 1 - ((perm[cell] != 0 && perm[cell] < value) ? smallerAmnt[value] - 1 : smallerAmnt[value]);
            int n2 = n - value - ((perm[cell] != 0 && perm[cell] > value) ? greaterAmnt[value] - 1 : greaterAmnt[value]);

            if (n1 >= 0 && n2 >= 0) {
                for (int row = 0; row <= n - cell - 1; row++) {
                    int m1 = row - lessValues[cell][value];
                    int m2 = undefinedAmnt[cell] - m1;

                    if (m1 >= 0 && m2 >= 0 && m1 <= n1 && m2 <= n2){
                        resArr[row] = bin(m1, n1) * bin(m2, n2);
                    }
                }
            }
        }

        long res = 0;

        for (int i = 0; i < n - cell; i++) {
            res += resArr[i] * i;
        }

        return res;
    }
}
