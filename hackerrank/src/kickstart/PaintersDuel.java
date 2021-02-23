package kickstart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PaintersDuel {
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
            return "{" + row + ", " + col + "}";
        }
    }

    public static void main(String[] args) {
        int ROWS = 6;

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

        long start = System.currentTimeMillis();
        Node startNode = nodes[ROWS / 2][ROWS];
        Set<Node> path = new HashSet<>();
        path.add(startNode);
        System.out.println(maxPathLen(startNode, path));
        long end = System.currentTimeMillis();

        System.out.println((end - start) + "ms");
        System.out.println(PATHS);
    }

    private static int PATHS = 0;

    private static int maxPathLen(Node start, Set<Node> path) {
        int maxLen = 0;
        boolean isEndPoint = true;
        for (Node neigh : start.getNeighbours()) {
            if (!path.contains(neigh)) {
                isEndPoint = false;
                path.add(neigh);
                maxLen = Math.max(maxLen, maxPathLen(neigh, path));
                path.remove(neigh);
            }
        }
        if (isEndPoint) {
            PATHS++;
        }
        return maxLen + 1;
    }
}
