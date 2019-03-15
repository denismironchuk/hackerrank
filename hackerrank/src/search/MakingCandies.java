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
        long leftCandies = 0;
        int iterations = 0;

        if (n <= p) {
            passes = (n / (n * w)) + (n % (n * w) == 0 ? 0 : 1);
        } else {
            while (candies < n) {
                iterations++;
                //System.out.println(m + " " + w);
                long locPasses = 1;
                if (candies - leftCandies < p) {
                    locPasses = ((p - candies + leftCandies) / (m * w)) + ((p - candies + leftCandies) % (m * w) == 0 ? 0 : 1);
                }
                candies += locPasses * m * w;
                passes += locPasses;
                //leftCandies += p;

                if (candies >= n) {
                    break;
                }

                long buy = candies / p;
                long remCandies = candies % p;

                long newM = m;
                long newW = w;

                if (m > w) {
                    newW = w + Math.min(buy, m - w);
                    long rest = buy - newW + w;

                    newM += (rest / 2);
                    newW += (rest / 2) + ((rest % 2) == 0 ? 0 : 1);
                } else if (w > m) {
                    newM = m + Math.min(buy, w - m);
                    long rest = buy - newM + m;

                    newM += (rest / 2);
                    newW += (rest / 2) + ((rest % 2) == 0 ? 0 : 1);
                } else {
                    newM += (buy / 2);
                    newW += (buy / 2) + ((buy % 2) == 0 ? 0 : 1);
                }

                long newPasses = ((n - remCandies) / (newM * newW)) + ((n - remCandies) % (newM * newW) == 0 ? 0 : 1);
                long oldPasses = ((n - candies) / (m * w)) + ((n - candies) % (m * w) == 0 ? 0 : 1);

                if (newPasses <= oldPasses) {
                    candies = remCandies;
                    m = newM;
                    w = newW;
                    leftCandies = 0;
                }
            }
        }

        System.out.println(passes);
        //System.out.println(iterations);
    }
}
