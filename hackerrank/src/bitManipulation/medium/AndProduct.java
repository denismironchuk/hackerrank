package bitManipulation.medium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class AndProduct {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        for (int i = 0; i < n; i++) {
            StringTokenizer pairTkn = new StringTokenizer(br.readLine());
            long a = Long.parseLong(pairTkn.nextToken());
            long b = Long.parseLong(pairTkn.nextToken());

            long res = 0;
            long currentBitMult = 1;

            long a_ = a;

            while (a_ != 0) {
                long bit = a_ % 2;
                if (bit == 1) {
                    long nextZero = currentBitMult * (a / currentBitMult + 1);
                    if (nextZero > b) {
                        res += currentBitMult;
                    }
                }
                a_ /= 2;
                currentBitMult *= 2;
            }

            System.out.println(res);
        }
    }
}
