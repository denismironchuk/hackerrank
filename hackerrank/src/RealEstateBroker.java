import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 10/3/2018.
 */
public class RealEstateBroker {
    static class Node {
        private int num;
        private List<Node> conns = new ArrayList<>();
        private int a;
        private int p;
        private int x;
        private int y;

        public Node(final int num) {
            this.num = num;
        }

        public void addConn(Node friend) {
            conns.add(friend);
        }

        public int getNum() {
            return num;
        }

        public List<Node> getConns() {
            return conns;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line1Tkn = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(line1Tkn.nextToken());
        int m = Integer.parseInt(line1Tkn.nextToken());

        Node[] nodes = new Node[n + m];

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }

        for (int i = n; i < m + n; i++) {
            nodes[i] = new Node(i);
        }

        for (int i = 0; i < n; i++) {
            StringTokenizer customerTkn = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(customerTkn.nextToken());
            int p = Integer.parseInt(customerTkn.nextToken());

            nodes[i].a = a;
            nodes[i].p = p;
        }

        for (int i = 0; i < m; i++) {
            StringTokenizer houseTkn = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(houseTkn.nextToken());
            int y = Integer.parseInt(houseTkn.nextToken());

            nodes[i + n].x = x;
            nodes[i + n].y = y;
        }

        for (int i = 0; i < n; i++) {
            for (int j = n; j < m + n; j++) {
                if (nodes[i].a < nodes[j].x && nodes[i].p >= nodes[j].y) {
                    nodes[i].addConn(nodes[j]);
                    nodes[j].addConn(nodes[i]);
                }
            }
        }

        int[] pair = new int[n + m];
        Arrays.fill(pair, -1);

        for (Node nd : nodes) {
            if (pair[nd.getNum()] == -1) {
                increasePath(nd, new int[n + m], pair, nodes);
            }
        }

        int res = 0;
        for (int i = 0; i < n + m; i++) {
            if (pair[i] != -1) {
                res++;
            }
        }

        System.out.println(res / 2);
    }

    private static boolean increasePath(Node nd, int[] processed, int[] pair, Node[] nodes) {
        int ndNum = nd.getNum();
        processed[ndNum] = 1;

        for (Node neigh : nd.getConns()) {
            int neighNum = neigh.getNum();

            if (processed[neighNum] == 1) {
                continue;
            }

            processed[neighNum] = 1;

            if (pair[neighNum] == -1 || increasePath(nodes[pair[neighNum]], processed, pair, nodes)) {
                pair[neighNum] = ndNum;
                pair[ndNum] = neighNum;
                return true;
            }
        }

        return false;
    }
}
