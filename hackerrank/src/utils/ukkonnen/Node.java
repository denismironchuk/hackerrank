package utils.ukkonnen;

import java.util.Map;
import java.util.TreeMap;

public class Node {
    Edge parentEdge = null;
    Map<Character, Edge> edgesMap = new TreeMap<>();
    Node suffixLink = null;

    public void process(String s) {
        if (s.length() == 0) {
            return;
        }

        Edge edge = edgesMap.get(s.charAt(0));

        if (null == edge) {
            Node child = new Node();
            Edge newEdge = new Edge(this, child, s);
            edgesMap.put(s.charAt(0), newEdge);
            child.setParentEdge(newEdge);
        } else {
            edgesMap.get(s.charAt(0)).process(s);
        }
    }

    public void setParentEdge(final Edge parentEdge) {
        this.parentEdge = parentEdge;
    }

    public void addEdge(Edge edge) {
        edgesMap.put(edge.edgeStr.charAt(0), edge);
    }

    public Node getSuffix() {
        if (null == suffixLink) {
            if (parentEdge != null) {
                Node parentSuffix = parentEdge.parent.getSuffix();
                suffixLink = null == parentSuffix ? parentEdge.parent.followPath(parentEdge.edgeStr.substring(1)) : parentSuffix.followPath(parentEdge.edgeStr);;
            }
        }

        return suffixLink;
    }

    public Node followPath(String path) {
        if (path.length() == 0) {
            return this;
        }

        Edge e = edgesMap.get(path.charAt(0));
        return e.child.followPath(path.substring(e.edgeStr.length()));
    }

    public String buildTree() {
        StringBuilder res = new StringBuilder();
        boolean isFirst = true;
        boolean hasAny = false;
        for (char c : edgesMap.keySet()) {
            Edge e = edgesMap.get(c);
            if (null != e) {
                if (!isFirst) {
                    res.append(",");
                }
                res.append("\'").append(e.edgeStr).append("\':").append(e.child.buildTree()).append("");
                isFirst = false;
                hasAny=true;
            }
        }
        return hasAny ? "{" + res.toString() + "}" : "\'\'";
    }

    @Override
    public String toString() {
        if (null == parentEdge) {
            return "";
        } else {
            return parentEdge.parent.toString() + parentEdge.edgeStr;
        }
    }
}
