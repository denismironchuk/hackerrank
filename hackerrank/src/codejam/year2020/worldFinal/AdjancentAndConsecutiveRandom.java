package codejam.year2020.worldFinal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class AdjancentAndConsecutiveRandom {

    public static void main(String[] args) throws IOException {
        int n = 7;
        PermutationsTest permTest = new PermutationsTest(n);
        permTest.initAllStates();

        while (true) {
            int[] gameState = new int[n];

            int presentTilesCnt = (int) (Math.random() * (n + 1));
            Set<Integer> usedTiles = new HashSet<>();

            while (presentTilesCnt != 0) {
                int pos = (int) (Math.random() * n);
                while (gameState[pos] != 0) {
                    pos = (int) (Math.random() * n);
                }
                int tile = 1 + (int) (Math.random() * n);
                while (usedTiles.contains(tile)) {
                    tile = 1 + (int) (Math.random() * n);
                }

                gameState[pos] = tile;
                usedTiles.add(tile);
                presentTilesCnt--;
            }

            boolean firstPlayer = Math.random() > 0.5;
            //int[] gameState = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
            //boolean firstPlayer = false;
            System.out.printf("%s %s\n", firstPlayer, Arrays.toString(gameState));
            boolean winningOptimal = isWinningState(gameState, n, firstPlayer, 1);
            boolean winningTrivial = permTest.isStateWinning(gameState, firstPlayer);
            if (winningOptimal != winningTrivial) {
                throw new RuntimeException();
            }
        }
    }

    private static boolean isWinningState(int[] state, int n, boolean firstPlayer, int depth) {
        int[] usedVals = new int[n + 1];
        int usedValsCnt = 0;
        for (int i = 0; i < n; i++) {
            if (state[i] != 0) {
                usedVals[state[i]] = 1;
                usedValsCnt++;
            }
        }
        boolean has2Cons = alreadyHas2Consecutive(state, n);

        if (firstPlayer) {
            //Case 0
            if (usedValsCnt == n && !has2Cons) {
                return false;
            }

            //Case 1
            if (has2Cons) {
                return true;
            }
            //Case 2
            if (addOneTileToWin(state, usedVals, n)) {
                return true;
            }

            //Case 3
            if (has3AdjancentEmptyAnd3ConsecutiveFree(state, usedVals, n)) {
                return true;
            }

            //Case 4
            if (dontHave2AdjancentEmpty(state, n)) {
                return false;
            }

            //Case 4
            if (dontHave2ConsecutiveFree(usedVals, n)) {
                return false;
            }

            if (depth < 3) {
                //Brootforce
                if (brootforce(state, n, firstPlayer, depth, usedVals)) {
                    return true;
                }
            }

            return false;
        } else {
            if (usedValsCnt == n && !has2Cons) {
                return true;
            }

            if (has2Cons) {
                return false;
            }

            if (depth < 4) {
                if (brootforce(state, n, firstPlayer, depth, usedVals)) {
                    return true;
                }
            }

            return false;
        }
    }

    private static boolean brootforce(int[] state, int n, boolean firstPlayer, int depth, int[] usedVals) {
        for (int pos = 0; pos < n; pos++) {
            if (state[pos] != 0) {
                continue;
            }

            for (int val = 1; val <= n; val++) {
                if (usedVals[val] != 0) {
                    continue;
                }

                state[pos] = val;
                boolean nextMoveState = isWinningState(state, n, !firstPlayer, depth + 1);
                state[pos] = 0;
                if (!nextMoveState) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean dontHave2ConsecutiveFree(int[] usedVals, int n) {
        boolean has2ConsecutiveFree = false;
        for (int i = 1; !has2ConsecutiveFree && i <= n - 1; i++) {
            if (usedVals[i] == 0 && usedVals[i + 1] == 0) {
                has2ConsecutiveFree = true;
            }
        }

        if (!has2ConsecutiveFree) {
            return true;
        }

        return false;
    }

    private static boolean dontHave2AdjancentEmpty(int[] state, int n) {
        boolean has2Empty = false;
        for (int i = 0; !has2Empty && i < n - 1; i++) {
            if (state[i] == 0 && state[i + 1] == 0) {
                has2Empty = true;
            }
        }

        if (!has2Empty) {
            return true;
        }

        return false;
    }

    private static boolean has3AdjancentEmptyAnd3ConsecutiveFree(int[] state, int[] usedVals, int n) {
        boolean has3Empty = false;
        for (int i = 0; !has3Empty && i < n - 2; i++) {
            if (state[i] == 0 && state[i + 1] == 0 && state[i + 2] == 0) {
                has3Empty = true;
            }
        }

        boolean has3Increasing = false;
        for (int i = 1; !has3Increasing && i <= n - 2; i++) {
            if (usedVals[i] == 0 && usedVals[i + 1] == 0 && usedVals[i + 2] == 0) {
                has3Increasing = true;
            }
        }

        if (has3Empty && has3Increasing) {
            return true;
        }
        return false;
    }

    private static boolean addOneTileToWin(int[] state, int[] usedVals, int n) {
        for (int i = 0; i < n - 1; i++) {
            if (state[i] != 0 && state[i + 1] == 0) {
                if (state[i] - 1 >= 1 && usedVals[state[i] - 1] == 0) {
                    return true;
                }

                if (state[i] + 1 <= n && usedVals[state[i] + 1] == 0) {
                    return true;
                }
            }

            if (state[i] == 0 && state[i + 1] != 0) {
                if (state[i + 1] - 1 >= 1 && usedVals[state[i + 1] - 1] == 0) {
                    return true;
                }

                if (state[i + 1] + 1 <= n && usedVals[state[i + 1] + 1] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean alreadyHas2Consecutive(int[] state, int n) {
        for (int i = 0; i < n - 1; i++) {
            if (state[i] != 0 && state[i + 1] != 0 && Math.abs(state[i] - state[i + 1]) == 1) {
                return true;
            }
        }
        return false;
    }
}
