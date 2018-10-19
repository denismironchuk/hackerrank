import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

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

        @Override
        public String toString() {
            return String.valueOf(num + 1) + " - " + String.valueOf(cost);
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
            if (node.getCost() != 0) {
                p.add(node);
            }
        }

        long[] res = bronKerbosh(new HashSet<>(), p, new HashSet<>(), 0);

        System.out.printf("%s %s", res[0], res[1]);
    }

    private static long fastPow(long n, int p) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return fastPow(n * n, p / 2);
        } else {
            return n * fastPow(n, p - 1);
        }
    }

    public static long[] bronKerbosh(Set<Node> r, Set<Node> p, Set<Node> x, long sum) {
        if (p.isEmpty() && x.isEmpty()) {

            System.out.println(r);
            Set<Node> newP = new HashSet<>();
            for (Node coverNode : r) {
                for (Node neigh : coverNode.getNeighbours()) {
                    if (neigh.getCost() == 0) {
                        boolean conected = true;
                        for (Node n : r) {
                            if (!n.getNeighbours().contains(neigh)) {
                                conected = false;
                                break;
                            }
                        }

                        if (conected) {
                            newP.add(neigh);
                        }
                    }
                }
            }

            if (newP.size() == 0) {
                return new long[]{sum, 1};
            } else {
                long zeroRes = bronKerboshZero(r, newP, new HashSet<>(), 0);
                return new long[]{sum, 1 + (zeroRes / 2)};
            }
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

        long[] res = new long[2];

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
            long[] nextRes = bronKerbosh(r, newP, newX, sum + v.getCost());

            if (nextRes[0] > res[0]) {
                res = nextRes;
            } else if (nextRes[0] == res[0]) {
                res[1] += nextRes[1];
            }

            r.remove(v);

            p.remove(v);
            x.add(v);
        }

        return res;
    }

    public static long bronKerboshZero(Set<Node> r, Set<Node> p, Set<Node> x, long sum) {
        if (p.isEmpty() && x.isEmpty()) {
            return 1;
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

        long res = 0;

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
            res += 2 * bronKerboshZero(r, newP, newX, sum + v.getCost());

            r.remove(v);

            p.remove(v);
            x.add(v);
        }

        return res;
    }
}
