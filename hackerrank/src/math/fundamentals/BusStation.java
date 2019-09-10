package math.fundamentals;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BusStation {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[] a = new int[n];

        StringTokenizer aTkn = new StringTokenizer(br.readLine());
        int sum = 0;
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(aTkn.nextToken());
            sum += a[i];
        }

        Set<Integer> divs = new TreeSet<>(Comparator.comparing(Integer::intValue).reversed());
        for (int j = 1; j * j <= sum; j++) {
            if (sum % j == 0) {
                divs.add(j);
                divs.add(sum / j);
            }
        }

        for (Integer div : divs) {
            int divSum = sum / div;
            int locSum = 0;
            boolean sumIsValid = true;
            for (int i = 0; sumIsValid && i < n; i++) {
                locSum += a[i];
                if (locSum > divSum) {
                    sumIsValid = false;
                } else if (locSum == divSum) {
                    locSum = 0;
                }
            }

            if (sumIsValid) {
                System.out.printf("%s ", divSum);
            }
        }
    }
}
