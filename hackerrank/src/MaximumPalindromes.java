import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Влада on 23.02.2018.
 */
public class MaximumPalindromes {
    public static final int MODULO = 1000000007;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        char[] sArr = s.toCharArray();

        long[] precalcFact = new long[sArr.length / 2 + 1];
        precalcFact[0] = 1;

        for (int i = 1; i < precalcFact.length; i++){
            precalcFact[i] = (precalcFact[i - 1] * (long)i) % MODULO;
        }

        int[][] freqs = new int[sArr.length][26];
        freqs[0][sArr[0] - 97] = 1;
        for (int i = 1; i < sArr.length; i++) {
            for (int j = 0; j < 26; j++) {
                freqs[i][j] = freqs[i - 1][j];
            }
            freqs[i][sArr[i] - 97]++;
        }

        int Q = Integer.parseInt(br.readLine());

        StringBuilder ans = new StringBuilder();

        for (int q = 0; q < Q; q++) {
            String lr = br.readLine();
            StringTokenizer tkn = new StringTokenizer(lr, " ");
            int l = Integer.parseInt(tkn.nextToken());
            int r = Integer.parseInt(tkn.nextToken());

            int[] cnt = new int[26];

            /*for (int i = l - 1; i < r; i++) {
                cnt[sArr[i] - 97]++;
            }*/

            for (int i = 0; i < 26; i++) {
                cnt[i] = freqs[r - 1][i] - freqs[l - 1][i];
                if (cnt[i] != 0){
                    cnt[i]++;
                }
            }

            int rem = 0;
            int sum = 0;
            long fact2 = 1;
            for (int i = 0; i < 26; i++) {
                int count = cnt[i];
                sum += count / 2;
                fact2 = (fact2 * precalcFact[count / 2]) % MODULO;
                rem+=count % 2;
            }

            long fact1 = precalcFact[sum];

            if (rem == 0) {
                rem = 1;
            }

            long res = (((fact1 * fastPow(fact2, MODULO - 2)) % MODULO) * rem) % MODULO;
            ans.append(res).append("\n");
        }

        System.out.println(ans.toString());
    }

    private static long fastPow(long n, long p) {
        if (p == 0) {
            return 1;
        }

        return p % 2 == 0 ? fastPow((n * n) % MODULO, p / 2) % MODULO : (n * fastPow(n, p - 1)) % MODULO;
    }
}
