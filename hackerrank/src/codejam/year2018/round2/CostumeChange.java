package codejam.year2018.round2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class CostumeChange {
    private static int[][] grid;
    private static int[][] processed;
    private static Map<Integer, List<Integer>>[] rowColorColumns;
    private static Map<Integer, List<Integer>>[] colColorRows;

    private static class Node {
        private int num;
        private Set<Node> neighbours = new HashSet<>();

        public Node(int num) {
            this.num = num;
        }

        public void addNeighbour(Node node) {
            neighbours.add(node);
        }

        public int getNum() {
            return num;
        }

        public Set<Node> getNeighbours() {
            return neighbours;
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int n = Integer.parseInt(br.readLine());
                grid = new int[n][n];
                for (int i = 0; i < n; i++) {
                    StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                    for (int j = 0; j < n; j++) {
                        grid[i][j] = Integer.parseInt(tkn1.nextToken());
                    }
                }

                processed = new int[n][n];
                rowColorColumns = new HashMap[n];
                colColorRows = new HashMap[n];
                for (int i = 0; i < n; i++) {
                    rowColorColumns[i] = new HashMap<>();
                    colColorRows[i] = new HashMap<>();
                }

                for (int row = 0; row < n; row++) {
                    for (int col = 0; col < n; col++) {
                        if (!rowColorColumns[row].containsKey(grid[row][col])) {
                            rowColorColumns[row].put(grid[row][col], new ArrayList<>());
                        }
                        rowColorColumns[row].get(grid[row][col]).add(col);

                        if (!colColorRows[col].containsKey(grid[row][col])) {
                            colColorRows[col].put(grid[row][col], new ArrayList<>());
                        }
                        colColorRows[col].get(grid[row][col]).add(row);
                    }
                }

                int res = 0;
                for (int row = 0; row < n; row++) {
                    for (int col = 0; col < n; col++) {
                        if (processed[row][col] == 0 && (rowColorColumns[row].get(grid[row][col]).size() > 1 || colColorRows[col].get(grid[row][col]).size() > 1)) {
                            Node[] rowNodes = new Node[n];
                            Node[] colNodes = new Node[n];
                            buildGraph(row, col, rowNodes, colNodes);
                            Node[] nodes = new Node[n * 2];
                            for (Node nd : rowNodes) {
                                if (nd == null) {
                                    continue;
                                }
                                nodes[nd.num] = nd;
                                res += nd.getNeighbours().size();
                            }
                            for (Node nd : colNodes) {
                                if (nd == null) {
                                    continue;
                                }
                                nd.num += n;
                                nodes[nd.num] = nd;
                            }

                            int[] pair = new int[2 * n];
                            Arrays.fill(pair, -1);

                            for (Node nd : nodes) {
                                if (nd != null && pair[nd.getNum()] == -1) {
                                    increasePath(nd, new int[2 * n], pair, nodes);
                                }
                            }

                            for (int i = 0; i < n; i++) {
                                if (pair[i] != -1) {
                                    res--;
                                }
                            }
                        }
                    }
                }
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }

    private static boolean increasePath(Node nd, int[] processed, int[] pair, Node[] nodes) {
        int ndNum = nd.getNum();
        processed[ndNum] = 1;

        for (Node neigh : nd.getNeighbours()) {
            int neighNum = neigh.getNum();

            if (processed[neighNum] == 1) {
                continue;
            }

            processed[neighNum] = 1;

            if (pair[neighNum] == -1 || increasePath(nodes[pair[neighNum]], processed, pair, nodes)) {
                pair[neighNum] = ndNum;
                pair[ndNum] = neighNum;
                return true;
            }
        }

        return false;
    }

    private static void buildGraph(int row, int col, Node[] rowNodes, Node[] colNodes) {
        processed[row][col] = 1;
        if (rowNodes[row] == null) {
            rowNodes[row] = new Node(row);
        }
        if (colNodes[col] == null) {
            colNodes[col] = new Node(col);
        }
        rowNodes[row].addNeighbour(colNodes[col]);
        colNodes[col].addNeighbour(rowNodes[row]);

        for (int nextCol : rowColorColumns[row].get(grid[row][col])) {
            if (nextCol == col) {
                continue;
            }

            if (processed[row][nextCol] == 1) {
                continue;
            }

            buildGraph(row, nextCol, rowNodes, colNodes);
        }

        for (int nextRow : colColorRows[col].get(grid[row][col])) {
            if (nextRow == row) {
                continue;
            }

            if (processed[nextRow][col] == 1) {
                continue;
            }

            buildGraph(nextRow, col, rowNodes, colNodes);
        }
    }
}
