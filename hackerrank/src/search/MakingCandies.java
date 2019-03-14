package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MakingCandies {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer inpt = new StringTokenizer(br.readLine());
        long m = Long.parseLong(inpt.nextToken());
        long w = Long.parseLong(inpt.nextToken());
        long p = Long.parseLong(inpt.nextToken());
        long n = Long.parseLong(inpt.nextToken());

        long candies = 0;
        long passes = 0;

        int iterations = 0;

        while (candies < n) {
            iterations++;
            long locPasses = 1;
            if (candies < p) {
                locPasses = ((p - candies) / (m * w)) + ((p - candies) % (m * w) == 0 ? 0 : 1);
            }
            candies += locPasses * m * w;
            passes += locPasses;

            if (candies >= n) {
                break;
            }

            long buy = candies / p;
            long remCandies = candies % p;
            long newM = (m + w + buy) / 2;
            long newW = (m + w + buy) % 2 == 0 ? (m + w + buy) / 2 : 1 + ((m + w + buy) / 2);

            long newPasses = ((n - remCandies) / (newM * newW)) + ((n - remCandies) % (newM * newW) == 0 ? 0 : 1);
            long oldPasses = ((n - candies) / (m * w)) + ((n - candies) % (m * w) == 0 ? 0 : 1);

            if (newPasses <= oldPasses) {
                candies = remCandies;
                m = newM;
                w = newW;
            }
        }

        System.out.println(passes);
        System.out.println(iterations);
    }
}
