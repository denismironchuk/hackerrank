package math.numberTheory;

public class TriangleNumbers {
    public static void main(String[] agrs) {
        int n = 30;
        int[] triangles = new int[n];
        for (int i = 1; i <= n; i++) {
            triangles[i - 1] = (i * (i + 1)) / 2;
            System.out.printf("%3d ", i);
        }

        System.out.println();

        for (int i = 0; i < n; i++) {
            System.out.printf("%3d ", triangles[i]);
        }

        System.out.println();

        for (int i = 1; i <=n ; i++) {
            double cand = (Math.sqrt(8 * i + 1) - 1) / 2;
            System.out.printf("%2.1f ", cand);
        }
    }
}
