import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class RepairRoads {
    private static class Node {
        private int num;
        private Set<Node> neighbours = new HashSet<>();
        private boolean deleted = false;
        private boolean isVetvl = false;

        public Node(final int num) {
            this.num = num;
        }

        public void addNeighbour(Node neigh) {
            neighbours.add(neigh);
            isVetvl = neighbours.size() > 2;
        }

        public int getNum() {
            return num;
        }

        public Set<Node> getNeighbours() {
            return neighbours;
        }

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted(final boolean deleted) {
            this.deleted = deleted;
        }
    }

    public static void main(String[] args) throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new FileReader("D:/repairRoads.txt"));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            int n = Integer.parseInt(br.readLine());
            Node[] nodes = new Node[n];

            for (int i = 0; i < n; i++) {
                nodes[i] = new Node(i);
            }

            for (int i = 0; i < n - 1; i++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                int v1 = Integer.parseInt(tkn.nextToken());
                int v2 = Integer.parseInt(tkn.nextToken());

                nodes[v1].addNeighbour(nodes[v2]);
                nodes[v2].addNeighbour(nodes[v1]);
            }

            for (Node nd : nodes) {
                if (nd.getNeighbours().size() == 1) {
                    Node neighbour = nd.getNeighbours().iterator().next();
                    if (neighbour.getNeighbours().size() > 2) {
                        nd.setDeleted(true);
                        neighbour.getNeighbours().remove(nd);
                        nd.getNeighbours().remove(neighbour);
                    }
                }
            }

            List<Edge> toRemove = new ArrayList<>();
            for (Node nd : nodes) {
                if (!nd.isDeleted()) {
                    seperateIslands(nd, new int[n], 0, null, null, toRemove);
                    break;
                }
            }

            for (Edge edToRem : toRemove) {
                edToRem.getNd1().getNeighbours().remove(edToRem.getNd2());
                edToRem.getNd2().getNeighbours().remove(edToRem.getNd1());
            }

            int[] processed = new int[n];
            int res = 0;
            for (Node nd : nodes) {
                res += countPaths(nd, processed, true);
            }

            System.out.println(res);
        }
    }

    private static class Edge {
        private Node nd1;
        private Node nd2;

        public Edge(final Node nd1, final Node nd2) {
            this.nd1 = nd1;
            this.nd2 = nd2;
        }

        public Node getNd1() {
            return nd1;
        }

        public Node getNd2() {
            return nd2;
        }
    }

    private static void seperateIslands(Node nd, int[] processed, int distToPrevVetvl, Node prevVetvl, Node prevNode, List<Edge> toRemove) {
        processed[nd.getNum()] = 1;

        if (nd.isVetvl) {
            if (prevVetvl != null) {
                if (distToPrevVetvl == 1) {
                    toRemove.add(new Edge(nd, prevVetvl));
                } else if (distToPrevVetvl == 2) {
                    toRemove.add(new Edge(nd, prevNode));
                    toRemove.add(new Edge(prevNode, prevVetvl));
                    nd.getNeighbours().remove(prevNode);
                }
            }

            prevVetvl = nd;
            distToPrevVetvl = 0;
        }

        for (Node next : nd.getNeighbours()) {
            if (processed[next.getNum()] == 0) {
                seperateIslands(next, processed, distToPrevVetvl + 1, prevVetvl, nd, toRemove);
            }
        }
    }

    private static int countPaths(Node nd, int[] processed, boolean isRoot) {
        int nextCnt = 0;
        processed[nd.getNum()] = 1;
        int res = 0;

        for (Node next : nd.getNeighbours()) {
            if (processed[next.getNum()] == 0) {
                nextCnt++;
                res += countPaths(next, processed, false);
            }
        }

        res += nextCnt / 2;

        if (isRoot) {
            res += nextCnt % 2;
        }

        return res;
    }
}
