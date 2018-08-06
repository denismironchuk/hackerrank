import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class GridlandProvinces2 {
    private static final long HASH_BASE = 27;
    private static final long HASH_BASE2 = HASH_BASE * HASH_BASE;
    private static final long HASH_BASE4 = HASH_BASE2 * HASH_BASE2;
    private static final long MODULO = 1125899839733759l;
    //private static final long MODULO = 1000000007l;
    //private static final long MODULO = 1073676287l;

    private static long mult1(long a, long b) {
        if (b == 0) {
            return 0;
        }

        if (b % 2 == 0) {
            return mult((a + a) % MODULO, b / 2) % MODULO;
        } else {
            return (a + mult(a, (b - 1))) % MODULO;
        }
    }

    private static long mult(long a, long b) {
        //return (a * b) % MODULO;
        //if (a > b) {
            return mult1(a, b);
        //} else {
        //    return mult1(b, a);
        //}
    }

    private static void coutPaths(int n, char[][] grid, Set<Long> paths) {
        long[][] toDownToRight = new long[2][n];
        int toDownRow = 0;
        long toDownPrevHash = 0;
        long base = 1;

        for (int col = 0; col < n; col++) {
            toDownToRight[toDownRow][col] = (toDownPrevHash + mult(grid[toDownRow][col], base)) % MODULO;
            toDownPrevHash = toDownToRight[toDownRow][col];
            toDownRow = 1 - toDownRow;
            base = mult(base, HASH_BASE);

            toDownToRight[toDownRow][col] = (toDownPrevHash + mult(grid[toDownRow][col], base)) % MODULO;
            toDownPrevHash = toDownToRight[toDownRow][col];
            base = mult(base, HASH_BASE);
        }

        long [][] circularToLeft = new long[2][n];
        long [][] circularToRight = new long[2][n];

        long upToLeftPrevHash = 0;
        long downToLeftPrevHash = 0;

        long upToRightPrevHash = 0;
        long downToRightPrevHash = 0;

        base = HASH_BASE;
        for (int col = 0; col < n; col++) {
            circularToLeft[0][col] = (grid[0][col] + mult(upToLeftPrevHash, HASH_BASE) + mult(grid[1][col], base)) % MODULO;
            upToLeftPrevHash = circularToLeft[0][col];

            circularToLeft[1][col] = (grid[1][col] + mult(downToLeftPrevHash, HASH_BASE) + mult(grid[0][col], base)) % MODULO;
            downToLeftPrevHash = circularToLeft[1][col];

            circularToRight[0][n - col - 1] = (grid[0][n - col - 1] + mult(upToRightPrevHash, HASH_BASE) + mult(grid[1][n - col - 1], base)) % MODULO;
            upToRightPrevHash = circularToRight[0][n - col - 1];

            circularToRight[1][n - col - 1] = (grid[1][n - col - 1] + mult(downToRightPrevHash, HASH_BASE) + mult(grid[0][n - col - 1], base)) % MODULO;
            downToRightPrevHash = circularToRight[1][n - col - 1];

            base = mult(base, HASH_BASE2);
        }

        base = HASH_BASE2;
        for (int col = 1; col < n; col++) {
            circularToRight[0][col] = mult(circularToRight[0][col], base);
            circularToRight[1][col] = mult(circularToRight[1][col], base);
            base = mult(base, HASH_BASE2);
        }

        for (int col1 = 0; col1 < n; col1+=2) {
            long path = circularToLeft[0][col1];
            if (col1 < n - 1) {
                paths.add((path + circularToRight[1][col1 + 1]) % MODULO);
                for (int col2 = col1 + 3; col2 < n; col2 += 2) {
                    paths.add(((path + (toDownToRight[1][col2 - 1] - toDownToRight[1][col1] + MODULO)) % MODULO +
                            circularToRight[1][col2]) % MODULO);
                }

                for (int col2 = col1 + 2; col2 < n; col2 += 2) {
                    paths.add(((path + (toDownToRight[0][col2 - 1] - toDownToRight[1][col1] + MODULO)) % MODULO +
                            circularToRight[0][col2]) % MODULO);
                }
            } else {
                paths.add(path);
            }
        }

        for (int col1 = 1; col1 < n; col1+=2) {
            long path = circularToLeft[1][col1];
            if (col1 < n - 1) {
                paths.add((path + circularToRight[0][col1 + 1]) % MODULO);
                for (int col2 = col1 + 3; col2 < n; col2 += 2) {
                    paths.add(((path + (toDownToRight[0][col2 - 1] - toDownToRight[0][col1] + MODULO)) % MODULO +
                            circularToRight[0][col2]) % MODULO);
                }

                for (int col2 = col1 + 2; col2 < n; col2 += 2) {
                    paths.add(((path + (toDownToRight[1][col2 - 1] - toDownToRight[0][col1] + MODULO)) % MODULO +
                            circularToRight[1][col2]) % MODULO);
                }
            } else {
                paths.add(path);
            }
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
            int n = Integer.parseInt(br.readLine());
            char[][] grid = new char[2][n];
            String line1 = br.readLine();
            String line2 = br.readLine();
            for (int i = 0; i < line1.length(); i++) {
                grid[0][i] = line1.charAt(i);
                grid[1][i] = line2.charAt(i);
            }

            /*int n = 600;
            char[][] grid = new char[2][n];
            for (int i = 0; i < n; i++) {
                grid[0][i] = (char)('a' + (char)(26 * Math.random()));
                grid[1][i] = (char)('a' + (char)(26 * Math.random()));
            }*/

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
            System.out.println(paths.size());
            //System.out.println(end.getTime() - start.getTime() + "ms");
        }

    }
}
