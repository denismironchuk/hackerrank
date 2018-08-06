import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Denis_Mironchuk on 2/20/2018.
 */
public class FunnyString {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            String s = br.readLine();
            boolean isFunny = true;
            int lastIndex = s.length() - 1;

            for (int i = 1; i < s.length() && isFunny; i++) {
                if (Math.abs(s.charAt(i) - s.charAt(i-1)) != Math.abs(s.charAt(lastIndex - i) - s.charAt(lastIndex - i + 1))) {
                    isFunny = false;
                }
            }

            System.out.println(isFunny ? "Funny" : "Not Funny");
        }
    }
}
