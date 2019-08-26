package codejam.datacenterDuplex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class DatacenterDuplex {
    private static int rows;
    private static int cols;
    private static char[][] board;
    private static char[][] corridors;
    private static DisjointSet dSet;

    private static class DisjointSet {
        private int[][] rank;
        private int[][][] parents;

        public DisjointSet() {
            rank = new int[rows][cols];
            parents = new int[rows][cols][2];
        }

        public void makeSet(int row, int col) {
            parents[row][col] = new int[] {row, col};
        }

        public int[] find(int row, int col) {
            int[] parent = parents[row][col];
            if (parent[0] == row && parent[1] == col) {
                return parent;
            } else {
                parents[row][col] = find(parent[0], parent[1]);
                return parents[row][col];
            }
        }

        public void union(int row1, int col1, int row2, int col2) {
            int[] parent1 = find(row1, col1);
            int[] parent2 = find(row2, col2);

            if (rank[parent1[0]][parent1[1]] > rank[parent2[0]][parent2[1]]) {
                parents[parent2[0]][parent2[1]] = parent1;
            } else {
                parents[parent1[0]][parent1[1]] = parent2;
                if (rank[parent1[0]][parent1[1]] == rank[parent2[0]][parent2[1]]) {
                    rank[parent2[0]][parent2[1]]++;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t <= T; t++) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            rows = Integer.parseInt(tkn1.nextToken());
            cols = Integer.parseInt(tkn1.nextToken());
            board = new char[rows][cols];

            for (int i = 0; i < rows; i++) {
                board[i] = br.readLine().toCharArray();
            }
            corridors = new char[rows - 1][cols - 1];

            for (int i = 0; i < rows - 1; i++) {
                for (int j = 0; j < cols - 1; j++) {
                    corridors[i][j] = '.';
                }
            }

            dSet = new DisjointSet();

            List<int[]> coridorsPos = new ArrayList<>();

            for (int i = 0; i < rows - 1; i++) {
                for (int j = 0; j < cols - 1; j++) {
                    if (board[i][j] == board[i + 1][j + 1] && board[i][j + 1] == board[i + 1][j] && board[i][j] != board[i][j + 1]) {
                        coridorsPos.add(new int[]{i, j});
                    }
                }
            }

            long corridorsCnt = coridorsPos.size();

            int aCnt = countIslands('A');
            int bCnt = countIslands('B');

            if (aCnt + bCnt - corridorsCnt == 2) {
                for (int[] corPos : coridorsPos) {
                    int corRow = corPos[0];
                    int corCol = corPos[1];

                    if (dSet.find(corRow, corCol) == dSet.find(corRow + 1, corCol + 1)) {
                        corridors[corRow][corCol] = '/';
                        dSet.union(corRow, corCol + 1, corRow + 1, corCol);
                    } else {
                        corridors[corRow][corCol] = '\\';
                        dSet.union(corRow, corCol, corRow + 1, corCol + 1);
                    }
                }
                System.out.printf("Case #%s: %s\n", t, "POSSIBLE");
                printCoridors();
            } else {
                System.out.printf("Case #%s: %s\n", t, "IMPOSSIBLE");
            }
        }
    }

    private static void printCoridors() {
        for (int row = 0 ; row < rows - 1; row++) {
            for (int col = 0; col < cols - 1; col++) {
                System.out.printf("%s", corridors[row][col]);
            }
            System.out.printf("\n");
        }
    }

    private static int countIslands(char c) {
        int res = 0;
        int[][] proc = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (proc[i][j] == 0 && board[i][j] == c) {
                    res++;
                    bfs(i, j, proc);
                }
            }
        }

        return res;
    }

    private static void bfs(int startRow, int startCol, int[][] proc) {
        char c = board[startRow][startCol];

        Queue<int[]> q = new LinkedList<>();
        q.add(new int[] {startRow, startCol});
        proc[startRow][startCol] = 1;
        dSet.makeSet(startRow, startCol);

        while (!q.isEmpty()) {
            int[] pos = q.poll();
            int row = pos[0];
            int col = pos[1];

            if (row < rows - 1 && proc[row + 1][col] == 0 && board[row + 1][col] == c) {
                q.add(new int[] {row + 1, col});
                dSet.makeSet(row + 1, col);
                dSet.union(row, col, row + 1, col);
                proc[row + 1][col] = 1;
            }

            if (row > 0 && proc[row - 1][col] == 0 && board[row - 1][col] == c) {
                q.add(new int[] {row - 1, col});
                dSet.makeSet(row - 1, col);
                dSet.union(row, col, row - 1, col);
                proc[row - 1][col] = 1;
            }

            if (col < cols - 1 && proc[row][col + 1] == 0 && board[row][col + 1] == c) {
                q.add(new int[] {row, col + 1});
                dSet.makeSet(row, col + 1);
                dSet.union(row, col, row, col + 1);
                proc[row][col + 1] = 1;
            }

            if (col > 0 && proc[row][col - 1] == 0 && board[row][col - 1] == c) {
                q.add(new int[]{row, col - 1});
                dSet.makeSet(row, col - 1);
                dSet.union(row, col, row, col - 1);
                proc[row][col - 1] = 1;
            }

            //corridors

            if (row < rows - 1 && col < cols - 1 && corridors[row][col] == '\\' && proc[row + 1][col + 1] == 0 && board[row + 1][col + 1] == c) {
                q.add(new int[]{row + 1, col + 1});
                dSet.makeSet(row + 1, col + 1);
                dSet.union(row, col, row + 1, col + 1);
                proc[row + 1][col + 1] = 1;
            }

            if (row > 0 && col > 0 && corridors[row - 1][col - 1] == '\\' && proc[row - 1][col - 1] == 0 && board[row - 1][col - 1] == c) {
                q.add(new int[]{row - 1, col - 1});
                dSet.makeSet(row - 1, col - 1);
                dSet.union(row, col, row - 1, col - 1);
                proc[row - 1][col - 1] = 1;
            }

            if (row < rows - 1 && col > 0 && corridors[row][col - 1] == '/' && proc[row + 1][col - 1] == 0 && board[row + 1][col - 1] == c) {
                q.add(new int[]{row + 1, col - 1});
                dSet.makeSet(row + 1, col - 1);
                dSet.union(row, col, row + 1, col - 1);
                proc[row + 1][col - 1] = 1;
            }

            if (row > 0 && col < cols - 1 && corridors[row - 1][col] == '/' && proc[row - 1][col + 1] == 0 && board[row - 1][col + 1] == c) {
                q.add(new int[]{row - 1, col + 1});
                dSet.makeSet(row - 1, col + 1);
                dSet.union(row, col, row - 1, col + 1);
                proc[row - 1][col + 1] = 1;
            }
        }
    }
}
