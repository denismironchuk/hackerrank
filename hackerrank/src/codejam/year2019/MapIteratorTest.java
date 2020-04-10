package codejam.year2019;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapIteratorTest {
    public static void main(String[] args) {
        Map<Integer, Integer> testMap = new HashMap<>();
        testMap.put(1, 1);
        testMap.put(2, 1);
        testMap.put(3, 1);
        testMap.put(4, 1);
        testMap.put(5, 1);
        testMap.put(6, 1);

        System.out.println(testMap);

        Iterator<Integer> itr = testMap.keySet().iterator();

        while (itr.hasNext()) {
            int val = itr.next();
            if (val % 2 == 0) {
                itr.remove();
            }
        }

        System.out.println(testMap);
    }
}
