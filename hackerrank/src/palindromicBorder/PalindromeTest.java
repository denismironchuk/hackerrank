package palindromicBorder;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by Denis_Mironchuk on 7/16/2018.
 */
public class PalindromeTest {
    public static final int ALPHABET_SIZE = 26;
    public static final int SEPARATORS_COUNT = 1;

    private static int[] buildSuffixArray(String s) {
        int strLen = s.length();

        int[] suffixArr = new int[strLen];
        int[] priorities = new int[strLen];

        //initial count sort
        int[] charCnt = new int[ALPHABET_SIZE + SEPARATORS_COUNT];
        for (int i = 0; i < strLen; i++) {
            charCnt[s.charAt(i) - 'a' + SEPARATORS_COUNT]++;
        }

        int[] incrCnt = new int[ALPHABET_SIZE + SEPARATORS_COUNT];
        incrCnt[0] = charCnt[0];
        for (int i = 1; i < ALPHABET_SIZE + SEPARATORS_COUNT; i++) {
            incrCnt[i] = incrCnt[i - 1] + charCnt[i];
        }

        for (int i = strLen - 1; i >= 0; i--) {
            suffixArr[incrCnt[s.charAt(i) - 'a' + SEPARATORS_COUNT] - 1] = i;
            incrCnt[s.charAt(i) - 'a' + SEPARATORS_COUNT]--;
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

    private static int countPalindroms(String s) {
        int res = s.isEmpty() ? 0 : 1;

        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(0) == s.charAt(i)) {
                boolean isPalindrome = true;
                for (int j = 1; isPalindrome && i - j >= j; j++) {
                    if (s.charAt(j) != s.charAt(i - j)) {
                        isPalindrome=false;
                    }
                }
                if (isPalindrome) {
                    res++;
                }
            }
        }

        return res;
    }

    public static void main(String[] args) {
        int strLen = 20;
        StringBuilder build = new StringBuilder();

        for (int i = 0; i < strLen - 1; i++) {
            build.append((char) ('a' + (char) (Math.random() * ALPHABET_SIZE)));
        }

        build.append((char) ('a' - 1));

        //String s = build.toString();
        String s = "ababacabad" + (char) ('a' - 1);
        strLen = s.length();
        System.out.println(s);

        int[] suffixArr = buildSuffixArray(s);

        int[] lcp = buildLCPArray(s, suffixArr);
        for (int i = 0; i < strLen; i++) {
            int comPref = i == strLen - 1 ? -1 : lcp[i];
            int commonPalindroms = i != strLen - 1 ? countPalindroms(s.substring(suffixArr[i], suffixArr[i] + comPref)) : 0;

            System.out.printf("%4d %s %d\n", comPref, s.substring(suffixArr[i]), commonPalindroms);
        }
        System.out.println();
    }
}
