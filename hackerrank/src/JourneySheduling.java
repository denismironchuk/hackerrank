import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by Denis_Mironchuk on 11/19/2018.
 */
public class JourneySheduling {
    static class Node {
        private int num;
        private Set<Node> neighbours = new HashSet<>();
        private long dist1 = 0;
        private long dist2 = 0;

        public Node(final int num) {
            this.num = num;
        }

        public void addNeighbour(Node neigh) {
            neighbours.add(neigh);
        }

        public int getNum() {
            return num;
        }

        public Set<Node> getNeighbours() {
            return neighbours;
        }

        public long getDist1() {
            return dist1;
        }

        public long getDist2() {
            return dist2;
        }

        public void setDist1(final long dist1) {
            this.dist1 = dist1;
        }

        public void setDist2(final long dist2) {
            this.dist2 = dist2;
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader br = new BufferedReader(new FileReader("D:/journey.txt"));
        StringTokenizer line1Tkn = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(line1Tkn.nextToken());
        int m = Integer.parseInt(line1Tkn.nextToken());

        Node[] nodes = new Node[n];

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }

        for (int i = 0; i < n - 1; i++) {
            StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
            int n1 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int n2 = Integer.parseInt(edgeTkn.nextToken()) - 1;

            nodes[n1].addNeighbour(nodes[n2]);
            nodes[n2].addNeighbour(nodes[n1]);
        }

        StringBuilder res = new StringBuilder();

        Response rsp1 = findMostDistant(nodes[0], new int[n]);
        Node nd1 = rsp1.node;
        Response rsp2 = findMostDistant(nd1, new int[n]);
        Node nd2 = rsp2.node;
        setDistance(nd1, new int[n], 0, Node::setDist1, Node::getDist1);
        setDistance(nd2, new int[n], 0, Node::setDist2, Node::getDist2);

        long maxDist = nd2.getDist1();

        for (int i = 0; i < m; i++) {
            StringTokenizer queryTkn = new StringTokenizer(br.readLine());
            int v = Integer.parseInt(queryTkn.nextToken()) - 1;
            long k = Long.parseLong(queryTkn.nextToken());

            long result = Math.max(nodes[v].getDist1(), nodes[v].getDist2());

            if (k > 1) {
                result += maxDist * (k - 1);
            }

            res.append(result).append("\n");
        }

        System.out.println(res.toString());
    }

    private static void setDistance(Node nd, int[] processed, long dist, BiConsumer<Node, Long> distConsumer, Function<Node, Long> distSupplier) {
        distConsumer.accept(nd, dist);
        processed[nd.getNum()] = 1;

        for (Node neigh : nd.getNeighbours()) {
            if (processed[neigh.getNum()] == 0) {
                setDistance(neigh, processed, distSupplier.apply(nd) + 1, distConsumer, distSupplier);
            }
        }
    }

    static class Response {
        private Node node = null;
        private long dist = 0;

        public Response(final Node node, final long dist) {
            this.node = node;
            this.dist = dist;
        }
    }

    private static Response findMostDistant(Node node, int[] processed) {
        processed[node.getNum()] = 1;

        Response res = new Response(node, 0);

        for (Node neigh : node.getNeighbours()) {
            if (processed[neigh.getNum()] == 0) {
                Response dist = findMostDistant(neigh, processed);

                if (dist.dist + 1 > res.dist) {
                    dist.dist++;
                    res = dist;
                }
            }
        }

        return res;
    }
}
