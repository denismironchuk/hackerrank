import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 6/12/2018.
 */
public class ExtremumPermutations {
    public static final long MOD = 1000000007;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine(), " ");
        int n = Integer.parseInt(tkn1.nextToken());
        int k = Integer.parseInt(tkn1.nextToken());
        int l = Integer.parseInt(tkn1.nextToken());

        StringTokenizer aTkn = new StringTokenizer(br.readLine(), " ");
        int[] a = new int[n];

        for (int i = 0; i < k; i++) {
            int index = Integer.parseInt(aTkn.nextToken()) - 1;
            if ((index > 0 && a[index - 1] == 1) || (index < n - 1 && a[index + 1] == 1)) {
                System.out.println(0);
                return;
            }
            a[index] = 1;
        }

        StringTokenizer bTkn = new StringTokenizer(br.readLine(), " ");
        int[] b = new int[n];

        for (int i = 0; i < l; i++) {
            int index = Integer.parseInt(bTkn.nextToken()) - 1;
            if (a[index] != 0) {
                System.out.println(0);
                return;
            }

            if ((index > 0 && b[index - 1] == 1) || (index < n - 1 && b[index + 1] == 1)) {
                System.out.println(0);
                return;
            }

            b[index] = 1;
        }

        //-1 - less then previous
        //0 - any value
        //1 - greater then previous
        int[] direction = new int[n];

        for (int i = 1; i < n; i++) {
            if (a[i] == 1 || b[i - 1] == 1) {
                direction[i] = -1;
            } else if (b[i] == 1 || a[i - 1] == 1) {
                direction[i] = 1;
            }
        }

        long[] perms = new long[n];
        perms[0] = 1;

        for (int i = 1; i < n; i++) {
            long[] newPerms = new long[n];
            if (direction[i] == 1) {
                newPerms[0] = 0;
                for (int j = 1; j <= i; j++) {
                    newPerms[j] = (newPerms[j - 1] + perms[j - 1]) % MOD;
                }
            } else if (direction[i] == -1) {
                newPerms[i] = 0;
                for (int j = i - 1; j >= 0; j--) {
                    newPerms[j] = (newPerms[j + 1] + perms[j]) % MOD;
                }
            } else {
                long sum = 0;
                for (int j = 0; j < i; j++) {
                    sum = (sum + perms[j]) % MOD;
                }

                for (int j = 0; j <= i; j++) {
                    newPerms[j] = sum;
                }
            }
            perms = newPerms;
        }

        long res = 0;
        for (int i = 0; i < n; i++) {
            res = (res + perms[i]) % MOD;
        }

        System.out.println(res);
    }
}
