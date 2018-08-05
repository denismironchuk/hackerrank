import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class GridlandProvinces {
    private static void visitCells(int currRow, int currCol, char[][] grid, int[][] visited, StringBuilder path, Set<String> paths) {
        boolean moveWasMade = false;

        if (currCol - 1 > -1 && visited[currRow][currCol - 1] == 0) {
            visited[currRow][currCol - 1] = 1;
            visitCells(currRow, currCol - 1, grid, visited, path.append(grid[currRow][currCol - 1]), paths);
            path.deleteCharAt(path.length() - 1);
            visited[currRow][currCol - 1] = 0;
            moveWasMade = true;
        }

        if (currCol + 1 < visited[0].length && visited[currRow][currCol + 1] == 0) {
            visited[currRow][currCol + 1] = 1;
            visitCells(currRow, currCol + 1, grid, visited, path.append(grid[currRow][currCol + 1]), paths);
            path.deleteCharAt(path.length() - 1);
            visited[currRow][currCol + 1] = 0;
            moveWasMade = true;
        }
        int leftVisited = currCol == 0 ? 2 : visited[0][currCol - 1] + visited[1][currCol - 1];
        int rightVisited = currCol == visited[0].length - 1 ? 2 : visited[0][currCol + 1] + visited[1][currCol + 1];

        if (leftVisited == 2 || rightVisited == 2) {
            if (currRow - 1 > -1 && visited[currRow - 1][currCol] == 0){
                visited[currRow - 1][currCol] = 1;
                visitCells(currRow - 1, currCol, grid, visited, path.append(grid[currRow - 1][currCol]), paths);
                path.deleteCharAt(path.length() - 1);
                visited[currRow - 1][currCol] = 0;
                moveWasMade = true;
            }

            if (currRow + 1 < 2 && visited[currRow + 1][currCol] == 0){
                visited[currRow + 1][currCol] = 1;
                visitCells(currRow + 1, currCol, grid, visited, path.append(grid[currRow + 1][currCol]), paths);
                path.deleteCharAt(path.length() - 1);
                visited[currRow + 1][currCol] = 0;
                moveWasMade = true;
            }
        }

        if (!moveWasMade) {
            paths.add(path.toString());
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int P = Integer.parseInt(br.readLine());

        for (int p = 0; p < P; p++) {
            int n = Integer.parseInt(br.readLine());
            char[][] grid = new char[2][n];
            String line1 = br.readLine();
            String line2 = br.readLine();
            for (int i = 0; i < line1.length(); i++) {
                grid[0][i] = line1.charAt(i);
                grid[1][i] = line2.charAt(i);
            }

            Set<String> paths = new HashSet<>();
            int[][] visited = new int[2][n];
            StringBuilder path = new StringBuilder();
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < n; j++) {
                    visited[i][j] = 1;
                    visitCells(i,j, grid, visited, path.append(grid[i][j]), paths);
                    path.deleteCharAt(path.length() - 1);
                    visited[i][j] = 0;
                }
            }
            System.out.println(paths.size());
        }
    }
}
