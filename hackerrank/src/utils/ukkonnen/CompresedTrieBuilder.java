package utils.ukkonnen;

import java.util.List;

/**
 * Created by Denis_Mironchuk on 4/10/2018.
 */
public class CompresedTrieBuilder {
    public static Node buildTrie(List<String> strings) {
        Node nd = new Node();
        for (String s: strings) {
            nd.process(s);
        }
        return nd;
    }
}
