import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Denis_Mironchuk on 7/11/2018.
 */
public class FindStrings {
    private static final int ALPHABET_SIZE = 27;
    private static final char SEPARATOR = 'a' - 1;

    private static int[] buildSuffixArray(String s) {
        int strLen = s.length();

        int[] suffixArr = new int[strLen];
        int[] priorities = new int[strLen];

        //initial count sort
        int[] charCnt = new int[ALPHABET_SIZE];
        for (int i = 0; i < strLen; i++) {
            charCnt[s.charAt(i) - 'a' + 1]++;
        }

        int[] incrCnt = new int[ALPHABET_SIZE];
        incrCnt[0] = charCnt[0];
        for (int i = 1; i < ALPHABET_SIZE; i++) {
            incrCnt[i] = incrCnt[i - 1] + charCnt[i];
        }

        for (int i = strLen - 1; i >= 0; i--) {
            suffixArr[incrCnt[s.charAt(i) - 'a' + 1] - 1] = i;
            incrCnt[s.charAt(i) - 'a' + 1]--;
        }

        int prior = 0;
        priorities[suffixArr[0]] = prior;

        for (int i = 1; i < strLen; i++) {
            if (s.charAt(suffixArr[i]) != s.charAt(suffixArr[i - 1])) {
                prior++;
            }
            priorities[suffixArr[i]] = prior;
        }
        //*********************
        int len = 1;

        int[] newSuffixArr = new int[strLen];
        int[] prioritiesNew = new int[strLen];
        int[] priorsCnt = new int[strLen];
        int[] priorsIncr = new int[strLen];

        while (len < strLen) {

            for (int i = 0; i < strLen; i++) {
                newSuffixArr[i] = (suffixArr[i] - len + strLen) % strLen;
            }

            Arrays.fill(priorsCnt, 0);
            for (int i = 0; i < strLen; i++) {
                priorsCnt[priorities[i]]++;
            }

            priorsIncr[0] = priorsCnt[0];
            for (int i = 1; i < strLen; i++) {
                priorsIncr[i] = priorsIncr[i - 1] + priorsCnt[i];
            }

            for (int i = strLen - 1; i >= 0; i--) {
                suffixArr[priorsIncr[priorities[newSuffixArr[i]]] - 1] = newSuffixArr[i];
                priorsIncr[priorities[newSuffixArr[i]]]--;
            }

            prior = 0;

            for (int i = 1; i < strLen; i++) {
                int middle = (suffixArr[i] + len) % strLen;
                int middlePrev = (suffixArr[i - 1] + len) % strLen;

                if (priorities[suffixArr[i]] != priorities[suffixArr[i - 1]] || priorities[middle] != priorities[middlePrev]) {
                    prior++;
                }

                prioritiesNew[suffixArr[i]] = prior;
            }

            for (int i = 0; i < strLen; i++) {
                priorities[i] = prioritiesNew[i];
            }

            len *= 2;
        }

        return suffixArr;
    }

    public static int[] buildLCPArray(String s, int[] suffixArray) {
        int strLen = s.length();
        int[] lcpArr = new int[strLen - 1];
        int[] invSuffArr = new int[strLen];

        for (int i = 0; i < strLen; i++) {
            invSuffArr[suffixArray[i]] = i;
        }

        for (int i = 0; i < suffixArray.length - 1; i++) {
            int commonPrefix = lcpArr[i];
            while(suffixArray[i] + commonPrefix < strLen && suffixArray[i + 1] + commonPrefix < strLen
                    && s.charAt(suffixArray[i] + commonPrefix) == s.charAt(suffixArray[i + 1] + commonPrefix)) {
                commonPrefix++;
            }

            lcpArr[i] = commonPrefix;
            int cmnBac = 1;

            while (commonPrefix > 1) {
                int currentLcp = lcpArr[invSuffArr[suffixArray[i] + commonPrefix - 1]];
                lcpArr[invSuffArr[suffixArray[i] + commonPrefix - 1]] = Math.max(currentLcp, cmnBac);
                //lcpArr[invSuffArr[s.charAt(suffixArray[i] + commonPrefix)]] = cmnBac;
                cmnBac++;
                commonPrefix--;
            }
        }

        return lcpArr;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader br = new BufferedReader(new FileReader("D:\\findStrings2.txt"));
        //BufferedWriter wr = new BufferedWriter(new FileWriter("D:\\findStrings2out.txt"));
        int n = Integer.parseInt(br.readLine());
        StringBuilder allStrings = new StringBuilder();
        String[] w = new String[n];

        for (int i = 0; i < n; i++) {
            w[i] = br.readLine();
            allStrings.append(w[i]).append(SEPARATOR);
        }

        String s = allStrings.toString();
        int stringIndex = 0;
        int[] positionFromEnd = new int[s.length()];
        for (String word : w) {
            for (int i = 0; i < word.length(); i++) {
                positionFromEnd[stringIndex] = word.length() - i;
                stringIndex++;
            }
            stringIndex++;
        }

        int[] suffixArr = buildSuffixArray(s);
        int[] lcp = buildLCPArray(s, suffixArr);

        /*int uniqueWordsCnt = 0;
        Set<String> cntrlWordsCnt = new HashSet<>();

        int pos = 0;

        for (int i = n; i < s.length(); i++) {
            for (int j = lcp[i - 1] + 1; j <= positionFromEnd[suffixArr[i]]; j++) {
                uniqueWordsCnt++;
                String sbstr = s.substring(suffixArr[i], suffixArr[i] + j);
                System.out.printf("%d - %s\n", uniqueWordsCnt, sbstr);
                wr.write(String.format("%d - %s\n", uniqueWordsCnt, sbstr));
                wr.newLine();
                cntrlWordsCnt.add(sbstr);
            }
            pos += positionFromEnd[suffixArr[i]] - Math.min(lcp[i-1], positionFromEnd[suffixArr[i]]);
            System.out.println(pos + " suffix - " + s.substring(suffixArr[i]));
            wr.write(pos + " suffix - " + s.substring(suffixArr[i]));
            wr.newLine();
        }

        wr.flush();
        wr.close();
        System.out.println("===============");

        System.out.println(cntrlWordsCnt.size() + " = " + uniqueWordsCnt);*/

        int Q = Integer.parseInt(br.readLine());
        for (int q = 0; q < Q; q++) {
            int k = Integer.parseInt(br.readLine());

            int currentSuffixPos = positionFromEnd[suffixArr[n]];

            if (currentSuffixPos >= k) {
                System.out.println(s.substring(suffixArr[n], suffixArr[n] + k));
                continue;
            }
            int prevSuffixPos = currentSuffixPos;

            boolean resultFound = false;
            for (int i = n + 1; !resultFound && i < s.length(); i++) {
                int lcpLoc = Math.min(lcp[i-1], positionFromEnd[suffixArr[i]]);

                currentSuffixPos += positionFromEnd[suffixArr[i]] - lcpLoc;
                if (currentSuffixPos >= k) {
                    System.out.println(s.substring(suffixArr[i], suffixArr[i] + lcpLoc + k - prevSuffixPos));
                    resultFound = true;
                }
                prevSuffixPos = currentSuffixPos;
            }
            if (!resultFound) {
                System.out.println("INVALID");
            }
        }

    }
}
