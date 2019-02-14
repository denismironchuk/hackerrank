package sorting;

public class TreapTest {
    private static final int Y_DISP = 100;
    private static class Node {
        private int x;
        private int y;

        private Node l;
        private Node r;

        private int size = 0;

        public Node(final int x) {
            this.x = x;
            this.y = (int)(Y_DISP * Math.random());
        }

        public void print() {
            System.out.printf("%d ", x);
            if (l != null) {
                l.print();
            }
            if (r != null) {
                r.print();
            }
        }
    }

    private static Node[] split(Node nd, int x) {
        if (nd == null) {
            return new Node[2];
        }

        if (x < nd.x) {
            Node[] res = split(nd.l, x);
            nd.l = res[1];
            nd.size = (nd.r == null ? 0 : (nd.r.size + 1)) + (nd.l == null ? 0 : (nd.l.size + 1));
            return new Node[] {res[0], nd};
        } else {
            Node[] res = split(nd.r, x);
            nd.r = res[0];
            nd.size = (nd.r == null ? 0 : (nd.r.size + 1)) + (nd.l == null ? 0 : (nd.l.size + 1));
            return new Node[] {nd, res[1]};
        }
    }

    private static Node merge(Node less, Node greater) {
        if (less == null) {
            return greater;
        }

        if (greater == null) {
            return less;
        }

        if (less.y >= greater.y) {
            less.r = merge(less.r, greater);
            less.size = (less.r == null ? 0 : (less.r.size + 1)) + (less.l == null ? 0 : (less.l.size + 1));
            return less;
        } else {
            greater.l = merge(less, greater.l);
            greater.size = (greater.r == null ? 0 : (greater.r.size + 1)) + (greater.l == null ? 0 : (greater.l.size + 1));
            return greater;
        }
    }

    public static void main(String[] args) {
        int max = 10;
        int rootCh = (int)(max * Math.random());
        Node root = new Node(rootCh);
        System.out.printf("%d ", rootCh);
        int n = 10;
        for (int i = 0 ; i < n; i++) {
            int ch = (int)(max * Math.random());
            System.out.printf("%d ", ch);
            Node nd = new Node(ch);
            Node[] splitted = split(root, ch);
            root = merge(merge(splitted[0], nd), splitted[1]);
        }
        System.out.println();
        System.out.println(root.x);
        if (null != root.l) {
            System.out.println("Left");
            root.l.print();
        }
        System.out.println();
        if (null != root.r) {
            System.out.println("Right");
            root.r.print();
        }

        Node[] res = split(root, root.x);
        System.out.println();
        if (null != res[0]) {
            System.out.println("Left");
            res[0].print();
            System.out.println("\nSize = " + (res[0].size + 1));
        }
        System.out.println();
        if (null != res[1]) {
            System.out.println("Right");
            res[1].print();
            System.out.println("\nSize = " + (res[1].size + 1));
        }
    }
}
