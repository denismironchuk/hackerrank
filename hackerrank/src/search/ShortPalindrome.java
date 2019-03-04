package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShortPalindrome {
    private static final int ALPHABET_SIZE = 27;
    private static int MOD = 1000000007;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        int[] len1 = new int[ALPHABET_SIZE];
        int[][] len2 = new int[ALPHABET_SIZE][ALPHABET_SIZE];
        int[][] len3 = new int[ALPHABET_SIZE][ALPHABET_SIZE];
        int[][] len4 = new int[ALPHABET_SIZE][ALPHABET_SIZE];

        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i) - 'a';
            for (int j = 0; j < ALPHABET_SIZE; j++) {
                len4[c][j] = (len4[c][j] + len3[c][j]) % MOD;
            }

            for (int j = 0; j < ALPHABET_SIZE; j++) {
                len3[j][c] = (len3[j][c] + len2[j][c]) % MOD;
            }

            for (int j = 0; j < ALPHABET_SIZE; j++) {
                len2[j][c] = (len2[j][c] + len1[j]) % MOD;
            }

            len1[c]++;
        }

        int res = 0;
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            for (int j = 0; j < ALPHABET_SIZE; j++) {
                res = (res + len4[i][j]) % MOD;
            }
        }

        System.out.println(res);
    }
}
