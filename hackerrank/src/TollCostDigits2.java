import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class TollCostDigits2 {
    private static int COST_LIMIT = 10;

    static class Node {
        private int num;
        private Map<Node, List<Integer>> costs = new HashMap<>();
        private int pathFromRootCost = 0;
        long[] allPairs = new long[COST_LIMIT];
        long[] ingoing = new long[COST_LIMIT];
        long[] outgoing = new long[COST_LIMIT];

        public Node(final int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public void addCost(Node nd, int cost) {
            List<Integer> ndCosts = costs.get(nd);
            if (ndCosts == null) {
                ndCosts = new ArrayList<>();
                costs.put(nd, ndCosts);
            }
            ndCosts.add(cost);
        }

        public Map<Node, List<Integer>> getCosts() {
            return costs;
        }

        public int getPathFromRootCost() {
            return pathFromRootCost;
        }

        public void setPathFromRootCost(int pathFromRootCost) {
            this.pathFromRootCost = pathFromRootCost;
        }

        public long[] getAllPairs() {
            return allPairs;
        }

        public long[] getIngoing() {
            return ingoing;
        }

        public long[] getOutgoing() {
            return outgoing;
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

        long[] result = processGraph(nodes);

        for (int i = 0; i < COST_LIMIT; i++) {
            System.out.println(result[i]);
        }
    }

    public static long[] processGraph(Node[] graph) {
        int n = graph.length;
        int[] processed = new int[n];
        long[] resultPairs = new long[COST_LIMIT];

        for (int i = 0; i < n; i++) {
            if (processed[i] == 0) {
                Set<Integer> cycles = new HashSet<>();
                deepSearch(graph[i], graph, processed, cycles, new HashSet<>());
                combineResultsWithCycles(resultPairs, graph[i].getAllPairs(), cycles);
            }
        }

        return resultPairs;
    }

    private static void combineResultsWithCycles(long[] resultPairs, long[] localPairs, Set<Integer> cycles) {
        boolean hasOdd = cycles.contains(1) || cycles.contains(3) || cycles.contains(7) || cycles.contains(9);
        boolean hasEven = cycles.contains(2) || cycles.contains(4) || cycles.contains(6) || cycles.contains(8);
        boolean hasFive = cycles.contains(5);

        if (hasOdd || hasFive && hasEven) {
            for (int i = 0; i < COST_LIMIT; i++) {
                for (int j = 0; j < COST_LIMIT; j++) {
                    resultPairs[i] += localPairs[(i + j) % COST_LIMIT];
                }
            }
            return;
        }

        if (hasEven) {
            for (int i = 0; i < COST_LIMIT; i++) {
                for (int j = 0; j < COST_LIMIT; j+=2) {
                    resultPairs[i] += localPairs[(i + j) % COST_LIMIT];
                }
            }
            return;
        }

        if (hasFive) {
            for (int i = 0; i < COST_LIMIT; i++) {
                for (int j = 0; j < COST_LIMIT; j+=5) {
                    resultPairs[i] += localPairs[(i + j) % COST_LIMIT];
                }
            }
            return;
        }

        for (int i = 0; i < COST_LIMIT; i++) {
            resultPairs[i] += localPairs[i];
        }
    }

    private static void deepSearch(Node nd, Node[] graph, int[] processed, Set<Integer> cycles, Set<Node> path) {
        int currNum = nd.getNum();
        processed[currNum] = 1;
        path.add(nd);

        for (Map.Entry<Node, List<Integer>> entry : nd.getCosts().entrySet()) {
            int childNum = entry.getKey().getNum();
            List<Integer> costs = entry.getValue();
            Integer firstCost = costs.get(0);

            if (processed[childNum] == 0) {
                graph[childNum].setPathFromRootCost((graph[currNum].getPathFromRootCost() + firstCost) % COST_LIMIT);

                deepSearch(graph[childNum], graph, processed, cycles, path);

                combine(nd.getAllPairs(), nd.getIngoing(), nd.getOutgoing(),
                        graph[childNum].getAllPairs(), graph[childNum].getIngoing(), graph[childNum].getOutgoing(),
                        COST_LIMIT - firstCost, firstCost);
            } else {
                if (path.contains(graph[childNum])) {
                    for (Integer cost : costs) {
                        cycles.add((nd.getPathFromRootCost() + cost - graph[childNum].getPathFromRootCost() + COST_LIMIT) % COST_LIMIT);
                    }
                }
            }
        }

        path.remove(nd);
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
