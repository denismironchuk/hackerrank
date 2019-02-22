package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CutTheTree {
    private static class Node {
        private int num;
        private int val;
        private List<Node> neighbours = new ArrayList<>();
        private int treeVal;

        public Node(int num, int val) {
            this.num = num;
            this.val = val;
        }

        public void addNeighbour(Node n) {
            neighbours.add(n);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        Node[] nodes = new Node[n];
        StringTokenizer valuesTkn = new StringTokenizer(br.readLine());

        for (int i = 0; i < n; i++) {
            int val = Integer.parseInt(valuesTkn.nextToken());
            nodes[i] = new Node(i, val);
        }

        for (int i = 0; i < n - 1; i++) {
            StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
            int n1 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int n2 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            nodes[n1].addNeighbour(nodes[n2]);
            nodes[n2].addNeighbour(nodes[n1]);
        }

        int totalVal = dfs(nodes[0], new int[n]);
        int res = Integer.MAX_VALUE;

        for (int i = 1; i < n; i++) {
            int newVal = Math.abs(totalVal - 2 * nodes[i].treeVal);
            if (newVal < res) {
                res= newVal;
            }
        }

        System.out.println(res);
    }

    private static int dfs(Node nd, int[] processed) {
        processed[nd.num] = 1;
        int subtreeVal = nd.val;

        for (Node neigh : nd.neighbours) {
            if (processed[neigh.num] == 0) {
                subtreeVal += dfs(neigh, processed);
            }
        }

        nd.treeVal = subtreeVal;

        return subtreeVal;
    }
}
