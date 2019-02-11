package sorting;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BigSorting {
    private static final int max = 10;

    private static int[][] countSort(int[][] arr, int pos, int len, int[][] res) {
        int n = arr.length;
        int[] counts = new int[max];

        for (int i = 0; i < n; i++) {
            counts[arr[i][pos]]++;
        }

        for (int i = 1; i < max; i++) {
            counts[i] += counts[i - 1];
        }

        for (int i = n - 1; i > -1; i--) {
            res[counts[arr[i][pos]] - 1] = arr[i];
            counts[arr[i][pos]]--;
        }

        return res;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader br = new BufferedReader(new FileReader("D:\\sort.txt"));
        int n = Integer.parseInt(br.readLine());
        String[] inpt = new String[n];

        for (int i = 0; i < n; i++) {
            inpt[i] = br.readLine();
        }

        Arrays.sort(inpt, Comparator.comparingInt(String::length));

        List<int[]> unsorted = new ArrayList<>();
        unsorted.add(convertToDigitsArray(inpt[0]));

        for (int i = 1; i < n; i++) {
            int curLen = inpt[i].length();
            int prevLen = inpt[i - 1].length();

            if (curLen != prevLen) {
                int[][] arr = toArray(unsorted, prevLen);
                int[][] res = new int[arr.length][prevLen];
                for (int pos = prevLen - 1; pos > -1; pos--) {
                    countSort(arr, pos, prevLen, res);
                    int[][] tmp = arr;
                    arr = res;
                    res = tmp;
                }
                print(arr);
                unsorted = new ArrayList<>();
            }

            unsorted.add(convertToDigitsArray(inpt[i]));
        }

        int curLen = unsorted.get(0).length;
        int[][] arr = toArray(unsorted, curLen);
        int[][] res = new int[arr.length][curLen];
        for (int pos = curLen - 1; pos > -1; pos--) {
            countSort(arr, pos, curLen, res);
            int[][] tmp = arr;
            arr = res;
            res = tmp;
            //System.out.println(pos);
        }
        print(arr);
    }

    private static void print(int[][] arr) {
        int n = arr.length;
        int len = arr[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < len; j++) {
                System.out.print(arr[i][j]);
            }
            System.out.println();
        }
    }

    private static int[][] toArray(List<int[]> list, int len) {
        int n = list.size();
        int[][] res = new int[n][len];

        for (int i = 0; i < n; i++) {
            res[i] = list.get(i);
        }

        return res;
    }

    private static int[] convertToDigitsArray(String number) {
        int[] res = new int[number.length()];

        for (int i = 0; i < number.length(); i++) {
            res[i] = number.charAt(i) - '0';
        }

        return res;
    }
}
