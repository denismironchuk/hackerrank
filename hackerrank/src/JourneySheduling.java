import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 11/19/2018.
 */
public class JourneySheduling {
    static class Node {
        private int num;
        private Set<Node> neighbours = new HashSet<>();

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
    }
    public static void main(String[] args) throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new FileReader("D:/journey.txt"));
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

        for (int i = 0; i < m; i++) {
            StringTokenizer queryTkn = new StringTokenizer(br.readLine());
            int v = Integer.parseInt(queryTkn.nextToken()) - 1;
            long k = Long.parseLong(queryTkn.nextToken());

            long result = 0;

            Response rsp = findMostDistant(nodes[v], new int[n]);

            result += rsp.dist;

            if (k > 1) {
                Response resp2 = findMostDistant(rsp.node, new int[n]);
                result += resp2.dist * (k - 1);
            }

            res.append(result).append("\n");
        }

        System.out.println(res.toString());
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
