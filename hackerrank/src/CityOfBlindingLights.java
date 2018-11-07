import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class CityOfBlindingLights {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        int m = Integer.parseInt(tkn1.nextToken());

        long[][] dists = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dists[i][j] = i == j ? 0 : Integer.MAX_VALUE;
            }
        }

        for (int i = 0; i < m; i++) {
            StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
            int v1 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int v2 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int r = Integer.parseInt(edgeTkn.nextToken());

            dists[v1][v2] = r;
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    dists[i][j] = Math.min(dists[i][j], dists[i][k] + dists[k][j]);
                }
            }
        }

        StringBuilder res = new StringBuilder();

        int q = Integer.parseInt(br.readLine());

        for (int i = 0; i < q; i++) {
            StringTokenizer query = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(query.nextToken()) - 1;
            int b = Integer.parseInt(query.nextToken()) - 1;
            if (dists[a][b] >= Integer.MAX_VALUE) {
                res.append("-1\n");
            } else {
                res.append(dists[a][b]).append("\n");
            }
        }

        System.out.println(res.toString());
    }
}
