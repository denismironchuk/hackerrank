import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class TheMaximumSubarray {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            int n = Integer.parseInt(br.readLine());
            String aStr = br.readLine();
            StringTokenizer tkn = new StringTokenizer(aStr, " ");
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = Integer.parseInt(tkn.nextToken());
            }

            int[] sum = new int[n];
            sum[0] = a[0];
            for (int i = 1; i < n; i++) {
                sum[i] = sum[i - 1] + a[i];
            }

            int[] minIndex = new int[n];
            minIndex[0] = -1;

            for (int i = 1; i < n; i++) {
                if (minIndex[i - 1] == -1) {
                    if (sum[i-1] < 0) {
                        minIndex[i] = i - 1;
                    } else {
                        minIndex[i] = -1;
                    }
                } else {
                    if (sum[minIndex[i - 1]] < sum[i - 1]) {
                        minIndex[i] = minIndex[i - 1];
                    } else {
                        minIndex[i] = i - 1;
                    }
                }
            }

            long maxSubArray = Long.MIN_VALUE;

            for (int i = 0; i < n; i++) {
                long candidat = minIndex[i] == -1 ? sum[i] : sum[i] - sum[minIndex[i]];
                if (candidat > maxSubArray) {
                    maxSubArray = candidat;
                }
            }

            Arrays.sort(a);

            long maxSubseqence = a[n-1];

            for (int i = n - 2; i >= 0 && a[i] > 0; i--) {
                maxSubseqence += a[i];
            }

            System.out.printf("%s %s\n", maxSubArray, maxSubseqence);
        }
    }
}
