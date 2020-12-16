package kickstart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class GoldenStone {
    private static final int N = 100;
    private static final int M = 200;
    private static final int S = 4;

    private static class Receipt {
        private int[] income;
        private int incomeCnt;
        private int outcome;

        public Receipt(int outcome, int... income) {
            this.income = income;
            this.incomeCnt = income.length;
            this.outcome = outcome;
        }

        public boolean apply(int node, int[][] minEnergy) {
            boolean wasDecreased = false;
            int energySum = 0;
            for (int s = 0; s < incomeCnt; s++) {
                if (minEnergy[node][income[s]] == -1) {
                    return false;
                } else {
                    energySum += minEnergy[node][income[s]];
                }
            }

            if (minEnergy[node][outcome] == -1 || minEnergy[node][outcome] > energySum) {
                minEnergy[node][outcome] = energySum;
                wasDecreased = true;
            }

            return wasDecreased;
        }
    }

    private static class Node {
        private int num;
        private Set<Node> neighbours = new HashSet<>();

        public Node(int num) {
            this.num = num;
        }

        public void addNeighbour(Node neigbour) {
            neighbours.add(neigbour);
        }
    }

    public static void main(String[] args) {
        while (true) {
            Node[] nodes = new Node[N];
            for (int i = 0; i < N; i++) {
                nodes[i] = new Node(i);
            }

            for (int i = 1; i < N; i++) {
                int neighbIndex = (int) (Math.random() * i);
                nodes[i].addNeighbour(nodes[neighbIndex]);
                nodes[neighbIndex].addNeighbour(nodes[i]);
            }

            for (int m = N - 1; m < M; m++) {
                int n1, n2;

                do {
                    n1 = (int) (Math.random() * N);
                    n2 = (int) (Math.random() * N);
                } while (n1 == n2 || nodes[n1].neighbours.contains(nodes[n2]));

                nodes[n1].addNeighbour(nodes[n2]);
                nodes[n2].addNeighbour(nodes[n1]);
            }

            int[][] minEnergy = new int[N][S];
            int[] stonePresence = new int[S];
            boolean allStones = false;

            while (!allStones) {
                for (int n = 0; n < N; n++) {
                    for (int s = 0; s < S; s++) {
                        minEnergy[n][s] = -1;
                        if (Math.random() < 0.01) {
                            minEnergy[n][s] = 0;
                            stonePresence[s] = 1;
                        }
                    }
                }

                int stonesCnt = 0;
                for (int i = 0; i < S; i++) {
                    stonesCnt += stonePresence[i];
                }
                allStones = stonesCnt == S;
            }

            for (int n = 0; n < N; n++) {
                for (int s = 0; s < S; s++) {
                    if (minEnergy[n][s] != -1) {
                        propagateMinEnergy(nodes[n], s, minEnergy);
                    }
                }
            }

            List<Receipt> receipts = new ArrayList<>();

            receipts.add(new Receipt(2, 0, 1));
            receipts.add(new Receipt(3, 1, 2));
            receipts.add(new Receipt(0, 2, 3));

            for (Receipt r : receipts) {
                for (int n = 0; n < N; n++) {
                    boolean decreased = r.apply(n, minEnergy);
                    if (decreased) {
                        propagateMinEnergy(nodes[n], r.outcome, minEnergy);
                    }
                }
            }

            Receipt r1 = receipts.get(2);
            boolean atLeastOneDecrease = false;
            for (int n = 0; n < N; n++) {
                boolean decreased = r1.apply(n, minEnergy);
                if (decreased) {
                    propagateMinEnergy(nodes[n], r1.outcome, minEnergy);
                    atLeastOneDecrease = true;
                }
            }

            System.out.println(atLeastOneDecrease);
            if (atLeastOneDecrease) {
                throw new RuntimeException();
            }
        }
    }

    private static boolean propagateMinEnergy(Node node, int stone, int[][] minEnergy) {
        Queue<Node> q = new LinkedList<>();
        Queue<Integer> dists = new LinkedList<>();
        int[] processed = new int[N];
        q.add(node);
        dists.add(0);
        processed[node.num] = 1;

        int energy = minEnergy[node.num][stone];

        boolean wasDecreased = false;

        while (!q.isEmpty()) {
            Node nd = q.poll();
            int dist = dists.poll();

            for (Node neigh : nd.neighbours) {
                if (processed[neigh.num] == 0) {
                    processed[neigh.num] = 1;
                    q.add(neigh);
                    dists.add(dist + 1);
                    if (minEnergy[neigh.num][stone] > energy + dist + 1 || minEnergy[neigh.num][stone] == -1) {
                        minEnergy[neigh.num][stone] = energy + dist + 1;
                        wasDecreased = true;
                    }
                }
            }
        }
        return wasDecreased;
    }
}
