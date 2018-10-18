import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Влада on 04.10.2018.
 */
public class DemandingMoney {
    private static long CNT = 0;
    static class Node {
        private int num;
        private int cost;
        private Set<Node> neighbours = new HashSet<>();

        public Node(int num, int cost) {
            this.num = num;
            this.cost = cost;
        }

        public void addNeighbour(Node neigh) {
            neighbours.add(neigh);
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getCost() {
            return cost;
        }

        public Set<Node> getNeighbours() {
            return neighbours;
        }

        @Override
        public String toString() {
            return String.valueOf(num + 1) + "-" + getCost();
        }
    }

    public static Node[] generateGraph(int n) {
        Node[] nodes = new Node[n];
        List<Integer> notConnected = new ArrayList<>();
        List<Integer> connected = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i, 1);
            notConnected.add(i);
        }

        /*connected.add(notConnected.get(0));
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
        }*/

        return nodes;
    }

    private static void addEdges(int edgesToAdd, Node[] graph) {
        int n = graph.length;
        for (int i = 0; i < edgesToAdd; i++) {
            int v1 = (int)((n - 1) * Math.random());

            int v2 = v1;

            while (v1 == v2) {
                v2 = (int)((n - 1) * Math.random());
            }

            graph[v1].addNeighbour(graph[v2]);
            graph[v2].addNeighbour(graph[v1]);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line1Tkn = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(line1Tkn.nextToken());
        int m = Integer.parseInt(line1Tkn.nextToken());

        Node[] nodes = new Node[n];

        StringTokenizer costsTkn = new StringTokenizer(br.readLine());

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i, Integer.parseInt(costsTkn.nextToken()));
        }

        for (int i = 0; i < m; i++) {
            StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
            int n1 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int n2 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            nodes[n1].addNeighbour(nodes[n2]);
            nodes[n2].addNeighbour(nodes[n1]);
        }

        /*int n = 32;
        Node[] nodes = generateGraph(n);
        addEdges(0, nodes);*/

        Date start = new Date();
        int[] maxCost = new int[2];
        Set<Node> toProcess = new HashSet<>();
        for (Node nd : nodes) {
            toProcess.add(nd);
        }
        maxCost(toProcess, new HashSet<>(), 0, maxCost);
        System.out.println(maxCost[0] + " " + maxCost[1]);
        Date end = new Date();

        System.out.println((end.getTime() - start.getTime()) + "ms");
        System.out.println(CNT);
    }

    private static void maxCost(Set<Node> nodes, Set<Node> clique, int cost, int[] maxCost) {
        System.out.println(clique + " - " + cost);
        Set<Node> toProcess = new HashSet<>();
        toProcess.addAll(nodes);

        for (Node node : nodes) {
            CNT++;

            boolean connected = true;

            for (Node cliqueNode : clique) {
                if (cliqueNode.getNeighbours().contains(node)) {
                    connected = false;
                    break;
                }
            }

            toProcess.remove(node);

            if (connected) {
                clique.add(node);
                int newCost = cost + node.getCost();

                if (newCost > maxCost[0]) {
                    maxCost[0] = newCost;
                    maxCost[1] = 1;
                } else if (newCost == maxCost[0]) {
                    maxCost[1]++;
                }

                maxCost(toProcess, clique, newCost, maxCost);

                clique.remove(node);
            }
        }
    }
}
