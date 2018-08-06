import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Denis_Mironchuk on 2/20/2018.
 */
public class SherlockAndValidString2 {
    public static void  main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        Map<Character, Integer> chars = new HashMap<>();

        for (char c : s.toCharArray()) {
            chars.merge(c, 1, (o, n) -> o + n);
        }

        Map<Integer, List<Integer>> freqs = chars.values().stream().collect(Collectors.groupingBy(v -> v));
        if (freqs.keySet().size() == 1) {
            System.out.println("YES");
        } else if (freqs.keySet().size() == 1) {
            Iterator<List<Integer>> itr = freqs.values().iterator();

            List<Integer> list1 = itr.next();
            List<Integer> list2 = itr.next();

            //System.out.println(list1.size() == );
        } else {
            System.out.println("NO");
        }

    }
}
