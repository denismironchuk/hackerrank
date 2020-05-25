package codejam.year2020.round1C;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class Overrandomized {
    private static class Tuple implements Comparable<Tuple> {
        private char c;
        private int cnt;

        public Tuple(char c, int cnt) {
            this.c = c;
            this.cnt = cnt;
        }

        public char getC() {
            return c;
        }

        @Override
        public int compareTo(Tuple o) {
            return Integer.compare(o.cnt, cnt);
        }

        @Override
        public String toString() {
            return "Tuple{" +
                    "c=" + c +
                    ", cnt=" + cnt +
                    '}';
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
        //try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\dmiro\\Downloads\\sample.in.txt"))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int u = Integer.parseInt(br.readLine());
                Map<Character, Integer> stat = new HashMap<>();
                Set<Character> allSymbols = new HashSet<>();
                Set<Character> firstSymbols = new HashSet<>();
                for (int i = 0; i < 10000; i++) {
                    StringTokenizer tkn = new StringTokenizer(br.readLine());
                    long m = Long.parseLong(tkn.nextToken());
                    String r = tkn.nextToken();
                    boolean isFirst = true;
                    for (char c : r.toCharArray()) {
                        if (isFirst) {
                            stat.merge(c, 1, (oldVal, val) -> oldVal + 1);
                            firstSymbols.add(c);
                            isFirst = false;
                        }
                        allSymbols.add(c);
                    }
                }
                allSymbols.removeAll(firstSymbols);
                Character zeroPos = allSymbols.iterator().next();

                List<Tuple> tuples = new ArrayList<>();

                for (Map.Entry<Character, Integer> entry : stat.entrySet()) {
                    tuples.add(new Tuple(entry.getKey(), entry.getValue()));
                }
                Collections.sort(tuples);
                StringBuilder res = new StringBuilder();
                res.append(zeroPos);
                for (int i = 0; i < tuples.size(); i++) {
                    if (zeroPos != tuples.get(i).getC()) {
                        res.append(tuples.get(i).getC());
                    }
                }
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }
}
