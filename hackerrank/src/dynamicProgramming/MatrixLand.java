package dynamicProgramming;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MatrixLand {
    private static void countMaxSumsToLeft(int[] row, int[] maxSumsToLeft) {
        maxSumsToLeft[0] = row[0];
        for (int i = 1; i < row.length; i++) {
            maxSumsToLeft[i] = row[i] + Math.max(0, maxSumsToLeft[i - 1]);
        }
    }

    private static void countMaxSumsToRight(int[] row, int[] maxSumsToRight) {
        int len = row.length;
        maxSumsToRight[len - 1] = row[len - 1];
        for (int i = len - 2; i > -1; i--) {
            maxSumsToRight[i] = row[i] + Math.max(0, maxSumsToRight[i + 1]);
        }
    }

    private static void countMaxResToLeft(int[] row, int[] maxSum, int[] maxSumsToLeft, int[] maxResToLeft) {
        maxResToLeft[0] = row[0] + maxSum[0];

        for (int i = 1; i < row.length; i++) {
            maxResToLeft[i] = Math.max(maxResToLeft[i - 1] + row[i], maxSumsToLeft[i] + maxSum[i]);
        }
    }

    private static void countMaxResToRight(int[] row, int[] maxSum, int[] maxSumsToRight, int[] maxResToRight) {
        int len = row.length;
        maxResToRight[len - 1] = row[len - 1] + maxSum[len - 1];

        for (int i = len - 2; i > -1; i--) {
            maxResToRight[i] = Math.max(maxResToRight[i + 1] + row[i], maxSumsToRight[i] + maxSum[i]);
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
        //try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\dmiro\\Downloads\\input19.txt"))) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(tkn1.nextToken());
            int m = Integer.parseInt(tkn1.nextToken());

            int[][] matrix = new int[n][m];

            for (int i = n - 1; i > -1; i--) {
                StringTokenizer lineTkn = new StringTokenizer(br.readLine());
                for (int j = 0; j < m; j++) {
                    matrix[i][j] = Integer.parseInt(lineTkn.nextToken());
                }
            }

            if (m == 1) {
                int res = 0;
                for (int i = 0; i < n; i++) {
                    res += matrix[i][0];
                }
                System.out.println(res);
            } else {
                int[] maxSum = new int[m];
                int[] maxSumToRight = new int[m];
                int[] maxSumToLeft = new int[m];
                int[] maxResToRight = new int[m];
                int[] maxResToLeft = new int[m];

                countMaxSumsToRight(matrix[0], maxSumToRight);
                countMaxSumsToLeft(matrix[0], maxSumToLeft);

                for (int i = 0; i < m; i++) {
                    maxSum[i] = Math.max(Math.max(maxSumToRight[i], maxSumToLeft[i]), maxSumToRight[i] + maxSumToLeft[i] - matrix[0][i]);
                }

                for (int i = 1; i < n; i++) {
                    int[] row = matrix[i];

                    countMaxSumsToRight(row, maxSumToRight);
                    countMaxSumsToLeft(row, maxSumToLeft);

                    countMaxResToRight(row, maxSum, maxSumToRight, maxResToRight);
                    countMaxResToLeft(row, maxSum, maxSumToLeft, maxResToLeft);

                    for (int j = 0; j < m; j++) {
                        maxSum[j] = Math.max(
                                Math.max(maxResToLeft[j], maxResToRight[j]),
                                Math.max(maxResToRight[j] + maxSumToLeft[j] - row[j], maxResToLeft[j] + maxSumToRight[j] - row[j])
                        );
                    }
                }

                int max = Integer.MIN_VALUE;
                for (int i = 0; i < m; i++) {
                    if (maxSum[i] > max) {
                        max = maxSum[i];
                    }
                }
                System.out.println(max);
            }
        }
    }
}
