public class HowManySubstrings3 {

    public static final char ENDING = (char) ('z' + 1);

    public class SuffixTreeApp {
        public Node buildTreeOptimal(String s) {
            SuffixTreeInfo treeInfo = new SuffixTreeInfo(s);
            Node root = new Node(treeInfo, null);
            ActivePoint ap = new ActivePoint(treeInfo);
            treeInfo.setRoot(root);

            for (int i = 0; i < s.length(); i++) {

                char c = s.charAt(i);

                treeInfo.setPreviousPhaseAddedInnerNode(null);
                treeInfo.setCurrentPhaseAddedInnerNode(null);
                treeInfo.setLastVisitedNode(null);
                treeInfo.setPhaseLastCharIndex(i);

                boolean terminate;
                boolean finalExtension;

                do {
                    finalExtension = ap.getEdge() == null;

                    if (ap.getEdge() == null) {
                        terminate = root.process(c);
                    } else {
                        Edge edge = ap.getEdge();
                        if (ap.getPos() == edge.getEndIndex() - edge.getStartIndex()) {
                            terminate = edge.getChild().process(c);
                        } else {
                            terminate = edge.process(c, ap.getPos());
                        }
                    }

                    if (treeInfo.getPreviousPhaseAddedInnerNode() != null) {
                        treeInfo.getPreviousPhaseAddedInnerNode().setSuffix(treeInfo.getLastVisitedNode());
                    }
                    treeInfo.setPreviousPhaseAddedInnerNode(treeInfo.getCurrentPhaseAddedInnerNode());
                    treeInfo.setCurrentPhaseAddedInnerNode(null);

                    if (!finalExtension && !terminate) {
                        ap.moveToSuffix();
                    }
                } while(!terminate && !finalExtension);

                ap.moveForwardByChar(c, !terminate);
            }

            return root;
        }
    }

    public class Node {
        private Node parent;
        private Node suffix = null;
        private SuffixTreeInfo treeInfo;
        private int strLen = 0;

        private int childCnt = 0;
        private Edge[] edgesMap2 = new Edge[27];

        public Node(final SuffixTreeInfo treeInfo, final Node parent) {
            this.treeInfo = treeInfo;
            this.parent = parent;
        }

        public boolean process(char c) {
            Edge edge = edgesMap2[c - 'a'];
            treeInfo.setLastVisitedNode(this);

            boolean terminate = false;
            if (null == edge) {
                Node child = new Node(treeInfo, this);
                Edge newEdge = new Edge(this, child, treeInfo.getPhaseLastCharIndex(),
                        treeInfo.getPhaseLastCharIndex(), treeInfo);

                edgesMap2[c - 'a'] = newEdge;
                childCnt++;
            } else {
                terminate = true;
            }

            return terminate;
        }

        public boolean isLeaf() {
            return childCnt == 0;
        }

        public void addEdge(Edge edge) {
            edgesMap2[treeInfo.getFullString().charAt(edge.getStartIndex()) - 'a'] = edge;
            childCnt++;
        }

        public void setSuffix(final Node suffix) {
            this.suffix = suffix;
        }

        public Node getSuffix() {
            return suffix;
        }

        public Edge getEdge(char c) {
            return edgesMap2[c - 'a'];
        }

        public void setStrLen(int len) {
            strLen = len;
        }

        public int getStrLen() {
            return strLen;
        }

        public boolean isRoot() {
            return suffix == null;
        }

        public SuffixTreeInfo getTreeInfo() {
            return treeInfo;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public Node getParent() {
            return parent;
        }

        public String buildTree() {
            String fullString = treeInfo.getFullString();
            fullString = fullString.replaceAll("\\" + ENDING, "&");

            StringBuilder res = new StringBuilder();
            boolean isFirst = true;
            boolean hasAny = false;
            for (Edge e : edgesMap2) {
                if (null != e) {
                    if (!isFirst) {
                        res.append(",");
                    }
                    res.append("\"")
                            .append(fullString.substring(e.getStartIndex(), e.getEndIndex() + 1))
                            .append("\":").append(e.getChild().buildTree()).append("");
                    isFirst = false;
                    hasAny=true;
                }
            }
            return hasAny ? "{" + res.toString() + "}" : "\"\"";
        }
    }

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
            Node middleVerticle = new Node(treeInfo, parent);

