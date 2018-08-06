import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LetterIslands {
    public static void main(String[] args) throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new FileReader("D://letterIslands26.txt"));
        String s = br.readLine();

        /*int strLen = 5;
        StringBuilder build = new StringBuilder();

        for (int i = 0; i < strLen; i++) {
            build.append((char) ('a' + (char) (Math.random() * 2)));
        }


        String s = build.toString();*/
        //String s = "ababaabaaa";
        s+=(char)('z' + 1);
        //System.out.println(s);

        int k = Integer.parseInt(br.readLine());
        //Date start = new Date();
        System.out.println(new LetterIslands().countSubstrings(s, k));
        //Date end = new Date();
        //System.out.println(end.getTime() - start.getTime() + "ms");
    }

    private long countSubstrings(String s, int k) {
        Node root = new SuffixTreeApp().buildTreeOptimal(s);
        setStrLen(root, 0);
        //System.out.println(root.buildTree());
        long result = 0;

        for (Edge edge : root.getEdges()) {
            List<Integer> prefix = new ArrayList<>();
            prefix.add(0);
            List<Character> str = new ArrayList<>();
            str.add(edge.getChar(0));

            List<Edge> edges = new ArrayList<>();
            edges.add(edge);

            List<Long> islandsCount = new ArrayList<>();
            islandsCount.add(edge.getChild().getSuffixCount());

            result += prefixCalc(edge, 1, prefix, str, edges, islandsCount, k);

            if (islandsCount.get(islandsCount.size() - 1) == k && !edge.getChild().isLeaf()) {
                result++;
                //System.out.println(str);
            }

            //System.out.print(islandsCount.get(islandsCount.size() - 1) + " ");
            //System.out.println(str);
        }

        if (k == 1) {
            result += countLeafEdges(root);
        }

        return result;
    }

    private long countLeafEdges(Node node) {
        long result = 0;
        for (Edge edge : node.getEdges()) {
            if (edge.getChild().isLeaf()) {
                result += edge.getEndIndex() - edge.getStartIndex();
            } else {
                result += countLeafEdges(edge.getChild());
            }
        }
        return result;
    }

    private long prefixCalc(Edge edge, int pos, List<Integer> prefix, List<Character> str, List<Edge> edges, List<Long> islandsCount, int k) {
        long result = 0;
        if (!edge.getChild().isLeaf() || pos < edge.getParent().getStrLen()) {
            if (edge.getEndIndex() - edge.getStartIndex() < pos) {
                for (Edge ed : edge.getChild().getEdges()) {
                    result += prefixCalc(ed, 0, prefix, str, edges, islandsCount, k);
                }
            } else {
                char currChar = edge.getChar(pos);
                int kmp = kmp(str, prefix, currChar);

                prefix.add(kmp);
                str.add(currChar);
                edges.add(edge);
                islandsCount.add(edge.getChild().getSuffixCount());

                //System.out.println("Add");
                //System.out.println(str);
                //System.out.println(prefix);
                //System.out.println(islandsCount);
                //System.out.println("==========");

                if (kmp >= prefix.size() - kmp) {
                    long prevIslands = islandsCount.get(kmp - 1);
                    islandsCount.set(kmp - 1, prevIslands - edge.getChild().getSuffixCount());
                }

                result += prefixCalc(edge, pos + 1, prefix, str, edges, islandsCount, k);

                //System.out.print(islandsCount.get(islandsCount.size() - 1) + " ");
                //System.out.println(str);

                if (islandsCount.get(islandsCount.size() - 1) == k && !edge.getChild().isLeaf()) {
                    result++;
                    //System.out.println(str);
                }

                str.remove(str.size() - 1);
                prefix.remove(prefix.size() - 1);
                edges.remove(edges.size() - 1);
                islandsCount.remove(islandsCount.size() - 1);
            }
        }

        return result;
    }

    private int kmp(List<Character> str, List<Integer> prefix, char currentChar) {
        int prevIndex = str.size() - 1;
        while (str.get(prefix.get(prevIndex)) != currentChar && prefix.get(prevIndex) != 0) {
            prevIndex = prefix.get(prevIndex) - 1;
        }

        if (str.get(prefix.get(prevIndex)) == currentChar) {
           return prefix.get(prevIndex) + 1;
        } else {
            return 0;
        }
    }

    private  void setStrLen(Node node, int len) {
        for (Edge childEdge : node.getEdges()) {
            int newLen = len + childEdge.getEndIndex() - childEdge.getStartIndex() + 1;
            childEdge.getChild().setStrLen(newLen);
            setStrLen(childEdge.getChild(), newLen);
        }
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
        private Node parent;
        private Node suffix = null;
        private SuffixTreeInfo treeInfo;
        private int strLen = 0;
        private int suffixCount = -1;

        private int childCnt = 0;
        private Edge[] edgesMap2 = new Edge[27];

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
                            //.append("_").append(e.getChild().strLen)
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

        public char getChar(int pos) {
            return treeInfo.fullString.charAt(startIndex + pos);
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
}
