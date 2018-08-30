import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 8/30/2018.
 */
public class StandartDeviation {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        int[] x = new int[n];
        double sum = 0;

        StringTokenizer xTkn = new StringTokenizer(br.readLine());

        for (int i = 0; i < n; i++) {
            x[i] = Integer.parseInt(xTkn.nextToken());
            sum += x[i];
        }

        double mean = sum / n;

        double dev = 0;

        for (int i = 0; i < n; i++) {
            dev += (x[i] - mean) * (x[i] - mean);
        }

        dev /= n;
        System.out.printf("%.1f\n", Math.sqrt(dev));
    }
}
