package utils;

import java.util.Arrays;

/**
 * Created by Denis_Mironchuk on 3/28/2018.
 */
public class ZFunction {
    public static int[] zFunction(String s) {
        int n = s.length();
        int[] z = new int[n];

        int l = 0;
        int r = 0;

        for (int i = 1; i < n; i++) {
            if (i >= l && i <= r) {
                z[i] = z[i - l];

                if (z[i] >= r - i + 1) {
                    z[i] = r - i;
                    for (; i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i]); z[i]++);
                }
            } else {
                for (; i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i]); z[i]++);
            }

            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }

        return z;
    }

    public static int[] zFunctionWithMismatch(String s) {
        int n = s.length();
        int[] z = new int[n];
        int[] e = new int[n];

        Arrays.fill(e, -1);

        int l = 0;
        int r = 0;

        for (int i = 1; i < n; i++) {
            if (i >= l && i <= r) {
                z[i] = z[i - l];

                if (z[i] >= r - i + 1) {
                    int[] pointsToCheck;

                    if (e[l] < i - l || e[i - l] == e[l] - i + l) {
                        pointsToCheck = new int[1];
                        pointsToCheck[0] = e[i - l];
                    } else {
                        pointsToCheck = new int[2];
                        if (e[i - l] <= e[l] - i + l) {
                            pointsToCheck[0] = e[i - l];
                            pointsToCheck[1] = e[l] - i + l;
                        } else {
                            pointsToCheck[0] = e[l] - i + l;
                            pointsToCheck[1] = e[i - l];
                        }
                    }


                    z[i] = r - i;
                    for (; i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i]); z[i]++);
                }
            } else {
                for (; i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i]); z[i]++);
                if (i + z[i] < n) {
                    e[i] = z[i];
                    z[i]++;
                    for (; i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i]); z[i]++);
                }
            }

            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }

        return z;
    }

    public static void main(String[] args) {
        String s = "aababbababaabbababbababababababababababababaababaaaaaaaaaabbbbb";
        int[] z = zFunction(s);
    }
}
