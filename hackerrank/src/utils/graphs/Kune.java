package utils.graphs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 9/7/2018.
 */
public class Kune {
    static class Node {
        private int num;
        private Set<Node> neighbours = new HashSet<>();

        public Node(final int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public void addNeigh(Node neigh) {
            neighbours.add(neigh);
        }

        public Set<Node> getNeighbours() {
            return neighbours;
        }
    }

    private static Node[] generateFromInputStream() throws IOException {
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

        return nodes;
    }

    private static Node[] generateRandom(int n1, int n2, int m) throws IOException {
        int n = n1 + n2;

        Node[] nodes = new Node[n];

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }

        for (int i = 0; i < m; i++) {
            int v1 = (int)(Math.random() * n1);
            int v2 = (int)(Math.random() * n2) + n1;

            nodes[v1].addNeigh(nodes[v2]);
            nodes[v2].addNeigh(nodes[v1]);
        }

        return nodes;
    }

    public static void main(String[] args) throws IOException {
        //Node[] nodes = generateFromInputStream();
        Node[] nodes = generateRandom(1000, 1000, 50000);
        int n = nodes.length;
        int[] pair = new int[n];
        Arrays.fill(pair, -1);

        Date start = new Date();
        for (Node nd : nodes) {
            if (pair[nd.getNum()] != -1) {
                continue;
            }

            increasePath(nd, new int[n], pair, nodes);
        }
        Date end = new Date();

        System.out.println(end.getTime() - start.getTime() + "ms");
    }

    private static boolean increasePath(Node nd, int[] processed, int[] pair, Node[] nodes) {
        int ndNum = nd.getNum();
        processed[ndNum] = 1;

        for (Node neigh : nd.getNeighbours()) {
            int neighNum = neigh.getNum();

            if (processed[neighNum] == 1) {
                continue;
            }

            processed[neighNum] = 1;

            if (pair[neighNum] == -1 || increasePath(nodes[pair[neighNum]], processed, pair, nodes)) {
                pair[neighNum] = ndNum;
                pair[ndNum] = neighNum;
                return true;
            }
        }

        return false;
    }
}
