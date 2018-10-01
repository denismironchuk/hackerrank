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

        long[] allPairs = new long[COST_LIMIT];
        long[] ingoing = new long[COST_LIMIT];
        long[] outgoing = new long[COST_LIMIT];

        private Node parent;

        private Set<Integer> cyclesResult = new HashSet<>();

        private Set<Integer> pairsForLca = new HashSet<>();

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

        public long[] getAllPairs() {
            return allPairs;
        }

        public long[] getIngoing() {
            return ingoing;
        }

        public long[] getOutgoing() {
            return outgoing;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(final Node parent) {
            this.parent = parent;
        }

        public Set<Integer> getCyclesResult() {
            return cyclesResult;
        }

        public void setCyclesResult(final Set<Integer> cyclesResult) {
            this.cyclesResult = cyclesResult;
        }

        public Set<Integer> getPairsForLca() {
            return pairsForLca;
        }

        public void setPairsForLca(final Set<Integer> pairsForLca) {
            this.pairsForLca = pairsForLca;
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

        int n = 100000;
        Node[] nodes = generateGraph(n);
        addEdges(100000, nodes);

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
        int[] procForCountPairs = new int[n];
        DisjointSet dSet = new DisjointSet(n);

        for (int i = 0; i < n; i++) {
            if (processed[i] == 0) {
                deepSearchNoRecur(graph, tree, graph[i], processed);
                countPairsInTreeNoRecur(tree[i], tree, procForCountPairs);
                getCyclesNoRecur(tree[i], procForCycles, ancestors, black,
                        dSet, graph, tree);

                combineResultsWithCycles(resultPairs, tree[i].getAllPairs(), tree[i].getCyclesResult());
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

    private static void getCyclesNoRecur(Node nd, int[] proc, int[] anc, int[] black, DisjointSet dSet, Node[] graph, Node[] tree) {
        Stack<Node> stack = new Stack<>();
        stack.push(nd);

        while (!stack.isEmpty()) {
            Node currNode = stack.peek();
            int currNodeNum = currNode.getNum();
            if (proc[currNodeNum] == 0) {
                proc[currNodeNum] = 1;
                dSet.makeSet(currNodeNum);
                anc[dSet.find(currNodeNum)] = currNodeNum;

                for (Map.Entry<Integer, List<Integer>> entry : currNode.getCosts().entrySet()) {
                    int childNum = entry.getKey();
                    Node child = tree[childNum];
                    if (child != currNode.getParent()) {
                        stack.push(child);
                    }
                }
            } else {
                stack.pop();
                black[currNodeNum] = 1;

                for (Map.Entry<Integer, List<Integer>> entry : currNode.getCosts().entrySet()) {
                    int childNum = entry.getKey();
                    Node child = tree[childNum];
                    if (child != currNode.getParent()) {
                        currNode.getCyclesResult().addAll(child.getCyclesResult());
                        dSet.unite(dSet.find(currNodeNum), dSet.find(childNum));
                        anc[dSet.find(currNodeNum)] = currNodeNum;
                    }
                }

                for (Map.Entry<Integer, List<Integer>> entry : graph[currNodeNum].getCosts().entrySet()) {
                    int pair = entry.getKey();

                    if (black[pair] == 1) {
                        int lca = anc[dSet.find(pair)];

                        int cycleLen = (tree[currNodeNum].getPathFromRootCost() - tree[lca].getPathFromRootCost() + COST_LIMIT) % COST_LIMIT;
                        cycleLen += COST_LIMIT - ((tree[pair].getPathFromRootCost() - tree[lca].getPathFromRootCost() + COST_LIMIT) % COST_LIMIT);

                        for (Integer pairCost : entry.getValue()) {
                            int cycleRes = (cycleLen + pairCost) % COST_LIMIT;
                            currNode.getCyclesResult().add(cycleRes);

                            boolean hasOdd = currNode.getCyclesResult().contains(1) || currNode.getCyclesResult().contains(3) || currNode.getCyclesResult().contains(7) || currNode.getCyclesResult().contains(9);
                            boolean hasEven = currNode.getCyclesResult().contains(2) || currNode.getCyclesResult().contains(4) || currNode.getCyclesResult().contains(6) || currNode.getCyclesResult().contains(8);
                            boolean hasFive = currNode.getCyclesResult().contains(5);

                            if (hasOdd || (hasEven && hasFive)) {
                                nd.getCyclesResult().add(1);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void deepSearchNoRecur(Node[] graph, Node[] tree, Node nd, int[] processed) {
        Stack<Node> stack = new Stack<>();
        stack.push(nd);
        processed[nd.getNum()] = 1;

        while (!stack.isEmpty()) {
            Node current = stack.pop();
            int currNum = current.getNum();

            for (Map.Entry<Integer, List<Integer>> entry : current.getCosts().entrySet()) {
                int childNum = entry.getKey();
                List<Integer> costs = entry.getValue();
                Integer firstCost = costs.get(0);

                if (processed[childNum] == 0) {
                    tree[currNum].addCost(tree[childNum], firstCost);
                    tree[childNum].addCost(tree[currNum], COST_LIMIT - firstCost);

                    tree[childNum].setPathFromRootCost((tree[currNum].getPathFromRootCost() + firstCost) % COST_LIMIT);
                    tree[childNum].setParent(tree[currNum]);

                    processed[childNum] = 1;
                    stack.push(graph[childNum]);
                }
            }
        }
    }

    private static void countPairsInTreeNoRecur(Node nd, Node[] tree, int[] processed) {
        Stack<Node> stack = new Stack<>();
        stack.push(nd);

        while (!stack.isEmpty()) {
            Node currNode = stack.peek();
            int currNodeNum = currNode.getNum();
            if (processed[currNodeNum] == 0) {
                processed[currNodeNum] = 1;

                for (Map.Entry<Integer, List<Integer>> entry : currNode.getCosts().entrySet()) {
                    int nextNode = entry.getKey();

                    if (tree[nextNode] != currNode.getParent()) {
                        stack.add(tree[nextNode]);
                    }
                }
            } else {
                stack.pop();

                for (Map.Entry<Integer, List<Integer>> entry : currNode.getCosts().entrySet()) {
                    int nextNodeNum = entry.getKey();
                    Node nextNode = tree[nextNodeNum];
                    List<Integer> costs = entry.getValue();
                    Integer firstCost = costs.get(0);

                    if (nextNode != currNode.getParent()) {
                        combine(currNode.getAllPairs(), currNode.getIngoing(), currNode.getOutgoing(),
                                nextNode.getAllPairs(), nextNode.getIngoing(), nextNode.getOutgoing(),
                                COST_LIMIT - firstCost, firstCost);
                    }
                }
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
