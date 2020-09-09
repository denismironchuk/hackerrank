package dynamicProgramming;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LcsExtended {
    private static final int LETTERS_CNT = 3;
    private static final int MAX_GRP_LEN = 6;
    private static final int MIN_GRP_LEN = 2;

    public static void main(String[] args) {
        int len = 10;
        StringBuilder b1 = new StringBuilder();
        StringBuilder b2 = new StringBuilder();

        for (int i = 0; i < len; i++) {
            b1.append((char)('a' + (int)(LETTERS_CNT * Math.random())));
            b2.append((char)('a' + (int)(LETTERS_CNT * Math.random())));
        }

        String s1 = b1.toString();
        String s2 = b2.toString();

        System.out.println(s1);
        System.out.println(s2);

        List<Integer> lens1 = buildGroups(s1);
        List<Integer> lens2 = buildGroups(s2);

        System.out.println(lens1);
        System.out.println(lens2);

        printStringGroups(s1, lens1);
        printStringGroups(s2, lens2);

        int[] prevGroupLastPosition1 = buildPrevGroupLastPosition(s1, lens1);
        int[] prevGroupLastPosition2 = buildPrevGroupLastPosition(s2, lens2);

        long[][] dyn = new long[len][len];
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

        //simple case common subsequences count (CORRECT)
        /*for (int i = 1; i < lens1.get(0); i++) {
            for (int j = 1; j < lens2.get(0); j++) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    dyn[i][j] = dyn[i][j - 1] + dyn[i - 1][j] + 1;
                } else {
                    dyn[i][j] = dyn[i][j - 1] + dyn[i - 1][j] - dyn[i - 1][j - 1];
                }
            }
        }

        //---------------------------------
        for (int i = lens1.get(0); i < len; i++) {
            for (int j = 1; j < lens2.get(0); j++) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    if (prevGroupLastPosition1[i] != prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] == prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = 2 * dyn[i][j - 1] + dyn[i - 1][j - 1];
                    } else if (prevGroupLastPosition1[i] == prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] == prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i][j - 1] + dyn[i - 1][j] + 1;
                    }
                } else {
                    if (prevGroupLastPosition1[i] != prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] == prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i][j - 1];
                    } else if (prevGroupLastPosition1[i] == prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] == prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i][j - 1] + dyn[i - 1][j] - dyn[i - 1][j - 1];
                    }
                }
            }
        }

        for (int i = 1; i < lens1.get(0); i++) {
            for (int j = lens2.get(0); j < len; j++) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    if (prevGroupLastPosition1[i] == prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] != prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = 2 * dyn[i - 1][j] + dyn[i - 1][j - 1];
                    } else if (prevGroupLastPosition1[i] == prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] == prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i][j - 1] + dyn[i - 1][j] + 1;
                    }
                } else {
                    if (prevGroupLastPosition1[i] == prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] != prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i - 1][j];
                    } else if (prevGroupLastPosition1[i] == prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] == prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i][j - 1] + dyn[i - 1][j] - dyn[i - 1][j - 1];
                    }
                }
            }
        }*/

        for (int i = 1; i < len; i++) {
            for (int j = 1; j < len; j++) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    if (prevGroupLastPosition1[i] != prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] == prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = 2 * dyn[i][j - 1] + dyn[i - 1][j - 1];
                    } else if (prevGroupLastPosition1[i] == prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] != prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = 2 * dyn[i - 1][j] + dyn[i - 1][j - 1];
                    } else if (prevGroupLastPosition1[i] == prevGroupLastPosition1[i - 1]
                            && prevGroupLastPosition2[j] == prevGroupLastPosition2[j - 1]) {
                        dyn[i][j] = dyn[i][j - 1] + dyn[i - 1][j] + getDynVal(dyn, prevGroupLastPosition1[i], prevGroupLastPosition2[j]);
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

        System.out.println(dyn[len - 1][len - 1]);
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
