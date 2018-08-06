import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoveLetterMystery {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            String s = br.readLine();
            int res = 0;
            int len = s.length();

            for (int i = 0; i < len / 2; i++) {
                res += Math.abs(s.charAt(i) - s.charAt(len - i - 1));
            }

            System.out.println(res);
        }
    }
}
