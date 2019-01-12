package findPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Denis_Mironchuk on 1/2/2019.
 */
public class Node {
    private int nodeWeight;
    private int row;
    private int col;
    private int index;
    private Map<Node, Integer> edges = new HashMap<>();

    public Node(final int row, final int col, final int nodeWeight) {
        this.row = row;
        this.col = col;
        this.nodeWeight = nodeWeight;
    }

    public void addNeigh(Node neigh, int dist) {
        edges.put(neigh, dist);
    }

    public int getNodeWeight() {
        return nodeWeight;
    }

    public Map<Node, Integer> getEdges() {
        return edges;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(final int index) {
        this.index = index;
    }
}
