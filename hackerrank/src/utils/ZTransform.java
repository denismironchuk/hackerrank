package utils;

/**
 * Created by Влада on 24.03.2018.
 */
public class ZTransform {
    public static int[] effective(String s) {
        int n = s.length();
        int[] z = new int[n];
        int l = 0;
        int r = 0;

        for (int i = 1; i < n; i++) {
            if (i >= l && i <= r) {
                z[i] = z[i - l];

                if (i + z[i] - 1 >= r) {
                    z[i] = r - i + 1;
                    for (; i+z[i] < n && s.charAt(z[i]) == s.charAt(i+z[i]); z[i]++);
                }
            } else {
                for (; i+z[i] < n && s.charAt(z[i]) == s.charAt(i+z[i]); z[i]++);
            }

            if (i + z[i] - 1 > r) {
                r = i + z[i] - 1;
                l = i;
            }
        }

        return z;
    }

    public static int[] trivial(String s) {
        int n = s.length();
        int[] z = new int[n];

        for (int i = 1; i < n; i++){
            for (; i+z[i] < n && s.charAt(z[i]) == s.charAt(i+z[i]); z[i]++);
        }

        return z;
    }

    public static final void main(String[] args) {
        String s = "dddadcdddd";
        int[] z1 = effective(s);
        int[] z2 = trivial(s);

        System.out.println();

        /*while (true) {
            StringBuilder b = new StringBuilder();
            int n = 10;
            for (int i = 0; i < n; i++) {
                b.append((char) ('a' + (int) (4 * Math.random())));
            }
            String s = b.toString();

            int[] z1 = effective(s);
            int[] z2 = trivial(s);

            for (int i = 0; i < n; i++) {
                if (z1[i] != z2[i]) {
                    System.out.println(s);
                    throw new RuntimeException("NOT EQUAL!!!!");
                }
            }
        }*/
    }
}
