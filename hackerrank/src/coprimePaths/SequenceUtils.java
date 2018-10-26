package coprimePaths;

import java.util.List;

/**
 * Created by Denis_Mironchuk on 10/26/2018.
 */
public class SequenceUtils {
    public static int[] generate(List<Integer> primes, int dataLen, int factorLimit, boolean print) {
        int primesAmnt = primes.size();
        int[] data = new int[dataLen];

        for (int i = 0; i < dataLen; i++) {
            int mults = (int)(factorLimit * Math.random()) + 1;
            data[i] = 1;

            for (int j = 0; j < mults; j++) {
                int prm = primes.get((int)(primesAmnt * Math.random()));
                data[i] *= prm;

                if (print) {
                    System.out.printf("%s ", prm);
                }
            }

            if (print) {
                System.out.printf("\n%s\n", data[i]);
                System.out.println("===============");
            }
        }

        return data;
    }

    public static int maxVal(int[] data) {
        int max = data[0];

        for (int n : data) {
            if (n > max) {
                max = n;
            }
        }

        return max;
    }
}
