package codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class RobotProgrammingStrategy {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t <= T; t++) {
            int a = Integer.parseInt(br.readLine());
            String[] strats = new String[a];
            int maxStratLen = 1000;
            for (int i = 0 ; i < a; i++) {
                strats[i] = br.readLine();
                /*if (strats[i].length() > maxStratLen) {
                    maxStratLen = strats[i].length();
                }*/
            }

            for (int i = 0; i < a; i++) {
                String toAdd = strats[i];
                while (strats[i].length() < maxStratLen) {
                    if (maxStratLen - strats[i].length() >= toAdd.length()) {
                        strats[i] += toAdd;
                    } else {
                        strats[i] += toAdd.substring(0, maxStratLen - strats[i].length());
                    }
                }
            }

            Set<Integer> excluded = new HashSet<>();
            StringBuilder myStrat = new StringBuilder();
            boolean stratFound = false;

            for (int i = 0; excluded.size() < a && i < maxStratLen; i++) {
                Set<Character> chars = new HashSet<>();
                for (int j = 0; j < a; j++) {
                    if (excluded.contains(j)) {
                        continue;
                    }

                    chars.add(strats[j].charAt(i));
                }

                if (chars.size() == 3) {
                    break;
                } else if (chars.size() == 2) {
                    char chatToExclude = 'R';
                    if (chars.contains('R') && chars.contains('P')) {
                        myStrat.append('P');
                        chatToExclude = 'R';
                    } else if (chars.contains('S') && chars.contains('P')) {
                        myStrat.append('S');
                        chatToExclude = 'P';
                    } else if (chars.contains('S') && chars.contains('R')) {
                        myStrat.append('R');
                        chatToExclude = 'S';
                    }
                    for (int j = 0; j < a; j++) {
                        if (excluded.contains(j)) {
                            continue;
                        }
                        if (strats[j].charAt(i) == chatToExclude) {
                            excluded.add(j);
                        }
                    }
                } else {
                    if (chars.contains('R')) {
                        myStrat.append('P');
                    } else if (chars.contains('P')) {
                        myStrat.append('S');
                    } else if (chars.contains('S')) {
                        myStrat.append('R');
                    }
                    stratFound = true;
                    break;
                }
            }

            System.out.printf("Case #%s: %s\n", t, stratFound ? myStrat : "IMPOSSIBLE");
        }
    }
}
