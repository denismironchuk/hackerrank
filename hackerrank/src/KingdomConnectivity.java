import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;

public class KingdomConnectivity {
    public static long MOD = 1000000000;

    static class Node {
        private int num;
        private Set<Edge> outgoings = new HashSet<>();
        private Set<Edge> ingoings = new HashSet<>();
        private long paths;
        private boolean isFinal = false;
        private boolean isReachable = false;

        public Node(final int num) {
            this.num = num;
        }

        public void addOutgoiong(Edge ed) {
            outgoings.add(ed);
        }

        public void addIngoing(Edge ed) {
            ingoings.add(ed);
        }

        public int getNum() {
            return num;
        }

        public Set<Edge> getOutgoings() {
            return outgoings;
        }

        public Set<Edge> getIngoings() {
            return ingoings;
        }

        public long getPaths() {
            return paths;
        }

        public void setPaths(final long paths) {
            this.paths = paths;
        }

        public boolean isFinal() {
            return isFinal;
        }

        public void setFinal(final boolean aFinal) {
            isFinal = aFinal;
        }

        public boolean isReachable() {
            return isReachable;
        }

        public void setReachable(final boolean reachable) {
            isReachable = reachable;
        }

        @Override
        public String toString() {
            return "num=" + num;
        }
    }

    static class Edge {
        private Node out;
        private Node in;

        public Edge(final Node out, final Node in) {
            this.out = out;
            this.in = in;
        }

        public Node getOut() {
            return out;
        }

        public Node getIn() {
            return in;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line1Tkn = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(line1Tkn.nextToken());
        int m = Integer.parseInt(line1Tkn.nextToken());

        Node[] nodes = new Node[n];

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }

        nodes[n - 1].setFinal(true);
        nodes[n - 1].setPaths(1);

        for (int i = 0; i < m; i++) {
            StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
            int out = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int in = Integer.parseInt(edgeTkn.nextToken()) - 1;

            Edge ed = new Edge(nodes[out], nodes[in]);

            nodes[out].addOutgoiong(ed);
            nodes[in].addIngoing(ed);
        }

        countPaths(nodes[0], new int[n]);

        if (nodes[0].getPaths() == 0) {
            System.out.println(0);
        } else {
            Set<Edge> initialEdges = new HashSet<>();
            nodes[0].setReachable(true);
            generateList(nodes[0], new int[n], initialEdges);
            nodes[n - 1].setReachable(true);

            Queue<Node> q = new LinkedList<>();

            for (Node nd : nodes) {
                if (nd.isReachable()) {
                    q.add(nd);
                }
            }

            int[] processed = new int[n];

            boolean hasCycle = false;

            while (!q.isEmpty() && !hasCycle) {
                Node nd = q.poll();

                processed[nd.getNum()] = 1;

                for (Edge ed : nd.getIngoings()) {
                    if (!initialEdges.contains(ed)) {
                        Node out = ed.getOut();

                        if (out.isReachable()) {
                            hasCycle = true;
                            break;
                        }

                        if (processed[out.getNum()] == 0) {
                            q.add(out);
                        }
                    }
                }
            }

            System.out.println(hasCycle ? "INFINITE PATHS" : nodes[0].getPaths());
        }
    }

    public static void generateList(Node nd, int[] processed, Set<Edge> edges) {
        if (nd.isFinal()) {
            return;
        }

        processed[nd.getNum()] = 1;

        for (Edge out : nd.getOutgoings()) {
            Node next = out.getIn();

            if (next.getPaths() != 0) {
                if (processed[next.getNum()] == 0 || processed[next.getNum()] == 2) {
                    edges.add(out);
                    next.setReachable(true);

                    generateList(next, processed, edges);
                }
            }
        }

        processed[nd.getNum()] = 2;
    }

    public static void countPaths(Node nd, int[] processed) {
        if (nd.isFinal()) {
            return;
        }

        processed[nd.getNum()] = 1;
        long paths = 0;

        for (Edge ed : nd.getOutgoings()) {
            Node out = ed.getIn();
            if (processed[out.getNum()] == 0) {
                countPaths(out, processed);
            }

            paths = (paths + out.getPaths()) % MOD;
        }

        nd.setPaths(paths);
    }
}
