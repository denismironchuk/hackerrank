import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Created by Denis_Mironchuk on 8/30/2018.
 */
public class MinimumPenaltyPath {
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

    static class Pair implements Comparable{
        private int dist;
        private int num;

        public Pair(int dist, int num) {
            this.dist = dist;
            this.num = num;
        }

        public int getDist() {
            return dist;
        }

        public void setDist(final int dist) {
            this.dist = dist;
        }

        public int getNum() {
            return num;
        }

        public void setNum(final int num) {
            this.num = num;
        }

        @Override
        public int compareTo(final Object o) {
            Pair p2 = (Pair)o;
            int distComp = Integer.compare(this.dist, p2.dist);
            if (distComp == 0) {
                return Integer.compare(this.num, p2.num);
            } else {
                return distComp;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new FileReader("D:/penalty6.txt"));
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

        TreeSet<Pair> tree = new TreeSet<>();

        tree.add(new Pair(0, a));

        int[] d = new int[n];
        for (int i = 0; i < n; i++) {
            d[i] = Integer.MAX_VALUE;
        }

        d[a] = 0;
        int[] processed = new int[n];

        while (!tree.isEmpty()) {
            Pair p = tree.pollFirst();
            int dist = p.getDist();
            Node nd = nodes[p.getNum()];
            processed[p.getNum()] = 1;
            for (Map.Entry<Node, List<Integer>> neighEntry : nd.getNeighbours().entrySet()) {
                Node neigh = neighEntry.getKey();

                if (processed[neigh.getNum()] == 1) {
                    continue;
                }

                List<Integer> costs = neighEntry.getValue();
                for (int cost:costs) {
                    int newCost = dist | cost;
                    if (d[neigh.getNum()] > newCost) {
                        tree.remove(new Pair(d[neigh.getNum()], neigh.getNum()));
                        tree.add(new Pair(newCost, neigh.getNum()));
                        d[neigh.getNum()] = newCost;
                    }
                }
            }
        }

        int result = d[b];

        System.out.println(result == Integer.MAX_VALUE ? -1 : result);
    }
}
