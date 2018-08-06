import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BricksGame {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            int n = Integer.parseInt(br.readLine());
            long[] a = new long[n];
            String aStr = br.readLine();
            StringTokenizer aStrTkn = new StringTokenizer(aStr, " ");

            for (int i = 0; i < n; i++) {
                a[i] = Integer.parseInt(aStrTkn.nextToken());
            }

            long[] dynFirst = new long[n + 1];
            long[] dynSecond = new long[n + 1];
            long sum = 0;
            dynFirst[n] = 0;
            dynSecond[n] = 0;

            for (int i = n - 1; i >= 0; i--) {
                dynFirst[i] = dynSecond[i + 1] + a[i];
                if (i + 2 <= n) {
                    dynFirst[i] = Math.max(dynFirst[i], dynSecond[i + 2] + a[i] + a[i+1]);
                }
                if (i + 3 <= n) {
                    dynFirst[i] = Math.max(dynFirst[i], dynSecond[i + 3] + a[i] + a[i+1] + a[i + 2]);
                }

                sum += a[i];
                dynSecond[i] = sum - dynFirst[i];
            }

            System.out.println(dynFirst[0]);
        }
    }
}
