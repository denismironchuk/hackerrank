public class Edge {
    private Node parent;
    private Node child;
    private int startIndex;
    private int endIndex;
    private SuffixTreeInfo treeInfo;

    public Edge(final Node parent, final Node child, final int startIndex, int endIndex, SuffixTreeInfo treeInfo) {
        this.parent = parent;
        this.child = child;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.treeInfo = treeInfo;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        if (child.isLeaf()) {
            return treeInfo.getPhaseLastCharIndex();
        } else {
            return endIndex;
        }
    }

    public Node getChild() {
        return child;
    }

    public Node getParent() {
        return parent;
    }

    public boolean process(char c, int pos) {
        String fullString = treeInfo.getFullString();
        boolean terminate = false;
        treeInfo.setLastVisitedNode(getParent());

        if (fullString.charAt(getStartIndex() + pos + 1) == c) {
            terminate = true;
        } else {
            splitEdge(c, pos);
        }

        return terminate;
    }

    private boolean splitEdge(char c, int pos) {
        Node middleVerticle = new Node(treeInfo);

        treeInfo.setCurrentPhaseAddedInnerNode(middleVerticle);

        Edge bottomEdge = new Edge(middleVerticle, child, startIndex + pos + 1, getEndIndex(), treeInfo);
        middleVerticle.addEdge(bottomEdge);

        this.child = middleVerticle;
        this.endIndex = startIndex + pos;

        return middleVerticle.process(c);
    }
}
