package codejam.year2022.round3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class MascotMaze {

    private static final char[] COLORS = new char[] {'A','C','D','E','H','I','J','K','M','O','R','S','T'};

    private static class Node {
        private int index;
        private Node left;
        private Node right;
        private Set<Integer> inColors = new HashSet<>();

        private int color = -1;

        private int inCount = 0;

        public Node(int index) {
            this.index = index;
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int n = Integer.parseInt(br.readLine());
                List<Node> nodes = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    nodes.add(new Node(i));
                }
                StringTokenizer tknLeft = new StringTokenizer(br.readLine());
                StringTokenizer tknRight = new StringTokenizer(br.readLine());
                for (int i = 0; i < n; i++) {
                    int left = Integer.parseInt(tknLeft.nextToken()) - 1;
                    int right = Integer.parseInt(tknRight.nextToken()) - 1;
                    nodes.get(i).left = nodes.get(left);
                    nodes.get(i).right = nodes.get(right);
                }

                for (Node node : nodes) {
                    node.left.inCount++;
                    node.right.inCount++;

                    node.left.right.inCount++;
                    node.left.left.inCount++;

                    node.right.right.inCount++;
                    node.right.left.inCount++;
                }

                boolean impossible = false;
                boolean retry = true;
                while (retry) {
                    retry = false;
                    for (Node node : nodes) {
                        node.inColors.clear();
                        node.color = -1;
                    }

                    for (Node node : nodes) {
                        if (node.left.left == node || node.left.right == node
                                || node.right.left == node || node.right.right == node) {
                            impossible = true;
                            break;
                        }

                        Set<Integer> colors = new HashSet<>();
                        getOutgoingColors(node, 3, colors);
                        colors.addAll(node.inColors);

                        int color = 0;
                        for (; colors.contains(color); color++) ;
                        if (color >= COLORS.length) {
                            Collections.shuffle(nodes);
                            retry = true;
                        }
                        node.color = color;
                        addIngoingColor(node, color, 3);
                    }
                }

                if (impossible) {
                    System.out.printf("Case #%s: IMPOSSIBLE\n", t);
                } else {
                    String colorsAssignments = nodes.stream()
                            .sorted(Comparator.comparingInt(nd -> nd.index))
                            .map(node -> node.color)
                            .map(ind -> String.valueOf(COLORS[ind]))
                            .collect(Collectors.joining());
                    System.out.printf("Case #%s: %s\n", t, colorsAssignments);
                }
            }
        }
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
