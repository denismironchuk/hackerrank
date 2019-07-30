package codejam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphCycleVerts {
    private static final int N = 10;
    public static final double THRESHOLD = 0.99;
    private static Node[] nodes = new Node[N];

    private static class Node {
        private int num;
        private List<Node> outgoings = new ArrayList<>();

        public Node(int num) {
            this.num = num;
        }

        public void addOutgoing(Node n) {
            outgoings.add(n);
        }

        @Override
        public String toString() {
            return "num=" + num;
        }
    }

    private static void buildRandomGraph() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i != j && Math.random() > THRESHOLD) {
                    nodes[i].addOutgoing(nodes[j]);
                }
            }
        }
    }

    public static void main(String[] args) {
        while (true) {
            for (int i = 0; i < N; i++) {
                nodes[i] = new Node(i);
            }

            buildRandomGraph();

            Set<Node> cycleNodesOpt = getCycleNodesOpt();
            Set<Node> cycleNodesTriv = getCycleNodesTriv();

            if (cycleNodesOpt.size() != cycleNodesTriv.size()) {
                throw new RuntimeException("!!!!!!");
            }

            for (Node nd : cycleNodesOpt) {
                if (!cycleNodesTriv.contains(nd)) {
                    throw new RuntimeException("!!!!!!");
                }
            }
        }
    }

    private static Set<Node> getCycleNodesOpt(){
        Set<Node> cycleNodes = new HashSet<>();
        int[] processed = new int[N];
        int[] path = new int[N];

        for (int i = 0; i < N; i++) {
            if (processed[i] == 0) {
                getCycleNodes(nodes[i], 1, processed, path, cycleNodes);
            }
        }
        return cycleNodes;
    }

    private static Set<Node> getCycleNodesTriv() {
        Set<Node> cycleNodes = new HashSet<>();
        for (int i = 0; i < N; i++) {
            if (isCycleVert(nodes[i], new int[N], nodes[i])) {
                cycleNodes.add(nodes[i]);
            }
        }

        return cycleNodes;
    }

    private static boolean isCycleVert(Node n, int[] processed, Node target) {
        processed[n.num] = 1;

        for (Node out : n.outgoings) {
            if (out.num == target.num) {
                return true;
            }

            if (processed[out.num] == 0) {
                if (isCycleVert(out, processed, target)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static int getCycleNodes(Node n, int pathNum, int[] processed, int[] path, Set<Node> cycleNodes) {
        processed[n.num] = 1;
        path[n.num] = pathNum;

        int res = Integer.MAX_VALUE;

        for (Node out : n.outgoings) {
            if (processed[out.num] == 0) {
                int cyclePathNum = getCycleNodes(out, pathNum + 1, processed, path, cycleNodes);
                if (cyclePathNum <= pathNum) {
                    res = Math.min(cyclePathNum, res);
                    cycleNodes.add(n);
                }
            } else if (path[out.num] != 0) {
                res = Math.min(path[out.num], res);
                cycleNodes.add(n);
            }
        }

        path[n.num] = 0;

        return res;
    }
}
