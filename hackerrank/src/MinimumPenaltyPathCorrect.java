import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Denis_Mironchuk on 8/30/2018.
 */
public class MinimumPenaltyPathCorrect {
    static class Node {
        private int num;
        //node -> dist/cost
        private Map<Node, List<Integer>> neighbours = new HashMap<>();

        public Node(final int num) {
            this.num = num;
        }

        public void addNeighbour(Node neighbour, int cost) {
            List<Integer> costs = neighbours.get(neighbour);
            if (null == costs) {
                costs = new ArrayList<>();
                neighbours.put(neighbour, costs);
            }
            costs.add(cost);
        }

        public Map<Node, List<Integer>> getNeighbours() {
            return neighbours;
        }

        public int getNum() {
            return num;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader br = new BufferedReader(new FileReader("D:/penalty6.txt"));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        int m = Integer.parseInt(tkn1.nextToken());

        Node[] nodes = new Node[n];

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }

        for (int i = 0; i<m; i++) {
            StringTokenizer tkn2 = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(tkn2.nextToken()) - 1;
            int v = Integer.parseInt(tkn2.nextToken()) - 1;
            int c = Integer.parseInt(tkn2.nextToken());

            nodes[u].addNeighbour(nodes[v], c);
            nodes[v].addNeighbour(nodes[u], c);
        }

        StringTokenizer tkn3 = new StringTokenizer(br.readLine());
        int a = Integer.parseInt(tkn3.nextToken()) - 1;
        int b = Integer.parseInt(tkn3.nextToken()) - 1;

        int[] proc = new int[n];
        dfs(nodes[a], proc);

        if (proc[b] == 0) {
            System.out.println(-1);
            return;
        }

        int result = 0;
        int bit = 1024;

        while(bit > 0) {
            int[] processed = new int[n];
            dfs(nodes[a], processed, bit);
            if (processed[b] == 0) {
                result = result | bit;
            } else {
                removeEdges(nodes, bit);
            }
            bit /= 2;
        }

        System.out.println(result == 0 ? -1 : result);
    }

    private static void dfs(Node nd, int[] processed, int bit) {
        processed[nd.getNum()] = 1;
        for (Map.Entry<Node, List<Integer>> entry: nd.getNeighbours().entrySet()) {
            Node neigh = entry.getKey();

            if (processed[neigh.getNum()] == 0) {
                for (int cost : entry.getValue()) {
                    if ((cost | bit) != cost) {
                        dfs(neigh, processed, bit);
                    }
                }
            }
        }
    }

    private static void removeEdges(Node[] nodes, int bit) {
        for (Node nd : nodes) {
            for (Map.Entry<Node, List<Integer>> entry: nd.getNeighbours().entrySet()) {
                List<Integer> costs = entry.getValue();
                List<Integer> newCosts = new ArrayList<>();

                for (int cost : costs) {
                    if ((cost | bit) != cost) {
                        newCosts.add(cost);
                    }
                }

                entry.setValue(newCosts);
            }
        }
    }

    private static void dfs(Node nd, int[] processed) {
        processed[nd.getNum()] = 1;
        for (Map.Entry<Node, List<Integer>> entry: nd.getNeighbours().entrySet()) {
            Node neigh = entry.getKey();

            if (processed[neigh.getNum()] == 0) {
                dfs(neigh, processed);
            }
        }
    }
}
