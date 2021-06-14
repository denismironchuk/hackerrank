package kickstart.year2020;

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
import java.util.TreeSet;

public class GoldenStoneFinalSolution {

    private static final long LIMIT = 1000000000000l;
    private static int n, m, s, r;

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

    private static class Receipt {
        private int[] income;
        private int incomeCnt;
        private int outcome;

        public Receipt(int incomeCnt) {
            this.income = new int[incomeCnt];
            this.incomeCnt = incomeCnt;
        }

        public boolean apply(int node, long[][] minEnergy) {
            boolean wasDecreased = false;
            long energySum = 0;
            for (int s = 0; s < incomeCnt; s++) {
                if (minEnergy[node][income[s]] == Long.MAX_VALUE) {
                    return false;
                } else {
                    energySum += minEnergy[node][income[s]];
                }
            }

            if (energySum > LIMIT) {
                energySum = LIMIT;
            }

            if (minEnergy[node][outcome] == Long.MAX_VALUE || minEnergy[node][outcome] > energySum) {
                minEnergy[node][outcome] = energySum;
                wasDecreased = true;
            }

            return wasDecreased;
        }
    }

    private static class State implements Comparable<State> {
        private long energy;
        private int nodeNum;
        private int stoneNum;

        public State(long energy, int nodeNum, int stoneNum) {
            this.energy = energy;
            this.nodeNum = nodeNum;
            this.stoneNum = stoneNum;
        }

        @Override
        public int compareTo(State o) {
            int energyCompare = Long.compare(energy, o.energy);
            if (energyCompare != 0) {
                return energyCompare;
            }

            int nodeCompare = Integer.compare(nodeNum, o.nodeNum);
            if (nodeCompare != 0) {
                return nodeCompare;
            }

            return Integer.compare(stoneNum, o.stoneNum);
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                n = Integer.parseInt(tkn1.nextToken());
                m = Integer.parseInt(tkn1.nextToken());
                s = Integer.parseInt(tkn1.nextToken());
                r = Integer.parseInt(tkn1.nextToken());
                Node[] nodes = new Node[n];
                for (int i = 0; i < n; i++) {
                    nodes[i] = new Node(i);
                }

                for (int i = 0; i < m; i++) {
                    StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
                    int v1 = Integer.parseInt(edgeTkn.nextToken()) - 1;
                    int v2 = Integer.parseInt(edgeTkn.nextToken()) - 1;
                    nodes[v1].addNeighbour(nodes[v2]);
                    nodes[v2].addNeighbour(nodes[v1]);
                }

                long[][] minEnergy = new long[n][s];

                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < s; j++) {
                        minEnergy[i][j] = Long.MAX_VALUE;
                    }
                }

                TreeSet<State> prioritySet = new TreeSet<>();

                for (int i = 0; i < n; i++) {
                    StringTokenizer stoneTkn = new StringTokenizer(br.readLine());
                    int stoneCnt = Integer.parseInt(stoneTkn.nextToken());
                    for (int j = 0; j < stoneCnt; j++) {
                        int stoneNum = Integer.parseInt(stoneTkn.nextToken()) - 1;
                        minEnergy[i][stoneNum] = 0;
                    }
                }

                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < s; j++) {
                        prioritySet.add(new State(minEnergy[i][j], i, j));
                    }
                }

                List<Receipt> receipts = new ArrayList<>();
                Map<Integer, Set<Receipt>> receiptsByIncomeStone = new HashMap<>();
                for (int i = 0; i < r; i++) {
                    StringTokenizer receiptTkn = new StringTokenizer(br.readLine());
                    int cnt = Integer.parseInt(receiptTkn.nextToken());
                    Receipt receipt = new Receipt(cnt);
                    for (int j = 0; j < cnt; j++) {
                        receipt.income[j] = Integer.parseInt(receiptTkn.nextToken()) - 1;
                        Set<Receipt> receiptsByStone = receiptsByIncomeStone.get(receipt.income[j]);
                        if (receiptsByStone == null) {
                            receiptsByStone = new HashSet<>();
                        }
                        receiptsByStone.add(receipt);
                        receiptsByIncomeStone.put(receipt.income[j], receiptsByStone);
                    }
                    receipt.outcome = Integer.parseInt(receiptTkn.nextToken()) - 1;
                    receipts.add(receipt);
                }

                int[][] processed = new int[n][s];

                while (!prioritySet.isEmpty()) {
                    State curState = prioritySet.pollFirst();
                    int stone = curState.stoneNum;
                    Node node = nodes[curState.nodeNum];
                    processed[curState.nodeNum][curState.stoneNum] = 1;
                    long curEnergy = curState.energy;
                    for (Node neigh : node.neighbours) {
                        if (processed[neigh.num][stone] == 1) {
                            continue;
                        }
                        State st = new State(minEnergy[neigh.num][stone], neigh.num, stone);
                        prioritySet.remove(st);
                        long newEnergy = Math.min(minEnergy[neigh.num][stone], curEnergy + 1);
                        if (newEnergy < minEnergy[neigh.num][stone]) {
                            minEnergy[neigh.num][stone] = newEnergy;
                        }
                        prioritySet.add(new State(minEnergy[neigh.num][stone], neigh.num, stone));
                    }
                    Set<Receipt> receiptsByStone = receiptsByIncomeStone.get(stone);
                    if (null != receiptsByStone) {
                        for (Receipt receipt : receiptsByStone) {
                            if (processed[curState.nodeNum][receipt.outcome] == 1) {
                                continue;
                            }
                            State st = new State(minEnergy[curState.nodeNum][receipt.outcome], curState.nodeNum, receipt.outcome);
                            prioritySet.remove(st);
                            receipt.apply(curState.nodeNum, minEnergy);
                            prioritySet.add(new State(minEnergy[curState.nodeNum][receipt.outcome], curState.nodeNum, receipt.outcome));
                        }
                    }
                }

                long res = Long.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    if (minEnergy[i][0] < res) {
                        res = minEnergy[i][0];
                    }
                }

                if (res == LIMIT) {
                    res = -1;
                }

                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }
}
