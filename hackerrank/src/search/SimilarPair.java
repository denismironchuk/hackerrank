package search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

public class SimilarPair {
    private static int k = 5000;
    private static int n = 10000;

    private static class Node {
        private int num;
        private Node parent;
        private List<Node> children = new ArrayList<>();

        public Node(int num) {
            this.num = num;
        }

        public void addChild(Node child) {
            children.add(child);
            child.setParent(this);
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }
    }

    private static Node[] generateRandomGraph() {
        Node[] nodes = new Node[n];
        Node root = new Node(0);
        nodes[0] = root;
        //addChildren(root, nodes, 1);

        int i = 1;
        while (i < n) {
            Node parent = nodes[(int)(i * Math.random())];
            Node child = new Node(i);
            nodes[i] = child;
            parent.addChild(child);
            i++;
        }

        return nodes;
    }

    private static Node[] generateFromInput() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader br = new BufferedReader(new FileReader("/Users/denis_mironchuk/test"));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        n = Integer.parseInt(tkn1.nextToken());
        k = Integer.parseInt(tkn1.nextToken());

        Node[] nodes = new Node[n];
        for (int i = 0; i < n - 1; i++) {
            StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
            int p = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int c = Integer.parseInt(edgeTkn.nextToken()) - 1;

            Node parent = nodes[p] == null ? new Node(p) : nodes[p];
            Node child = nodes[c] == null ? new Node(c) : nodes[c];
            nodes[p] = parent;
            nodes[c] = child;
            parent.addChild(child);
        }

        return nodes;
    }

    public static void main(String[] args) throws IOException {
        Node[] nodes = generateFromInput();
        //while (true) {
            //Node[] nodes = generateRandomGraph();

            long optCnt = 0;
            Node root = null;
            for (Node node : nodes) {
                if (null != node) {
                    if (node.parent == null) {
                        root = node;
                        break;
                    }
                }
            }

        long[] segTree = new long[n * 4];
        optCnt += dfsNoRecur(root, segTree);

            /*int trivCnt = countTrivial(root, new ArrayList<>());

            System.out.println(optCnt + " " + trivCnt);

            if (optCnt != trivCnt) {
                throw new RuntimeException("My exception");
            }*/
        //}
        System.out.println(optCnt);
    }

    private static int countTrivial(Node node, List<Node> path) {
        int cnt = 0;
        path.add(node);
        for (Node child : node.children) {
            cnt += countTrivial(child, path);
        }
        path.remove(path.size() - 1);

        for (Node pathNode : path) {
            if (Math.abs(pathNode.num - node.num) <= k) {
                cnt++;
            }
        }

        return cnt;
    }

    private static long dfs(Node node, long[] segTree) {
        long cnt = 0;
        updateSegTree(segTree, 1, 0, n - 1, node.num, 1);
        for (Node child : node.children) {
            cnt += dfs(child, segTree);
        }

        updateSegTree(segTree, 1, 0, n - 1, node.num, -1);
        cnt += getCount(segTree, 1, 0, n - 1, Math.max(0, node.num - k), Math.min(node.num + k, n - 1));

        return cnt;
    }

    private static long dfsNoRecur(Node root, long[] segTree) {
        long cnt = 0;
        Stack<Node> st = new Stack<>();
        st.push(root);
        int[] status = new int[n];

        while (!st.isEmpty()) {
            Node node = st.peek();
            if (status[node.num] == 0) {
                status[node.num] = 1;
                updateSegTree(segTree, 1, 0, n - 1, node.num, 1);
                for (Node child : node.children) {
                    st.push(child);
                }
            } else {
                st.pop();
                updateSegTree(segTree, 1, 0, n - 1, node.num, -1);
                cnt += getCount(segTree, 1, 0, n - 1, Math.max(0, node.num - k), Math.min(node.num + k, n - 1));
            }
        }

        return cnt;
    }

    private static long getCount(long[] tree, int v, int l, int r, int intrL, int intrR) {
        if (intrR < intrL) {
            return 0;
        }

        if (l == intrL && r == intrR) {
            return tree[v];
        } else {
            int mid = (l + r) / 2;
            return getCount(tree, 2 * v, l, mid, intrL, Math.min(mid, intrR)) +
                    getCount(tree, 2 * v + 1, mid + 1, r, Math.max(mid + 1, intrL), intrR);
        }
    }

    private static void updateSegTree(long[] tree, int v, int l, int r, int pos, int incrVal) {
        if (l == r && l == pos) {
            tree[v] += incrVal;
        } else {
            int mid = (l + r) / 2;
            if (pos <= mid) {
                updateSegTree(tree, 2 * v, l, mid, pos, incrVal);
            } else {
                updateSegTree(tree, (2 * v) + 1, mid + 1, r, pos, incrVal);
            }
            tree[v] = tree[2 * v] + tree[(2 * v) + 1];
        }
    }
}