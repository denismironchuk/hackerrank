import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class MeanMedianMode {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[] arr = new int[n];
        StringTokenizer tkn = new StringTokenizer(br.readLine());

        double sum = 0;
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(tkn.nextToken());
            sum += arr[i];
        }

        double mean = sum / n;

        System.out.printf("%.1f\n", mean);

        Arrays.sort(arr);
        double median = 0;
        if (n % 2 == 1) {
            median = arr[n / 2];
        } else {
            median = ((double)(arr[n / 2] + arr[(n / 2) - 1])) / 2;
        }
        System.out.printf("%.1f\n", median);

        int mode = arr[0];
        int modeCnt = 0;
        int cnt = 1;

        for (int i = 1; i < n; i++) {
            if (arr[i] == arr[i - 1]) {
                cnt++;
            } else {
                if (cnt > modeCnt) {
                    mode = arr[i - 1];
                    modeCnt = cnt;
                }
                cnt = 1;
            }
        }

        System.out.println(mode);
    }
}
