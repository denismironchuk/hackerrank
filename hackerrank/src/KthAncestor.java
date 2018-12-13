import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class KthAncestor {
    private static class Node {
        private int num;
        private Node parent;
        private Set<Node> children = new HashSet<>();
        private Node heavy = null;
        private int subtreeSize = 0;
        private List<Node> path;
        private int numInPath;

        public Node(final int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public void setParent(final Node parent) {
            this.parent = parent;
        }

        public Node getParent() {
            return parent;
        }

        public void addChild(Node child) {
            children.add(child);
        }

        public Set<Node> getChildren() {
            return children;
        }

        public int getSubtreeSize() {
            return subtreeSize;
        }

        public void setSubtreeSize(final int subtreeSize) {
            this.subtreeSize = subtreeSize;
        }

        public void increaseSubtreeSize(final int size) {
            this.subtreeSize += size;
        }

        public void initHeavyEdge() {
            for (Node child : children) {
                if (2 * child.getSubtreeSize() >= getSubtreeSize()) {
                    heavy = child;
                }
            }
        }

        public List<Node> getPath() {
            return path;
        }

        public void setPath(final List<Node> path) {
            this.path = path;
        }

        public int getNumInPath() {
            return numInPath;
        }

        public void setNumInPath(final int numInPath) {
            this.numInPath = numInPath;
        }

        public Node getHeavy() {
            return heavy;
        }
    }

    private static class Query {
        private int nodeNum;
        private int k;

        public Query(final int nodeNum, final int k) {
            this.nodeNum = nodeNum;
            this.k = k;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            int p = Integer.parseInt(br.readLine());
            Node root = null;
            Map<Integer, Node> nodes = new HashMap<>();

            for (int i = 0; i < p; i++) {
                StringTokenizer pair = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(pair.nextToken());
                int y = Integer.parseInt(pair.nextToken());

                Node nd = new Node(x);
                nodes.put(x, nd);

                if (y == 0) {
                    root = nd;
                } else {
                    Node parent = nodes.get(y);
                    parent.addChild(nd);
                }
            }

            int q = Integer.parseInt(br.readLine());

            List<Query> queries = new LinkedList<>();

            for (int i = 0; i < q; i++) {
                StringTokenizer queryTkn = new StringTokenizer(br.readLine());
                int queryType = Integer.parseInt(queryTkn.nextToken());

                if (queryType == 0) {
                    int x = Integer.parseInt(queryTkn.nextToken());
                    int y = Integer.parseInt(queryTkn.nextToken());

                    Node parent = nodes.get(y);
                    Node child = nodes.get(x);
                    parent.addChild(child);
                } else if (queryType == 2) {
                    int x = Integer.parseInt(queryTkn.nextToken());
                    int k = Integer.parseInt(queryTkn.nextToken());

                    queries.add(new Query(x, k));
                }
            }

            countSubtreeSize(root);

            for (Node nd : nodes.values()) {
                nd.initHeavyEdge();
            }

            initPaths(root, new ArrayList<>());

            StringBuilder res = new StringBuilder();

            for (Query query : queries) {
                Node nd = nodes.get(query.nodeNum);
                int k = query.k;

            }
        }
    }

    private static void countSubtreeSize(Node node) {
        node.setSubtreeSize(1);

        for (Node child : node.getChildren()) {
            countSubtreeSize(child);
            node.increaseSubtreeSize(child.getSubtreeSize());
        }
    }

    private static void initPaths(Node nd, List<Node> path) {
        path.add(nd);
        nd.setPath(path);
        nd.setNumInPath(path.size() - 1);

        for (Node child : nd.getChildren()) {
            if (nd.getHeavy() == child) {
                initPaths(child, path);
            } else {
                initPaths(child, new ArrayList<>());
            }
        }
    }
}