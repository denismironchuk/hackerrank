package sorting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 2/12/2019.
 */
public class FraudulentActivity {
    private static final int MAX = 200;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(line1.nextToken());
        int d = Integer.parseInt(line1.nextToken());

        StringTokenizer line2 = new StringTokenizer(br.readLine());
        int[] arr = new int[n];

        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(line2.nextToken());
        }

        int[] count = new int[MAX + 1];

        for (int i = 0; i < d; i++) {
            count[arr[i]]++;
        }

        int notification = 0;

        for (int i = d; i < n; i++) {
            double median = getMedian(count, d);
            if (arr[i] >= median * 2) {
                notification++;
            }
            count[arr[i]]++;
            count[arr[i - d]]--;
        }

        System.out.println(notification);
    }

    private static double getMedian(int[] count, int d) {
        int median = 0;
        if (d % 2 == 1) {
            int pos = d / 2 + 1;
            int cnt = 0;
            for (int i = 0; i <= MAX; i++) {
                cnt += count[i];
                if (cnt>= pos) {
                    return i;
                }
            }
        } else {
            int pos1 = d / 2;
            int pos2 = d / 2 + 1;
            double m1 = -1;
            double m2 = -1;
            int cnt = 0;
            for (int i = 0; (m1 == -1 || m2 == -1) && i <= MAX; i++) {
                cnt += count[i];
                if (m1 == -1 && cnt>= pos1) {
                    m1 = i;
                }
                if (m2 == -1 && cnt>= pos2) {
                    m2 = i;
                }
            }

            return (m1 + m2) / 2;
        }

        return median;
    }
}
