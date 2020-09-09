package dynamicProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class QueensOnBoard {
    private static final long MOD = 1000000000 + 7;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 0; t < T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                int m = Integer.parseInt(tkn1.nextToken());
                int[] blockedCells = new int[n];
                for (int i = 0; i < n; i++) {
                    String line = br.readLine();
                    int mul = 1;
                    for (int j = 0; j < m; j++) {
                        if (line.charAt(j) == '#') {
                            blockedCells[i] += mul;
                        }
                        mul *= 2;
                    }
                }

                int maxState = 1;
                for (int i = 0; i < m; i++) {
                    maxState *= 2;
                }

                Map<Integer, Set<Integer>> statesMap = new HashMap<>();
                Set<Integer> stateDescrInit = new HashSet<>();
                stateDescrInit.add(0);
                statesMap.put(0, stateDescrInit);

                Map<Integer, Long> descrCnt = new HashMap<>();
                descrCnt.put(0, 1l);

                for (int row = 0; row < n; row++) {
                    Map<Integer, Set<Integer>> nextStates = new HashMap<>();
                    Map<Integer, Long> nextDescrCnt = new HashMap<>();

                    for (Map.Entry<Integer, Set<Integer>> entry : statesMap.entrySet()) {
                        int state = entry.getKey();
                        Set<Integer> stateDescriptions = entry.getValue();

                        for (int newState = 0; newState < maxState; newState++) {
                            if (validateNewStateCandidate(newState, state, blockedCells[row], m)) {
                                for (Integer stateDescription : stateDescriptions) {
                                    int mul = 1;
                                    int mulDescr = 1;
                                    int newStateDescr = 0;
                                    for (int pos = 0; pos < m; pos++) {
                                        if ((newState / mul) % 2 == 1) {
                                            newStateDescr += mulDescr;
                                            newStateDescr += mulDescr * 16;
                                            newStateDescr += mulDescr / 2;
                                        } else if ((blockedCells[row] / mul % 2) == 0) {
                                            if ((stateDescription / mulDescr) % 2 == 1) {
                                                newStateDescr += mulDescr;
                                            }

                                            if ((stateDescription / mulDescr / 2) % 2 == 1) {
                                                newStateDescr += mulDescr * 16;
                                            }

                                            if ((stateDescription / mulDescr / 4) % 2 == 1) {
                                                newStateDescr += mulDescr / 2;
                                            }
                                        }

                                        mul *= 2;
                                        mulDescr *= 8;
                                    }

                                    long newCnt = (descrCnt.get(stateDescription) + nextDescrCnt.getOrDefault(newStateDescr, 0l)) % MOD;
                                    nextDescrCnt.put(newStateDescr, newCnt);

                                    int consState = consStateDescription(newStateDescr) % maxState;
                                    Set<Integer> newStateDescriptions = nextStates.get(consState);
                                    if (newStateDescriptions == null) {
                                        newStateDescriptions = new HashSet<>();
                                        nextStates.put(consState, newStateDescriptions);
                                    }
                                    newStateDescriptions.add(newStateDescr);
                                }
                            }
                        }
                    }
                    statesMap = nextStates;
                    descrCnt = nextDescrCnt;
                }
                long res = 0;

                for (long cnt : descrCnt.values()) {
                    res += cnt;
                    res %= MOD;
                }
                System.out.println((res - 1 + MOD) % MOD);
            }
        }
    }

    private static int consStateDescription(int stateDescr) {
        int state = 0;
        int stateMul = 1;

        while (stateDescr != 0) {
            if (stateDescr % 8 != 0) {
                state += stateMul;
            }
            stateDescr /= 8;
            stateMul *= 2;
        }

        return state;
    }

    private static boolean validateNewStateCandidate(int candidateState, int freeCells, int blockedCells, int m) {
        if ((candidateState & freeCells) != 0) {
            return false;
        }

        if ((candidateState & blockedCells) != 0) {
            return false;
        }

        boolean queenIsSet = false;

        for (int i = 0; i < m; i++) {
            if (candidateState % 2 == 1) {
                if (queenIsSet) {
                    return false;
                }

                queenIsSet = true;
            } else {
                if (blockedCells % 2 == 1) {
                    queenIsSet = false;
                }
            }

            candidateState /= 2;
            blockedCells /= 2;
        }

        return true;
    }
}
