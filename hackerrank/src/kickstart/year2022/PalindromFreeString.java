package kickstart.year2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PalindromFreeString {

    public static final int MIN_LEN = 5;

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

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int n = Integer.parseInt(br.readLine());
                String s = br.readLine();
                //String s = buildRandomString(15);
                //System.out.println(s);

                boolean isPalindromFree = isPalindromFree(s);

                System.out.printf("Case #%s: %s\n", t, isPalindromFree ? "POSSIBLE" : "IMPOSSIBLE");
            }
        }
    }

    private static boolean isPalindromFree(String s) {
        int freeCells = countFreeCells(0, s);
        Set<Integer> states = buildStates(freeCells);
        boolean isPalindromFree = false;
        if (freeCells == 0) {
            isPalindromFree = !isPalindrom(s.substring(0, Math.min(MIN_LEN, s.length())));
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
                isPalindromFree = !isPalindrom(s.substring(i, i + MIN_LEN));
            } else {
                states = getNonPalindromStates(i, s, freeCells, states);
                isPalindromFree = states.size() != 0;
            }
        }
        return isPalindromFree;
    }

    private static Set<Integer> getNonPalindromStates(int position, String s, int freeCells, Set<Integer> states) {
        /*if (freeCells == 0) {
            return !isPalindrom(s.substring(position, MIN_LEN));
        }*/

        Set<Integer> notPalindromStates = new HashSet<>();

        for (Integer state : states) {
            Integer _state = state;
            StringBuilder stringForCheck = new StringBuilder();
            int div = fastPow(2, freeCells - 1);
            for (int i = 0; i < Math.min(s.length(),MIN_LEN); i++) {
                if (s.charAt(position + i) == '?') {
                    stringForCheck.append(_state / div);
                    _state %= div;
                    div /= 2;
                } else {
                    stringForCheck.append(s.charAt(position + i));
                }
            }

            if (!isPalindrom(stringForCheck.toString())) {
                notPalindromStates.add(state);
            }
        }
        return notPalindromStates;
    }

    private static boolean isPalindrom(String s) {
        boolean isPalindrom = true;
        for (int i = 0; isPalindrom && i < s.length() / 2; i++) {
            isPalindrom = s.charAt(i) == s.charAt(s.length() - 1 - i);
        }
        return isPalindrom;
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
