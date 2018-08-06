package utils.ukkonnen;

import java.util.ArrayList;
import java.util.List;

public class CompressedTrie {
    class Node {
        Edge parentEdge = null;
        Edge[] edgesMap = new Edge[26];

        public void process(String s) {
            if (s.length() == 0) {
                return;
            }

            Edge edge = edgesMap[s.charAt(0) - 'a'];

            if (null == edge) {
                Node child = new Node();
                edgesMap[s.charAt(0) - 'a'] = new Edge(this, child, s);
                child.setParentEdge(edgesMap[s.charAt(0) - 'a']);
            } else {
                edgesMap[s.charAt(0) - 'a'].process(s);
            }
        }

        public void setParentEdge(final Edge parentEdge) {
            this.parentEdge = parentEdge;
        }

        public void addEdge(Edge edge) {
            edgesMap[edge.edgeStr.charAt(0) - 'a'] = edge;
        }

        @Override
        public String toString() {
            StringBuilder res = new StringBuilder();
            boolean isFirst = true;
            boolean hasAny = false;
            for (int i = 0; i < 26; i++) {
                Edge e = edgesMap[i];
                if (null != e) {
                    if (!isFirst) {
                        res.append(",");
                    }
                    res.append("\'").append(e.edgeStr).append("\':").append(e.child.toString()).append("");
                    isFirst = false;
                    hasAny=true;
                }
            }
            return hasAny ? "{" + res.toString() + "}" : "\'\'";
        }
    }

    class Edge {
        Node parent;
        Node child;
        String edgeStr;

        public Edge(final Node parent, final Node child, final String s) {
            this.parent = parent;
            this.child = child;
            this.edgeStr = s;
        }

        public void process(String str) {
            int commonSymbols = 0;
            for (;commonSymbols < str.length() && commonSymbols < edgeStr.length() && edgeStr.charAt(commonSymbols) == str.charAt(commonSymbols); commonSymbols++);

            if (commonSymbols == edgeStr.length()) {
                child.process(str.substring(commonSymbols));
            } else if (commonSymbols < str.length() && commonSymbols < edgeStr.length()) {
                splitEdge(str, commonSymbols);
            }
        }

        private void splitEdge(final String str, final int commonSymbols) {
            Node middleVerticle = new Node();
            middleVerticle.setParentEdge(this);

            Edge bottomEdge = new Edge(middleVerticle, child, edgeStr.substring(commonSymbols));
            middleVerticle.addEdge(bottomEdge);
            child.setParentEdge(bottomEdge);

            this.child = middleVerticle;
            this.edgeStr = edgeStr.substring(0, commonSymbols);

            middleVerticle.process(str.substring(commonSymbols));
        }
    }

    private void run() {
        Node root = new Node();

        List<String> strings = new ArrayList<>();

        for (int n = 0; n < 15; n++) {
            StringBuilder builder = new StringBuilder();
            int len = 1 + (int) (10 * Math.random());
            for (int i = 0; i < len; i++) {
                builder.append((char) ('a' + (int) (Math.random() * 3)));
            }
            String s = builder.toString();
            strings.add(s);
            root.process(s);
        }

        strings.sort(String::compareTo);
        for (String s : strings) {
            System.out.println(s);
        }

        System.out.println(root);
    }

    public static void main(String[] args) {
        new CompressedTrie().run();
    }
}
