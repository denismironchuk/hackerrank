package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class KnightOnAChessboard {
    private static class Cell {
        private int row;
        private int col;
        private int move;

        public Cell(int row, int col, int move) {
            this.row = row;
            this.col = col;
            this.move = move;
        }

        public boolean isValid(int n) {
            return row > -1 && col > -1 && row < n && col < n;
        }

        private List<Cell> generateAllMoves(int a, int b, int n) {
            return Arrays.asList(new Cell(row - a, col - b, move + 1),
                    new Cell(row + a, col - b, move + 1),
                    new Cell(row - a, col + b, move + 1),
                    new Cell(row + a, col + b, move + 1),
                    new Cell(row - b, col - a, move + 1),
                    new Cell(row + b, col - a, move + 1),
                    new Cell(row - b, col + a, move + 1),
                    new Cell(row + b, col + a, move + 1));
        }

        private List<Cell> generateCorrectMoves(int a, int b, int n, int[][] processed) {
            List<Cell> allMoves = generateAllMoves(a, b, n);
            List<Cell> correctMoves = new ArrayList<>();

            for (Cell c : allMoves) {
                if (c.isValid(n) && processed[c.row][c.col] == 0) {
                    correctMoves.add(c);
                }
            }

            return correctMoves;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[][] res = new int[n - 1][n - 1];
        for (int a = 1; a < n; a++){
            for (int b = a; b < n; b++) {
                res[a - 1][b - 1] = -1;
                res[b - 1][a - 1] = -1;
                int[][] processed = new int[n][n];
                Queue<Cell> q = new LinkedList<>();
                q.add(new Cell(0, 0, 0));
                processed[0][0] = 1;
                while (!q.isEmpty()) {
                    Cell current = q.poll();

                    if (current.row == n - 1 && current.col == n - 1) {
                        res[a - 1][b - 1] = current.move;
                        res[b - 1][a - 1] = current.move;
                        break;
                    }

                    for (Cell next: current.generateCorrectMoves(a, b, n, processed)) {
                        processed[next.row][next.col] = 1;
                        q.add(next);
                    }
                }
            }
        }

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1; j++) {
                System.out.printf("%d ", res[i][j]);
            }
            System.out.println();
        }
    }
}
