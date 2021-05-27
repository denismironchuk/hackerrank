package codejam.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class DoubleOrNoting3 {

    public static int increaseFirstPosition(LinkedList<Integer> state, int expectedValueInFirst, int finalStateLen) {
        if (state.getFirst() == expectedValueInFirst) {
            return 0;
        }

        if (state.getFirst() > expectedValueInFirst) {
            return -1;
        }

        if (state.size() % 2 == 0) {
            if (finalStateLen > state.size()) {
                return -1;
            }
            int first = state.pollFirst();
            state.addFirst(expectedValueInFirst);
            return expectedValueInFirst - first;
        } else {
            if (finalStateLen >= state.size()) {
                return -1;
            }
            state.removeLast();
            int first = state.pollFirst();
            state.addFirst(expectedValueInFirst);
            return expectedValueInFirst - first + 1;
        }
    }

    public static int insertNewFirstElement(LinkedList<Integer> state, int insertedValue, int finalStateLen) {
        if (state.size() % 2 == 0) {
            if (finalStateLen > state.size()) {
                return -1;
            }
            state.removeLast();
            state.addFirst(insertedValue);
            return insertedValue + 1;
        } else {
            if (finalStateLen > state.size() + 1) {
                return -1;
            }
            state.addFirst(insertedValue);
            return insertedValue;
        }
    }

    private static LinkedList<Integer> convertToList(String state) {
        LinkedList<Integer> res = new LinkedList<>();
        int cnt = 1;
        for (int pos = state.length() - 2; pos >= 0; pos--) {
            if (state.charAt(pos) != state.charAt(pos + 1)) {
                res.addLast(cnt);
                cnt = 1;
            } else {
                cnt++;
            }
        }
        res.addLast(cnt);
        return res;
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                String start = tkn.nextToken();
                String end = tkn.nextToken();

                LinkedList<Integer> startState = convertToList(start);
                LinkedList<Integer> endState = convertToList(end);

                if (end.equals("0")) {
                    if (start.equals("0")) {
                        System.out.printf("Case #%s: 0\n", t);
                        continue;
                    } else {
                        System.out.printf("Case #%s: %s\n", t, startState.size());
                        continue;
                    }
                }

                int addition = 0;

                if (start.equals("0")) {
                    if (end.equals("0")) {
                        System.out.printf("Case #%s: 0\n", t);
                        continue;
                    } else {
                        addition = 1;
                    }
                }

                if (endState.size() % 2 == 0) {
                    if (startState.size() < endState.size() - 1) {
                        System.out.printf("Case #%s: IMPOSSIBLE\n", t);
                        continue;
                    }
                } else {
                    if (startState.size() < endState.size()) {
                        System.out.printf("Case #%s: IMPOSSIBLE\n", t);
                        continue;
                    }
                }

                List<Integer> candidates = new ArrayList<>();

                tryAllCombinations(startState, endState, candidates, true);
                tryAllCombinations(startState, endState, candidates, false);

                int res = candidates.stream().sorted().findFirst().get() + addition;
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }

    private static void tryAllCombinations(LinkedList<Integer> startState, LinkedList<Integer> endState, List<Integer> candidates, boolean firstInsert) {
        int endSize = endState.size();
        for (int itemsToInsert = 0; itemsToInsert <= endSize; itemsToInsert++) {
            LinkedList<Integer> startStateClone = (LinkedList<Integer>) startState.clone();
            int moves = 0;
            if (itemsToInsert != 0) {
                int movesToAdd = firstInsert ? insertNewFirstElement(startStateClone, endState.get(itemsToInsert - 1), endSize) :
                        increaseFirstPosition(startStateClone, endState.get(itemsToInsert - 1), endSize);
                if (movesToAdd == -1) {
                    continue;
                }
                moves += movesToAdd;
            }
            boolean canReach = true;
            for (int i = 1; i < itemsToInsert; i++) {
                int movesToAdd = insertNewFirstElement(startStateClone, endState.get(itemsToInsert - i - 1), endSize);
                if (movesToAdd == -1) {
                    canReach = false;
                    break;
                } else {
                    moves += movesToAdd;
                }
            }

            if (!canReach) {
                continue;
            }

            if (startStateClone.size() > endSize) {
                moves += startStateClone.size() - endSize;
            }

            boolean reachedEndState = true;
            LinkedList<Integer> endStateClone = (LinkedList<Integer>) endState.clone();
            while (reachedEndState && !endStateClone.isEmpty()) {
                int e1 = startStateClone.pollFirst();
                int e2 = endStateClone.pollFirst();
                if (e1 != e2) {
                    reachedEndState = false;
                }
            }
            if (reachedEndState) {
                candidates.add(moves);
            }
        }
    }
}
