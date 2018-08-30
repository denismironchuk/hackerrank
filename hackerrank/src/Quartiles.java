import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 8/30/2018.
 */
public class Quartiles {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[] a = new int[n];
        StringTokenizer tkn = new StringTokenizer(br.readLine());

        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(tkn.nextToken());
        }

        Arrays.sort(a);

        if (n % 2 == 1) {
            System.out.printf("%d\n", getMedian(a, 0, (n / 2) - 1));
            System.out.printf("%d\n", getMedian(a, 0, n - 1));
            System.out.printf("%d\n", getMedian(a, (n / 2) + 1, n - 1));
        } else {
            System.out.printf("%d\n", getMedian(a, 0, (n / 2) - 1));
            System.out.printf("%d\n", getMedian(a, 0, n - 1));
            System.out.printf("%d\n", getMedian(a, (n / 2), n - 1));
        }
    }

    public static int getMedian(int[] a, int start, int end) {
        int len = end - start + 1;

        if (len % 2 == 1) {
            return a[start + (len / 2)];
        } else {
            return (a[start + (len / 2)] + a[start + (len / 2) - 1]) / 2;
        }
    }
}
