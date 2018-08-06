import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Candies {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader br = new BufferedReader(new FileReader("D:/candies.txt"));
        int n = Integer.parseInt(br.readLine());
        int[] ratings = new int[n];

        for (int i = 0; i < n; i++) {
            ratings[i] = Integer.parseInt(br.readLine());
        }

        int incr = 1;
        int decr = 0;

        long sum = 1;

        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                if (decr > 0) {
                    decr = 0;
                    incr = 1;
                }
                incr++;
                sum+=incr;
            } else if (ratings[i] < ratings[i - 1]) {
                decr++;
                sum+=decr;
                if (incr <= decr) {
                    sum++;
                    incr++;
                }
            } else if (ratings[i] == ratings[i - 1]) {
                decr = 1;
                incr = Integer.MAX_VALUE;
                sum++;
            }
        }

        System.out.println(sum);
    }
}
