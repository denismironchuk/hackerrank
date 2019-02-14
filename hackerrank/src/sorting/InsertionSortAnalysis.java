package sorting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class InsertionSortAnalysis {
    private static final int Y_DISP = 10000000;

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

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            int n = Integer.parseInt(br.readLine());
            int[] arr = new int[n];
            StringTokenizer arrTkn = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                arr[i] = Integer.parseInt(arrTkn.nextToken());
            }

            Node root = new Node(arr[0]);
            long shifts = 0;
            for (int i = 1; i < n; i++) {
                Node nd = new Node(arr[i]);
                Node[] splitRes = split(root, arr[i]);
                shifts += null == splitRes[1] ? 0 : splitRes[1].size + 1;
                root = merge(merge(splitRes[0], nd), splitRes[1]);
            }
            System.out.println(shifts);
        }
    }
}
