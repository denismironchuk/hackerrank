package dynamicProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class BrickTiling {
    private static abstract class BaseTile {
        public abstract int getBottomLen();
        public abstract int[] getUpPositionMasks(int bottomRightPos, int columns);
        public abstract int getStartIndex();
        public abstract int getEndIndex(int cols);
    }

    private static class Tile1 extends BaseTile {
        /*.....
          ...#.
          .###.*/

        @Override
        public int getBottomLen() {
            return 3;
        }

        @Override
        public int[] getUpPositionMasks(int bottomRightPos, int columns) {
            return new int[] {
                    1 << (bottomRightPos + columns),
                    1 << (bottomRightPos),
                    1 << (bottomRightPos - 1),
                    1 << (bottomRightPos - 2),
            };
        }

        @Override
        public int getStartIndex() {
            return 2;
        }

        @Override
        public int getEndIndex(int cols) {
            return cols - 1;
        }
    }

    private static class Tile2 extends BaseTile {
        /*.....
          .#...
          .###.*/

        @Override
        public int getBottomLen() {
            return 3;
        }

        @Override
        public int[] getUpPositionMasks(int bottomRightPos, int columns) {
            return new int[] {
                    1 << (bottomRightPos + columns - 2),
                    1 << (bottomRightPos),
                    1 << (bottomRightPos - 1),
                    1 << (bottomRightPos - 2)
            };
        }

        @Override
        public int getStartIndex() {
            return 2;
        }

        @Override
        public int getEndIndex(int cols) {
            return cols - 1;
        }
    }

    private static class Tile3 extends BaseTile {
        /*.##..
          .#...
          .#...*/

        @Override
        public int getBottomLen() {
            return 1;
        }

        @Override
        public int[] getUpPositionMasks(int bottomRightPos, int columns) {
            return new int[] {
                    1 << (bottomRightPos + columns),
                    1 << (bottomRightPos + 2 * columns),
                    1 << (bottomRightPos + 2 * columns + 1),
                    1 << (bottomRightPos)
            };
        }

        @Override
        public int getStartIndex() {
            return 0;
        }

        @Override
        public int getEndIndex(int cols) {
            return cols - 2;
        }
    }

    private static class Tile4 extends BaseTile {
        /*.##..
          ..#..
          ..#..*/

        @Override
        public int getBottomLen() {
            return 1;
        }

        @Override
        public int[] getUpPositionMasks(int bottomRightPos, int columns) {
            return new int[] {
                    1 << (bottomRightPos + columns),
                    1 << (bottomRightPos + 2 * columns),
                    1 << (bottomRightPos + 2 * columns - 1),
                    1 << (bottomRightPos)
            };
        }

        @Override
        public int getStartIndex() {
            return 1;
        }

        @Override
        public int getEndIndex(int cols) {
            return cols - 1;
        }
    }

    private static class Tile5 extends BaseTile {
        /*.#...
          .#...
          .##..*/

        @Override
        public int getBottomLen() {
            return 2;
        }

        @Override
        public int[] getUpPositionMasks(int bottomRightPos, int columns) {
            return new int[] {
                    1 << (bottomRightPos - 1 + columns),
                    1 << (bottomRightPos - 1 + 2 * columns),
                    1 << (bottomRightPos),
                    1 << (bottomRightPos - 1)
            };
        }

        @Override
        public int getStartIndex() {
            return 1;
        }

        @Override
        public int getEndIndex(int cols) {
            return cols - 1;
        }
    }

    private static class Tile6 extends BaseTile {
        /*..#..
          ..#..
          .##..*/

        @Override
        public int getBottomLen() {
            return 2;
        }

        @Override
        public int[] getUpPositionMasks(int bottomRightPos, int columns) {
            return new int[] {
                    1 << (bottomRightPos + columns),
                    1 << (bottomRightPos + 2 * columns),
                    1 << (bottomRightPos),
                    1 << (bottomRightPos - 1)
            };
        }

        @Override
        public int getStartIndex() {
            return 1;
        }

        @Override
        public int getEndIndex(int cols) {
            return cols - 1;
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            List<BaseTile> tiles = Arrays.asList(new Tile1(), new Tile2(), new Tile3(), new Tile4(), new Tile5(), new Tile6());

            for (int t = 0; t < T; t++) {
                StringTokenizer sizeTkn = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(sizeTkn.nextToken());
                int m = Integer.parseInt(sizeTkn.nextToken());
                char[][] board = new char[n][m];

                for (int i = 0; i < n; i++) {
                    board[i] = br.readLine().toCharArray();
                }

                LinkedList<Map<Integer, Long>> lastStates = new LinkedList<>();

                for (int row = 0; row < n; row++) {
                    Map<Integer, Long> prevFree = new HashMap<>();

                    if (row == 0) {
                        lastStates.clear();
                        lastStates.add(new HashMap<>());
                        lastStates.add(new HashMap<>());
                        lastStates.add(prevFree);

                        int mul = 1;
                        int initState = 0;
                        for (int row1 = 0; row1 < 3; row1++) {
                            for (int col1 = 0; col1 < m; col1++) {
                                if (board[row1][col1] == '#') {
                                    initState += mul;
                                }
                                mul <<= 1;
                            }
                        }

                        prevFree.put(initState, 1l);
                    } else {
                        Map<Integer, Long> last = lastStates.pollLast();
                        lastStates.clear();
                        lastStates.add(new HashMap<>());
                        lastStates.add(new HashMap<>());
                        lastStates.add(prevFree);

                        int lastRow = 0;
                        int mul = 1;

                        for (int col = 0; col < m; col++) {
                            if (board[row][col] == '#') {
                                lastRow += mul;
                            }
                            mul *= 2;
                        }

                        lastRow <<= 2 * m;

                        for (Map.Entry<Integer, Long> entry : last.entrySet()) {
                            Integer state = entry.getKey();
                            Long count = entry.getValue();

                            state >>= m;
                            state += lastRow;

                            prevFree.put(state, count);
                        }
                    }

                    for (int col = 0; col < m; col++) {
                        Map<Integer, Long> newStates = new HashMap<>();

                        for (BaseTile tile : tiles) {
                            boolean skip = false;
                            int bottomLen = tile.getBottomLen();

                            for (int prevPos = 0; prevPos < bottomLen; prevPos++) {
                                if (col < tile.getStartIndex() || col > tile.getEndIndex(m) || board[row][col - prevPos] == '#') {
                                    skip = true;
                                    break;
                                }
                            }

                            if (skip) {
                                continue;
                            }

                            Map<Integer, Long> statesToCheck = lastStates.get(3 - bottomLen);

                            for (Map.Entry<Integer, Long> entry : statesToCheck.entrySet()) {
                                Integer state = entry.getKey();
                                Long cnt = entry.getValue();

                                skip = false;

                                for(int mask : tile.getUpPositionMasks(col, m)) {
                                    if ((state | mask) == state) {
                                        skip = true;
                                        break;
                                    } else {
                                        state = state | mask;
                                    }
                                }

                                if (skip) {
                                    continue;
                                }

                                newStates.merge(state, cnt, (oldVal, newVal) -> oldVal + newVal);
                            }
                        }
                    }

                    lastStates.add(prevFree);
                    lastStates.pollFirst();
                }
            }
        }
    }
}
