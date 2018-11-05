import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MinMST {
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
            }

            System.out.println(res);
        }
    }
}
