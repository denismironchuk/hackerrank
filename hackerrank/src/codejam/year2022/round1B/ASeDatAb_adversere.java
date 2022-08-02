package codejam.year2022.round1B;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class ASeDatAb_adversere {
    private static class State {
        private int num;
        private int bitCnt;

        public State(int num, int bitCnt) {
            this.num = num;
            this.bitCnt = bitCnt;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return num == state.num && bitCnt == state.bitCnt;
        }

        @Override
        public int hashCode() {
            return Objects.hash(num, bitCnt);
        }

        @Override
        public String toString() {
            return "State{" +
                    "num=" + num +
                    ", bitCnt=" + bitCnt +
                    '}';
        }
    }

    private static final Map<State, List<State>> statesMap = new HashMap<>();
    private static final Map<Integer, String> statesMoves = new HashMap<>();

    static {
        statesMap.put(new State(8, 1), Arrays.asList(new State(7, 2), new State(6, 2), new State(4, 2)));
        statesMap.put(new State(8, 3), Arrays.asList(new State(7, 2), new State(7, 4), new State(6, 4),
                new State(5, 4), new State(3, 4), new State(2, 4), new State(6, 2), new State(4, 2)));

        statesMap.put(new State(7, 2), Arrays.asList(new State(6, 4), new State(5, 4), new State(3, 4),
                new State(2, 4), new State(6, 2), new State(4, 2)));
        statesMap.put(new State(7, 4), Arrays.asList(new State(6, 4), new State(5, 4), new State(3, 4),
                new State(2, 4), new State(6, 2), new State(4, 2)));

        statesMap.put(new State(6, 2), Arrays.asList(new State(5, 4), new State(4, 2), new State(3, 4), new State(2, 4)));
        statesMap.put(new State(6, 4), Arrays.asList(new State(5, 4), new State(4, 2), new State(3, 4), new State(2, 4)));

        statesMap.put(new State(5, 4), Arrays.asList(new State(4, 2), new State(3, 4), new State(2, 4)));

        statesMap.put(new State(4, 2), Arrays.asList(new State(3, 4), new State(2, 4)));
        statesMap.put(new State(3, 4), Arrays.asList(new State(2, 4)));

        statesMap.put(new State(2, 4), Arrays.asList(new State(1, 8)));

        statesMoves.put(8, "00000001");
        statesMoves.put(7, "00000011");
        statesMoves.put(6, "00000101");
        statesMoves.put(5, "00001111");
        statesMoves.put(4, "00010001");
        statesMoves.put(3, "00110011");
        statesMoves.put(2, "01010101");
        statesMoves.put(1, "00000000");
    }

    public static void main(String[] args) throws IOException {
        Random rnd = new Random();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                System.out.println("00000000");
                int bitCnt = Integer.parseInt(br.readLine());
                int move = 0;
                State state = null;
                while (true) {
                    if (move % 9 == 0) {
                        if (bitCnt % 2 == 0) {
                            if (bitCnt > 4) {
                                System.out.println("11111110");
                            } else {
                                System.out.println("00000001");
                            }
                            bitCnt = Integer.parseInt(br.readLine());
                        }
                        state = new State(8, bitCnt > 4 ? 8 - bitCnt : bitCnt);
                    }

                    System.out.println(bitCnt > 4 ? xor("11111111", statesMoves.get(state.num)) : statesMoves.get(state.num));
                    bitCnt = Integer.parseInt(br.readLine());

                    if (bitCnt == 0) {
                        break;
                    } else if (bitCnt == 8) {
                        System.out.println("11111111");
                        bitCnt = Integer.parseInt(br.readLine());
                        break;
                    }

                    List<State> nextStates = new ArrayList<>();
                    for (State st : statesMap.get(state)) {
                        if (st.bitCnt == bitCnt || 8 - st.bitCnt == bitCnt) {
                            nextStates.add(st);
                        }
                    }

                    if (nextStates.size() == 0) {
                        move = 0;
                        continue;
                    }

                    state = nextStates.get(rnd.nextInt(nextStates.size()));

                    move++;
                }
            }
        }
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
