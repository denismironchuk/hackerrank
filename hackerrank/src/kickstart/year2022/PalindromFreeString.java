package kickstart.year2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PalindromFreeString {

    public static int MIN_LEN = 6;

    private static String buildRandomString(int len) {
        StringBuilder s = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < len; i++) {
            int rndVal = rnd.nextInt(3);
            if (rndVal == 0) {
                s.append('0');
            } else if (rndVal == 1) {
                s.append('1');
            } else {
                s.append('?');
            }
        }
        return s.toString();
    }

    public static boolean isPalindromFreeTrivial(String s) {
        int freeCells = 0;
        for (char c : s.toCharArray()) {
            if (c == '?') {
                freeCells++;
            }
        }

        Set<Integer> states = buildStates(freeCells);
        for (Integer state : states) {
            StringBuilder fullS = new StringBuilder();
            int div = fastPow(2, freeCells - 1);
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '?') {
                    fullS.append(state / div);
                    state %= div;
                    div /= 2;
                } else {
                    fullS.append(s.charAt(i));
                }
            }

            int maxLen = findMaxPalindromLen(fullS.toString());

            if (maxLen < 5) {
                return true;
            }
        }
        return false;
    }

    public static Set<String> buildAllPossibleStates(int n) {
        Set<String> res = new HashSet<>();

        for (int i = 0; i < fastPow(3, n); i++) {
            StringBuilder str = new StringBuilder();
            int i_ = i;
            for (int j = 0; j < n; j++) {
                int d = i_ % 3;
                if (d == 0) {
                    str.append(0);
                } else if (d == 1) {
                    str.append(1);
                } else {
                    str.append('?');
                }
                i_ /= 2;
            }
            res.add(str.toString());
        }

        return res;
    }

    private static int findMaxPalindromLen(String s) {
        int[] pLenOdd = new int[s.length()];
        pLenOdd[0] = 1;
        int l = 0;
        int r = 0;
        for (int i = 1; i < s.length(); i++) {
            if (i >= l && i <= r) {
                pLenOdd[i] = Math.min(pLenOdd[l + r - i], 2 * (r - i) + 1);
            } else {
                pLenOdd[i] = 1;
            }
            int pos = i + (pLenOdd[i] / 2) + 1;
            if (pos <= r) {
                continue;
            }
            boolean isPalindrom = true;
            while (isPalindrom && pos < s.length() && i - (pos - i) >= 0) {
                if (s.charAt(pos) == s.charAt(i - (pos - i))) {
                    r = pos;
                    l = i - (pos - i);
                    pLenOdd[i]+=2;
                } else {
                    isPalindrom = false;
                }
                pos++;
            }
        }

        int[] pLenEven = new int[s.length() + 1];
        pLenEven[0] = 0;
        l = 0;
        r = 0;
        for (int i = 1; i <= s.length(); i++) {
            if (i >= l && i <= r) {
                pLenEven[i] = Math.min(pLenEven[l + r - i], 2 * (r - i));
            } else {
                pLenEven[i] = 0;
            }
            int pos = i + (pLenEven[i] / 2) + 1;
            if (pos <= r) {
                continue;
            }
            boolean isPalindrom = true;
            while (isPalindrom && (pos - 1) < s.length() && i - (pos - i) >= 0 && (pos - 1 >= 0)) {
                if (s.charAt(pos - 1) == s.charAt(i - (pos - i))) {
                    r = pos;
                    l = i - (pos - i);
                    pLenEven[i] += 2;
                } else {
                    isPalindrom = false;
                }
                pos++;
            }
        }

        int res = 0;
        for (int i = 0; i < pLenOdd.length; i++) {
            res = Math.max(res, pLenOdd[i]);
        }

        for (int i = 0; i < pLenEven.length; i++) {
            res = Math.max(res, pLenEven[i]);
        }

        return res;
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int n = Integer.parseInt(br.readLine());
                String s = br.readLine();
                boolean isPalindromFree = isPalindromFree(s);
                System.out.printf("Case #%s: %s\n", t, isPalindromFree ? "POSSIBLE" : "IMPOSSIBLE");
            }
        }
    }

    private static boolean isPalindromFree(String s) {
        if (s.length() < 5) {
            return true;
        }

        int freeCells = countFreeCells(0, s);
        Set<Integer> states = buildStates(freeCells);
        boolean isPalindromFree = false;
        if (freeCells == 0) {
            isPalindromFree = findMaxPalindromLen(s.substring(0, Math.min(MIN_LEN, s.length()))) < 5;
        } else {
            states = getNonPalindromStates(0, s, freeCells, states);
            isPalindromFree = states.size() != 0;
        }

        for (int i = 1; isPalindromFree && i <= s.length() - MIN_LEN; i++) {
            if (s.charAt(i - 1) == '?') {
                int div = fastPow(2, freeCells - 1);
                Set<Integer> newStates = new HashSet<>();
                for (Integer state : states) {
                    newStates.add(state % div);
                }
                states = newStates;
                freeCells--;
            }

            if (s.charAt(i + MIN_LEN - 1) == '?') {
                Set<Integer> newStates = new HashSet<>();
                for (Integer state : states) {
                    newStates.add(state * 2);
                    newStates.add(state * 2 + 1);
                }
                states = newStates;
                freeCells++;
            }

            if (freeCells == 0) {
                isPalindromFree = findMaxPalindromLen(s.substring(i, i + MIN_LEN)) < 5;
            } else {
                states = getNonPalindromStates(i, s, freeCells, states);
                isPalindromFree = states.size() != 0;
            }
        }
        return isPalindromFree;
    }

    private static Set<Integer> getNonPalindromStates(int position, String s, int freeCells, Set<Integer> states) {
        Set<Integer> notPalindromStates = new HashSet<>();

        for (Integer state : states) {
            Integer _state = state;
            StringBuilder stringForCheck = new StringBuilder();
            int div = fastPow(2, freeCells - 1);
            for (int i = 0; i < Math.min(s.length(), MIN_LEN); i++) {
                if (s.charAt(position + i) == '?') {
                    stringForCheck.append(_state / div);
                    _state %= div;
                    div /= 2;
                } else {
                    stringForCheck.append(s.charAt(position + i));
                }
            }

            if (findMaxPalindromLen(stringForCheck.toString()) < 5) {
                notPalindromStates.add(state);
            }
        }
        return notPalindromStates;
    }

    private static int countFreeCells(int position, String s) {
        int res = 0;
        for (int i = 0; i < Math.min(s.length(), MIN_LEN); i++) {
            if (s.charAt(i + position) == '?') {
                res++;
            }
        }
        return res;
    }

    private static Set<Integer> buildStates(int n) {
        Set<Integer> states = new HashSet<>();

        for (int i = 0; i < fastPow(2, n); i++) {
            states.add(i);
        }

        return states;
    }

    private static int fastPow(int n, int p) {
        if (p <= 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return fastPow(n * n, p / 2);
        } else {
            return n * fastPow(n, p - 1);
        }
    }
}
