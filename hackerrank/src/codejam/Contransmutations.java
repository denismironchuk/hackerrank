package codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

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
    private static int m;
    private static Node[] metals;
    private static int[] grams;

    private static class Node {
        private int num;
        private Set<Node> outs = new HashSet<>();
        private Set<Node> ins = new HashSet<>();

        public Node(int num) {
            this.num = num;
        }

        public void addOutNode(Node nd) {
            outs.add(nd);
            nd.addInNode(this);
        }

        private void addInNode(Node nd) {
            ins.add(nd);
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
                //0 value can be obtained
                return;
            } else {
                Set<Node> cycle = getCycle(metals[1], new int[m + 1], new LinkedList<>());
                System.out.println(cycle);
            }
        }
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
                for (Node in : nd.ins) {
                    in.outs.remove(nd);
                }
                for (Node out : nd.outs) {
                    out.ins.remove(nd);
                }
                metals[i] = null;
            }
        }
    }

    private static void findAllIngoings(Node nd, int[] processed) {
        processed[nd.num] = 1;

        for (Node in : nd.ins) {
            if (processed[in.num] == 0) {
                findAllIngoings(in, processed);
            }
        }
    }

    private static void findAllOutgoings(Node nd, int[] processed) {
        processed[nd.num] = 1;

        for (Node in : nd.outs) {
            if (processed[in.num] == 0) {
                findAllOutgoings(in, processed);
            }
        }
    }

    private static Set<Node> getCycle(Node nd, int[] processed, LinkedList<Node> path) {
        processed[nd.num] = 1;
        path.add(nd);

        for (Node out : nd.outs) {
            Set<Node> cycle = null;

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

    private static Set<Node> restoreCycleNodes(LinkedList<Node> path, Node out) {
        Set<Node> cycle = new HashSet<>();
        cycle.add(out);

        Iterator<Node> itr = path.descendingIterator();

        for (Node nd = itr.next(); nd != out; nd = itr.next()) {
            cycle.add(nd);
        }
        return cycle;
    }
}