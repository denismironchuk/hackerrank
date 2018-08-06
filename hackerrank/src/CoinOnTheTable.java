import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CoinOnTheTable {
    private final char[][] table;
    private final int n;
    private final int m;
    private final int k;
    private final int[][] time;

    public CoinOnTheTable(final char[][] table, final int n, final int m, final int k) {
        this.table = table;
        this.n = n;
        this.m = m;
        this.k = k;
        this.time = new int[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                time[i][j] = Integer.MAX_VALUE;
            }
        }
    }

    private boolean moveOnTable(int row, int col, int currentTime, List<int[]> visitedPoints, int[][] isPointAdded) {
        if (currentTime > k || row >= n || col >= m || row < 0 || col < 0 || time[row][col] <= currentTime) {
            return false;
        }

        if (isPointAdded[row][col] == 0) {
            visitedPoints.add(new int[]{row, col});
            isPointAdded[row][col] = 1;
        }
        time[row][col] = currentTime;

        int nextTime = currentTime + 1;
        switch (table[row][col]) {
            case 'U':
                return moveOnTable(row - 1, col, nextTime, visitedPoints, isPointAdded);
            case 'D':
                return moveOnTable(row + 1, col, nextTime, visitedPoints, isPointAdded);
            case 'L':
                return moveOnTable(row, col - 1, nextTime, visitedPoints, isPointAdded);
            case 'R':
                return moveOnTable(row, col + 1, nextTime, visitedPoints, isPointAdded);
            case '*':
                return true;
        }

        return false;
    }

    private void run() throws IOException {
        List<int[]> visitedPoints = new ArrayList<>();
        boolean isStarReached = moveOnTable(0 ,0, 0, visitedPoints, new int[n][m]);
        int changes = 0;
        while (!isStarReached && !visitedPoints.isEmpty()) {
            changes++;
            List<int[]> pointsToVisit = new ArrayList<>();
            int[][] isPointAdded = new int[n][m];

            for (int[] visitedPoint : visitedPoints) {
                int pointRow = visitedPoint[0];
                int pointCol = visitedPoint[1];

                char pointDirection = table[pointRow][pointCol];
                int pointTime = time[pointRow][pointCol];

                if (pointDirection != 'U') {
                    isStarReached = moveOnTable(pointRow - 1, pointCol, pointTime + 1, pointsToVisit, isPointAdded);
                }

                if (!isStarReached && pointDirection != 'D') {
                    isStarReached = moveOnTable(pointRow + 1, pointCol, pointTime + 1, pointsToVisit, isPointAdded);
                }

                if (!isStarReached && pointDirection != 'L') {
                    isStarReached = moveOnTable(pointRow, pointCol - 1, pointTime + 1, pointsToVisit, isPointAdded);
                }

                if (!isStarReached && pointDirection != 'R') {
                    isStarReached = moveOnTable(pointRow, pointCol + 1, pointTime + 1, pointsToVisit, isPointAdded);
                }

                if (isStarReached) {
                    break;
                }
            }
            visitedPoints = pointsToVisit;
        }

        System.out.println(isStarReached ? changes : -1);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn = new StringTokenizer(br.readLine(), " ");
        int n = Integer.parseInt(tkn.nextToken());
        int m = Integer.parseInt(tkn.nextToken());
        int k = Integer.parseInt(tkn.nextToken());

        char[][] table = new char[n][m];

        for (int i = 0; i < n; i++) {
            String line = br.readLine();
            for (int j = 0; j < m; j++) {
                table[i][j] = line.charAt(j);
            }
        }

        new CoinOnTheTable(table, n, m, k).run();
    }
}
