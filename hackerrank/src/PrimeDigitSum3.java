import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class PrimeDigitSum3 {
    private static Set<Integer> PRIMES = new HashSet<>();
    private static long MODULO = 1000000007;

    static {
        PRIMES.add(2);PRIMES.add(3);PRIMES.add(5);
        PRIMES.add(7);PRIMES.add(11);PRIMES.add(13);
        PRIMES.add(17);PRIMES.add(19);PRIMES.add(23);
        PRIMES.add(29);PRIMES.add(31);PRIMES.add(37);
        PRIMES.add(41);PRIMES.add(43);
    }

    private class PrimePresentation implements Comparable<PrimePresentation>{
        int sum1;
        int sum2;
        int sum3;
        int sum4;

        int nextIterationsCount = 0;
        List<PrimePresentation> transitions = new ArrayList<>();

        public PrimePresentation(final int sum1, final int sum2, final int sum3, final int sum4) {
            this.sum1 = sum1;
            this.sum2 = sum2;
            this.sum3 = sum3;
            this.sum4 = sum4;

            for (int i = 0; i < 10; i++) {
                if (PRIMES.contains(sum2 + i) && PRIMES.contains(sum3 + i) && PRIMES.contains(sum4 + i)) {
                    nextIterationsCount++;
                }
            }
        }

        boolean hasNextIteration() {
            return nextIterationsCount != 0;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PrimePresentation that = (PrimePresentation) o;

            if (sum1 != that.sum1) return false;
            if (sum2 != that.sum2) return false;
            if (sum3 != that.sum3) return false;
            return sum4 == that.sum4;
        }

        @Override
        public int hashCode() {
            int result = sum1;
            result = 31 * result + sum2;
            result = 31 * result + sum3;
            result = 31 * result + sum4;
            return result;
        }

        @Override
        public String toString() {
            return String.format("%s %s %s %s - %s", sum1, sum2, sum3, sum4, nextIterationsCount);
        }

        @Override
        public int compareTo(final PrimePresentation o) {
            if (Integer.compare(sum2, o.sum2) == 0) {
                if(Integer.compare(sum3, o.sum3) == 0) {
                    return Integer.compare(sum4, o.sum4);
                } else {
                    return Integer.compare(sum3, o.sum3);
                }
            } else {
                return Integer.compare(sum2, o.sum2);
            }
        }
    }

    public void run() {
        Set<PrimePresentation> input = new HashSet<>();

        for (int i = 0; i < 99999; i++) {
            int digit5 = i % 10;
            int digit4 = (i / 10) % 10;
            int digit3 = (i / 100) % 10;
            int digit2 = (i / 1000) % 10;
            int digit1 = (i / 10000) % 10;

            if (PRIMES.contains(digit1 + digit2 + digit3) && PRIMES.contains(digit2 + digit3 + digit4) && PRIMES.contains(digit3 + digit4 + digit5)
                    && PRIMES.contains(digit1 + digit2 + digit3 + digit4) && PRIMES.contains(digit2 + digit3 + digit4 + digit5)
                    && PRIMES.contains(digit1 + digit2 + digit3 + digit4 + digit5) ) {
                PrimePresentation p = new PrimePresentation(digit5,digit5 + digit4, digit5 + digit4 + digit3, digit5 + digit4 + digit3 + digit2);
                if (p.hasNextIteration()) {
                    input.add(p);
                }
            }
        }


        for (PrimePresentation s : input.stream().sorted(PrimePresentation::compareTo).collect(Collectors.toList())) {
            System.out.println(s);
        }

        /*int verticles = verts.size();

        Map<Integer, Integer> valueIndex = new TreeMap<>();
        Map<Integer, Integer> indexValue = new TreeMap<>();

        int index = 0;

        for (int number : verts) {
            valueIndex.put(number, index);
            indexValue.put(index, number);
            index++;
        }

        long[][] matrix = new long[verticles][verticles];

        for (Map.Entry<Integer, List<Integer>> entry : transitions.entrySet()) {
            int src = entry.getKey();
            int srcIndex = valueIndex.get(src);
            for (int dest : entry.getValue()) {
                int destIndex = valueIndex.get(dest);
                matrix[srcIndex][destIndex] = 1;
            }
        }

        long[][] res = powMatrix(matrix, 9);
        int sum = 0;
        for (int i = 0; i < verticles; i++) {
            if (indexValue.get(i) > 9999) {
                for (int j = 0; j < verticles; j++) {
                    sum += res[i][j];
                }
            }
        }

        System.out.println(sum);*/
        //printMatrix(res, indexValue);
    }

    private long[][] powMatrix(long[][] a, int p) {
        if (p == 1) {
            return a;
        }

        if (p % 2 == 0) {
            return powMatrix(matrixMul(a,a), p / 2);
        } else {
            return matrixMul(a, powMatrix(a, p - 1));
        }
    }

    private long[][] matrixMul(long[][] a, long[][] b) {
        Date start = new Date();
        int newMatixSide = a.length;
        long[][] c = new long[newMatixSide][newMatixSide];
        for (int i = 0; i < newMatixSide; i++) {
            for (int j = 0; j < newMatixSide; j++) {
                for (int k = 0; k < newMatixSide; k++) {
                    c[i][j] = (c[i][j] + (a[i][k] * b[k][j]) % MODULO) % MODULO;
                }
            }
        }
        Date end = new Date();
        System.out.printf("Multiplication took - %sms\n",end.getTime() - start.getTime());
        return c;
    }

    private void printMatrix(long[][] a, Map<Integer, Integer> indexValue) {
        int rows = a.length;
        int cols = a[0].length;

        System.out.printf("%10s ", -1);
        for (int i = 0; i < cols; i++) {
            System.out.printf("%10s ", indexValue.get(i));
        }

        System.out.println("\n------------------");

        for (int i = 0; i < rows; i++) {
            System.out.printf("%10s|", indexValue.get(i));
            for (int j = 0; j < cols; j++) {
                System.out.printf("%10s ", a[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        new PrimeDigitSum3().run();
    }
}
