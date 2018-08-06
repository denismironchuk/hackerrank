package utils;

public class SuffixArray {
    public static final int ALPHBET_SIZE_NO_SEPAR = 2;
    public static final int ALPHBET_SIZE = ALPHBET_SIZE_NO_SEPAR + 1;
    public static final char SEPARATOR = 'A' + ALPHBET_SIZE_NO_SEPAR;

    public static void main(String[] args) {
        int n1 = 10;
        int n = n1 * 2 + 2;

        StringBuilder builder1 = new StringBuilder();

        for (int i = 0; i < n1; i++) {
            builder1.append((char)('A' + (int)(Math.random() * ALPHBET_SIZE_NO_SEPAR - 1)));
        }

        String s1 = builder1.toString();

        StringBuilder builder2 = new StringBuilder();

        for (int i = 0; i < n1; i++) {
            builder2.append((char)('A' + (int)(Math.random() * ALPHBET_SIZE_NO_SEPAR - 1)));
        }

        String s2 = builder2.toString();

        String s = s1 + SEPARATOR + s2 + SEPARATOR;

        System.out.println(s);
        System.out.println("==============");

        int[] p = new int[n];
        int[] cnt = new int[ALPHBET_SIZE];

        for (int i = 0; i < s.length(); i++) {
            cnt[s.charAt(i) - 'A']++;
        }

        for (int i = 1; i < ALPHBET_SIZE; i++) {
            cnt[i] += cnt[i-1];
        }

        for (int i = 0; i < n; i++) {
            p[cnt[s.charAt(i) - 'A'] - 1] = i;
            cnt[s.charAt(i) - 'A']--;
        }

        int[] c = new int[n];
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

            c[p[0]] = 0;
            classes = 1;

            for (int i = 1; i< n; i++) {
                int mid0 = (p[i-1] + len) % n;
                int mid1 = (p[i] + len) % n;
                if (c[p[i]] != c[p[i-1]] || c[mid0] != c[mid1]) {
                    classes++;
                }
                c[p[i]] = classes-1;
            }

            len *= 2;
        }

        for (int i = 0; i < n; i++) {
            System.out.printf("%2d - %s\n", c[p[i]], s.substring(p[i]));
        }
    }
}
