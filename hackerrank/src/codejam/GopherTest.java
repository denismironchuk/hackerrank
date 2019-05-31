package codejam;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GopherTest {
    public static void main(String[] args) {
        int[] dividers = new int[] {17, 13, 11, 9, 7, 5, 4};
        Set<Integer> hashes = new HashSet<>();

        for (int i = 0; i < 1000000; i++) {
            int[] res = new int[7];
            for (int j = 0; j < 7; j++) {
                res[j] = i % dividers[j];
            }
            hashes.add(Arrays.hashCode(res));
        }

        System.out.println(hashes.size());

        int[] res = new int[7];
        int num = 123124;
        for (int j = 0; j < 7; j++) {
            System.out.printf("%s ", num % dividers[j]);
        }
    }
}
