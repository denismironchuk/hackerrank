import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class MandragoraForest {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            int n = Integer.parseInt(br.readLine());
            StringTokenizer tkn = new StringTokenizer(br.readLine(), " ");
            int[] h = new int[n];
            for (int i = 0; i < n; i++) {
                h[i] = Integer.parseInt(tkn.nextToken());
            }

            Arrays.sort(h);

            long[] sum = new long[n];
            sum[n - 1] = h[n - 1];
            long max = sum[n - 1] * n;

            for (int i = n - 2; i >= 0; i--) {
                sum[i] = sum[i + 1] + h[i];
                long res = sum[i] * (i + 1);
                if (res > max) {
                    max = res;
                }
            }

            System.out.println(max);
        }
    }
}
