package sorting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class LilysHomework2 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[] arr = new int[n];
        int[] ordered = new int[n];
        StringTokenizer arrTkn = new StringTokenizer(br.readLine());

        Map<Integer, Integer> positions = new HashMap<>();
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(arrTkn.nextToken());
            positions.put(arr[i], i);
            ordered[i] = arr[i];
        }

        Arrays.sort(ordered);

        int perms1 = getPerms(arr, ordered, positions, n);

        for (int i = 0; i < n / 2; i++) {
            int tmp = ordered[i];
            ordered[i] = ordered[n - i - 1];
            ordered[n - i - 1] = tmp;
        }

        int perms2 = getPerms(arr, ordered, positions, n);

        System.out.println(Math.min(perms1, perms2));
    }

    private static int getPerms(int[] arr, int[] ordered, Map<Integer, Integer> positions, int n) {
        int[] processed = new int[n];
        int perms = 0;
        for (int i = 0; i < n; i++) {
            int j = i;
            int movedElmnt = arr[j];

            while (processed[j] == 0 && movedElmnt != ordered[j]) {
                processed[j] = 1;
                j = positions.get(ordered[j]);
                perms++;
            }

            processed[j] = 1;
        }

        return perms;
    }
}