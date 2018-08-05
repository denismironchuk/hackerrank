/**
 * Created by Влада on 11.03.2018.
 */
public class HashTest {
    public static void main(String[] args) {
        char[] chars = {'a','b','a','b','a','b','a','b','a','b'};
        long base = 31;
        long[] hashes = new long[10];
        hashes[0] = chars[0];
        for (int i = 1; i < 10; i++) {
            hashes[i] = hashes[i-1] + chars[i] * base;
            base *= 31;
        }
        System.out.println();
    }
}
