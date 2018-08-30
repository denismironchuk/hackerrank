import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 8/29/2018.
 */
public class Clique {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader br = new BufferedReader(new FileReader("D:/clique6.txt"));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(tkn.nextToken());
            int m = Integer.parseInt(tkn.nextToken());

            if (m == (n * (n - 1)) / 2) {
                System.out.println(n);
                continue;
            }

            long start = 2;
            long end = n;

            while (true) {
                long middle = (start + end) / 2;
                long res = calculateEdges(n, middle);
                long res1 = calculateEdges(n, middle + 1);

                if (res < m && res1 >= m) {
                    break;
                }

                if (res < m) {
                    start = middle;
                } else {
                    end = middle;
                }
            }

            System.out.println((start + end) / 2);
        }
    }

    private static long calculateEdges(long v, long n) {
        long r = v % (n - 1);
        return ((n - 2) * (v * v - r * r)) / (2 * (n - 1)) + ((r * (r - 1)) / 2);
    }
}
