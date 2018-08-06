import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Denis_Mironchuk on 2/20/2018.
 */
public class GameOfThrones {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();

        Map<Character, Integer> chars = new HashMap<>();


        for (char c : s.toCharArray()) {
            chars.merge(c, 1, (o, n) -> o + n);
        }

        System.out.println(chars.values().stream().filter(v -> v % 2 != 0).count() < 2 ? "YES" : "NO");
    }
}
