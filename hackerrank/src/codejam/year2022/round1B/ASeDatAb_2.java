package codejam.year2022.round1B;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ASeDatAb_2 {
    public static void main(String[] args) {
        Map<String, String> rotationSegmentation = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            String currState = buildBinView(i);
            for (int r = 0; r < 9; r++) {
                String rotation = rotate(currState, r);
                if (!rotationSegmentation.containsKey(rotation)) {
                    rotationSegmentation.put(rotation, currState);
                }
            }
        }

        Set<String> uniqueStates = new HashSet<>(rotationSegmentation.values());

        Set<String> winStates = new HashSet<>();
        winStates.add("00000000");

        while (winStates.size() != 36) {
            Set<String> newWinStates = new HashSet<>();
            Set<String> winMoves = new HashSet<>();
            for (int i = 1; i < 256; i++) {
                String currState = buildBinView(i);
                if (winStates.contains(rotationSegmentation.get(currState))) {
                    continue;
                }
                boolean isWinning = false;
                for (int j = 1; j < 256; j++) {
                    isWinning = true;
                    String myMove = buildBinView(j);
                    for (int r = 0; isWinning && r < 9; r++) {
                        isWinning = winStates.contains(rotationSegmentation.get(xor(currState, rotate(myMove, r))));
                    }
                    if (isWinning) {
                        winMoves.add(rotationSegmentation.get(myMove));
                        newWinStates.add(rotationSegmentation.get(currState));
                    }
                }

                /*if (isWinning) {
                    newWinStates.add(rotationSegmentation.get(currState));
                }*/
            }

            System.out.println("Win moves - " + winMoves);

            winStates.addAll(newWinStates);
            System.out.println(newWinStates);
        }

        /*Set<String> winStates = new HashSet<>();
        winStates.add("00000000");

        while (winStates.size() != 256) {
            Set<String> newWinStates = new HashSet<>();
            for (int i = 1; i < 256; i++) {
                String currState = buildBinView(i);
                if (winStates.contains(currState)) {
                    continue;
                }
                boolean isWinning = false;
                for (int j = 1; !isWinning && j < 256; j++) {
                    isWinning = true;
                    String myMove = buildBinView(j);
                    for (int r = 0; isWinning && r < 9; r++) {
                        isWinning = winStates.contains(xor(currState, rotate(myMove, r)));
                    }
                }

                if (isWinning) {
                    newWinStates.add(currState);
                }
            }

            winStates.addAll(newWinStates);
            System.out.println(newWinStates);
        }*/
    }

    private static Set<String> buildPossibilities(int n) {
        Set<String> res = new HashSet<>();
        for (int i = 0; i < 255; i++) {
            if (countBits(i) == n) {
                res.add(buildBinView(i));
            }
        }
        return res;
    }

    private static String buildBinView(int num) {
        String res = "";
        for (int i = 0; i < 8; i++) {
            res = (num % 2 == 0 ? "0" : "1") + res;
            num /= 2;
        }
        return res;
    }

    private static int countBits(int num) {
        int bitsCnt = 0;
        while (num != 0) {
            bitsCnt += num % 2;
            num /= 2;
        }
        return bitsCnt;
    }

    private static int bitsCount(String s) {
        int res = 0;
        for (char c : s.toCharArray()) {
            if (c == '1') {
                res += 1;
            }
        }
        return res;
    }

    private static String rotate(String s, int r) {
        char[] sArr = s.toCharArray();
        char[] newArr = new char[sArr.length];
        for (int i = 0; i < sArr.length; i++) {
            newArr[i] = sArr[(i + r) % 8];
        }
        return new String(newArr);
    }

    private static String xor(String s1, String s2) {
        char[] s1Arr = s1.toCharArray();
        char[] s2Arr = s2.toCharArray();
        char[] newArr = new char[s1Arr.length];
        for (int i = 0; i < s1Arr.length; i++) {
            int val1 = s1Arr[i] == '1' ? 1 : 0;
            int val2 = s2Arr[i] == '1' ? 1 : 0;
            newArr[i] = (val1 ^ val2) == 1 ? '1' : '0';
        }
        return new String(newArr);
    }
}
