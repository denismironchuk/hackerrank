import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class LCSReturns {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String a = br.readLine();
        String b = br.readLine();

        int[][] lcsToLeft = new int[a.length()][b.length()];
        lcsToLeft[0][0] = a.charAt(0) == b.charAt(0) ? 1 : 0;
        long max = lcsToLeft[0][0];
        for (int i = 1; i < a.length(); i++) {
            if (a.charAt(i) == b.charAt(0)) {
                lcsToLeft[i][0] = 1;
                max = 1;
            } else {
                lcsToLeft[i][0] = lcsToLeft[i - 1][0];
            }
        }

        for (int i = 1; i < b.length(); i++) {
            if (b.charAt(i) == a.charAt(0)) {
                lcsToLeft[0][i] = 1;
                max = 1;
            } else {
                lcsToLeft[0][i] = lcsToLeft[0][i - 1];
            }
        }

        for (int i = 1; i < a.length(); i++) {
            for (int j = 1; j < b.length(); j++) {
                if (a.charAt(i) == b.charAt(j)) {
                    lcsToLeft[i][j] = lcsToLeft[i - 1][j - 1] + 1;
                } else {
                    lcsToLeft[i][j] = Math.max(lcsToLeft[i - 1][j], lcsToLeft[i][j - 1]);
                }

                if (lcsToLeft[i][j] > max) {
                    max = lcsToLeft[i][j];
                }
            }
        }

        //*********************
        int[][] lcsToRight = new int[a.length()][b.length()];
        lcsToRight[a.length() - 1][b.length() - 1] = a.charAt(a.length() - 1) == b.charAt(b.length() - 1) ? 1 : 0;
        for (int i = a.length() - 2; i >= 0; i--) {
            if (a.charAt(i) == b.charAt(b.length() - 1)) {
                lcsToRight[i][b.length() - 1] = 1;
            } else {
                lcsToRight[i][b.length() - 1] = lcsToRight[i + 1][b.length() - 1];
            }
        }

        for (int i = b.length() - 2; i >= 0; i--) {
            if (b.charAt(i) == a.charAt(a.length() - 1)) {
                lcsToRight[a.length() - 1][i] = 1;
            } else {
                lcsToRight[a.length() - 1][i] = lcsToRight[a.length() - 1][i + 1];
            }
        }

        for (int i = a.length() - 2; i >= 0; i--) {
            for (int j = b.length() - 2; j >= 0; j--) {
                if (a.charAt(i) == b.charAt(j)) {
                    lcsToRight[i][j] = lcsToRight[i + 1][j + 1] + 1;
                } else {
                    lcsToRight[i][j] = Math.max(lcsToRight[i + 1][j], lcsToRight[i][j + 1]);
                }
            }
        }

        //**********************


        Set<Character>[] insertChars = new Set[a.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            insertChars[i] = new HashSet<>();
        }

        for (int i = 0; i < b.length(); i++) {
            for (int j = 0; j <= a.length(); j++) {
                long testVal = 0;
                if (j > 0 && i > 0) {
                    testVal += lcsToLeft[j - 1][i - 1];
                }
                if (i < b.length() - 1 && j < a.length()) {
                    testVal += lcsToRight[j][i + 1];
                }
                if (testVal == max) {
                    insertChars[j].add(b.charAt(i));
                }
            }
        }

        long result = 0;
        for (Set<Character> chars : insertChars) {
            result += chars.size();
        }
        System.out.println(result);
    }
}
