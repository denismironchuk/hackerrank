package dynamicProgramming.stringReduction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StringReductionTest {
    public static void main(String[] args) {
        long upperLim = 9;
        long maxLen = 10;
        Map<String, Integer> minReductSize = new HashMap<>();
        minReductSize.put("a", 1);
        minReductSize.put("b", 1);
        minReductSize.put("c", 1);

        Map<String, Integer> stats = new HashMap<>();
        stats.put("1", 1);

        Map<String, Integer> statsCnt = new HashMap<>();
        stats.put("1", 3);

        for (int len = 2; len < maxLen; len++) {
            System.out.println("Len = " + len);
            for (int j = 0; j < upperLim; j++) {
                if (j % 1000000 == 0) {
                    System.out.println(j);
                }
                int j_ = j;
                StringBuilder s = new StringBuilder();
                for (int k = 0; k < len; k++) {
                    s.append((char)('a' + (j_ % 3)));
                    j_ = j_ / 3;
                }
                String resStr = s.toString();
                //System.out.println(resStr);
                int minSize = processReductions(resStr, minReductSize);
                minReductSize.put(resStr, minSize);

                String stat = convertToStat(resStr);
                if (!stats.containsKey(stat)) {
                    stats.put(stat, minSize);
                    statsCnt.put(stat, 1);
                } else {
                    if (stats.get(stat) != minSize) {
                        throw new RuntimeException("I was wrong :(((");
                    }
                    statsCnt.merge(stat, 1, (oldVal, newVal) -> oldVal + 1);
                }
            }
            upperLim *= 3;
        }

        System.out.println();
    }

    private static String convertToStat(String s) {
        int[] stat = new int[3];

        for (char c : s.toCharArray()) {
            stat[c - 'a']++;
        }

        Arrays.sort(stat);
        return Arrays.stream(stat).mapToObj(String::valueOf).collect(Collectors.joining("|"));
    }

    private static int processReductions(String s, Map<String, Integer> minReductSize) {
        int minSize = s.length();
        for (int i = 0; i < s.length() - 1; i++) {
            char c1 = s.charAt(i);
            char c2 = s.charAt(i + 1);
            if (c1 != c2) {
                String newStr = new StringBuilder().append(s, 0, i).append(mergeChars(c1, c2)).append(s.substring(i + 2)).toString();
                if (minReductSize.get(newStr) < minSize) {
                    minSize = minReductSize.get(newStr);
                }
            }
        }
        return minSize;
    }

    private static char mergeChars(char c1, char c2) {
        if (c1 == 'a') {
             if (c2 == 'b') {
                 return 'c';
             } else {
                 return 'b';
             }
        } else if (c1 == 'b') {
            if (c2 == 'a') {
                return 'c';
            } else {
                return 'a';
            }
        } else {
            if (c2 == 'b') {
                return 'a';
            } else {
                return 'b';
            }
        }
    }
}
