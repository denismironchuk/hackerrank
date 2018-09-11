import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Created by Denis_Mironchuk on 9/11/2018.
 */
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

    /*static class Edge {
        private int n1;
        private int n2;
        private int dist;

        public Edge(final int n1, final int n2, final int dist) {
            this.n1 = n1;
            this.n2 = n2;
            this.dist = dist;
        }
    }*/

    static class TreeElem implements Comparable<TreeElem> {
        private int outNum;
        private int inNum;
        private int dist;

        public TreeElem(final int outNum, final int inNum, final int dist) {
            this.outNum = outNum;
            this.inNum = inNum;
            this.dist = dist;
        }

        @Override
        public int compareTo(final TreeElem o) {
            int distsCompare = Integer.compare(dist, o.dist);
            if (distsCompare == 0) {
                return Integer.compare(inNum, o.inNum);
            } else {
                return distsCompare;
            }
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

        int[] processedNodes = new int[n];
        List<TreeElem> spanningTree = new ArrayList<>();
        TreeSet<TreeElem> tree = new TreeSet<>();

        Node start = nodes[0];
        processedNodes[start.num] = 1;

        for (Map.Entry<Integer, Integer> entry : start.dists.entrySet()) {
            int nodeNum = entry.getKey();
            int dist = entry.getValue();

            tree.add(new TreeElem(start.num, nodeNum, dist));
        }

        while (!tree.isEmpty()) {
            TreeElem first = tree.pollFirst();
            spanningTree.add(first);
            processedNodes[first.inNum] = 1;

            for (Map.Entry<Integer, Integer> entry : nodes[first.inNum].dists.entrySet()) {
                int nodeNum = entry.getKey();
                int dist = entry.getValue();
                if (processedNodes[nodeNum] == 0) {
                    tree.add(new TreeElem(first.inNum, nodeNum, dist));
                }
            }
        }

        System.out.println();
    }
}
