package codejam.year2022.round3;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GraphPaintingTest {

    private static final int N = 1000;
    private static final int COLORS =6;

    private static class Node {
        private int index;
        private Node left;
        private Node right;
        private Set<Node> inNodes = new HashSet<>();
        private Set<Integer> inColors = new HashSet<>();

        private int color = -1;

        public Node(int index) {
            this.index = index;
        }
    }
    public static void main(String[] args) {
        int cnt = 0;
        while (true) {
            if (cnt % 1 == 0) {
                System.out.println(cnt);
            }
            cnt++;

            Node[] nodes = new Node[N];
            for (int i = 0; i < N; i++) {
                nodes[i] = new Node(i);
            }
            Random rnd = new Random();
            for (int i = 0; i < N; i++) {
                Node curNode = nodes[i];

                int leftIndex = i;
                while (leftIndex == i || nodes[leftIndex].left == curNode || nodes[leftIndex].right == curNode) {
                    leftIndex = rnd.nextInt(N);
                }

                int rightIndex = i;
                while (rightIndex == leftIndex || rightIndex == i || nodes[rightIndex].left == curNode || nodes[rightIndex].right == curNode) {
                    rightIndex = rnd.nextInt(N);
                }

                curNode.left = nodes[leftIndex];
                curNode.right = nodes[rightIndex];

                curNode.left.inNodes.add(curNode);
                curNode.right.inNodes.add(curNode);
            }

            for (Node node : nodes) {
                Set<Integer> colors = occupiedColours(node);
                if (colors.size() == COLORS) {
                    throw new RuntimeException("I can't assign colors correctly");
                }
                int color = 0;
                for (; colors.contains(color); color++) ;
                node.color = color;
                addIngoingColor(node, color, 2);
            }

            //System.out.println();
        }
    }

    private static Set<Integer> occupiedColours(Node n) {
        Set<Integer> res = new HashSet<>();
        getOutgoingColors(n, 2, res);

        Set<Node> nodeForIngoing = new HashSet<>();
        nodeForIngoing.add(n);
        //getIngoingColors(nodeForIngoing, 2, res);
        res.addAll(n.inColors);
        res.remove(-1);
        return res;
    }

    private static void getIngoingColors(Set<Node> nodes, int level, Set<Integer> colors) {
        if (level == 0) {
            return;
        }

        Set<Node> nextNodes = new HashSet<>();
        for (Node node : nodes) {
            colors.add(node.color);
            nextNodes.addAll(node.inNodes);
        }
        getIngoingColors(nextNodes, level - 1, colors);
    }

    private static void addIngoingColor(Node n, int color, int level) {
        if (level == 0) {
            return;
        }
        n.inColors.add(color);
        addIngoingColor(n.left, color, level - 1);
        addIngoingColor(n.right, color, level - 1);
    }

    private static void getOutgoingColors(Node n, int level, Set<Integer> colors) {
        if (level == 0) {
            return;
        }

        colors.add(n.color);

        getOutgoingColors(n.left, level - 1, colors);
        getOutgoingColors(n.right, level - 1, colors);
    }
}
