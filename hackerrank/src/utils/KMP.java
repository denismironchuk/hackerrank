package utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KMP {
    /*public static void main(String[] args) {
        int strLen = 10000000;
        StringBuilder build = new StringBuilder();

        for (int i = 0; i < strLen; i++) {
            build.append((char) ('a' + (char) (Math.random() * 2)));
        }

        String s = build.toString();
        Date start = new Date();
        List<Integer> prefix = new ArrayList<>();
        prefix.add(0);
        for (int i = 1; i < s.length(); i++) {
            int prevIndex = i - 1;
            while (s.charAt(prefix.get(prevIndex)) != s.charAt(i) && prefix.get(prevIndex) != 0) {
                prevIndex = prefix.get(prevIndex) - 1;
            }

            if (s.charAt(prefix.get(prevIndex)) == s.charAt(i)) {
                prefix.add(prefix.get(prevIndex) + 1);
            } else {
                prefix.add(0);
            }
        }

        Date end = new Date();

        System.out.println(end.getTime() - start.getTime() + "ms");
    }*/

    public static void main(String[] args) {
        int strLen = 10000000;
        StringBuilder build = new StringBuilder();

        for (int i = 0; i < strLen; i++) {
            build.append((char) ('a' + (char) (Math.random() * 2)));
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
