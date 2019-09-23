package games;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StonesGame {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        StringBuilder build = new StringBuilder();
        for (int t = 0; t < T; t++) {
            long n = Integer.parseInt(br.readLine());
            if (n % 2 == 0) {
                long lastMex = log2(n) + 1;
                long cand = fastPow(2, lastMex - 1) - 1;

                for (long i = 2; i < lastMex; i++) {
                    long xorRes = i ^ lastMex ^ 1;
                    if (xorRes < i) {
                        long newHeight = xorRes + 1 == i ? fastPow(2, i - 2) : fastPow(2, xorRes) - 1;
                        long toRemove = fastPow(2, i - 1) - newHeight;
                        if (toRemove < cand) {
                            cand = toRemove;
                        }
                    }
                }
                build.append(cand);
            } else {
                build.append(1);
            }
            build.append("\n");
        }

        System.out.println(build);
    }

    private static final long fastPow(long n, long pow) {
        if (pow == 0) {
            return 1;
        }

        if (pow % 2 == 0) {
            return fastPow(n * n, pow / 2);
        } else {
            return n * fastPow(n, pow - 1);
        }
    }

    private static long log2(long n) {
        long pow = 0;
        long mul = 1;

        while (mul < n) {
            pow++;
            mul *= 2;
        }

        return mul == n ? pow : pow - 1;
    }
}
