import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class ProblemSolving {
    static class Node {
        private int num;
        private int v;
        private Set<Node> neighbours = new HashSet<>();

        public Node(final int num, final int v) {
            this.num = num;
            this.v = v;
        }

        public void addNeighbour(Node neigh) {
            neighbours.add(neigh);
        }

        public int getNum() {
            return num;
        }

        public Set<Node> getNeighbours() {
            return neighbours;
        }

        public int getV() {
            return v;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader br = new BufferedReader(new FileReader("D:/problemSolving.txt"));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(tkn1.nextToken());
            int k = Integer.parseInt(tkn1.nextToken());

            Node[] nodes = new Node[n * 2];

            StringTokenizer vTkn = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                int v = Integer.parseInt(vTkn.nextToken());
                nodes[i] = new Node(i, v);
                nodes[i + n] = new Node(i + n, v);
            }

            for (int i = 0; i < n; i++) {
                for (int j = n + i + 1; j < 2 * n; j++) {
                    if (Math.abs(nodes[i].getV() - nodes[j].getV()) >= k) {
                        nodes[i].addNeighbour(nodes[j]);
                    }
                }
            }

            int[] pair = new int[2 * n];
            Arrays.fill(pair, -1);

            for (Node nd : nodes) {
                if (pair[nd.getNum()] == -1) {
                    increasePath(nd, new int[2 * n], pair, nodes);
                }
            }

            int[] processed = new int[2 * n];
            int days = 0;

            for (int i = 0; i < n; i++) {
                if (processed[i] == 0) {
                    days++;
                    followPath(nodes[i], processed, pair, nodes, n);
                }
            }

            System.out.println(days);
        }
    }

    private static void followPath(Node nd, int[] processed, int[] pair, Node[] nodes, int n) {
        processed[nd.getNum()] = 1;

        if (pair[nd.getNum()] != -1) {
            followPath(nodes[pair[nd.getNum()] - n], processed, pair, nodes, n);
        }
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
