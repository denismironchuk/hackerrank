package palindromicBorder;

/**
 * Created by Влада on 31.07.2018.
 */
public class StartingPalindrome {
    public static final int ALPHABET_SIZE = 2;

    public static int[] countOddPalindroms(String s) {
        int strLen = s.length();

        int[] oddEffective = new int[strLen];

        int l = 0;
        int r = -1;

        for (int i = 0; i < strLen; i++) {
            if (i <= r) {
                oddEffective[i] = Math.min(oddEffective[l + r - i], r - i);
            }
            for (int j = oddEffective[i] + 1; i - j >= 0 && i + j < strLen && s.charAt(i - j) == s.charAt(i + j); j++) {
                oddEffective[i]++;
            }
            if (oddEffective[i] + i > r) {
                r = i + oddEffective[i];
                l = i - oddEffective[i];
            }
        }

        return oddEffective;
    }

    public static int[] countEvenPalindroms(String s) {
        int strLen = s.length();
        int[] evenEffective = new int[strLen];

        int l = 0;
        int r = -1;

        for (int i = 0; i < strLen; i++) {
            if (i <= r) {
                evenEffective[i] = Math.min(evenEffective[l + r - i + 1], r - i + 1);
            }
            for (int j = evenEffective[i] + 1; i - j >= 0 && i + j - 1 < strLen && s.charAt(i - j) == s.charAt(i + j - 1); j++) {
                evenEffective[i]++;
            }
            if (evenEffective[i] + i - 1 > r) {
                r = i + evenEffective[i] - 1;
                l = i - evenEffective[i];
            }
        }

        return evenEffective;
    }

    public static void main(String[] args) {
        int strLen = 20;
        StringBuilder build = new StringBuilder();

        for (int i = 0; i < strLen; i++) {
            build.append((char) ('a' + (char) (Math.random() * ALPHABET_SIZE)));
        }

        String s = build.toString();
        System.out.println(s);

        int[] oddPalindoms = countOddPalindroms(s);
        int[] evenPalindroms = countEvenPalindroms(s);

        int[] startPalindroms = countAllStartingPalindroms(oddPalindoms, evenPalindroms, 3, 19);
        for (int i = 0; i < startPalindroms.length; i++) {
            System.out.print(startPalindroms[i] + " ");
        }
    }

    /*private static int[] countAllStartingPalindroms(int[] oddPalindoms, int[] evenPalindroms,
                                                    int startPos, int endPos) {
        int[] startPalindromsPrepareOdd = new int[endPos - startPos + 1];
        int[] startPalindromsPrepareEven = new int[endPos - startPos + 1];
        for (int i = endPos; i >= startPos; i--) {
            int oddIndex = Math.max(i - oddPalindoms[i], startPos);
            startPalindromsPrepareOdd[oddIndex - startPos]++;
            int evenIndex = Math.max(i - evenPalindroms[i], startPos);
            startPalindromsPrepareEven[evenIndex - startPos]++;
        }
        int[] startPalindromsOdd = new int[endPos - startPos + 1];
        int[] startPalindromsEven = new int[endPos - startPos + 1];
        int[] startPalindroms = new int[endPos - startPos + 1];
        startPalindromsOdd[0] = startPalindromsPrepareOdd[0];
        startPalindromsEven[0] = startPalindromsPrepareEven[0] - 1;
        startPalindroms[0] = startPalindromsOdd[0] + startPalindromsEven[0];
        for (int i = 1; i < endPos - startPos + 1; i++) {
            startPalindromsOdd[i] = startPalindromsPrepareOdd[i] + startPalindromsOdd[i - 1] - 1;
            startPalindromsEven[i] = startPalindromsPrepareEven[i] + startPalindromsEven[i - 1] - 1;
            startPalindroms[i] = startPalindromsOdd[i] + startPalindromsEven[i];
        }
        return startPalindroms;
    }*/

    private static int[] countAllStartingPalindroms(int[] oddPalindoms, int[] evenPalindroms,
                                                    int startPos, int endPos) {
        int[] startPalindromsPrepare = new int[endPos - startPos + 1];
        for (int i = endPos; i >= startPos; i--) {
            int oddIndex = Math.max(i - oddPalindoms[i], startPos);
            startPalindromsPrepare[oddIndex - startPos]++;
            int evenIndex = Math.max(i - evenPalindroms[i], startPos);
            startPalindromsPrepare[evenIndex - startPos]++;
        }
        int[] startPalindroms = new int[endPos - startPos + 1];
        startPalindroms[0] = startPalindromsPrepare[0] - 1;
        for (int i = 1; i < endPos - startPos + 1; i++) {
            startPalindroms[i] = startPalindromsPrepare[i] + startPalindroms[i - 1] - 2;;
        }
        return startPalindroms;
    }
}
