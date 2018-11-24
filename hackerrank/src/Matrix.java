import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Matrix {
    static class Node {
        private int num;
        private Set<Edge> neighbours = new HashSet<>();
        private boolean isMachine = false;

        public Node(final int num) {
            this.num = num;
        }

        public void addNeigh(Edge neigh) {
            neighbours.add(neigh);
        }

        public Set<Edge> getNeighbours() {
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

    static class Edge implements Comparable<Edge> {
        private int nd1;
        private int nd2;
        private int id;
        private long time;

        public Edge(int nd1, int nd2, int id, long time) {
            this.nd1 = nd1;
            this.nd2 = nd2;
            this.id = id;
            this.time = time;
        }

        public int getNd1() {
            return nd1;
        }

        public int getNd2() {
            return nd2;
        }

        public long getTime() {
            return time;
        }

        public int getId() {
            return id;
        }

        @Override
        public int compareTo(Edge o) {
            int timeCompare = Long.compare(time, o.getTime());
            if (timeCompare == 0) {
                return Integer.compare(id, o.getId());
            } else {
                return timeCompare;
            }
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

        TreeSet<Edge> edges = new TreeSet<>();

        for (int i = 0; i < n - 1; i++) {
            StringTokenizer roadTkn = new StringTokenizer(br.readLine());
            int n1 = Integer.parseInt(roadTkn.nextToken());
            int n2 = Integer.parseInt(roadTkn.nextToken());
            long time = Long.parseLong(roadTkn.nextToken());

            Edge ed = new Edge(n1, n2, i, time);

            nodes[n1].addNeigh(ed);
            nodes[n2].addNeigh(ed);

            edges.add(ed);
        }

        for (int i = 0; i < k; i++) {
            int machineId = Integer.parseInt(br.readLine());
            nodes[machineId].setMachine(true);
        }

        Queue<Node> toRemoveQ = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            addNodeToQueue(nodes[i], toRemoveQ);
        }

        removeEges(toRemoveQ, edges, nodes);

        long result = 0;

        while (!edges.isEmpty()) {
            Edge minTimeEdge = edges.pollFirst();
            result += minTimeEdge.getTime();

            Node n1 = nodes[minTimeEdge.nd1];
            Node n2 = nodes[minTimeEdge.nd2];

            n1.getNeighbours().remove(minTimeEdge);
            n2.getNeighbours().remove(minTimeEdge);

            addNodeToQueue(n1, toRemoveQ);
            addNodeToQueue(n2, toRemoveQ);

            removeEges(toRemoveQ, edges, nodes);
        }

        System.out.println(result);
    }

    private static void addNodeToQueue(Node nd, Queue<Node> toRemoveQ) {
        if (!nd.isMachine() && nd.getNeighbours().size() == 1) {
            toRemoveQ.add(nd);
        }
    }

    private static void removeEges(Queue<Node> toRemoveQ, TreeSet<Edge> edges, Node[] nodes) {
        while (!toRemoveQ.isEmpty()) {
            Node toRemove = toRemoveQ.poll();
            Edge edToRemove = toRemove.getNeighbours().iterator().next();
            edges.remove(edToRemove);

            Node neighbour = nodes[edToRemove.getNd1() == toRemove.getNum() ? edToRemove.getNd2() : edToRemove.getNd1()];
            neighbour.getNeighbours().remove(edToRemove);

            addNodeToQueue(neighbour, toRemoveQ);
        }
    }
}
