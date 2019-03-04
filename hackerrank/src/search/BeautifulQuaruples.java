package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 3/4/2019.
 */
public class BeautifulQuaruples {
    private static final int CNT = 4;
    private static final int MAX_VAL = 4096;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn = new StringTokenizer(br.readLine());
        int[] vals = new int[CNT];
        for (int i = 0; i < CNT; i++) {
            vals[i] = Integer.parseInt(tkn.nextToken());
        }
        Arrays.sort(vals);

        int A = vals[0];
        int B = vals[1];
        int C = vals[2];
        int D = vals[3];
        int[][] valuesCnt = new int[MAX_VAL][C + 1];

        for (int c = 1; c <= C; c++) {
            for (int d = c; d <= D; d++) {
                valuesCnt[c ^ d][c]++;
            }
        }

        int[][] valuesCumCnt = new int[MAX_VAL][C + 1];

        for (int i = 0; i < MAX_VAL; i++) {
            valuesCumCnt[i][C] = valuesCnt[i][C];
        }

        for (int i = 0; i < MAX_VAL; i++) {
            for (int c = C - 1; c > 0; c--) {
                valuesCumCnt[i][c] = valuesCnt[i][c] + valuesCumCnt[i][c + 1];
            }
        }

        long res = 0;

        for (int a = 1; a <= A; a++) {
            for (int b = a; b <= B; b++) {
                int n1 = C - b + 1;
                res += (n1 * (n1 + 1)) / 2;
                int n2 = D - C;
                res += n1 * n2;
                res -= valuesCumCnt[a ^ b][b];
            }
        }

        System.out.println(res);
    }
}
