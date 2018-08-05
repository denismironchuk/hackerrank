import trivialTree.TrivialSuffixTreeApp;

import java.util.Date;

/**
 * Created by Denis_Mironchuk on 6/15/2018.
 */
public class BuildersCompare {
    public static void main(String[] args) {
        while (true) {
            int len = 10;
            StringBuilder build = new StringBuilder();

            for (int i = 0; i < len; i++) {
                build.append((char) ('a' + (int) (Math.random() * 2)));
            }
            build.append((char)('z' + 1));
            String str = build.toString();

            System.out.println(str);
            String treeTrivial = TrivialSuffixTreeApp.buildTreeTrivial(str).buildTree();
            String treeOptimal = SuffixTreeApp.buildTreeOptimal(str).buildTree();
            System.out.println(treeOptimal);

            if (!treeTrivial.equals(treeOptimal)) {
                throw new RuntimeException();
            }

            /*Date start = new Date();
            SuffixTreeApp.buildTreeOptimal(str);
            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + "ms");
            System.out.println("======================");*/
        }
    }
}
