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
