package codejam.zillionim;

import java.util.Set;
import java.util.TreeSet;

public class Zillionim {
    public static void main(String[] args) {
        int C_MAX = 50; //coins to take
        int N_MAX = 5000; //initial coins amount

        for (int c = 1; c <= C_MAX; c++) {
            System.out.printf("%3d: ", c);
            int[] grand = new int[N_MAX + 1];
            for (int n = 0; n <= N_MAX; n++) {
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
            for (int i = 1; i <= N_MAX; i++) {
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
