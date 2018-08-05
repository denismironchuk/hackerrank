package palindromicBorder;

import java.util.Date;

public class PalindromArrayTrivial {
    public static final int ALPHABET_SIZE = 2;

    public static void main(String[] args) {
        while (true) {
            int strLen = 20000000;
            StringBuilder build = new StringBuilder();

            for (int i = 0; i < strLen; i++) {
                build.append((char) ('a' + (char) (Math.random() * ALPHABET_SIZE)));
                //build.append('a');
            }

            String s = build.toString();
            //System.out.println(s);

            int[] oddPalindroms = new int[strLen];
            int[] evenPalindroms = new int[strLen];

            Date start1 = new Date();
            for (int i = 0; i < strLen; i++) {
                for (int j = 1; i - j >= 0 && i + j < strLen && s.charAt(i - j) == s.charAt(i + j); j++) {
                    oddPalindroms[i]++;
                }

                for (int j = 1; i - j >= 0 && i + j - 1 < strLen && s.charAt(i - j) == s.charAt(i + j - 1); j++) {
                    evenPalindroms[i]++;
                }
            }
            Date end1 = new Date();

            System.out.println(end1.getTime() - start1.getTime() + "ms");

            int[] oddEffective = new int[strLen];
            int[] evenEffective = new int[strLen];

            int l = 0;
            int r = -1;

            Date start2 = new Date();
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

                if (oddEffective[i] != oddPalindroms[i]) {
                    throw new RuntimeException();
                }
            }

            Date end2 = new Date();

            System.out.println(end2.getTime() - start2.getTime() + "ms");

            l = 0;
            r = -1;

            Date start3 = new Date();
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
                if (evenEffective[i] != evenPalindroms[i]) {
                    throw new RuntimeException();
                }
            }

            Date end3 = new Date();

            System.out.println(end3.getTime() - start3.getTime() + "ms");
            System.out.println("=========================");
        }
        //System.out.println();
    }
}
