import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class PalindromBorder {
    private static long MOD = 1000000007;

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
        private int suffixNum = -1;
        private int suffixCount = -1;

        private int childCnt = 0;
        private Edge[] edgesMap2 = new Edge[27];

        private List<Edge> edges = new ArrayList<>();

        private long palindomsCnt = 0;

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
                child.suffixNum = treeInfo.getSuffixNum();
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

        public long getSuffixCount() {
            if (suffixCount == -1) {
                if (isLeaf()) {
                    suffixCount = 1;
                } else {
                    suffixCount = 0;
                    for (Edge edge : getEdges()) {
                        suffixCount += edge.getChild().getSuffixCount();
                    }
                }
            }

            return suffixCount;
        }

        public int getSuffixNum() {
            if (suffixNum == -1) {
                suffixNum = getEdges().get(0).getChild().getSuffixNum();
            }

            return suffixNum;
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
                            //.append("_").append(e.getChild().getSuffixNum())
                            //.append("_").append(e.getChild().strLen)
                            //.append("_").append(e.getChild().palindomsCnt)
                            .append("_").append(e.getChild().getSuffixCount())
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
            treeInfo.increaseNonLeafCnt();

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
        private int suffixNum = 0;
        private int nonLeafCnt = 1;

        public SuffixTreeInfo(final String fullString) {
            this.fullString = fullString;
        }

        public String getFullString() {
            return fullString;
        }

        public int getSuffixNum() {
            int suffixNumToReturn = suffixNum;
            suffixNum++;
            return suffixNumToReturn;
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

        public int getNonLeafCnt() {
            return nonLeafCnt;
        }

        public void increaseNonLeafCnt() {
            nonLeafCnt++;
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

    private long count(String s) {
        Node root = new SuffixTreeApp().buildTreeOptimal(s);
        setStrLen(root, 0);

        int[] oddPalindoms = countOddPalindroms(s);
        int[] evenPalindroms = countEvenPalindroms(s);

        Node[] nonLeafNodes = new Node[root.getTreeInfo().getNonLeafCnt()];
        collectAllNonLeafNodes(root, nonLeafNodes, 0);
        Arrays.sort(nonLeafNodes, (node1, node2) -> node2.getStrLen() - node1.getStrLen());
        Set<Node> processedNodes = new HashSet<>();

        for (Node node : nonLeafNodes) {
            if (!processedNodes.contains(node) && !node.isRoot()) {
                processedNodes.add(node);
                int[] palCnts = countAllStartingPalindroms(oddPalindoms, evenPalindroms,
                        node.getSuffixNum(), node.getSuffixNum() + node.strLen - 1);
                node.palindomsCnt = palCnts[0];
                Node suffix = node.getSuffix();
                int index = 1;
                while (suffix != null && !suffix.isRoot()) {
                    processedNodes.add(suffix);
                    suffix.palindomsCnt = palCnts[index];
                    index++;
                    suffix = suffix.getSuffix();
                }

            }
        }

        long res = 0;

        for (Node node : nonLeafNodes) {
            if (!node.isRoot()) {
                long p0 = node.getSuffixCount();
                long p1 = (p0 - 1);

                if (p0 % 2 == 0) {
                    p0 /= 2;
                } else {
                    p1 /= 2;
                }

                long p2 = node.palindomsCnt;
                long p3 = node.getParent().palindomsCnt;

                res = (res + (((p2 - p3) * ((p0 * p1) % MOD)) % MOD)) % MOD;
                //res = (res + (((p2 - p3) * ((p0 * p1)))));
                //res += (node.palindomsCnt - node.getParent().palindomsCnt) * node.getSuffixCount() * (node.getSuffixCount() - 1) / 2;
            }
        }

        //System.out.println(root.buildTree());

        return res;
    }

    private int collectAllNonLeafNodes(Node node, Node[] nodes, int cnt) {
        if (!node.isLeaf()) {
            nodes[cnt] = node;
            cnt++;
            for (Edge edge : node.getEdges()) {
                cnt = collectAllNonLeafNodes(edge.getChild(), nodes, cnt);
            }
            return cnt;
        }

        return cnt;
    }

    private  void setStrLen(Node node, int len) {
        for (Edge childEdge : node.getEdges()) {
            int newLen = len + childEdge.getEndIndex() - childEdge.getStartIndex() + 1;
            childEdge.getChild().setStrLen(newLen);
            setStrLen(childEdge.getChild(), newLen);
        }
    }

    public static int[] countOddPalindroms(String s) {
        int strLen = s.length();

        int[] oddEffective = new int[strLen];

        int l = 0;
        int r = -1;

        for (int i = 0; i < strLen; i++) {
            if (i <= r) {
                oddEffective[i] = Math.min(oddEffective[l + r - i], r - i);
            }
            for (int j = oddEffective[i] + 1; i - j >= 0 && i + j < strLen && s.charAt(i - j) == s.charAt(i + j); j++) {
                oddEffective[i]++;
            }
            if (oddEffective[i] + i > r) {
                r = i + oddEffective[i];
                l = i - oddEffective[i];
            }
        }

        return oddEffective;
    }

    public static int[] countEvenPalindroms(String s) {
        int strLen = s.length();
        int[] evenEffective = new int[strLen];

        int l = 0;
        int r = -1;

        for (int i = 0; i < strLen; i++) {
            if (i <= r) {
                evenEffective[i] = Math.min(evenEffective[l + r - i + 1], r - i + 1);
            }
            for (int j = evenEffective[i] + 1; i - j >= 0 && i + j - 1 < strLen && s.charAt(i - j) == s.charAt(i + j - 1); j++) {
                evenEffective[i]++;
            }
            if (evenEffective[i] + i - 1 > r) {
                r = i + evenEffective[i] - 1;
                l = i - evenEffective[i];
            }
        }

        return evenEffective;
    }

    private static int[] countAllStartingPalindroms(int[] oddPalindoms, int[] evenPalindroms,
                                                    int startPos, int endPos) {
        int[] startPalindromsPrepare = new int[endPos - startPos + 1];
        for (int i = endPos; i >= startPos; i--) {
            int oddIndex = Math.max(i - Math.min(oddPalindoms[i], endPos - i), startPos);
            startPalindromsPrepare[oddIndex - startPos]++;
            int evenIndex = Math.max(i - Math.min(evenPalindroms[i], endPos - i + 1), startPos);
            startPalindromsPrepare[evenIndex - startPos]++;
        }
        int[] startPalindroms = new int[endPos - startPos + 1];
        startPalindroms[0] = startPalindromsPrepare[0] - 1;
        for (int i = 1; i < endPos - startPos + 1; i++) {
            startPalindroms[i] = startPalindromsPrepare[i] + startPalindroms[i - 1] - 2;;
        }
        return startPalindroms;
    }

    public static void main(String[] args) throws IOException {
        /*int strLen = 10;
        StringBuilder build = new StringBuilder();

        for (int i = 0; i < strLen; i++) {
            build.append((char) ('a' + (char) (Math.random() * 2)));
        }


        String s = build.toString();
        System.out.println(s);*/
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        //String s = "bbbbbabaaa";
        s += (char)('z' + 1);
        Date start = new Date();
        System.out.println(new PalindromBorder().count(s));
        Date end = new Date();
        //System.out.println(end.getTime() - start.getTime() + "ms");
    }
}
