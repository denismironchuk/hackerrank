package dynamicProgramming.palindromicSubstrings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CommonSubsequenceCountSimple {
    private static final int LETTERS_CNT = 5;
    private static final int LEN = 20;

    public static void main(String[] args) {
        while (true) {
            StringBuilder b1 = new StringBuilder();
            StringBuilder b2 = new StringBuilder();

            for (int i = 0; i < LEN; i++) {
                b1.append((char) ('a' + (int) (LETTERS_CNT * Math.random())));
                b2.append((char) ('a' + (int) (LETTERS_CNT * Math.random())));
            }

            String s1 = b1.toString();
            String s2 = b2.toString();

            System.out.println(s1);
            System.out.println(s2);

            long[][] dyn = new long[LEN][LEN];
            dyn[0][0] = s1.charAt(0) == s2.charAt(0) ? 1 : 0;

            for (int i = 1; i < LEN; i++) {
                if (s1.charAt(0) == s2.charAt(i)) {
                    dyn[0][i] = dyn[0][i - 1] + 1;
                } else {
                    dyn[0][i] = dyn[0][i - 1];
                }
            }

            for (int i = 1; i < LEN; i++) {
                if (s1.charAt(i) == s2.charAt(0)) {
                    dyn[i][0] = dyn[i - 1][0] + 1;
                } else {
                    dyn[i][0] = dyn[i - 1][0];
                }
            }

            //simple case common subsequences count
            for (int i = 1; i < LEN; i++) {
                for (int j = 1; j < LEN; j++) {
                    if (s1.charAt(i) == s2.charAt(j)) {
                        dyn[i][j] = dyn[i][j - 1] + dyn[i - 1][j] + 1;
                    } else {
                        dyn[i][j] = dyn[i][j - 1] + dyn[i - 1][j] - dyn[i - 1][j - 1];
                    }
                }
            }

            System.out.println(dyn[LEN - 1][LEN - 1]);

            List<String> subs1 = generateSubsequences(s1);
            Map<String, Long> subs1Cnt = new HashMap<>();
            for (String sub1 : subs1) {
                subs1Cnt.merge(sub1, 1l, (oldVal, newVal) -> oldVal + newVal);
            }

            List<String> subs2 = generateSubsequences(s2);

            int cnt = 0;

            for (String sub : subs2) {
                if (subs1Cnt.containsKey(sub)) {
                    cnt += subs1Cnt.get(sub);
                }
            }

            System.out.println(cnt);

            if (dyn[LEN - 1][LEN - 1] != cnt) {
                throw new RuntimeException();
            }
        }
    }

    private static List<String> generateSubsequences(String s) {
        List<String> subs = new ArrayList<>();
        for (long i = 1; i < pow2(s.length()); i++) {
            StringBuilder sub = new StringBuilder();
            int pos = 0;
            long i_ = i;
            while (i_ != 0) {
                if (i_ % 2 == 1) {
                    sub.append(s.charAt(pos));
                }
                pos++;
                i_ /= 2;
            }
            subs.add(sub.toString());
        }
        return subs;
    }

    private static long pow2(int pow) {
        long res = 1;
        for (int i = 0; i < pow; i++) {
            res *= 2;
        }
        return res;
    }
}
