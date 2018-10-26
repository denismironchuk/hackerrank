package coprimePaths;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denis_Mironchuk on 10/26/2018.
 */
public class PrimesGenerator {
    public static List<Integer> generate(int numbers) {
        int[] isPrime = new int[numbers];
        int[] processed = new int[numbers];

        List<Integer> primes = new ArrayList<>();

        for (int i = 2; i < numbers; i++) {
            if (processed[i] == 0) {
                processed[i] = 1;
                isPrime[i] = 1;
                primes.add(i);

                for (int j = 2; i * j < numbers; j++) {
                    processed[i * j] = 1;
                }
            }
        }

        return primes;
    }
}
