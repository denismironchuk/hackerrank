package utils.graphs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 9/7/2018.
 */
public class Edmonds {
    static class Node {
        private int num;
        private List<Node> neighbours = new ArrayList<>();

        public Node(final int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public void addNeigh(Node neigh) {
            neighbours.add(neigh);
        }

        public List<Node> getNeighbours() {
            return neighbours;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        Node[] nodes = new Node[n];

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }

        int m = Integer.parseInt(br.readLine());

        for (int i = 0; i < m; i++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int v1 = Integer.parseInt(tkn.nextToken());
            int v2 = Integer.parseInt(tkn.nextToken());

            nodes[v1].addNeigh(nodes[v2]);
            nodes[v2].addNeigh(nodes[v1]);
        }

        int[] pairs = new int[n];

        for (int i = 0; i < n; i++) {
            pairs[i] = -1;
        }

        for (int i = 0; i < n; i++) {
            if (pairs[i] == -1) {
                int[] processed = new int[n];
                int[] parents = new int[n];

                for (int j = 0; j < n; j++) {
                    parents[j] = -1;
                }

                Queue<Node> queue = new LinkedList<>();
                queue.add(nodes[i]);
                boolean foundPath = false;
                Node end = null;

                while (!queue.isEmpty() && !foundPath) {
                    Node nd = queue.poll();
                    processed[nd.getNum()] = 1;
                    for (Node neigh : nd.getNeighbours()) {
                        int neighNum = neigh.getNum();

                        if (processed[neighNum] == 0) {
                            processed[neighNum] = -1;
                            parents[neighNum] = nd.getNum();

                            if (pairs[neighNum] == -1) {
                                foundPath = true;
                                end = neigh;
                                break;
                            } else {
                                queue.add(nodes[pairs[neighNum]]);
                            }
                        } else if (processed[neighNum] == 1) {
                            System.out.println("Cycle found");
                            Node base = findBase(nd, neigh, pairs, parents, nodes);
                            return;
                            /*parents[nd.getNum()] = neighNum;
                            parents[neighNum] = nd.getNum();

                            queue.add(neigh);*/
                        }
                    }
                }

                if (foundPath) {
                    increasePath(nodes[i], end, pairs, parents, nodes);
                }
            }
        }
    }

    private static void increasePath(Node start, Node end, int[] pairs, int[] parents, Node[] nodes) {
        while (parents[end.getNum()] != start.getNum()) {
            int endNum = end.getNum();
            pairs[endNum] = parents[endNum];

            end = nodes[pairs[parents[endNum]]];

            pairs[parents[endNum]] = endNum;
        }

        int endNum = end.getNum();
        pairs[endNum] = parents[endNum];
        pairs[parents[endNum]] = endNum;
    }

    private static void markCycleNodes(Node base, Node end, int[] pairs, int[] parents, Node[] nodes, Queue<Node> q) {
        while (base != end) {

        }
    }

    private static Node findBase(Node nd1, Node nd2, int[] pairs, int[] parents, Node[] nodes) {
        Node nd = nd1;
        int[] processedNodes = new int[nodes.length];
        while (pairs[nd.getNum()] != -1) {
            Node pairStart = nodes[pairs[nd.getNum()]];

            processedNodes[nd.getNum()] = 1;
            processedNodes[pairStart.getNum()] = 1;

            nd = nodes[parents[pairStart.getNum()]];
        }

        Node base = nd2;
        while (processedNodes[base.getNum()] == 0) {
            Node pairStart = nodes[pairs[base.getNum()]];
            base = nodes[parents[pairStart.getNum()]];
        }

        return base;
    }
}
