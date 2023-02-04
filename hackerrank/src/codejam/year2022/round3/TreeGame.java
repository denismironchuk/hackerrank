package codejam.year2022.round3;

import cardsPermutations.SearchTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TreeGame {

    private static class State {
        private List<Integer> state;

        public State() {
            state = new ArrayList<>();
        }

        public State(List<Integer> state) {
            this.state = state;
        }

        public State(Integer state) {
            this.state = new ArrayList<>();
            this.state.add(state);
        }

        public void addBranchIfNotEmpty(Integer branchLen) {
            if (branchLen != 0) {
                state.add(branchLen);
            }
        }

        public long hashCodeLong() {
            long hashCode = 1;
            for (Integer e : state)
                hashCode = 97l * hashCode + (long)e;
            return hashCode;
        }

        public int getStateSize() {
            return state.size();
        }

        public int getLastBranch() {
            return state.get(state.size() - 1);
        }

        public boolean isLastBranch(int branch) {
            return branch == state.size() - 1;
        }

        public void rebuildIfLen2() {
            if (getStateSize() != 2) {
                return;
            }
            int newSize = state.get(0) + state.get(1);
            state.clear();
            state.add(newSize);
        }

        public void sort() {
            state.sort(Integer::compareTo);
        }

        public int getBranchLen(int branch) {
            return state.get(branch);
        }

        /**
         *
         * @param branch - position in state list
         * @param positionFromTop - position from root starting wit 0
         * @return new set of states
         */
        public List<State> removeItem(int branch, int positionFromTop) {
            List<State> res = new ArrayList<>();

            if (isLastBranch(branch) && positionFromTop == 0) {
                //remove root
                for (int i = 0; i < state.size() - 1; i++) {
                    res.add(new State(state.get(i)));
                }
                if (state.get(state.size() - 1) != 1) {
                    res.add(new State(state.get(state.size() - 1)));
                }
            } else {
                State newState1 = new State();
                for (int i = 0; i < state.size(); i++) {
                    if (branch == i && positionFromTop != state.get(i) - 1) {
                        res.add(new State(state.get(i) - positionFromTop - 1));
                        newState1.addBranchIfNotEmpty(positionFromTop);
                    } else {
                        newState1.addBranchIfNotEmpty(state.get(i));
                    }
                }
                newState1.rebuildIfLen2();
                newState1.sort();
                res.add(newState1);
            }

            return res;
        }
    }

    public static void main(String[] args) {
        List<State> statesUniverse = new ArrayList<>();
        int k = 5;
        for (int n = 40; n > 0; n--) {
            statesUniverse.addAll(buildSums(n, k, n).stream().filter(l -> l.size() != 2).map(State::new)
                    .collect(Collectors.toList()));
        }
        System.out.println(statesUniverse.size());
        Set<Long> hashes = new HashSet<>();
        statesUniverse.stream().map(State::hashCodeLong).forEach(hashes::add);
        System.out.println(hashes.size());

        for (State state : statesUniverse) {
            for (int branch = 0; branch < state.getStateSize(); branch++) {
                for (int positionFromTop = 0; positionFromTop < state.getBranchLen(branch); positionFromTop++) {
                    List<State> nextStatesRemove1 = state.removeItem(branch, positionFromTop);

                }
            }
        }
    }

    private static List<List<Integer>> buildSums(int n, int k, int lastSet) {
        if (n == 0) {
            List<List<Integer>> res = new ArrayList<>();
            res.add(new ArrayList<>());
            return res;
        }

        if (k == 0) {
            return new ArrayList<>();
        }

        List<List<Integer>> result = new ArrayList<>();

        for (int i = Math.min(lastSet, n); i > 0; i--) {
            List<List<Integer>> sums = buildSums(n - i, k - 1, i);
            for (List<Integer> sum : sums) {
                sum.add(i);
                result.add(sum);
            }
        }

        return result;
    }


}
