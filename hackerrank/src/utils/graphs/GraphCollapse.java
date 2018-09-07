package utils.graphs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 9/7/2018.
 */
public class GraphCollapse {
    static class Node {
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
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        List<Node> nodes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            nodes.add(new Node(i));
        }

        int m = Integer.parseInt(br.readLine());

        for (int i = 0; i < m; i++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int v1 = Integer.parseInt(tkn.nextToken());
            int v2 = Integer.parseInt(tkn.nextToken());

            nodes.get(v1).addNeigh(nodes.get(v2));
            nodes.get(v2).addNeigh(nodes.get(v1));
        }

        StringTokenizer collapseTkn = new StringTokenizer(br.readLine());
        Set<Integer> nodesToCollapse = new HashSet<>();

        while (collapseTkn.hasMoreTokens()) {
            nodesToCollapse.add(Integer.parseInt(collapseTkn.nextToken()));
        }

        Node fake1 = collapseNodes(nodes, nodesToCollapse);

        collapseTkn = new StringTokenizer(br.readLine());
        nodesToCollapse = new HashSet<>();

        while (collapseTkn.hasMoreTokens()) {
            nodesToCollapse.add(Integer.parseInt(collapseTkn.nextToken()));
        }

        Node fake2 = collapseNodes(nodes, nodesToCollapse);

        fake2.unwrap();
        fake1.unwrap();

        System.out.println();
    }

    private static Node collapseNodes(List<Node> nodes, Set<Integer> nodesToCollapse) {
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
