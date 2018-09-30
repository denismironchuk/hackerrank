package utils.graphs;

import utils.disjointSet.DisjointSet;

import java.util.*;
import java.util.stream.Collectors;

public class LCA {
    static class Node {
        private int num;
        private List<Node> neighbours = new ArrayList<>();

        public Node(final int num) {
            this.num = num;
        }

        public void addNeighbour(Node neigh) {
            neighbours.add(neigh);
        }

        public int getNum() {
            return num;
        }

        public List<Node> getNeighbours() {
            return neighbours;
        }

        @Override
        public String toString() {
            return "{" + toString(null) + "}";
        }

        public String toString(Node parent) {
            return String.format("\"%s\":{%s}", num, getNeighbours().stream().filter(nd -> nd != parent).
                    map(nd -> nd.toString(this)).collect(Collectors.joining(",")));
        }
    }

    public static void main(String[] args) {
        int n = 20;
        Node[] nodes = new Node[n];
        List<Integer> notConnected = new ArrayList<>();
        List<Integer> connected = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
            notConnected.add(i);
        }

        connected.add(notConnected.get(0));
        notConnected.remove(0);

        while (!notConnected.isEmpty()) {
            int conIndex = (int) (Math.random() * (connected.size() - 1));
            int conNodeNum = connected.get(conIndex);
            Node conNode = nodes[conNodeNum];

            int notConIndex = (int) (Math.random() * (notConnected.size() - 1));
            int notConNodeNum = notConnected.get(notConIndex);
            Node notConNode = nodes[notConNodeNum];

            connected.add(notConNodeNum);
            notConnected.remove(notConIndex);

            conNode.addNeighbour(notConNode);
            notConNode.addNeighbour(conNode);
        }

        System.out.println(nodes[0]);

        int pairsCt = (n * (n - 1)) / 10;

        Map<Integer, Set<Integer>> pairs = new HashMap<>();

        for (int i = 0; i < pairsCt; i++) {
            int v1 = (int)((n - 1) * Math.random());

            if (pairs.get(v1) == null) {
                pairs.put(v1, new HashSet<>());
            }

            int v2 = v1;

            while (v1 == v2 || pairs.get(v1).contains(v2)) {
                v2 = (int)((n - 1) * Math.random());
            }

            if (pairs.get(v2) == null) {
                pairs.put(v2, new HashSet<>());
            }

            pairs.get(v1).add(v2);
            pairs.get(v2).add(v1);
        }

        lca(nodes[0], new int[n], new int[n], pairs, new DisjointSet(n), new int[n]);

        System.out.println();
    }

    private static void lca(Node nd, int[] processed, int[] black, Map<Integer, Set<Integer>> pairs, DisjointSet dSet, int[] ancestors) {
        int nodeNum = nd.getNum();
        processed[nodeNum] = 1;
        dSet.makeSet(nodeNum);
        ancestors[dSet.find(nodeNum)] = nodeNum;

        for (Node child : nd.getNeighbours()) {
            int childNum = child.getNum();
            if (processed[childNum] == 0) {
                lca(child, processed, black, pairs, dSet, ancestors);
                dSet.unite(dSet.find(nodeNum), dSet.find(childNum));
                ancestors[dSet.find(nodeNum)] = nodeNum;
            }
        }
        black[nodeNum] = 1;

        if (null == pairs.get(nodeNum)) {
            return;
        }

        for (int pair : pairs.get(nodeNum)) {
            if (black[pair] == 1) {
                System.out.printf("LCA(%s, %s) = %s\n", nodeNum, pair, ancestors[dSet.find(pair)]);
            }
        }
    }
}
