package codejam.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;

public class DoubleOrNoting {

    private static int MAX_LEN = 0;

    private static class State {

        private int[] digitsCnt;
        private int firstDigit;
        private int digitsChanges;

        public State() {
            this.digitsCnt = new int[MAX_LEN + 1];
        }

        public State(String s) {
            this();
            int len = s.length();
            this.firstDigit = s.charAt(len - 1) == '1' ? 1 : 0;
            digitsChanges = 0;
            digitsCnt[digitsChanges] = 1;
            for (int i = len - 2; i >= 0; i--) {
                if (s.charAt(i) == s.charAt(i + 1)) {
                    digitsCnt[digitsChanges]++;
                } else {
                    digitsChanges++;
                    digitsCnt[digitsChanges] = 1;
                }
            }
            digitsChanges++;
        }

        public State not() {
            State newState = new State();
            newState.digitsChanges = this.digitsChanges - 1;
            newState.firstDigit = this.firstDigit == 1 ? 0 : 1;
            for (int i = 0; i < newState.digitsChanges; i++) {
                newState.digitsCnt[i] = this.digitsCnt[i];
            }
            return newState;
        }

        public State x2() {
            State newState = new State();
            newState.firstDigit = 0;
            if (this.firstDigit == 1) {
                newState.digitsChanges = this.digitsChanges + 1;
                newState.digitsCnt[0] = 1;
                for (int i = 0; i < this.digitsChanges; i++) {
                    newState.digitsCnt[i + 1] = this.digitsCnt[i];
                }
            } else {
                newState.digitsChanges = this.digitsChanges;
                newState.digitsCnt[0] = 1;
                for (int i = 0; i < newState.digitsChanges; i++) {
                    newState.digitsCnt[i] = this.digitsCnt[i];
                }
                newState.digitsCnt[0]++;
            }
            return newState;
        }

        public int getLen() {
            int len = 0;
            for (int i = 0; i < digitsChanges; i++) {
                len += digitsCnt[i];
            }
            return len;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return firstDigit == state.firstDigit &&
                    digitsChanges == state.digitsChanges &&
                    Arrays.equals(digitsCnt, state.digitsCnt);
        }

        public long customHashCode() {
            long result = Objects.hash(firstDigit, digitsChanges);
            result = 31 * result + hashCode(digitsCnt);
            return result;
        }

        public static long hashCode(int a[]) {
            if (a == null)
                return 0;

            long result = 1;
            for (int element : a)
                result = 31 * result + (long)element;

            return result;
        }

        @Override
        public String toString() {
            String res = "";
            int digit = firstDigit;
            for (int pos = 0; pos < digitsChanges; pos++) {
                for (int i = 0; i < digitsCnt[pos]; i++) {
                    res = digit + res;
                }
                digit = digit == 1 ? 0 : 1;
            }

            return res;
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                String start = tkn.nextToken();
                String end = tkn.nextToken();
                MAX_LEN = 2 * (start.length() + end.length());

                State startState = new State(start);
                State endState = new State(end);

                Set<Long> processedStates = new HashSet<>();
                List<State> statesToProcess = new ArrayList<>();
                statesToProcess.add(startState);
                processedStates.add(startState.customHashCode());
                int len = 0;
                boolean foundRes = false;
                Set<String> stringHashes = new HashSet<>();
                while (!foundRes && !statesToProcess.isEmpty()) {
                    List<State> nextStates = new ArrayList<>();
                    for (State stateToProcess : statesToProcess) {
                        if (stateToProcess.customHashCode() == endState.customHashCode()) {
                            foundRes = true;
                            break;
                        }
                        State notState = stateToProcess.not();
                        long hash1 = notState.customHashCode();
                        if (!processedStates.contains(hash1) && notState.getLen() <= MAX_LEN) {
                            nextStates.add(notState);
                            stringHashes.add(notState.toString());
                            //System.out.println(notState);
                        }
                        State x2State = stateToProcess.x2();
                        long hash2 = x2State.customHashCode();
                        if (!processedStates.contains(hash2) && x2State.getLen() <= MAX_LEN) {
                            nextStates.add(x2State);
                            stringHashes.add(x2State.toString());
                            //System.out.println(x2State);
                        }
                        processedStates.add(hash1);
                        processedStates.add(hash2);
                    }

                    //System.out.println(processedStates.size());

                    if (!foundRes) {
                        len++;
                        statesToProcess = nextStates;
                    }
                }

                if (foundRes) {
                    System.out.printf("Case #%s: %s\n", t, len);
                } else {
                    System.out.printf("Case #%s: IMPOSSIBLE\n", t);
                }
            }
        }
    }
}
