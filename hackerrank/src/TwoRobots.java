import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class TwoRobots {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            String line1 = br.readLine();
            StringTokenizer line1Tkn = new StringTokenizer(line1, " ");
            int containers = Integer.parseInt(line1Tkn.nextToken());
            int queriesAmount = Integer.parseInt(line1Tkn.nextToken());

            int[][] queries = new int[queriesAmount][2];

            for (int i = 0; i < queriesAmount; i++) {
                String query = br.readLine();
                StringTokenizer queryTkn = new StringTokenizer(query, " ");
                queries[i][0] = Integer.parseInt(queryTkn.nextToken());
                queries[i][1] = Integer.parseInt(queryTkn.nextToken());
            }

            int[] res = new int[queriesAmount];
            res[0] = Math.abs(queries[0][0] - queries[0][1]);
            for (int i = 1; i < queriesAmount; i++) {
                int newDist = Math.abs(queries[i][0] - queries[i][1]);
                res[i] = res[0] + newDist;
                for (int j = 1; j < i; j++) {
                    int newVal = res[j] + newDist + Math.abs(queries[j - 1][1] - queries[i][0]);
                    if (newVal < res[i]) {
                        res[i] = newVal;
                    }
                }

                newDist += Math.abs(queries[i - 1][1] - queries[i][0]);

                for (int j = 0; j < i; j++) {
                    res[j] += newDist;
                }
            }

            int min = res[0];

            for (int i = 1; i < queriesAmount; i++) {
                if (res[i] < min) {
                    min = res[i];
                }
            }

            System.out.println(min);
        }
    }
}
