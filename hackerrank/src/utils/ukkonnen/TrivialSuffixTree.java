package utils.ukkonnen;

import java.util.ArrayList;
import java.util.List;

public class TrivialSuffixTree {
    public static void main(String[] main) {
        /*StringBuilder b = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            b.append((char)('a' + (int)(Math.random() * 3)));
        }*/
        //b.append("#");
        //String s = b.toString();
        //String s = "bacaaaacacac";
        String s = "acacac";
        List<String> suffixes = new ArrayList<>();
        for (int i = 0; i < s.length() - 1; i++) {
            suffixes.add(s.substring(i, s.length()));
        }

        suffixes.sort(String::compareTo);
        for (String suff : suffixes) {
            System.out.println(suff);
        }

        Node nd = CompresedTrieBuilder.buildTrie(suffixes);
        System.out.println(nd.buildTree());
        buildSuffixes(nd);
        System.out.println();
    }

    private static void buildSuffixes(Node nd) {
        System.out.println(nd);
        Node suff = nd.getSuffix();
        System.out.println(null == suff ? "-":suff);
        System.out.println("==========");
        for (Edge e : nd.edgesMap.values()) {
            buildSuffixes(e.child);
        }
    }
}
