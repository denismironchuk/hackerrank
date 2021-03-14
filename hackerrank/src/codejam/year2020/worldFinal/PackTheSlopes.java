package codejam.year2020.worldFinal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class PackTheSlopes {

    private static class Node {
        private int num;
        private Node parent;
        private List<Node> children = new ArrayList<>();

        private long maxSkiersFromParent = Integer.MAX_VALUE;
        private long priceFromParent = 0;

        private long priceFromRoot = 0;

        private int subtreeSize = 1;

        private Path path;
        private int pathPosition;

        public Node(int num) {
            this.num = num;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public void addChild(Node child) {
            children.add(child);
        }

        public List<Node> getChildren() {
            return children;
        }

        public long getMaxSkiersFromParent() {
            return maxSkiersFromParent;
        }

        public void setMaxSkiersFromParent(long maxSkiersFromParent) {
            this.maxSkiersFromParent = maxSkiersFromParent;
        }

        public long getPriceFromParent() {
            return priceFromParent;
        }

        public void setPriceFromParent(long priceFromParent) {
            this.priceFromParent = priceFromParent;
        }

        public void setPriceFromRoot(long priceFromRoot) {
            this.priceFromRoot = priceFromRoot;
        }

        public long getPriceFromRoot() {
            return priceFromRoot;
        }

        public int getSubtreeSize() {
            return subtreeSize;
        }

        public void setSubtreeSize(int subtreeSize) {
            this.subtreeSize = subtreeSize;
        }

        public Path getPath() {
            return path;
        }

        public void setPath(Path path) {
            this.path = path;
        }

        public int getPathPosition() {
            return pathPosition;
        }

        public void setPathPosition(int pathPosition) {
            this.pathPosition = pathPosition;
        }

        public static void initChildrenPriceFromNode(Node node) {
            for (Node child : node.getChildren()) {
                child.setPriceFromRoot(node.getPriceFromRoot() + child.priceFromParent);
                Node.initChildrenPriceFromNode(child);
            }
        }

        public long findMinSkiersToRoot() {
            if (getParent() == null) {
                return Long.MAX_VALUE;
            }

            return Math.min(getMaxSkiersFromParent(), getParent().findMinSkiersToRoot());
        }

        public long findMinSkiersToRootFast() {
            long pathMinVal = path.getMinValue(0, getPathPosition());
            Node nextPathNode = path.getRoot().getParent();
            if (nextPathNode == null) {
                return pathMinVal;
            }

            return Math.min(pathMinVal, nextPathNode.findMinSkiersToRootFast());
        }

        public void decreaseSkiersAmountToRoot(long amount) {
            if (getParent() != null) {
                setMaxSkiersFromParent(getMaxSkiersFromParent() - amount);
                getParent().decreaseSkiersAmountToRoot(amount);
            }
        }

        public void decreaseSkiersAmountToRootFast(long amount) {
            Node nextPathNode = path.getRoot().getParent();
            path.update(amount, 0, getPathPosition());
            if (nextPathNode != null) {
                nextPathNode.decreaseSkiersAmountToRootFast(amount);
            }
        }

        public void calculateSubtreeSize() {
            for (Node child : children) {
                child.calculateSubtreeSize();
                setSubtreeSize(getSubtreeSize() + child.getSubtreeSize());
            }
        }

        public void heavyLight(List<Node> path, List<List<Node>> paths) {
            path.add(this);

            for (Node child : getChildren()) {
                if (2 * child.getSubtreeSize() < getSubtreeSize()) {
                    List<Node> newPath = new ArrayList<>();
                    paths.add(newPath);
                    child.heavyLight(newPath, paths);
                } else {
                    child.heavyLight(path, paths);
                }
            }
        }
    }

    private static class Path {
        private List<Node> nodes;
        private long[] segmentTree;
        private long[] updates;

        public Path(List<Node> nodes) {
            this.nodes = nodes;
            for (int i = 0; i < this.nodes.size(); i++) {
                Node node = this.nodes.get(i);
                node.setPath(this);
                node.setPathPosition(i);
            }
            segmentTree = new long[4 * nodes.size()];
            Arrays.fill(segmentTree, Long.MAX_VALUE);
            updates = new long[4 * nodes.size()];
            initSegmentTree(1, 0, this.nodes.size() - 1);
        }

        public Node getRoot() {
            return nodes.get(0);
        }

        public List<Node> getNodes() {
            return nodes;
        }

        public void setNodes(List<Node> nodes) {
            this.nodes = nodes;
        }

        private void initSegmentTree(int p, int start, int end) {
            if (start == end) {
                segmentTree[p] = nodes.get(start).getMaxSkiersFromParent();
            } else {
                int middle = (start + end) / 2;
                initSegmentTree(2 * p, start, middle);
                initSegmentTree(2 * p + 1, middle + 1, end);
                segmentTree[p] = Math.min(segmentTree[2 * p], segmentTree[2 * p + 1]);
            }
        }

        public void update(long updVal, int intrStart, int intrEnd) {
            update(1, updVal, 0, nodes.size() - 1, intrStart, intrEnd);
        }

        private void update(int p, long updVal, int start, int end, int intrStart, int intrEnd) {
            if (intrStart > intrEnd) {
                return;
            }

            if (start == intrStart && end == intrEnd) {
                updates[p] += updVal;
                segmentTree[p] -= updVal;
            } else {
                int middle = (start + end) / 2;
                update(2 * p, updVal, start, middle, intrStart, Math.min(intrEnd, middle));
                update(2 * p + 1, updVal, middle + 1, end, Math.max(middle + 1, intrStart), intrEnd);
                segmentTree[p] = Math.min(segmentTree[2 * p], segmentTree[2 * p + 1]) - updates[p];
            }
        }

        public long getMinValue(int intrStart, int intrEnd) {
            return getMinValue(1, updates[1], 0, nodes.size() - 1, intrStart, intrEnd);
        }

        private long getMinValue(int p, long updVal, int start, int end, int intrStart, int intrEnd) {
            if (intrStart > intrEnd) {
                return Long.MAX_VALUE;
            }

            if (start == intrStart && end == intrEnd) {
                return segmentTree[p];
            } else {
                int middle = (start + end) / 2;
                return Math.min(getMinValue(2 * p, updVal + updates[2 * p], start, middle, intrStart, Math.min(intrEnd, middle)),
                        getMinValue(2 * p + 1, updVal + updates[2 * p + 1], middle + 1, end, Math.max(middle + 1, intrStart), intrEnd)) - updates[p];
            }
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int n = Integer.parseInt(br.readLine());
                Node[] nodes = new Node[n];

                for (int i = 0; i < n; i++) {
                    nodes[i] = new Node(i);
                }

                for (int i = 0; i < n - 1; i++) {
                    StringTokenizer tkn = new StringTokenizer(br.readLine());
                    int start = Integer.parseInt(tkn.nextToken()) - 1;
                    int end = Integer.parseInt(tkn.nextToken()) - 1;

                    long maxSkiers = Long.parseLong(tkn.nextToken());
                    long price = Long.parseLong(tkn.nextToken());

                    nodes[start].addChild(nodes[end]);
                    nodes[end].setParent(nodes[start]);

                    nodes[end].setMaxSkiersFromParent(maxSkiers);
                    nodes[end].setPriceFromParent(price);
                }

                nodes[0].calculateSubtreeSize();
                List<List<Node>> paths = new ArrayList<>();
                List<Node> path = new ArrayList<>();
                paths.add(path);
                nodes[0].heavyLight(path, paths);

                paths.forEach(Path::new);

                long maxSkiers = nodes[0].getChildren().stream().collect(Collectors.summingLong(Node::getMaxSkiersFromParent));
                long minPrice = nodes[0].getChildren().stream().collect(Collectors.summingLong(node -> node.getMaxSkiersFromParent() * node.getPriceFromParent()));
                nodes[0].getChildren().forEach(node -> Node.initChildrenPriceFromNode(node));
                List<Node> nodesSortedByPrice = Arrays.stream(nodes)
                        .filter(node -> node != nodes[0])
                        .filter(node -> node.getPriceFromRoot() < 0)
                        .filter(node -> node.getParent() != nodes[0])
                        .sorted(Comparator.comparingLong(Node::getPriceFromRoot))
                        .collect(Collectors.toList());

                long remainingSkiers = maxSkiers;

                for (Node node : nodesSortedByPrice) {
                    if (remainingSkiers <= 0) {
                        break;
                    }

                    long possibleSkiers = Math.min(remainingSkiers, node.findMinSkiersToRootFast());
                    minPrice += node.getPriceFromRoot() * possibleSkiers;
                    node.decreaseSkiersAmountToRootFast(possibleSkiers);
                    remainingSkiers -= possibleSkiers;
                }

                System.out.printf("Case #%s: %s %s\n", t, maxSkiers, minPrice);
            }
        }
    }
}
