import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Denis_Mironchuk on 2/20/2018.
 */
public class MakingAnagrams {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String a = br.readLine();
        String b = br.readLine();

        Map<Character, Integer> aChars = new HashMap<>();
        Map<Character, Integer> bChars = new HashMap<>();

        for (char c : a.toCharArray()) {
            aChars.merge(c, 1, (oldVal, newVal) -> oldVal + newVal);
            bChars.put(c, 0);
        }

        for (char c : b.toCharArray()) {
            aChars.merge(c, 0, (oldVal, newVal) -> oldVal);
            bChars.merge(c, 1, (oldVal, newVal) -> oldVal + newVal);
        }

        Map<Character, Integer> chars = new HashMap<>();
        for (char c : aChars.keySet()) {
            chars.put(c, Math.abs(aChars.get(c) - bChars.get(c)));
        }

        System.out.println(chars.values().stream().reduce((sum, val) -> sum + val).orElse(0));
    }
}
