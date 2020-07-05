package codejam.year2020.round2.emacs;

import codejam.year2020.round2.emacs.dataStructures.NodesPair;
import codejam.year2020.round2.emacs.dataStructures.Parenthesis;
import codejam.year2020.round2.emacs.dataStructures.ParenthesisDistTime;
import codejam.year2020.round2.emacs.dataStructures.PathDecompose;
import utils.disjointSet.DisjointSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MinDistTester {
    private static final long MAX_PAIRS = 100000l;
    private static final double PROBABILITY = 0.04;
    private static final boolean VALIDATE = false;
    public static final int PAIRS_CNT = 50000;

    public static void main(String[] args) throws IOException {
        System.in.read();
        Parenthesis[] treeArray = new Parenthesis[110000];
        for (int t = 0; t < 100; t++) {
        //while (true) {
            Parenthesis root = Parenthesis.generateRandomParenthesis(PAIRS_CNT);
            int treeArrLen = root.buildParenthesisArray(treeArray);

            StringBuilder builder = new StringBuilder();
            root.buildString(builder);
            String pStr = builder.toString();
            pStr = pStr.substring(1, pStr.length() - 1);
            System.out.println(pStr);

            long[][] dists = null;
            if (VALIDATE) {
                dists = buildGraphMatrix(treeArray, treeArrLen);
                Date start1 = new Date();
                countDists(dists);
                Date end1 = new Date();
                System.out.println(end1.getTime() - start1.getTime() + "ms");
            }

            root.dfs(1);
            root.fillTreeSet();

            Date start2 = new Date();
            root.calculateTiming();
            Date end2 = new Date();

            /********Heavy-light decomposition block*********/
            List<Parenthesis> noHeavyChildNodes = new ArrayList();
            root.markHeavyEdges(noHeavyChildNodes);
            List<PathDecompose> paths = Parenthesis.buildHeavyLightDecomposition(noHeavyChildNodes);
            System.out.println("Max path len = " + paths.stream().map(p -> p.nodes.size()).max(Integer::compareTo).orElse(-1));
            /***********************************************/

            System.out.println(end2.getTime() - start2.getTime() + "ms");

            List<NodesPair> pairs = new ArrayList<>();
            for (int i = 0; pairs.size() <= MAX_PAIRS && i < treeArrLen; i++) {
                Parenthesis par1 = treeArray[i];
                if (par1.closeAbsPosition == i) {
                    continue;
                }

                for (int j = 0; pairs.size() <= MAX_PAIRS && j < treeArrLen; j++) {
                    if (i == j) {
                        continue;
                    }

                    Parenthesis par2 = treeArray[j];
                    if (par2 == null) {
                        System.out.println();
                    }
                    if (par2.closeAbsPosition == j) {
                        continue;
                    }

                    if (Math.random() <= PROBABILITY) {
                        pairs.add(new NodesPair(par1, par2));
                    }
                }
            }

            System.out.println("Pairs amount = " + pairs.size());
            Map<Parenthesis, Set<NodesPair>> pairsGrouped = new HashMap<>();

            for (NodesPair pair : pairs) {
                Set<NodesPair> list1 = pairsGrouped.get(pair.par1);
                if (list1 == null) {
                    list1 = new HashSet<>();
                }
                list1.add(pair);
                pairsGrouped.put(pair.par1, list1);

                Set<NodesPair> list2 = pairsGrouped.get(pair.par2);
                if (list2 == null) {
                    list2 = new HashSet<>();
                }
                list2.add(pair);
                pairsGrouped.put(pair.par2, list2);
            }

            Date start3 = new Date();
            root.lca(new int[treeArrLen], pairsGrouped, new DisjointSet(treeArrLen), new int[treeArrLen], treeArray);
            Date end3 = new Date();

            System.out.println("LCA calculation took " + (end3.getTime() - start3.getTime()) + "ms");

            Date start4 = new Date();
            for (NodesPair pair : pairs) {
                Parenthesis par1 = pair.par1;
                Parenthesis par2 = pair.par2;
                Parenthesis lca = pair.lca;

                ParenthesisDistTime time = par1.calculateMinTime(par2, lca);
                if (VALIDATE) {
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
            Date end4 = new Date();
            System.out.println("Dist calculation = " + (end4.getTime() - start4.getTime()) + "ms");

            //System.in.read();
        }
    }

    private static long[][] buildGraphMatrix(Parenthesis[] treeArray, int treeArrLen) {
        long[][] matrix = new long[treeArrLen][treeArrLen];

        for (long[] line : matrix) {
            Arrays.fill(line, Long.MAX_VALUE / 2);
        }

        for (int i = 0; i < treeArrLen; i++) {
        //for (Parenthesis par : treeArray) {
            Parenthesis par = treeArray[i];
            matrix[par.openAbsPosition][par.openAbsPosition] = 0;
            matrix[par.openAbsPosition][par.openAbsPosition + 1] = Math.min(matrix[par.openAbsPosition][par.openAbsPosition + 1], par.openTime.toRight);
            if (par.openAbsPosition > 0) {
                matrix[par.openAbsPosition][par.openAbsPosition - 1] = Math.min(par.openTime.toLeft, matrix[par.openAbsPosition][par.openAbsPosition - 1]);
            }
            matrix[par.openAbsPosition][par.closeAbsPosition] = Math.min(par.openTime.teleport, matrix[par.openAbsPosition][par.closeAbsPosition]);

            matrix[par.closeAbsPosition][par.closeAbsPosition] = 0;
            if (par.closeAbsPosition < treeArrLen - 1) {
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
