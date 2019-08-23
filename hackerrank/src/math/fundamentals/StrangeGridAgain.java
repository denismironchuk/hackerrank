package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class StrangeGridAgain {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn = new StringTokenizer(br.readLine());

        long r = Long.parseLong(tkn.nextToken());
        long c = Long.parseLong(tkn.nextToken());

        long res = 10 * ((r - 1) / 2) + (c - 1) * 2;
        if (r % 2 == 0) {
            res++;
        }

        System.out.println(res);
    }
}
