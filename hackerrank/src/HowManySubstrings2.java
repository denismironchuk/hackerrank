import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 8/13/2018.
 */
public class HowManySubstrings2 {
    public static final int ALPHABET_SIZE = 2;

    private static int[] buildSuffixArray(String s) {
        int strLen = s.length();

        int[] suffixArr = new int[strLen];
        int[] priorities = new int[strLen];

        //initial count sort
        int[] charCnt = new int[ALPHABET_SIZE + 1];
        for (int i = 0; i < strLen; i++) {
            charCnt[s.charAt(i) - 'a' + 1]++;
        }

        int[] incrCnt = new int[ALPHABET_SIZE + 1];
        incrCnt[0] = charCnt[0];
        for (int i = 1; i < ALPHABET_SIZE + 1; i++) {
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
        int[] lcpArr = new int[strLen];
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

        lcpArr[strLen - 1] = -1;

        return lcpArr;
    }

    public static void main(String[] args) throws IOException {
        /*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(line1.nextToken());
        int q = Integer.parseInt(line1.nextToken());
        String s = br.readLine() + (char)('a' - 1);*/

        int strLen = 12;
        /*StringBuilder build = new StringBuilder();

        for (int i = 0; i < strLen - 1; i++) {
            build.append((char) ('a' + (char) (Math.random() * ALPHABET_SIZE)));
        }

        build.append((char) ('a' - 1));

        String s = build.toString();*/
        String s = "aabbbbbbaab" + (char) ('a' - 1);
        System.out.println(s);

        int[] suffixArr = buildSuffixArray(s);
        System.out.println("======================");

        int[] lcp = buildLCPArray(s, suffixArr);
        for (int i = 0; i < strLen; i++) {
            int comPref = i == strLen - 1 ? -1 : lcp[i];
            System.out.printf("%4d %s\n", comPref, s.substring(suffixArr[i]));
        }
        System.out.println();

        int[] lcp2 = new int[lcp.length];
        for (int i = 0; i < lcp.length; i++) {
            lcp2[i] = lcp[i];
        }

        int[] suffixSubstringsForward = new int[s.length()];
        int[] suffixSubstringsBackward = new int[s.length()];
        int[] nextSuffix = new int[s.length()];
        int[] prevSuffix = new int[s.length()];

        int[] nextSuffix2 = new int[s.length()];
        int[] prevSuffix2 = new int[s.length()];

        for (int i = 0; i < s.length() - 1; i++) {
            nextSuffix[i] = i + 1;
            nextSuffix2[i] = i + 1;
        }
        nextSuffix[s.length() - 1] = -1;
        nextSuffix2[s.length() - 1] = -1;

        for (int i = 1; i < s.length(); i++) {
            prevSuffix[i] = i - 1;
            prevSuffix2[i] = i - 1;
        }

        prevSuffix[0] = -1;
        prevSuffix2[0] = -1;

        int[] invSuffixArray = new int[s.length()];

        for (int i = 0; i < s.length(); i++) {
            invSuffixArray[suffixArr[i]] = i;
        }

        System.out.println();

        for (int i = 0; i < s.length() - 1; i++) {
            int suffixIndex = invSuffixArray[i];

            suffixSubstringsForward[i] = s.length() - 1 - i;

            if (nextSuffix[suffixIndex] == -1 && prevSuffix[suffixIndex] != -1) {
                suffixSubstringsForward[i] -= lcp[prevSuffix[suffixIndex]];
            } else if (nextSuffix[suffixIndex] != -1 && prevSuffix[suffixIndex] == -1) {
                suffixSubstringsForward[i] -= lcp[suffixIndex];
            } else if (nextSuffix[suffixIndex] != -1 && prevSuffix[suffixIndex] != -1) {
                suffixSubstringsForward[i] -= Math.max(lcp[suffixIndex], lcp[prevSuffix[suffixIndex]]);
            }

            if (prevSuffix[suffixIndex] != -1) {
                nextSuffix[prevSuffix[suffixIndex]] = nextSuffix[suffixIndex];
                lcp[prevSuffix[suffixIndex]] = Math.min(lcp[prevSuffix[suffixIndex]], lcp[suffixIndex]);
            }

            if (nextSuffix[suffixIndex] != -1) {
                prevSuffix[nextSuffix[suffixIndex]] = prevSuffix[suffixIndex];
            }
        }

        for (int i = s.length() - 2; i >= 0; i--) {
            int suffixIndex = invSuffixArray[i];

            suffixSubstringsBackward[i] = s.length() - 1 - i;

            if (nextSuffix2[suffixIndex] == -1 && prevSuffix2[suffixIndex] != -1) {
                suffixSubstringsBackward[i] -= lcp2[prevSuffix2[suffixIndex]];
            } else if (nextSuffix2[suffixIndex] != -1 && prevSuffix2[suffixIndex] == -1) {
                suffixSubstringsBackward[i] -= lcp2[suffixIndex];
            } else if (nextSuffix2[suffixIndex] != -1 && prevSuffix2[suffixIndex] != -1) {
                suffixSubstringsBackward[i] -= Math.max(lcp2[suffixIndex], lcp2[prevSuffix2[suffixIndex]]);
            }

            if (prevSuffix2[suffixIndex] != -1) {
                nextSuffix2[prevSuffix2[suffixIndex]] = nextSuffix2[suffixIndex];
                lcp2[prevSuffix2[suffixIndex]] = Math.min(lcp2[prevSuffix2[suffixIndex]], lcp2[suffixIndex]);
            }

            if (nextSuffix2[suffixIndex] != -1) {
                prevSuffix2[nextSuffix2[suffixIndex]] = prevSuffix2[suffixIndex];
            }
        }

        System.out.println();
    }
}
