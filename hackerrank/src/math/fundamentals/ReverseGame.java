package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class ReverseGame {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(tkn.nextToken());
            int k = Integer.parseInt(tkn.nextToken());

            if (k  < n / 2) {
                System.out.println(2 * k + 1);
            } else {
                System.out.println((n - k - 1) * 2);
            }
        }
    }
}
