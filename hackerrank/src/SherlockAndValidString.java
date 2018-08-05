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
public class SherlockAndValidString {
    private static boolean isValid(List<Integer> list) {
        int len = list.size();
        return list.get(0).equals(list.get(len - 1))
                || (list.get(0).equals(list.get(len - 2)) && list.get(len - 1) - list.get(len - 2) == 1)
                || (list.get(0) == 1 && list.get(1).equals(list.get(len - 1)));
    }

    public static void  main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        Map<Character, Integer> chars = new HashMap<>();

        for (char c : s.toCharArray()) {
            chars.merge(c, 1, (o, n) -> o + n);
        }

        List<Integer> freqs = chars.values().stream().sorted(Integer::compareTo).collect(Collectors.toList());

        System.out.println(isValid(freqs) ? "YES" : "NO");
    }
}
