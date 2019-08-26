package codejam.zillionim;

import java.util.Set;
import java.util.TreeSet;

public class Zillionim {
    public static void main(String[] args) {
        int C_MIN = 2; //coins to take
        int C_MAX = 100; //coins to take
        int N_MAX = 10000; //initial coins amount

        for (int c = C_MIN; c <= C_MAX; c++) {
            System.out.printf("%3d: ", c);
            int[] grand = new int[100 * c + 1];
            for (int n = 0; n <= 100 * c; n++) {
                if (n - c < 0) {
                    grand[n] = 0;
                } else {
                    Set<Integer> mexs = new TreeSet<>();
                    int lim = (n - c);
                    for (int i = 0; i <= lim; i++) {
                        mexs.add(grand[i] ^ grand[n - c - i]);
                    }
                    grand[n] = mex(mexs);
                }
            }
            int prev = grand[0];
            int cnt = 1;
            for (int i = 1; i < grand.length; i++) {
                if (grand[i] == prev) {
                    cnt++;
                } else {
                    System.out.printf("%2d (%2d); ", prev, cnt);
                    prev = grand[i];
                    cnt = 1;
                }
            }

            System.out.printf("%2d (%2d) ", prev, cnt);
            System.out.println();
        }
    }

    private static int mex(Set<Integer> mexs) {
        int res = 0;
        for (int m : mexs) {
            if (m == res) {
                res++;
            } else {
                break;
            }
        }
        return res;
    }
}
