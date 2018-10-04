import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Влада on 04.10.2018.
 */
public class DemandingMoney {
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

        public void setCost(int cost) {
            this.cost = cost;
        }

        public Set<Node> getNeighbours() {
            return neighbours;
        }

        @Override
        public String toString() {
            return String.valueOf(num);
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

        System.out.println(maxCost(nodes, new HashSet<>(), new HashSet<>(), 0));
    }

    private static int maxCost(Node[] nodes, Set<Node> clique, Set<Node> ignore, int cost) {
        int n = nodes.length;
        List<Node> addedIgnoreNodes = new ArrayList<>();
        int maxCost = cost;
        for (Node node : nodes) {
            if (ignore.contains(node)) {
                continue;
            }

            boolean connected = true;

            for (Node cliqueNode : clique) {
                if (!cliqueNode.getNeighbours().contains(node)) {
                    connected = false;
                    break;
                }
            }

            ignore.add(node);
            addedIgnoreNodes.add(node);

            if (connected) {
                clique.add(node);
                System.out.println(clique);
                int newCost = cost + node.getCost();
                int newMaxCost = maxCost(nodes, clique, ignore, newCost);
                maxCost = Math.max(Math.max(maxCost, newCost), newMaxCost);
                clique.remove(node);
            }
        }

        for (Node ignoreNode : addedIgnoreNodes) {
            ignore.remove(ignoreNode);
        }

        return maxCost;
    }
}
