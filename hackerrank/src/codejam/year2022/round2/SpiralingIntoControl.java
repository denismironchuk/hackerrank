package codejam.year2022.round2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SpiralingIntoControl {

    private static final int N = 51;
    private static final int JUMP_POS = N / 2;

    static class Cell {
        private int row;
        private int col;

        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }

    public static void main(String[] args) {
        int[][] spiral = new int[N][N];
        Cell[] cells = new Cell[N * N + 1];

        int rowInc = 0;
        int colInc = 1;

        int row = 0;
        int col = 0;

        for (int i = 1; i <= N * N; i++) {
            spiral[row][col] = i;
            cells[i] = new Cell(row, col);
            if (row + rowInc == N || row + rowInc < 0 || col + colInc == N || col + colInc < 0 || spiral[row + rowInc][col + colInc] != 0) {
                if (rowInc == 0 && colInc == 1) {
                    rowInc = 1;
                    colInc = 0;
                } else if (rowInc == 0 && colInc == -1) {
                    rowInc = -1;
                    colInc = 0;
                } else if (rowInc == 1 && colInc == 0) {
                    rowInc = 0;
                    colInc = -1;
                } else if (rowInc == -1 && colInc == 0) {
                    rowInc = 0;
                    colInc = 1;
                }
            }
            row += rowInc;
            col += colInc;
        }

        Set<Integer>[] possibleMoves = new Set[N * N + 1];
        possibleMoves[N * N] = new TreeSet<>(Integer::compare);
        possibleMoves[N * N].add(0);
        for (int i = N * N - 1; i > 0; i--) {
            Cell c = cells[i];
            List<Set<Integer>> sets = new ArrayList<>();
            if (c.row - 1 > -1 && spiral[c.row - 1][c.col] > spiral[c.row][c.col]) {
                sets.add(possibleMoves[spiral[c.row - 1][c.col]]);
            }
            if (c.row + 1 < N && spiral[c.row + 1][c.col] > spiral[c.row][c.col]) {
                sets.add(possibleMoves[spiral[c.row + 1][c.col]]);
            }
            if (c.col - 1 > -1 && spiral[c.row][c.col - 1] > spiral[c.row][c.col]) {
                sets.add(possibleMoves[spiral[c.row][c.col - 1]]);
            }
            if (c.col + 1 < N && spiral[c.row][c.col + 1] > spiral[c.row][c.col]) {
                sets.add(possibleMoves[spiral[c.row][c.col + 1]]);
            }
            possibleMoves[i] = new TreeSet<>(Integer::compare);
            for (Set<Integer> s : sets) {
                for (Integer j : s) {
                    possibleMoves[i].add(j + 1);
                }
            }
        }

        Set<Integer>[] possibleMoves2 = new Set[N * N + 1];
        possibleMoves2[N * N] = new TreeSet<>(Integer::compare);
        possibleMoves2[N * N].add(0);

        for (int i = N * N - 1; i > 0; i--) {
            Cell c = cells[i];
            List<Set<Integer>> sets = new ArrayList<>();
            if (c.col == JUMP_POS || c.row == JUMP_POS) {
                if (c.row - 1 > -1 && spiral[c.row - 1][c.col] > spiral[c.row][c.col]) {
                    sets.add(possibleMoves2[spiral[c.row - 1][c.col]]);
                }
                if (c.row + 1 < N && spiral[c.row + 1][c.col] > spiral[c.row][c.col]) {
                    sets.add(possibleMoves2[spiral[c.row + 1][c.col]]);
                }
                if (c.col - 1 > -1 && spiral[c.row][c.col - 1] > spiral[c.row][c.col]) {
                    sets.add(possibleMoves2[spiral[c.row][c.col - 1]]);
                }
                if (c.col + 1 < N && spiral[c.row][c.col + 1] > spiral[c.row][c.col]) {
                    sets.add(possibleMoves2[spiral[c.row][c.col + 1]]);
                }
            } else {
                sets.add(possibleMoves2[i + 1]);
            }

            possibleMoves2[i] = new TreeSet<>(Integer::compare);
            for (Set<Integer> s : sets) {
                for (Integer j : s) {
                    possibleMoves2[i].add(j + 1);
                }
            }
        }

        if (!possibleMoves[1].containsAll(possibleMoves2[1]) || !possibleMoves2[1].containsAll(possibleMoves[1])) {
            throw new RuntimeException("!!!!!!!!!");
        }

        /*for (int i = 1; i <= N * N; i++) {
            System.out.println("i - " + possibleMoves[i].size() + " - " + possibleMoves[i]);
        }*/

        System.out.println();
    }
}
