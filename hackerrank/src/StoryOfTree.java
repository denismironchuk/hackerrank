import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class StoryOfTree {
    static class Node {
        private int num;
        private List<Node> neighbours = new ArrayList<>();
        private Set<Integer> expectedParents = new HashSet<>();
        private Set<Integer> expectedChildren = new HashSet<>();
        private int rightGuesses = 0;

        public Node(final int num) {
            this.num = num;
        }

        public void addNeighbour(Node neighbour) {
            neighbours.add(neighbour);
        }

        public int getNum() {
            return num;
        }

        public List<Node> getNeighbours() {
            return neighbours;
        }

        public void addExpectedParent(final int expectedParent) {
            this.expectedParents.add(expectedParent);
        }

        public void addExpectedChild(final int expectedChild) {
            this.expectedChildren.add(expectedChild);
        }

        public Set<Integer> getExpectedParents() {
            return expectedParents;
        }

        public Set<Integer> getExpectedChildren() {
            return expectedChildren;
        }

        public int getRightGuesses() {
            return rightGuesses;
        }

        public void setRightGuesses(int rightGuesses) {
            this.rightGuesses = rightGuesses;
        }

        public void addRightGuesses(int rightGuesses) {
            this.rightGuesses += rightGuesses;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());

        for (int q = 0; q < Q; q++) {
            int n = Integer.parseInt(br.readLine());
            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node(i);
            }

            for (int i = 0; i < n - 1; i++) {
                StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
                int nd1 = Integer.parseInt(edgeTkn.nextToken()) - 1;
                int nd2 = Integer.parseInt(edgeTkn.nextToken()) - 1;

                nodes[nd1].addNeighbour(nodes[nd2]);
                nodes[nd2].addNeighbour(nodes[nd1]);
            }

            StringTokenizer condsTkn = new StringTokenizer(br.readLine());
            int g = Integer.parseInt(condsTkn.nextToken());
            int k = Integer.parseInt(condsTkn.nextToken());

            for (int i = 0; i < g; i++) {
                StringTokenizer pairTkn = new StringTokenizer(br.readLine());
                int u = Integer.parseInt(pairTkn.nextToken()) - 1;
                int v = Integer.parseInt(pairTkn.nextToken()) - 1;

                nodes[v].addExpectedParent(u);
                nodes[u].addExpectedChild(v);
            }

            nodes[0].setRightGuesses(countInclusiveClasses(nodes[0], new int[n]));
            countRightGuesses(nodes[0], new int[n]);

            int successCnt = 0;
            for (int i = 0; i< n; i++) {
                if (nodes[i].getRightGuesses() >= k) {
                    successCnt++;
                }
            }

            if (successCnt == 0) {
                System.out.println("0/1");
            } else {
                int gcd = gcd(n, successCnt);
                System.out.printf("%d/%d\n", successCnt / gcd, n/ gcd);
            }
        }
    }

    private static int gcd(int a, int b) {
        int ost = a % b;

        if (ost != 0) {
            return gcd(b, ost);
        } else {
            return b;
        }
    }

    private static int countInclusiveClasses(Node nd, int[] processed) {
        int result = 0;
        processed[nd.getNum()] = 1;

        for (Node neighbour : nd.getNeighbours()) {
            if (processed[neighbour.getNum()] == 0) {
                if (neighbour.getExpectedParents().contains(nd.num)) {
                    result += 1;
                }
                result += countInclusiveClasses(neighbour, processed);
            }
        }

        return result;
    }

    private static void countRightGuesses(Node nd, int[] processed) {
        processed[nd.getNum()] = 1;

        for (Node neighbour : nd.getNeighbours()) {
            if (processed[neighbour.getNum()] == 0) {
                neighbour.addRightGuesses(nd.getRightGuesses());

                if (neighbour.getExpectedParents().contains(nd.getNum())) {
                    neighbour.addRightGuesses(-1);
                }

                if (nd.getExpectedParents().contains(neighbour.getNum())) {
                    neighbour.addRightGuesses(1);
                }

                countRightGuesses(neighbour, processed);
            }
        }
    }
}
