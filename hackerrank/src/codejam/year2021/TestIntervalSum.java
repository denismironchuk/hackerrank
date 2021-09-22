package codejam.year2021;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestIntervalSum {

    private static final int N = 300;

    public static void main(String[] args) {
        int[] numbers = new int[N + 1];
        Random rnd = new Random();
        for (int i = 1; i <= N; i++) {
            numbers[i] = rnd.nextInt(100);
        }
        List<long[]> intervalSums = new ArrayList<>();
        intervalSums.add(null);

        int totalLen = 0;

        for (int i = 1; i <= N; i++) {
            long[] sums = new long[N / i + 1];
            intervalSums.add(sums);
            int index = 1;
            for (int j = i; j <= N; j += i) {
                sums[index] = sums[index - 1] + numbers[j];
                index++;
            }
            totalLen += sums.length;
        }

        while (true) {
            int l = rnd.nextInt(N + 1);
            int r = rnd.nextInt(N - l + 1) + l;
            int m = rnd.nextInt(10) + 1;

            long trivRes = 0;
            for (int i = l; i <= r; i++) {
                if (i % m == 0) {
                    trivRes += numbers[i];
                }
            }

            long[] sums = intervalSums.get(m);
            long optRes = sums[r / m];
            if (l >= m) {
                optRes -= ((l % m == 0) ? sums[l / m - 1] : sums[l / m]);
            }

            System.out.println(trivRes);
            System.out.println(optRes);
            System.out.println("==================");

            if (trivRes != optRes) {
                throw new RuntimeException();
            }
        }
    }
}
