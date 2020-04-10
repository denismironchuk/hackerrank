package codejam.year2019;

import java.util.ArrayList;
import java.util.List;

public class AntStackBruteforce {
    private static class AntStack {
         private int weight;
         private int size;

        public AntStack(int weight) {
            this.weight = weight;
            this.size = 1;
        }

        public AntStack(AntStack stack, int weight) {
            this.weight = stack.weight + weight;
            this.size = stack.size + 1;
        }
    }

    private static class Ant {
        private long weight;

        private long commonWeight;
        private int stackSize = 1;

        public Ant(long weight) {
            this.weight = weight;
            this.commonWeight = weight;
        }
    }

    private static int findMax(int[] w) {
        int n = w.length;
        long[][] dynTable = new long[n][150];
        for (int row = 0; row < n; row++) {
            for (int col = 1; col < 150; col++) {
                dynTable[row][col] = Long.MAX_VALUE;
            }
        }

        dynTable[0][1] = w[0];
        for (int row = 1; row < n; row++) {
            for (int col = 1; col < 150; col++) {
                if (w[row] * 6 >= dynTable[row - 1][col - 1]) {
                    dynTable[row][col] = Math.min(dynTable[row - 1][col], dynTable[row - 1][col - 1] + w[row]);
                } else {
                    dynTable[row][col] = dynTable[row - 1][col];
                }
            }
        }

        int res = 1;
        while(dynTable[n - 1][res] != Long.MAX_VALUE) {
            res++;
        }

        return res - 1;
    }

    private static int findMaxStackBruteForce(int[] ants) {
        List<AntStack> possibleStacks = new ArrayList<>();

        for (int i = 0; i < ants.length; i++) {
            List<AntStack> newStacks = new ArrayList<>();
            newStacks.add(new AntStack(ants[i]));
            for (AntStack possible : possibleStacks) {
                if (possible.weight <= 6 * ants[i]) {
                    newStacks.add(new AntStack(possible, ants[i]));
                }
            }
            possibleStacks.addAll(newStacks);
        }

        int maxStack = 0;

        for (AntStack possible : possibleStacks) {
            if (possible.size > maxStack) {
                maxStack = possible.size;
            }
        }

        return maxStack;
    }

    public static void main(String[] args) {
        int n = 40;

        while (true) {
            int[] ants = new int[n];

            for (int i = 0; i < n; i++) {
                ants[i] = 1 + (int) (Math.random() * 100);
                System.out.printf("%s ", ants[i]);
            }

            System.out.println();

            int max1 = findMaxStackBruteForce(ants);
            int max2 = findMax(ants);

            System.out.printf("%s %s\n", max1, max2);

            if (max1 != max2) {
                throw new RuntimeException();
            }
        }
    }
}
