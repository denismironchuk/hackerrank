import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashSet;
import java.util.LongSummaryStatistics;
import java.util.Set;

public class GridlandProvincesCorrect {
    private static final long HASH_BASE = 3;

    private static void coutPaths(int n,char[][] grid, Set<Long> paths) {
        long[][] toDownToRight = new long[2][n];
        int toDownRow = 0;
        long toDownPrevHash = 0;
        long base = 1;

        for (int col = 0; col < n; col++) {
            toDownToRight[toDownRow][col] = toDownPrevHash + grid[toDownRow][col] * base;
            toDownPrevHash = toDownToRight[toDownRow][col];
            toDownRow = 1 - toDownRow;
            base *= HASH_BASE;

            toDownToRight[toDownRow][col] = toDownPrevHash + grid[toDownRow][col] * base;
            toDownPrevHash = toDownToRight[toDownRow][col];
            base *= HASH_BASE;
        }

        long [][] circularToLeft = new long[2][n];
        long [][] circularToRight = new long[2][n];

        long upToLeftPrevHash = 0;
        long downToLeftPrevHash = 0;

        long upToRightPrevHash = 0;
        long downToRightPrevHash = 0;

        base = HASH_BASE;
        for (int col = 0; col < n; col++) {
            circularToLeft[0][col] = grid[0][col] + upToLeftPrevHash * HASH_BASE + grid[1][col] * base;
            upToLeftPrevHash = circularToLeft[0][col];

            circularToLeft[1][col] = grid[1][col] + downToLeftPrevHash * HASH_BASE + grid[0][col] * base;
            downToLeftPrevHash = circularToLeft[1][col];

            circularToRight[0][n - col - 1] = grid[0][n - col - 1] + upToRightPrevHash * HASH_BASE + grid[1][n - col - 1] * base;
            upToRightPrevHash = circularToRight[0][n - col - 1];

            circularToRight[1][n - col - 1] = grid[1][n - col - 1] + downToRightPrevHash * HASH_BASE + grid[0][n - col - 1] * base;
            downToRightPrevHash = circularToRight[1][n - col - 1];

            base *= HASH_BASE * HASH_BASE;
        }

        long base1 = 1;
        for (int col1 = 0; col1 < n; col1+=2) {
            long path = circularToLeft[0][col1];
            if (col1 < n - 1) {
                long base2 = base1 * HASH_BASE * HASH_BASE;
                paths.add(path + base2 * circularToRight[1][col1 + 1]);
                for (int col2 = col1 + 3; col2 < n; col2 += 2) {
                    base2 *= HASH_BASE * HASH_BASE * HASH_BASE * HASH_BASE;
                    paths.add(path + toDownToRight[1][col2 - 1] - toDownToRight[1][col1] +
                            base2 * circularToRight[1][col2]);
                }

                base2 = base1;
                for (int col2 = col1 + 2; col2 < n; col2 += 2) {
                    base2 *= HASH_BASE * HASH_BASE * HASH_BASE * HASH_BASE;
                    paths.add(path + toDownToRight[0][col2 - 1] - toDownToRight[1][col1] +
                            base2 * circularToRight[0][col2]);
                }
            } else {
                paths.add(path);
            }

            base1 *= HASH_BASE * HASH_BASE * HASH_BASE * HASH_BASE;
        }

        base1 = HASH_BASE * HASH_BASE;
        for (int col1 = 1; col1 < n; col1+=2) {
            long path = circularToLeft[1][col1];
            if (col1 < n - 1) {
                long base2 = base1 * HASH_BASE * HASH_BASE;
                paths.add(path + base2 * circularToRight[0][col1 + 1]);
                for (int col2 = col1 + 3; col2 < n; col2 += 2) {
                    base2 *= HASH_BASE * HASH_BASE * HASH_BASE * HASH_BASE;
                    paths.add(path + toDownToRight[0][col2 - 1] - toDownToRight[0][col1] +
                            base2 * circularToRight[0][col2]);
                }

                base2 = base1;
                for (int col2 = col1 + 2; col2 < n; col2 += 2) {
                    base2 *= HASH_BASE * HASH_BASE * HASH_BASE * HASH_BASE;
                    paths.add(path + toDownToRight[1][col2 - 1] - toDownToRight[0][col1] +
                            base2 * circularToRight[1][col2]);
                }
            } else {
                paths.add(path);
            }

            base1 *= HASH_BASE * HASH_BASE * HASH_BASE * HASH_BASE;
        }
    }

    private static void flipVertical(int n, char[][] grid) {
        for (int col = 0; col < n; col++) {
            char temp = grid[0][col];
            grid[0][col] = grid[1][col];
            grid[1][col] = temp;
        }
    }

    private static void flipHorizontal(int n, char[][] grid) {
        for (int col = 0; col < n / 2; col++) {
            char temp = grid[0][col];
            grid[0][col] = grid[0][n - col - 1];
            grid[0][n - col - 1] = temp;

            temp = grid[1][col];
            grid[1][col] = grid[1][n - col - 1];
            grid[1][n - col - 1] = temp;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int P = Integer.parseInt(br.readLine());

        for (int p = 0; p < P; p++) {
            /*int n = Integer.parseInt(br.readLine());
            char[][] grid = new char[2][n];
            String line1 = br.readLine();
            String line2 = br.readLine();
            for (int i = 0; i < line1.length(); i++) {
                grid[0][i] = line1.charAt(i);
                grid[1][i] = line2.charAt(i);
            }*/

            int n = 600;
            char[][] grid = new char[2][n];
            for (int i = 0; i < n; i++) {
                grid[0][i] = (char)('a' + (char)(26 * Math.random()));
                grid[1][i] = (char)('a' + (char)(26 * Math.random()));
            }

            Set<Long> paths = new HashSet<>();

            Date start = new Date();
            coutPaths(n, grid, paths);
            flipVertical(n, grid);
            coutPaths(n, grid, paths);
            flipHorizontal(n, grid);
            coutPaths(n, grid, paths);
            flipVertical(n, grid);
            coutPaths(n, grid, paths);

            Date end = new Date();

            System.out.println(end.getTime() - start.getTime() + "ms");
            System.out.println(paths.size());
            System.out.println(Long.MAX_VALUE);
            System.out.println(1000000007l * 1000000007l);
        }

    }
}
