package kickstart.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class PalindromicCrossword {

    private static class Cell {

        private int row;
        private int col;

        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    private static class Word {

        private Cell start;
        private Cell end;

        public Word(Cell start, Cell end) {
            this.start = start;
            this.end = end;
        }

        public int reflectVertical(int row) {
            return start.row + end.row - row;
        }

        public int reflectHorizontal(int col) {
            return start.col + end.col - col;
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int rows = Integer.parseInt(tkn1.nextToken());
                int cols = Integer.parseInt(tkn1.nextToken());
                char[][] crossword = new char[rows][cols];

                for (int i = 0; i < rows; i++) {
                    crossword[i] = br.readLine().toCharArray();
                }

                Word[][] horizontalWords = new Word[rows][cols];
                Word[][] verticalWords = new Word[rows][cols];

                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        if (verticalWords[i][j] == null && crossword[i][j] != '#') {
                            Cell vertStart = new Cell(i, j);
                            Cell vertEnd = new Cell(i, j);
                            Word vertical = new Word(vertStart, vertEnd);

                            int k = i;
                            for (; k > -1 && crossword[k][j] != '#'; k--) {
                                verticalWords[k][j] = vertical;
                            }
                            vertStart.row = k + 1;

                            k = i;
                            for (; k < rows && crossword[k][j] != '#'; k++) {
                                verticalWords[k][j] = vertical;
                            }
                            vertEnd.row = k - 1;
                        }

                        if (horizontalWords[i][j] == null && crossword[i][j] != '#') {
                            Cell horizonStart = new Cell(i, j);
                            Cell horizonEnd = new Cell(i, j);
                            Word horizontal = new Word(horizonStart, horizonEnd);

                            int k = j;
                            for (; k > -1 && crossword[i][k] != '#'; k--) {
                                horizontalWords[i][k] = horizontal;
                            }
                            horizonStart.col = k + 1;

                            k = j;
                            for (; k < cols && crossword[i][k] != '#'; k++) {
                                horizontalWords[i][k] = horizontal;
                            }
                            horizonEnd.col = k - 1;
                        }
                    }
                }

                Queue<Cell> cellsQ = new LinkedList<>();

                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        if (crossword[row][col] != '.' && crossword[row][col] != '#') {
                            cellsQ.add(new Cell(row, col));
                        }
                    }
                }

                int restoredLetters = 0;
                while (!cellsQ.isEmpty()) {
                    Cell cell = cellsQ.poll();
                    Word verticalWord = verticalWords[cell.row][cell.col];
                    Word horizontalWord = horizontalWords[cell.row][cell.col];

                    int reflectedRow = verticalWord.reflectVertical(cell.row);
                    if (crossword[reflectedRow][cell.col] == '.') {
                        crossword[reflectedRow][cell.col] = crossword[cell.row][cell.col];
                        cellsQ.add(new Cell(reflectedRow, cell.col));
                        restoredLetters++;
                    }

                    int reflectedCol = horizontalWord.reflectHorizontal(cell.col);
                    if (crossword[cell.row][reflectedCol] == '.') {
                        crossword[cell.row][reflectedCol] = crossword[cell.row][cell.col];
                        cellsQ.add(new Cell(cell.row, reflectedCol));
                        restoredLetters++;
                    }
                }

                System.out.printf("Case #%s: %s\n", t, restoredLetters);
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        System.out.print(crossword[row][col]);
                    }
                    System.out.println();
                }
            }
        }
    }
}