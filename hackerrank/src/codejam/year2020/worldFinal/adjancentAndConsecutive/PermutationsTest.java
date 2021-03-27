package codejam.year2020.worldFinal.adjancentAndConsecutive;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PermutationsTest {
    private int n;
    private Map<Long, GameState> allStates = new HashMap<>();

    private class GameState {

        private int[] state;
        private boolean isTerminal;
        private boolean isWinning1 = false;
        private boolean isWinning2 = false;

        public GameState(int[] state, boolean isTerminal) {
            this.state = state;
            this.isTerminal = isTerminal;
            if (this.isTerminal) {
                for (int i = 0; i < state.length - 1; i++) {
                    if (Math.abs(state[i] - state[i + 1]) == 1) {
                        isWinning1 = true;
                    }
                }

                isWinning2 = !isWinning1;
            } else {
                int[] usedVals = new int[n + 1];
                for (int i = 0; i < n; i++) {
                    if (state[i] != 0) {
                        usedVals[state[i]] = 1;
                    }
                }

                for (int pos = 0; pos < n; pos++) {
                    if (this.state[pos] != 0) {
                        continue;
                    }

                    for (int val = 1; val <= n; val++) {
                        if (usedVals[val] != 0) {
                            continue;
                        }

                        state[pos] = val;
                        long hash = PermutationsTest.hashCode(state);
                        GameState nextState = allStates.get(hash);
                        if (!nextState.isWinning2) {
                            isWinning1 = true;
                        }
                        if (!nextState.isWinning1) {
                            isWinning2 = true;
                        }
                        state[pos] = 0;
                    }
                }
            }
        }

        public Set<Long> buildIngoingStates() {
            Set<Long> ingoingStates = new HashSet<>();
            for (int pos = 0; pos < n; pos++) {
                if (state[pos] != 0) {
                    int val = state[pos];
                    state[pos] = 0;
                    long hash = PermutationsTest.hashCode(state);
                    ingoingStates.add(hash);
                    if (!allStates.containsKey(hash)) {
                        allStates.put(hash, new GameState(Arrays.copyOf(state, n), false));
                    }
                    state[pos] = val;
                }
            }
            return ingoingStates;
        }

        @Override
        public String toString() {
            return Arrays.toString(state);
        }
    }

    public static long hashCode(int a[]) {
        if (a == null)
            return 0;

        long result = 1;
        for (int element : a)
            result = 31 * result + element;

        return result;
    }

    public PermutationsTest(int n) {
        this.n = n;
    }

    public void initAllStates() {
        Set<Long> curStepStates = new HashSet<>();
        buildAllTerminationStates(new int[n], new int[n + 1], 0, n, curStepStates);
        System.out.println(curStepStates.size());
        for (int step = n; step > 0; step--) {
            Set<Long> prevStates = new HashSet<>();
            for (long stateHash : curStepStates) {
                GameState state = allStates.get(stateHash);
                prevStates.addAll(state.buildIngoingStates());
            }
            curStepStates = prevStates;
            System.out.println(curStepStates.size());
        }
        System.out.println(allStates.size());
    }

    public static void main(String[] args) {
        new PermutationsTest(9).initAllStates();
    }

    private void buildAllTerminationStates(int[] perm, int[] usedVals, int pos, int n, Set<Long> terminationStates) {
        if (pos == n) {
            long hash = hashCode(perm);
            terminationStates.add(hash);
            allStates.put(hash, new GameState(Arrays.copyOf(perm, n), true));
            return;
        }

        for (int val = 1; val <= n; val++) {
            if (usedVals[val] == 0) {
                perm[pos] = val;
                usedVals[val] = 1;
                buildAllTerminationStates(perm, usedVals, pos + 1, n, terminationStates);
                perm[pos] = 0;
                usedVals[val] = 0;
            }
        }
    }

    public boolean isStateWinning(int[] state, boolean firstPlayer) {
        GameState gameState = allStates.get(hashCode(state));
        if (firstPlayer) {
            return gameState.isWinning1;
        } else {
            return gameState.isWinning2;
        }
    }
}
