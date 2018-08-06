import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.StringTokenizer;

public class SummingPieces2 {
    public static long MOD = 1000000007;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        String aStr = br.readLine();
        StringTokenizer aStrTkn = new StringTokenizer(aStr, " ");
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = Long.parseLong(aStrTkn.nextToken());
        }

        long[] sum = new long[n];
        sum[0] = a[0];

        long sumsum = a[0];
        long aSum = a[0];
        long aSumCnt = 2;

        long twoPow = 2;
        long twoPowSum = 0;

        long incTwoPowSum = 0;
        long incTwo = 0;

        for (int i = 1; i<n; i++) {
            incTwoPowSum = (incTwoPowSum + incTwo) % MOD;

            incTwo = (incTwo + (twoPow * a[i]) % MOD) % MOD;

            twoPow = (twoPow * 2) % MOD;
            twoPowSum = (twoPowSum + (a[i] * twoPow) % MOD) % MOD;

            aSum = (aSum + a[i]) % MOD;
            sum[i] = (sum[i] + sumsum + (aSum * aSumCnt) % MOD + twoPowSum + incTwoPowSum) % MOD;
            sum[i] = (sum[i] - (((aSumCnt + 1) * ((aSum - a[0] + MOD) % MOD)) % MOD) + MOD) % MOD;


            aSumCnt++;
            //---------------
            sumsum = (sumsum + sum[i]) % MOD;

        }

        System.out.println(sum[n-1]);
    }
}
