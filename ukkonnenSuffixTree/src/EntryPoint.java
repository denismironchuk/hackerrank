import java.util.*;

/**
 * Created by Denis_Mironchuk on 6/21/2018.
 */
public class EntryPoint {
    public static void main(String[] args) {
        int len = 100000;
        StringBuilder build = new StringBuilder();

        for (int i = 0; i < len; i++) {
            build.append((char) ('a' + (int) (Math.random() * 20)));
        }
        build.append((char)('z' + 1));
        String str = build.toString();
        //System.out.println(str);

        Date start = new Date();
        Node root = SuffixTreeApp.buildTreeOptimal(str);

        System.out.println("Verts count = " + countNodes(root));
        System.out.println("Total edge len = " + countEdgesLen(root));
        setStrLen(root, 0);
        //System.out.println(root.buildTree());
        List<Node> nonLeafNodes = new ArrayList<>();
        collectAllNonFeafNodes(root, nonLeafNodes);
        nonLeafNodes.sort((node1, node2) -> Long.compare(node2.getStrLen(), node1.getStrLen()));
        Set<Node> processedNodes = new HashSet<>();
        long lenToProcess = 0;

        for (Node node : nonLeafNodes) {
            if (!processedNodes.contains(node)) {
                lenToProcess += node.getStrLen();
                //processedNodes.add(node);
                //Node suffix = node.getSuffix();
                //while (suffix != null) {
                //    processedNodes.add(suffix);
                //    suffix = suffix.getSuffix();
                //}

            }
        }

        System.out.println("lenToProcess = " + lenToProcess + " !!!!!");

        Date end = new Date();

        System.out.println(end.getTime() - start.getTime() + "ms");
    }

    private static void collectAllNonFeafNodes(Node node, List<Node> nodes) {
        if (!node.isLeaf()) {
            nodes.add(node);
            for (Node child : node.getChildenNodes()) {
                collectAllNonFeafNodes(child, nodes);
            }
        }
    }

    private static void setStrLen(Node node, long len) {
        for (Edge childEdge : node.getChildenEdges()) {
            long newLen = len + childEdge.getEndIndex() - childEdge.getStartIndex() + 1;
            childEdge.getChild().setStrLen(newLen);
            setStrLen(childEdge.getChild(), newLen);
        }
    }

    private static int countNodes(Node node) {
        int cnt = 1;
        for (Node child : node.getChildenNodes()) {
            cnt += countNodes(child);
        }
        return cnt;
    }

    private static long countEdgesLen(Node node) {
        long len = 0;
        for (Edge childEdge : node.getChildenEdges()) {
            if (!childEdge.getChild().isLeaf()) {
                len += childEdge.getEndIndex() - childEdge.getStartIndex() + 1;
            }
            len += countEdgesLen(childEdge.getChild());
        }
        return len;
    }
}
