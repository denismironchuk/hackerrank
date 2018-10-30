package coprimePaths;

import java.util.Date;
import java.util.List;

public class LinearVersionSimple {
    public static final int PRIMES_LIMIT = 100;
    public static final int PRIME_FACTOR_LIMIT = 3;
    public static final int DATA_LEN = 5;

    public static void main(String[] args) {
        //while (true) {
            List<Integer> primes = PrimesGenerator.generate(PRIMES_LIMIT);
            //int[] data = SequenceUtils.generate(primes, DATA_LEN, PRIME_FACTOR_LIMIT, false);
            int[] data = {6887, 22649, 7198, 89, 22649};
            int maxValue = SequenceUtils.maxVal(data);

            for (int i = 0; i < DATA_LEN; i++) {
                int[] fact = new int[PRIME_FACTOR_LIMIT];
                int size = NumberUtils.factor(data[i], fact);
                for (int j = 0; j < size; j++) {
                    System.out.printf("%s ", fact[j]);
                }
                System.out.println();
            }

            Date start = new Date();

            int[] dynMap = new int[maxValue + 1];
            long resPairs = 0;
            int[] fact = new int[PRIME_FACTOR_LIMIT];
            int size = NumberUtils.factor(data[0], fact);
            addMultsCombinations(dynMap, data[0], fact, size);

            for (int i = 1; i < DATA_LEN; i++) {
                resPairs = addNumberToSequence(data[i], dynMap, resPairs, i);
            }

            //System.out.println(resPairs);
            //System.out.println(countPairsTrivial(data));

            if (resPairs != countPairsTrivial(data)) {
                throw new RuntimeException();
            }
        //}
        /*int seqLen = DATA_LEN;
        for (int i = 0; i < DATA_LEN; i++) {
            resPairs = removeNumberFromSequence(data[i], dynMap, resPairs, seqLen);
            seqLen--;
        }

        Date end = new Date();
        System.out.println(resPairs);
        System.out.println(end.getTime() - start.getTime() + "ms");*/
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
        resPairs -= (sequenceLen - common - 1);

        return resPairs;
    }

    private static int getCommonMults(final int num, final int[] dynMap, final int[] fact, final int size) {
        int common = 0;

        if (size == 1) {
            common = dynMap[num];
        } else if (size == 2) {
            int a = fact[0];
            int b = fact[1];

            if (a == b) {
                common = dynMap[a];
            } else {
                common = (dynMap[a] + dynMap[b] - dynMap[a*b]);
            }
        } else if (size == 3) {
            int a = fact[0];
            int b = fact[1];
            int c = fact[2];

            if (a == b && b == c) {
                common = dynMap[a];
            } else if (a == b && b != c) {
                common = dynMap[a] + dynMap[c] - dynMap[a * c] - 3 * dynMap[a*a*c];
            } else if (a != b && b == c) {
                common = dynMap[a] + dynMap[b] - dynMap[a * b] - 3 * dynMap[a*b*b];
            } else {
                common = dynMap[a] + dynMap[b] + dynMap[c] -
                        (dynMap[a * b] + dynMap[b * c] + dynMap[a * c]) -
                        5 * dynMap[a * b * c];
            }
        }

        return common;
    }

    private static void addMultsCombinations(int[] dynMap, int n, int[] fact, int size){
        manageMultsCombinations(dynMap, n, fact, size, 1);
    }

    private static void removeMultsCombinations(int[] dynMap, int n, int[] fact, int size){
        manageMultsCombinations(dynMap, n, fact, size, -1);
    }

    private static void manageMultsCombinations(int[] dynMap, int n, int[] fact, int size, int inc) {
        //System.out.printf("%s %s %s\n", fact[0], fact[1], fact[2]);
        if (size == 1) {
            dynMap[n] += inc;
        } else if (size == 2) {
            dynMap[n] += inc;
            dynMap[fact[0]] += inc;

            if (fact[0] != fact[1]) {
                dynMap[fact[1]] += inc;
            }
        } else if (size == 3) {
            dynMap[n]+=inc;
            dynMap[fact[0]] += inc;
            dynMap[fact[0] * fact[1]] += inc;

            if (fact[0] != fact[1] && fact[1] != fact[2]) {
                dynMap[fact[1]] += inc;
                dynMap[fact[2]] += inc;
                dynMap[fact[0] * fact[2]] += inc;
                dynMap[fact[1] * fact[2]] += inc;
            } else if (fact[0] == fact[1] && fact[1] != fact[2]) {
                dynMap[fact[2]] += inc;
                dynMap[fact[0] * fact[2]] += inc;
            } else if (fact[0] != fact[1] && fact[1] == fact[2]) {
                dynMap[fact[1]] += inc;
                dynMap[fact[1] * fact[2]] += inc;
            }
        }
    }
}
