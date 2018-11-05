package coprimePaths;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class LinearVersionSimple {
    public static final int PRIMES_LIMIT = 100;
    public static final int PRIME_FACTOR_LIMIT = 3;
    public static final int DATA_LEN = 1000;

    public static class Range {
        private int start;
        private int end;

        public Range(final int start, final int end) {
            this.start = start;
            this.end = end;
        }

        public int getStart() {
            return start;
        }

        public void setStart(final int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(final int end) {
            this.end = end;
        }
    }

    public static void main(String[] args) {
        while (true) {
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

            List<Range> ranges = new ArrayList<>();

            for (int i = 0; i < DATA_LEN; i++) {
                int start = 0;
                int end = 0;

                while (start >= end) {
                    start = (int) (DATA_LEN * Math.random());
                    end = (int) (DATA_LEN * Math.random());
                }

                ranges.add(new Range(start, end));
            }

            double groupLen = Math.sqrt(DATA_LEN);

            ranges.sort(new Comparator<Range>() {
                @Override
                public int compare(final Range o1, final Range o2) {
                    int group1 = (int)((double)o1.getStart() / groupLen);
                    int group2 = (int)((double)o2.getStart() / groupLen);

                    if (group1 == group2) {
                        return Integer.compare(o1.getEnd(), o2.getEnd());
                    } else {
                        return Integer.compare(o1.getStart(), o2.getStart());
                    }
                }
            });

            Date start = new Date();
            List<Long> optResult = countRanges(factorizations, factorSizes, ranges, new int[maxValue + 1]);
            Date end = new Date();

            System.out.println(end.getTime() - start.getTime() + "ms");

            Date start2 = new Date();
            List<Long> optTrivial = countRangesTrivial(factorizations, factorSizes, ranges);
            Date end2 = new Date();

            System.out.println(end2.getTime() - start2.getTime() + "ms");

            for (int i = 0; i < optResult.size(); i++) {
                if (!optResult.get(i).equals(optTrivial.get(i))) {
                    System.out.println(optResult);
                    System.out.println(optTrivial);
                    throw new RuntimeException();
                }
            }
        }
    }

    private static List<Long> countRanges(int[][] factorizations, int[] factorSizes, List<Range> ranges, int[] dynMap) {
        List<Long> result = new ArrayList<>();

        int startPos = 0;
        int endPos = 0;

        long resPairs = 0;
        addMultsCombinations(dynMap, factorizations[0], factorSizes[0]);

        int len = 1;

        for (Range range : ranges) {
            while (range.getStart() < startPos) {
                startPos--;
                resPairs += addNumberToSequence(factorizations[startPos], factorSizes[startPos], dynMap, len);
                len++;
            }

            while (range.getEnd() < endPos) {
                resPairs -= removeNumberFromSequence(factorizations[endPos], factorSizes[endPos], dynMap, len);
                endPos--;
                len--;
            }

            while (range.getEnd() > endPos) {
                endPos++;
                resPairs += addNumberToSequence(factorizations[endPos], factorSizes[endPos], dynMap, len);
                len++;
            }

            while (range.getStart() > startPos) {
                resPairs -= removeNumberFromSequence(factorizations[startPos], factorSizes[startPos], dynMap, len);
                startPos++;
                len--;
            }

            result.add(resPairs);
        }

        return result;
    }

    private static List<Long> countRangesTrivial(int[][] factorizations, int[] factorSizes, List<Range> ranges) {
        List<Long> result = new ArrayList<>();

        for (Range range : ranges) {
            result.add(countPairsTrivial(factorizations, factorSizes, range));
        }

        return result;
    }

    private static long countPairsTrivial(int[][] factorizations, int[] factorSizes, Range range) {
        long res = 0;
        for (int i = range.getStart(); i <= range.getEnd(); i++) {
            int[] factor1 = factorizations[i];
            int size1 = factorSizes[i];

            for (int j = i+1; j <= range.getEnd(); j++) {
                int[] factor2 = factorizations[j];
                int size2 = factorSizes[j];
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

    private static long addNumberToSequence(int[] factor, int factorSize, int[] dynMap, int sequenceLen) {
        int common = getCommonMults(dynMap, factor, factorSize);
        addMultsCombinations(dynMap, factor, factorSize);

        return (sequenceLen - common);
    }

    //sequenceLen - includes removedElement
    private static long removeNumberFromSequence(int[] factor, int factorSize, int[] dynMap, int sequenceLen) {
        removeMultsCombinations(dynMap, factor, factorSize);
        int common = getCommonMults(dynMap, factor, factorSize);

        return (sequenceLen - common - 1);
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
