package utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KMPOptimized {
    public static void main(String[] args) {
        int strLen = 1000000;
        StringBuilder build = new StringBuilder();

        for (int i = 0; i < strLen; i++) {
            build.append((char) ('a' + (char) (Math.random() * 2)));
        }

        String s = build.toString();

        //System.out.println(s);

        Set<Character> chars = new HashSet<>();
        for (int i = 0; i < s.length(); i++) {
            chars.add(s.charAt(i));
        }

        Date start = new Date();
        List<Integer> prefix = new ArrayList<>();
        List<Map<Character, Integer>> transitions = new ArrayList<>();
        initFirst(s, chars, prefix, transitions);

        for (int i = 1; i < s.length(); i++) {
            char curChar = s.charAt(i);
            Map<Character, Integer> lastTransitions = transitions.get(transitions.size() - 1);
            int newPrefix = lastTransitions.get(curChar);
            prefix.add(newPrefix);

            Map<Character, Integer> transMap = new HashMap<>();
            if (newPrefix > 0) {
                Map<Character, Integer> prefixTransitions = transitions.get(newPrefix - 1);
                for (Character c : chars) {
                    if (s.charAt(newPrefix) == c) {
                        transMap.put(c, newPrefix + 1);
                    } else {
                        transMap.put(c, prefixTransitions.get(c));
                    }
                }
            } else {
                initFirstTransitions(s, chars, transMap);
            }
            transitions.add(transMap);
        }
        Date end = new Date();

        System.out.println((end.getTime() - start.getTime()) + "ms");
    }

    private static void initFirstTransitions(final String s, final Set<Character> chars, final Map<Character, Integer> transMap) {
        for (Character c : chars) {
            if (s.charAt(0) == c) {
                transMap.put(c, 1);
            } else {
                transMap.put(c, 0);
            }
        }
    }

    private static void initFirst(final String s, final Set<Character> chars, final List<Integer> prefix, final List<Map<Character, Integer>> transitions) {
        prefix.add(0);
        Map<Character, Integer> transMap = new HashMap<>();
        initFirstTransitions(s, chars, transMap);
        transitions.add(transMap);
    }


}
