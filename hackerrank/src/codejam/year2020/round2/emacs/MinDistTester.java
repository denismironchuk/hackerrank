package codejam.year2020.round2.emacs;

import codejam.year2020.round2.emacs.dataStructures.Parenthesis;
import codejam.year2020.round2.emacs.dataStructures.Time;

import java.util.Arrays;
import java.util.Date;

public class MinDistTester {
    public static void main(String[] args) {
        while (true) {
            Parenthesis root = Parenthesis.generateRandomParenthesis(1000);
            Parenthesis[] treeArray = root.buildParenthesisArray();
            String pStr = root.toString();
            pStr = pStr.substring(1, pStr.length() - 1);
            System.out.println(pStr);

            long[][] dists = buildGraphMatrix(treeArray);
            Date start1 = new Date();
            countDists(dists);
            Date end1 = new Date();

            Date start2 = new Date();
            root.calculateTiming();
            Date end2 = new Date();

            System.out.println(end1.getTime() - start1.getTime() + "ms");
            System.out.println(end2.getTime() - start2.getTime() + "ms");

            for (Parenthesis par : treeArray) {
                if (par.fromOpenToCloseTiming != dists[par.openAbsPosition][par.closeAbsPosition]) {
                    throw new RuntimeException();
                }

                if (par.fromCloseToOpenTiming != dists[par.closeAbsPosition][par.openAbsPosition]) {
                    throw new RuntimeException();
                }

                for (int srcIndex = 0; srcIndex < par.children.size(); srcIndex++) {
                    for (int destIndex = 0; destIndex < par.children.size(); destIndex++) {
                        if (srcIndex == destIndex) {
                            continue;
                        }

                        Time timeFromOpening = par.countTimeFromInnerOpening(srcIndex, destIndex);
                        Time timeFromClosing = par.countTimeFromInnerClosing(srcIndex, destIndex);

                        Parenthesis src = par.children.get(srcIndex);
                        Parenthesis dest = par.children.get(destIndex);

                        if (timeFromOpening.opening != dists[src.openAbsPosition][dest.openAbsPosition]) {
                            throw new RuntimeException();
                        }

                        if (timeFromOpening.closing != dists[src.openAbsPosition][dest.closeAbsPosition]) {
                            throw new RuntimeException();
                        }

                        if (timeFromClosing.opening != dists[src.closeAbsPosition][dest.openAbsPosition]) {
                            throw new RuntimeException();
                        }

                        if (timeFromClosing.closing != dists[src.closeAbsPosition][dest.closeAbsPosition]) {
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
