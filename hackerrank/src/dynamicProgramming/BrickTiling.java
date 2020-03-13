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
        public abstract int getLen();
        public abstract int getHeight();
        public abstract int[] getUpPositionMasks(int bottomRightPos, int columns);
        public abstract int getStartIndex();
        public abstract int getEndIndex(int cols);
    }

    private static class Tile1 extends BaseTile {
        /*.....
          ...#.
          .###.*/

        @Override
        public int getLen() {
            return 3;
        }

        @Override
        public int getHeight() {
            return 2;
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

        @Override
        public String toString() {
            return ".....\n" +
                   "...#.\n" +
                   ".###.";
        }
    }

    private static class Tile2 extends BaseTile {
        /*.....
          .#...
          .###.*/

        @Override
        public int getLen() {
            return 3;
        }

        @Override
        public int getHeight() {
            return 2;
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

        @Override
        public String toString() {
            return ".....\n" +
                   ".#...\n" +
                   ".###.";
        }
    }

    private static class Tile3 extends BaseTile {
        /*.##..
          .#...
          .#...*/

        @Override
        public int getLen() {
            return 1;
        }

        @Override
        public int getHeight() {
            return 3;
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

        @Override
        public String toString() {
            return ".##..\n" +
                   ".#...\n" +
                   ".#...";
        }
    }

    private static class Tile4 extends BaseTile {
        /*.##..
          ..#..
          ..#..*/

        @Override
        public int getLen() {
            return 1;
        }

        @Override
        public int getHeight() {
            return 3;
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

        @Override
        public String toString() {
            return ".##..\n" +
                   "..#..\n" +
                   "..#..";
        }
    }

    private static class Tile5 extends BaseTile {
        /*.#...
          .#...
          .##..*/

        @Override
        public int getLen() {
            return 2;
        }

        @Override
        public int getHeight() {
            return 3;
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

        @Override
        public String toString() {
            return ".#...\n" +
                   ".#...\n" +
                   ".##..";
        }
    }

    private static class Tile6 extends BaseTile {
        /*..#..
          ..#..
          .##..*/

        @Override
        public int getLen() {
            return 2;
        }

        @Override
        public int getHeight() {
            return 3;
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

        @Override
        public String toString() {
            return "..#..\n" +
                   "..#..\n" +
                   ".##..";
        }
    }

    private static class Tile7 extends BaseTile {
        /*.....
          .###.
          .#...*/

        @Override
        public int getLen() {
            return 1;
        }

        @Override
        public int getHeight() {
            return 2;
        }

        @Override
        public int[] getUpPositionMasks(int bottomRightPos, int columns) {
            return new int[] {
                    1 << (bottomRightPos),
                    1 << (bottomRightPos + columns),
                    1 << (bottomRightPos + columns + 1),
                    1 << (bottomRightPos + columns + 2),
            };
        }

        @Override
        public int getStartIndex() {
            return 0;
        }

        @Override
        public int getEndIndex(int cols) {
            return cols - 3;
        }

        @Override
        public String toString() {
            return ".....\n" +
                   ".###.\n" +
                   ".#...";
        }
    }

    private static class Tile8 extends BaseTile {
        /*.....
          .###.
          ...#.*/

        @Override
        public int getLen() {
            return 3;
        }

        @Override
        public int getHeight() {
            return 2;
        }

        @Override
        public int[] getUpPositionMasks(int bottomRightPos, int columns) {
            return new int[] {
                    1 << (bottomRightPos),
                    1 << (bottomRightPos + columns),
                    1 << (bottomRightPos + columns - 1),
                    1 << (bottomRightPos + columns - 2),
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

        @Override
        public String toString() {
            return ".....\n" +
                   ".###.\n" +
                   "...#.";
        }
    }

    private static int n;
    private static int m;
    private static char[][] board;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            List<BaseTile> tiles = Arrays.asList(new Tile1(), new Tile2(), new Tile3(), new Tile4(),
                    new Tile5(), new Tile6(), new Tile7(), new Tile8());

            for (int t = 0; t < T; t++) {
                StringTokenizer sizeTkn = new StringTokenizer(br.readLine());
                n = Integer.parseInt(sizeTkn.nextToken());
                m = Integer.parseInt(sizeTkn.nextToken());
                board = new char[n][m];

                for (int i = 0; i < n; i++) {
                    board[i] = br.readLine().toCharArray();
                }

                LinkedList<Map<Integer, Long>> columnStates = new LinkedList<>();
                columnStates.add(new HashMap<>());
                columnStates.add(new HashMap<>());
                Map<Integer, Long> initStates = new HashMap<>();
                columnStates.add(initStates);

                int mul = 1 << m;
                int initState = 0;
                for (int row1 = 0; row1 < 2; row1++) {
                    for (int col1 = 0; col1 < m; col1++) {
                        if (board[row1][col1] == '#') {
                            initState += mul;
                        }
                        mul <<= 1;
                    }
                }

                initStates.put(initState, 1l);

                for (int row = 0; row < n - 1; row++) {
                    addNewRowToStates(columnStates, row);

                    for (int col = 0; col < m; col++) {
                        Map<Integer, Long> newStates = new HashMap<>();

                        if (board[row][col] == '#') {
                            newStates.putAll(columnStates.peekLast());
                        } else {
                            for (BaseTile tile : tiles) {
                                if (col < tile.getStartIndex() || col > tile.getEndIndex(m) || row + tile.getHeight() > n) {
                                    continue;
                                }

                                Map<Integer, Long> states = columnStates.get(3 - tile.getLen());
                                for (Map.Entry<Integer, Long> entry : states.entrySet()) {
                                    Integer state = entry.getKey();
                                    Long cnt = entry.getValue();

                                    boolean skip = false;

                                    for (int mask : tile.getUpPositionMasks(col, m)) {
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

                        columnStates.add(newStates);
                        columnStates.pollFirst();
                    }
                }
            }
        }
    }

    private static void addNewRowToStates(LinkedList<Map<Integer, Long>> columnStates, int row) {
        Map<Integer, Long> rowInitStates = new HashMap<>();

        int lastRow = 0;

        if (row < n - 2) {
            int mul = 1;

            for (int col = 0; col < m; col++) {
                if (board[row + 2][col] == '#') {
                    lastRow += mul;
                }
                mul *= 2;
            }

            lastRow <<= 2 * m;
        }

        for (Map.Entry<Integer, Long> entry : columnStates.pollLast().entrySet()) {
            Integer state = entry.getKey();
            Long count = entry.getValue();

            state >>= m;
            state += lastRow;

            rowInitStates.put(state, count);
        }

        columnStates.clear();
        columnStates.add(new HashMap<>());
        columnStates.add(new HashMap<>());
        columnStates.add(rowInitStates);
    }
}