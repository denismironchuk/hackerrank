import java.io.*;
import java.util.*;

public class SaveHumanity {
    private static int[] buildPrefixFunction(String s) {
        int n = s.length();
        int[] prefix = new int[n];

        for (int i = 1; i < n; i++) {
            int parentPrefix = prefix[i-1];

            while (0 != parentPrefix && s.charAt(i) != s.charAt(parentPrefix)) {
                parentPrefix = prefix[parentPrefix - 1];
            }

            if (0 == parentPrefix) {
                if (s.charAt(i) == s.charAt(parentPrefix)) {
                    prefix[i] = 1;
                }
            } else {
                prefix[i] = parentPrefix + 1;
            }
        }

        return prefix;
    }

    private static int[] prefixTrivial(String s) {
        int n = s.length();
        int[] prefix = new int[n];

        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= i; j++) {
                int k = j;
                for (; k <= i && s.charAt(k) == s.charAt(k - j); k++);
                if (k - 1 == i) {
                    prefix[i] = k - j;
                    break;
                }
            }
        }

        return prefix;
    }

    private static List<Integer> countTrivial(String p, String v) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < p.length(); i++) {
            boolean diff = false;
            boolean full = true;
            for (int j = 0; j < v.length(); j++) {
                if (p.charAt(i) != v.charAt(j)) {
                    if (!diff) {
                        diff = true;
                    } else {
                        full = false;
                        break;
                    }
                }
            }

            if (full) {
                result.add(i);
            }
        }

        return result;
    }

    public static void main(String[] args) throws IOException {
        /*while (true) {
            StringBuilder builder = new StringBuilder();
            int n = 1000;

            for (int i = 0; i < n; i++) {
                builder.append((char) ('A' + (int) (Math.random() * 2)));
            }

            String s = builder.toString();
            int[] prefix1 = buildPrefixFunction(s);
            int[] prefix2 = prefixTrivial(s);
            for (int i = 0; i < n; i++) {
                if (prefix1[i] != prefix2[i]) {
                    throw new RuntimeException();
                }
            }
        }*/

        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new FileReader("D://test.txt"));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            String inputData = br.readLine();
            StringTokenizer tkn = new StringTokenizer(inputData, " ");
            String patientDNA = tkn.nextToken();
            /*int[] stat1 = new int[26];
            Map<Character, List<Integer>> positions1 = new HashMap<>();
            int[] stat2 = new int[26];
            Map<Character, List<Integer>> positions2 = new HashMap<>();

            for (int i = 0; i < patientDNA.length(); i++) {
                stat1[patientDNA.charAt(i) - 'a']++;
                positions1.computeIfAbsent(patientDNA.charAt(i), (a) -> new ArrayList<>()).add(i);
            }

            for (int i = 0; i < 26; i++) {
                System.out.print(stat1[i] + " ");
            }

            System.out.println();*/
            String virusDNA = tkn.nextToken();
            /*for (int i = 0; i < virusDNA.length(); i++) {
                stat2[virusDNA.charAt(i) - 'a']++;
                positions2.computeIfAbsent(virusDNA.charAt(i), (a) -> new ArrayList<>()).add(i);
            }
            for (int i = 0; i < 26; i++) {
                System.out.print(stat2[i] + " ");
            }
            System.out.println();

            List<Integer> trivial = countTrivial(patientDNA, virusDNA);*/
            List<Integer> kmp = new ArrayList<>();

            int[] virusPrefix = buildPrefixFunction(virusDNA);
            int virusIndex = 0;
            int matches = 0;
            int indexDiff = -1;

            for (int i = 0; i < patientDNA.length(); i++) {
                if (patientDNA.charAt(i) == virusDNA.charAt(virusIndex) || indexDiff == -1) {
                    if (patientDNA.charAt(i) != virusDNA.charAt(virusIndex)) {
                        indexDiff = i;
                    }

                    virusIndex++;

                    if (virusIndex == virusDNA.length()) {
                        matches++;
                        int startIndex = (i - virusDNA.length() + 1);
                        //System.out.print(startIndex + " ");
                        kmp.add(startIndex);
                        virusIndex = virusPrefix[virusIndex - 1];

                        if (indexDiff != -1) {
                            if (i - virusIndex + 1 > indexDiff
                                    || patientDNA.charAt(indexDiff) == virusDNA.charAt(virusIndex - (i - indexDiff))) {
                                indexDiff = -1;
                            }
                        }

                        if (0 == virusIndex) {
                            i--;
                        }
                    }
                } else {
                    if (0 != virusIndex) {
                        virusIndex = virusPrefix[virusIndex - 1];
                        if (indexDiff != -1) {
                            if (i - virusIndex + 1 > indexDiff
                                    || patientDNA.charAt(indexDiff) == virusDNA.charAt(virusIndex - (i - indexDiff))) {
                                indexDiff = -1;
                            }
                        }
                        i--;
                    }
                }
            }

            if (0 == matches) {
                System.out.println("No Match!");
            }else {
                System.out.println();
            }
        }
    }
}