package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class MatrixTreeSolution {
    private static final long MOD = 1000000007;

    private static class Node {
        private int num;
        private List<Node> neighbours = new ArrayList<>();
        private long weight;
        private Node parent;

        public Node(int num, long weight) {
            this.num = num;
            this.weight = weight;
        }

        public void setWeight(long weight) {
            this.weight = weight;
        }

        public void addNeighbour(Node nd) {
            neighbours.add(nd);
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int n = Integer.parseInt(br.readLine());
            Node[] nodes = new Node[n];
            StringTokenizer weightsTkn = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                long weight = Long.parseLong(weightsTkn.nextToken());
                nodes[i] = new Node(i, weight);
            }
            for (int i = 0; i < n - 1; i++) {
                StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
                int n1 = Integer.parseInt(edgeTkn.nextToken()) - 1;
                int n2 = Integer.parseInt(edgeTkn.nextToken()) - 1;

                nodes[n1].addNeighbour(nodes[n2]);
                nodes[n2].addNeighbour(nodes[n1]);
            }

            setParent(nodes[0]);

            long res = nodes[0].weight;

            for (int i = 1; i < n; i++) {
                res *= (nodes[i].weight - nodes[i].parent.weight + MOD) % MOD;
                res %= MOD;
            }

            System.out.println(res);
        }
    }

    private static void setParent(Node nd) {
        Queue<Node> q = new LinkedList<>();
        q.add(nd);

        while (!q.isEmpty()) {
            Node curr = q.poll();

            for (Node neigh : curr.neighbours) {
                if (neigh != curr.parent) {
                    neigh.parent = curr;
                    q.add(neigh);
                }
            }
        }
    }
}
