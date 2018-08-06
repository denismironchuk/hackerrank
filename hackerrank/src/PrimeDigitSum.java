import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class PrimeDigitSum {
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

        public int buildNumber() {
            int result = d;
            int mult = 10;
            DigitNode p = this.parent;
            while (p != null && p.d != -1) {
                result = p.d * mult + result;
                mult *= 10;
                p = p.parent;
            }

            return result;
        }
    }

    public void run() {
        DigitNode root = new DigitNode();

        Map<Integer, Long> numbersMap = new HashMap<>();
        Set<Integer> set1 = new TreeSet<>();

        for (int i = 0; i < 99999; i++) {
            int digit5 = i % 10;
            int digit4 = (i / 10) % 10;
            int digit3 = (i / 100) % 10;
            int digit2 = (i / 1000) % 10;
            int digit1 = (i / 10000) % 10;

            if (PRIMES.contains(digit1 + digit2 + digit3) && PRIMES.contains(digit2 + digit3 + digit4) && PRIMES.contains(digit3 + digit4 + digit5)
                    && PRIMES.contains(digit1 + digit2 + digit3 + digit4) && PRIMES.contains(digit2 + digit3 + digit4 + digit5)
                    && PRIMES.contains(digit1 + digit2 + digit3 + digit4 + digit5) ) {
                root.addNumber(i, 5);
                System.out.printf("%5s\n", i);
                set1.add(i);
                if (i > 9999) {
                    numbersMap.put(i, 1l);
                }
            }
        }

        //System.out.println("===============");

        //List<Integer> res = new ArrayList<>();
        Set<Integer> set2 = new TreeSet<>();
        Set<Integer> set3 = new TreeSet<>();

        Date start = new Date();
        for (int i = 0; i < 1; i++) {
            Map<Integer, Long> newNumbersMap = new TreeMap<>();
            for (Map.Entry<Integer, Long> entry : numbersMap.entrySet()) {
                int number = entry.getKey();
                long cnt = entry.getValue();

                DigitNode node = root.getNode(number % 10000, 4);
                if (null != node) {
                    set2.add(number);
                    for (DigitNode child : node.childrenArray) {
                        set3.add((number * 10 + child.d) % 10000);
                        int newNumber = child.buildNumber();
                        newNumbersMap.merge(newNumber, cnt, (newVal, oldVal) -> (newVal + oldVal) % MODULO);
                    }
                }
            }
            numbersMap = newNumbersMap;
        }

        /*System.out.println(numbersMap.values().stream().reduce((s, a) -> s + a).get());
        Date end = new Date();
        System.out.println(end.getTime() - start.getTime() + "ms");*/

        /*set1.retainAll(set2);
        set1.retainAll(set3);

        for (int i : set1) {
            System.out.println(i);
        }*/
    }

    public static void main(String[] args) {
        new PrimeDigitSum().run();
    }
}
