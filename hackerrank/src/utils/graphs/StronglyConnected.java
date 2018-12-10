package utils.graphs;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class StronglyConnected {
    static class Node {
        private int num;
        private Set<Node> outgoings = new HashSet<>();
        private Set<Node> ingoings = new HashSet<>();
        private int outTime;
        private int conComp = -1;

        public Node(final int num) {
            this.num = num;
        }

        public void addOutgoing(Node nd) {
            outgoings.add(nd);
            nd.addIngoing(this);
        }

        public void addIngoing(Node nd) {
            ingoings.add(nd);
        }

        public int getNum() {
            return num;
        }

        public Set<Node> getOutgoings() {
            return outgoings;
        }

        public Set<Node> getIngoings() {
            return ingoings;
        }

        public void setOutTime(final int outTime) {
            this.outTime = outTime;
        }

        public int getOutTime() {
            return outTime;
        }

        public int getConComp() {
            return conComp;
        }

        public void setConComp(final int conComp) {
            this.conComp = conComp;
        }

        @Override
        public String toString() {
            return String.valueOf(num);
        }
    }

    public static Node[] generateGraph(int verts, int edges) {
        Node[] nodes = new Node[verts];

        for (int i = 0; i < verts; i++) {
            nodes[i] = new Node(i);
        }

        for (int i = 0; i < edges; i++) {
            int v1 = 0;
            int v2 = 0;

            while (v1 == v2 || nodes[v1].getOutgoings().contains(nodes[v2])) {
                v1 = (int)(verts * Math.random());
                v2 = (int)(verts * Math.random());
            }

            nodes[v1].addOutgoing(nodes[v2]);
        }

        return nodes;
    }

    public static void main(String[] args) {
        int n = 5;
        Node[] graph = generateGraph(n, 6);
        int[] processed = new int[n];

        int lastOut = 0;
        for (int i = 0; i < n; i++) {
            if (processed[i] == 0) {
                lastOut = topoSort(graph[i], processed, lastOut);
            }
        }

        Arrays.sort(graph, Comparator.comparingInt(Node::getOutTime).reversed());

        processed = new int[n];
        Queue<Node> q = new LinkedList<>();
        int compNum = 0;

        for (int i = 0; i < n; i++) {
            Node nd = graph[i];
            if (processed[nd.getNum()] == 0) {
                compNum++;
                q.add(nd);

                while (!q.isEmpty()) {
                    Node currNode = q.poll();
                    processed[currNode.getNum()] = 1;
                    currNode.setConComp(compNum);

                    for (Node ingNode : currNode.getIngoings()) {
                        if (processed[ingNode.getNum()] == 0) {
                            q.add(ingNode);
                        }
                    }
                }
            }
        }

        for (Node nd : graph) {
            System.out.println("num=" + nd + "; outTime=" + nd.getOutTime() + "; compComp=" + nd.getConComp() + "; " + nd.getOutgoings());
        }
    }

    private static int topoSort(Node nd, int[] processed, int lastOut) {
        processed[nd.getNum()] = 1;

        System.out.println("in - " + nd);

        for (Node next : nd.getOutgoings()) {
            if (processed[next.getNum()] == 0) {
                lastOut = topoSort(next, processed, lastOut);
            }
        }

        System.out.println("out - " + nd);

        nd.setOutTime(lastOut + 1);
        return nd.getOutTime();
    }
}
