import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class LetterIslands {
    private static long KMP_TIME = 0;
    private static Set<Character> CHARS = new HashSet<>();
    private static char[] CHARS_ARRAY;
    private static int STR_LEN;
    private static int ARRAY_LEN = 0;

    public static void main(String[] args) throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new FileReader("D://letterIslands31.txt"));
        String s = br.readLine();

        /*int strLen = 100000;
        StringBuilder build = new StringBuilder();

        for (int i = 0; i < strLen; i++) {
            //build.append((char) ('a' + (char) (Math.random() * 2)));
            build.append("ab");
        }


        String s = build.toString();*/
        s+=(char)('z' + 1);
        STR_LEN = s.length();
        //System.out.println(s);

        int k = Integer.parseInt(br.readLine());
        Date start = new Date();
        System.out.println(new LetterIslands().countSubstrings(s, k));
        Date end = new Date();
        //System.out.println("KMP took " + KMP_TIME + " ms");
        System.out.println(end.getTime() - start.getTime() + "ms");
    }

    private long countSubstrings(String s, int k) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != '{') {
                CHARS.add(s.charAt(i));
            }
        }

        CHARS_ARRAY = new char[CHARS.size()];
        int i = 0;
        for (Character c : CHARS) {
            CHARS_ARRAY[i] = c;
            i++;
        }

        Node root = new SuffixTreeApp().buildTreeOptimal(s);
        setStrLenNoRecursion(root);
        setSuffixCountNoRecursion(root);
        //System.out.println(root.buildTree());
        long result = prefixCalcNoRecursion(root, k);

        /*if (k == 1) {
            result += countLeafEdges(root);
        }*/

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

    private long prefixCalcNoRecursion(Node root, int k) {
        Stack<Edge> stack = new Stack<>();
        Set<Edge> processed = new HashSet<>();

        long result = 0;
        //int edgeCnt = 0;

        char[] str = new char[STR_LEN];
        long[] islandsCount = new long[STR_LEN];
        int[][] transitions = new int[STR_LEN][CHARS_ARRAY.length];

        for (Edge ed : root.getEdges()) {

            stack.push(ed);

            while (!stack.isEmpty()) {
                /*edgeCnt++;
                if (edgeCnt % 10000 == 0) {
                    System.out.println(edgeCnt);
                }*/

                Edge edge = stack.peek();

                if (!processed.contains(edge)) {
                    processed.add(edge);
                    int pos = moveForward(edge, str, islandsCount, transitions);

                    if (!edge.getChild().isLeaf()) {
                        for (Edge nextEdge : edge.getChild().getEdges()) {
                            stack.push(nextEdge);
                        }
                    } else {
                        result += moveBackward(edge, pos, islandsCount, k);
                        stack.pop();
                    }
                } else {
                    int pos = edge.getEndIndex() - edge.getStartIndex() + 1;
                    result += moveBackward(edge, pos, islandsCount, k);
                    stack.pop();
                }
            }
        }

        return result;
    }

    private int moveForward(Edge edge, char[] str, long[] islandsCount, int[][] transitions) {
        int edgeLen = edge.getEndIndex() - edge.getStartIndex() + 1;
        int limit = edge.getChild().isLeaf() ? Math.min(edge.getParent().getStrLen(), edgeLen) : edgeLen;
        int pos = 0;

        while (pos < limit) {
            char currChar = edge.getChar(pos);
            str[ARRAY_LEN] = currChar;
            //Date kmpStart = new Date();
            int kmp = kmp(str, currChar, transitions);
            //Date kmpEnd = new Date();
            //KMP_TIME += kmpEnd.getTime() - kmpStart.getTime();

            islandsCount[ARRAY_LEN] = edge.getChild().getSuffixCount();
            ARRAY_LEN++;
            if (kmp >= ARRAY_LEN - kmp) {
                islandsCount[kmp - 1] -= edge.getChild().getSuffixCount();
            }

            pos++;
        }

        return pos;
    }

    private long moveBackward(Edge edge, int pos, long[] islandsCount, int k) {
        long result = 0;

        while (pos > 0) {
            if (islandsCount[ARRAY_LEN - 1] == k && !edge.getChild().isLeaf()) {
                result++;
            }

            pos--;
            ARRAY_LEN--;
        }

        return result;
    }

    private int kmp(char[] str, char currentChar, int[][] transitions) {
        if (ARRAY_LEN == 0 || currentChar == '{') {
            initFirstTransitions(str, transitions);
            return 0;
        }

        int[] lastTransitions = transitions[ARRAY_LEN - 1];
        int newPrefix = lastTransitions[currentChar - 'a'];

        if (newPrefix > 0) {
            int[] prefixTransitions = transitions[newPrefix - 1];
            for (char c : CHARS_ARRAY) {
                if (str[newPrefix] == c) {
                    transitions[ARRAY_LEN][c - 'a'] = newPrefix + 1;
                } else {
                    transitions[ARRAY_LEN][c - 'a'] = prefixTransitions[c - 'a'];
                }
            }
        } else {
            initFirstTransitions(str, transitions);
        }

        return newPrefix;
    }

    private void initFirstTransitions(char[] str, final int[][] transitions) {
        for (char c : CHARS_ARRAY) {
            if (str[0] == c) {
                transitions[ARRAY_LEN][c - 'a'] = 1;
            } else {
                transitions[ARRAY_LEN][c - 'a'] = 0;
            }
        }
    }


    private void setStrLenNoRecursion(Node root) {
        Queue<Edge> queue = new LinkedList<>();
        for (Edge edge : root.getEdges()) {
            queue.add(edge);
        }

        while (!queue.isEmpty()) {
            Edge edge = queue.poll();
            edge.getChild().setStrLen(edge.getParent().getStrLen() + edge.getEndIndex() - edge.getStartIndex() + 1);

            for (Edge childEdge : edge.getChild().getEdges()) {
                queue.add(childEdge);
            }
        }
    }

    private void setSuffixCountNoRecursion(Node root) {
        Stack<Node> stack = new Stack<>();
        Set<Node> processed = new HashSet<>();

        stack.push(root);
        while (!stack.isEmpty()) {
            Node nd = stack.peek();

            if (!processed.contains(nd)) {
                if (nd.isLeaf()) {
                    nd.setSuffixCount(1);
                    stack.pop();
                } else {
                    for (Edge edge : nd.getEdges()) {
                        stack.push(edge.getChild());
                    }
                }

                processed.add(nd);
            } else {
                for (Edge edge : nd.getEdges()) {
                    nd.setSuffixCount(nd.getSuffixCount() + edge.getChild().getSuffixCount());
                }
                stack.pop();
            }
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
        private long suffixCount = 0;

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
            return suffixCount;
        }

        public void setSuffixCount(final long suffixCount) {
            this.suffixCount = suffixCount;
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
