package codejam.year2020.qualification;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LatinSquares {
    private static final int N = 6;

    public static void main(String[] args) {
        int[][] sqr = new int[N][N];
        Set<Integer>[] rowsSets = new Set[N];
        Set<Integer>[] colsSets = new Set[N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sqr[i][j] = N + 1;
            }
        }

        for (int i = 0; i < N; i++) {
            rowsSets[i] = new HashSet<>();
            colsSets[i] = new HashSet<>();
        }

        int col = 0;
        int row = 0;

        Set<Integer> all = new HashSet<>();
        for (int i = 1; i <= N; i++) {
            all.add(i);
        }

        while (col < N && row < N) {
            printState(row,col);

            int newVal = -1;

            /*for (int i = sqr[row][col] - 1; newVal == -1 && i > 0; i--) {
                if (!rowsSets[row].contains(i) && !colsSets[col].contains(i)) {
                    newVal = i;
                }
            }*/
            List<Integer> cands = getAvailales(rowsSets[row], colsSets[col]);
            if (!cands.isEmpty()) {
                newVal = cands.get((int)(Math.random() * cands.size()));
            }

            if (newVal != -1) {
                sqr[row][col] = newVal;
                rowsSets[row].add(newVal);
                colsSets[col].add(newVal);
                int[] nextCell = getNextCell(row, col);
                row = nextCell[0];
                col = nextCell[1];
            } else {
                sqr[row][col] = N + 1;
                int[] prevCell = getPrevCell(row, col);
                row = prevCell[0];
                col = prevCell[1];
                rowsSets[row].remove(sqr[row][col]);
                colsSets[col].remove(sqr[row][col]);
            }
        }

        printSquare(sqr);
    }

    private static List<Integer> getAvailales(Set<Integer> s1, Set<Integer> s2) {
        List<Integer> all = new ArrayList<>();
        for (int i = 1; i <= N; i++) {
            if (!s1.contains(i) && !s2.contains(i)) {
                all.add(i);
            }
        }

        return all;
    }

    private static void printState(int row, int col) {
        for (int i = 0; i < col * N + row; i++) {
            System.out.print("#");
        }
        System.out.printf(" (%d, %d)\n", row, col);
    }

    private static void printSquare(int[][] sqr) {
        for (int i = 0; i < sqr.length; i++) {
            for (int j = 0; j < sqr[0].length; j++) {
                System.out.printf("%2d ", sqr[i][j]);
            }
            System.out.println();
        }
    }

    private static int[] getNextCell(int row, int col) {
        int[] nextCell = _getNextCell(row, col);
        /*while (nextCell[0] == nextCell[1]) {
            nextCell = _getNextCell(nextCell[0], nextCell[1]);
        }*/
        return nextCell;
    }

    private static int[] _getNextCell(int row, int col) {
        if (row == N - 1) {
            return new int[] {0, col + 1};
        } else {
            return new int[] {row + 1, col};
        }
    }

    private static int[] getPrevCell(int row, int col) {
        int[] prevCell = _getPrevCell(row, col);
        /*while (prevCell[0] == prevCell[1]) {
            prevCell = _getPrevCell(prevCell[0], prevCell[1]);
        }*/
        return prevCell;
    }

    private static int[] _getPrevCell(int row, int col) {
        if (row == 0) {
            return new int[] {N - 1, col - 1};
        } else {
            return new int[] {row - 1, col};
        }
    }
}
