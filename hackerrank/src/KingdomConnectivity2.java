import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

public class KingdomConnectivity2 {
    public static long MOD = 1000000000;

    static class Node {
        private int num;
        private Collection<Node> outgoings = new ArrayList<>();
        private long paths;
        private boolean isFinal = false;
        private boolean hasPaths = false;

        public Node(final int num) {
            this.num = num;
        }

        public void addOutgoiong(Node nd) {
            outgoings.add(nd);
        }

        public int getNum() {
            return num;
        }

        public Collection<Node> getOutgoings() {
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

        public boolean isHasPaths() {
            return hasPaths;
        }

        public void setHasPaths(final boolean hasPaths) {
            this.hasPaths = hasPaths;
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
        nodes[n - 1].setHasPaths(true);

        for (int i = 0; i < m; i++) {
            StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
            int out = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int in = Integer.parseInt(edgeTkn.nextToken()) - 1;

            nodes[out].addOutgoiong(nodes[in]);
        }

        countPaths(nodes[0], new int[n]);
        boolean hasCycles = hasCycles(nodes[0], new int[n]);

        System.out.println(hasCycles ? "INFINITE PATHS" : nodes[0].getPaths());
    }

    public static boolean hasCycles(Node nd, int[] processed) {
        processed[nd.getNum()] = 1;

        for (Node next : nd.getOutgoings()) {
            if (processed[next.getNum()] == 1 && next.isHasPaths()) {
                return true;
            }

            if (processed[next.getNum()] == 0 && hasCycles(next, processed)) {
                return true;
            }
        }

        processed[nd.getNum()] = 2;

        return false;
    }

    public static void countPaths(Node nd, int[] processed) {
        if (nd.isFinal()) {
            return;
        }

        processed[nd.getNum()] = 1;
        long paths = 0;

        for (Node out : nd.getOutgoings()) {
            if (processed[out.getNum()] == 0) {
                countPaths(out, processed);
            }

            paths = (paths + out.getPaths()) % MOD;
            nd.setHasPaths(nd.isHasPaths() || out.isHasPaths());
        }

        nd.setPaths(paths);
    }
}
