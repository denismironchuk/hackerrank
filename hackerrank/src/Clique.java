import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 8/29/2018.
 */
public class Clique {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(tkn.nextToken());
            int m = Integer.parseInt(tkn.nextToken());

            int start = 2;
            int end = n;
            int res = 0;

            while (start < end) {
                int middle = (start + end) / 2;
                res = calculateEdges(n, (start + end) / 2);
                if (res < m) {
                    start = middle + 1;
                } else {
                    end = middle;
                }
            }

            System.out.println(res);
        }
    }

    private static int calculateEdges(int v, int n) {
        int r = v % (n - 1);
        return ((n - 2) * (v * v - r * r)) / (2 * (n - 1)) + ((r * (r - 1)) / 2);
    }
}
