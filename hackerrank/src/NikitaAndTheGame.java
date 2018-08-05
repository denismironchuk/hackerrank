import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class NikitaAndTheGame {
    private static int countScore(long[] arr, int start, int end, long sum) {
        if (sum == 0) {
            return end;
        }
        if (start == end || sum % 2 == 1) {
            return 0;
        }

        long tempSum = 0;
        int i = start;
        while (i <= end && tempSum < sum / 2) {
            tempSum += arr[i];
            i++;
        }

        if (tempSum == sum / 2) {
            return Math.max(countScore(arr, start, i - 1, tempSum), countScore(arr, i, end, tempSum)) + 1;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            int n = Integer.parseInt(br.readLine());
            long[] arr = new long[n];
            String arrStr = br.readLine();
            StringTokenizer tkn = new StringTokenizer(arrStr, " ");
            long sum = 0;
            for (int i = 0; i < n; i++) {
                arr[i] = Integer.parseInt(tkn.nextToken());
                sum += arr[i];
            }

            System.out.println(countScore(arr, 0, n - 1, sum));

        }
    }
}
