package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class ConnectedCellsOnAGrid {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int m = Integer.parseInt(br.readLine());
        int[][] grid = new int[n][m];

        for (int i = 0; i < n; i++) {
            StringTokenizer lineTkn = new StringTokenizer(br.readLine());
            for (int j = 0; j < m; j++) {
                grid[i][j] = lineTkn.nextToken().equals("0") ? 0 : 1;
            }
        }

        int[][] processed = new int[n][m];
        Queue<int[]> q = new LinkedList<>();

        int maxRegion = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 1 && processed[i][j] == 0) {
                    int regSize = 0;
                    q.add(new int[] {i,j});
                    processed[i][j] = 1;
                    while (!q.isEmpty()) {
                        int[] cell = q.poll();
                        regSize++;
                        int row = cell[0];
                        int col = cell[1];

                        if (row > 0 && col > 0 && grid[row - 1][col - 1] == 1 && processed[row - 1][col - 1] == 0) {
                            q.add(new int[] {row - 1,col - 1});
                            processed[row - 1][col - 1] = 1;
                        }

                        if (row > 0 && grid[row - 1][col] == 1 && processed[row - 1][col] == 0) {
                            q.add(new int[] {row - 1,col});
                            processed[row - 1][col] = 1;
                        }

                        if (row > 0 && col < m - 1 && grid[row - 1][col + 1] == 1 && processed[row - 1][col + 1] == 0) {
                            q.add(new int[] {row - 1,col + 1});
                            processed[row - 1][col + 1] = 1;
                        }

                        if (col < m - 1 && grid[row][col + 1] == 1 && processed[row][col + 1] == 0) {
                            q.add(new int[] {row,col + 1});
                            processed[row][col + 1] = 1;
                        }

                        if (row < n - 1 && col < m - 1 && grid[row + 1][col + 1] == 1 && processed[row + 1][col + 1] == 0) {
                            q.add(new int[] {row + 1,col + 1});
                            processed[row + 1][col + 1] = 1;
                        }

                        if (row < n - 1 && grid[row + 1][col] == 1 && processed[row + 1][col] == 0) {
                            q.add(new int[] {row + 1,col});
                            processed[row + 1][col] = 1;
                        }

                        if (row < n - 1 && col > 0 && grid[row + 1][col - 1] == 1 && processed[row + 1][col - 1] == 0) {
                            q.add(new int[] {row + 1,col - 1});
                            processed[row + 1][col - 1] = 1;
                        }

                        if (col > 0 && grid[row][col - 1] == 1 && processed[row][col - 1] == 0) {
                            q.add(new int[] {row,col - 1});
                            processed[row][col - 1] = 1;
                        }
                    }

                    if (regSize > maxRegion) {
                        maxRegion = regSize;
                    }
                }
            }
        }

        System.out.println(maxRegion);
    }
}
