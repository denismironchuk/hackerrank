package utils.disjointSet;

/**
 * Created by Denis_Mironchuk on 9/28/2018.
 */
public class DisjointSetTest {
    public static void main(String[] args) {
        DisjointSet set = new DisjointSet(10);
        set.makeSet(1);
        set.makeSet(2);

        set.unite(1, 2);

        System.out.println(set.find(1));
        System.out.println(set.find(2));
    }
}
