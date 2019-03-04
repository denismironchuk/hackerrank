package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Pairs {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        int k = Integer.parseInt(tkn1.nextToken());
        StringTokenizer tkn2 = new StringTokenizer(br.readLine());
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(tkn2.nextToken());
        }

        Arrays.sort(arr);
        int p1 = 0;
        int p2 = 1;
        int pairs = 0;
        while (p1 != n - 1 || p2 != n - 1) {
            if (arr[p2] - arr[p1] == k) {
                pairs++;
                p1++;
                if (p2 == n - 1) {
                    break;
                }else {
                    p2++;
                }
            } else if (arr[p2] - arr[p1] < k) {
                if (p2 == n - 1) {
                    break;
                } else {
                    p2++;
                }
            } else {
                p1++;
            }
        }

        System.out.println(pairs);
    }
}
