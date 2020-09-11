package dynamicProgramming.palindromicSubstrings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LcsExtended {
    private static final int LETTERS_CNT = 3;
    private static final int MAX_GRP_LEN = 9;
    private static final int MIN_GRP_LEN = 2;
    private static final int LEN = 20;

    public static void main(String[] args) {
        while (true) {
            StringBuilder b1 = new StringBuilder();
            StringBuilder b2 = new StringBuilder();

            for (int i = 0; i < LEN; i++) {
                b1.append((char) ('a' + (int) (LETTERS_CNT * Math.random())));
                b2.append((char) ('a' + (int) (LETTERS_CNT * Math.random())));
            }

            String s1 = b1.toString();
            String s2 = b2.toString();

            //s1 = "acbcc";
            //s2 = "bcbac";

            System.out.println(s1);
            System.out.println(s2);

            List<Integer> lens1 = buildGroups(s1);
            List<Integer> lens2 = buildGroups(s2);

            //lens1 = Arrays.asList(5);
            //lens2 = Arrays.asList(2, 3);

            System.out.println(lens1);
            System.out.println(lens2);

            printStringGroups(s1, lens1);
            printStringGroups(s2, lens2);

            int[] prevGroupLastPosition1 = buildPrevGroupLastPosition(s1, lens1);
            int[] prevGroupLastPosition2 = buildPrevGroupLastPosition(s2, lens2);

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

            System.out.println(dyn[LEN - 1][LEN - 1]);

            List<String> subs1 = generateSubstringsCoveringAllGroups(s1, lens1);
            Map<String, Long> subs1Cnt = new HashMap<>();
            for (String sub1 : subs1) {
                subs1Cnt.merge(sub1, 1l, (oldVal, newVal) -> oldVal + newVal);
            }

            List<String> subs2 = generateSubstringsCoveringAllGroups(s2, lens2);

            int cnt = 0;

            for (String sub : subs2) {
                if (subs1Cnt.containsKey(sub)) {
                    cnt += subs1Cnt.get(sub);
                }
            }

            System.out.println(cnt);

            if (dyn[LEN - 1][LEN - 1] != cnt) {
                throw new RuntimeException();
            }
        }
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
}
