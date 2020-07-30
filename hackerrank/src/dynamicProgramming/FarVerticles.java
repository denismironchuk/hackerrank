package dynamicProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class FarVerticles {

    private static Node[] nodes;
    private static TreeNode[] treeNodes;

    private static class Node {
        private int num;
        private List<Node> neighbours = new ArrayList<>();

        public Node(int num) {
            this.num = num;
        }

        public void addNeighbour(Node neighbour) {
            neighbours.add(neighbour);
        }
    }

    private static class TreeNode {
        private int num;
        private TreeNode parent;
        private List<TreeNode> children = new ArrayList<>();
        private int[] dyn;

        public TreeNode(int num, int k) {
            this.num = num;
            this.dyn = new int[k + 1];
        }

        public void clear() {
            this.parent = null;
            children.clear();
            Arrays.fill(dyn, 0);
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            StringTokenizer line1Tkn = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(line1Tkn.nextToken());
            int k = Integer.parseInt(line1Tkn.nextToken());

            nodes = new Node[n + 1];
            treeNodes = new TreeNode[n + 1];

            for (int i = 1; i <= n; i++) {
                nodes[i] = new Node(i);
                treeNodes[i] = new TreeNode(i, k);
            }

            for (int i = 0; i < n - 1; i++) {
                StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
                int v1 = Integer.parseInt(edgeTkn.nextToken());
                int v2 = Integer.parseInt(edgeTkn.nextToken());
                nodes[v1].addNeighbour(nodes[v2]);
                nodes[v2].addNeighbour(nodes[v1]);
            }

            int res = Integer.MAX_VALUE;

            for (int i = 1; i <= n; i++) {
                for (TreeNode node : treeNodes) {
                    if (node != null) {
                        node.clear();
                    }
                }
                buildTree(nodes[i]);
                fillDynTable(treeNodes[i], k);
                res = Math.min(res, treeNodes[i].dyn[k]);
            }

            System.out.println(res);
        }
    }

    private static void buildTree(Node nd) {
        TreeNode treeNode = treeNodes[nd.num];

        for (Node neighbour : nd.neighbours) {
            if (treeNode.parent != null && treeNode.parent.num == neighbour.num) {
                continue;
            }

            treeNode.children.add(treeNodes[neighbour.num]);
            treeNodes[neighbour.num].parent = treeNode;
            buildTree(neighbour);
        }
    }

    private static void fillDynTable(TreeNode nd, int k) {
        if (nd.children.size() == 0) {
            return;
        }

        for (TreeNode child : nd.children) {
            fillDynTable(child, k);
            nd.dyn[0] += child.dyn[0] + 1;
        }

        for (int h1 = 1; h1 <= k; h1++) {
            int min = Integer.MAX_VALUE;

            for (TreeNode child1 : nd.children) {
                int minSum = child1.dyn[h1 - 1];
                for (TreeNode child2 : nd.children) {
                    if (child2 == child1) {
                        continue;
                    }
                    if (h1 == k) {
                        minSum += child2.dyn[0] + 1;
                    } else {
                        minSum += child2.dyn[Math.min(k - h1 - 1, h1 - 1)];
                    }
                }
                min = Math.min(min, minSum);
            }

            nd.dyn[h1] = min;
        }

        for (int h = 1; h <= k; h++) {
            nd.dyn[h] = Math.min(nd.dyn[h], nd.dyn[h - 1]);
        }
    }
}
