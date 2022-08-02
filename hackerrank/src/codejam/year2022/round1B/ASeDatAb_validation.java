package codejam.year2022.round1B;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ASeDatAb_validation {
    private static final String[][] STATES = {
            {"11111111"},
            {"01010101"},
            {"00110011"},
            {"01110111", "00010001"},
            {"00001111", "01001011"},
            {"00000101", "00011011", "01011111", "00100111"},
            {"00101011", "01101111", "00000011", "00001001", "00010111", "01000111", "01010011", "00111111"},
            {"00101111", "00000001", "00000111", "01000011", "00110111", "01001111", "01111111", "00010011", "00100011", "00011111", "01100111", "00010101", "00001011", "01010111", "00100101", "01011011"}
    };

    private static final String[] MOVES = {
            "11111111",
            "01010101",
            "00110011",
            "00010001",
            "00001111",
            "00000101",
            "00000011",
            "00000001"};

    public static void main(String[] args) {
        Map<String, String> rotationSegmentation = new HashMap<>();
        rotationSegmentation.put("00000000", "00000000");
        Map<String, Integer> stateLevel = new HashMap<>();
        stateLevel.put("00000000", -1);
        for (int i = 0; i < STATES.length; i++) {
            for (String state: STATES[i]) {
                stateLevel.put(state, i);
                for (int r = 0; r < 9; r++) {
                    String rotation = rotate(state, r);
                    if (!rotationSegmentation.containsKey(rotation)) {
                        rotationSegmentation.put(rotation, state);
                    }
                }
            }
        }

        for (Map.Entry<String, Integer> entry : stateLevel.entrySet()) {
            String state = entry.getKey();
            Integer level = entry.getValue();

            if (level == -1) {
                continue;
            }

            String move = MOVES[level];
            for (int r = 0; r < 9; r++) {
                String nextState = rotationSegmentation.get(xor(state, rotate(move, r)));
                if (stateLevel.get(nextState) + 1 != level) {
                    throw new RuntimeException();
                }
            }
        }
    }

    private static String buildBinView(int num) {
        String res = "";
        for (int i = 0; i < 8; i++) {
            res = (num % 2 == 0 ? "0" : "1") + res;
            num /= 2;
        }
        return res;
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
