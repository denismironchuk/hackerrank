package findPath;

import java.util.Map;

/**
 * Created by Denis_Mironchuk on 1/2/2019.
 */
public class DistanceChecker {
    public static boolean checkDistsInOneColumn(int rows, int cols, int[][] rect, long[][][] dists) {
        long[][] floydWarshlDists = new long[rows * cols][rows * cols];
        Node[][] nodesTable = buildFloydWarshalDists(rows, cols, rect, floydWarshlDists);

        for (int col = 0; col < cols; col++) {
            for (int r1 = 0; r1 < rows; r1++) {
                for (int r2 = 0; r2 < rows; r2++) {
                    long dynDist = dists[col][r1][r2];
                    Node nd1 = nodesTable[r1][col];
                    Node nd2 = nodesTable[r2][col];
                    int fwDist = (int) floydWarshlDists[nd1.getIndex()][nd2.getIndex()];

                    if (dynDist != fwDist) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static boolean checkNeighbourColumnsDists(int rows, int cols, int[][] rect, long[][] dists, int col) {
        long[][] floydWarshlDists = new long[rows * cols][rows * cols];
        Node[][] nodesTable = buildFloydWarshalDists(rows, cols, rect, floydWarshlDists);

        for (int row1 = 0; row1 < rows; row1++) {
            for (int row2 = 0; row2 < rows; row2++) {
                long dynDist = dists[row1][row2];
                Node nd1 = nodesTable[row1][col];
                Node nd2 = nodesTable[row2][col];
                int fwDist = (int) floydWarshlDists[nd1.getIndex()][nd2.getIndex()];

                if (dynDist != fwDist) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean checkTwoColumnsDists(int rows, int cols, int[][] rect, long[][] dists, int col1, int col2) {
        long[][] floydWarshlDists = new long[rows * cols][rows * cols];
        Node[][] nodesTable = buildFloydWarshalDists(rows, cols, rect, floydWarshlDists);

        for (int row1 = 0; row1 < rows; row1++) {
            for (int row2 = 0; row2 < rows; row2++) {
                long dynDist = dists[row1][row2];
                Node nd1 = nodesTable[row1][col1];
                Node nd2 = nodesTable[row2][col2];
                int fwDist = (int) floydWarshlDists[nd1.getIndex()][nd2.getIndex()];

                if (dynDist != fwDist) {
                    return false;
                }
            }
        }

        return true;
    }

    public static Node[][] buildFloydWarshalDists(int rows, int cols, int[][] rect, long[][] floydWarshlDists) {
        Node[][] nodesTable = Converter.convertToGraph(rect, rows, cols);
        Node[] nodes = new Node[rows * cols];

        int nodesCnt = 0;
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                Node nd = nodesTable[row][col];
                nd.setIndex(nodesCnt);
                nodes[nodesCnt] = nd;
                nodesCnt++;
            }
        }

        for (int row = 0; row < nodesCnt; row++) {
            for (int col = 0; col < nodesCnt; col++) {
                if (row != col) {
                    floydWarshlDists[row][col] = Integer.MAX_VALUE;
                }
            }
        }

        for (int i = 0; i < nodesCnt; i++) {
            Node nd = nodes[i];
            for (Map.Entry<Node, Integer> entry : nd.getEdges().entrySet()) {
                Node neigh = entry.getKey();
                int dist = entry.getValue();
                floydWarshlDists[nd.getIndex()][neigh.getIndex()] = dist;
            }
        }

        for (int k = 0; k < nodesCnt; k++) {
            for (int i = 0; i < nodesCnt; i++) {
                for (int j = 0; j < nodesCnt; j++) {
                    floydWarshlDists[i][j] = Math.min(floydWarshlDists[i][j], floydWarshlDists[i][k] + floydWarshlDists[k][j]);
                }
            }
        }

        return nodesTable;
    }
}
