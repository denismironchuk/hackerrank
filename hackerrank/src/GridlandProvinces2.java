import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class GridlandProvinces2 {
    public static final long HASH_BASE = 31;
    public static final long SQR_HASH_BASE = HASH_BASE * HASH_BASE;

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

            //Left hashes
            long lastCharMultilier = HASH_BASE;
            long[][] leftCircularHash = new long[2][n];
            leftCircularHash[0][0] = grid[0][0] + grid[1][0] * lastCharMultilier;
            leftCircularHash[1][0] = grid[1][0] + grid[0][0] * lastCharMultilier;

            for (int col = 1; col < n; col++) {
                lastCharMultilier = lastCharMultilier * HASH_BASE * HASH_BASE;
                leftCircularHash[0][col] = grid[0][col] + leftCircularHash[0][col - 1] * HASH_BASE + grid[1][col] * lastCharMultilier;
                leftCircularHash[1][col] = grid[1][col] + leftCircularHash[1][col - 1] * HASH_BASE + grid[0][col] * lastCharMultilier;
            }

            Set<Long>[][] leftHashes = new Set[2][n];
            leftHashes[0][0] = new HashSet<>();
            leftHashes[0][0].add(leftCircularHash[0][0]);
            leftHashes[1][0] = new HashSet<>();
            leftHashes[1][0].add(leftCircularHash[1][0]);

            for (int col = 1; col < n; col++) {
                long startHash = grid[0][col] + grid[1][col] * HASH_BASE;
                leftHashes[0][col] = new HashSet<>();
                for (Long hash : leftHashes[1][col - 1]) {
                    leftHashes[0][col].add(hash * SQR_HASH_BASE + startHash);
                }
                leftHashes[0][col].add(leftCircularHash[0][col]);

                startHash = grid[1][col] + grid[0][col] * HASH_BASE;
                leftHashes[1][col] = new HashSet<>();
                for (Long hash : leftHashes[1][col - 1]) {
                    leftHashes[1][col].add(hash * SQR_HASH_BASE + startHash);
                }
                leftHashes[1][col].add(leftCircularHash[1][col]);
            }

            //Right hashes
            lastCharMultilier = HASH_BASE;
            long[][] rightCircularHash = new long[2][n];
            rightCircularHash[0][n - 1] = grid[0][n - 1] + grid[1][n - 1] * lastCharMultilier;
            rightCircularHash[1][n - 1] = grid[1][n - 1] + grid[0][n - 1] * lastCharMultilier;

            for (int col = n - 2; col > -1; col--) {
                lastCharMultilier = lastCharMultilier * HASH_BASE * HASH_BASE;
                rightCircularHash[0][col] = grid[0][col] + rightCircularHash[0][col + 1] * HASH_BASE + grid[1][col] * lastCharMultilier;
                rightCircularHash[1][col] = grid[1][col] + rightCircularHash[1][col + 1] * HASH_BASE + grid[0][col] * lastCharMultilier;
            }

            Set<Long>[][] rightHashes = new Set[2][n];
            rightHashes[0][n-1] = new HashSet<>();
            rightHashes[0][n-1].add(rightCircularHash[0][n-1]);
            rightHashes[1][n-1] = new HashSet<>();
            rightHashes[1][n-1].add(rightCircularHash[1][n-1]);

            for (int col = n-2; col > -1; col--) {
                long startHash = grid[0][col] + grid[1][col] * HASH_BASE;
                rightHashes[0][col] = new HashSet<>();
                for (Long hash : rightHashes[1][col + 1]) {
                    leftHashes[0][col].add(hash * SQR_HASH_BASE + startHash);
                }
                rightHashes[0][col].add(rightCircularHash[0][col]);

                startHash = grid[1][col] + grid[0][col] * HASH_BASE;
                rightHashes[1][col] = new HashSet<>();
                for (Long hash : rightHashes[1][col + 1]) {
                    rightHashes[1][col].add(hash * SQR_HASH_BASE + startHash);
                }
                rightHashes[1][col].add(rightCircularHash[1][col]);
            }
        }
    }
}
