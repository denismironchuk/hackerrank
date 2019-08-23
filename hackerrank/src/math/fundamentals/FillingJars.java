package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class FillingJars {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        int m = Integer.parseInt(tkn1.nextToken());

        long res = 0;

        for (int i = 0; i < m; i++) {
            StringTokenizer tkn2 = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(tkn2.nextToken());
            int b = Integer.parseInt(tkn2.nextToken());
            long k = Long.parseLong(tkn2.nextToken());

            res += k * (b - a + 1);
        }

        res /= n;

        System.out.println(res);
    }
}
