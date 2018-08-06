/**
 * Created by Denis_Mironchuk on 3/23/2018.
 */
public class MorganAndStringTest {
    public static final int ALPHBET_SIZE_NO_SEPAR = 26;
    public static final int ALPHBET_SIZE = ALPHBET_SIZE_NO_SEPAR + 1;
    public static final char SEPARATOR = 'A' + ALPHBET_SIZE_NO_SEPAR;

    public static void buildSuffixArray(String s, int[] p, int[] c, int n) {
        int[] cnt = new int[ALPHBET_SIZE];

        for (int i = 0; i < n; i++) {
            cnt[s.charAt(i) - 'A']++;
        }

        for (int i = 1; i < ALPHBET_SIZE; i++) {
            cnt[i] += cnt[i-1];
        }

        for (int i = 0; i < n; i++) {
            p[cnt[s.charAt(i) - 'A'] - 1] = i;
            cnt[s.charAt(i) - 'A']--;
        }

        c[p[0]] = 0;
        int classes = 1;

        for (int i = 1; i < n; i++) {
            if (s.charAt(p[i]) != s.charAt(p[i-1])) {
                classes++;
            }
            c[p[i]] = classes - 1;
        }

        int len = 1;

        int[] pn = new int[n];
        int[] cn = new int[n];
        while(len < n) {
            for (int i = 0; i < n; i++) {
                pn[i] = p[i] - len;

                if (pn[i] < 0) {
                    pn[i] += n;
                }
            }

            int[] classCnt = new int[n];

            for (int i = 0; i < n; i++) {
                classCnt[c[pn[i]]]++;
            }

            for (int i = 1; i < n; i++) {
                classCnt[i] += classCnt[i-1];
            }

            for (int i = n - 1; i > -1; i--) {
                p[classCnt[c[pn[i]]] - 1] = pn[i];
                classCnt[c[pn[i]]]--;
            }

            cn[p[0]] = 0;
            classes = 1;

            for (int i = 1; i< n; i++) {
                int mid0 = (p[i-1] + len) % n;
                int mid1 = (p[i] + len) % n;
                if (c[p[i]] != c[p[i-1]] || c[mid0] != c[mid1]) {
                    classes++;
                }
                cn[p[i]] = classes-1;
            }

            for (int i = 0; i < n; i++) {
                c[i] = cn[i];
            }

            len *= 2;
        }
    }

    private static String countSlowButCorrect(String a, String b) {
        a += 'z';
        b += 'z';

        StringBuilder res = new StringBuilder();

        int ai = 0;
        int bi = 0;

        while (ai < a.length() && bi < b.length()) {
            int i = 0;
            for (; i + ai < a.length() && i + bi < b.length() && a.charAt(i + ai) == b.charAt(i + bi); i++);

            if (i + ai == a.length() || a.charAt(i + ai) < b.charAt(i + bi)) {
                res.append(a.charAt(ai));
                ai++;
            } else {
                res.append(b.charAt(bi));
                bi++;
            }
        }

        for (int i = ai; i < a.length(); i++) {
            res.append(a.charAt(i));
        }

        for (int i = bi; i < b.length(); i++) {
            res.append(b.charAt(i));
        }

        res.delete(res.length() - 2, res.length());

        return res.toString();
    }

    private static String countFastButWrong(String a, String b) {
        a += SEPARATOR;
        b += SEPARATOR;

        StringBuilder res = new StringBuilder();

        int aLen = a.length();
        int bLen = b.length();

        String s = a + b;
        int n = aLen + bLen;

        int[] p = new int[n];
        int[] c = new int[n];

        buildSuffixArray(s, p, c, n);

        int ai = 0;
        int bi = aLen;

        while (ai < aLen - 1 && bi < n - 1) {
            if (c[ai] < c[bi]) {
                res.append(s.charAt(ai));
                ai++;
            } else {
                res.append(s.charAt(bi));
                bi++;
            }
        }

        for (int i = ai; i < a.length() - 1; i++) {
            res.append(s.charAt(i));
        }

        for (int i = bi; i < n - 1; i++) {
            res.append(s.charAt(i));
        }

        return res.toString();
    }

    public static void main(String[] args) {
        boolean result = true;
        while(result) {
            int size1 = (int) (10 * Math.random());
            StringBuilder builder1 = new StringBuilder();

            for (int i = 0; i < size1; i++) {
                builder1.append((char) ('A' + (int) (Math.random() * ALPHBET_SIZE_NO_SEPAR)));
            }

            String a = builder1.toString();

            int size2 = (int) (10 * Math.random());
            StringBuilder builder2 = new StringBuilder();

            for (int i = 0; i < size2; i++) {
                builder2.append((char) ('A' + (int) (Math.random() * ALPHBET_SIZE_NO_SEPAR)));
            }

            String b = builder2.toString();

            String res1 = countSlowButCorrect(a, b);
            String res2 = countFastButWrong(a, b);

            result = res1.equals(res2);

            if (!result) {
                System.out.println(a);
                System.out.println(b);
                System.out.println();
                System.out.println(res1);
                System.out.println(res2);
            }
        }
    }
}
