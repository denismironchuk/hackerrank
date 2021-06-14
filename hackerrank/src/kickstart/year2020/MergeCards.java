package kickstart.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MergeCards {
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int n = Integer.parseInt(br.readLine());
                double[] a = new double[n];
                StringTokenizer aTkn = new StringTokenizer(br.readLine());
                for (int i = 0; i < n; i++) {
                    a[i] = Double.parseDouble(aTkn.nextToken());
                }

                double exp = 0.0;

                for (int positions = n - 1; positions > 0; positions--) {
                    exp += (2 * sum(a) - a[0] - a[a.length - 1]) / positions;
                    double[] newA = new double[positions];
                    for (int pos = 0; pos < positions; pos++) {
                        newA[pos] = ((a[pos] + a[pos + 1]) + a[pos] * (positions - pos - 1) + a[pos + 1] * (pos)) / positions;
                    }
                    a = newA;
                }

                System.out.printf("Case #%s: %s\n", t, exp);
            }
        }
    }

    private static double sum(double[] a) {
        double res = 0;
        for (double val : a) {
            res += val;
        }
        return res;
    }
}
