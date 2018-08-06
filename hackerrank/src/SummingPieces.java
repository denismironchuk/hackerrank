import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SummingPieces {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        String aStr = br.readLine();
        StringTokenizer aStrTkn = new StringTokenizer(aStr, " ");
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = Long.parseLong(aStrTkn.nextToken());
        }

        long[] sum = new long[n];
        sum[0] = a[0];

        long twoPow = 1;
        for (int i = 1; i < n; i++) {
            long tempTwoPow = twoPow;
            long aSum = a[i];
            long cnt = 1;
            for (int j = i - 1; j > -1; j--) {
                sum[i] = sum[i] + sum[j] + aSum * tempTwoPow * cnt;
                tempTwoPow /= 2;
                cnt++;
                aSum += a[j];
            }

            sum[i] += aSum * cnt;
            twoPow *= 2;
        }

        System.out.println(sum[n - 1]);
    }
}
