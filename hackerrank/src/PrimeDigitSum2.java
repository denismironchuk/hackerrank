import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

public class PrimeDigitSum2 {
    private static Set<Integer> PRIMES = new HashSet<>();
    private static long MODULO = 1000000007;

    static {
        PRIMES.add(2);PRIMES.add(3);PRIMES.add(5);
        PRIMES.add(7);PRIMES.add(11);PRIMES.add(13);
        PRIMES.add(17);PRIMES.add(19);PRIMES.add(23);
        PRIMES.add(29);PRIMES.add(31);PRIMES.add(37);
        PRIMES.add(41);PRIMES.add(43);
    }

    private class DigitNode {
        int d = -1;
        DigitNode[] children = new DigitNode[10];
        DigitNode parent = null;
        List<DigitNode> childrenArray = new ArrayList<>();

        public DigitNode() {
        }

        public DigitNode(final int d, final DigitNode parent) {
            this.d = d;
            this.parent = parent;
        }

        public void addNumber(int n, int digits) {
            int digit1 = n;
            int divide = 1;
            for (int i = 1; i < digits; i++) {
                digit1 /= 10;
                divide *= 10;
            }

            if (children[digit1] == null) {
                DigitNode newNode = new DigitNode(digit1, this);
                children[digit1] = newNode;
                childrenArray.add(newNode);
                if (digits > 1) {
                    newNode.addNumber(n % divide, digits - 1);
                }
            } else {
                children[digit1].addNumber(n % divide, digits - 1);
            }
        }

        public DigitNode getNode(int n, int digits) {
            if (digits == 1) {
                return children[n];
            }

            int digit1 = n;
            int divide = 1;
            for (int i = 1; i < digits; i++) {
                digit1 /= 10;
                divide *= 10;
            }

            if (children[digit1] != null) {
                return children[digit1].getNode(n % divide, digits - 1);
            } else {
                return null;
            }
        }
    }

    public void run() {
        Set<Integer> input = new TreeSet<>();

        for (int i = 0; i < 99999; i++) {
            int digit5 = i % 10;
            int digit4 = (i / 10) % 10;
            int digit3 = (i / 100) % 10;
            int digit2 = (i / 1000) % 10;
            int digit1 = (i / 10000) % 10;

            if (PRIMES.contains(digit1 + digit2 + digit3) && PRIMES.contains(digit2 + digit3 + digit4) && PRIMES.contains(digit3 + digit4 + digit5)
                    && PRIMES.contains(digit1 + digit2 + digit3 + digit4) && PRIMES.contains(digit2 + digit3 + digit4 + digit5)
                    && PRIMES.contains(digit1 + digit2 + digit3 + digit4 + digit5) ) {
                input.add(i);
            }
        }

        DigitNode root = buildDigitTree(input);
        Set<Integer> verts = new TreeSet<>();
        Map<Integer, List<Integer>> transitions = buildTransitions(input, root, verts);

        int verticles = verts.size();

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

        long[][] res = powMatrix(matrix, 300);
        int sum = 0;
        for (int i = 0; i < verticles; i++) {
            if (indexValue.get(i) > 9999) {
                for (int j = 0; j < verticles; j++) {
                    sum += res[i][j];
                }
            }
        }

        System.out.println(sum);
        printMatrix(res, indexValue);
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
            //if (indexValue.get(i) > 9999) {
                System.out.printf("%10s ", indexValue.get(i));
            //}
        }

        System.out.println("\n------------------");

        for (int i = 0; i < rows; i++) {
            //if (indexValue.get(i) > 9999) {
                System.out.printf("%10s|", indexValue.get(i));
                for (int j = 0; j < cols; j++) {
                    //if (indexValue.get(j) > 9999) {
                        System.out.printf("%10s ", a[i][j]);
                    //}
                }
                System.out.println();
           //}
        }
    }

    private DigitNode buildDigitTree(Set<Integer> numbers) {
        DigitNode root = new DigitNode();

        for (int n : numbers) {
            root.addNumber(n, 5);
        }

        return root;
    }

    private Map<Integer, List<Integer>> buildTransitions(Set<Integer> input, DigitNode root, Set<Integer> verticles) {
        Map<Integer, List<Integer>> output = new TreeMap<>();

        for (Integer number : input) {
            DigitNode node = root.getNode(number % 10000, 4);
            if (null != node) {
                for (DigitNode child : node.childrenArray) {
                    List<Integer> transitions = output.getOrDefault(number, new ArrayList<>());
                    int dest = (number * 10 + child.d) % 100000;
                    transitions.add(dest);
                    output.put(number, transitions);
                    verticles.add(dest);
                    verticles.add(number);
                }
            }
        }

        return output;
    }

    public static void main(String[] args) {
        new PrimeDigitSum2().run();
    }
}
