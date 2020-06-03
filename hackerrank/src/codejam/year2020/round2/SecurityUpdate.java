package codejam.year2020.round2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class SecurityUpdate {
    private static class Node {
        private int num;
        private List<Connection> connections = new ArrayList<>();
        private int x;

        private int reachTime = -1;
        private int prevVersCnt = -1;

        public Node(int num, int x) {
            this.num = num;
            this.x = x;
        }

        public void addConnection(Connection con) {
            if (con.nd1.equals(con.nd2)) {
                return;
            }

            connections.add(con);
        }

        public int getReachTime() {
            return reachTime;
        }

        public int getPrevVersCnt() {
            return prevVersCnt;
        }
    }

    private static class Connection {
        private Node nd1;
        private Node nd2;
        private int time = 1;

        public Connection(Node nd1, Node nd2) {
            this.nd1 = nd1;
            this.nd2 = nd2;
        }

        public Node getConnectedNode(Node nd) {
            if (nd1.equals(nd)) {
                return nd2;
            } else {
                return nd1;
            }
        }

        public int getTime() {
            return time;
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int c = Integer.parseInt(tkn1.nextToken());
                int d = Integer.parseInt(tkn1.nextToken());
                int[] x = new int[c + 1];
                Node[] nodes = new Node[c + 1];
                nodes[1] = new Node(1, 0);
                nodes[1].reachTime = 0;
                nodes[1].prevVersCnt = 0;

                StringTokenizer xTkn = new StringTokenizer(br.readLine());
                for (int i = 0; i < c - 1; i++) {
                    x[i + 2] = Integer.parseInt(xTkn.nextToken());
                    nodes[i + 2] = new Node(i + 2, x[i + 2]);
                }
                List<Connection> connections = new ArrayList<>();
                for (int i = 0; i < d; i++) {
                    StringTokenizer dTkn = new StringTokenizer(br.readLine());
                    int c1 = Integer.parseInt(dTkn.nextToken());
                    int c2 = Integer.parseInt(dTkn.nextToken());
                    Connection con = new Connection(nodes[c1], nodes[c2]);
                    nodes[c1].addConnection(con);
                    nodes[c2].addConnection(con);
                    connections.add(con);
                }

                List<Node> positiveNodes = new ArrayList<>();
                List<Node> negativeNodes = new ArrayList<>();
                for (int i = 2; i <= c; i++) {
                    if (nodes[i].x > 0) {
                        nodes[i].reachTime = nodes[i].x;
                        positiveNodes.add(nodes[i]);
                    } else {
                        nodes[i].prevVersCnt = -nodes[i].x;
                        negativeNodes.add(nodes[i]);
                    }
                }
                positiveNodes.sort(Comparator.comparingInt(Node::getReachTime));
                negativeNodes.sort(Comparator.comparingInt(Node::getPrevVersCnt));

                List<Node> updateSeq = new ArrayList<>();
                updateSeq.add(nodes[1]);
                int positiveIndex = 0;

                for (Node nd : negativeNodes) {
                    while(nd.prevVersCnt - updateSeq.size() > 0) {
                        updateSeq.add(positiveNodes.get(positiveIndex));
                        positiveIndex++;
                    }
                    updateSeq.add(nd);
                }

                for (; positiveIndex < positiveNodes.size(); positiveIndex++) {
                    updateSeq.add(positiveNodes.get(positiveIndex));
                }

                updateSeq.remove(0);

                int currentTime = 0;
                int prev = -1;
                for (Node nd : updateSeq) {
                    if (nd.prevVersCnt != -1) {
                        if (prev != nd.prevVersCnt) {
                            currentTime++;
                        }
                        prev = nd.prevVersCnt;
                        nd.reachTime = currentTime;
                    } else {
                        currentTime = nd.reachTime;
                        prev = -1;
                    }

                    for (Connection con : nd.connections) {
                        Node neigh = con.getConnectedNode(nd);
                        if (neigh.reachTime != -1) {
                            con.time = Math.max(1, currentTime - neigh.reachTime);
                        }
                    }
                }

                String res = connections.stream().map(Connection::getTime).map(String::valueOf).collect(Collectors.joining(" "));
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }
}
