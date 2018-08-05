import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class GridlandProvincesBig {
    private static final BigInteger HASH_BASE = BigInteger.valueOf(3);
    private static final BigInteger HASH_BASE2 = HASH_BASE.multiply(HASH_BASE);
    private static final BigInteger HASH_BASE4 = HASH_BASE2.multiply(HASH_BASE2);
    //private static final BigInteger MODULO = BigInteger.valueOf(1125899839733759l);
    private static final BigInteger MODULO = BigInteger.valueOf(1000000007l);

    private static void coutPaths(int n, BigInteger[][] grid, Set<BigInteger> paths) {
        BigInteger[][] toDownToRight = new BigInteger[2][n];
        int toDownRow = 0;
        BigInteger toDownPrevHash = BigInteger.ZERO;
        BigInteger base = BigInteger.ONE;

        for (int col = 0; col < n; col++) {
            toDownToRight[toDownRow][col] = toDownPrevHash.add(base.multiply(grid[toDownRow][col]).mod(MODULO)).mod(MODULO);
            toDownPrevHash = toDownToRight[toDownRow][col];
            toDownRow = 1 - toDownRow;
            base = base.multiply(HASH_BASE).mod(MODULO);

            toDownToRight[toDownRow][col] = toDownPrevHash.add(base.multiply(grid[toDownRow][col]).mod(MODULO)).mod(MODULO);
            toDownPrevHash = toDownToRight[toDownRow][col];
            base = base.multiply(HASH_BASE).mod(MODULO);
        }

        BigInteger [][] circularToLeft = new BigInteger[2][n];
        BigInteger [][] circularToRight = new BigInteger[2][n];

        BigInteger upToLeftPrevHash = BigInteger.ZERO;
        BigInteger downToLeftPrevHash = BigInteger.ZERO;

        BigInteger upToRightPrevHash = BigInteger.ZERO;
        BigInteger downToRightPrevHash = BigInteger.ZERO;

        base = HASH_BASE;
        for (int col = 0; col < n; col++) {
            circularToLeft[0][col] = upToLeftPrevHash.multiply(HASH_BASE).mod(MODULO).add(base.multiply(grid[1][col]).mod(MODULO)).add(grid[0][col]).mod(MODULO);
            upToLeftPrevHash = circularToLeft[0][col];

            circularToLeft[1][col] = downToLeftPrevHash.multiply(HASH_BASE).mod(MODULO).add(base.multiply(grid[0][col]).mod(MODULO)).add(grid[1][col]).mod(MODULO);
            downToLeftPrevHash = circularToLeft[1][col];

            circularToRight[0][n - col - 1] = grid[0][n - col - 1].add(upToRightPrevHash.multiply(HASH_BASE).mod(MODULO)).mod(MODULO).add(grid[1][n - col - 1].multiply(base).mod(MODULO)).mod(MODULO);
            upToRightPrevHash = circularToRight[0][n - col - 1];

            circularToRight[1][n - col - 1] = grid[1][n - col - 1].add(downToRightPrevHash.multiply(HASH_BASE).mod(MODULO)).mod(MODULO).add(grid[0][n - col - 1].multiply(base)).mod(MODULO).mod(MODULO);
            downToRightPrevHash = circularToRight[1][n - col - 1];

            base = base.multiply(HASH_BASE2).mod(MODULO);
        }

        BigInteger base1 = BigInteger.ONE;
        for (int col1 = 0; col1 < n; col1+=2) {
            BigInteger path = circularToLeft[0][col1];
            if (col1 < n - 1) {
                BigInteger base2 = base1.multiply(HASH_BASE2).mod(MODULO);
                paths.add(path.add(base2.multiply(circularToRight[1][col1 + 1]).mod(MODULO)).mod(MODULO));
                for (int col2 = col1 + 3; col2 < n; col2 += 2) {
                    base2 = base2.multiply(HASH_BASE4).mod(MODULO);
                    paths.add(path.add(toDownToRight[1][col2 - 1]).subtract(toDownToRight[1][col1]).add(
                            base2.multiply(circularToRight[1][col2])).mod(MODULO));
                }

                base2 = base1;
                for (int col2 = col1 + 2; col2 < n; col2 += 2) {
                    base2 = base2.multiply(HASH_BASE4).mod(MODULO);
                    paths.add(path.add(toDownToRight[0][col2 - 1]).subtract(toDownToRight[1][col1]).add(
                            base2.multiply(circularToRight[0][col2])).mod(MODULO));
                }
            } else {
                paths.add(path);
            }

            base1 = base1.multiply(HASH_BASE4).mod(MODULO);
        }

        base1 = HASH_BASE2;
        for (int col1 = 1; col1 < n; col1+=2) {
            BigInteger path = circularToLeft[1][col1];
            if (col1 < n - 1) {
                BigInteger base2 = base1.multiply(HASH_BASE2).mod(MODULO);
                paths.add(path.add(base2.multiply(circularToRight[0][col1 + 1])).mod(MODULO));
                for (int col2 = col1 + 3; col2 < n; col2 += 2) {
                    base2 = base2.multiply(HASH_BASE4).mod(MODULO);
                    paths.add(path.add(toDownToRight[0][col2 - 1]).subtract(toDownToRight[0][col1]).add(
                            base2.multiply(circularToRight[0][col2])).mod(MODULO));
                }

                base2 = base1;
                for (int col2 = col1 + 2; col2 < n; col2 += 2) {
                    base2 = base2.multiply(HASH_BASE4).mod(MODULO);
                    paths.add(path.add(toDownToRight[1][col2 - 1]).subtract(toDownToRight[0][col1]).add(
                            base2.multiply(circularToRight[1][col2])).mod(MODULO));
                }
            } else {
                paths.add(path);
            }

            base1 = base1 .multiply(HASH_BASE4).mod(MODULO);
        }
    }

    private static void flipVertical(int n, BigInteger[][] grid) {
        for (int col = 0; col < n; col++) {
            BigInteger temp = grid[0][col];
            grid[0][col] = grid[1][col];
            grid[1][col] = temp;
        }
    }

    private static void flipHorizontal(int n, BigInteger[][] grid) {
        for (int col = 0; col < n / 2; col++) {
            BigInteger temp = grid[0][col];
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
            BigInteger[][] grid = new BigInteger[2][n];
            String line1 = br.readLine();
            String line2 = br.readLine();
            for (int i = 0; i < line1.length(); i++) {
                grid[0][i] = BigInteger.valueOf((long)line1.charAt(i));
                grid[1][i] = BigInteger.valueOf((long)line2.charAt(i));
            }

            /*int n = 600;
            BigInteger[][] grid = new BigInteger[2][n];
            for (int i = 0; i < n; i++) {
                grid[0][i] = BigInteger.valueOf((long)('a' + (char)(26 * Math.random())));
                grid[1][i] = BigInteger.valueOf((long)('a' + (char)(26 * Math.random())));
            }*/

            Set<BigInteger> paths = new HashSet<>();

            Date start = new Date();
            coutPaths(n, grid, paths);
            flipVertical(n, grid);
            coutPaths(n, grid, paths);
            flipHorizontal(n, grid);
            coutPaths(n, grid, paths);
            flipVertical(n, grid);
            coutPaths(n, grid, paths);
            Date end = new Date();

            //System.out.println(end.getTime() - start.getTime() + "ms");

            System.out.println(paths.size());

        }

    }
}
