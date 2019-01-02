package findPath;

import java.util.Date;
import java.util.Map;

public class FindPath {
        public static void main(String[] args) {
        while(true) {
            int rows = 4;
            int cols = 2;

            int[][] rect = new int[rows][cols];

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    rect[i][j] = (int) (10 * Math.random());
                }
            }

            int[][][] dists = new int[cols][rows][rows];

            for (int i = 0; i < rows; i++) {
                dists[0][i][i] = rect[i][0];
                for (int j = i + 1; j < rows; j++) {
                    dists[0][i][j] = dists[0][i][j - 1] + rect[j][0];
                    dists[0][j][i] = dists[0][i][j];
                }
            }

            Date start = new Date();

            for (int col = 1; col < cols; col++) {
                for (int rowStart = 0; rowStart < rows; rowStart++) {
                    dists[col][rowStart][rowStart] = rect[rowStart][col];

                    for (int rowEnd = rowStart + 1; rowEnd < rows; rowEnd++) {
                        dists[col][rowStart][rowEnd] = rect[rowEnd][col];

                        if (rowStart == 0) {
                            dists[col][rowStart][rowEnd] += Math.min(rect[rowStart][col] + dists[col - 1][rowStart][rowEnd], dists[col][rowStart][rowEnd - 1]);
                        } else {
                            dists[col][rowStart][rowEnd] += Math.min(Math.min(rect[rowStart][col] + dists[col - 1][rowStart][rowEnd], dists[col][rowStart][rowEnd - 1]),
                                    rect[rowStart][col] + dists[col][rowStart - 1][rowEnd]);
                        }
                    }

                    dists[col][rows - 1][rowStart] = dists[col][rowStart][rows - 1];

                    for (int rowEnd = rows - 2; rowEnd > rowStart; rowEnd--) {
                        dists[col][rowStart][rowEnd] = Math.min(dists[col][rowStart][rowEnd + 1] + rect[rowEnd][col], dists[col][rowStart][rowEnd]);
                        dists[col][rowEnd][rowStart] = dists[col][rowStart][rowEnd];
                    }
                }
            }

            Date end = new Date();

            //System.out.println((end.getTime() - start.getTime()) + "ms");

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

            long[][] floydWarshlDists = new long[nodesCnt][nodesCnt];

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

            //System.out.println("Floyd-Warshal calculation started");

            for (int k = 0; k < nodesCnt; k++) {
                for (int i = 0; i < nodesCnt; i++) {
                    for (int j = 0; j < nodesCnt; j++) {
                        floydWarshlDists[i][j] = Math.min(floydWarshlDists[i][j], floydWarshlDists[i][k] + floydWarshlDists[k][j]);
                    }
                }
            }

            //System.out.println("Floyd-Warshal calculation finished");

            for (int r1 = 0; r1 < rows; r1++) {
                for (int r2 = 0; r2 < rows; r2++) {
                    int dynDist = dists[cols - 1][r1][r2];
                    Node nd1 = nodesTable[r1][cols - 1];
                    Node nd2 = nodesTable[r2][cols - 1];
                    int fwDist = (int) floydWarshlDists[nd1.getIndex()][nd2.getIndex()] + nd1.getNodeWeight();

                    if (dynDist != fwDist) {
                        System.out.println(dynDist);
                        System.out.println(fwDist);

                        printRectangle(rows, cols, rect);
                        printDists(rows, cols, dists);
                        printFW(floydWarshlDists, nodes);

                        throw new RuntimeException();
                    }
                }
            }
        }
    }

    private static void printFW(long[][] wfDists, Node[] nodes) {
        System.out.println("Floyd-Warshal dists");

        for (int i = 0; i < wfDists.length; i++) {
            for (int j = 0; j < wfDists.length; j++) {
                System.out.printf("%3d ", wfDists[i][j] + nodes[i].getNodeWeight());
            }
            System.out.println();
        }
    }

    private static void printRectangle(int rows, int cols, int[][] rect) {
        System.out.println("Rectangle");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("%2d", rect[i][j]);
            }
            System.out.println();
        }
    }

    private static void printDists(int rows, int cols, int[][][] dists) {
        System.out.println("Dynamic dists");

        for (int col = 0; col < cols; col++) {
            System.out.println("\nColumn " + col);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < rows; j++) {
                    System.out.printf("%3d ", dists[col][i][j]);
                }
                System.out.println();
            }
        }
    }
}
