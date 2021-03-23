package codejam.year2020.worldFinal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class AdjancentAndConsecutive {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int n = Integer.parseInt(br.readLine());
                int[] gameState = new int[n];
                boolean firstPlayer = true;
                boolean firstPlayerWinning = isWinningState(gameState, n, firstPlayer, 1);
                int errors1 = 0;
                int errors2 = 0;
                for (int i = 0; i < n; i++) {
                    StringTokenizer move = new StringTokenizer(br.readLine());
                    int tile = Integer.parseInt(move.nextToken());
                    int pos = Integer.parseInt(move.nextToken()) - 1;
                    gameState[pos] = tile;

                    if (firstPlayer) {
                        if (firstPlayerWinning && isWinningState(gameState, n, !firstPlayer, 1)) {
                            errors1++;
                            firstPlayerWinning = false;
                        }
                    } else {
                        if (!firstPlayerWinning && isWinningState(gameState, n, !firstPlayer, 1)) {
                            errors2++;
                            firstPlayerWinning = true;
                        }
                    }

                    firstPlayer = !firstPlayer;
                }
                System.out.printf("Case #%s: %s %s\n", t, errors1, errors2);
            }
        }
    }

    private static boolean isWinningState(int[] state, int n, boolean firstPlayer, int depth) {
        int[] usedVals = new int[n + 1];
        for (int i = 0; i < n; i++) {
            if (state[i] != 0) {
                usedVals[state[i]] = 1;
            }
        }

        if (firstPlayer) {
            //Case 1
            if (alreadyHas2Consecutive(state, n)) {
                return true;
            }

            if (depth < 3) {
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

                //Brootforce
                if (brootforce(state, n, firstPlayer, depth, usedVals)) {
                    return true;
                }
            }

            return false;
        } else {
            if (alreadyHas2Consecutive(state, n)) {
                return false;
            }

            if (depth < 3) {
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
            return false;
        }

        return true;
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
