package codejam.year2022.round2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PixelatedCircleSolution {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int R = Integer.parseInt(br.readLine());
                int[][] fullBoard = new int[2 * R + 1][2 * R + 1];
                for (int x = -R; x <= R; x++) {
                    for (int y = -R; y <= R; y++) {
                        double xD = x;
                        double yD = y;
                        if (Math.round(Math.sqrt(xD * xD + yD * yD)) <= R) {
                            fullBoard[x + R][y + R] = 1;
                        }
                    }
                }

                int[][] radiusBoard = new int[2 * R + 1][2 * R + 1];
                for (int r = 0; r <= R; r++) {
                    for (int x = -r; x <= r; x++) {
                        double xD = x;
                        double rD = r;
                        int y = (int) Math.round(Math.sqrt(rD * rD - xD * xD));
                        radiusBoard[x + R][y + R] = 1;
                        radiusBoard[x + R][-y + R] = 1;
                        radiusBoard[y + R][x + R] = 1;
                        radiusBoard[-y + R][x + R] = 1;
                    }
                }
                int res = 0;
                for (int i = 0; i < 2 * R + 1; i++) {
                    for (int j = 0; j < 2 * R + 1; j++) {
                        if (fullBoard[i][j] != radiusBoard[i][j]) {
                            res++;
                        }
                    }
                }
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }

}
