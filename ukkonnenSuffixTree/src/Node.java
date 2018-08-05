import java.util.ArrayList;
import java.util.List;

public class Node {
    private Node suffix = null;
    private SuffixTreeInfo treeInfo;
    private int num;
    private long strLen = 0;
    private int suffixNum = -1;

    private int childCnt = 0;
    private Edge[] edgesMap2 = new Edge[27];

    public Node(final SuffixTreeInfo treeInfo) {
        this.treeInfo = treeInfo;
        this.num = treeInfo.getVertCnt();
    }

    public boolean process(char c) {
        Edge edge = edgesMap2[c - 'a'];
        treeInfo.setLastVisitedNode(this);

        boolean terminate = false;
        if (null == edge) {
            Node child = new Node(treeInfo);
            child.suffixNum = treeInfo.getSuffixNum();
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

    public int getSuffixNum() {
        if (null != suffix) {
            return suffix.num;
        } else {
            return 0;
        }
    }

    public void setStrLen(long len) {
        strLen = len;
    }

    public long getStrLen() {
        return strLen;
    }

    public List<Node> getChildenNodes() {
        List<Node> res = new ArrayList<>();

        for (Edge edge : edgesMap2) {
            if (null != edge) {
                res.add(edge.getChild());
            }
        }

        return res;
    }

    public List<Edge> getChildenEdges() {
        List<Edge> res = new ArrayList<>();

        for (Edge edge : edgesMap2) {
            if (null != edge) {
                res.add(edge);
            }
        }

        return res;
    }

    public int getNum() {
        return num;
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
                res.append("\"")//.append(e.getChild().num + "->" + e.getChild().getSuffixNum()).append("_")
                        .append(fullString.substring(e.getStartIndex(), e.getEndIndex() + 1)).append("_").append(e.getChild().suffixNum).append("\":")
                        .append(e.getChild().buildTree()).append("");
                isFirst = false;
                hasAny=true;
            }
        }
        return hasAny ? "{" + res.toString() + "}" : "\"\"";
    }
}
