package kickstart.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IncreasingSequenceCardGame {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                long n = Long.parseLong(br.readLine());
                double res = 0;
                if (n <= 1000000) {
                    for (int i = 1; i <= n; i++) {
                        res += 1l / (double) i;
                    }
                } else {
                    double nd = (double)n;
                    res = Math.log(nd) + 0.5772156649015328606065120900824024310421 + (1 / nd) - ((1 / (12 * nd)) / nd);
                }
                System.out.printf("Case #%s: %.6f\n", t, res);
            }
        }
    }
}
