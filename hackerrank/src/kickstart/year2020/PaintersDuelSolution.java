package kickstart.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class PaintersDuelSolution {

    private static class Node {
        private int row;
        private int col;
        private List<Node> neighbours = new ArrayList<>();

        public Node(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public void addNeighbour(Node neighbour) {
            neighbours.add(neighbour);
        }

        public List<Node> getNeighbours() {
            return neighbours;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "row=" + row +
                    ", col=" + col +
                    '}';
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int ROWS = Integer.parseInt(tkn1.nextToken());
                int rowA = Integer.parseInt(tkn1.nextToken()) - 1;
                int colA = Integer.parseInt(tkn1.nextToken()) - 1;
                int rowB = Integer.parseInt(tkn1.nextToken()) - 1;
                int colB = Integer.parseInt(tkn1.nextToken()) - 1;
                int c = Integer.parseInt(tkn1.nextToken());

                Node[][] nodes = new Node[ROWS][2 * ROWS - 1];
                for (int row = 0; row < ROWS; row++) {
                    for (int col = 0; col < 2 * (row + 1) - 1; col++) {
                        nodes[row][col] = new Node(row + 1, col + 1);
                    }
                }

                for (int row = 0; row < ROWS; row++) {
                    for (int col = 0; col < 2 * (row + 1) - 1; col++) {
                        if (col - 1 > -1) {
                            nodes[row][col].addNeighbour(nodes[row][col - 1]);
                            nodes[row][col - 1].addNeighbour(nodes[row][col]);
                        }

                        if (row < ROWS - 1 && col % 2 == 0) {
                            nodes[row][col].addNeighbour(nodes[row + 1][col + 1]);
                            nodes[row + 1][col + 1].addNeighbour(nodes[row][col]);
                        }
                    }
                }

                Set<Node> underConstruction = new HashSet<>();
                for (int i = 0; i < c; i++) {
                    StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                    int rowUnderConstr = Integer.parseInt(tkn2.nextToken()) - 1;
                    int colUnderConstr = Integer.parseInt(tkn2.nextToken()) - 1;
                    underConstruction.add(nodes[rowUnderConstr][colUnderConstr]);
                }

                Node player1 = nodes[rowA][colA];
                Node player2 = nodes[rowB][colB];
                Set<Node> processed = new HashSet<>();
                processed.add(player1);
                processed.add(player2);
                processed.addAll(underConstruction);
                int score = countOptimalScore(player1, player2, true, processed, true, true);
                System.out.printf("Case #%s: %s\n", t, score);
            }
        }
    }

    private static int countOptimalScore(Node player1, Node player2, boolean player1Turn, Set<Node> processed, boolean player1HasMoves, boolean player2HasMoves) {
        if (!player1HasMoves && !player2HasMoves) {
            return 0;
        }

        if (player1Turn) {
            int res = Integer.MIN_VALUE;
            boolean hasMoves = false;
            for (Node neigh : player1.getNeighbours()) {
                if (processed.contains(neigh)) {
                    continue;
                }
                hasMoves = true;
                processed.add(neigh);
                res = Math.max(res, countOptimalScore(neigh, player2, false, processed, player1HasMoves, player2HasMoves) + 1);
                processed.remove(neigh);
            }
            if (!hasMoves) {
                return countOptimalScore(player1, player2, false, processed, false, player2HasMoves);
            } else {
                return res;
            }
        } else {
            int res = Integer.MAX_VALUE;
            boolean hasMoves = false;
            for (Node neigh : player2.getNeighbours()) {
                if (processed.contains(neigh)) {
                    continue;
                }
                hasMoves = true;
                processed.add(neigh);
                res = Math.min(res, countOptimalScore(player1, neigh, true, processed, player1HasMoves, player2HasMoves) - 1);
                processed.remove(neigh);
            }
            if (!hasMoves) {
                return countOptimalScore(player1, player2, true, processed, player1HasMoves, false);
            } else {
                return res;
            }
        }
    }
}
