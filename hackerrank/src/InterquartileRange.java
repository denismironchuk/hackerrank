import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 8/30/2018.
 */
public class InterquartileRange {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        int[] x = new int[n];
        int[] f = new int[n];

        StringTokenizer xTkn = new StringTokenizer(br.readLine());
        StringTokenizer fTkn = new StringTokenizer(br.readLine());

        int sSize = 0;
        for (int i = 0; i < n; i++) {
            x[i] = Integer.parseInt(xTkn.nextToken());
            f[i] = Integer.parseInt(fTkn.nextToken());
            sSize += f[i];
        }

        int[] s = new int[sSize];
        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < f[i]; j++) {
                s[index] = x[i];
                index++;
            }
        }

        Arrays.sort(s);

        double q1 = getMedian(s, 0, (sSize / 2) - 1);;
        double q3;

        if (n % 2 == 1) {
            q3 = getMedian(s, (sSize / 2) + 1, sSize - 1);
        } else {
            q3 = getMedian(s, (sSize / 2), sSize - 1);
        }

        System.out.println(q3 - q1);
    }

    public static double getMedian(int[] a, int start, int end) {
        int len = end - start + 1;

        if (len % 2 == 1) {
            return a[start + (len / 2)];
        } else {
            return ((double)(a[start + (len / 2)] + a[start + (len / 2) - 1])) / 2;
        }
    }
}
