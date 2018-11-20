import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Matrix {
    static class Node {
        private int num;
        private Map<Node, Long> neighbours = new HashMap<>();
        private boolean isMachine = false;

        public Node(final int num) {
            this.num = num;
        }

        public void addNeigh(Node neigh, Long time) {
            neighbours.put(neigh, time);
        }

        public Map<Node, Long> getNeighbours() {
            return neighbours;
        }

        public int getNum() {
            return num;
        }

        public boolean isMachine() {
            return isMachine;
        }

        public void setMachine(final boolean machine) {
            isMachine = machine;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line1Tkn = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(line1Tkn.nextToken());
        int k = Integer.parseInt(line1Tkn.nextToken());

        Node[] nodes = new Node[n];

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }

        for (int i = 0; i < n - 1; i++) {
            StringTokenizer roadTkn = new StringTokenizer(br.readLine());
            int n1 = Integer.parseInt(roadTkn.nextToken());
            int n2 = Integer.parseInt(roadTkn.nextToken());
            long time = Long.parseLong(roadTkn.nextToken());

            nodes[n1].addNeigh(nodes[n2], time);
            nodes[n2].addNeigh(nodes[n1], time);
        }

        Node machine = null;

        for (int i = 0; i < k; i++) {
            int machineId = Integer.parseInt(br.readLine());
            machine = nodes[machineId];
            machine.setMachine(true);
        }

        System.out.println(getMinTime(machine, new int[n], 0));
    }

    private static long getMinTime(Node nd, int[] processed, long minTime) {
        long res = 0;

        if (nd.isMachine()) {
            res += minTime;
            minTime = Long.MAX_VALUE;
        }

        processed[nd.getNum()] = 1;

        for (Map.Entry<Node, Long> entry : nd.getNeighbours().entrySet()) {
            Node next = entry.getKey();
            long time = entry.getValue();

            if (processed[next.getNum()] == 0) {
                res += getMinTime(next, processed, Math.min(minTime, time));
            }
        }

        return res;
    }
}
