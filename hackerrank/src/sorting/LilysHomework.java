package sorting;

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class LilysHomework {
    // Complete the lilysHomework function below.
    static int lilysHomework(int[] arr) {
        int[] ordered = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ordered[i] = arr[i];
        }
        Arrays.sort(ordered);

        int mism1 = 0;
        int mism2 = 0;

        int pairs1 = 0;
        int pairs2 = 0;

        Map<Integer, Set<Integer>> map1 = new HashMap<>();
        Map<Integer, Set<Integer>> map2 = new HashMap<>();

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != ordered[i]) {
                Set<Integer> pairs = map1.get(arr[i]);
                if (pairs == null) {
                    pairs = new HashSet<>();
                }

                if (pairs.contains(ordered[i])) {
                    pairs.remove(ordered[i]);
                    pairs1++;
                    mism1--;
                } else {
                    mism1++;
                    pairs.add(arr[i]);
                    map1.put(ordered[i], pairs);
                }
            }

            if (arr[i] != ordered[ordered.length - i - 1]) {
                Set<Integer> pairs = map2.get(arr[i]);
                if (pairs == null) {
                    pairs = new HashSet<>();
                }

                if (pairs.contains(ordered[ordered.length - i - 1])) {
                    pairs.remove(ordered[ordered.length - i - 1]);
                    pairs2++;
                    mism2--;
                } else {
                    mism2++;
                    pairs.add(arr[i]);
                    map2.put(ordered[ordered.length - i - 1], pairs);
                }
            }
        }

        return Math.min(mism1 - 1 + pairs1, mism2 - 1 + pairs2);
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        int[] arr = new int[n];

        String[] arrItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int i = 0; i < n; i++) {
            int arrItem = Integer.parseInt(arrItems[i]);
            arr[i] = arrItem;
            System.out.println(arrItem);
        }

        int result = lilysHomework(arr);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}
