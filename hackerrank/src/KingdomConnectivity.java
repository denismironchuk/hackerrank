import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class KingdomConnectivity {
    static class Node {
        private int num;
        private Set<Node> outgoings = new HashSet<>();
        private long paths;
        private boolean isFinal = false;
        private boolean inCycle = false;

        public Node(final int num) {
            this.num = num;
        }

        public void addOutgoiong(Node nd) {
            outgoings.add(nd);
        }

        public int getNum() {
            return num;
        }

        public Set<Node> getOutgoings() {
            return outgoings;
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

        public boolean isInCycle() {
            return inCycle;
        }

        public void setInCycle(final boolean inCycle) {
            this.inCycle = inCycle;
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

            nodes[out].addOutgoiong(nodes[in]);
        }

        countPaths(nodes[0], new int[n], new HashSet<>());

        System.out.println(nodes[0].getPaths());
    }

    public static void countPaths(Node nd, int[] processed, Set<Node> path) {
        if (nd.isFinal()) {
            return;
        }

        processed[nd.getNum()] = 1;
        long paths = 0;
        path.add(nd);

        for (Node out : nd.getOutgoings()) {
            if (processed[out.getNum()] == 0) {
                countPaths(out, processed, path);
            }

            paths += out.getPaths();
        }

        nd.setPaths(paths);
        path.remove(nd);
    }
}
