package codejam.year2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class AlienRhyme {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t <= T; t++) {
            int n = Integer.parseInt(br.readLine());
            List<String> w = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                w.add(reverseString(br.readLine()));
            }

            w.sort(String::compareTo);

            int res = 0;
            Set<String> processedPrefs = new HashSet<>();

            while (w.size() > 1) {
                List<Integer> comPrefs = new ArrayList<>();

                for (int i = 0; i < w.size() - 1; i++) {
                    comPrefs.add(comPrefLen(w.get(i), w.get(i + 1), processedPrefs));
                }

                int maxComPrefIndex = 0;

                for (int i = 0; i < comPrefs.size(); i++) {
                    if (comPrefs.get(i) > comPrefs.get(maxComPrefIndex)) {
                        maxComPrefIndex = i;
                    }
                }

                if (comPrefs.get(maxComPrefIndex) == 0) {
                    break;
                }

                processedPrefs.add(w.get(maxComPrefIndex).substring(0, comPrefs.get(maxComPrefIndex)));
                res += 2;

                w.remove(maxComPrefIndex);
                w.remove(maxComPrefIndex);
            }

            System.out.printf("Case #%s: %s\n", t, res);
        }
    }

    private static int comPrefLen(String str1, String str2, Set<String> processedPrefs) {
        int res = 0;
        String prefix = "";

        for (int i = 0; i < Math.min(str1.length(), str2.length()); i++) {
            if (str1.charAt(i) == str2.charAt(i)) {
                prefix += str1.charAt(i);
                if (processedPrefs.contains(prefix)) {
                    break;
                } else {
                    res++;
                }
            } else {
                break;
            }
        }

        return res;
    }

    private static String reverseString(String s) {
        StringBuilder strBuild = new StringBuilder();
        char[] chars = s.toCharArray();

        for (int i = chars.length - 1; i >= 0; i--) {
            strBuild.append(chars[i]);
        }

        return strBuild.toString();
    }
}