            child.setParent(middleVerticle);
            treeInfo.setCurrentPhaseAddedInnerNode(middleVerticle);

            Edge bottomEdge = new Edge(middleVerticle, child, startIndex + pos + 1, getEndIndex(), treeInfo);
            middleVerticle.addEdge(bottomEdge);

            this.child = middleVerticle;
            this.endIndex = startIndex + pos;

            return middleVerticle.process(c);
        }
    }

    public class SuffixTreeInfo {
        private String fullString;
        private Node lastVisitedNode;
        private Node currentPhaseAddedInnerNode;
        private Node previousPhaseAddedInnerNode;
        private int phaseLastCharIndex;
        private Node root;

        public SuffixTreeInfo(final String fullString) {
            this.fullString = fullString;
        }

        public String getFullString() {
            return fullString;
        }

        public Node getLastVisitedNode() {
            return lastVisitedNode;
        }

        public void setLastVisitedNode(final Node lastVisitedNode) {
            this.lastVisitedNode = lastVisitedNode;
        }

        public Node getCurrentPhaseAddedInnerNode() {
            return currentPhaseAddedInnerNode;
        }

        public void setCurrentPhaseAddedInnerNode(final Node currentPhaseAddedInnerNode) {
            this.currentPhaseAddedInnerNode = currentPhaseAddedInnerNode;
        }

        public Node getPreviousPhaseAddedInnerNode() {
            return previousPhaseAddedInnerNode;
        }

        public void setPreviousPhaseAddedInnerNode(final Node previousPhaseAddedInnerNode) {
            this.previousPhaseAddedInnerNode = previousPhaseAddedInnerNode;
        }

        public int getPhaseLastCharIndex() {
            return phaseLastCharIndex;
        }

        public void setPhaseLastCharIndex(final int phaseLastCharIndex) {
            this.phaseLastCharIndex = phaseLastCharIndex;
        }

        public Node getRoot() {
            return root;
        }

        public void setRoot(final Node root) {
            this.root = root;
        }
    }

    public class ActivePoint {
        private SuffixTreeInfo treeInfo;
        private Edge edge = null;
        private int pos = 0;

        public ActivePoint(final SuffixTreeInfo treeInfo) {
            this.treeInfo = treeInfo;
        }

        public Edge getEdge() {
            return edge;
        }

        public int getPos() {
            return pos;
        }

        public void moveForwardByChar(char c, boolean foolExtension) {
            if (null != edge) {
                pos++;
                if (edge.getStartIndex() + pos > edge.getEndIndex()) {
                    edge = edge.getChild().getEdge(c);
                    pos = 0;
                }
            } else {
                pos = 0;
                if (!foolExtension) {
                    edge = treeInfo.getRoot().getEdge(c);
                }
            }
        }

        public void moveToSuffix() {
            if (null == edge) {
                return;
            }

            if (edge.getParent().getSuffix() == null) {
                if (pos == 0) {
                    edge = null;
                } else {
                    moveFromNode(edge.getParent(), edge.getStartIndex() + 1, edge.getStartIndex() + pos);
                }
            } else {
                moveFromNode(edge.getParent().getSuffix(), edge.getStartIndex(), edge.getStartIndex() + pos);
            }
        }

        private void moveFromNode(Node node, int startIndex, int endIndex) {
            String fullString = treeInfo.getFullString();

            int fullLen = endIndex - startIndex + 1;
            char startChar = fullString.charAt(startIndex);
            Edge tempEdge = node.getEdge(startChar);
            int tempEdgeLen = tempEdge.getEndIndex() - tempEdge.getStartIndex() + 1;

            if (fullLen > tempEdgeLen) {
                moveFromNode(tempEdge.getChild(), startIndex + tempEdgeLen, endIndex);
            } else {
                edge = tempEdge;
                pos = fullLen - 1;
            }
        }
    }

    public void run() {
        String s = "aaabababa" + ENDING;

        SuffixTreeApp suffixApp = new SuffixTreeApp();
        Node root = suffixApp.buildTreeOptimal(s);
        System.out.println(root.buildTree());
    }

    public static void main(String[] args) {
        new HowManySubstrings3().run();
    }
}
