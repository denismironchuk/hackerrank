import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Denis_Mironchuk on 9/10/2018.
 */
public class JeaniesRoute {
    static class Node {
        private int num;
        //node -> dist
        private Map<Node, Integer> neighbours = new HashMap<>();

        public Node(final int num) {
            this.num = num;
        }

        public void addNeighbour(Node neighbour, int dist) {
            neighbours.put(neighbour, dist);
        }

        public Map<Node, Integer> getNeighbours() {
            return neighbours;
        }

        public int getNum() {
            return num;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        int k = Integer.parseInt(tkn1.nextToken());

        Set<Integer> deliveryCities = new HashSet<>();

        StringTokenizer citiesTkn = new StringTokenizer(br.readLine());

        for (int i = 0; i < k; i++) {
            deliveryCities.add(Integer.parseInt(citiesTkn.nextToken()) - 1);
        }

        List<Node> nodes = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            nodes.add(new Node(i));
        }

        for (int i = 0; i < n - 1; i++) {
            StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
            int n1 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int n2 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int dist = Integer.parseInt(edgeTkn.nextToken());

            nodes.get(n1).addNeighbour(nodes.get(n2), dist);
            nodes.get(n2).addNeighbour(nodes.get(n1), dist);
        }

        Queue<Node> leaveNodes = new LinkedList<>();

        for (int i = 0; i < nodes.size(); i++) {
            Node nd = nodes.get(i);
            if (nd.getNeighbours().size() == 1) {
                leaveNodes.add(nd);
            }
        }

        int[] nodesToDelete = new int[n];

        while (!leaveNodes.isEmpty()) {
            Node leaf = leaveNodes.poll();
            if (!deliveryCities.contains(leaf.getNum())) {
                nodesToDelete[leaf.getNum()] = 1;
                for (Node nd : leaf.getNeighbours().keySet()) {
                    nd.getNeighbours().remove(leaf);
                    if (nd.getNeighbours().size() == 1) {
                        leaveNodes.add(nd);
                    }
                }
            }
        }

        Node startNode = null;

        for (int i = 0; i < n && startNode == null; i++) {
            if (nodesToDelete[i] == 0) {
                startNode = nodes.get(i);
            }
        }

        long[] dists = new long[n];

        countDists(startNode, new int[n], dists);

        int maxDistNode = 0;

        for (int i = 0; i < n; i++) {
            if (dists[i] > dists[maxDistNode]) {
                maxDistNode = i;
            }
        }

        long[] dists2 = new long[n];

        countDists(nodes.get(maxDistNode), new int[n], dists2);

        long maxDist = 0;
        for (int i = 0; i < n; i++) {
            if (dists2[i] > maxDist) {
                maxDist = dists2[i];
            }
        }

        long allEdgesLen = sumEdgesLen(startNode, new int[n]);

        System.out.println(allEdgesLen * 2 - maxDist);
    }

    private static long sumEdgesLen(Node nd, int[] processed) {
        long res = 0;
        processed[nd.getNum()] = 1;

        for (Map.Entry<Node, Integer> neighEntry : nd.getNeighbours().entrySet()) {
            Node neigh = neighEntry.getKey();
            Integer dist = neighEntry.getValue();

            if (processed[neigh.getNum()] == 0) {
                res += dist + sumEdgesLen(neigh, processed);
            }
        }

        return res;
    }

    private static void countDists(Node nd, int[] processed, long[] dists) {
        processed[nd.getNum()] = 1;

        for (Map.Entry<Node, Integer> neighEntry : nd.getNeighbours().entrySet()) {
            Node neigh = neighEntry.getKey();
            Integer dist = neighEntry.getValue();

            if (processed[neigh.getNum()] == 0) {
                dists[neigh.getNum()] = dists[nd.getNum()] + dist;
                countDists(neigh, processed, dists);
            }
        }
    }
}
