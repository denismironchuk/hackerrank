import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class RoadsInHackerland {
    static class Node {
        private int num;
        private Map<Integer, Integer> dists = new HashMap<>();

        public Node(final int num) {
            this.num = num;
        }

        public void addNeighbour(Node neigh, int dist) {
            dists.merge(neigh.num, dist, (oldVal, newVal) -> Math.min(oldVal, newVal));
        }
    }

    static class Edge {
        private int n1;
        private int n2;
        private int dist;

        public Edge(final int n1, final int n2, final int dist) {
            this.n1 = n1;
            this.n2 = n2;
            this.dist = dist;
        }
    }

    static class TreeElem implements Comparable<TreeElem> {
        private int num;
        private int dist;

        public TreeElem(final int num, final int dist) {
            this.num = num;
            this.dist = dist;
        }

        @Override
        public int compareTo(final TreeElem o) {
            return Integer.compare(dist, o.dist);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        int m = Integer.parseInt(tkn1.nextToken());

        Node[] nodes = new Node[n];

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }

        for (int i = 0; i < m; i++) {
            StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
            int n1 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int n2 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int dist = Integer.parseInt(edgeTkn.nextToken());

            nodes[n1].addNeighbour(nodes[n2], dist);
            nodes[n2].addNeighbour(nodes[n1], dist);
        }

        List<Edge> spanningTree = createSpanningTree(nodes, n);

        Node[] spanningNodes = new Node[n];

        for (Edge edge : spanningTree) {
            if (spanningNodes[edge.n1] == null) {
                spanningNodes[edge.n1] = new Node(edge.n1);
            }
            if (spanningNodes[edge.n2] == null) {
                spanningNodes[edge.n2] = new Node(edge.n2);
            }

            spanningNodes[edge.n1].addNeighbour(spanningNodes[edge.n2], edge.dist);
            spanningNodes[edge.n2].addNeighbour(spanningNodes[edge.n1], edge.dist);
        }


    }

    private static List<Edge> createSpanningTree(Node[] nodes, int n) {
        int[] processedNodes = new int[n];
        List<Edge> spanningTree = new ArrayList<>();
        TreeSet<TreeElem> tree = new TreeSet<>();

        Node start = nodes[0];
        processedNodes[start.num] = 1;

        int[] dists = new int[n];
        Arrays.fill(dists, Integer.MAX_VALUE);

        int[] parentNode = new int[n];
        Arrays.fill(parentNode, -1);

        for (Map.Entry<Integer, Integer> entry : start.dists.entrySet()) {
            int nodeNum = entry.getKey();
            int dist = entry.getValue();

            tree.add(new TreeElem(nodeNum, dist));
            dists[nodeNum] = dist;
            parentNode[nodeNum] = start.num;
        }

        while (!tree.isEmpty()) {
            TreeElem first = tree.pollFirst();
            spanningTree.add(new Edge(parentNode[first.num], first.num, first.dist));
            processedNodes[first.num] = 1;

            for (Map.Entry<Integer, Integer> entry : nodes[first.num].dists.entrySet()) {
                int nodeNum = entry.getKey();
                int dist = entry.getValue();

                if (processedNodes[nodeNum] == 0) {
                    int oldDist = dists[nodeNum];
                    if (oldDist > dist) {
                        tree.remove(new TreeElem(nodeNum, oldDist));
                        tree.add(new TreeElem(nodeNum, dist));
                        dists[nodeNum] = dist;
                        parentNode[nodeNum] = first.num;
                    }
                }
            }
        }

        return spanningTree;
    }
}
