import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class RoadsInHackerland {
    static class Node {
        private int num;
        private Map<Integer, Integer> dists = new HashMap<>();

        public Node(final int num) {
            this.num = num;
        }

        public void addNeighbour(Node neigh, int dist) {
            dists.merge(neigh.num, dist, (oldVal, newVal) -> Math.min(oldVal, newVal));
        }
    }

    static class Edge {
        private int n1;
        private int n2;
        private int dist;
        private int cnt;

        public Edge(final int n1, final int n2, final int dist) {
            this.n1 = n1;
            this.n2 = n2;
            this.dist = dist;
        }

        public Edge(int n1, int n2, int dist, int cnt) {
            this.n1 = n1;
            this.n2 = n2;
            this.dist = dist;
            this.cnt = cnt;
        }
    }

    static class TreeElem implements Comparable<TreeElem> {
        private int num;
        private int dist;

        public TreeElem(final int num, final int dist) {
            this.num = num;
            this.dist = dist;
        }

        @Override
        public int compareTo(final TreeElem o) {
            return Integer.compare(dist, o.dist);
        }
    }

    public static void main(String[] args) throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new FileReader("D:/test16.txt"));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        int m = Integer.parseInt(tkn1.nextToken());

        Node[] nodes = new Node[n];

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }

        for (int i = 0; i < m; i++) {
            StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
            int n1 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int n2 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int dist = Integer.parseInt(edgeTkn.nextToken());

            nodes[n1].addNeighbour(nodes[n2], dist);
            nodes[n2].addNeighbour(nodes[n1], dist);
        }

        List<Edge> spanningTree = createSpanningTree(nodes, n);

        Node[] spanningNodes = new Node[n];

        for (Edge edge : spanningTree) {
            if (spanningNodes[edge.n1] == null) {
                spanningNodes[edge.n1] = new Node(edge.n1);
            }
            if (spanningNodes[edge.n2] == null) {
                spanningNodes[edge.n2] = new Node(edge.n2);
            }

            spanningNodes[edge.n1].addNeighbour(spanningNodes[edge.n2], edge.dist);
            spanningNodes[edge.n2].addNeighbour(spanningNodes[edge.n1], edge.dist);
        }

        List<Edge> edges = new ArrayList<>();
        countFurtherVerts(spanningNodes[0], null, -1, new int[n], edges, spanningNodes);

        edges.sort((e1, e2) -> Integer.compare(e1.dist, e2.dist));

        int[] res = new int[edges.get(edges.size() - 1).dist + 100];

        /*BigDecimal resLong = BigDecimal.ZERO;
        for (Edge ed : edges) {
            BigDecimal edgeCnt = new BigDecimal(ed.cnt * (n - ed.cnt));
            //resLong += edgeCnt * Math.pow(2, ed.dist);
            resLong = BigDecimal.valueOf(2).pow(ed.dist).multiply(edgeCnt).add(resLong);

        }
        System.out.println(resLong);

        StringBuilder resStr = new StringBuilder();

        int i = 0;
        while (!resLong.equals(BigDecimal.ZERO)) {
            resStr.insert(0, resLong.remainder(BigDecimal.valueOf(2)));
            resLong = resLong.divide(BigDecimal.valueOf(2));
            System.out.println(i);
            i++;
        }*/

        //System.out.println(resStr.toString());

        for (Edge ed : edges) {
            long edgeCnt = ed.cnt * (n - ed.cnt);
            int resIndex = ed.dist;
            int perenos = 0;

            while (edgeCnt != 0) {
                int sum = ((int)(edgeCnt % 2) + res[resIndex] + perenos);
                res[resIndex] = sum % 2;
                perenos = sum > 1 ? 1 : 0;
                resIndex++;
                edgeCnt /= 2;
            }

            while (perenos != 0) {
                int sum = res[resIndex] + perenos;
                res[resIndex] = sum % 2;
                perenos = sum > 1 ? 1 : 0;
                resIndex++;
            }
        }

        boolean started = false;
        int i = res.length - 1;

        for (; i >= 0 && !started; i--) {
            if (res[i] == 1) {
                started = true;
            }
        }

        if (i < 0) {
            System.out.println(0);
        } else {
            i++;
            for (; i >= 0; i--) {
                System.out.print(res[i]);
            }
        }
    }

    private static int countFurtherVerts(Node curr, Node prev, int dist, int[] processed, List<Edge> edges, Node[] nodes) {
        processed[curr.num] = 1;

        int cnt = 1;

        for (Map.Entry<Integer, Integer> entry : curr.dists.entrySet()) {
            int nextNum = entry.getKey();
            Node next = nodes[nextNum];
            int nextDist = entry.getValue();

            if (processed[nextNum] == 0) {
                cnt += countFurtherVerts(next, curr, nextDist, processed, edges, nodes);
            }
        }

        if (prev != null) {
            edges.add(new Edge(curr.num, prev.num, dist, cnt));
        }

        return cnt;
    }

    private static List<Edge> createSpanningTree(Node[] nodes, int n) {
        int[] processedNodes = new int[n];
        List<Edge> spanningTree = new ArrayList<>();
        TreeSet<TreeElem> tree = new TreeSet<>();

        Node start = nodes[0];
        processedNodes[start.num] = 1;

        int[] dists = new int[n];
        Arrays.fill(dists, Integer.MAX_VALUE);

        int[] parentNode = new int[n];
        Arrays.fill(parentNode, -1);

        for (Map.Entry<Integer, Integer> entry : start.dists.entrySet()) {
            int nodeNum = entry.getKey();
            int dist = entry.getValue();

            tree.add(new TreeElem(nodeNum, dist));
            dists[nodeNum] = dist;
            parentNode[nodeNum] = start.num;
        }

        while (!tree.isEmpty()) {
            TreeElem first = tree.pollFirst();
            spanningTree.add(new Edge(parentNode[first.num], first.num, first.dist));
            processedNodes[first.num] = 1;

            for (Map.Entry<Integer, Integer> entry : nodes[first.num].dists.entrySet()) {
                int nodeNum = entry.getKey();
                int dist = entry.getValue();

                if (processedNodes[nodeNum] == 0) {
                    int oldDist = dists[nodeNum];
                    if (oldDist > dist) {
                        tree.remove(new TreeElem(nodeNum, oldDist));
                        tree.add(new TreeElem(nodeNum, dist));
                        dists[nodeNum] = dist;
                        parentNode[nodeNum] = first.num;
                    }
                }
            }
        }

        return spanningTree;
    }
}
