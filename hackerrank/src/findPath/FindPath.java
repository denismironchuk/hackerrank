package findPath;

import java.util.Date;
import java.util.Map;

public class FindPath {
        public static void main(String[] args) {
        while(true) {
            int rows = 5;
            int cols = 100;

            int[][] rect = new int[rows][cols];

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    rect[i][j] = (int) (10 * Math.random());
                }
            }

            long[][][] dists = new long[cols][rows * 2][rows * 2];

            for (int i = 0; i < rows; i++) {
                dists[0][i][i] = 0;
                for (int j = i + 1; j < rows; j++) {
                    dists[0][i][j] = Integer.MAX_VALUE;
                    dists[0][j][i] = Integer.MAX_VALUE;
                }
            }

            for (int i = 0; i < rows - 1; i++) {
                dists[0][i][i + 1] = rect[i + 1][0];
                dists[0][i + 1][i] = rect[i][0];
            }

            Date start = new Date();

            floydWarshal(dists[0], rows);

            for (int col = 1; col < cols; col++) {
                for (int i = 0; i < rows; i++) {
                    dists[col][i][i] = 0;
                    for (int j = i + 1; j < rows; j++) {
                        dists[col][i][j] = Integer.MAX_VALUE;
                        dists[col][j][i] = Integer.MAX_VALUE;
                    }
                }

                for (int i = 0; i < rows - 1; i++) {
                    dists[col][i][i + 1] = rect[i + 1][col];
                    dists[col][i + 1][i] = rect[i][col];
                }

                for (int i = rows; i < rows * 2; i++) {
                    for (int j = rows; j < rows * 2; j++) {
                        dists[col][i][j] = dists[col - 1][i - rows][j - rows];
                    }
                }

                for (int i = 0; i < rows; i++) {
                    for (int j = rows; j < rows * 2; j++) {
                        dists[col][i][j] = Integer.MAX_VALUE;
                        dists[col][j][i] = Integer.MAX_VALUE;
                    }
                }

                for (int i = 0; i < rows; i++) {
                    dists[col][i][i + rows] = rect[i][col - 1];
                    dists[col][i + rows][i] = rect[i][col];
                }

                floydWarshal(dists[col], rows * 2);
            }

            Date end = new Date();

            System.out.println((end.getTime() - start.getTime()) + "ms");

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

            System.out.println("Floyd-Warshal calculation started");

            for (int k = 0; k < nodesCnt; k++) {
                for (int i = 0; i < nodesCnt; i++) {
                    for (int j = 0; j < nodesCnt; j++) {
                        floydWarshlDists[i][j] = Math.min(floydWarshlDists[i][j], floydWarshlDists[i][k] + floydWarshlDists[k][j]);
                    }
                }
            }

            System.out.println("Floyd-Warshal calculation finished");

            for (int r1 = 0; r1 < rows; r1++) {
                for (int r2 = 0; r2 < rows; r2++) {
                    long dynDist = dists[cols - 1][r1][r2];
                    Node nd1 = nodesTable[r1][cols - 1];
                    Node nd2 = nodesTable[r2][cols - 1];
                    long fwDist = floydWarshlDists[nd1.getIndex()][nd2.getIndex()];

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

    private static void floydWarshal(long[][] dists, int n) {
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    dists[i][j] = Math.min(dists[i][k] + dists[k][j], dists[i][j]);
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

    private static void printDists(int rows, int cols, long[][][] dists) {
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
