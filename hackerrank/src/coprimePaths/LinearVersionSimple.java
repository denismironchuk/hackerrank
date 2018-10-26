package coprimePaths;

import java.util.Date;
import java.util.List;

public class LinearVersionSimple {
    public static final int PRIMES_LIMIT = 10;
    public static final int PRIME_FACTOR_LIMIT = 3;
    public static final int DATA_LEN = 5;

    public static void main(String[] args) {
        List<Integer> primes = PrimesGenerator.generate(PRIMES_LIMIT);
        int[] data = SequenceUtils.generate(primes, DATA_LEN, PRIME_FACTOR_LIMIT, true);
        int maxValue = SequenceUtils.maxVal(data);

        Date start = new Date();

        int[] dynMap = new int[maxValue + 1];
        long resPairs = 0;
        int[] fact = new int[PRIME_FACTOR_LIMIT];
        int size = NumberUtils.factor(data[0], fact);
        addMultsCombinations(dynMap, data[0], fact, size);

        for (int i = 1; i < DATA_LEN; i++) {
            resPairs = addNumberToSequence(data[i], dynMap, resPairs, i);
        }

        System.out.println(resPairs);

        int seqLen = DATA_LEN;
        for (int i = 0; i < DATA_LEN; i++) {
            resPairs = removeNumberFromSequence(data[i], dynMap, resPairs, seqLen);
            seqLen--;
        }

        Date end = new Date();
        System.out.println(resPairs);
        System.out.println(end.getTime() - start.getTime() + "ms");
    }

    private static long addNumberToSequence(int num, int[] dynMap, long pairs, int sequenceLen) {
        int[] fact = new int[PRIME_FACTOR_LIMIT];
        int size = NumberUtils.factor(num, fact);
        long resPairs = pairs;

        int common = getCommonMults(num, dynMap, fact, size);
        resPairs += (sequenceLen - common);

        addMultsCombinations(dynMap, num, fact, size);

        return resPairs;
    }

    private static long removeNumberFromSequence(int num, int[] dynMap, long pairs, int sequenceLen) {
        int[] fact = new int[PRIME_FACTOR_LIMIT];
        int size = NumberUtils.factor(num, fact);
        long resPairs = pairs;

       removeMultsCombinations(dynMap, num, fact, size);

        int common = getCommonMults(num, dynMap, fact, size);
        resPairs -= (sequenceLen - common);

        return resPairs;
    }

    private static int getCommonMults(final int num, final int[] dynMap, final int[] fact, final int size) {
        int common = 0;

        if (size == 1) {
            common = dynMap[num];
        } else if (size == 2) {
            int a = fact[0];
            int b = fact[1];

            common = (dynMap[a] + dynMap[b] - 2 * dynMap[a*b]);
        } else if (size == 3) {
            int a = fact[0];
            int b = fact[1];
            int c = fact[2];

            common = dynMap[a] + dynMap[b] + dynMap[c] -
                    (dynMap[a * b] + dynMap[b * c] + dynMap[a * c]) -
                    5 * dynMap[a * b * c];
        }
        return common;
    }

    private static void addMultsCombinations(int[] dynMap, int n, int[] fact, int size){
        int[] factUnique = new int[PRIME_FACTOR_LIMIT];
        int sizeUnique = NumberUtils.getUniqueValues(fact, size, factUnique);

        manageMultsCombinations(dynMap, n, factUnique, sizeUnique, 1);
    }

    private static void removeMultsCombinations(int[] dynMap, int n, int[] fact, int size){
        int[] factUnique = new int[PRIME_FACTOR_LIMIT];
        int sizeUnique = NumberUtils.getUniqueValues(fact, size, factUnique);

        manageMultsCombinations(dynMap, n, factUnique, sizeUnique, -1);
    }

    private static void manageMultsCombinations(int[] dynMap, int n, int[] fact, int size, int inc) {
        if (size == 1) {
            dynMap[n] += inc;
        } else if (size == 2) {
            dynMap[n] += inc;
            dynMap[fact[0]] += inc;
            dynMap[fact[1]] += inc;
        } else if (size == 3) {
            dynMap[n]++;

            dynMap[fact[0] * fact[1]] += inc;
            dynMap[fact[0] * fact[2]] += inc;
            dynMap[fact[1] * fact[2]] += inc;

            dynMap[fact[0]] += inc;
            dynMap[fact[1]] += inc;
            dynMap[fact[2]] += inc;
        }
    }
}
