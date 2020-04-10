package codejam.year2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map.Entry;

/*
test data
1
7
5 6
3 1
4 1
1 2
6 7
7 2
1 4
1 1 1 1 1 1 1
 */

public class Contransmutations {
    private static final int MOD = 1000000007;

    private static int m;
    private static Node[] metals;
    private static int[] grams;

    private static class Node {
        private int num;
        private Map<Node, Integer> outs = new HashMap<>();
        private Map<Node, Integer> ins = new HashMap<>();

        public Node(int num) {
            this.num = num;
        }

        public void addOutNode(Node nd) {
            addOutNode(nd, 1);
        }

        private void addInNode(Node nd) {
            addInNode(nd, 1);
        }

        public void addOutNode(Node nd, int amnt) {
            int cnt = 0;
            if (outs.containsKey(nd)) {
                cnt = outs.get(nd);
            }
            outs.put(nd, cnt + amnt);
            nd.addInNode(this, amnt);
        }

        private void addInNode(Node nd, int amnt) {
            int cnt = 0;
            if (ins.containsKey(nd)) {
                cnt = ins.get(nd);
            }
            ins.put(nd, cnt + amnt);
        }

        @Override
        public String toString() {
            return "num=" + num;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        for (int t = 1; t <= T; t++) {
            m = Integer.parseInt(br.readLine());
            metals = new Node[m + 1];
            for (int i = 1; i <= m; i++) {
                metals[i] = new Node(i);
            }

            for (int i = 1; i <= m; i++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                int r1 = Integer.parseInt(tkn.nextToken());
                int r2 = Integer.parseInt(tkn.nextToken());

                metals[i].addOutNode(metals[r1]);
                metals[i].addOutNode(metals[r2]);
            }

            grams = new int[m + 1];
            StringTokenizer gramTkn = new StringTokenizer(br.readLine());
            for (int i = 1; i <=m ;i++) {
                grams[i] = Integer.parseInt(gramTkn.nextToken());
            }

            removeNonReachingFirst();
            removeEmptyMetals();

            if (metals[1] == null) {
                System.out.printf("Case #%s: %s\n", t, 0);
            } else {
                Map<Node, Node> cycle = getCycle(metals[1], new LinkedList<>());
                if (cycle == null) {
                    long[] processedWeight = new long[m + 1];
                    Arrays.fill(processedWeight, -1);
                    countMaxWeight(metals[1], processedWeight);
                    System.out.printf("Case #%s: %s\n", t, processedWeight[1]);
                } else {
                    if (!cycle.containsKey(metals[1])) {
                        System.out.printf("Case #%s: %s\n", t, "UNBOUNDED");
                    } else {
                        collapseCycle(cycle);
                        Map<Node, Node> cycle2 = getCycle(metals[1], new LinkedList<>());
                        if (null == cycle2) {
                            long[] processedWeight = new long[m + 1];
                            Arrays.fill(processedWeight, -1);
                            countMaxWeight(metals[1], processedWeight);
                            System.out.printf("Case #%s: %s\n", t, processedWeight[1]);
                        } else {
                            System.out.printf("Case #%s: %s\n", t, "UNBOUNDED");
                        }
                    }
                }
            }
        }
    }

    private static void countMaxWeight(Node nd, long[] processedWeight) {
        processedWeight[nd.num] = grams[nd.num];

        for (Entry<Node, Integer> inEntry : nd.ins.entrySet()) {
            Node in = inEntry.getKey();
            long amnt = inEntry.getValue();

            if (processedWeight[in.num] == -1) {
                countMaxWeight(in, processedWeight);
            }

            processedWeight[nd.num] = (processedWeight[nd.num] + (amnt * processedWeight[in.num]) % MOD ) % MOD;
        }
    }

