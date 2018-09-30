import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class TollCostDigits {
    private static int COST_LIMIT = 10;

    static class Node {
        private int num;
        private Map<Integer, List<Integer>> costs = new HashMap<>();
        private int pathFromRootCost = 0;

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

        public int getPathFromRootCost() {
            return pathFromRootCost;
        }

        public void setPathFromRootCost(int pathFromRootCost) {
            this.pathFromRootCost = pathFromRootCost;
        }
    }

    public static Node[] generateGraph(int n) {
        Node[] nodes = new Node[n];
        List<Integer> notConnected = new ArrayList<>();
        List<Integer> connected = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
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

            int cost = (int) (10 * Math.random());

            conNode.addCost(notConNode, cost);
            notConNode.addCost(conNode, COST_LIMIT - cost);
        }

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

            int cost = (int) (10 * Math.random());

            graph[v1].addCost(graph[v2], cost);
            graph[v2].addCost(graph[v1], COST_LIMIT - cost);
        }
    }

    public static void main(String[] args) throws IOException {
        /*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
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
        }*/

        int n = 10000;
        Node[] nodes = generateGraph(n);
        addEdges(10000, nodes);

        System.out.println("Graph is generated");

        Node[] tree = new Node[n];

        for (int i = 0; i < n; i++) {
            tree[i] = new Node(i);
        }

        long[] result = processGraph(nodes, tree);

        for (int i = 0; i < COST_LIMIT; i++) {
            System.out.println(result[i]);
        }
    }

    public static long[] processGraph(Node[] graph, Node[] tree) {
        int n = graph.length;
        int[] processed = new int[n];
        long[] resultPairs = new long[COST_LIMIT];
        int[] procForCycles = new int[n];
        int[] ancestors = new int[n];
        int[] black = new int[n];
        DisjointSet dSet = new DisjointSet(n);

        for (int i = 0; i < n; i++) {
            if (processed[i] == 0) {
                Map<Integer, Set<Integer>> pairsForLca = new HashMap<>();
                deepSearch(graph, tree, processed, graph[i], pairsForLca);
                long[] localPairs = new long[COST_LIMIT];
                countPairsInTree(tree[i], tree, new int[n], localPairs, new long[COST_LIMIT], new long[COST_LIMIT]);
                Set<Integer> cycles = getCycles(tree[i], procForCycles, ancestors, black,
                        dSet, pairsForLca, graph, tree);
                combineResultsWithCycles(resultPairs, localPairs, cycles);
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

    private static Set<Integer> getCycles(Node nd, int[] proc, int[] anc, int[] black, DisjointSet dSet,
                                          Map<Integer, Set<Integer>> pairs, Node[] graph, Node[] tree) {
        Set<Integer> cyclesResult = new HashSet<>();
        int ndNum = nd.getNum();
        proc[ndNum] = 1;
        dSet.makeSet(ndNum);
        anc[dSet.find(ndNum)] = ndNum;

        for (Map.Entry<Integer, List<Integer>> entry : nd.getCosts().entrySet()) {
            int childNum = entry.getKey();

            if (proc[childNum] == 0) {
                cyclesResult.addAll(getCycles(tree[childNum], proc, anc, black, dSet, pairs, graph, tree));
                dSet.unite(dSet.find(ndNum), dSet.find(childNum));
                anc[dSet.find(ndNum)] = ndNum;
            }
        }

        black[ndNum] = 1;

        if (null != pairs.get(ndNum)) {
            for (Integer pair : pairs.get(ndNum)) {
                if (black[pair] == 1) {
                    int lca = anc[dSet.find(pair)];

                    int cycleLen = (tree[ndNum].getPathFromRootCost() - tree[lca].getPathFromRootCost() + COST_LIMIT) % COST_LIMIT;
                    cycleLen += COST_LIMIT - ((tree[pair].getPathFromRootCost() - tree[lca].getPathFromRootCost() + COST_LIMIT) % COST_LIMIT);

                    for (Integer pairCost : graph[ndNum].getCosts().get(pair)) {
                        cyclesResult.add((cycleLen + pairCost) % COST_LIMIT);
                    }
                }
            }
        }

        return cyclesResult;
    }

    private static void deepSearch(Node[] graph, Node[] tree, int[] processed, Node nd, Map<Integer, Set<Integer>> pairs) {
        int ndNum = nd.getNum();
        processed[ndNum] = 1;

        for (Map.Entry<Integer, List<Integer>> entry : nd.getCosts().entrySet()) {
            int childNum = entry.getKey();
            List<Integer> costs = entry.getValue();
            Integer firstCost = costs.get(0);

            if (processed[childNum] == 0) {
                tree[ndNum].addCost(tree[childNum], firstCost);
                tree[childNum].addCost(tree[ndNum], COST_LIMIT - firstCost);

                tree[childNum].setPathFromRootCost((tree[ndNum].getPathFromRootCost() + firstCost) % COST_LIMIT);

                deepSearch(graph, tree, processed, graph[childNum], pairs);
            } else {
                Set<Integer> pairSet1 = pairs.get(ndNum);
                if (pairSet1 == null) {
                    pairSet1 = new HashSet<>();
                    pairs.put(ndNum, pairSet1);
                }
                pairSet1.add(childNum);

                Set<Integer> pairSet2 = pairs.get(childNum);
                if (pairSet2 == null) {
                    pairSet2 = new HashSet<>();
                    pairs.put(childNum, pairSet2);
                }
                pairSet2.add(ndNum);
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

    static class DisjointSet {
        private int[] parents;
        private int[] rank;

        public DisjointSet(int n) {
            parents = new int[n];
            rank = new int[n];
        }

        public void makeSet(int x) {
            parents[x] = x;
        }

        public int find(int x) {
            if (parents[x] == x) {
                return x;
            } else {
                parents[x] = find(parents[x]);
                return parents[x];
            }
        }

        public void unite(int x, int y) {
            int px = find(x);
            int py = find(y);

            if (rank[px] > rank[py]) {
                parents[py] = px;
            } else {
                parents[px] = py;
                if (rank[px] == rank[py]) {
                    rank[py]++;
                }
            }
        }
    }
}
