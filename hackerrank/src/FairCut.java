import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class FairCut {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line1 = br.readLine();
        StringTokenizer line1Tkn = new StringTokenizer(line1, " ");
        int n = Integer.parseInt(line1Tkn.nextToken());
        int k = Integer.parseInt(line1Tkn.nextToken());
        String line2 = br.readLine();
        StringTokenizer line2Tkn = new StringTokenizer(line2, " ");
        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(line2Tkn.nextToken());
        }

        Arrays.sort(a);

        long[] s = new long[n];

        for (int i = 1; i < n; i++) {
            s[0] += a[i] - a[0];
        }

        for (int i = 1; i < n; i++) {
            s[i] = s[i-1] + (a[i] - a[i-1]) * (2 * i - n);
        }

        long[][] sigma = new long[k][n];
        long[][] d = new long[k][n];

        sigma[0][0] = s[0];
        d[0][0] = 0;

        for (int i = 1; i < n; i++) {
            if (s[i] <= sigma[0][i - 1]) {
                sigma[0][i] = s[i];
                d[0][i] = 0;
            } else {
                sigma[0][i] = sigma[0][i - 1];
                d[0][i] = d[0][i-1] + a[i] - a[i - 1];
            }
        }

        for (int j = 0; j < n; j++) {
            for (int i = 1; i < k; i++) {
                if (i > j) {
                    sigma[i][j] = Long.MAX_VALUE;
                    d[i][j] = Long.MAX_VALUE;
                } else {
                    long d2 = d[i-1][j-1] + (a[j]-a[j-1]) * i;
                    long sigma2 = sigma[i-1][j-1] + s[j] - 2 * d2;
                    if (sigma2 < sigma[i][j-1]) {
                        sigma[i][j] = sigma2;
                        d[i][j] = d2;
                    } else {
                        sigma[i][j] = sigma[i][j-1];
                        d[i][j] = d[i][j-1] + (a[j] - a[j-1]) * (i + 1);
                    }
                }
            }
        }

        System.out.println(sigma[k-1][n-1]);
    }
}
