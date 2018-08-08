package utils;

import java.util.Date;

public class KMPArray {
    public static void main(String[] args) {
        int strLen = 100000;
        StringBuilder build = new StringBuilder();

        for (int i = 0; i < strLen / 2; i++) {
            //build.append((char) ('a' + (char) (Math.random() * 2)));
            build.append("ab");
        }

        String s = build.toString();
        Date start = new Date();
        int[] prefix = new int[strLen];
        for (int i = 1; i < s.length(); i++) {
            int prevIndex = i - 1;
            while (s.charAt(prefix[prevIndex]) != s.charAt(i) && prefix[prevIndex] != 0) {
                prevIndex = prefix[prevIndex] - 1;
            }

            if (s.charAt(prefix[prevIndex]) == s.charAt(i)) {
                prefix[i]= prefix[prevIndex] + 1;
            }
        }

        Date end = new Date();

        System.out.println(end.getTime() - start.getTime() + "ms");
    }
}
