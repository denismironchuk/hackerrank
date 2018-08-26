import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BreadthFirstSearch {
    static class Node {
        private int num;
        private List<Node> neighbours = new ArrayList<>();

        public Node(int num) {
            this.num = num;
        }

        public void addNeighbour(Node nd) {
            neighbours.add(nd);
        }

        public int getNum() {
            return num;
        }

        public List<Node> getNeighbours() {
            return neighbours;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());

        for (int q = 0; q < Q; q++) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(tkn1.nextToken());
            int m = Integer.parseInt(tkn1.nextToken());

            Node[] nodes = new Node[n];

            for (int i = 0; i < n; i++) {
                nodes[i] = new Node(i);
            }

            for (int i = 0; i < m; i++) {
                StringTokenizer edge = new StringTokenizer(br.readLine());
                int nd1Num = Integer.parseInt(edge.nextToken()) - 1;
                int nd2Num = Integer.parseInt(edge.nextToken()) - 1;

                nodes[nd1Num].addNeighbour(nodes[nd2Num]);
                nodes[nd2Num].addNeighbour(nodes[nd1Num]);
            }

            int start = Integer.parseInt(br.readLine()) - 1;

            int[] dists = new int[n];

            for (int i = 0; i < n; i++) {
                dists[i] = -1;
            }

            Queue<Node> nodesQ = new LinkedList<>();
            int[] processed = new int[n];
            nodesQ.add(nodes[start]);
            dists[start] = 0;

            while (!nodesQ.isEmpty()) {
                Node nd = nodesQ.poll();
                processed[nd.getNum()] = 1;
                int currentDist = dists[nd.getNum()];

                for (Node neighbour : nd.getNeighbours()) {
                    if (processed[neighbour.getNum()] == 0) {
                        processed[neighbour.getNum()] = 1;
                        nodesQ.add(neighbour);
                        dists[neighbour.getNum()] = currentDist + 6;
                    }
                }
            }

            for (int i = 0; i < n; i++) {
                if (i != start) {
                    System.out.print(dists[i] + " ");
                }
            }
            System.out.println();
        }
    }
}
