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
        private boolean deleted;

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
            child.setParent(this);
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

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted(final boolean deleted) {
            this.deleted = deleted;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "num=" + num +
                    '}';
        }
    }

    private static class Query {
        private Node node;
        private int k;

        public Query() {
        }

        public Query(final Node node, final int k) {
            this.node = node;
            this.k = k;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        StringBuilder res = new StringBuilder();

        for (int t = 0; t < T; t++) {
            int p = Integer.parseInt(br.readLine());
            Node root = null;
            //Map<Integer, List<Node>> nodes = new HashMap<>();
            List[] nodes = new List[100001];

            for (int i = 0; i < p; i++) {
                StringTokenizer pair = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(pair.nextToken());
                int y = Integer.parseInt(pair.nextToken());


                List<Node> currentNodes = nodes[x];
                if (null == currentNodes) {
                    currentNodes = new ArrayList<>();
                    nodes[x] = currentNodes;
                    currentNodes.add(new Node(x));
                }
                Node nd = currentNodes.get(currentNodes.size() - 1);

                if (y == 0) {
                    root = nd;
                } else {
                    List<Node> parentList = nodes[y];
                    if (parentList == null) {
                        parentList = new ArrayList<>();
                        parentList.add(new Node(y));
                        nodes[y] = parentList;
                    }
                    Node parent = parentList.get(parentList.size() - 1);
                    parent.addChild(nd);
                }
            }

            int q = Integer.parseInt(br.readLine());

            List<Query> queries = new LinkedList<>();

            for (int i = 0; i < q; i++) {
                StringTokenizer queryTkn = new StringTokenizer(br.readLine());
                int queryType = Integer.parseInt(queryTkn.nextToken());

                if (queryType == 0) {
                    int y = Integer.parseInt(queryTkn.nextToken());
                    int x = Integer.parseInt(queryTkn.nextToken());

                    List<Node> parentsList = nodes[y];
                    Node parent = parentsList.get(parentsList.size() - 1);
                    List<Node> childrenList = nodes[x];
                    if (null == childrenList) {
                        childrenList = new ArrayList<>();
                        nodes[x] = childrenList;
                    }
                    Node child = new Node(x);
                    parent.addChild(child);
                    childrenList.add(child);
                } else if (queryType == 1) {
                    int x = Integer.parseInt(queryTkn.nextToken());
                    List<Node> nodesList = nodes[x];
                    if (null != nodesList) {
                        nodesList.get(nodesList.size() - 1).setDeleted(true);
                    }
                } else if (queryType == 2) {
                    int x = Integer.parseInt(queryTkn.nextToken());
                    int k = Integer.parseInt(queryTkn.nextToken());

                    List<Node> nodesList = nodes[x];
                    if (nodesList == null) {
                        queries.add(new Query());
                    } else {
                        Node node = nodesList.get(nodesList.size() - 1);
                        if (node.isDeleted()) {
                            queries.add(new Query());
                        } else {
                            queries.add(new Query(node, k));
                        }
                    }
                }
            }

            countSubtreeSize(root);
            initHeavyEdges(root);

            initPaths(root, new ArrayList<>());

            for (Query query : queries) {
                Node nd = query.node;
                int k = query.k;

                while (null != nd && k > nd.getNumInPath()) {
                    k -= nd.getNumInPath() + 1;
                    nd = nd.getPath().get(0).getParent();
                }

                if (nd != null) {
                    nd = nd.getPath().get(nd.getNumInPath() - k);
                    res.append(nd.getNum()).append("\n");
                } else {
                    res.append(0).append("\n");
                }
            }
        }
        System.out.print(res.toString());
    }

    private static void initHeavyEdges(Node node) {
        node.initHeavyEdge();

        for (Node child : node.getChildren()) {
            initHeavyEdges(child);
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