package codejam.year2022.round2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SpiralingIntoControlFinal {

    static class Cell {
        private long row;
        private long col;

        public Cell(long row, long col) {
            this.row = row;
            this.col = col;
        }

        public long getRow() {
            return row;
        }

        public long getCol() {
            return col;
        }

        @Override
        public String toString() {
            return "{" +
                    "row=" + row +
                    ", col=" + col +
                    '}';
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                long n = Long.parseLong(tkn.nextToken());
                long k = Long.parseLong(tkn.nextToken());
                long minMoves = n - 1;
                long maxMoves = n * n - 1;
                if (k % 2 == 1 || k < minMoves || k > maxMoves) {
                    System.out.printf("Case #%s: IMPOSSIBLE\n", t);
                    continue;
                }
                List<Cell> jumpCells = new ArrayList<>();
                long row = 0;
                long col = -1;
                boolean isHorizontal = true;
                while (row != n / 2 && col != n / 2) {
                    if (isHorizontal) {
                        jumpCells.add(new Cell(row, n / 2));
                    } else {
                        jumpCells.add(new Cell(n / 2, col));
                    }

                    if (isHorizontal) {
                        if (row < n / 2) {
                            col = (n - 1) - col - 1;
                        } else {
                            col = (n - 1) - col;
                        }
                    } else {
                        if (col < n / 2) {
                            row = n - row;
                        } else {
                            row = n - row - 1;
                        }
                    }

                    isHorizontal = !isHorizontal;
                }

                List<Long> jumpVals = new ArrayList<>();
                jumpVals.add(n / 2 + 1);
                for (int i = 1; i < jumpCells.size(); i++) {
                    Long prevVal = jumpVals.get(i - 1);
                    Cell prevPos = jumpCells.get(i - 1);
                    Cell curPos = jumpCells.get(i);
                    jumpVals.add(prevVal + Math.abs(curPos.row - prevPos.row) + Math.abs(curPos.col - prevPos.col));
                }

                k -= n / 2;
                List<long[]> resJumps = new ArrayList<>();
                int i = 0;
                while (i < jumpCells.size() - 1) {
                    Cell cell = jumpCells.get(i);
                    Long val = jumpVals.get(i);
                    if (i + 4 < jumpCells.size()) {
                        Cell jumpCandidate = jumpCells.get(i + 4);
                        Long jumpVal = jumpVals.get(i + 4);
                        Long jumpMin = Math.abs(jumpCandidate.row - n / 2) + Math.abs(jumpCandidate.col - n / 2);
                        Long jumpMax = n * n - jumpVal;
                        if (k - 1 >= jumpMin && k - 1 <= jumpMax) {
                            resJumps.add(new long[] {val, jumpVal});
                            k -= 1;
                            i += 4;
                        } else {
                            i += 1;
                            k -= jumpVals.get(i) - val;
                        }
                    } else {
                        if (k == 1) {
                            resJumps.add(new long[] {val, n * n});
                            break;
                        } else {
                            i += 1;
                            k -= jumpVals.get(i) - val;
                        }
                    }
                }

                System.out.printf("Case #%s: %s\n", t, resJumps.size());
                for (long[] jump : resJumps) {
                    System.out.printf("%s %s\n", jump[0], jump[1]);
                }
            }
        }
    }
}
