package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 2/14/2019.
 */
public class RadioTransmitters {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(line1.nextToken());
        int k = Integer.parseInt(line1.nextToken());

        int[] houses = new int[n];
        int maxPos = -1;

        StringTokenizer line2 = new StringTokenizer(br.readLine());

        for (int i = 0; i < n; i++) {
            houses[i] = Integer.parseInt(line2.nextToken());
            if (houses[i] > maxPos) {
                maxPos = houses[i];
            }
        }

        int[] positions = new int[maxPos + 1];
        int[] housemark = new int[maxPos + 1];
        for (int i = 0; i < n; i++) {
            positions[houses[i]] = 1;
            housemark[houses[i]] = 1;
        }

        int houseCnt = 1;
        for (int i = 1; i <= maxPos; i++) {
            if (positions[i] == 0) {
                positions[i] = positions[i - 1];
            } else {
                positions[i] = houseCnt;
                houseCnt++;
            }
        }

        int[] transAmnt = new int[maxPos + 1];
        int[] reachDist = new int[maxPos + 1];

        transAmnt[0] = housemark[0] == 0 ? 0 : 1;
        reachDist[0] = housemark[0] == 0 ? 0 : k;

        for (int i = 1; i <= maxPos; i++) {
            if (housemark[i] == 1) {
                int a1 = Integer.MAX_VALUE;
                if (reachDist[i - 1] > 0) {
                    a1 = transAmnt[i - 1];
                }

                int a2 = (i - k - 1 > 0 ? transAmnt[i - k - 1] : 0) + 1;
                if (a1 < a2) {
                    transAmnt[i] = a1;
                    reachDist[i] = reachDist[i - 1] - 1;
                } else {
                    transAmnt[i] = a2;
                    reachDist[i] = k;
                }
            } else {
                transAmnt[i] = transAmnt[i - 1];
                reachDist[i] = reachDist[i - 1] == 0 ? 0 : reachDist[i - 1] - 1;
            }
        }

        System.out.println(transAmnt[maxPos]);
    }
}
