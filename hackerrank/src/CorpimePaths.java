import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class CorpimePaths {
    public static final int PRIME_FACTOR_LIMIT = 3;

    public static class Node {
        private int num;
        private int value;
        private Set<Node> neighbours = new HashSet<>();
        private int factSize = 0;
        private int[] factor = new int[PRIME_FACTOR_LIMIT];
        private int cnt = 0;

        private int index1;
        private int index2;

        public Node(final int num, final int value) {
            this.num = num;
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(final int value) {
            this.value = value;
        }

        public void addNeighbour(Node neighbour) {
            neighbours.add(neighbour);
        }

        public Set<Node> getNeighbours() {
            return neighbours;
        }

        public int getNum() {
            return num;
        }

        public int getIndex1() {
            return index1;
        }

        public void setIndex1(final int index1) {
            this.index1 = index1;
        }

        public int getIndex2() {
            return index2;
        }

        public void setIndex2(final int index2) {
            this.index2 = index2;
        }

        public void initFactor(int[] primes) {
            for (int prime : primes) {
                if (value % prime == 0) {
                    factor[factSize] = prime;
                    factSize++;
                }

                while (value % prime == 0) {
                    value /= prime;
                }

                if (value == 1) {
                    break;
                }
            }

            if (value != 1) {
                factor[factSize] = value;
                factSize++;
            }
        }

        public int getFactSize() {
            return factSize;
        }

        public int[] getFactor() {
            return factor;
        }
    }

    public static class NodesPair {
        private int order;
        private Node node1;
        private Node node2;
        private Node lca;

        public NodesPair(final Node node1, final Node node2, final int order) {
            this.node1 = node1;
            this.node2 = node2;
            this.order = order;
        }

        public Node getNode1() {
            return node1;
        }

        public void setNode1(final Node node1) {
            this.node1 = node1;
        }

        public Node getNode2() {
            return node2;
        }

        public void setNode2(final Node node2) {
            this.node2 = node2;
        }

        public Node getLca() {
            return lca;
        }

        public void setLca(final Node lca) {
            this.lca = lca;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(final int order) {
            this.order = order;
        }
    }

    public static class Range {
        private int order;
        private int start;
        private int end;
        private Node lca;
        private long res;

        public Range(final int start, final int end, final int order) {
            this.start = start;
            this.end = end;
            this.order = order;
        }

        public Range(final int start, final int end, final Node lca, final int order) {
            this.start = start;
            this.end = end;
            this.lca = lca;
            this.order = order;
        }

        public int getStart() {
            return start;
        }

        public void setStart(final int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(final int end) {
            this.end = end;
        }

        public Node getLca() {
            return lca;
        }

        public void setLca(final Node lca) {
            this.lca = lca;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(final int order) {
            this.order = order;
        }

        public long getRes() {
            return res;
        }

        public void setRes(final long res) {
            this.res = res;
        }
    }

    public static List<Integer> generate(int numbers) {
        int[] isPrime = new int[numbers];
        int[] processed = new int[numbers];

        List<Integer> primes = new ArrayList<>();

        for (int i = 2; i < numbers; i++) {
            if (processed[i] == 0) {
                processed[i] = 1;
                isPrime[i] = 1;
                primes.add(i);

                for (int j = 2; i * j < numbers; j++) {
                    processed[i * j] = 1;
                }
            }
        }

        return primes;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        int q = Integer.parseInt(tkn1.nextToken());

        Node[] nodes = new Node[n];

        StringTokenizer valTkn = new StringTokenizer(br.readLine());
        int maxVal = -1;

        for (int i = 0; i < n; i++) {
            int val = Integer.parseInt(valTkn.nextToken());
            nodes[i] = new Node(i, val);
            if (val > maxVal) {
                maxVal = val;
            }
        }

        int[] primes = getPrimesForFactor(maxVal);

        for (Node nd : nodes) {
            nd.initFactor(primes);
        }

        for (int i = 0; i < n - 1; i++) {
            StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
            int v1 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int v2 = Integer.parseInt(edgeTkn.nextToken()) - 1;

            nodes[v1].addNeighbour(nodes[v2]);
            nodes[v2].addNeighbour(nodes[v1]);
        }

        Map<Integer, List<NodesPair>> pairsMap = new HashMap<>();

        for (int i = 0; i < q; i++) {
            StringTokenizer pairTkn = new StringTokenizer(br.readLine());
            int num1 = Integer.parseInt(pairTkn.nextToken()) - 1;
            int num2 = Integer.parseInt(pairTkn.nextToken()) - 1;

            Node v1 = nodes[num1];
            Node v2 = nodes[num2];

            NodesPair np1 = new NodesPair(v1, v2, i);
            NodesPair np2 = new NodesPair(v2, v1, i);

            List<NodesPair> numPairs1 = pairsMap.get(num1);
            if (null == numPairs1) {
                numPairs1 = new ArrayList<>();
            }

            List<NodesPair> numPairs2 = pairsMap.get(num2);
            if (null == numPairs2) {
                numPairs2 = new ArrayList<>();
            }

            numPairs1.add(np1);
            numPairs2.add(np2);

            pairsMap.put(num1, numPairs1);
            pairsMap.put(num2, numPairs2);
        }

        int sequenceLen = 2 * n;
        Node[] sequence = new Node[sequenceLen];
        DisjointSet dSet = new DisjointSet(n);
        buildSequence(sequence, nodes[0], new int[n], 0, dSet, new int[n], new int[n], pairsMap, nodes);
        List<Range> ranges = generateRanges(pairsMap);

        ranges.sort(new Comparator<Range>() {
            @Override
            public int compare(final Range o1, final Range o2) {
                int group1 = (int)((double)o1.getStart() / sequenceLen);
                int group2 = (int)((double)o2.getStart() / sequenceLen);

                if (group1 == group2) {
                    return Integer.compare(o1.getEnd(), o2.getEnd());
                } else {
                    return Integer.compare(o1.getStart(), o2.getStart());
                }
            }
        });

        countRanges(sequence, ranges, new int[maxVal + 1]);

        ranges.sort(Comparator.comparingInt(Range::getOrder));

        for (Range range : ranges) {
            System.out.println(range.getRes());
        }
    }

    private static void countRanges(Node[] sequence, List<Range> ranges, int[] dynMap) {
        int startPos = 0;
        int endPos = 0;

        long resPairs = 0;
        addMultsCombinations(dynMap, sequence[0].getFactor(), sequence[0].getFactSize());
        sequence[0].cnt = 1;

        int len = 1;

        for (Range range : ranges) {
            while (range.getStart() < startPos) {
                startPos--;

                if (sequence[startPos].cnt == 0) {
                    resPairs += addNumberToSequence(sequence[startPos].getFactor(), sequence[startPos].getFactSize(), dynMap, len, true);
                    len++;
                } else {
                    resPairs -= removeNumberFromSequence(sequence[startPos].getFactor(), sequence[startPos].getFactSize(), dynMap, len);
                    len--;
                }

                sequence[startPos].cnt++;
            }

            while (range.getEnd() < endPos) {
                if (sequence[endPos].cnt == 1) {
                    resPairs -= removeNumberFromSequence(sequence[endPos].getFactor(), sequence[endPos].getFactSize(), dynMap, len);
                    len--;
                } else {
                    resPairs += addNumberToSequence(sequence[endPos].getFactor(), sequence[endPos].getFactSize(), dynMap, len, true);
                    len++;
                }
                sequence[endPos].cnt--;
                endPos--;
            }

            while (range.getEnd() > endPos) {
                endPos++;
                if (sequence[endPos].cnt == 0) {
                    resPairs += addNumberToSequence(sequence[endPos].getFactor(), sequence[endPos].getFactSize(), dynMap, len, true);
                    len++;
                } else {
                    resPairs -= removeNumberFromSequence(sequence[endPos].getFactor(), sequence[endPos].getFactSize(), dynMap, len);
                    len--;
                }
                sequence[endPos].cnt++;
            }

            while (range.getStart() > startPos) {
                if (sequence[startPos].cnt == 1) {
                    resPairs -= removeNumberFromSequence(sequence[startPos].getFactor(), sequence[startPos].getFactSize(), dynMap, len);
                    len--;
                } else {
                    resPairs += addNumberToSequence(sequence[startPos].getFactor(), sequence[startPos].getFactSize(), dynMap, len, true);
                    len++;
                }

                sequence[startPos].cnt--;
                startPos++;
            }

            if (range.getLca() != null) {
                Node lca = range.getLca();
                long addPairs = addNumberToSequence(lca.getFactor(), lca.getFactSize(), dynMap, len, false);
                range.setRes(resPairs + addPairs);
            } else {
                range.setRes(resPairs);
            }
        }
    }

    //sequenceLen - does not includes new element
    private static long addNumberToSequence(int[] factor, int factorSize, int[] dynMap, int sequenceLen, boolean addCombinations) {
        int common = getCommonMults(dynMap, factor, factorSize);

        if (addCombinations) {
            addMultsCombinations(dynMap, factor, factorSize);
        }

        return (sequenceLen - common);
    }

    //sequenceLen - includes removed element
    private static long removeNumberFromSequence(int[] factor, int factorSize, int[] dynMap, int sequenceLen) {
        removeMultsCombinations(dynMap, factor, factorSize);
        int common = getCommonMults(dynMap, factor, factorSize);

        return (sequenceLen - common - 1);
    }

    private static int getCommonMults(final int[] dynMap, final int[] fact, final int size) {
        int common = 0;

        if (size == 1) {
            common = dynMap[fact[0]];
        } else if (size == 2) {
            int a = fact[0];
            int b = fact[1];

            common = (dynMap[a] + dynMap[b] - dynMap[a*b]);
        } else if (size == 3) {
            int a = fact[0];
            int b = fact[1];
            int c = fact[2];

            common = dynMap[a] + dynMap[b] + dynMap[c] - (dynMap[a * b] + dynMap[b * c] + dynMap[a * c]) + dynMap[a * b * c];
        }

        return common;
    }

    private static void addMultsCombinations(int[] dynMap, int[] fact, int size){
        manageMultsCombinations(dynMap, fact, size, 1);
    }

    private static void removeMultsCombinations(int[] dynMap, int[] fact, int size){
        manageMultsCombinations(dynMap, fact, size, -1);
    }

    private static void manageMultsCombinations(int[] dynMap, int[] fact, int size, int inc) {
        if (size == 1) {
            dynMap[fact[0]] += inc;
        } else if (size == 2) {
            dynMap[fact[0] * fact[1]] += inc;
            dynMap[fact[0]] += inc;
            dynMap[fact[1]] += inc;
        } else if (size == 3) {
            dynMap[fact[0] * fact[1] * fact[2]]+=inc;

            dynMap[fact[0]] += inc;
            dynMap[fact[1]] += inc;
            dynMap[fact[2]] += inc;

            dynMap[fact[0] * fact[1]] += inc;
            dynMap[fact[0] * fact[2]] += inc;
            dynMap[fact[1] * fact[2]] += inc;
        }
    }

    private static List<Range> generateRanges(final Map<Integer, List<NodesPair>> pairsMap) {
        List<Range> ranges = new ArrayList<>();

        for (Map.Entry<Integer, List<NodesPair>> entry : pairsMap.entrySet()) {
            for (NodesPair pair : entry.getValue()) {
                if (null != pair.getLca()) {
                    if (pair.getNode1() == pair.getLca()) {
                        int start = pair.getNode1().getIndex1();
                        int end = pair.getNode2().getIndex1();
                        ranges.add(new Range(start, end, pair.getOrder()));
                    } else if (pair.getNode2() == pair.getLca()) {
                        int start = pair.getNode2().getIndex1();
                        int end = pair.getNode1().getIndex1();
                        ranges.add(new Range(start, end, pair.getOrder()));
                    } else {
                        int start = pair.getNode2().getIndex2();
                        int end = pair.getNode1().getIndex1();
                        ranges.add(new Range(start, end, pair.getLca(), pair.getOrder()));
                    }
                }
            }
        }

        return ranges;
    }

    private static int buildSequence(Node[] sequence, Node nd, int[] processed, int index, DisjointSet dSet, int[] anc, int[] black, Map<Integer, List<NodesPair>> pairsMap, Node[] nodes) {
        processed[nd.getNum()] = 1;
        sequence[index] = nd;
        nd.setIndex1(index);
        index++;
        dSet.makeSet(nd.getNum());
        anc[dSet.find(nd.getNum())] = nd.getNum();

        for (Node neigh : nd.getNeighbours()) {
            if (processed[neigh.getNum()] == 1) {
                continue;
            }

            index = buildSequence(sequence, neigh, processed, index, dSet, anc, black, pairsMap, nodes);

            dSet.unite(dSet.find(nd.getNum()), dSet.find(neigh.getNum()));
            anc[dSet.find(nd.getNum())] = nd.getNum();
        }

        sequence[index] = nd;
        nd.setIndex2(index);
        index++;

        black[nd.getNum()] = 1;

        List<NodesPair> pairs = pairsMap.get(nd.getNum());
        if (null != pairs) {
            for (NodesPair pair : pairs) {
                if (black[pair.getNode2().getNum()] == 1) {
                    int lcaNum = anc[dSet.find(pair.getNode2().getNum())];
                    pair.setLca(nodes[lcaNum]);
                }
            }
        }

        return index;
    }

    private static int[] getPrimesForFactor(final int maxVal) {
        List<Integer> primesForFactor = generate(1 + (int) Math.sqrt(maxVal));
        int[] primesArray = new int[primesForFactor.size()];
        int index = 0;
        for (int prime : primesForFactor) {
            primesArray[index] = prime;
            index++;
        }

        return primesArray;
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
