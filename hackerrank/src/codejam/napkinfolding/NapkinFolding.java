package codejam.napkinfolding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.StringTokenizer;

public class NapkinFolding {
    private static Fraction HALF = new Fraction(1, 2);
    private static Fraction NEG = new Fraction(-1, 1);
    private static Fraction TWO = new Fraction(2, 1);

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

            if (gcd == 0) {
                System.out.println();
            }

            return new Fraction(commonChisl / gcd, commonZnam / gcd);
        }

        public Fraction inv() {
            return new Fraction(znam, chisl);
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

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Fraction fraction = (Fraction) o;
            return chisl == fraction.chisl &&
                    znam == fraction.znam;
        }

        @Override
        public int hashCode() {
            return Objects.hash(chisl, znam);
        }

        @Override
        public String toString() {
            return String.format("%d/%d", chisl, znam);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t <= T; t++) {
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

            boolean solutionFound = false;
            for (int i = 0; !solutionFound && i < 2 * n; i++) {
                Fraction lineStartX = points[i][0];
                Fraction lineStartY = points[i][1];

                Fraction lineEndX = points[(i + pointsToCheck) % (2 * n)][0];
                Fraction lineEndY = points[(i + pointsToCheck) % (2 * n)][1];

                solutionFound = true;

                for (int pointToCheck = 1; solutionFound && pointToCheck < pointsToCheck; pointToCheck++) {
                    Fraction x1 = points[(i + pointToCheck) % (2 * n)][0];
                    Fraction y1 = points[(i + pointToCheck) % (2 * n)][1];

                    Fraction x2 = points[(i - pointToCheck + 2 * n) % (2 * n)][0];
                    Fraction y2 = points[(i - pointToCheck + 2 * n) % (2 * n)][1];

                    Fraction[] symPoint = getSymmetryPoint(new Fraction[]{x1, y1}, new Fraction[]{lineStartX, lineStartY}, new Fraction[]{lineEndX, lineEndY});

                    if (!symPoint[0].equals(x2) || !symPoint[1].equals(y2)) {
                        solutionFound = false;
                    }
                }

                if (solutionFound) {
                    System.out.printf("Case #%s: POSSIBLE\n%s %s %s %s\n", t, lineStartX, lineStartY, lineEndX, lineEndY);
                }
            }
            if (!solutionFound) {
                System.out.printf("Case #%s: IMPOSSIBLE\n", t);
            }
        }
    }

    private static Fraction[] getSymmetryPoint(Fraction[] point, Fraction[] lineStart, Fraction[] lineEnd) {
        Fraction A = lineEnd[1].add(lineStart[1].mul(NEG));
        Fraction B = lineEnd[0].add(lineStart[0].mul(NEG));
        Fraction C = lineStart[0].mul(lineEnd[1]).add(lineEnd[0].mul(lineStart[1]).mul(NEG));

        Fraction x = B.mul(B.mul(point[0]).add(A.mul(point[1]))).add(C.mul(A)).mul(A.mul(A).add(B.mul(B)).inv());
        Fraction y = A.mul(B.mul(point[0]).add(A.mul(point[1]))).add(C.mul(B).mul(NEG)).mul(A.mul(A).add(B.mul(B)).inv());

        x = TWO.mul(x).add(point[0].mul(NEG));
        y = TWO.mul(y).add(point[1].mul(NEG));

        return new Fraction[] {x, y};
    }
}
