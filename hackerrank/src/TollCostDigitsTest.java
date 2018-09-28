import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Denis_Mironchuk on 9/28/2018.
 */
public class TollCostDigitsTest {
    private static int COST_LIMIT = 10;

    static class Node {
        private int num;
        private Map<Integer, Integer> costs = new HashMap<>();

        public Node(final int num) {
            this.num = num;
        }

        public void addCost(Node neigh, int cost) {
            costs.put(neigh.num, cost);
        }

        public int getNum() {
            return num;
        }

        public Map<Integer, Integer> getCosts() {
            return costs;
        }
    }

    public static void main(String[] args) {
        while (true) {
            int n = 1000;
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

            long[] pairsTriv = new long[COST_LIMIT];

            Date start1 = new Date();
            for (int i = 0; i < n; i++) {
                long[] newPairs = countPairsSimple(nodes[i], new int[n], nodes);

                for (int j = 0; j < COST_LIMIT; j++) {
                    pairsTriv[j] += newPairs[j];
                }
            }
            Date end1 = new Date();
            //System.out.println(end1.getTime() - start1.getTime() + "ms");

            long[] allPairsOpt = new long[COST_LIMIT];
            Date start2 = new Date();
            countPairsOptimal(nodes[0], nodes, new int[n], allPairsOpt, new long[COST_LIMIT], new long[COST_LIMIT]);
            Date end2 = new Date();

            //System.out.println(end2.getTime() - start2.getTime() + "ms");

            int sum = 0;
            StringBuilder res = new StringBuilder();
            for (int i = 0; i < COST_LIMIT; i++) {
                res.append(allPairsOpt[i]).append(" ");
                sum += allPairsOpt[i];
                if (pairsTriv[i] != allPairsOpt[i]) {
                    throw new RuntimeException();
                }
            }
            System.out.println(sum + " = " + res.toString());
        }
    }

    private static long[] countPairsSimple(Node nd, int[] processed, Node[] nodes) {
        processed[nd.getNum()] = 1;
        long[] pairs = new long[COST_LIMIT];

        for (Map.Entry<Integer, Integer> entry : nd.getCosts().entrySet()) {
            int nextNum = entry.getKey();
            int cost = entry.getValue();

            if (processed[nextNum] == 0) {
                long[] newPairs = countPairsSimple(nodes[nextNum], processed, nodes);

                pairs[cost % 10]++;

                for (int i = 0; i < COST_LIMIT; i++) {
                    pairs[(i + cost) % COST_LIMIT] += newPairs[i];
                }
            }
        }

        return pairs;
    }

    private static void countPairsOptimal(Node nd, Node[] tree, int[] processed, long[] allPairs, long[] ingoing, long[] outgoing) {
        processed[nd.getNum()] = 1;

        for (Map.Entry<Integer, Integer> entry : nd.getCosts().entrySet()) {
            int nodeNum = entry.getKey();
            Integer firstCost = entry.getValue();

            if (processed[nodeNum] == 0) {
                long[] allPairsNew = new long[COST_LIMIT];
                long[] ingoingNew = new long[COST_LIMIT];
                long[] outgoingNew = new long[COST_LIMIT];

                countPairsOptimal(tree[nodeNum], tree, processed, allPairsNew, ingoingNew, outgoingNew);
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
