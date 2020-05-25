package codejam.year2020.round1C;

public class OverrandomizedTest {
    private static final long U = 999999999999999l;
    private static final int T = 10000;

    public static void main(String[] args) {
        int[] stat = new int[10];
        for (int t = 0; t < T; t++) {
            long m = (long)(Math.random() * U) + 1;
            //long m = U;
            long r = (long)(Math.random() * m) + 1;

            //System.out.println(r);

            //while (r != 0) {
            while (r > 9) {
                //stat[(int)(r % 10)]++;
                r /= 10;
            }
            stat[(int)(r)]++;
        }

        for (int i = 1; i < 10; i++) {
            System.out.printf("%s - %s\n", i % 10, stat[i % 10]);
        }
    }
}
