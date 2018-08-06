package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Denis_Mironchuk on 3/23/2018.
 */
public class CountSort {
    //A = 65
    //Z = 90
    public static final int ALPHBET_SIZE = 5;

    private static List<String> sort(List<String> strings, int sortIndex, int n) {
        String s = strings.get(sortIndex);

        int[] cnt = new int[ALPHBET_SIZE];

        for (int i = 0; i < n; i++) {
            cnt[s.charAt(i) - 'A']++;
        }

        for (int i = 1; i < ALPHBET_SIZE; i++) {
            cnt[i]+= cnt[i-1];
        }

        List<char[]> res = new ArrayList<>();

        for (int i = 0; i < strings.size(); i++) {
            res.add(new char[n]);
        }

        for (int i = n - 1; i > -1; i--) {
            for (int j = 0; j < strings.size(); j++) {
                res.get(j)[cnt[s.charAt(i) - 'A'] - 1] = strings.get(j).charAt(i);
            }

            cnt[s.charAt(i) - 'A']--;
        }

        return res.stream().map(String::new).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        int stringsCnt = 3;
        int n = 10;

        List<StringBuilder> builders = new ArrayList<>();
        for (int i = 0; i < stringsCnt; i++) {
            builders.add(new StringBuilder());
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < stringsCnt; j++) {
                builders.get(j).append((char)('A' + (int)(ALPHBET_SIZE * Math.random())));
            }
        }

        List<String> strings = builders.stream().map(StringBuilder::toString).collect(Collectors.toList());

        for (String s : strings) {
            System.out.println(s);
        }

        System.out.println();

        List<String> result = strings;

        for (int i = 0; i < stringsCnt; i++) {
            result = sort(result, i, n);
        }

        for (String s : result) {
            System.out.println(s);
        }
    }
}
