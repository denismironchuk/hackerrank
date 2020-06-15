package codejam.year2020.round2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class Emacs {
    private static class MoveTime {
        private long toLeft = 0;
        private long toRight = 0;
        private long teleport = 0;
    }

    private static class Parenthesis {
        private Parenthesis parent;
        private List<Parenthesis> children = new ArrayList<>();
        private int position = -1;

        private MoveTime openTime = new MoveTime();
        private MoveTime closeTime = new MoveTime();

        private long fromOpenToCloseTiming = 0;
        private long fromCloseToOpenTiming = 0;

        long[] timeFromOpeningToInnerNodesOpenings;
        long[] timeFromClosingToInnerNodesClosings;

        long[] timeFromInnerNodesOpeningsToOpening;
        long[] timeFromInnerNodesClosingsToClosing;

        public Parenthesis(Parenthesis parent) {
            this.parent = parent;
            if (null != this.parent) {
                this.parent.addChild(this);
            }
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void setParent(Parenthesis parent) {
            this.parent = parent;
        }

        public void addChild(Parenthesis childParenthesis) {
            childParenthesis.setPosition(children.size());
            childParenthesis.setParent(this);
            children.add(childParenthesis);
        }

        @Override
        public String toString() {
            return "(" + children.stream().map(Parenthesis::toString).collect(Collectors.joining()) + ")";
        }
    }

    public static void main(String[] aggs) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int k = Integer.parseInt(tkn1.nextToken());
                int q = Integer.parseInt(tkn1.nextToken());
                String p = br.readLine();
                Parenthesis[] tree = new Parenthesis[k];
                Stack<Parenthesis> stack = new Stack<>();
                Parenthesis root = new Parenthesis(null);
                stack.push(root);

                for (int i = 0; i < k; i++) {
                    Parenthesis current = stack.peek();

                    if (p.charAt(i) == '(') {
                        tree[i] = new Parenthesis(current);
                        stack.push(tree[i]);
                    } else {
                        tree[i] = current;
                        stack.pop();
                    }
                }

                StringTokenizer leftTkn = new StringTokenizer(br.readLine());
                StringTokenizer rightTkn = new StringTokenizer(br.readLine());
                StringTokenizer teleportTkn = new StringTokenizer(br.readLine());

                for (int i = 0; i < k; i++) {
                    MoveTime moveTime = p.charAt(i) == '(' ? tree[i].openTime : tree[i].closeTime;
                    moveTime.toLeft = Long.parseLong(leftTkn.nextToken());
                    moveTime.toRight = Long.parseLong(rightTkn.nextToken());
                    moveTime.teleport = Long.parseLong(teleportTkn.nextToken());
                }

                countFromOpenToCloseTiming(root);
                countFromCloseToOpenTiming(root);

                initTimeFromOpeningToInnerNodesOpenings(root);
                initTimeFromClosingToInnerNodesClosings(root);

                initTimeFromInnerNodesOpeningsToOpening(root);
                initTimeFromInnerNodesClosingsToClosing(root);

                System.out.println();
            }
        }
    }

    /**
     * (  (  (  (  )  )  )  (  (  )  )  (  )  )
     * |------------------------------------->|
     */
    private static long countFromOpenToCloseTiming(Parenthesis node) {
        node.fromOpenToCloseTiming = node.openTime.toRight;
        for (Parenthesis child : node.children) {
            node.fromOpenToCloseTiming += countFromOpenToCloseTiming(child);
            node.fromOpenToCloseTiming += child.closeTime.toRight;
        }
        node.fromOpenToCloseTiming = Math.min(node.fromOpenToCloseTiming, node.openTime.teleport);

        return node.fromOpenToCloseTiming;
    }

    /**
     * (  (  (  (  )  )  )  (  (  )  )  (  )  )
     * |<-------------------------------------|
     */
    private static long countFromCloseToOpenTiming(Parenthesis node) {
        node.fromCloseToOpenTiming = node.closeTime.toLeft;
        for (Parenthesis child : node.children) {
            node.fromCloseToOpenTiming += countFromCloseToOpenTiming(child);
            node.fromCloseToOpenTiming += child.openTime.toLeft;
        }
        node.fromCloseToOpenTiming = Math.min(node.fromCloseToOpenTiming, node.closeTime.teleport);

        return node.fromCloseToOpenTiming;
    }

    /**
     * (  (  (  (  )  )  )  (  (  )  )  (  )  )
     * |->|  |  |           |  |        |
     * |---->|  |           |  |        |
     * |------->|           |  |        |
     * |------------------->|  |        |
     * |---------------------->|        |
     * |------------------------------->|
     */
    private static void initTimeFromOpeningToInnerNodesOpenings(Parenthesis node) {
        node.timeFromOpeningToInnerNodesOpenings = new long[node.children.size()];
        long time = node.openTime.toRight;
        for (int index = 0; index < node.children.size(); index++) {
            Parenthesis child = node.children.get(index);
            node.timeFromOpeningToInnerNodesOpenings[index] = time;
            time += child.fromOpenToCloseTiming + child.closeTime.toRight;

            initTimeFromOpeningToInnerNodesOpenings(child);
        }
    }

    /**
     * (  (  (  (  )  )  )  (  (  )  )  (  )  )
     *             |  |  |        |  |     |<-|
     *             |  |  |        |  |<-------|
     *             |  |  |        |<----------|
     *             |  |  |<-------------------|
     *             |  |<----------------------|
     *             |<-------------------------|
     */
    private static void initTimeFromClosingToInnerNodesClosings(Parenthesis node) {
        node.timeFromClosingToInnerNodesClosings = new long[node.children.size()];
        long time = node.closeTime.toLeft;
        for (int index = node.children.size() - 1; index >= 0; index--) {
            Parenthesis child = node.children.get(index);
            node.timeFromClosingToInnerNodesClosings[index] = time;
            time += child.fromCloseToOpenTiming + child.openTime.toLeft;

            initTimeFromClosingToInnerNodesClosings(child);
        }
    }

    /**
     * (  (  (  (  )  )  )  (  (  )  )  (  )  )
     * |<-|  |  |           |  |        |
     * |<----|  |           |  |        |
     * |<-------|           |  |        |
     * |<-------------------|  |        |
     * |<----------------------|        |
     * |<-------------------------------|
     */
    private static void initTimeFromInnerNodesOpeningsToOpening(Parenthesis node) {
        node.timeFromInnerNodesOpeningsToOpening = new long[node.children.size()];
        long time = 0;
        for (int index = 0; index < node.children.size(); index++) {
            Parenthesis child = node.children.get(index);
            time += child.openTime.toLeft;
            node.timeFromInnerNodesOpeningsToOpening[index] = time;
            time += child.fromCloseToOpenTiming;

            initTimeFromInnerNodesOpeningsToOpening(child);
        }
    }

    /**
     * (  (  (  (  )  )  )  (  (  )  )  (  )  )
     *             |  |  |        |  |     |->|
     *             |  |  |        |  |------->|
     *             |  |  |        |---------->|
     *             |  |  |------------------->|
     *             |  |---------------------->|
     *             |------------------------->|
     */
    private static void initTimeFromInnerNodesClosingsToClosing(Parenthesis node) {
        node.timeFromInnerNodesClosingsToClosing = new long[node.children.size()];
        long time = 0;
        for (int index = node.children.size() - 1; index >= 0; index--) {
            Parenthesis child = node.children.get(index);
            time += child.closeTime.toRight;
            node.timeFromInnerNodesClosingsToClosing[index] = time;
            time += child.fromOpenToCloseTiming;

            initTimeFromInnerNodesClosingsToClosing(child);
        }
    }
}
