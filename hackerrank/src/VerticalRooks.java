import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Denis_Mironchuk on 2/21/2018.
 */
public class VerticalRooks {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            int n = Integer.parseInt(br.readLine());

            int[] p1 = new int[n];
            int[] p2 = new int[n];

            for (int i = 0; i < n; i++) {
                p1[i] = Integer.parseInt(br.readLine());
            }

            for (int i = 0; i < n; i++) {
                p2[i] = Integer.parseInt(br.readLine());
            }

            int res = 0;

            for (int i = 0; i < n; i++) {
                res ^= Math.abs(p1[i] - p2[i]) - 1;
            }

            System.out.println(res == 0 ? "player-1" : "player-2");
        }
    }
}
