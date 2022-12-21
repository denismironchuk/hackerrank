package kickstart.year2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class PizzaDelivery {

    private static class Operation {
        private String op;
        private int k;

        public Operation(String inpt) {
            StringTokenizer tkn = new StringTokenizer(inpt);
            op = tkn.nextToken();
            k = Integer.parseInt(tkn.nextToken());
        }

        public Operation(String op, int k) {
            this.op = op;
            this.k = k;
        }

        public long calculate(long coin) {
            if (op.equals("+")) {
                return coin + k;
            } else if (op.equals("-")) {
                return coin - k;
            } else if (op.equals("*")) {
                return coin * k;
            } else {
                return Math.floorDiv(coin, k);
            }
        }
    }

    private static class Position {
        private int row;
        private int col;

        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return row == position.row && col == position.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    private static class State {
        private TreeSet<Integer> visitedCustomers = new TreeSet<>(Integer::compare);

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return Arrays.equals(visitedCustomers.toArray(new Integer[0]),
                    state.visitedCustomers.toArray(new Integer[0]));
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(visitedCustomers.toArray(new Integer[0]));
        }
    }

    private static int[][] customers;
    private static Map<State, Long>[][] states;
    private static int n;
    private static int p;
    private static long[] custCoins;
    private static Map<State, Long>[][] nextStates;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                n = Integer.parseInt(tkn1.nextToken());
                p = Integer.parseInt(tkn1.nextToken());
                int M = Integer.parseInt(tkn1.nextToken());
                int startRow = Integer.parseInt(tkn1.nextToken()) - 1;
                int startCol = Integer.parseInt(tkn1.nextToken()) - 1;
                Operation north = new Operation(br.readLine());
                Operation east = new Operation(br.readLine());
                Operation west = new Operation(br.readLine());
                Operation south = new Operation(br.readLine());
                int[] custRow = new int[p];
                int[] custCol = new int[p];
                custCoins = new long[p];
                customers = new int[n][n];
                for (int row = 0; row < n; row++) {
                    for (int col = 0; col < n; col++) {
                        customers[row][col] = -1;
                    }
                }
                for (int i = 0; i < p; i++) {
                    StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                    custRow[i] = Integer.parseInt(tkn2.nextToken()) - 1;
                    custCol[i] = Integer.parseInt(tkn2.nextToken()) - 1;
                    custCoins[i] = Integer.parseInt(tkn2.nextToken());
                    customers[custRow[i]][custCol[i]] = i;
                }
                states = new HashMap[n][n];
                for (int row = 0; row < n; row++) {
                    for (int col = 0; col < n; col++) {
                        states[row][col] = new HashMap<>();
                    }
                }
                Set<Position> curPositions = new HashSet<>();
                curPositions.add(new Position(startRow, startCol));
                states[startRow][startCol].put(new State(), 0l);
                for (int m = 0; m < M; m++) {
                    Set<Position> nextPositions = new HashSet<>();
                    nextStates = new HashMap[n][n];
                    for (int row = 0; row < n; row++) {
                        for (int col = 0; col < n; col++) {
                            nextStates[row][col] = new HashMap<>();
                        }
                    }
                    for (Position pos : curPositions) {
                        for (Map.Entry<State, Long> entry : states[pos.row][pos.col].entrySet()) {
                            State currState = entry.getKey();
                            Long coins = entry.getValue();
                            //North
                            if (pos.row > 0) {
                                Position nexPos = new Position(pos.row - 1, pos.col);
                                long nextCoins = north.calculate(coins);
                                move(currState, nexPos, nextCoins);
                                nextPositions.add(nexPos);
                            }

                            //East
                            if (pos.col < n - 1) {
                                Position nexPos = new Position(pos.row, pos.col + 1);
                                long nextCoins = east.calculate(coins);
                                move(currState, nexPos, nextCoins);
                                nextPositions.add(nexPos);
                            }

                            //South
                            if (pos.row < n - 1) {
                                Position nexPos = new Position(pos.row + 1, pos.col);
                                long nextCoins = south.calculate(coins);
                                move(currState, nexPos, nextCoins);
                                nextPositions.add(nexPos);
                            }

                            //West
                            if (pos.col > 0) {
                                Position nexPos = new Position(pos.row, pos.col - 1);
                                long nextCoins = west.calculate(coins);
                                move(currState, nexPos, nextCoins);
                                nextPositions.add(nexPos);
                            }

                            //stay in place
                            Position nexPos = new Position(pos.row, pos.col);
                            long nextCoins = coins;
                            move(currState, nexPos, nextCoins);
                            nextPositions.add(new Position(pos.row, pos.col));
                        }
                    }
                    curPositions = nextPositions;
                    states = nextStates;
                }
                boolean possible = false;
                long res = Long.MIN_VALUE;
                for (int row = 0; row < n; row++) {
                    for (int col = 0; col < n; col++) {
                        for (Map.Entry<State, Long> entry : states[row][col].entrySet()) {
                            if (entry.getKey().visitedCustomers.size() == p) {
                                possible = true;
                                if (entry.getValue() > res) {
                                    res = entry.getValue();
                                }
                            }
                        }
                    }
                }
                System.out.printf("Case #%s: %s\n", t, possible ? String.valueOf(res) : "IMPOSSIBLE");
            }
        }
    }

    private static void move(State currState, Position nexPos, long nextCoins) {
        if (customers[nexPos.row][nexPos.col] == -1) {
            long coins = nextCoins;
            State nextState = new State();
            nextState.visitedCustomers.addAll(currState.visitedCustomers);

            if (!nextStates[nexPos.row][nexPos.col].containsKey(nextState)
                    || nextStates[nexPos.row][nexPos.col].get(nextState) < coins) {
                nextStates[nexPos.row][nexPos.col].put(nextState, coins);
            }
        } else {
            //do not leave pizza
            long coins = nextCoins;
            State nextState = new State();
            nextState.visitedCustomers.addAll(currState.visitedCustomers);
            if (!nextStates[nexPos.row][nexPos.col].containsKey(nextState)
                    || nextStates[nexPos.row][nexPos.col].get(nextState) < coins) {
                nextStates[nexPos.row][nexPos.col].put(nextState, coins);
            }

            //leave pizza
            long coins1 = nextCoins;
            State nextState1 = new State();
            nextState1.visitedCustomers.addAll(currState.visitedCustomers);
            if (!nextState1.visitedCustomers.contains(customers[nexPos.row][nexPos.col])) {
                coins1 += custCoins[customers[nexPos.row][nexPos.col]];
            }
            nextState1.visitedCustomers.add(customers[nexPos.row][nexPos.col]);
            if (!nextStates[nexPos.row][nexPos.col].containsKey(nextState1)
                    || nextStates[nexPos.row][nexPos.col].get(nextState1) < coins1) {
                nextStates[nexPos.row][nexPos.col].put(nextState1, coins1);
            }
        }
    }
}
