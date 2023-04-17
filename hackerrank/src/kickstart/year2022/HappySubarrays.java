package kickstart.year2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class HappySubarrays {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 0; t < T; t++) {
                int n = Integer.parseInt(br.readLine());
                int[] a = new int[n];
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                for (int i = 0; i < n; i++) {
                    a[i] = Integer.parseInt(tkn.nextToken());
                }
                int[] maxLenHappy = new int[n];
                maxLenHappy[n - 1] = a[n - 1] < 0 ? 0 : 1;
                for (int i = n - 2; i >= 0; i--) {
                    if (a[i] < 0) {
                        maxLenHappy[i] = 0;
                    } else {
                        int sum = 0;
                        int j = i;
                        for (; sum + a[j] >= 0 && maxLenHappy[j] == 0; j++) {
                            sum += a[j];
                            maxLenHappy[i]++;
                        }
                        if (maxLenHappy[j] != 0) {
                            maxLenHappy[i] += maxLenHappy[j];
                            j += maxLenHappy[j] + 1;
                            for (; j < n && sum + a[j] >= 0; j++) {
                                sum += a[j];
                                maxLenHappy[i]++;
                            }
                        }
                    }
                }
                Arrays.stream(maxLenHappy).forEach(i -> System.out.printf("%s ", i));
            }
        }
    }
}
