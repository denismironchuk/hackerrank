import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 9/21/2018.
 */
public class RustAndMurderer {
    static class Node {
        private int num;
        private Set<Node> neighbours = new HashSet<>();

        public Node(final int num) {
            this.num = num;
        }

        public void addNeighbour(Node neigh) {
            neighbours.add(neigh);
        }

        public Set<Node> getNeighbours() {
            return neighbours;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(tkn1.nextToken());
            int m = Integer.parseInt(tkn1.nextToken());

            Node[] nodes = new Node[n];

            for (int i = 0; i < n; i++) {
                nodes[i] = new Node(i);
            }

            for (int i = 0; i < m; i++) {
                StringTokenizer roadTkn = new StringTokenizer(br.readLine());
                int n1 = Integer.parseInt(roadTkn.nextToken()) - 1;
                int n2 = Integer.parseInt(roadTkn.nextToken()) - 2;

                nodes[n1].addNeighbour(nodes[n2]);
                nodes[n2].addNeighbour(nodes[n1]);
            }

            int s = Integer.parseInt(br.readLine()) - 1;

            int[] dists = new int[n];
            int dist = 0;
            Set<Node> lastProcessed = new HashSet<>();
            lastProcessed.add(nodes[s]);

            Set<Node> notProcessed = new HashSet<>();

            for (int i = 0; i < n; i++) {
                if (i != s) {
                    notProcessed.add(nodes[i]);
                }
            }

            while (!lastProcessed.isEmpty()) {
                Set<Node> newProc = new HashSet<>();

                for (Node nd : notProcessed) {
                    Set<Node> neighToRemove = new HashSet<>();
                    for (Node neigh: nd.getNeighbours()) {
                        if (!lastProcessed.contains(neigh)) {
                            newProc.add(nd);
                        } else {
                            neighToRemove.add(neigh);
                        }
                    }
                }
            }
        }
    }
}
