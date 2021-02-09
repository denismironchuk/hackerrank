package kickstart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class Yeetzhee {
    private static int diceSides;

    private static class State {
        private int[] state;
        private double stateProbability = -1;
        private double expectedRolls = -1;
        private int usedSides = 0;
        private double averageStayTime = 0;

        private Set<State> ingoingStates = new HashSet<>();
        private Set<State> outgoingStates = new HashSet<>();

        private Map<State, Double> outgoingProb = new HashMap<>();

        public State(int[] state) {
            this.state = state;
            for (int i = 1; i < state.length; i++) {
                this.usedSides += state[i];
            }
        }

        public List<State> buildIngoingStates() {
            List<State> ingoingStates = new ArrayList<>();

            for (int grpCnt = 1; grpCnt < state.length; grpCnt++) {
                if (state[grpCnt] > 0) {
                    int[] newState = Arrays.copyOf(state, state.length);
                    newState[grpCnt]--;
                    newState[grpCnt - 1]++;
                    if (grpCnt > 1) {
                        newState[0]++;
                    }
                    ingoingStates.add(new State(newState));
                }
            }

            return ingoingStates;
        }

        public static State buildState(int[] groups) {
            int stateLen = groups[groups.length - 1] + 1;
            int[] state = new int[stateLen];
            for (int group : groups) {
                if (group != 0) {
                    state[group]++;
                }
            }
            return new State(state);
        }

        public double countTransitionProbability(State nextState) {
            for (int i = 1; i < state.length; i++) {
                if (nextState.state[i] == state[i] + 1) {
                    if (i == 1) {
                        return 1 - ((double) usedSides / (double) diceSides);
                    } else {
                        return (double) state[i - 1] / (double) diceSides;
                    }
                }
            }
            return -1;
        }

        public void initOutgoingProb() {
            double probSum = 0;
            for (State outState : outgoingStates) {
                double outProb = countTransitionProbability(outState);
                outgoingProb.put(outState, outProb);
                probSum += outProb;
            }

            averageStayTime = (1 - probSum) / probSum;

            for (State outState : outgoingStates) {
                double outProb = outgoingProb.get(outState);
                outgoingProb.put(outState, outProb / probSum);
            }
        }

        public double getExpectedRolls() {
            return expectedRolls;
        }

        public void setExpectedRolls(double expectedRolls) {
            this.expectedRolls = expectedRolls;
        }

        public double getStateProbability() {
            return stateProbability;
        }

        public void setStateProbability(double stateProbability) {
            this.stateProbability = stateProbability;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state1 = (State) o;
            return Arrays.equals(state, state1.state);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(state);
        }

        @Override
        public String toString() {
            return "State{" +
                    "state=" + Arrays.toString(state) +
                    ", stateProbability=" + stateProbability +
                    '}';
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken()); //dice count
                diceSides = Integer.parseInt(tkn1.nextToken()); //dice sides count
                int k = Integer.parseInt(tkn1.nextToken()); //groups count
                int[] groups = new int[k + 1];
                for (int i = 1; i <= k; i++) {
                    groups[i] = Integer.parseInt(br.readLine());
                }

                State finalState = State.buildState(groups);
                Map<State, State> allValidStates = new HashMap();
                buildAllValidStates(finalState, allValidStates);
                for (State state : allValidStates.values()) {
                    state.initOutgoingProb();
                }
                countStateProbability(finalState);
                countExpectedRolls(finalState);
                System.out.printf("Case #%s: %.6f\n", t, finalState.expectedRolls);
            }
        }
    }

    private static void buildAllValidStates(State state, Map<State, State> allValidStates) {
        for (State ingoingState : state.buildIngoingStates()) {
            if (!allValidStates.containsKey(ingoingState)) {
                state.ingoingStates.add(ingoingState);
                ingoingState.outgoingStates.add(state);

                allValidStates.put(ingoingState, ingoingState);
                buildAllValidStates(ingoingState, allValidStates);
            } else {
                State cachedIngState = allValidStates.get(ingoingState);
                state.ingoingStates.add(cachedIngState);
                cachedIngState.outgoingStates.add(state);
            }
        }
    }

    private static Double countStateProbability(State state) {
        if (state.getStateProbability() != -1) {
            return state.getStateProbability();
        }

        if (state.ingoingStates.isEmpty()) {
            state.setStateProbability(1);
            return state.getStateProbability();
        }

        double prob = 0;
        for (State ingoing : state.ingoingStates) {
            prob += countStateProbability(ingoing) * ingoing.outgoingProb.get(state);
        }
        state.setStateProbability(prob);

        return state.getStateProbability();
    }

    private static Double countExpectedRolls(State state) {
        if (state.getExpectedRolls() != -1) {
            return state.getExpectedRolls();
        }

        if (state.ingoingStates.isEmpty()) {
            state.setExpectedRolls(0);
            return state.getExpectedRolls();
        }

        double expectedRolls = 0;
        double sumForNormalize = 0;

        for (State ingoing : state.ingoingStates) {
            double prb = ingoing.getStateProbability() * ingoing.outgoingProb.get(state);
            sumForNormalize += prb;
            expectedRolls += prb * (countExpectedRolls(ingoing) + ingoing.averageStayTime + 1);
        }

        expectedRolls /= sumForNormalize;

        state.setExpectedRolls(expectedRolls);

        return state.getExpectedRolls();
    }
}
