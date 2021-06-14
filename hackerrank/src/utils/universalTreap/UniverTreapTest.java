package utils.universalTreap;

public class UniverTreapTest {

    public static void main(String[] args) {
        TreapUniversal<Integer, Integer> treap = new TreapUniversal<>();
        treap.setAggregationConfig((node) -> 1,
                (node) -> 1 + (node.getLeft() == null ? 0 : node.getLeft().getTreeAggregatedValue()) +
                        (node.getRight() == null ? 0 : node.getRight().getTreeAggregatedValue()));

        for (int i = 1; i < 20; i++) {
            treap.add(i);
        }

        System.out.println();
    }
}
