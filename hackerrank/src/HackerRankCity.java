import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 4/23/2018.
 */
public class HackerRankCity {
    public static long MODULO = 1000000007;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        int[] a = new int[N];

        String aStr = br.readLine();
        StringTokenizer aStrTkn = new StringTokenizer(aStr, " ");
        for (int i = 0; i < N; i++) {
            a[i] = Integer.parseInt(aStrTkn.nextToken());
        }

        long cnt = 1;
        long diagLen = 0;
        long distToCorner = 0;
        long pairwiseDist = 0;
        for (int i = 0; i < N; i++) {
            /*pairwiseDist = 4 * pairwiseDist + a[i] + 4 * ((distToCorner + cnt * a[i]) + (distToCorner + cnt * 2 * a[i])) +

                    2 * cnt *(distToCorner + cnt * 3 * a[i] + distToCorner) + cnt * (distToCorner + cnt * 2 * a[i] + distToCorner)+

                    cnt * (distToCorner + cnt * 2 * a[i] + distToCorner) + cnt * (distToCorner + cnt * 3 * a[i] + distToCorner) +

                    cnt * (distToCorner + cnt * 3 * a[i] + distToCorner);*/

            pairwiseDist = (((4 * (pairwiseDist + 2 * distToCorner + (cnt * 3 * a[i]) % MODULO)) % MODULO) + a[i] +

                    ((2 * cnt * ((6 * distToCorner + (cnt * 8 * a[i]) % MODULO) % MODULO)) % MODULO)) % MODULO;

            /*distToCorner = distToCorner + (distToCorner + cnt * (2 * a[i] + diagLen)) +
                    2 * (distToCorner + cnt * (3*a[i] + diagLen)) + (a[i] + diagLen) + (2 * a[i] + diagLen);*/

            distToCorner = (4 * distToCorner + (cnt * ((8 * a[i] + 3 * diagLen) % MODULO) % MODULO) + 3 * a[i] + 2 * diagLen) % MODULO;

            cnt = (cnt * 4 + 2) % MODULO;
            diagLen = (diagLen * 2 + 3 * a[i]) % MODULO;
        }

        System.out.println(pairwiseDist);
    }
}
