import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Denis_Mironchuk on 2/20/2018.
 */
public class Anagram {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            String s = br.readLine();
            int len = s.length();

            if (len % 2 != 0) {
                System.out.println(-1);
                continue;
            } else {
                String s1 = s.substring(0, len / 2);
                String s2 = s.substring(len / 2, len);

                Map<Character, Integer> s1Chars = new HashMap<>();
                Map<Character, Integer> s2Chars = new HashMap<>();

                for (char c : s.toCharArray()) {
                    s1Chars.put(c, 0);
                    s2Chars.put(c, 0);
                }

                for (char c : s1.toCharArray()) {
                    s1Chars.merge(c, 1, (oldVal, newVal) -> oldVal + newVal);
                }

                for (char c : s2.toCharArray()) {
                    s2Chars.merge(c, 1, (oldVal, newVal) -> oldVal + newVal);
                }

                for (char c : s1Chars.keySet()) {
                    s1Chars.put(c, s1Chars.get(c) - s2Chars.get(c));
                }

                System.out.println(s1Chars.values().stream().filter(val -> val > 0).reduce((sum ,val) -> sum + val).orElse(0));
            }
        }
    }
}
