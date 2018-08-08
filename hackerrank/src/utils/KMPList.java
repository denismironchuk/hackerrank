package utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KMPList {
    public static void main(String[] args) {
        int strLen = 1000000;
        StringBuilder build = new StringBuilder();

        for (int i = 0; i < strLen; i++) {
            build.append((char) ('a' + (char) (Math.random() * 26)));
            //build.append("ab");
        }

        String s = build.toString();
        Date start = new Date();
        new KMPList().kmp(s);

        Date end = new Date();

        System.out.println(end.getTime() - start.getTime() + "ms");
    }

    public List<Integer> kmp(String s) {
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

        return prefix;
    }
}
