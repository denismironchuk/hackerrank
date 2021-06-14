package kickstart.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;

public class GoldenStoneSimpleSolution {

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

        List<Receipt> ingoingReceipts = new ArrayList<>();

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

        public boolean isIngoingReceipt(Receipt receipt) {
            for (int stone : income) {
                if (stone == receipt.outcome) {
                    return true;
                }
            }
            return false;
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

                for (int i = 0; i < n; i++) {
                    StringTokenizer stoneTkn = new StringTokenizer(br.readLine());
                    int stoneCnt = Integer.parseInt(stoneTkn.nextToken());
                    for (int j = 0; j < stoneCnt; j++) {
                        int stoneNum = Integer.parseInt(stoneTkn.nextToken()) - 1;
                        minEnergy[i][stoneNum] = 0;
                    }
                }

                List<Receipt> receipts = new ArrayList<>();
                for (int i = 0; i < r; i++) {
                    StringTokenizer receiptTkn = new StringTokenizer(br.readLine());
                    int cnt = Integer.parseInt(receiptTkn.nextToken());
                    Receipt receipt = new Receipt(cnt);
                    for (int j = 0; j < cnt; j++) {
                        receipt.income[j] = Integer.parseInt(receiptTkn.nextToken()) - 1;
                    }
                    receipt.outcome = Integer.parseInt(receiptTkn.nextToken()) - 1;
                    receipts.add(receipt);
                }

                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < s; j++) {
                        if (minEnergy[i][j] != Long.MAX_VALUE) {
                            propagateMinEnergy(nodes[i], j, minEnergy);
                        }
                    }
                }

                for (Receipt receipt : buildReceiptsOrder(receipts)) {
                    for (int i = 0; i < n; i++) {
                        boolean decreased = receipt.apply(i, minEnergy);
                        if (decreased) {
                            propagateMinEnergy(nodes[i], receipt.outcome, minEnergy);
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

    private static List<Receipt> buildReceiptsOrder(List<Receipt> receipts) {
        for (Receipt rec1 : receipts) {
            for (Receipt rec2 : receipts) {
                if (rec1 == rec2) {
                    continue;
                }
                if (rec1.isIngoingReceipt(rec2)) {
                    rec1.ingoingReceipts.add(rec2);
                }
            }
        }

        Queue<Receipt> receiptsQueue = new LinkedList<>();
        Set<Receipt> processed = new HashSet<>();
        List<Receipt> inversedProcessList = new ArrayList<>();

        for (Receipt receipt : receipts) {
            if (receipt.outcome == 0) {
                receiptsQueue.add(receipt);
                processed.add(receipt);
            }
        }

        while (!receiptsQueue.isEmpty()) {
            Receipt receipt = receiptsQueue.poll();
            inversedProcessList.add(receipt);
            for (Receipt ingoing : receipt.ingoingReceipts) {
                if (!processed.contains(ingoing)) {
                    processed.add(ingoing);
                    receiptsQueue.add(ingoing);
                }
            }
        }

        List<Receipt> processList = new ArrayList<>();
        for (int i = inversedProcessList.size() - 1; i >= 0; i--) {
            processList.add(inversedProcessList.get(i));
        }

        return processList;
    }

    private static boolean propagateMinEnergy(Node node, int stone, long[][] minEnergy) {
        Queue<Node> q = new LinkedList<>();
        long[] dists = new long[n];
        int[] processed = new int[n];
        q.add(node);
        dists[node.num] = 0;
        processed[node.num] = 1;

        long energy = minEnergy[node.num][stone];

        boolean wasDecreased = false;

        while (!q.isEmpty()) {
            Node nd = q.poll();
            long dist = dists[nd.num];

            for (Node neigh : nd.neighbours) {
                if (processed[neigh.num] == 0) {
                    processed[neigh.num] = 1;
                    q.add(neigh);
                    dists[neigh.num] = dist + 1;
                    long newEnergy = energy + dist + 1;
                    if (newEnergy > LIMIT) {
                        newEnergy = LIMIT;
                    }
                    if (minEnergy[neigh.num][stone] > newEnergy || minEnergy[neigh.num][stone] == Long.MAX_VALUE) {
                        minEnergy[neigh.num][stone] = newEnergy;
                        wasDecreased = true;
                    }
                }
            }
        }
        return wasDecreased;
    }
}
