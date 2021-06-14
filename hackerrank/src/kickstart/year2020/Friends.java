package kickstart.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class Friends {

    private static final int CHARS = 26;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                int q = Integer.parseInt(tkn1.nextToken());
                List<String> names = new ArrayList<>();
                StringTokenizer namesTkn = new StringTokenizer(br.readLine());
                for (int i = 0; i < n; i++) {
                    names.add(namesTkn.nextToken());
                }

                List<Set<Character>> charsNames = new ArrayList<>();

                for (String name : names) {
                    Set<Character> chars = new HashSet<>();
                    for (char c : name.toCharArray()) {
                        chars.add(c);
                    }
                    charsNames.add(chars);
                }

                int[][] graph = new int[CHARS][CHARS];

                for (char c = 'A'; c <= 'Z'; c++) {
                    for (Set<Character> name : charsNames) {
                        if (name.contains(c)) {
                            for (Character nameChar : name) {
                                graph[c - 'A'][nameChar - 'A'] = 1;
                            }
                        }
                    }
                }

                for (int i = 0; i < CHARS; i++) {
                    for (int j = 0; j < CHARS; j++) {
                        if (i == j) {
                            graph[i][j] = 0;
                        } else {
                            if (graph[i][j] == 0) {
                                graph[i][j] = Integer.MAX_VALUE / 2;
                            }
                        }
                    }
                }

                for (int k = 0; k < CHARS; k++) {
                    for (int i = 0; i < CHARS; i++) {
                        for (int j = 0; j < CHARS; j++) {
                            graph[i][j] = Math.min(graph[i][j], graph[i][k] + graph[k][j]);
                        }
                    }
                }

                StringBuilder res = new StringBuilder();
                for (int i = 0; i < q; i++) {
                    StringTokenizer qTkn = new StringTokenizer(br.readLine());
                    int start = Integer.parseInt(qTkn.nextToken()) - 1;
                    int dest = Integer.parseInt(qTkn.nextToken()) - 1;
                    Set<Character> w1 = charsNames.get(start);
                    Set<Character> w2 = charsNames.get(dest);
                    int minDest = Integer.MAX_VALUE;
                    for (Character c1 : w1) {
                        for (Character c2 : w2) {
                            minDest = Math.min(minDest, graph[c1 - 'A'][c2 - 'A']);
                        }
                    }
                    if (minDest == Integer.MAX_VALUE / 2) {
                        minDest = -1;
                    } else {
                        minDest += 2;
                    }
                    res.append(" ").append(minDest);
                }

                System.out.printf("Case #%s:%s\n", t, res.toString());
            }
        }
    }
}
