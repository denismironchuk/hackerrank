import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class RoadsAndLibraries {
    static class Node {
        private int num;
        private List<Node> neighbours = new ArrayList<>();

        public Node(final int num) {
            this.num = num;
        }

        public void addNeighbour(Node nd) {
            neighbours.add(nd);
        }

        public List<Node> getNeighbours() {
            return neighbours;
        }

        public int getNum() {
            return num;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader br = new BufferedReader(new FileReader("D://input03.txt"));
        int Q = Integer.parseInt(br.readLine());

        for (int q = 0; q < Q; q++) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(tkn1.nextToken());
            int m = Integer.parseInt(tkn1.nextToken());
            int cLib = Integer.parseInt(tkn1.nextToken());
            int cRoad = Integer.parseInt(tkn1.nextToken());

            Node[] nodes = new Node[n + 1];

            for (int i = 1; i <= n; i++) {
                nodes[i] = new Node(i);
            }

            for (int i = 0; i < m; i++) {
                StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
                int node1 = Integer.parseInt(edgeTkn.nextToken());
                int node2 = Integer.parseInt(edgeTkn.nextToken());
                nodes[node1].addNeighbour(nodes[node2]);
                nodes[node2].addNeighbour(nodes[node1]);
            }

            int[] processed = new int[n + 1];
            long cost = 0;
            for (int i = 1; i <= n; i++) {
                if (processed[i] == 0) {
                    Node nd = nodes[i];
                    int reachableCnt = findReachableCnt(nd,  processed);
                    long minCost = Long.MAX_VALUE;
                    for (int libs = 1; libs <= reachableCnt; libs++) {
                        long posCost = libs * cLib + (reachableCnt - libs) * cRoad;
                        if (posCost < minCost) {
                            minCost = posCost;
                        }
                    }
                    cost += minCost;
                }
            }

            System.out.println(cost);
        }
    }

    private static int findReachableCnt(Node nd, int[] processed) {
        int result = 1;
        processed[nd.getNum()] = 1;

        for (Node n : nd.getNeighbours()) {
            if (processed[n.getNum()] == 0) {
                result += findReachableCnt(n, processed);
            }
        }

        return result;
    }
}
