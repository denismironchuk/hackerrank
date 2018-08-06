import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;

/**
 * Created by Denis_Mironchuk on 2/20/2018.
 */
public class PalindromIndex {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            String s = br.readLine();
            int len = s.length();

            int res = -1;
            for (int i = 0; i < len / 2; i++) {
                if (s.charAt(i) != s.charAt(len -i - 1)) {
                    if (s.charAt(i + 1) == s.charAt(len - i - 1)) {
                        res = i;
                        break;
                    } else {
                        res = len - i - 1;
                        break;
                    }
                }
            }
            System.out.println(res);
        }
    }
}
