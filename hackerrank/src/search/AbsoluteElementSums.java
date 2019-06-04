package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class AbsoluteElementSums {
    private static int[] generateArr(int n) {
        int[] arr = new int[n];

        for (int i = 0; i < n; i++) {
            arr[i] = (int)(Math.random() * 100);
        }

        return arr;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        //int[] arr = generateArr(n);
        int[] arr = new int[n];
        StringTokenizer arrTkn = new StringTokenizer(br.readLine());

        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(arrTkn.nextToken());
        }

        Arrays.sort(arr);

        long[] cumSum = new long[n];
        cumSum[0] = arr[0];

        for (int i = 1; i < n; i++) {
            cumSum[i] = cumSum[i - 1] + arr[i];
        }

        int Q = Integer.parseInt(br.readLine());
        StringTokenizer qTkn = new StringTokenizer(br.readLine());

        long qSum = 0;
        for (int q = 0; q < Q; q++) {
            int query = Integer.parseInt(qTkn.nextToken());
            //int query = (int)(Math.random() * 100) - 50;
            qSum += query;

            long res = 0;

            if (-qSum > arr[n - 1]) {
                res = Math.abs(cumSum[n - 1] + qSum * n);
            } else {
                int greaterOrEqualPos = getGreaterOrEqualPosition(arr, -qSum, 0, n - 1);
                if (greaterOrEqualPos == 0) {
                    res = cumSum[n - 1] + qSum * n;
                } else {
                    res = cumSum[n - 1] - cumSum[greaterOrEqualPos - 1] + (n - greaterOrEqualPos) * qSum;
                    res += Math.abs(cumSum[greaterOrEqualPos - 1] + greaterOrEqualPos * qSum);
                }
            }

            //int trivial = countResTrivial(arr, qSum);
            System.out.println(res);
            /*if (res != trivial) {
                throw new RuntimeException("sfsdfsdf");
            }*/
        }
    }

    private static int countResTrivial(int[] arr, long qSum) {
        int res = 0;

        for (int i = 0; i < arr.length; i++) {
            res += Math.abs(arr[i] + qSum);
        }

        return res;
    }

    private static int getGreaterOrEqualPosition(int[] arr, long limitVal, int start, int end) {
        if (start == end) {
            return start;
        }

        int mid = (start + end) / 2;
        if (arr[mid] < limitVal) {
            return getGreaterOrEqualPosition(arr, limitVal, mid + 1, end);
        } else {
            return getGreaterOrEqualPosition(arr, limitVal, start, mid);
        }
    }
}
