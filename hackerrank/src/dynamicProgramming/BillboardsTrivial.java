package dynamicProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BillboardsTrivial {
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(tkn1.nextToken());
            int k = Integer.parseInt(tkn1.nextToken());
            long[] revs = new long[n];
            for (int i = 0; i < n; i++) {
                revs[i] = Long.parseLong(br.readLine());
                //revs[i] = (long)(Math.random() * 20);
                //System.out.printf("%s ", revs[i]);
            }
            //System.out.println();

            long[] maxRevenue = new long[n];
            maxRevenue[0] = revs[0];
            int subsLen = 1;

            for (int i = 1; i < n; i++) {
                if (subsLen == k) {
                    maxRevenue[i] = -1;
                    long sum = 0;
                    for (int j = i - 1; j >= i - k - 1; j--) {
                        long maxRev = j < 0 ? 0 : maxRevenue[j];
                        if (maxRev + sum >= maxRevenue[i]) {
                            maxRevenue[i] = maxRev + sum;
                            subsLen = i - j - 1;
                        }
                        sum += revs[j + 1];
                    }
                } else {
                    subsLen++;
                    maxRevenue[i] = maxRevenue[i - 1] + revs[i];
                }
            }

            System.out.println(maxRevenue[n - 1]);
        }
    }
}
