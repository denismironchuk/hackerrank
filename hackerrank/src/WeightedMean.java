import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 8/30/2018.
 */
public class WeightedMean {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        int[] x = new int[n];
        int[] w = new int[n];

        StringTokenizer xTkn = new StringTokenizer(br.readLine());
        StringTokenizer wTkn = new StringTokenizer(br.readLine());

        double wSum = 0;
        double weightedSum = 0;
        for (int i = 0; i < n; i++) {
            x[i] = Integer.parseInt(xTkn.nextToken());
            w[i] = Integer.parseInt(wTkn.nextToken());
            wSum += w[i];
            weightedSum += x[i] * w[i];
        }

        double result = weightedSum / wSum;
        System.out.printf("%.1f\n", result);
    }
}
