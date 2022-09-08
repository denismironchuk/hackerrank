package kickstart.year2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class HamiltonianTour {

    private static char[][] map;
    private static char[][] fullMap;
    private static int r;
    private static int c;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                r = Integer.parseInt(tkn1.nextToken());
                c = Integer.parseInt(tkn1.nextToken());
                map = new char[r][c];
                for (int i = 0; i < r; i++) {
                    map[i] = br.readLine().toCharArray();
                }
                fullMap = new char[r * 2][c * 2];
                int freeCllCnt = 0;
                for (int row = 0; row < r; row++) {
                    for (int col = 0; col < c; col++) {
                        if (map[row][col] == '*') {
                            freeCllCnt += 4;
                        }
                        fullMap[row * 2][col * 2] = map[row][col];
                        fullMap[row * 2 + 1][col * 2] = map[row][col];
                        fullMap[row * 2][col * 2 + 1] = map[row][col];
                        fullMap[row * 2 + 1][col * 2 + 1] = map[row][col];
                    }
                }
                StringBuilder fullPath = new StringBuilder();
                buildPath(0, 0, 0, new int[r][c], new LinkedList<>(), fullPath);
                String path = fullPath.substring(1) + 'W';
                System.out.printf("Case #%s: %s\n", t, freeCllCnt == path.length() ? path : "IMPOSSIBLE");
            }
        }
    }

    /*
     dir:
       0 - down
       1 - right
       2 - up
       3 - left

    MOVES:
        down
        right
        up
        left
     DIRS:
        left
        down
        right
        up
     */

    private static final int[][] MOVES = new int[][] {{0, -1},{1, 0},{0, 1},{-1, 0}};
    private static final int[] DIRS = new int[] {3, 0, 1, 2};
    private static final char[] DIRS_LABES = new char[] {'S', 'E', 'N', 'W'};

    private static boolean buildPath(int curRow, int curCol, int dir, int[][] processed, LinkedList<int[]> linearPath, StringBuilder fullPath) {
        fullMap[curRow][curCol] = '#';
        fullPath.append(DIRS_LABES[dir]);
        processed[curRow / 2][curCol / 2] = 1;

        if (linearPath.size() == 0 || linearPath.getLast()[0] != curRow / 2 || linearPath.getLast()[1] != curCol / 2) {
            linearPath.add(new int[]{curRow / 2, curCol / 2});
        }
        if (isFullCell(curRow, curCol)) {
            linearPath.pollLast();
        }
        if (linearPath.isEmpty()) {
            return false;
        }

        int[] lastCell = linearPath.getLast();

        for (int i = 0; i < 4; i++) {
            int rowMove = MOVES[(i + dir) % 4][0];
            int colMove = MOVES[(i + dir) % 4][1];

            int nextRow = curRow + rowMove;
            int nextCol = curCol + colMove;

            if (nextRow >= 0 && nextRow < r * 2 && nextCol >= 0 && nextCol < c * 2 && fullMap[nextRow][nextCol] == '*') {
                if (processed[nextRow / 2][nextCol / 2] == 0 || (lastCell[0] == nextRow / 2 && lastCell[1] == nextCol / 2)) {
                    boolean res = buildPath(nextRow, nextCol, DIRS[(i + dir) % 4], processed, linearPath, fullPath);
                    if (!res) {
                        return false;
                    }
                }
            }
        }

        return true;
    }



    private static boolean isFullCell(int row, int col) {
        int rowGroup = row / 2;
        int colGroup = col / 2;
        return fullMap[rowGroup * 2][colGroup * 2] != '*' &&
        fullMap[rowGroup * 2 + 1][colGroup * 2] != '*' &&
        fullMap[rowGroup * 2][colGroup * 2 + 1] != '*' &&
        fullMap[rowGroup * 2 + 1][colGroup * 2 + 1] != '*';
    }
}
