package trivialTree;

/**
 * Created by Denis_Mironchuk on 6/15/2018.
 */
public class TrivialSuffixTreeApp {
    private static int NUM = 0;

    public static Node buildTreeTrivial(String s) {
        //String s = "aabaacaccabacac";
        NUM = 0;
        //String s = "aaabbababbabba";
        Node root = new Node(TrivialSuffixTreeApp.getNum());

        for (int i = 0; i < s.length(); i++) {
            String phaseStr = s.substring(0, i + 1);
            for (int j = 0; j < i + 1; j++) {
                String phaseSuffix = phaseStr.substring(j);
                //System.out.println(phaseSuffix);
                root.process(phaseSuffix);
            }

            //System.out.println(root.buildTree());
            //System.out.println("==============");
        }

        return root;
    }

    public static int getNum() {
        int toReturn = NUM;
        NUM++;
        return toReturn;
    }
}
