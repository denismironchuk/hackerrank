import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class TollCostDigits {
    private static int COST_LIMIT = 10;

    static class Node {
        private int num;
        private Map<Integer, List<Integer>> costs = new HashMap<>();

        public Node(final int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public void addCost(Node nd, int cost) {
            List<Integer> ndCosts = costs.get(nd.getNum());
            if (ndCosts == null) {
                ndCosts = new ArrayList<>();
                costs.put(nd.getNum(), ndCosts);
            }
            ndCosts.add(cost);
        }

        public Map<Integer, List<Integer>> getCosts() {
            return costs;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        int e = Integer.parseInt(tkn1.nextToken());

        Node[] nodes = new Node[n];

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }

        for (int i = 0; i < e; i++) {
            StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
            int n1 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int n2 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int cost = Integer.parseInt(edgeTkn.nextToken()) % COST_LIMIT;

            nodes[n1].addCost(nodes[n2], cost);
            nodes[n2].addCost(nodes[n1], COST_LIMIT - cost);
        }

        Node[] tree = new Node[n];

        for (int i = 0; i < n; i++) {
            tree[i] = new Node(i);
        }

        processGraph(nodes, tree);
    }

    public static long[] processGraph(Node[] graph, Node[] tree) {
        int n = graph.length;
        int[] processed = new int[n];
        long[] resultPairs = new long[COST_LIMIT];

        for (int i = 0; i < n; i++) {
            if (processed[i] == 0) {
                deepSearch(graph, tree, processed, graph[i]);
                long[] allPairs = new long[COST_LIMIT];
                countPairsInTree(tree[i], tree, new int[n], allPairs, new long[COST_LIMIT], new long[COST_LIMIT]);
            }
        }

        return resultPairs;
    }

    private static void deepSearch(Node[] graph, Node[] tree, int[] processed, Node nd) {
        processed[nd.getNum()] = 1;

        for (Map.Entry<Integer, List<Integer>> entry : nd.getCosts().entrySet()) {
            int nodeNum = entry.getKey();
            List<Integer> costs = entry.getValue();
            Integer firstCost = costs.get(0);

            if (processed[nodeNum] == 0) {
                tree[nd.getNum()].addCost(tree[nodeNum], firstCost);
                tree[nodeNum].addCost(tree[nd.getNum()], COST_LIMIT - firstCost);

                deepSearch(graph, tree, processed, graph[nodeNum]);
            }
        }
    }

    private static void countPairsInTree(Node nd, Node[] tree, int[] processed, long[] allPairs, long[] ingoing, long[] outgoing) {
        processed[nd.getNum()] = 1;

        for (Map.Entry<Integer, List<Integer>> entry : nd.getCosts().entrySet()) {
            int nodeNum = entry.getKey();
            List<Integer> costs = entry.getValue();
            Integer firstCost = costs.get(0);

            if (processed[nodeNum] == 0) {
                long[] allPairsNew = new long[COST_LIMIT];
                long[] ingoingNew = new long[COST_LIMIT];
                long[] outgoingNew = new long[COST_LIMIT];

                countPairsInTree(tree[nodeNum], tree, processed, allPairsNew, ingoingNew, outgoingNew);
                combine(allPairs, ingoing, outgoing, allPairsNew, ingoingNew, outgoingNew,
                            COST_LIMIT - firstCost, firstCost);
            }
        }
    }

    private static void combine(long[] allPairs, long[] ingoing, long[] outgoing,
                         long[] allPairsNew, long[] ingoing_, long[] outgoing_,
                         int inCost, int outCost) {
        long[] ingoingNew = new long[COST_LIMIT];
        long[] outgoingNew = new long[COST_LIMIT];

        for (int i = 0; i < COST_LIMIT; i++) {
            ingoingNew[(i + inCost) % COST_LIMIT] = ingoing_[i];
            outgoingNew[(i + outCost) % COST_LIMIT] = outgoing_[i];
        }

        outgoingNew[outCost % COST_LIMIT]++;
        ingoingNew[inCost % COST_LIMIT]++;

        for (int i = 0; i < COST_LIMIT; i++) {
            allPairsNew[i] += ingoingNew[i];
            allPairsNew[i] += outgoingNew[i];
        }

        for (int i = 0; i < COST_LIMIT; i++) {
            for (int j = 0; j < COST_LIMIT; j++) {
                allPairs[(i + j) % COST_LIMIT] += ingoing[i] * outgoingNew[j];
                allPairs[(i + j) % COST_LIMIT] += outgoing[i] * ingoingNew[j];
            }
        }

        for (int i = 0; i < COST_LIMIT; i++) {
            allPairs[i] += allPairsNew[i];
            outgoing[i] += outgoingNew[i];
            ingoing[i] += ingoingNew[i];
        }
    }
}
