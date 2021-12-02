package kickstart.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class DependentEvents {

    private static final long MOD = 1000000000 + 7;
    private static final long MIL_INVERSE = fastPow(1000000, MOD - 2);

    private static class Pair {
        private int num;
        private int u;
        private int v;
        private int lca = -1;

        public Pair(int num, int u, int v) {
            this.num = num;
            this.u = u;
            this.v = v;
        }
    }

    static class Node {
        private int num;
        private List<Node> neighbours = new ArrayList<>();

        public Node(final int num) {
            this.num = num;
        }

        public void addNeighbour(Node neigh) {
            neighbours.add(neigh);
        }

        public int getNum() {
            return num;
        }

        public List<Node> getNeighbours() {
            return neighbours;
        }
    }

    private static long fastPow(long v, long p) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return fastPow((v * v) % MOD, p / 2);
        } else {
            return (v * fastPow(v, p - 1)) % MOD;
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                int q = Integer.parseInt(tkn1.nextToken());
                int[] parents = new int[n];
                //prob[i][0] - parent event happened
                //prob[i][1] - parent event didn't happen
                long[][] probs = new long[n][2];
                long k = Long.parseLong(br.readLine());
                probs[0][0] = (k * MIL_INVERSE) % MOD;
                parents[0] = -1;
                Node[] nodes = new Node[n];
                nodes[0] = new Node(0);
                for (int i = 1; i < n; i++) {
                    StringTokenizer tkn = new StringTokenizer(br.readLine());
                    int parent = Integer.parseInt(tkn.nextToken()) - 1;
                    parents[i] = parent;
                    probs[i][0] = (Long.parseLong(tkn.nextToken()) * MIL_INVERSE) % MOD;
                    probs[i][1] = (Long.parseLong(tkn.nextToken()) * MIL_INVERSE) % MOD;
                    nodes[i] = new Node(i);
                    if (probs[i][0] == probs[i][1]) {
                        parents[i] = -1;
                    } else {
                        nodes[parents[i]].addNeighbour(nodes[i]);
                    }
                }

                long[][][] accumulativeProbs = new long[n][2][2];
                long[][][] inverseAcc = new long[n][2][2];
                int[] subtreeRoot = new int[n];
                Arrays.fill(subtreeRoot, -1);
                int treeIndex = 0;
                for (int v = 0; v < n; v++) {
                    if (subtreeRoot[v] == -1) {
                        buildProbabilityOnRoot(v, parents, probs, accumulativeProbs, subtreeRoot, treeIndex, inverseAcc);
                        treeIndex++;
                    }
                }

                List<Pair> pairs = new ArrayList<>();
                Map<Integer, List<Pair>> pairsMap = new HashMap<>();
                for (int i = 0; i < q; i++) {
                    StringTokenizer tkn = new StringTokenizer(br.readLine());
                    int u = Integer.parseInt(tkn.nextToken()) - 1;
                    int v = Integer.parseInt(tkn.nextToken()) - 1;
                    Pair pair = new Pair(i, u, v);
                    pairs.add(pair);
                    if (!pairsMap.containsKey(u)) {
                        pairsMap.put(u, new ArrayList<>());
                    }
                    if (!pairsMap.containsKey(v)) {
                        pairsMap.put(v, new ArrayList<>());
                    }
                    pairsMap.get(u).add(pair);
                    pairsMap.get(v).add(pair);
                }

                int[] processed = new int[n];
                int[] black = new int[n];
                int[] ancestors = new int[n];
                DisjointSet dSet = new DisjointSet(n);

                for (int i = 0; i < n; i++) {
                    if (processed[i] == 0) {
                        lca(nodes[i], processed, black, pairsMap, dSet, ancestors);
                    }
                }

                StringBuilder results = new StringBuilder();
                for (Pair pair : pairs) {
                    int u = pair.u;
                    int v = pair.v;
                    if (subtreeRoot[u] == subtreeRoot[v]) {
                        int root = subtreeRoot[u];
                        int lca = pair.lca;

                        if (lca == u || lca == v) {
                            int top = lca;
                            int bottom = lca == u ? v : u;

                            //matr[0][0] - parent occurred, current occurred (+ +)
                            //matr[0][1] - parent occurred, current not occurred (+ -)
                            //matr[1][0] - parent not occurred, current occurred (- +)
                            //matr[1][1] - parent not occurred, current not occurred (- -)
                            long[][] matr1 = matrMul(inverseAcc[top], accumulativeProbs[bottom]);
                            long[][] res1 = matrMul(new long[][]{{probs[root][0], (MOD + 1 - probs[root][0]) % MOD}}, accumulativeProbs[top]);

                            results.append((res1[0][0] * matr1[0][0]) % MOD).append(" ");
                        } else {
                            long[][] matrU = matrMul(inverseAcc[lca], accumulativeProbs[u]);
                            long[][] matrV = matrMul(inverseAcc[lca], accumulativeProbs[v]);
                            long[][] res1 = matrMul(new long[][]{{probs[root][0], (MOD + 1 - probs[root][0]) % MOD}}, accumulativeProbs[lca]);
                            long res = ((((res1[0][0] * matrU[0][0]) % MOD) * matrV[0][0]) % MOD + (((res1[0][1] * matrU[1][0]) % MOD) * matrV[1][0]) % MOD) % MOD;

                            results.append(res).append(" ");
                        }
                    } else {
                        int uRoot = subtreeRoot[u];
                        int vRoot = subtreeRoot[v];
                        long[][] res1 = matrMul(new long[][]{{probs[uRoot][0], (MOD + 1 - probs[uRoot][0]) % MOD}}, accumulativeProbs[u]);
                        long[][] res2 = matrMul(new long[][]{{probs[vRoot][0], (MOD + 1 - probs[vRoot][0]) % MOD}}, accumulativeProbs[v]);
                        long res = (res1[0][0]  * res2[0][0]) % MOD;
                        results.append(res).append(" ");
                    }
                }
                System.out.printf("Case #%s: %s\n", t, results);
            }
        }
    }

    private static void lca(Node nd, int[] processed, int[] black, Map<Integer, List<Pair>> pairs, DisjointSet dSet, int[] ancestors) {
        int nodeNum = nd.getNum();
        processed[nodeNum] = 1;
        dSet.makeSet(nodeNum);
        ancestors[dSet.find(nodeNum)] = nodeNum;

        for (Node child : nd.getNeighbours()) {
            int childNum = child.getNum();
            if (processed[childNum] == 0) {
                lca(child, processed, black, pairs, dSet, ancestors);
                dSet.unite(dSet.find(nodeNum), dSet.find(childNum));
                ancestors[dSet.find(nodeNum)] = nodeNum;
            }
        }
        black[nodeNum] = 1;

        if (null == pairs.get(nodeNum)) {
            return;
        }

        for (Pair pair : pairs.get(nodeNum)) {
            if (pair.u == nodeNum) {
                if (black[pair.v] == 1) {
                    pair.lca = ancestors[dSet.find(pair.v)];
                }
            } else {
                if (black[pair.u] == 1) {
                    pair.lca = ancestors[dSet.find(pair.u)];
                }
            }
        }
    }

    private static long[][] matrMul(long[][] m1, long[][] m2) {
        long[][] res = new long[m1.length][m2[0].length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m2[0].length; j++) {
                for (int k = 0; k < m2.length; k++) {
                    res[i][j] += (m1[i][k] * m2[k][j]) % MOD;
                    res[i][j] %= MOD;
                }
            }
        }
        return res;
    }

    private static void inverse(long[][] src, long[][] dest) {
        long a = src[0][0];
        long b = src[0][1];
        long c = src[1][0];
        long d = src[1][1];
        long denum = (MOD + ((a * d) % MOD) - ((b * c) % MOD)) % MOD;
        long invDenum = fastPow(denum, MOD - 2);
        dest[0][0] = (d * invDenum) % MOD;
        dest[0][1] = ((MOD - b) * invDenum) % MOD;
        dest[1][0] = ((MOD - c) * invDenum) % MOD;
        dest[1][1] = (a * invDenum) % MOD;
    }

    //Don't forget to set unity matrix to accumulativeProbs[0]
    private static int buildProbabilityOnRoot(int v, int[] parents, long[][] probs, long[][][] accumulativeProbs, int[] processed, int treeIndex, long[][][] inverseAcc) {
        if (processed[v] != -1) {
            return processed[v];
        }

        if (parents[v] == -1) {
            accumulativeProbs[v][0][0] = 1;
            accumulativeProbs[v][1][1] = 1;
            inverse(accumulativeProbs[v], inverseAcc[v]);
            processed[v] = treeIndex;
            return treeIndex;
        }

        if (processed[parents[v]] == -1) {
            buildProbabilityOnRoot(parents[v], parents, probs, accumulativeProbs, processed, treeIndex, inverseAcc);
        }

        accumulativeProbs[v] = matrMul(accumulativeProbs[parents[v]],
                new long[][] {{probs[v][0], (MOD + 1 - probs[v][0]) % MOD}, {probs[v][1], (MOD + 1 - probs[v][1]) % MOD}});
        inverse(accumulativeProbs[v], inverseAcc[v]);
        processed[v] = processed[parents[v]];
        return processed[v];
    }

    public static class DisjointSet {
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