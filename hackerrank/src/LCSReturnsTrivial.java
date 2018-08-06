import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LCSReturnsTrivial {
    private static int lcs(String a, String b) {
        int[][] lcsToLeft = new int[a.length()][b.length()];
        int maxLen = 0;

        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == b.charAt(0)) {
                lcsToLeft[i][0] = 1;
                maxLen = 1;
            }
        }

        for (int j = 0; j < b.length(); j++) {
            if (b.charAt(j) == a.charAt(0)) {
                lcsToLeft[0][j] = 1;
                maxLen = 1;
            }
        }

        for (int i = 1; i < a.length(); i++) {
            for (int j = 1; j < b.length(); j++) {
                if (a.charAt(i) == b.charAt(j)) {
                    lcsToLeft[i][j] = lcsToLeft[i - 1][j - 1] + 1;
                    if (lcsToLeft[i][j] > maxLen) {
                        maxLen = lcsToLeft[i][j];
                    }
                }
            }
        }

        return maxLen;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String a = br.readLine();
        String b = br.readLine();

        int lcsLen = lcs(a, b);

        for (char c = 'a'; c <= 'z'; c++) {
            String aNew = c + a;
            int lcsLenNew = lcs(aNew, b);
            if (lcsLenNew == lcsLen + 1) {
                System.out.println("0 " + c);
            }
        }

        for (int i = 1; i < a.length(); i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                String aNew = a.substring(0, i) + c + a.substring(i, a.length());
                int lcsLenNew = lcs(aNew, b);
                if (lcsLenNew == lcsLen + 1) {
                    System.out.println(i + " " + c);
                }
            }
        }

        for (char c = 'a'; c <= 'z'; c++) {
            String aNew = a + c;
            int lcsLenNew = lcs(aNew, b);
            if (lcsLenNew == lcsLen + 1) {
                System.out.println("last " + c);
            }
        }
    }
}
