package codejam.year2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class WholeNewWord {
    private static char[][] words;
    private static Set<String> wordsSet;
    private static Set<Character>[] chars;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(tkn1.nextToken());
            int l = Integer.parseInt(tkn1.nextToken());
            words = new char[n][l];
            wordsSet = new HashSet<>();
            for (int i = 0; i < n; i++) {
                String word = br.readLine();
                words[i] = word.toCharArray();
                wordsSet.add(word);
            }

            chars = new Set[l];
            for (int i = 0; i < l; i++) {
                chars[i] = new HashSet<>();
                for (int j = 0; j < n; j++) {
                    chars[i].add(words[j][i]);
                }
            }

            String res = generateNewWord(0, "");
            System.out.printf("Case #%s: %s\n", t + 1, null == res ? "-" : res);
        }
    }

    public static String generateNewWord(int letterNum, String str) {
        if (letterNum == chars.length) {
            if (!wordsSet.contains(str)) {
                return str;
            } else {
                return null;
            }
        }

        for (char c : chars[letterNum]) {
            String word = generateNewWord(letterNum+1, str + c);
            if (word != null) {
                return word;
            }
        }

        return null;
    }
}
