import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StoryOfTree {
    static class Node {
        private int num;
        private List<Node> neighbours = new ArrayList<>();
        private int expectedParent;
        private int expectedChild;

        public Node(final int num) {
            this.num = num;
        }

        public void addNeighbour(Node neighbour) {
            neighbours.add(neighbour);
        }

        public int getNum() {
            return num;
        }

        public List<Node> getNeighbours() {
            return neighbours;
        }

        public int getExpectedParent() {
            return expectedParent;
        }

        public void setExpectedParent(final int expectedParent) {
            this.expectedParent = expectedParent;
        }

        public int getExpectedChild() {
            return expectedChild;
        }

        public void setExpectedChild(final int expectedChild) {
            this.expectedChild = expectedChild;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());

        for (int q = 0; q < Q; q++) {
            int n = Integer.parseInt(br.readLine());
            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node(i);
            }

            for (int i = 0; i < n - 1; i++) {
                StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
                int nd1 = Integer.parseInt(edgeTkn.nextToken()) - 1;
                int nd2 = Integer.parseInt(edgeTkn.nextToken()) - 1;

                nodes[nd1].addNeighbour(nodes[nd2]);
                nodes[nd2].addNeighbour(nodes[nd1]);
            }

            StringTokenizer condsTkn = new StringTokenizer(br.readLine());
            int g = Integer.parseInt(condsTkn.nextToken());
            int k = Integer.parseInt(condsTkn.nextToken());

            for (int i = 0; i < g; i++) {
                StringTokenizer pairTkn = new StringTokenizer(br.readLine());
                int u = Integer.parseInt(pairTkn.nextToken()) - 1;
                int v = Integer.parseInt(pairTkn.nextToken()) - 1;

                nodes[v].setExpectedParent(u);
                nodes[u].setExpectedChild(v);
            }

            Node start = null;

            for (int i = 0; i < n && start == null; i++) {
                if (nodes[i].getNeighbours().size() == 1) {
                    start = nodes[i];
                }
            }
        }
    }

    private static int countInclusiveClasses(Node nd, int[] processed) {
        int result = 0;
        processed[nd.getNum()] = 1;

        for (Node neighbour : nd.getNeighbours()) {
            if (processed[neighbour.getNum()] == 0) {
                result += countInclusiveClasses(neighbour, processed);
            }
        }

        return result;
    }
}
