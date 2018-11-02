package coprimePaths;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrimesTest {
    private static final int LIMIT = 1000000000;

    public static void main(String[] args) {
        int n = 200000;
        int[] nums = new int[n];

        int max = -1;

        for (int i = 0; i < n; i++) {
            nums[i] = 2 + (int)(LIMIT * Math.random());
            if (nums[i] > max) {
                max = nums[i];
            }
        }

        List<Integer> primes = PrimesGenerator.generate(1 + (int)Math.sqrt(max));
        int[] primesArray = new int[primes.size()];
        int index = 0;
        for (int prime : primes) {
            primesArray[index] = prime;
            index++;
        }

        Date start = new Date();
        for (int i = 0; i < n ; i++) {
            List<Integer> fact = factor(nums[i], primesArray);
        }
        Date end = new Date();

        System.out.println(end.getTime() - start.getTime() + "ms");
    }

    private static List<Integer> factor(int n, int[] primes) {
        List<Integer> factor = new ArrayList<>();

        for (int prime : primes) {
            if (n % prime == 0) {
                factor.add(prime);
            }

            while (n % prime == 0) {
                n /= prime;
            }

            if (n == 1) {
                break;
            }
        }

        if (n != 1) {
            factor.add(n);
        }

        return factor;
    }
}
