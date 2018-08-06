package utils.ukkonnen;

public class Edge {
    Node parent;
    Node child;
    String edgeStr;

    public Edge(final Node parent, final Node child, final String s) {
        this.parent = parent;
        this.child = child;
        this.edgeStr = s;
    }

    public void process(String str) {
        int commonSymbols = 0;
        for (;commonSymbols < str.length() && commonSymbols < edgeStr.length() && edgeStr.charAt(commonSymbols) == str.charAt(commonSymbols); commonSymbols++);

        if (commonSymbols == edgeStr.length()) {
            if (child.edgesMap.isEmpty()) {
                edgeStr += str.substring(commonSymbols);
            } else {
                child.process(str.substring(commonSymbols));
            }
        } else if (commonSymbols < str.length() && commonSymbols < edgeStr.length()) {
            splitEdge(str, commonSymbols);
        }
    }

    private void splitEdge(final String str, final int commonSymbols) {
        Node middleVerticle = new Node();
        middleVerticle.setParentEdge(this);

        Edge bottomEdge = new Edge(middleVerticle, child, edgeStr.substring(commonSymbols));
        middleVerticle.addEdge(bottomEdge);
        child.setParentEdge(bottomEdge);

        this.child = middleVerticle;
        this.edgeStr = edgeStr.substring(0, commonSymbols);

        middleVerticle.process(str.substring(commonSymbols));
    }
}
