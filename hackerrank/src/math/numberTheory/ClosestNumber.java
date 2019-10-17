package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class ClosestNumber {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        StringBuilder build = new StringBuilder();

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(tkn.nextToken());
            int b = Integer.parseInt(tkn.nextToken());
            int x = Integer.parseInt(tkn.nextToken());

            if (a == 1) {
                if (x == 1) {
                    build.append(1);
                } else {
                    build.append(0);
                }
            } else {
                if (b < 0) {
                    build.append(0);
                } else {
                    int pow = fastPow(a, b);
                    int cand = pow / x;
                    if (pow - cand * x <= (cand + 1) * x - pow) {
                        build.append(cand * x);
                    } else {
                        build.append((cand + 1) * x);
                    }
                }
            }

            build.append("\n");
        }

        System.out.println(build);
    }

    private static int fastPow(int n, int p) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return fastPow(n * n, p / 2);
        } else {
            return n * fastPow(n, p - 1);
        }
    }

}
