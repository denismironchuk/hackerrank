import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 10/18/2018.
 */
public class DemandingMoney3 {
    static class Node {
        private int num;
        private long cost;
        private Set<Node> neighbours = new HashSet<>();

        public Node(int num, long cost) {
            this.num = num;
            this.cost = cost;
        }

        public void addNeighbour(Node neigh) {
            neighbours.add(neigh);
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public long getCost() {
            return cost;
        }

        public Set<Node> getNeighbours() {
            return neighbours;
        }

        public void inverseEdges(Node[] nodes) {
            Set<Node> newNeighbours = new HashSet<>();

            for (Node nd : nodes) {
                if (!neighbours.contains(nd) && nd != this) {
                    newNeighbours.add(nd);
                }
            }

            neighbours = newNeighbours;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line1Tkn = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(line1Tkn.nextToken());
        int m = Integer.parseInt(line1Tkn.nextToken());

        Node[] nodes = new Node[n];

        StringTokenizer costsTkn = new StringTokenizer(br.readLine());

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i, Integer.parseInt(costsTkn.nextToken()));
        }

        for (int i = 0; i < m; i++) {
            StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
            int n1 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int n2 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            nodes[n1].addNeighbour(nodes[n2]);
            nodes[n2].addNeighbour(nodes[n1]);
        }

        for (Node node : nodes) {
            node.inverseEdges(nodes);
        }

        Set<Node> p = new HashSet<>();
        for (Node node : nodes) {
            p.add(node);
        }

        long[] res = new long[2];
        bronKerbosh(new HashSet<>(), p, new HashSet<>(), 0, res, 0);
        System.out.printf("%s %s", res[0], res[1]);
    }

    private static long pow(long n, int p) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return pow(n * n, p / 2);
        } else {
            return n * pow(n, p - 1);
        }
    }

    public static void bronKerbosh(Set<Node> r, Set<Node> p, Set<Node> x, long sum, long[] result, int zeros) {
        if (p.isEmpty() && x.isEmpty()) {
            if (sum > result[0]) {
                result[0] = sum;
                result[1] = pow(2, zeros);
            } else if (sum == result[0]) {
                result[1] += pow(2, zeros);
            }

            return;
        }

        Node pivot = null;
        int maxNeighCnt = -1;

        for (Node nd : p) {
            int neignCnt = 0;
            for (Node neigh : nd.getNeighbours()) {
                if (p.contains(neigh)) {
                    neignCnt++;
                }
            }

            if (neignCnt > maxNeighCnt) {
                maxNeighCnt = neignCnt;
                pivot = nd;
            }
        }

        for (Node nd : x) {
            int neignCnt = 0;
            for (Node neigh : nd.getNeighbours()) {
                if (p.contains(neigh)) {
                    neignCnt++;
                }
            }

            if (neignCnt > maxNeighCnt) {
                pivot = nd;
            }
        }

        Set<Node> pNoPivot = new HashSet<>();
        pNoPivot.addAll(p);
        pNoPivot.removeAll(pivot.getNeighbours());

        Iterator<Node> itr = pNoPivot.iterator();

        while (itr.hasNext()) {
            Node v = itr.next();

            Set<Node> newP = new HashSet<>();
            for (Node neigh : v.getNeighbours()) {
                if (p.contains(neigh)) {
                    newP.add(neigh);
                }
            }

            Set<Node> newX = new HashSet<>();
            for (Node neigh : v.getNeighbours()) {
                if (x.contains(neigh)) {
                    newX.add(neigh);
                }
            }

            r.add(v);
            bronKerbosh(r, newP, newX, sum + v.getCost(), result, v.getCost() == 0 ? zeros + 1:zeros);
            r.remove(v);

            p.remove(v);
            x.add(v);
        }
    }
}
