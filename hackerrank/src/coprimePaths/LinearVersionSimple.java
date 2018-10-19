package coprimePaths;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinearVersionSimple {
    public static void main(String[] args) {
        int numbers = 100;
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

        System.out.println(primes);

        int primesAmnt = primes.size();

        int primeFactorLimit = 3;
        int dataLen = 25000;
        int[] data = new int[dataLen];

        int maxValue = -1;
        for (int i = 0; i < dataLen; i++) {
            int mults = (int)(primeFactorLimit * Math.random()) + 1;
            data[i] = 1;

            for (int j = 0; j < mults; j++) {
                int prm = primes.get((int)(primesAmnt * Math.random()));
                data[i] *= prm;
            }

            if (data[i] > maxValue) {
                maxValue = data[i];
            }
        }

        Date start = new Date();

        //Map<Integer, Integer> dynMap = new HashMap<>();
        int[] dynMap = new int[maxValue + 1];
        long resPairs = 0;
        fillMultsCombinations(dynMap, data[0]);

        for (int i = 1; i < dataLen; i++) {
            List<Integer> fact = factor(data[i]);

            if (fact.size() == 1) {
                resPairs += (i - dynMap[data[i]]);
            } else if (fact.size() == 2) {
                int a = fact.get(0);
                int b = fact.get(1);

                resPairs += (i - (dynMap[a] + dynMap[b] - 2 * dynMap[a*b]));
            } else if (fact.size() == 3) {
                int a = fact.get(0);
                int b = fact.get(1);
                int c = fact.get(2);

                int common = dynMap[a] + dynMap[b] + dynMap[c] -
                        (dynMap[a * b] + dynMap[b * c] + dynMap[a * c]) -
                        5 * dynMap[a * b * c];

                resPairs += (i - common);
            }

            fillMultsCombinations(dynMap, data[i]);
        }

        Date end = new Date();
        System.out.println(resPairs);
        System.out.println(end.getTime() - start.getTime() + "ms");
    }

    private static void fillMultsCombinations(int[] dynMap, int n) {
        List<Integer> fact = factor(n);

        if (fact.size() == 1) {
            dynMap[n]++;
        } else if (fact.size() == 2) {
            dynMap[n]++;
            dynMap[fact.get(0)]++;
            dynMap[fact.get(1)]++;
        } else if (fact.size() == 3) {
            dynMap[n]++;

            dynMap[fact.get(0) * fact.get(1)]++;
            dynMap[fact.get(0) * fact.get(2)]++;
            dynMap[fact.get(1) * fact.get(2)]++;

            dynMap[fact.get(0)]++;
            dynMap[fact.get(1)]++;
            dynMap[fact.get(2)]++;
        }

    }

    private static List<Integer> factor(int n) {
        List<Integer> result = new ArrayList<>();

        int n_ = n;
        for (int i = 2; i * i <= n; i++) {
            while (n_ % i == 0) {
                result.add(i);
                n_ /= i;
            }
        }

        if (n_ != 1) {
            result.add(n_);
        }

        return result;
    }
}
