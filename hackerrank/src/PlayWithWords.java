import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Denis_Mironchuk on 5/31/2018.
 */
public class PlayWithWords {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();

        int[][] dyn = new int[s.length()][s.length()];

        for (int i = 0; i < s.length(); i++) {
            dyn[i][i] = 1;
        }

        for (int i = s.length() - 1; i >= 0 ; i--) {
            for (int j = i + 1; j < s.length(); j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    dyn[i][j] = dyn[i+1][j-1] + 2;
                } else {
                    dyn[i][j] = Math.max(dyn[i][j-1], dyn[i+1][j]);
                }
            }
        }

        int res = 0;

        for (int i = 1; i < s.length() - 1; i++) {
            int candidat = dyn[0][i] * dyn[i + 1][s.length() - 1];
            if (candidat > res) {
                res = candidat;
            }
        }

        System.out.println(res);
    }
}
