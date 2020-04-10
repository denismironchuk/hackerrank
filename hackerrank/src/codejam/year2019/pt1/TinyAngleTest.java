package codejam.year2019.pt1;

import java.util.Arrays;

public class TinyAngleTest {
    public static void main(String[] args) {
        double[] vector1 = new double[]{0, 1};
        double[] vector2 = new double[]{1, 1000000000};

        double sin1 = getSin(vector1, vector2);
        double cos1 = getCos(vector1, vector2);

        System.out.printf("%.30f\n", sin1);

        vector1 = new double[]{0, 1};
        vector2 = new double[]{2, 1000000000};

        double sin2 = getSin(vector1, vector2);
        double cos2 = getCos(vector1, vector2);

        System.out.printf("%.30f\n", sin2);

        vector1 = new double[]{1, 1000000000};
        vector2 = new double[]{1, 999999999};

        double sin3 = getSin(vector1, vector2);
        double cos3 = getCos(vector1, vector2);

        System.out.printf("%.30f\n", sin3);

        vector1 = new double[]{1, 999999999};
        vector2 = new double[]{1, 999999998};

        double sin4 = getSin(vector1, vector2);
        double cos4 = getCos(vector1, vector2);

        System.out.printf("%.30f\n", sin4);

        System.out.println("================");

        Arrays.asList(sin1, sin2, sin3, sin4).stream().sorted().forEach(sin -> System.out.printf("%.30f\n", sin));
        System.out.println("================");
        Arrays.asList(sin1, sin2, sin3, sin4).stream().map(Math::asin).sorted().forEach(sin -> System.out.printf("%.30f\n", sin));
    }

    private static double getSin(double[] vector1, double[] vector2) {
        double area = vector1[0] * vector2[1] - vector1[1] * vector2[0];

        double len1 = Math.sqrt(vector1[0] * vector1[0] + vector1[1] * vector1[1]);
        double len2 = Math.sqrt(vector2[0] * vector2[0] + vector2[1] * vector2[1]);

        return area / (len1 * len2);
    }

    private static double getCos(double[] vector1, double[] vector2) {
        double scalar = vector1[0] * vector2[0] + vector1[1] * vector2[1];

        double len1 = Math.sqrt(vector1[0] * vector1[0] + vector1[1] * vector1[1]);
        double len2 = Math.sqrt(vector2[0] * vector2[0] + vector2[1] * vector2[1]);

        return scalar / (len1 * len2);
    }
}
