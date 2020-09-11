package dynamicProgramming.palindromicSubstrings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PalindromicStringFinalSolution {
    private static long MOD = 1000000000 + 7;
    private static int LEN = 0;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 0; t < T; t++) {
                int n = Integer.parseInt(br.readLine());
                StringBuilder builder = new StringBuilder();
                List<Integer> lens1 = new ArrayList<>();

                for (int i = 0; i < n; i++) {
                    String s = br.readLine();
                    builder.append(s);
                    lens1.add(s.length());
                }

                String s1 = builder.toString();
                LEN = s1.length();
                String s2 = flipStr(s1);
                List<Integer> lens2 = flipList(lens1);

                int[] prevGroupLastPosition1 = buildPrevGroupLastPosition(s1, lens1);
                int[] prevGroupLastPosition2 = buildPrevGroupLastPosition(s2, lens2);

                int[] groupFirstPosition = buildGroupFirstPosition(s1, lens1);

                long[][] dyn = buildDynTable(s1, lens1, s2, lens2, prevGroupLastPosition1, prevGroupLastPosition2);
                long res = 0;

                for (int i = 0; i < s1.length() - 1; i++) {
                    if (i == 0) {
                        if (lens1.size() == 1) {
                            for (int j = 0; j < s1.length(); j++) {
                                if (s1.charAt(i) == s1.charAt(j)) {
                                    res += 1;
                                    res %= MOD;
                                }
                            }
                        } else if (lens1.size() == 2) {
                            for (int j = lens1.get(0); j < s1.length(); j++) {
                                if (s1.charAt(i) == s1.charAt(j)) {
                                    res += 1;
                                    res %= MOD;
                                }
                            }
                        }
                    } else {
                        for (int j = i; j < s1.length() - 1; j++) {
                            if (groupFirstPosition[i] != groupFirstPosition[j] && groupFirstPosition[i] != groupFirstPosition[prevGroupLastPosition1[j]]) {
                                break;
                            }

                            if (s1.charAt(i) == s1.charAt(j)) {
                                int s1Pos = i;
                                int s2Pos = s1.length() - j - 1;

                                if (prevGroupLastPosition1[s1Pos] != prevGroupLastPosition1[s1Pos - 1]
                                        && prevGroupLastPosition2[s2Pos] == prevGroupLastPosition2[s2Pos - 1]) {
                                    res += dyn[s1Pos - 1][s2Pos - 1] + getDynVal(dyn, prevGroupLastPosition1[s1Pos], prevGroupLastPosition2[s2Pos]);
                                    res %= MOD;
                                } else if (prevGroupLastPosition1[s1Pos] == prevGroupLastPosition1[s1Pos - 1]
                                        && prevGroupLastPosition2[s2Pos] != prevGroupLastPosition2[s2Pos - 1]) {
                                    res += dyn[s1Pos - 1][s2Pos - 1] + getDynVal(dyn, prevGroupLastPosition1[s1Pos], prevGroupLastPosition2[s2Pos]);
                                    res %= MOD;
                                } else if (prevGroupLastPosition1[s1Pos] == prevGroupLastPosition1[s1Pos - 1]
                                        && prevGroupLastPosition2[s2Pos] == prevGroupLastPosition2[s2Pos - 1]) {
                                    res += dyn[s1Pos - 1][s2Pos - 1] +
                                            getDynVal(dyn, s1Pos - 1, prevGroupLastPosition2[s2Pos]) +
                                            getDynVal(dyn, prevGroupLastPosition1[s1Pos], s2Pos - 1) +
                                            getDynVal(dyn, prevGroupLastPosition1[s1Pos], prevGroupLastPosition2[s2Pos]);
                                    res %= MOD;
                                } else if (prevGroupLastPosition1[s1Pos] != prevGroupLastPosition1[s1Pos - 1]
                                        && prevGroupLastPosition2[s2Pos] != prevGroupLastPosition2[s2Pos - 1]) {
                                    res += dyn[s1Pos - 1][s2Pos - 1];
                                    res %= MOD;
                                }
                            }
                        }

                        if (s1.charAt(i) == s1.charAt(s1.length() - 1)) {
                            if (lens1.size() == 1) {
                                res += 1;
                                res %= MOD;
                            } else if (lens1.size() == 2 && groupFirstPosition[i] != groupFirstPosition[s1.length() - 1]) {
                                res += 1;
                                res %= MOD;
                            }
                        }
                    }
                }

                if (lens1.size() == 1) {
                    res += 1;
                    res %= MOD;
                }

                System.out.println(res);
            }
        }
    }

    private static String flipStr(String s) {
        StringBuilder flippedStr = new StringBuilder();

        for (int i = s.length() - 1; i >= 0; i--) {
            flippedStr.append(s.charAt(i));
        }

        return flippedStr.toString();
    }

    private static List<Integer> flipList(List<Integer> l) {
        List<Integer> flippedList = new ArrayList<>();

        for (int i = l.size() - 1; i >= 0; i--) {
            flippedList.add(l.get(i));
        }

        return flippedList;
    }

    private static long getDynVal(long[][] dyn, int preGroupLastPosition1, int preGroupLastPosition2) {
        if (preGroupLastPosition1 == -1 && preGroupLastPosition2 == -1) {
            return 1;
        }

        if (preGroupLastPosition1 == -1 || preGroupLastPosition2 == -1) {
            return 0;
        }

        return dyn[preGroupLastPosition1][preGroupLastPosition2];
    }

    private static long[][] buildDynTable(String s1, List<Integer> lens1, String s2, List<Integer> lens2,
                                          int[] prevGroupLastPosition1, int[] prevGroupLastPosition2) {
        long[][] dyn = new long[LEN][LEN];
        dyn[0][0] = s1.charAt(0) == s2.charAt(0) ? 1 : 0;

        for (int i = 1; i < lens2.get(0); i++) {
            if (s1.charAt(0) == s2.charAt(i)) {
                dyn[0][i] = dyn[0][i - 1] + 1;
                dyn[0][i] %= MOD;
            } else {
                dyn[0][i] = dyn[0][i - 1];
                dyn[0][i] %= MOD;
            }
        }

        for (int i = 1; i < lens1.get(0); i++) {
            if (s1.charAt(i) == s2.charAt(0)) {
                dyn[i][0] = dyn[i - 1][0] + 1;
                dyn[i][0] %= MOD;
            } else {
                dyn[i][0] = dyn[i - 1][0];
                dyn[i][0] %= MOD;
            }
        }

        for (int i = 1; i < LEN; i++) {
            for (int j = 1; j < LEN; j++) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    if (prevGroupLastPosition1[i] != prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] == prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i][j - 1] + dyn[i - 1][j - 1] + getDynVal(dyn, prevGroupLastPosition1[i], prevGroupLastPosition2[j]);
                        dyn[i][j] %= MOD;
                    } else if (prevGroupLastPosition1[i] == prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] != prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i - 1][j] + dyn[i - 1][j - 1] + getDynVal(dyn, prevGroupLastPosition1[i], prevGroupLastPosition2[j]);
                        dyn[i][j] %= MOD;
                    } else if (prevGroupLastPosition1[i] == prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] == prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i][j - 1] + dyn[i - 1][j] +
                                getDynVal(dyn, i - 1, prevGroupLastPosition2[j]) +
                                getDynVal(dyn, prevGroupLastPosition1[i], j - 1) +
                                getDynVal(dyn, prevGroupLastPosition1[i], prevGroupLastPosition2[j]);
                        dyn[i][j] %= MOD;
                    } else if (prevGroupLastPosition1[i] != prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] != prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i - 1][j - 1];
                        dyn[i][j] %= MOD;
                    }
                } else {
                    if (prevGroupLastPosition1[i] != prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] == prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i][j - 1];
                        dyn[i][j] %= MOD;
                    } else if (prevGroupLastPosition1[i] == prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] != prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i - 1][j];
                        dyn[i][j] %= MOD;
                    } else if (prevGroupLastPosition1[i] == prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] == prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = ((dyn[i][j - 1] + dyn[i - 1][j]) % MOD - dyn[i - 1][j - 1] + MOD) % MOD;
                        dyn[i][j] %= MOD;
                    } else if (prevGroupLastPosition1[i] != prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] != prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = 0;
                    }
                }
            }
        }

        return dyn;
    }

    private static int[] buildPrevGroupLastPosition(String s, List<Integer> lens) {
        int[] prevGroupLastPosition = new int[s.length()];

        int prevLastPos = -1;
        Iterator<Integer> grpLen = lens.iterator();
        int currGroupLen = grpLen.next();
        for (int i = 0; i < s.length(); i++) {
            if (prevLastPos + currGroupLen < i) {
                prevLastPos = i - 1;
                currGroupLen = grpLen.next();
            }
            prevGroupLastPosition[i] = prevLastPos;
        }

        return prevGroupLastPosition;
    }

    private static int[] buildGroupFirstPosition(String s, List<Integer> lens) {
        int[] groupFirstPosition = new int[s.length()];

        int groupFirstPos = 0;
        Iterator<Integer> grpLen = lens.iterator();
        int currGroupLen = grpLen.next();
        for (int i = 0; i < s.length(); i++) {
            if (groupFirstPos + currGroupLen == i) {
                groupFirstPos = i;
                currGroupLen = grpLen.next();
            }
            groupFirstPosition[i] = groupFirstPos;
        }

        return groupFirstPosition;
    }
}
