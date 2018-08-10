import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class HowManySubstrings {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(line1.nextToken());
        int q = Integer.parseInt(line1.nextToken());
        String s = br.readLine() + (char)('z' + 1);

        new HowManySubstrings().work(s);
    }

    private void work(String s) {
        Node root = new SuffixTreeApp().buildTreeOptimal(s);
        //Node rootClone = root.getClone();
        root = root.getClone();

        Node[] suffixNodes = root.getTreeInfo().leafNodes;
        long[] suffixSubstrings = new long[s.length() - 1];
        System.out.println(root.buildTree());

        for (int i = 0; i < suffixNodes.length - 1; i++) {
            Edge parentEdge = suffixNodes[i].getParentEdge();
            suffixSubstrings[i] = (i == 0 ? 0 : suffixSubstrings[i - 1]) + parentEdge.getEndIndex() - parentEdge.getStartIndex();
            parentEdge.getParent().removeEdge(parentEdge);
            System.out.println(root.buildTree());
        }

        System.out.println();
    }

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
        public static final int ALPHABET_SIZE = 27;
        private Node parent;
        private Edge parentEdge;
        private Node suffix = null;
        private SuffixTreeInfo treeInfo;
        private int strLen = 0;

        private Node clone = null;

        private int childCnt = 0;
        private Edge[] edgesMap2 = new Edge[ALPHABET_SIZE];

        private List<Edge> edges = new ArrayList<>();

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
                treeInfo.addLeafNode(child);
                Edge newEdge = new Edge(this, child, treeInfo.getPhaseLastCharIndex(),
                        treeInfo.getPhaseLastCharIndex(), treeInfo);

                edgesMap2[c - 'a'] = newEdge;
                edges.add(newEdge);
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
            edges.add(edge);
            childCnt++;
        }

        public void removeEdge(Edge ed) {
            edgesMap2[ed.getChar(0) - 'a'] = null;
            childCnt--;
            if (childCnt == 1 && !isRoot()) {
                Edge childEdge = findSingleEdge();
                Edge parentEdge = getParentEdge();

                int parentEdgeLen = parentEdge.getEndIndex() - parentEdge.getStartIndex() + 1;
                parentEdge.setStartIndex(childEdge.getStartIndex() - parentEdgeLen);
                parentEdge.setEndIndex(childEdge.getEndIndex());

                childEdge.getChild().setParent(parentEdge.getParent());
                childEdge.getChild().setParentEdge(parentEdge);

                parentEdge.setChild(childEdge.getChild());
            }
        }

        public Edge findSingleEdge() {
            for (int i = 0; i < ALPHABET_SIZE; i++) {
                if (edgesMap2[i] != null) {
                    return edgesMap2[i];
                }
            }

            return null;
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

        public List<Edge> getEdges() {
            return edges;
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

        public Edge getParentEdge() {
            return parentEdge;
        }

        public void setParentEdge(final Edge parentEdge) {
            this.parentEdge = parentEdge;
        }

        public String buildTree() {
            String fullString = treeInfo.getFullString();

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

        public Node getClone() {
            if (null == clone) {
                clone = new Node(treeInfo.getClone(), getParent() == null ? null : getParent().getClone());
                clone.setParentEdge(getParentEdge() == null ? null : getParentEdge().getClone());
                clone.childCnt = childCnt;

                for (int i = 0; i < ALPHABET_SIZE; i++) {
                    if (null != edgesMap2[i]) {
                        clone.edgesMap2[i] = edgesMap2[i].getClone();
                    }
                }
            }

            return clone;
        }
    }

    public class Edge {
        private Node parent;
        private Node child;
        private int startIndex;
        private int endIndex;
        private SuffixTreeInfo treeInfo;

        private Edge clone = null;

        public Edge(final Node parent, final Node child, final int startIndex, int endIndex, SuffixTreeInfo treeInfo) {
            this.parent = parent;
            this.child = child;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.treeInfo = treeInfo;
            child.setParentEdge(this);
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
            middleVerticle.setParentEdge(this);

            child.setParent(middleVerticle);
            treeInfo.setCurrentPhaseAddedInnerNode(middleVerticle);

            Edge bottomEdge = new Edge(middleVerticle, child, startIndex + pos + 1, getEndIndex(), treeInfo);
            middleVerticle.addEdge(bottomEdge);

            this.child = middleVerticle;
            this.endIndex = startIndex + pos;

            return middleVerticle.process(c);
        }

        public char getChar(int pos) {
            return treeInfo.fullString.charAt(startIndex + pos);
        }

        public void setChild(final Node child) {
            this.child = child;
        }

        public void setEndIndex(final int endIndex) {
            this.endIndex = endIndex;
        }

        public void setParent(final Node parent) {
            this.parent = parent;
        }

        public void setStartIndex(final int startIndex) {
            this.startIndex = startIndex;
        }

        public Edge getClone() {
            if (clone == null) {
                clone = new Edge(getParent().getClone(), getChild().getClone(), getStartIndex(), getEndIndex(), treeInfo.getClone());
            }

            return clone;
        }
    }

    public class SuffixTreeInfo {
        private String fullString;
        private Node lastVisitedNode;
        private Node currentPhaseAddedInnerNode;
        private Node previousPhaseAddedInnerNode;
        private int phaseLastCharIndex;
        private Node root;
        private int leafNodeCnt = 0;
        private Node[] leafNodes;

        private SuffixTreeInfo clone;

        public SuffixTreeInfo(final String fullString) {
            this.fullString = fullString;
            leafNodes = new Node[fullString.length()];
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

        public void addLeafNode(Node nd) {
            leafNodes[leafNodeCnt] = nd;
            leafNodeCnt++;
        }

        public SuffixTreeInfo getClone() {
            if (clone == null) {
                clone = new SuffixTreeInfo(fullString);
                clone.phaseLastCharIndex = phaseLastCharIndex;
                for (int i = 0; i < leafNodes.length; i++) {
                    if (leafNodes[i] != null) {
                        clone.leafNodes[i] = leafNodes[i].getClone();
                    }
                }
            }

            return clone;
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
}
