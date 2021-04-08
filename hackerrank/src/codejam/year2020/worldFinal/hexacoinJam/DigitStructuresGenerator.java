package codejam.year2020.worldFinal.hexacoinJam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DigitStructuresGenerator {
    private static int n = 10;
    private static int maxDigit = 16;
    private static int[] powers = new int[] {1, 2, 4, 8, 16, 32};

    public static void main(String[] args) {
        List<int[]> structures = new ArrayList<>();
        generateDigitStructures(1, new int[n], 0, structures);
        Set<Integer> hashes = new HashSet<>();
        for (int[] struct : structures) {
            hashes.add(Arrays.hashCode(struct));
        }
        System.out.println(structures.size());
        System.out.println(hashes.size());

        for (int[] struct : structures) {
            analyzeStructure(struct, hashes);
        }
        System.out.println(hashes.size());
        //analyzeStructure(new int[] {1,2,3,4,5,6,7,8,9,0});
    }

    private static void analyzeStructure(int[] structure, Set<Integer> hashes) {
        if (!hashes.contains(Arrays.hashCode(structure))) {
            return;
        }

        for (int i = 1; i < powers[n / 2]; i++) {
            int i_ = i;
            int pos = 0;
            while (i_ != 0) {
                if (i_ % 2 == 1) {
                    int tmp = structure[pos];
                    structure[pos] = structure[pos + n / 2];
                    structure[pos + n / 2] = tmp;
                }
                i_ /= 2;
                pos++;
            }

            /*for (int j = 0; j < n; j++) {
                System.out.printf("%s ", structure[j]);
            }
            System.out.println();*/

            hashes.remove(Arrays.hashCode(structure));

            i_ = i;
            pos = 0;
            while (i_ != 0) {
                if (i_ % 2 == 1) {
                    int tmp = structure[pos];
                    structure[pos] = structure[pos + n / 2];
                    structure[pos + n / 2] = tmp;
                }
                i_ /= 2;
                pos++;
            }
        }
    }

    private static void generateDigitStructures(int pos, int[] structure, int maxVal, List<int[]> structures) {
        if (pos == n) {
            structures.add(Arrays.copyOf(structure, n));
            return;
        }

        for (int val = 0; val <= maxVal; val++) {
            structure[pos] = val;
            generateDigitStructures(pos + 1, structure, maxVal, structures);
        }

        structure[pos] = maxVal + 1;
        generateDigitStructures(pos + 1, structure, maxVal + 1, structures);
    }
}
