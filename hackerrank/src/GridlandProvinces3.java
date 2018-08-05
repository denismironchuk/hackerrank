import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class GridlandProvinces3 {
    private static int cnt = 0;
    private static void visitCells(int currRow, int currCol, char[][] grid, int[][] visited, long path, Set<Long> paths, long base, int startCol) {
        boolean moveWasMade = false;

        /*if (currCol - 1 > -1 && visited[currRow][currCol - 1] == 0) {
            visited[currRow][currCol - 1] = 1;
            visitCells(currRow, currCol - 1, grid, visited, path + grid[currRow][currCol - 1] * base, paths, base * 31);
            visited[currRow][currCol - 1] = 0;
            moveWasMade = true;
        }*/

        if (currCol + 1 < visited[0].length && visited[currRow][currCol + 1] == 0) {
            visited[currRow][currCol + 1] = 1;
            visitCells(currRow, currCol + 1, grid, visited, path + grid[currRow][currCol + 1] * base, paths, base * 31, startCol);
            visited[currRow][currCol + 1] = 0;
            moveWasMade = true;
        }

        int leftVisited = currCol == startCol ? 2 : visited[0][currCol - 1] + visited[1][currCol - 1];
        int rightVisited = currCol == visited[0].length - 1 ? 2 : visited[0][currCol + 1] + visited[1][currCol + 1];

        if (leftVisited == 2 || rightVisited == 2) {
            if (currRow - 1 > -1 && visited[currRow - 1][currCol] == 0) {
                visited[currRow - 1][currCol] = 1;
                visitCells(currRow - 1, currCol, grid, visited, path + grid[currRow - 1][currCol] * base, paths, base * 31, startCol);
                visited[currRow - 1][currCol] = 0;
                moveWasMade = true;
            }

            if (currRow + 1 < 2 && visited[currRow + 1][currCol] == 0){
                visited[currRow + 1][currCol] = 1;
                visitCells(currRow + 1, currCol, grid, visited, path + grid[currRow + 1][currCol] * base, paths, base * 31, startCol);
                visited[currRow + 1][currCol] = 0;
                moveWasMade = true;
            }
        }

        if (!moveWasMade) {
            cnt++;
            paths.add(path);
        }
    }

    public static void main(String[] args) throws IOException {
            int n = 600;
            char[][] grid = new char[2][n];
            for (int i = 0; i < n; i++) {
                grid[0][i] = (char)('a' + (char)(26 * Math.random()));
                grid[1][i] = (char)('a' + (char)(26 * Math.random()));
            }

            Date start = new Date();
            Set<Long> paths = new HashSet<>();
            int[][] visited = new int[2][n];
        /*int i = 0;
        int j = 1;
        visited[i][j] = 1;
        visitCells(i,j, grid, visited, grid[i][j], paths, 31, j);

        System.out.println(paths.size());*/

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < n; j++) {
                    visited[i][j] = 1;
                    visitCells(i,j, grid, visited, grid[i][j], paths, 31, j);
                    visited[i][j] = 0;
                    //System.out.println(paths.size());
                }
            }
            Date end = new Date();
            System.out.println(cnt);
            System.out.println(paths.size());
        System.out.println(end.getTime() - start.getTime() + "ms");
    }
}
