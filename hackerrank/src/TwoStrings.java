import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Denis_Mironchuk on 2/20/2018.
 */
public class TwoStrings {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int P = Integer.parseInt(br.readLine());

        for (int p = 0; p < P; p++) {
            String s1 = br.readLine();
            String s2 = br.readLine();

            Set<Character> chars1 = new HashSet<>();
            Set<Character> chars2 = new HashSet<>();

            for (char c : s1.toCharArray()) {
                chars1.add(c);
            }

            for (char c : s2.toCharArray()) {
                chars2.add(c);
            }
            chars1.retainAll(chars2);
            System.out.println(chars1.isEmpty() ? "NO" : "YES");
        }
    }
}
