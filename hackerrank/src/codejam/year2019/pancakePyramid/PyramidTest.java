package codejam.year2019.pancakePyramid;

public class PyramidTest {
    public static void main(String[] args) {
        while (true) {
            int s = 100;
            long[] p = new long[s];//{5, 0, 1, 0, 1, 8};

            for (int i = 0; i < s; i++) {
                p[i] = (long) (Math.random() * 1000);
                System.out.printf("%2d ", p[i]);
            }
            System.out.println();
            long triv = PyramidTrivial.count(p);
            long opt = PyramidOptimal.count(p);
            System.out.printf("%d %d\n", triv, opt);
            if (triv != opt) {
                throw new RuntimeException();
            }
        }
    }
}
