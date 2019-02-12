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

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != ordered[i]) {
                mism1++;
            }

            if (arr[i] != ordered[ordered.length - i - 1]) {
                mism2++;
            }
        }

        return Math.min(mism1, mism2) - 1;
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
        }

        int result = lilysHomework(arr);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}
