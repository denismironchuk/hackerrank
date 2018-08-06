import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class GridWalking2 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            String line1 = br.readLine();
            StringTokenizer line1Tkn = new StringTokenizer(line1, " ");
            int n = Integer.parseInt(line1Tkn.nextToken());
            int m = Integer.parseInt(line1Tkn.nextToken());

            String xStr = br.readLine();
            StringTokenizer xStrTkn = new StringTokenizer(xStr, " ");
            int[] x = new int[n];

            for (int i = 0; i < n; i++) {
                x[i] = Integer.parseInt(xStrTkn.nextToken());
            }

            String dStr = br.readLine();
            StringTokenizer dStrTkn = new StringTokenizer(dStr, " ");
            int[] d = new int[n];
            for (int i = 0; i < n; i++) {
                d[i] = Integer.parseInt(dStrTkn.nextToken());
            }

            long[][] oneDimensionsMove = new long[n][m];
        }
    }
}
