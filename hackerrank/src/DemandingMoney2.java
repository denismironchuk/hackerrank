import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by Влада on 04.10.2018.
 */
public class DemandingMoney2 {
    static class Node {
        private int num;
        private long cost;
        private Set<Node> neighbours = new HashSet<>();

        public Node(int num, long cost) {
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

        public long getCost() {
            return cost;
        }

        public Set<Node> getNeighbours() {
            return neighbours;
        }

        @Override
        public String toString() {
            return String.valueOf(num + 1) + "-" + String.valueOf(cost);
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

        connected.add(notConnected.get(0));
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
        }

        return nodes;
    }

    private static void addEdges(int edgesToAdd, Node[] graph) {
        int n = graph.length;
        for (int i = 0; i < edgesToAdd; i++) {
            int v1 = (int)((n) * Math.random());

            int v2 = v1;

            while (v1 == v2) {
                v2 = (int)((n) * Math.random());
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

        /*int n = 34;
        Node[] nodes = generateGraph(n);
        addEdges(500, nodes);*/

        long[] maxCost = new long[2];
        List<Set<Node>> maxCliques = new ArrayList<>();
        maxCost(nodes, new HashSet<>(), 0, maxCost, maxCliques);
        maxCliques.forEach(System.out::println);

        for (Node nd : nodes) {
            if (nd.getCost() == 0) {
                List<Set<Node>> newCliques = new ArrayList<>();

                for (Set<Node> clique : maxCliques) {
                    boolean notConected = true;
                    for (Node cliqueNode : clique) {
                        if (cliqueNode.getNeighbours().contains(nd)) {
                            notConected = false;
                            break;
                        }
                    }

                    if (notConected) {
                        Set<Node> newClique = new HashSet<>();
                        newClique.addAll(clique);
                        newClique.add(nd);
                        newCliques.add(newClique);
                    }
                }
                maxCliques.addAll(newCliques);
            }
        }
        System.out.println(maxCost[0] + " " + maxCliques.size());
    }

    private static Set<Node> maxCost(Node[] nodes, Set<Node> clique, long cost, long[] maxCost, List<Set<Node>> maxCliques) {
        Set<Node> processed = new HashSet<>();

        boolean wasIncreased = false;
        for (Node nd : nodes) {
            if (clique.contains(nd) || processed.contains(nd) || nd.getCost() == 0) {
                continue;
            }

            boolean connected = true;

            for (Node cliqueNode : clique) {
                if (cliqueNode.getNeighbours().contains(nd)) {
                    connected = false;
                    break;
                }
            }

            if (!connected) {
                continue;
            }

            wasIncreased = true;
            processed.add(nd);

            clique.add(nd);
            processed.addAll(maxCost(nodes, clique, cost + nd.getCost(), maxCost, maxCliques));
            clique.remove(nd);
        }

        if (!wasIncreased) {
            //System.out.println(clique + " " + cost);
            if (cost > maxCost[0]) {
                maxCost[0] = cost;
                maxCost[1] = 1;

                maxCliques.clear();
                Set<Node> maxClique = new HashSet<>();
                maxClique.addAll(clique);
                maxCliques.add(maxClique);
            } else if (cost == maxCost[0]) {
                maxCost[1] += 1;

                Set<Node> maxClique = new HashSet<>();
                maxClique.addAll(clique);
                maxCliques.add(maxClique);
            }
        }

        return processed;
    }
}
