package dynamicProgramming.palindromicSubstrings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PalindromicSubsequence {
    private static final int LETTERS_CNT = 10;
    private static final int MAX_GRP_LEN = 10;
    private static final int MIN_GRP_LEN = 5;
    private static final int LEN = 20;

    public static void main(String[] args) {
        while (true) {
            StringBuilder b1 = new StringBuilder();

            for (int i = 0; i < LEN; i++) {
                b1.append((char) ('a' + (int) (LETTERS_CNT * Math.random())));
            }

            String s1 = b1.toString();
            String s2 = flipStr(s1);

            System.out.println(s1);
            System.out.println(s2);

            List<Integer> lens1 = buildGroups(s1);
            List<Integer> lens2 = flipList(lens1);

            //lens1 = Arrays.asList(5);
            //lens2 = Arrays.asList(2, 3);

            System.out.println(lens1);
            System.out.println(lens2);

            printStringGroups(s1, lens1);
            printStringGroups(s2, lens2);

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
                                res++;
                            }
                        }
                    } else if (lens1.size() == 2) {
                        for (int j = lens1.get(0); j < s1.length(); j++) {
                            if (s1.charAt(i) == s1.charAt(j)) {
                                res++;
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
                            } else if (prevGroupLastPosition1[s1Pos] == prevGroupLastPosition1[s1Pos - 1]
                                    && prevGroupLastPosition2[s2Pos] != prevGroupLastPosition2[s2Pos - 1]) {
                                res += dyn[s1Pos - 1][s2Pos - 1] + getDynVal(dyn, prevGroupLastPosition1[s1Pos], prevGroupLastPosition2[s2Pos]);
                            } else if (prevGroupLastPosition1[s1Pos] == prevGroupLastPosition1[s1Pos - 1]
                                    && prevGroupLastPosition2[s2Pos] == prevGroupLastPosition2[s2Pos - 1]) {
                                res += dyn[s1Pos - 1][s2Pos - 1] +
                                        getDynVal(dyn, s1Pos - 1, prevGroupLastPosition2[s2Pos]) +
                                        getDynVal(dyn, prevGroupLastPosition1[s1Pos], s2Pos - 1) +
                                        getDynVal(dyn, prevGroupLastPosition1[s1Pos], prevGroupLastPosition2[s2Pos]);
                            } else if (prevGroupLastPosition1[s1Pos] != prevGroupLastPosition1[s1Pos - 1]
                                    && prevGroupLastPosition2[s2Pos] != prevGroupLastPosition2[s2Pos - 1]) {
                                res += dyn[s1Pos - 1][s2Pos - 1];
                            }
                        }
                    }

                    if (s1.charAt(i) == s1.charAt(s1.length() - 1)) {
                        if (lens1.size() == 1) {
                            res += 1;
                        } else if (lens1.size() == 2 && groupFirstPosition[i] != groupFirstPosition[s1.length() - 1]) {
                            res += 1;
                        }
                    }
                }
            }

            if (lens1.size() == 1) {
                res += 1;
            }

            System.out.println(res);

            List<String> subs = generateSubstringsCoveringAllGroups(s1, lens1);

            int control = 0;

            for (String sub : subs) {
                if (sub.equals(flipStr(sub))) {
                    control++;
                }
            }

            System.out.println(control);

            if (res != control) {
                throw new RuntimeException();
            }
        }
    }

    private static long[][] buildDynTable(String s1, List<Integer> lens1, String s2, List<Integer> lens2,
                                          int[] prevGroupLastPosition1, int[] prevGroupLastPosition2) {
        long[][] dyn = new long[LEN][LEN];
        dyn[0][0] = s1.charAt(0) == s2.charAt(0) ? 1 : 0;

        for (int i = 1; i < lens2.get(0); i++) {
            if (s1.charAt(0) == s2.charAt(i)) {
                dyn[0][i] = dyn[0][i - 1] + 1;
            } else {
                dyn[0][i] = dyn[0][i - 1];
            }
        }

        for (int i = 1; i < lens1.get(0); i++) {
            if (s1.charAt(i) == s2.charAt(0)) {
                dyn[i][0] = dyn[i - 1][0] + 1;
            } else {
                dyn[i][0] = dyn[i - 1][0];
            }
        }

        for (int i = 1; i < LEN; i++) {
            for (int j = 1; j < LEN; j++) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    if (prevGroupLastPosition1[i] != prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] == prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i][j - 1] + dyn[i - 1][j - 1] + getDynVal(dyn, prevGroupLastPosition1[i], prevGroupLastPosition2[j]);
                    } else if (prevGroupLastPosition1[i] == prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] != prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i - 1][j] + dyn[i - 1][j - 1] + getDynVal(dyn, prevGroupLastPosition1[i], prevGroupLastPosition2[j]);
                    } else if (prevGroupLastPosition1[i] == prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] == prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i][j - 1] + dyn[i - 1][j] +
                                getDynVal(dyn, i - 1, prevGroupLastPosition2[j]) +
                                getDynVal(dyn, prevGroupLastPosition1[i], j - 1) +
                                getDynVal(dyn, prevGroupLastPosition1[i], prevGroupLastPosition2[j]);
                    } else if (prevGroupLastPosition1[i] != prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] != prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i - 1][j - 1];
                    }
                } else {
                    if (prevGroupLastPosition1[i] != prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] == prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i][j - 1];
                    } else if (prevGroupLastPosition1[i] == prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] != prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i - 1][j];
                    } else if (prevGroupLastPosition1[i] == prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] == prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i][j - 1] + dyn[i - 1][j] - dyn[i - 1][j - 1];
                    } else if (prevGroupLastPosition1[i] != prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] != prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = 0;
                    }
                }
            }
        }

        return dyn;
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

    private static void printStringGroups(String s, List<Integer> lens) {
        int procLen = 0;
        for (int l : lens) {
            System.out.printf("%s ", s.substring(procLen, procLen + l));
            procLen += l;
        }
        System.out.println();
    }

    private static List<Integer> buildGroups(String s) {
        List<Integer> lens1 = new ArrayList<>();
        int procLen = 0;
        while (procLen < s.length()) {
            int grpLen = MIN_GRP_LEN + (int)((MAX_GRP_LEN - MIN_GRP_LEN) * Math.random());
            grpLen = Math.min(grpLen, s.length() - procLen);
            lens1.add(grpLen);
            procLen += grpLen;
        }
        return lens1;
    }

    private static class Group {
        private int startPos;
        private int endPos;
        private boolean present = false;

        public Group(int startPos, int endPos) {
            this.startPos = startPos;
            this.endPos = endPos;
        }

        public void markGroup(int pos) {
            present = present || (pos >= startPos && pos <= endPos);
        }

        public void refresh() {
            present = false;
        }
    }

    private static List<String> generateSubstringsCoveringAllGroups(String s, List<Integer> groupsLen) {
        int startPos = 0;
        List<Group> groups = new ArrayList<>();
        for (int grpLen : groupsLen) {
            groups.add(new Group(startPos, startPos + grpLen - 1));
            startPos = startPos + grpLen;
        }

        List<String> subs = new ArrayList<>();
        for (long i = 1; i < pow2(s.length()); i++) {
            StringBuilder sub = new StringBuilder();
            int pos = 0;
            long i_ = i;
            while (i_ != 0) {
                if (i_ % 2 == 1) {
                    sub.append(s.charAt(pos));
                    for (Group grp : groups) {
                        grp.markGroup(pos);
                    }
                }
                pos++;
                i_ /= 2;
            }

            boolean validSubs = true;
            for (Group grp : groups) {
                validSubs &= grp.present;
                grp.refresh();
            }
            if (validSubs) {
                subs.add(sub.toString());
            }
        }
        return subs;
    }

    private static long pow2(int pow) {
        long res = 1;
        for (int i = 0; i < pow; i++) {
            res *= 2;
        }
        return res;
    }
}
