import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Denis_Mironchuk on 2/20/2018.
 */
public class BeautifulString {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        String bin = br.readLine();
        int res = 0;
        for (int i = 0; i < bin.length() - 2; i++) {
            if (bin.charAt(i) == '0' && bin.charAt(i + 1) == '1' && bin.charAt(i + 2) == '0') {
                res++;
                i+=2;
            }
        }

        System.out.println(res);
    }
}
