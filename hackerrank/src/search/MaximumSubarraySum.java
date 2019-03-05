package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class MaximumSubarraySum {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());

        for (int q = 0; q < Q; q++) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(tkn1.nextToken());
            long m = Long.parseLong(tkn1.nextToken());
            StringTokenizer tkn2 = new StringTokenizer(br.readLine());
            long[] a = new long[n];


            for (int i = 0; i < n; i++) {
                a[i] = Long.parseLong(tkn2.nextToken());
            }

            TreeSet<Long> sums = new TreeSet<>(Long::compareTo);
            long lastSum = a[0] % m;
            long maxSum = lastSum;
            sums.add(lastSum);

            for (int i = 1; i < n; i++) {
                lastSum = (lastSum + a[i]) % m;
                if (lastSum > maxSum) {
                    maxSum = lastSum;
                }

                SortedSet<Long> head = sums.headSet(lastSum);
                SortedSet<Long> tail = sums.tailSet(lastSum, true);
                try {
                    Long headMin = head.first();
                    long cand1 = lastSum - headMin;
                    if (cand1 > maxSum) {
                        maxSum = cand1;
                    }
                } catch (NoSuchElementException ex) {
                }

                try {
                    Long tailMin = tail.first();
                    long cand2 = (m - (tailMin - lastSum)) % m;
                    if (cand2 > maxSum) {
                        maxSum = cand2;
                    }
                } catch (NoSuchElementException ex) {

                }

                sums.add(lastSum);
            }

            System.out.println(maxSum);
        }
    }
}