    private static void collapseCycle(Map<Node, Node> cycle) {
        int weight = 0;
        Node initNode = new Node(1);

        for (Entry<Node, Node> entry : cycle.entrySet()) {
            Node cycleNode = entry.getKey();
            Node outgoingNode = entry.getValue();

            for (Entry<Node, Integer> inEntry : outgoingNode.ins.entrySet()) {
                Node in = inEntry.getKey();
                int amnt = inEntry.getValue();

                if (in != cycleNode) {
                    Node toProc = cycle.containsKey(in) ? initNode : in;
                    toProc.addOutNode(initNode, amnt);
                } else if (in == cycleNode && amnt > 1) {
                    initNode.addOutNode(initNode, amnt - 1);
                }
            }

            for (Entry<Node, Integer> outEntry : cycleNode.outs.entrySet()) {
                Node out = outEntry.getKey();
                int amnt = outEntry.getValue();

                if (out != outgoingNode) {
                    Node toProc = cycle.containsKey(out) ? initNode : out;
                    initNode.addOutNode(toProc, amnt);
                } else if (out == outgoingNode && amnt > 1) {
                    initNode.addOutNode(initNode, amnt - 1);
                }
            }

            metals[cycleNode.num] = null;
            weight = (weight + grams[cycleNode.num]) % MOD;
        }

        for (Node nd : metals) {
            if (nd != null) {
                Iterator<Node> insItr = nd.ins.keySet().iterator();
                while (insItr.hasNext()) {
                    Node in = insItr.next();
                    if (cycle.containsKey(in)) {
                        insItr.remove();
                    }
                }

                Iterator<Node> outsItr = nd.outs.keySet().iterator();
                while (outsItr.hasNext()) {
                    Node out = outsItr.next();
                    if (cycle.containsKey(out)) {
                        outsItr.remove();
                    }
                }
            }
        }

        metals[1] = initNode;
        grams[1] = weight;
    }

    private static void removeNonReachingFirst() {
        int[] processed = new int[m + 1];
        findAllIngoings(metals[1], processed);

        removeNotProcessed(processed);
    }

    private static void removeEmptyMetals() {
        int[] processed = new int[m + 1];

        for (int i = 1; i <= m; i++) {
            if (metals[i] != null && grams[i] > 0) {
                findAllOutgoings(metals[i], processed);
            }
        }

        removeNotProcessed(processed);
    }

    private static void removeNotProcessed(int[] processed) {
        for (int i = 1; i <= m; i++) {
            if (metals[i] != null && processed[i] == 0) {
                Node nd = metals[i];
                for (Node in : nd.ins.keySet()) {
                    in.outs.remove(nd);
                }
                for (Node out : nd.outs.keySet()) {
                    out.ins.remove(nd);
                }
                metals[i] = null;
            }
        }
    }

    private static void findAllIngoings(Node nd, int[] processed) {
        processed[nd.num] = 1;

        for (Node in : nd.ins.keySet()) {
            if (processed[in.num] == 0) {
                findAllIngoings(in, processed);
            }
        }
    }

    private static void findAllOutgoings(Node nd, int[] processed) {
        processed[nd.num] = 1;

        for (Node in : nd.outs.keySet()) {
            if (processed[in.num] == 0) {
                findAllOutgoings(in, processed);
            }
        }
    }

    private static Map<Node, Node> getCycle(Node nd, LinkedList<Node> path) {
        int[] processed = new int[m + 1];
        Map<Node, Node> cycle = null;
        for (int i = 1; null == cycle && i <= m; i++) {
            if (null != metals[i] && processed[i] == 0) {
                cycle = getCycle(metals[i], processed, new LinkedList<>());
            }
        }
        return cycle;
    }

    //returns list or edges represented as outgoing node -> ingoing node
    private static Map<Node, Node> getCycle(Node nd, int[] processed, LinkedList<Node> path) {
        processed[nd.num] = 1;
        path.add(nd);

        for (Node out : nd.outs.keySet()) {
            Map<Node, Node> cycle = null;

            if (processed[out.num] == 0) {
                cycle = getCycle(out, processed, path);
            } else if (processed[out.num] == 1) {
                cycle = restoreCycleNodes(path, out);
            }

            if (null != cycle) {
                return cycle;
            }
        }

        path.removeLast();
        processed[nd.num] = 2;

        return null;
    }

    private static Map<Node, Node> restoreCycleNodes(LinkedList<Node> path, Node out) {
        Map<Node, Node> cycle = new HashMap<>();

        Iterator<Node> itr = path.descendingIterator();
        Node outgoing = out;

        for (Node nd = itr.next(); nd != out; nd = itr.next()) {
            cycle.put(nd, outgoing);
            outgoing = nd;
        }

        cycle.put(out, outgoing);

        return cycle;
    }
}