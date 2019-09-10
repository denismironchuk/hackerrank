package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class EvenOddQuery {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[] a = new int[n];

        StringTokenizer aTkn = new StringTokenizer(br.readLine());

        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(aTkn.nextToken());
        }

        int q = Integer.parseInt(br.readLine());

        StringBuilder res = new StringBuilder();

        for (int i = 0; i < q; i++) {
            StringTokenizer qTkn = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(qTkn.nextToken());
            int y = Integer.parseInt(qTkn.nextToken());

            if (a[x - 1] % 2 == 1 || x < y && a[x] == 0) {
                res.append("Odd");
            } else {
                res.append("Even");
            }
            res.append("\n");
        }

        System.out.println(res);
    }
}
