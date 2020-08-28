package dynamicProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class HexagonalGrid {
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 0; t < T; t++) {
                int n = Integer.parseInt(br.readLine());
                String line1 = br.readLine();
                String line2 = br.readLine();

                long initialState = 0;
                int mul = 1;
                for (int i = 0; i < n; i++) {
                    if (line1.charAt(i) == '1') {
                        initialState += mul;
                    }
                    mul *= 2;
                    if (line2.charAt(i) == '1') {
                        initialState += mul;
                    }
                    mul *= 2;
                }

                Set<Long> states = new HashSet<>();
                states.add(initialState);
                int checkedRow = 0;

                while (checkedRow < n - 1) {
                    Iterator<Long> stateIterator = states.iterator();
                    Set<Long> newStates = new HashSet<>();

                    while (stateIterator.hasNext()) {
                        Long state = stateIterator.next();
                        stateIterator.remove();
                        if (state % 2 == 0) {
                            /**
                             * #.
                             * #.
                             */
                            if ((state / 4) % 2 == 0) {
                                newStates.add(state + 1 + 4);
                            }

                            /**
                             * ..
                             * ##
                             */
                            if ((state / 2) % 2 == 0) {
                                newStates.add(state + 1 + 2);
                            }
                        } else {
                            newStates.add(state);
                        }
                    }

                    Iterator<Long> newStatesIterator = newStates.iterator();

                    while (newStatesIterator.hasNext()) {
                        Long state = newStatesIterator.next();
                        if ((state / 2) % 2 == 0) {
                            /**
                             * .#
                             * .#
                             */
                            if ((state / 4) % 2 == 0) {
                                states.add((state + 2 + 4) / 4);
                            }

                            /**
                             * #.
                             * .#
                             */
                            if ((state / 8) % 2 == 0) {
                                states.add((state + 2 + 8) / 4);
                            }
                        } else {
                            states.add(state / 4);
                        }
                    }
                    checkedRow++;
                }

                if (states.contains(0l) || states.contains(3l)) {
                    System.out.println("YES");
                } else {
                    System.out.println("NO");
                }
            }
        }
    }
}
