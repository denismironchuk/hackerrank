package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MatrixTree {
    private static final int N = 10;
    private static final int MAX_WEIGHT = 20;
    private static final int CHILD_LIM = N / 3;

    private static Node[] nodes = new Node[N];

    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private static class Node {
        private int num;
        private List<Node> children = new ArrayList<>();
        private Node parent;
        private long weight;

        public Node(int num) {
            this.num = num;
            nodes[num] = this;
        }

        public Node(int num, long weight) {
            this(num);
            this.weight = weight;
        }

        public void addChild(Node child) {
            children.add(child);
            child.setParent(this);
        }

        private void setParent(Node parent) {
            this.parent = parent;
        }
    }

    public static void main(String[] args) {
        int n = N;

        Node start = new Node(0, (long)(Math.random() * MAX_WEIGHT));
        nodes[0] = start;

        Queue<Node> q = new LinkedList<>();
        q.add(start);

        int index = 1;
        n -= 1;

        Node root = start;

        while (n != 0) {
            Node curr = q.poll();

            /*if (curr.parent == null && Math.random() > 0.2) {
                Node parent = new Node(index, (long)(Math.random() * MAX_WEIGHT));
                parent.addChild(curr);
                index++;
                n--;
                q.add(parent);

                root = parent;
            }*/

            int childrenCnt = Math.min(CHILD_LIM, (int)(n * Math.random()) + 1);

            n -= childrenCnt;

            for (int i = 0; i < childrenCnt; i++) {
                Node nd = new Node(index, (long)(Math.random() * MAX_WEIGHT));
                index++;
                curr.addChild(nd);
                q.add(nd);
            }
        }

        long[][] lca = new long[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                lca[i][j] = lca(nodes, nodes[i], nodes[j]).weight;
            }
        }

        printLca(lca);

        System.out.println();

        Queue<Node> q2 = new LinkedList<>();
        q2.add(root);

        while (!q2.isEmpty()) {
            Node curr = q2.poll();
            List<Node> descs =  getAllDescendantsStream(curr);
            descs.forEach(nd -> substractFrmDesc(lca, curr.num, nd.num));

            printLca(lca);

            try {
                br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            q2.addAll(curr.children);
        }

        System.out.println();
    }

    private static List<Node> getAllDescendantsStream(Node nd) {
        List<Node> descs = nd.children.stream().flatMap(c -> getAllDescendantsStream(c).stream()).collect(Collectors.toList());
        descs.addAll(nd.children);
        return descs;
    }

    private static void substractFrmDesc(long[][] lca, int toSubstr, int fromSubstr) {
        for (int i = 0; i < N; i++) {
            lca[fromSubstr][i] -= lca[toSubstr][i];
        }
    }

    private static void printLca(long[][] lca) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.printf("%3d ", lca[i][j]);
            }
            System.out.println();
        }
    }

    private static Node lca(Node[] nodes, Node n1, Node n2) {
        Set<Node> pathToRoot1 = new HashSet<>();
        while (n1 != null) {
            pathToRoot1.add(n1);
            n1 = n1.parent;
        }

        while (!pathToRoot1.contains(n2)) {
            n2 = n2.parent;
        }

        return n2;
    }
}
