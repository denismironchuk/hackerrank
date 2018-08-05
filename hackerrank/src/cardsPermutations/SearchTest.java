package cardsPermutations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Влада on 07.07.2018.
 */
public class SearchTest {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(3);
        list.add(7);
        list.add(9);
        int pos = CardsPermutation.findFirstGreater(list, -1, 0, list.size() - 1);
        System.out.println(pos);
    }
}
