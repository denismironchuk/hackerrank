package dynamicProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BeautifulStrings {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String s = br.readLine();
            System.out.println(countOptimal(s));
        }
    }

    private static long countOptimal(String s) {
        long[] oneLetter = new long[s.length() + 1];
        long[] twoLetters = new long[s.length() + 1];
        oneLetter[2] = s.charAt(0) == s.charAt(1) ? 1 : 2;
        twoLetters[2] = 1;
        for (int i = 3; i <= s.length(); i++) {
            int posInString = i - 1;
            twoLetters[i] = twoLetters[i - 1];
            oneLetter[i] = oneLetter[i - 1];
            if (s.charAt(posInString) != s.charAt(posInString - 1)) {
                if (s.charAt(posInString) == s.charAt(posInString - 2)) {
                    twoLetters[i] += oneLetter[i - 1] - 1;
                } else {
                    twoLetters[i] += oneLetter[i - 1];
                }
                oneLetter[i] += 1;
            } else {
                if (s.charAt(posInString) != s.charAt(posInString - 2)) {
                    twoLetters[i] += 1;
                }
            }
        }
        return twoLetters[s.length()];
    }
}
