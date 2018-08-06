import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RedJohnIsBack {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            int n = Integer.parseInt(br.readLine());
            int[] arrangements = new int[n + 1];
            arrangements[0] = 1;
            for (int i = 1; i <= n; i++) {
                arrangements[i] = arrangements[i - 1];

                if (i - 4 >= 0) {
                    arrangements[i] += arrangements[i - 4];
                }
            }

            int p = arrangements[n];
            int[] processed = new int[p + 1];
            int primes = 0;

            for (int i = 2; i <= p; i++) {
                if (processed[i] == 0) {
                    primes++;
                    for (int j = 1; j * i <= p; j++) {
                        processed[i * j] = 1;
                    }
                }
            }

            System.out.println(primes);
        }
    }
}
