import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MinMST3 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int G = Integer.parseInt(br.readLine());
        for (int g = 0; g < G; g++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            long n = Long.parseLong(tkn.nextToken());
            long m = Long.parseLong(tkn.nextToken());
            long s = Long.parseLong(tkn.nextToken());

            long maxEdge = s - n + 2;
            long res = 0;
            long edgeLim = ((n - 1) * (n - 2) / 2) + 1;

            if (m <= edgeLim) {
                res = m - 1 + maxEdge;
            } else {
                res = edgeLim - 1 + (m - edgeLim + 1) * maxEdge;

                long n1 = n - 1;
                long m1 = m - (n1 * (n1 - 1)) / 2;

                if (maxEdge - n1 + 1 > 1) {
                    long decr = m1 * (n1 - 1) - (n1) * (n1 - 1) / 2;
                    if (decr > 0) {
                        long k = (maxEdge - 1) / n1;
                        res -= k * decr;

                        long singleCost = k + 1;
                        maxEdge -= (n1 - 1) * k;

                        long k2 = Math.max(maxEdge - singleCost - 1, 0);

                        long newDecr = m1 * k2 - ((n1 - 1 + n1 - 1 - k2 + 1) * k2) / 2;

                        if (newDecr > 0) {
                            res = res - newDecr;
                        }
                    }
                } else {
                    long k = Math.max(maxEdge - 2, 0);
                    long newRes = res - m1 * k + ((n1 - 1 + n1 - 1 - k + 1) * k) / 2;
                    if (newRes < res) {
                        res = newRes;
                    }
                }
            }

            System.out.println(res);
        }
    }
}
