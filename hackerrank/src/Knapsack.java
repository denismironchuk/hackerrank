import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Knapsack {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            String line1 = br.readLine();
            StringTokenizer line1Tkn = new StringTokenizer(line1, " ");
            int n = Integer.parseInt(line1Tkn.nextToken());
            int k = Integer.parseInt(line1Tkn.nextToken());

            String line2 = br.readLine();
            StringTokenizer line2Tkn = new StringTokenizer(line2, " ");

            int[] arr = new int[n];

            for (int i = 0; i < n; i++) {
                arr[i] = Integer.parseInt(line2Tkn.nextToken());
            }

            int[] results = new int[k+1];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j <= k; j++) {
                     if (j - arr[i] >= 0) {
                         if (j - results[j] > j - (results[j - arr[i]] + arr[i])) {
                             results[j] = results[j - arr[i]] + arr[i];
                         }
                     }
                }
            }

            System.out.println(results[k]);
        }
    }
}
