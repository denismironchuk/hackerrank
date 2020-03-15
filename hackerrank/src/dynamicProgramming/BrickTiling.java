package dynamicProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BrickTiling {
    private static final long MOD = 1000000007;

    private static abstract class BaseTile {
        public abstract int getLen();
        public abstract int getHeight();
        public abstract int getStartIndex();
        public abstract int getEndIndex();

        public abstract int[] getUpPositionMasks(int bottomRightPos);
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
        public int[] getUpPositionMasks(int bottomRightPos) {
            return new int[] {
                    1 << (bottomRightPos + m),
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
        public int getEndIndex() {
            return m - 1;
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
        public int[] getUpPositionMasks(int bottomRightPos) {
            return new int[] {
                    1 << (bottomRightPos + m - 2),
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
        public int getEndIndex() {
            return m - 1;
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
        public int[] getUpPositionMasks(int bottomRightPos) {
            return new int[] {
                    1 << (bottomRightPos + m),
                    1 << (bottomRightPos + 2 * m),
                    1 << (bottomRightPos + 2 * m + 1),
                    1 << (bottomRightPos)
            };
        }

        @Override
        public int getStartIndex() {
            return 0;
        }

        @Override
        public int getEndIndex() {
            return m - 2;
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
        public int[] getUpPositionMasks(int bottomRightPos) {
            return new int[] {
                    1 << (bottomRightPos + m),
                    1 << (bottomRightPos + 2 * m),
                    1 << (bottomRightPos + 2 * m - 1),
                    1 << (bottomRightPos)
            };
        }

        @Override
        public int getStartIndex() {
            return 1;
        }

        @Override
        public int getEndIndex() {
            return m - 1;
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
        public int[] getUpPositionMasks(int bottomRightPos) {
            return new int[] {
                    1 << (bottomRightPos - 1 + m),
                    1 << (bottomRightPos - 1 + 2 * m),
                    1 << (bottomRightPos),
                    1 << (bottomRightPos - 1)
            };
        }

        @Override
        public int getStartIndex() {
            return 1;
        }

        @Override
        public int getEndIndex() {
            return m - 1;
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
        public int[] getUpPositionMasks(int bottomRightPos) {
            return new int[] {
                    1 << (bottomRightPos + m),
                    1 << (bottomRightPos + 2 * m),
                    1 << (bottomRightPos),
                    1 << (bottomRightPos - 1)
            };
        }

        @Override
        public int getStartIndex() {
            return 1;
        }

        @Override
        public int getEndIndex() {
            return m - 1;
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
        public int[] getUpPositionMasks(int bottomRightPos) {
            return new int[] {
                    1 << (bottomRightPos),
                    1 << (bottomRightPos + m),
                    1 << (bottomRightPos + m + 1),
                    1 << (bottomRightPos + m + 2),
            };
        }

        @Override
        public int getStartIndex() {
            return 0;
        }

        @Override
        public int getEndIndex() {
            return m - 3;
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
            return 1;
        }

        @Override
        public int getHeight() {
            return 2;
        }

        @Override
        public int[] getUpPositionMasks(int bottomRightPos) {
            return new int[] {
                    1 << (bottomRightPos),
                    1 << (bottomRightPos + m),
                    1 << (bottomRightPos + m - 1),
                    1 << (bottomRightPos + m - 2),
            };
        }

        @Override
        public int getStartIndex() {
            return 2;
        }

        @Override
        public int getEndIndex() {
            return m - 1;
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

            List<BaseTile> tiles1 = Arrays.asList(new Tile3(), new Tile4(), new Tile7(), new Tile8());

            List<BaseTile> tiles2 = Arrays.asList(new Tile5(), new Tile6());

            List<BaseTile> tiles3 = Arrays.asList(new Tile1(), new Tile2());

            for (int t = 0; t < T; t++) {
                StringTokenizer sizeTkn = new StringTokenizer(br.readLine());
                n = Integer.parseInt(sizeTkn.nextToken());
                m = Integer.parseInt(sizeTkn.nextToken());
                board = new char[n][m];

                for (int i = 0; i < n; i++) {
                    board[n - i - 1] = br.readLine().toCharArray();
                }

                LinkedList<Map<Integer, Long>> columnStates = new LinkedList<>();
                columnStates.add(new HashMap<>());
                columnStates.add(new HashMap<>());
                Map<Integer, Long> initStates = new HashMap<>();
                columnStates.add(initStates);

                int mul = 1 << m;
                int initState = mul - 1;
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
                    //printStates(columnStates);

                    for (int col = 0; col < m; col++) {
                        Map<Integer, Long> newStates = new HashMap<>();

                        buildNewStates(newStates, tiles1, columnStates.get(2), col, row);
                        buildNewStates(newStates, tiles2, columnStates.get(1), col, row);
                        buildNewStates(newStates, tiles3, columnStates.get(0), col, row);

                        columnStates.add(newStates);
                        columnStates.pollFirst();

                        //printStates(columnStates);
                    }
                }

                System.out.println(columnStates.get(2).getOrDefault((1 << (2 * m)) - 1, 0l));
            }
        }
    }

    private static void buildNewStates(Map<Integer, Long> newStates, List<BaseTile> tiles, Map<Integer, Long> states, int col, int row) {
        int colMask = 1 << col;
        Iterator<Map.Entry<Integer, Long>> itr = states.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<Integer, Long> entry = itr.next();
            Integer state = entry.getKey();
            Long cnt = entry.getValue();

            if ((state | colMask) == state) {
                newStates.merge(state, cnt, (oldVal, newVal) -> (oldVal + newVal) % MOD);
                itr.remove();
                continue;
            }

            for (BaseTile tile : tiles) {
                //System.out.println(tile);
                if (col < tile.getStartIndex() || col > tile.getEndIndex() || row + tile.getHeight() > n) {
                    continue;
                }

                boolean skip = false;

                Integer newState = state;

                for (int mask : tile.getUpPositionMasks(col)) {
                    if ((newState | mask) == newState) {
                        skip = true;
                        break;
                    } else {
                        newState = newState | mask;
                    }
                }

                if (skip) {
                    continue;
                }

                newStates.merge(newState, cnt, (oldVal, newVal) -> (oldVal + newVal) % MOD);
            }
        }
    }

    private static void printStates(LinkedList<Map<Integer, Long>> columnStates) {
        int ind = 0;
        for (Map<Integer, Long> states : columnStates) {
            System.out.println(ind);
            for (Integer state : states.keySet()) {
                printState(state);
                System.out.println("======");
            }
            ind++;
        }
    }

    private static void printState(Integer state) {
        char[][] board = new char[3][m];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < m; j++) {
                board[i][j] = '.';
            }
        }

        for (int row = 2; state != 0 && row > -1; row--) {
            for (int col = 0; state != 0 && col < m; col++) {
                if (state % 2 == 1) {
                    board[row][col] = '#';
                }
                state /= 2;
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < m; col++) {
                System.out.print(board[row][col]);
            }
            System.out.println();
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

        int neededRow = (1 << m) - 1;

        for (Map.Entry<Integer, Long> entry : columnStates.pollLast().entrySet()) {
            Integer state = entry.getKey();
            //printState(state);

            if ((state | neededRow) != state) {
                continue;
            }

            Long count = entry.getValue();

            state >>= m;
            state += lastRow;
            //System.out.println("========");
            //printState(state);
            //System.out.println("========");
            rowInitStates.put(state, count);
        }

        columnStates.clear();
        columnStates.add(new HashMap<>());
        columnStates.add(new HashMap<>());
        columnStates.add(rowInitStates);
    }
}