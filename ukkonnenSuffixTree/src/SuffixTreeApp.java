import java.util.Date;

/**
 * Created by Denis_Mironchuk on 6/14/2018.
 */
public class SuffixTreeApp {
    public static Node buildTreeOptimal(String s) {
        SuffixTreeInfo treeInfo = new SuffixTreeInfo(s);
        Node root = new Node(treeInfo);
        ActivePoint ap = new ActivePoint(treeInfo);
        treeInfo.setRoot(root);

        for (int i = 0; i < s.length(); i++) {

            char c = s.charAt(i);

            treeInfo.setPreviousPhaseAddedInnerNode(null);
            treeInfo.setCurrentPhaseAddedInnerNode(null);
            treeInfo.setLastVisitedNode(null);
            treeInfo.setPhaseLastCharIndex(i);

            boolean terminate;
            boolean finalExtension;

            do {
                finalExtension = ap.getEdge() == null;

                if (ap.getEdge() == null) {
                    terminate = root.process(c);
                } else {
                    Edge edge = ap.getEdge();
                    if (ap.getPos() == edge.getEndIndex() - edge.getStartIndex()) {
                        terminate = edge.getChild().process(c);
                    } else {
                        terminate = edge.process(c, ap.getPos());
                    }
                }

                if (treeInfo.getPreviousPhaseAddedInnerNode() != null) {
                    treeInfo.getPreviousPhaseAddedInnerNode().setSuffix(treeInfo.getLastVisitedNode());
                }
                treeInfo.setPreviousPhaseAddedInnerNode(treeInfo.getCurrentPhaseAddedInnerNode());
                treeInfo.setCurrentPhaseAddedInnerNode(null);

                if (!finalExtension && !terminate) {
                    ap.moveToSuffix();
                }
            } while(!terminate && !finalExtension);

            ap.moveForwardByChar(c, !terminate);
        }

        return root;
    }
}
