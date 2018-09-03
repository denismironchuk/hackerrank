import java.io.BufferedReader;
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
public class JackGoesToRapture {
    static class Node {
        private int num;
        //node -> dist/cost
        private Map<Node, Integer> neighbours = new HashMap<>();

        public Node(final int num) {
            this.num = num;
        }

        public void addNeighbour(Node neighbour, int cost) {
            neighbours.put(neighbour, cost);
        }

        public Map<Node, Integer> getNeighbours() {
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
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
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

        TreeSet<Pair> tree = new TreeSet<>();

        tree.add(new Pair(0, 0));

        int[] d = new int[n];
        for (int i = 0; i < n; i++) {
            d[i] = Integer.MAX_VALUE;
        }

        d[0] = 0;
        int[] processed = new int[n];
        processed[0] = 1;

        while (!tree.isEmpty()) {
            Pair p = tree.pollFirst();
            int paydCost = p.getDist();
            Node nd = nodes[p.getNum()];

            for (Map.Entry<Node, Integer> neighEntry : nd.getNeighbours().entrySet()) {
                Node neigh = neighEntry.getKey();

                if (processed[neigh.getNum()] == 1) {
                    continue;
                }

                Integer cost = neighEntry.getValue();

                int newCost = Math.max(paydCost, cost);
                if (d[neigh.getNum()] > newCost) {
                    tree.remove(new Pair(d[neigh.getNum()], neigh.getNum()));
                    tree.add(new Pair(newCost, neigh.getNum()));
                    d[neigh.getNum()] = newCost;
                }
            }
        }

        int result = d[n - 1];

        System.out.println(result == Integer.MAX_VALUE ? "NO PATH EXISTS" : result);
    }
}
