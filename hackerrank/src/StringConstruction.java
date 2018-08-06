import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Denis_Mironchuk on 2/20/2018.
 */
public class StringConstruction {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        for (int i = 0; i < n; i++) {
            String s = br.readLine();
            Set<Character> chars = new HashSet<>();
            for (char c : s.toCharArray()) {
                chars.add(c);
            }
            System.out.println(chars.size());
        }
    }
}
