package codejam.year2020.worldFinal.adjancentAndConsecutive;

import java.util.HashMap;
import java.util.Map;

public class GameTest {
    private static int n = 6;
    private static Map<Long, GameState> states = new HashMap<>();

    private static class GameState {
        private int[] state;
        private int freeCnt = 0;
        private int[] freePositions;
        private int[] freeValues;

        private boolean player1Wins = false;
        private boolean player2Wins = false;

        public GameState(int[] state) {
            this.state = state;
            int[] valUsage = new int[n + 1];
            for (int posVal : this.state) {
                if (posVal == 0) {
                    freeCnt++;
                } else {
                    valUsage[posVal] = 1;
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
                if (valUsage[i] == 0) {
                    freeValues[freePos] = i;
                    freePos++;
                }
            }
        }

        public void initWinning() {
            long hash = GameTest.hashCode(this.state);
            if (freeCnt == 0) {
                for (int i = 0; i < state.length - 1; i++) {
                    if (Math.abs(state[i] - state[i + 1]) == 1) {
                        this.player1Wins = true;
                    }
                }

                this.player2Wins = !this.player1Wins;
            } else {
                for (int i = 0; i < freeCnt; i++) {
                    for (int j = 0; j < freeCnt; j++) {
                        this.state[freePositions[i]] = freeValues[j];
                        long nextHash = GameTest.hashCode(this.state);
                        GameState nextState;

                        if (states.containsKey(nextHash)) {
                            nextState = states.get(nextHash);
                        } else {
                            nextState = new GameState(state);
                            nextState.initWinning();
                        }

                        if (!nextState.player2Wins) {
                            this.player1Wins = true;
                        }
                        if (!nextState.player1Wins) {
                            this.player2Wins = true;
                        }

                        this.state[freePositions[i]] = 0;
                    }
                }
            }
            states.put(hash, this);
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

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int[] state = new int[] {0, 2, 0, 0, 0, 0};
        GameState gameState = new GameState(state);
        gameState.initWinning();
        long end = System.currentTimeMillis();
        System.out.println(states.size());
        System.out.printf("Took %sms\n", end - start);

    }
}
