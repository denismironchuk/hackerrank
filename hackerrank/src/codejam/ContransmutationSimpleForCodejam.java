package codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class ContransmutationSimpleForCodejam {
    private static int m;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t <= T; t++) {
            m = Integer.parseInt(br.readLine());

            int metalls[][] = new int[m + 1][2];
            int[] initialGrams = new int[m + 1];
            int[] gramms = new int[m + 1];

            for (int i = 1; i <= m; i++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                metalls[i][0] = Integer.parseInt(tkn.nextToken());
                metalls[i][1] = Integer.parseInt(tkn.nextToken());
            }

            StringTokenizer gramTkn = new StringTokenizer(br.readLine());
            for (int i = 1; i <= m; i++) {
                initialGrams[i] = Integer.parseInt(gramTkn.nextToken());
                gramms[i] = initialGrams[i];
            }

            gramms = runSimulation(metalls, gramms);
            int res1 = gramms[1];
            gramms = runSimulation(metalls, gramms);
            int res2 = gramms[1];

            if (res1 != res2) {
                System.out.printf("Case #%s: %s\n", t, "UNBOUNDED");
            } else {
                int gram1 = gramms[1];
                gramms[1] = 0;
                gramms[metalls[1][0]] += gram1;
                gramms[metalls[1][1]] += gram1;

                gramms = runSimulation(metalls, gramms);
                int res3 = gramms[1];

                if (res3 > res2) {
                    System.out.printf("Case #%s: %s\n", t, "UNBOUNDED");
                } else {
                    System.out.printf("Case #%s: %s\n", t, res2);
                }
            }
        }
    }

    private static int[] runSimulation(int metalls[][], int[] grammsNext) {
        int[] gramms;

        for (int sim = 0; sim < m - 1; sim++) {
            gramms = grammsNext;
            grammsNext = new int[m + 1];
            grammsNext[1] = gramms[1];
            for (int i = 2; i <= m; i++) {
                grammsNext[metalls[i][0]] += gramms[i];
                grammsNext[metalls[i][1]] += gramms[i];
            }
        }

        return grammsNext;
    }
}
