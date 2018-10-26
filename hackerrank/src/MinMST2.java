import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MinMST2 {
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

                long fullCost = 2;
                long newRes = res;
                maxEdge--;

                while (maxEdge >= fullCost) {
                    for (long add = n1 - 1; maxEdge >= fullCost && add > 0; add--) {
                        newRes = newRes - m1 + add;
                        res = Math.min(res, newRes);
                        maxEdge--;
                    }
                    fullCost++;
                }
            }

            System.out.println(res);
        }
    }
}
