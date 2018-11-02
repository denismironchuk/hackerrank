package coprimePaths;

import java.util.Date;
import java.util.List;

public class LinearVersionSimple {
    public static final int PRIMES_LIMIT = 100;
    public static final int PRIME_FACTOR_LIMIT = 3;
    public static final int DATA_LEN = 25000;

    public static void main(String[] args) {
        List<Integer> primes = PrimesGenerator.generate(PRIMES_LIMIT);
        int[] data = SequenceUtils.generate(primes, DATA_LEN, PRIME_FACTOR_LIMIT, false);
        int maxValue = SequenceUtils.maxVal(data);
        List<Integer> primesForFactor = PrimesGenerator.generate(1 + (int) Math.sqrt(maxValue));
        int[] primesArray = new int[primesForFactor.size()];
        int index = 0;
        for (int prime : primesForFactor) {
            primesArray[index] = prime;
            index++;
        }

        int[][] factorizations = new int[DATA_LEN][PRIME_FACTOR_LIMIT];
        int[] factorSizes = new int[DATA_LEN];

        for (int i = 0; i < DATA_LEN; i++) {
            int[] fact = new int[PRIME_FACTOR_LIMIT];
            factorSizes[i] = NumberUtils.factor(data[i], fact, primesArray);
            factorizations[i] = fact;
        }

        Date start = new Date();

        int[] dynMap = new int[maxValue + 1];
        long resPairs = 0;
        addMultsCombinations(dynMap, factorizations[0], factorSizes[0]);

        for (int i = 1; i < DATA_LEN; i++) {
            resPairs = addNumberToSequence(factorizations[i], factorSizes[i], dynMap, resPairs, i, primesArray);
        }

        System.out.println(resPairs);

        int seqLen = DATA_LEN;
        for (int i = 0; i < DATA_LEN; i++) {
            resPairs = removeNumberFromSequence(factorizations[i], factorSizes[i], dynMap, resPairs, seqLen, primesArray);
            seqLen--;
        }

        Date end = new Date();
        System.out.println(resPairs);
        System.out.println(end.getTime() - start.getTime() + "ms");
    }

    private static long countPairsTrivial(int[] data) {
        long res = 0;
        int[] factor1 = new int[PRIME_FACTOR_LIMIT];
        int[] factor2 = new int[PRIME_FACTOR_LIMIT];
        for (int i = 0; i < DATA_LEN; i++) {
            int size1 = NumberUtils.factor(data[i], factor1);
            for (int j = i+1; j < DATA_LEN; j++) {
                int size2 = NumberUtils.factor(data[j], factor2);
                boolean coPrime = true;
                for (int k = 0; coPrime && k < size1; k++) {
                    for (int l = 0; coPrime && l < size2; l++) {
                        if (factor1[k] == factor2[l]) {
                            coPrime = false;
                        }
                    }
                }
                if (coPrime) {
                    res++;
                }
            }
        }

        return res;
    }

    private static long addNumberToSequence(int[] factor, int factorSize, int[] dynMap, long pairs, int sequenceLen, int[] primes) {
        long resPairs = pairs;

        int common = getCommonMults(dynMap, factor, factorSize);
        resPairs += (sequenceLen - common);

        addMultsCombinations(dynMap, factor, factorSize);

        return resPairs;
    }

    private static long removeNumberFromSequence(int[] factor, int factorSize, int[] dynMap, long pairs, int sequenceLen, int[] primes) {
        long resPairs = pairs;

        removeMultsCombinations(dynMap, factor, factorSize);

        int common = getCommonMults(dynMap, factor, factorSize);
        resPairs -= (sequenceLen - common - 1);

        return resPairs;
    }

    private static int getCommonMults(final int[] dynMap, final int[] fact, final int size) {
        int common = 0;

        if (size == 1) {
            common = dynMap[fact[0]];
        } else if (size == 2) {
            int a = fact[0];
            int b = fact[1];

            common = (dynMap[a] + dynMap[b] - dynMap[a*b]);
        } else if (size == 3) {
            int a = fact[0];
            int b = fact[1];
            int c = fact[2];

            common = dynMap[a] + dynMap[b] + dynMap[c] - (dynMap[a * b] + dynMap[b * c] + dynMap[a * c]) + dynMap[a * b * c];
        }

        return common;
    }

    private static void addMultsCombinations(int[] dynMap, int[] fact, int size){
        manageMultsCombinations(dynMap, fact, size, 1);
    }

    private static void removeMultsCombinations(int[] dynMap, int[] fact, int size){
        manageMultsCombinations(dynMap, fact, size, -1);
    }

    private static void manageMultsCombinations(int[] dynMap, int[] fact, int size, int inc) {
        if (size == 1) {
            dynMap[fact[0]] += inc;
        } else if (size == 2) {
            dynMap[fact[0] * fact[1]] += inc;
            dynMap[fact[0]] += inc;
            dynMap[fact[1]] += inc;
        } else if (size == 3) {
            dynMap[fact[0] * fact[1] * fact[2]]+=inc;

            dynMap[fact[0]] += inc;
            dynMap[fact[1]] += inc;
            dynMap[fact[2]] += inc;

            dynMap[fact[0] * fact[1]] += inc;
            dynMap[fact[0] * fact[2]] += inc;
            dynMap[fact[1] * fact[2]] += inc;
        }
    }
}
