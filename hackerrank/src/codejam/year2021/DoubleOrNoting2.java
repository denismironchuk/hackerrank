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

public class DoubleOrNoting2 {
    private static int MAX_LEN = 0;

    private static class State {
        private static final long PT_LEN = 64;

        private long[] parts = new long[4];
        private long len;

        public State() {
        }

        public State(String s) {
            this.len = s.length();
            long point = 1;
            int part = -1;

            s = reverseString(s);

            for (int i = 0; i < len; i++) {
                if (i % PT_LEN == 0) {
                    part++;
                    point = 1;
                }
                if (s.charAt(i) == '1') {
                    parts[part] |= point;
                }
                point <<= 1;
            }
        }

        private String reverseString(String s) {
            StringBuilder reversed = new StringBuilder();
            for (int i = s.length() - 1; i >= 0; i--) {
                reversed.append(s.charAt(i));
            }
            return reversed.toString();
        }

        public State x2() {
            State newState = new State();
            newState.len = this.len + 1;
            newState.parts[0] = this.parts[0];
            newState.parts[1] = this.parts[1];
            newState.parts[2] = this.parts[2];
            newState.parts[3] = this.parts[3];

            newState.parts[3] <<= 1;
            if ((newState.parts[2] & (1 << PT_LEN - 1)) != 0) {
                newState.parts[3] |= 1;
            }

            newState.parts[2] <<= 1;
            if ((newState.parts[1] & (1 << PT_LEN - 1)) != 0) {
                newState.parts[2] |= 1;
            }

            newState.parts[1] <<= 1;
            if ((newState.parts[0] & (1 << PT_LEN - 1)) != 0) {
                newState.parts[1] |= 1;
            }

            newState.parts[0] <<= 1;

            return newState;
        }

        public State not() {
            State newState = new State();
            newState.len = this.len;
            newState.parts[0] = ~this.parts[0];
            newState.parts[1] = ~this.parts[1];
            newState.parts[2] = ~this.parts[2];
            newState.parts[3] = ~this.parts[3];
            newState.correctLen();
            return newState;
        }

        private void correctLen() {
            if (len <= PT_LEN) {
                long point = 1l << (len - 1);
                while ((parts[0] & point) == 0 && len > 1) {
                    len--;
                    point >>= 1l;
                }
            } else if (len <= 2 * PT_LEN) {
                long point = 1l << (len - PT_LEN - 1);
                while ((parts[1] & point) == 0 && len > PT_LEN) {
                    len--;
                    point >>= 1l;
                }
                if (len == PT_LEN) {
                    correctLen();
                }
            } else if (len <= 3 * PT_LEN) {
                long point = 1l << (len - 2 * PT_LEN - 1);
                while ((parts[2] & point) == 0 && len > 2 * PT_LEN) {
                    len--;
                    point >>= 1l;
                }
                if (len == 2 * PT_LEN) {
                    correctLen();
                }
            } else if (len <= 4 * PT_LEN) {
                long point = 1l << (len - 3 * PT_LEN - 1);
                while ((parts[3] & point) == 0 && len > 3 * PT_LEN) {
                    len--;
                    point >>= 1l;
                }
                if (len == 3 * PT_LEN) {
                    correctLen();
                }
            }
        }

        @Override
        public String toString() {
            String s = toBinaryString(parts[3]) + toBinaryString(parts[2]) + toBinaryString(parts[1]) + toBinaryString(parts[0]);
            s = s.substring((int)(4 * PT_LEN - len));
            return s;
        }

        private String toBinaryString(long l) {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < PT_LEN; i++) {
                b.append(l & 1);
                l >>= 1;
            }
            return reverseString(b.toString());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return toString().equals(state.toString());
            /*return len == state.len &&
                    Arrays.equals(parts, state.parts);*/
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
            /*int result = Objects.hash(len);
            result = 31 * result + Arrays.hashCode(parts);
            return result;*/
        }
    }

    public static void main(String[] args) throws IOException {
        int i = 1;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                String start = tkn.nextToken();
                String end = tkn.nextToken();

                MAX_LEN = start.length() + end.length();

                State startState = new State(start);
                State endState = new State(end);

                Set<State> processedStates = new HashSet<>();
                List<State> statesToProcess = new ArrayList<>();
                statesToProcess.add(startState);
                processedStates.add(startState);
                while (!statesToProcess.isEmpty()) {
                    List<State> nextStates = new ArrayList<>();
                    for (State stateToProcess : statesToProcess) {
                        State notState = stateToProcess.not();
                        if (!processedStates.contains(notState) && notState.len <= MAX_LEN) {
                            nextStates.add(notState);
                            processedStates.add(notState);
                        }
                        State x2State = stateToProcess.x2();
                        if (!processedStates.contains(x2State) && x2State.len <= MAX_LEN) {
                            nextStates.add(x2State);
                            processedStates.add(x2State);
                        }
                    }
                    System.out.println(processedStates.size());
                    statesToProcess = nextStates;
                }
            }
        }
    }
}
