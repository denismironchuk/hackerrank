package utils.graphs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Denis_Mironchuk on 9/7/2018.
 */
public class Node {
    private int num;
    private boolean fake = false;
    private Set<Node> neighbours = new HashSet<>();
    private Set<Node> collapsedNodes = new HashSet<>();

    public Node(final int num) {
        this.num = num;
    }

    public Node(final int num, final boolean fake) {
        this.num = num;
        this.fake = fake;
    }

    public int getNum() {
        return num;
    }

    public void addNeigh(Node neigh) {
        neighbours.add(neigh);
    }

    public Set<Node> getNeighbours() {
        return neighbours;
    }

    public boolean isFake() {
        return fake;
    }

    public void addCollapseNode(Node nd) {
        collapsedNodes.add(nd);
    }

    public void unwrap() {
        for (Node collapsed : collapsedNodes) {
            for (Node neigh : collapsed.getNeighbours()) {
                neigh.addNeigh(collapsed);
            }
        }

        for (Node neigh : neighbours) {
            neigh.getNeighbours().remove(this);
        }
    }

    public static Node collapseNodes(List<Node> nodes, Set<Integer> nodesToCollapse) {
        int n = nodes.size();
        Node fakeNode = new Node(n, true);
        nodes.add(fakeNode);

        for (Integer nodeNum : nodesToCollapse) {
            Node nd = nodes.get(nodeNum);
            fakeNode.addCollapseNode(nd);
            for (Node neigh : nd.getNeighbours()) {
                if (!nodesToCollapse.contains(neigh.getNum())) {
                    neigh.getNeighbours().remove(nd);
                    fakeNode.addNeigh(neigh);
                    neigh.getNeighbours().add(fakeNode);
                }
            }
        }

        return fakeNode;
    }
}
