package codejam.year2020.worldFinal.adjancentAndConsecutive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class AdjancentAndConsecutive {
    private static int n;
    private static Map<Long, Boolean> states1;
    private static Map<Long, Map<Long, Boolean>> states1Opt;
    private static Map<Long, Boolean> states2;

    private static class GameState {
        private int[] state;
        private int freeCnt = 0;
        private int[] freePositions;
        private int[] freeValues;
        int[] usedVals = new int[n + 1];
        int usedValsCnt = 0;

        long[] optHashes = new long[2];

        public GameState(int[] state) {
            this.state = state;

            for (int posVal : this.state) {
                if (posVal == 0) {
                    freeCnt++;
                } else {
                    usedVals[posVal] = 1;
                    usedValsCnt++;
                }
            }
            freePositions = new int[freeCnt];
            freeValues = new int[freeCnt];

            int freePos = 0;
            for (int i = 0; i < n; i++) {
                if (this.state[i] == 0) {
                    freePositions[freePos] = i;
                    freePos++;
                }
            }
            freePos = 0;
            for (int i = 1; i <= n; i++) {
                if (usedVals[i] == 0) {
                    freeValues[freePos] = i;
                    freePos++;
                }
            }

            List<Integer> consPositionsLen = new ArrayList<>();
            int len = 1;
            for (int i = 1; i < freeCnt; i++) {
                if (freePositions[i] == freePositions[i - 1] + 1) {
                    len++;
                } else {
                    consPositionsLen.add(len);
                    len = 1;
                }
            }
            consPositionsLen.add(len);

            //approach from editorial
            List<Integer> consValsLen = new ArrayList<>();
            len = 1;
            for (int i = 1; i < freeCnt; i++) {
                if (freeValues[i] == freeValues[i - 1] + 1) {
                    len++;
                } else {
                    consValsLen.add(len);
                    len = 1;
                }
            }
            consValsLen.add(len);

            consPositionsLen.sort(Integer::compare);
            consValsLen.sort(Integer::compare);

            if (consPositionsLen.get(consPositionsLen.size() - 1) > 2) {
                List<Integer> tmp = consPositionsLen;
                consPositionsLen = consValsLen;
                consValsLen = tmp;
            }
            optHashes[0] = hashCode(consPositionsLen);
            optHashes[1] = hashCode(consValsLen);
        }

        public boolean isWinning(boolean forPlayer1) {
            //long hash = hashCode(this.state);
            boolean has2Cons = alreadyHas2Consecutive();
            if (forPlayer1) {
                /*if (states1.containsKey(hash)) {
                    return states1.get(hash);
                } else */if (usedValsCnt == n && !has2Cons) {
                    //states1.put(hash, false);
                    return false;
                } else if (has2Cons) {
                    //states1.put(hash, true);
                    return true;
                } else if (addOneTileToWin()) {
                    //states1.put(hash, true);
                    return true;
                } else if (has3AdjancentEmptyAnd3ConsecutiveFree()) {
                    //states1.put(hash, true);
                    return true;
                } else if (dontHave2AdjancentEmpty()) {
                    //states1.put(hash, false);
                    return false;
                } else if (dontHave2ConsecutiveFree()) {
                    //states1.put(hash, false);
                    return false;
                } else {
                    if (!states1Opt.containsKey(optHashes[0]) || !states1Opt.get(optHashes[0]).containsKey(optHashes[1])) {
                        boolean isWinning = false;
                        for (int i = 0; !isWinning && i < freeCnt; i++) {
                            for (int j = 0; !isWinning && j < freeCnt; j++) {
                                this.state[freePositions[i]] = freeValues[j];
                                long nextHash = hashCode(this.state);
                                boolean nextState;

                                if (states2.containsKey(nextHash)) {
                                    nextState = states2.get(nextHash);
                                } else {
                                    nextState = new GameState(state).isWinning(false);
                                }

                                this.state[freePositions[i]] = 0;

                                if (!nextState) {
                                    isWinning = true;
                                }
                            }
                        }
                        //states1.put(hash, isWinning);
                        if (!states1Opt.containsKey(optHashes[0])) {
                            states1Opt.put(optHashes[0], new HashMap<>());
                        }
                        states1Opt.get(optHashes[0]).put(optHashes[1], isWinning);
                    }
                    return states1Opt.get(optHashes[0]).get(optHashes[1]);
                }

                //return states1.get(hash);
            } else {
                /*if (states2.containsKey(hash)) {
                    return states2.get(hash);
                } else */if (usedValsCnt == n && !has2Cons) {
                    //states2.put(hash, true);
                    return true;
                } else if (has2Cons) {
                    //states2.put(hash, false);
                    return false;
                } else {
                    boolean isWinning = false;
                    for (int i = 0; !isWinning && i < freeCnt; i++) {
                        for (int j = 0; !isWinning && j < freeCnt; j++) {
                            this.state[freePositions[i]] = freeValues[j];
                            //long nextHash = hashCode(this.state);
                            boolean nextState = new GameState(state).isWinning(true);

                            this.state[freePositions[i]] = 0;

                            if (!nextState) {
                                isWinning = true;
                            }
                        }
                    }
                    //states2.put(hash, isWinning);
                    return isWinning;
                }

                //return states2.get(hash);
            }
        }

        private boolean dontHave2ConsecutiveFree() {
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

        private boolean dontHave2AdjancentEmpty() {
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

        private boolean has3AdjancentEmptyAnd3ConsecutiveFree() {
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

        private boolean addOneTileToWin() {
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

        private boolean alreadyHas2Consecutive() {
            for (int i = 0; i < n - 1; i++) {
                if (state[i] != 0 && state[i + 1] != 0 && Math.abs(state[i] - state[i + 1]) == 1) {
                    return true;
                }
            }
            return false;
        }

        public static long hashCode(List<Integer> a) {
            if (a == null)
                return 0;

            long result = 1;
            for (int element : a)
                result = 31 * result + element;

            return result;
        }

        public static long hashCode(int a[]) {
            if (a == null)
                return 0;

            long result = 1;
            for (int element : a)
                result = 31 * result + element;

            return result;
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                states1 = new HashMap<>();
                states1Opt = new HashMap<>();
                states2 = new HashMap<>();
                n = Integer.parseInt(br.readLine());
                int[] gameState = new int[n];
                boolean firstPlayer = true;
                boolean firstPlayerWinning = new GameState(gameState).isWinning(firstPlayer);
                int errors1 = 0;
                int errors2 = 0;
                for (int i = 0; i < n; i++) {
                    StringTokenizer move = new StringTokenizer(br.readLine());
                    int tile = Integer.parseInt(move.nextToken());
                    int pos = Integer.parseInt(move.nextToken()) - 1;
                    gameState[pos] = tile;
                    GameState nextGameState = new GameState(gameState);
                    if (firstPlayer) {
                        if (firstPlayerWinning && nextGameState.isWinning(!firstPlayer)) {
                            errors1++;
                            firstPlayerWinning = false;
                        }
                    } else {
                        if (!firstPlayerWinning && nextGameState.isWinning(!firstPlayer)) {
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
}
