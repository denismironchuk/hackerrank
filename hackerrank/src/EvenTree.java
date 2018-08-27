import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Влада on 26.08.2018.
 */
public class EvenTree {
    static class Node {
        private int num;
        private Node parent;
        private List<Node> children = new ArrayList<>();
        private List<Node> neighbours = new ArrayList<>();
        private int subtreeSize = 0;

        public Node(int num) {
            this.num = num;
        }

        public void addChild(Node child) {
            children.add(child);
        }

        public void addNeighbour(Node child) {
            neighbours.add(child);
        }

        public int getNum() {
            return num;
        }

        public void setNum(final int num) {
            this.num = num;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(final Node parent) {
            this.parent = parent;
        }

        public List<Node> getChildren() {
            return children;
        }

        public List<Node> getNeighbours() {
            return neighbours;
        }

        public int getSubtreeSize() {
            return subtreeSize;
        }

        public void setSubtreeSize(final int subtreeSize) {
            this.subtreeSize = subtreeSize;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        int edges = Integer.parseInt(tkn1.nextToken());

        Node[] nodes = new Node[n];

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }

        for (int i = 0; i < edges; i++) {
            StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
            int node1 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int node2 = Integer.parseInt(edgeTkn.nextToken()) - 1;

            nodes[node1].addNeighbour(nodes[node2]);
            nodes[node2].addNeighbour(nodes[node1]);
        }

        int[] processed = new int[n];
        deepSearch(nodes[0], processed);
        setSubtreeSize(nodes[0]);

        System.out.println(countEdges(nodes[0]));
    }

    private static void deepSearch(Node nd, int[] processed) {
        processed[nd.getNum()] = 1;

        for (Node neighbour : nd.getNeighbours()) {
            if (processed[neighbour.getNum()] == 0) {
                neighbour.setParent(nd);
                nd.addChild(neighbour);
                deepSearch(neighbour, processed);
            }
        }
    }

    private static void setSubtreeSize(Node nd) {
        int subtreeSize = 0;
        for (Node child : nd.getChildren()) {
            setSubtreeSize(child);
            subtreeSize += child.getSubtreeSize() + 1;
        }
        nd.setSubtreeSize(subtreeSize);
    }

    private static int countEdges(Node nd) {
        int result = 0;

        for (Node child : nd.getChildren()) {
            if ((child.getSubtreeSize() + 1) % 2 == 0) {
                result++;
            }
            result += countEdges(child);
        }

        return result;
    }
}