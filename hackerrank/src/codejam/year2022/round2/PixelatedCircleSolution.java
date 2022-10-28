package codejam.year2022.round2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PixelatedCircleSolution {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                long R = Long.parseLong(br.readLine());
                long diagMismatch = 0;
                long misMatch = 0;
                long x = 1;
                for (x = 1; 2 * x * x <= R * R; x++) {
                    double xD = x;
                    double rD = R;
                    long maxY = Math.round(Math.sqrt(rD * rD - xD * xD));
                    //total mismatch in a row
                    long inc = (maxY + 1) - (R + 1 - x);
                    misMatch += inc;

                    //mismatch on diagonal
                    long diagRadius = Math.round(Math.sqrt(xD * xD + xD * xD));
                    double diagRadiusD = diagRadius;
                    if (Math.round(Math.sqrt(diagRadiusD * diagRadiusD - xD * xD)) != x
                            && Math.round(Math.sqrt((diagRadiusD + 1) * (diagRadiusD + 1) - xD * xD)) != x
                            && Math.round(Math.sqrt((diagRadiusD - 1) * (diagRadiusD - 1) - xD * xD)) != x) {
                        diagMismatch += 1;
                        misMatch -= 1;
                    }

                    //mismatch under diagonal
                    long maxRadiusUnderDiagonal = -1;
                    if (Math.round(Math.sqrt((diagRadiusD + 1) * (diagRadiusD + 1) - xD * xD)) < x) {
                        maxRadiusUnderDiagonal = diagRadius + 1;
                    } else if (Math.round(Math.sqrt(diagRadiusD * diagRadiusD - xD * xD)) < x) {
                        maxRadiusUnderDiagonal = diagRadius;
                    } else {
                        maxRadiusUnderDiagonal = diagRadius - 1;
                    }

                    long underDiagonalMismatch = (x - 1 + 1) - (maxRadiusUnderDiagonal + 1 - x);
                    misMatch -= underDiagonalMismatch;

                    //upper element mismatch
                    if (maxY + 1 > x && Math.round(Math.sqrt(x * x + (maxY + 1) * (maxY + 1))) <= R) {
                        misMatch += 1;
                    }
                }

                if (Math.round(Math.sqrt(R * R - x * x)) != x && Math.round(Math.sqrt(2 * x * x)) <= R) {
                    diagMismatch += 1;
                }

                System.out.printf("Case #%s: %s\n", t, 8 * misMatch + 4 * diagMismatch);
            }
        }
    }
}
