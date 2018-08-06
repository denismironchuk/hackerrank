import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MorganAndString2 {
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

    public static void main(String[] agrs) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder res = new StringBuilder();

        int T = Integer.parseInt(br.readLine());
        for (int t = 0; t < T; t++) {

            String a = br.readLine() + SEPARATOR;
            String b = br.readLine() + SEPARATOR;

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

            res.append("\n");
        }

        System.out.println(res.toString());
    }
}
