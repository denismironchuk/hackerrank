import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Created by Denis_Mironchuk on 10/18/2018.
 */
public class DemandingMoney3 {
    static class Node implements Comparable<Node> {
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

        @Override
        public String toString() {
            return String.valueOf(num + 1);
        }

        @Override
        public int compareTo(final Node o) {
            return Integer.compare(num, o.getNum());
        }
    }

    public static Node[] generateGraph(int n) {
        Node[] nodes = new Node[n];
        List<Integer> notConnected = new ArrayList<>();
        List<Integer> connected = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i, 1);
            notConnected.add(i);
        }

        connected.add(notConnected.get(0));
        notConnected.remove(0);

        while (!notConnected.isEmpty()) {
            int conIndex = (int) (Math.random() * (connected.size() - 1));
            int conNodeNum = connected.get(conIndex);
            Node conNode = nodes[conNodeNum];

            int notConIndex = (int) (Math.random() * (notConnected.size() - 1));
            int notConNodeNum = notConnected.get(notConIndex);
            Node notConNode = nodes[notConNodeNum];

            connected.add(notConNodeNum);
            notConnected.remove(notConIndex);

            conNode.addNeighbour(notConNode);
            notConNode.addNeighbour(conNode);
        }

        return nodes;
    }

    private static void addEdges(int edgesToAdd, Node[] graph) {
        int n = graph.length;
        for (int i = 0; i < edgesToAdd; i++) {
            int v1 = (int)((n) * Math.random());

            int v2 = v1;

            while (v1 == v2) {
                v2 = (int)((n) * Math.random());
            }

            graph[v1].addNeighbour(graph[v2]);
            graph[v2].addNeighbour(graph[v1]);
        }
    }

    public static void main(String[] args) throws IOException {
        /*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
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
        }*/

        int n = 34;
        Node[] nodes = generateGraph(n);
        addEdges(2000, nodes);

        Set<Node> p = new HashSet<>();
        for (Node node : nodes) {
            p.add(node);
        }

        bronKerbosh(new HashSet<>(), p, new HashSet<>());
    }

    public static void bronKerbosh(Set<Node> r, Set<Node> p, Set<Node> x) {
        if (p.isEmpty() && x.isEmpty()) {
            Set<Node> res = new TreeSet<>();
            res.addAll(r);
            System.out.println(res);
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
            bronKerbosh(r, newP, newX);
            r.remove(v);

            p.remove(v);
            x.add(v);
        }
    }
}
