package codejam.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SquareFree {
    private static int rows = 4;
    private static int cols = 4;

    private static void fillCounts(int[] rowSlashes, int[] colSlashes) {
        int checkSum = 0;

        for (int row = 0; row < rows; row++) {
            int candidate;
            do {
                candidate = (int)((Math.min(rows * cols - checkSum, cols)) * Math.random());
            } while (checkSum + candidate > rows * cols);
            rowSlashes[row] = candidate;
            checkSum += candidate;
        }

        do {
            int checkSum2 = 0;

            for (int col = 0; col < cols - 1; col++) {
                int candidate;
                do {
                    candidate = (int)((Math.min(checkSum - checkSum2, rows)) * Math.random());
                } while (checkSum2 + candidate > checkSum);
                colSlashes[col] = candidate;
                checkSum2 += candidate;
            }

            colSlashes[cols - 1] = checkSum - checkSum2;
        } while (colSlashes[cols - 1] > rows);
    }

    private static void fillCountFromStream(BufferedReader br, int[] rowSlashes, int[] colSlashes) throws IOException {
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        for (int i = 0; i < rows; i++) {
            rowSlashes[i] = Integer.parseInt(tkn1.nextToken());
        }
        StringTokenizer tkn2 = new StringTokenizer(br.readLine());
        for (int i = 0; i < cols; i++) {
            colSlashes[i] = Integer.parseInt(tkn2.nextToken());
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            //int T = Integer.parseInt(br.readLine());
            int T = 1;
            for (int t = 1; t <= T; t++) {
                //StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                //rows = Integer.parseInt(tkn1.nextToken());
                //cols = Integer.parseInt(tkn1.nextToken());

                int[] rowSlashes = new int[rows];
                int[] colSlashes = new int[cols];

                //fillCountFromStream(br, rowSlashes, colSlashes);
                fillCounts(rowSlashes, colSlashes);

                System.out.println("Slashes in columns:");
                for (int i = 0; i < cols; i++) {
                    System.out.printf("%s ", colSlashes[i]);
                }
                System.out.println();

                System.out.println("Slashes in rows:");
                for (int i = 0; i < rows; i++) {
                    System.out.printf("%s ", rowSlashes[i]);
                }
                System.out.println();

                char[][] slashes = new char[rows][cols];

                int[] filledRows = new int[rows];
                int[] filledCols = new int[cols];

                int leftRows = rows;
                int leftCols = cols;

                boolean impossible = false;
                for (int col = 0; col < cols; col++) {
                    int[] left = fillDefined(slashes, rowSlashes, colSlashes, filledRows, filledCols, leftRows, leftCols);
                    leftRows = left[0];
                    leftCols = left[1];

                    if (leftRows == -1 || leftCols == -1) {
                        impossible = true;
                        break;
                    }

                    if (filledCols[col] == 0) {
                        int backSlashedToAdd = leftRows - colSlashes[col];
                        for (int row = 0; row < rows; row++) {
                            if (backSlashedToAdd != 0) {
                                if (slashes[row][col] == 0) {
                                    slashes[row][col] = '\\';
                                    backSlashedToAdd--;
                                }
                            } else {
                                if (slashes[row][col] == 0) {
                                    slashes[row][col] = '/';
                                    rowSlashes[row]--;
                                }
                            }
                        }
                        colSlashes[col] = 0;
                        filledCols[col] = 1;
                        leftCols--;
                    }
                }

                if (impossible) {
                    System.out.printf("Case #%s: IMPOSSIBLE\n", t);
                } else {
                    System.out.printf("Case #%s: POSSIBLE\n", t);
                    for (int row = 0; row < rows; row++) {
                        for (int col = 0; col < cols; col++) {
                            System.out.print(slashes[row][col]);
                        }
                        System.out.println();
                    }
                }
            }
        }
    }

    private static int[] fillDefined(char[][] slashes,  int[] rowSlashes, int[] colSlashes,
                                     int[] filledRows, int[] filledCols, int leftRows, int leftCols) {
        // index 0 - rows
        // index 1 - cols
        int[] res = new int[] {leftRows, leftCols};
        boolean filledInOneRound = true;
        while (filledInOneRound) {
            filledInOneRound = false;
            for (int row = 0; row < rows; row++) {
                if (rowSlashes[row] < 0 || rowSlashes[row] > res[1]) {
                    return new int[] {-1, -1};
                }

                if (filledRows[row] == 0 && rowSlashes[row] == res[1]) {
                    for (int col = 0; col < cols; col++) {
                        if (slashes[row][col] == 0) {
                            slashes[row][col] = '/';
                            colSlashes[col]--;
                        }
                    }

                    res[0]--;
                    filledRows[row] = 1;
                    rowSlashes[row] = 0;
                    filledInOneRound = true;
                } else if (filledRows[row] == 0 && rowSlashes[row] == 0) {
                    for (int col = 0; col < cols; col++) {
                        if (slashes[row][col] == 0) {
                            slashes[row][col] = '\\';
                        }
                    }
                    res[0]--;
                    filledRows[row] = 1;
                    filledInOneRound = true;
                }
            }

            for (int col = 0; col < cols; col++) {
                if (colSlashes[col] < 0 || colSlashes[col] > res[0]) {
                    return new int[] {-1, -1};
                }
                if (filledCols[col] == 0 && colSlashes[col] == res[0]) {
                    for (int row = 0; row < rows; row++) {
                        if (slashes[row][col] == 0) {
                            slashes[row][col] = '/';
                            rowSlashes[row]--;
                        }
                    }

                    res[1]--;
                    filledCols[col] = 1;
                    colSlashes[col] = 0;
                    filledInOneRound = true;
                } else if (filledCols[col] == 0 && colSlashes[col] == 0) {
                    for (int row = 0; row < rows; row++) {
                        if (slashes[row][col] == 0) {
                            slashes[row][col] = '\\';
                        }
                    }
                    res[1]--;
                    filledCols[col] = 1;
                    filledInOneRound = true;
                }
            }
        }

        return res;
    }
}
