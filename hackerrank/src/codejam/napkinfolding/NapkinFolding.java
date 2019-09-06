package codejam.napkinfolding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class NapkinFolding {
    private static Fraction HALF = new Fraction(1, 2);
    private static Fraction NEG = new Fraction(-1, 1);

    private static class Fraction {
        private int chisl;
        private int znam;

        public Fraction(int chisl, int znam) {
            int gcd = gcd(chisl, znam);
            this.chisl = chisl / gcd;
            this.znam = znam / gcd;
        }

        public Fraction add(Fraction f) {
            int gcd = gcd(znam, f.znam);
            int commonZnam = (znam * f.znam) / gcd;
            int commonChisl = (chisl * f.znam + f.chisl * znam) / gcd;

            return new Fraction(commonChisl, commonZnam);
        }

        public Fraction mul(Fraction f) {
            int commonZnam = znam * f.znam;
            int commonChisl = chisl * f.chisl;
            int gcd = gcd(commonZnam, commonChisl);

            return new Fraction(commonChisl / gcd, commonZnam / gcd);
        }

        public Fraction inv(Fraction f) {
            return new Fraction(f.znam, f.chisl);
        }

        private int gcd(int a, int b) {
            return a >= b ? gcdInternal(a, b) : gcdInternal(b, a);
        }

        private int gcdInternal(int a, int b) {
            if (b == 0) {
                return a;
            } else {
                return gcdInternal(b, a % b);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t < T; t++) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(tkn1.nextToken());
            Fraction[][] points = new Fraction[n * 2][2];

            for (int i = 0; i < n * 2; i += 2) {
                StringTokenizer pointTkn = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(pointTkn.nextToken());
                int y = Integer.parseInt(pointTkn.nextToken());

                points[i][0] = new Fraction(x, 1);
                points[i][1] = new Fraction(y, 1);
            }

            for (int i = 2; i < n * 2; i += 2) {
                points[i - 1][0] = points[i][0].add(points[i - 2][0]).mul(HALF);
                points[i - 1][1] = points[i][1].add(points[i - 2][1]).mul(HALF);
            }

            points[n * 2 - 1][0] = points[0][0].add(points[2 * (n - 1)][0]).mul(HALF);
            points[n * 2 - 1][1] = points[0][1].add(points[2 * (n - 1)][1]).mul(HALF);

            int pointsToCheck = n;

            for (int i = 0; i < 2 * n; i++) {
                Fraction lineStartX = points[i][0];
                Fraction lineStartY = points[i][1];

                Fraction lineEndX = points[(i + pointsToCheck) % (2 * n)][0];
                Fraction lineEndY = points[(i + pointsToCheck) % (2 * n)][1];

                for (int pointToCheck = 1; pointToCheck < pointsToCheck; pointToCheck++) {
                    Fraction x1 = points[(i + pointToCheck) % (2 * n)][0];
                    Fraction y1 = points[(i + pointToCheck) % (2 * n)][1];

                    Fraction x2 = points[(i - pointToCheck + 2 * n) % (2 * n)][0];
                    Fraction y2 = points[(i - pointToCheck + 2 * n) % (2 * n)][1];
                }
            }
        }
    }

    private Fraction[] getSymmetryPoint(Fraction[] point, Fraction[] lineStart, Fraction[] lineEnd) {
        Fraction A = lineEnd[1].add(lineStart[1].mul(NEG));
        Fraction B = lineEnd[0].add(lineStart[0].mul(NEG));
        Fraction C = lineStart[0].mul(lineEnd[1]).add(lineEnd[0].mul(lineStart[1]).mul(NEG));

        /*int x = (B * (B * ax + A * ay) + C * A) / (A * A + B * B);
        int y = (A * (B * ax + A * ay) - C * B) / (A * A + B * B);

        x = 2 * x - ax;
        y = 2 * y - ay;

        g.drawLine(ax, ay, x, y);*/

        return null;
    }
}
