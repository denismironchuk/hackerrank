import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class ScherlockAndCost {
    static int cost(int[] arr) {
        int sumFirst = 0;
        int sumLast = 0;

        for (int i = 1; i < arr.length; i++) {
            int newFirst = Math.max(sumLast + arr[i - 1] - 1, sumFirst);
            int newLast = Math.max(sumFirst + Math.abs(arr[i] - 1), sumLast + Math.abs(arr[i] - arr[i - 1]));

            sumFirst = newFirst;
            sumLast = newLast;
        }

        return Math.max(sumFirst, sumLast);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for(int a0 = 0; a0 < t; a0++){
            int n = in.nextInt();
            int[] arr = new int[n];
            for(int arr_i = 0; arr_i < n; arr_i++){
                arr[arr_i] = in.nextInt();
            }
            int result = cost(arr);
            System.out.println(result);
        }
        in.close();
    }
}
