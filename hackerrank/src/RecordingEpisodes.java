import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class RecordingEpisodes {
    private static class Node {
        private int num;
        private Set<Node> neighbours = new HashSet<>();

        public Node(final int num) {
            this.num = num;
        }

        public void addNeighbour(Node neigh) {
            neighbours.add(neigh);
        }
    }

    private static class Episode {
        private int num;
        private int[] airTime = new int[2];
        private int[] repeatTime = new int[2];

        private Node airNode;
        private Node repeatNode;

        private Node negAirNode;
        private Node negRepeatNode;

        public Episode(final int num, final int[] airTime, final int[] repeatTime) {
            this.num = num;
            this.airTime = airTime;
            this.repeatTime = repeatTime;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());

        for (int q = 0; q < Q; q++) {
            int n = Integer.parseInt(br.readLine());
            Episode[] episodes = new Episode[n];
            Node[] nodes = new Node[n * 4];

            for (int i = 0; i < n; i++) {
                StringTokenizer episodeTkn = new StringTokenizer(br.readLine());
                int airStart = Integer.parseInt(episodeTkn.nextToken());
                int airEnd = Integer.parseInt(episodeTkn.nextToken());

                int repeatStart = Integer.parseInt(episodeTkn.nextToken());
                int repeatEnd = Integer.parseInt(episodeTkn.nextToken());

                episodes[i] = new Episode(i, new int[] {airStart, airEnd}, new int[] {repeatStart, repeatEnd});

                episodes[i].airNode = new Node(i * 4);
                episodes[i].repeatNode = new Node(i * 4 + 1);
                episodes[i].negAirNode = new Node(i * 4 + 2);
                episodes[i].negRepeatNode = new Node(i * 4 + 3);

                nodes[i * 4] = episodes[i].airNode;
                nodes[i * 4 + 1] = episodes[i].repeatNode;
                nodes[i * 4 + 2] = episodes[i].negAirNode;
                nodes[i * 4 + 3] = episodes[i].negRepeatNode;

                episodes[i].airNode.addNeighbour(episodes[i].negRepeatNode);
                episodes[i].repeatNode.addNeighbour(episodes[i].negAirNode);
                episodes[i].negAirNode.addNeighbour(episodes[i].repeatNode);
                episodes[i].negRepeatNode.addNeighbour(episodes[i].airNode);
            }

            int point1 = 0;
            int point2 = 1;

            while (point2 < n) {

            }

            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    Episode ep1 = episodes[i];
                    Episode ep2 = episodes[j];

                    if (overlaps(ep1.airTime, ep2.airTime)) {
                        ep1.airNode.addNeighbour(ep2.negAirNode);
                        ep2.airNode.addNeighbour(ep1.negAirNode);
                    }

                    if (overlaps(ep1.airTime, ep2.repeatTime)) {
                        ep1.airNode.addNeighbour(ep2.negRepeatNode);
                        ep2.repeatNode.addNeighbour(ep1.negAirNode);
                    }

                    if (overlaps(ep1.repeatTime, ep2.airTime)) {
                        ep1.repeatNode.addNeighbour(ep2.negAirNode);
                        ep2.airNode.addNeighbour(ep1.negRepeatNode);
                    }

                    if (overlaps(ep1.repeatTime, ep2.repeatTime)) {
                        ep1.repeatNode.addNeighbour(ep2.negRepeatNode);
                        ep2.repeatNode.addNeighbour(ep1.negRepeatNode);
                    }
                }
            }
        }
    }

    private static boolean overlaps(int[] intr1, int[] intr2) {
        return intr1[2] > intr2[1] && intr2[2] > intr1[1];
    }
}
