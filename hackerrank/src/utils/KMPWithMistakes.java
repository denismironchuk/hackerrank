package utils;

/**
 * Created by Denis_Mironchuk on 3/27/2018.
 */
public class KMPWithMistakes {
    private static void prefixWithMistakes(String s, int[] prefix, int[] mistakesPositions) {
        int n = s.length();

        for (int i = 0; i < n; i++) {
            mistakesPositions[i] = -1;
        }

        for (int i = 1; i < n; i++) {
            int parentPrefixLen = prefix[i-1];
            int prevPrefix = i - 1;

            while (0 != parentPrefixLen && (s.charAt(i) != s.charAt(parentPrefixLen) && mistakesPositions[prevPrefix] != -1)) {
                prevPrefix = parentPrefixLen - 1;
                parentPrefixLen = prefix[parentPrefixLen - 1];
            }

            if (0 == parentPrefixLen) {
                if (s.charAt(i) != s.charAt(parentPrefixLen)) {
                    mistakesPositions[i] = parentPrefixLen;
                }

                parentPrefixLen++;
            } else {
                if (s.charAt(i) != s.charAt(parentPrefixLen) && mistakesPositions[prevPrefix] == -1) {
                    mistakesPositions[i] = parentPrefixLen;
                    parentPrefixLen++;
                } else if (s.charAt(i) == s.charAt(parentPrefixLen)) {
                    mistakesPositions[i] = mistakesPositions[prevPrefix];
                    parentPrefixLen++;
                }
            }

            prefix[i] = parentPrefixLen;
        }
    }

    public static void main(String[] args) {
        StringBuilder builder = new StringBuilder();
        int n = 10;

        for (int i = 0; i < n; i++) {
            builder.append((char) ('A' + (int) (Math.random() * 2)));
        }

        String s = "BABBAABBAA";//builder.toString();

        int[] prefix = new int[s.length()];
        int[] mistakesPositions = new int[s.length()];

        prefixWithMistakes(s, prefix, mistakesPositions);

        System.out.println();
    }
}
