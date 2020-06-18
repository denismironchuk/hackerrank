package codejam.year2020.round2.emacs;

import codejam.year2020.round2.emacs.dataStructures.Parenthesis;
import codejam.year2020.round2.emacs.dataStructures.ParenthesisDistTime;

import java.util.Arrays;
import java.util.Date;

public class MinDistTester {
    public static void main(String[] args) {
        while (true) {
            Parenthesis root = Parenthesis.generateRandomParenthesis(100);
            Parenthesis[] treeArray = root.buildParenthesisArray();
            String pStr = root.toString();
            pStr = pStr.substring(1, pStr.length() - 1);
            System.out.println(pStr);

            long[][] dists = buildGraphMatrix(treeArray);
            Date start1 = new Date();
            countDists(dists);
            Date end1 = new Date();

            root.dfs(1);
            root.fillTreeSet();

            Date start2 = new Date();
            root.calculateTiming();
            Date end2 = new Date();

            System.out.println(end1.getTime() - start1.getTime() + "ms");
            System.out.println(end2.getTime() - start2.getTime() + "ms");

            for (Parenthesis par1 : treeArray) {
                for (Parenthesis par2 : treeArray) {
                    if (!par1.equals(par2)) {
                        ParenthesisDistTime time = par1.calculateMinTime(par2);

                        if (time.timeFromOpening.opening != dists[par1.openAbsPosition][par2.openAbsPosition]) {
                            throw new RuntimeException();
                        }
                        if (time.timeFromOpening.closing != dists[par1.openAbsPosition][par2.closeAbsPosition]) {
                            throw new RuntimeException();
                        }
                        if (time.timeFromClosing.opening != dists[par1.closeAbsPosition][par2.openAbsPosition]) {
                            throw new RuntimeException();
                        }
                        if (time.timeFromClosing.closing != dists[par1.closeAbsPosition][par2.closeAbsPosition]) {
                            throw new RuntimeException();
                        }
                    }
                }
            }
        }
    }

    private static long[][] buildGraphMatrix(Parenthesis[] treeArray) {
        long[][] matrix = new long[treeArray.length][treeArray.length];

        for (long[] line : matrix) {
            Arrays.fill(line, Long.MAX_VALUE / 2);
        }

        for (Parenthesis par : treeArray) {
            matrix[par.openAbsPosition][par.openAbsPosition] = 0;
            matrix[par.openAbsPosition][par.openAbsPosition + 1] = Math.min(matrix[par.openAbsPosition][par.openAbsPosition + 1], par.openTime.toRight);
            if (par.openAbsPosition > 0) {
                matrix[par.openAbsPosition][par.openAbsPosition - 1] = Math.min(par.openTime.toLeft, matrix[par.openAbsPosition][par.openAbsPosition - 1]);
            }
            matrix[par.openAbsPosition][par.closeAbsPosition] = Math.min(par.openTime.teleport, matrix[par.openAbsPosition][par.closeAbsPosition]);

            matrix[par.closeAbsPosition][par.closeAbsPosition] = 0;
            if (par.closeAbsPosition < treeArray.length - 1) {
                matrix[par.closeAbsPosition][par.closeAbsPosition + 1] = Math.min(par.closeTime.toRight, matrix[par.closeAbsPosition][par.closeAbsPosition + 1]);
            }
            matrix[par.closeAbsPosition][par.closeAbsPosition - 1] = Math.min(par.closeTime.toLeft, matrix[par.closeAbsPosition][par.closeAbsPosition - 1]);
            matrix[par.closeAbsPosition][par.openAbsPosition] = Math.min(par.closeTime.teleport, matrix[par.closeAbsPosition][par.openAbsPosition]);
        }

        return matrix;
    }

    private static void countDists(long[][] dists) {
        for (int d = 0; d < dists.length; d++) {
            for (int i = 0; i < dists.length; i++) {
                for (int j = 0; j < dists.length; j++) {
                    long candidate = dists[i][d] + dists[d][j];
                    if (candidate < dists[i][j]) {
                        dists[i][j] = candidate;
                    }
                }
            }
        }
    }
}
