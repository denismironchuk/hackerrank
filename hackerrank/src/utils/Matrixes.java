package utils;

import java.util.Date;

/**
 * Created by Denis_Mironchuk on 8/17/2018.
 */
public class Matrixes {
    private final static int N = 20;

    public static void main(String[] args) {
        double[][] matrix = new double[N][N * 2];
        double[][] originalMatrix = new double[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = Math.random();
                originalMatrix[i][j] = matrix[i][j];
            }
        }

        for (int i = 0; i < N; i++) {
            matrix[i][N + i] = 1;
        }

        Date start = new Date();
        for (int i = 0; i < N; i++) {
            int maxRowIndex = i;
            for (int j = i + 1; j < N; j++) {
                if (Math.abs(matrix[j][i]) > Math.abs(matrix[maxRowIndex][i])) {
                    maxRowIndex = j;
                }
            }

            if (maxRowIndex != i) {
                double[] temp = matrix[i];
                matrix[i] = matrix[maxRowIndex];
                matrix[maxRowIndex] = temp;
            }

            divideRow(matrix[i], matrix[i][i]);

            for (int j = i + 1; j < N; j++) {
                mulAndSubstrRow(matrix[j], matrix[i], matrix[j][i]);
            }
        }

        for (int i = N - 2; i >= 0; i--) {
            for (int j = N - 1; j > i; j--) {
                mulAndSubstrRow(matrix[i], matrix[j], matrix[i][j]);
            }
        }

        Date end = new Date();

        System.out.println(end.getTime() - start.getTime());

        printMatrix(originalMatrix);
        System.out.println("=============");
        printInverseMatrix(matrix);
        System.out.println("=============");
        printMatrix(matrixProd(originalMatrix, getInverseMatrix(matrix)));
    }

    private static double[][] matrixProd(double[][] matrix1, double[][] matrix2) {
        double[][] result = new double[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < N; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

        return result;
    }

    private static void printMatrix(double[][] matrix) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.printf("%15.12f ", matrix[i][j]);
            }
            System.out.println();
        }
    }

    private static void printInverseMatrix(double[][] matrix) {
        for (int i = 0; i < N; i++) {
            for (int j = N; j < 2 * N; j++) {
                System.out.printf("%15.12f ", matrix[i][j]);
            }
            System.out.println();
        }
    }

    private static double[][] getInverseMatrix(double[][] matrix) {
        double[][] result = new double[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                result[i][j] = matrix[i][j + N];
            }
        }

        return result;
    }

    private static double[][] getMatrix(double[][] matrix) {
        double[][] result = new double[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                result[i][j] = matrix[i][j];
            }
        }

        return result;
    }

    private static void mulRow(double[] row, double mul) {
        for (int i = 0; i < row.length; i++) {
            row[i] *= mul;
        }
    }

    private static void divideRow(double[] row, double mul) {
        for (int i = 0; i < row.length; i++) {
            row[i] /= mul;
        }
    }

    private static void addRow(double[] row, double[] rowToAdd) {
        for (int i = 0; i < row.length; i++) {
            row[i] += rowToAdd[i];
        }
    }

    private static void substractRow(double[] row, double[] rowToSubstr) {
        for (int i = 0; i < row.length; i++) {
            row[i] -= rowToSubstr[i];
        }
    }

    private static void mulAndSubstrRow(double[] row, double[] rowToSubstr, double mul) {
        for (int i = 0; i < row.length; i++) {
            row[i] -= rowToSubstr[i] * mul;
        }
    }
}
