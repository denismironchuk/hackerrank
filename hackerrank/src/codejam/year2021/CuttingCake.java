package codejam.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;

public class CuttingCake {
    public static class Rational implements Comparable<Rational> {

        public static final Rational ZERO = new Rational(0);
        public static final Rational ONE = new Rational(1);
        public static final Rational TWO = new Rational(2);
        public static final Rational MINUS_ONE = new Rational(-1);

        private BigInteger chisl;
        private BigInteger znam;

        public static Rational random(long lim) {
            return new Rational((long)(Math.random() * lim));
        }

        public Rational(BigInteger chisl, BigInteger znam) {
            if (znam.equals(BigInteger.ZERO)) {
                throw new RuntimeException("Znam can't be zero!!!");
            }

            BigInteger gcd = chisl.gcd(znam);
            this.chisl = chisl.divide(gcd);
            this.znam = znam.divide(gcd);
            normalSigns();
        }

        public Rational(BigInteger chisl) {
            this(chisl, BigInteger.ONE);
        }

        public Rational(long chisl) {
            this(BigInteger.valueOf(chisl), BigInteger.ONE);
        }

        public Rational(long chisl, long znam) {
            this(BigInteger.valueOf(chisl), BigInteger.valueOf(znam));
        }

        public Rational add(Rational r) {
            return new Rational(chisl.multiply(r.znam).add(r.chisl.multiply(znam)), znam.multiply(r.znam));
        }

        public Rational add(long v) {
            return add(new Rational(v));
        }

        public Rational mul(Rational r) {
            return new Rational(chisl.multiply(r.chisl), znam.multiply(r.znam));
        }

        public Rational mul(long v) {
            return mul(new Rational(v));
        }

        public Rational neg() {
            return mul(MINUS_ONE);
        }

        public Rational substract(Rational r) {
            return add(r.neg());
        }

        public Rational substract(long v) {
            return add(new Rational(-v));
        }

        public Rational inverse() {
            if (chisl.compareTo(BigInteger.ZERO) == -1) {
                return new Rational(znam.negate(), chisl.negate());
            } else {
                return new Rational(znam, chisl);
            }
        }

        public void normalSigns() {
            if (znam.compareTo(BigInteger.ZERO) == -1) {
                znam = znam.negate();
                chisl = chisl.negate();
            }
        }

        public Rational divide(Rational r) {
            return mul(r.inverse());
        }

        public Rational divide(long v) {
            return mul(new Rational(v).inverse());
        }

        public int getValue() {
            return (chisl.divide(znam).intValue());
        }

        public double getDoubleValue() {
            return new BigDecimal(chisl, 6).divide(new BigDecimal(znam, 6), 6, RoundingMode.HALF_EVEN).doubleValue();
        }

        public Rational abs() {
            return new Rational(this.chisl.abs(), this.znam);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Rational rational = (Rational) o;
            return compareTo(rational) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(chisl, znam);
        }

        @Override
        public String toString() {
            return chisl + "/" + znam;
        }

        @Override
        public int compareTo(Rational r) {
            return chisl.multiply(r.znam).compareTo(r.chisl.multiply(znam));
        }
    }

    public static class Point {
        private long x;
        private long y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Line {
        private Point p1;
        private Point p2;
        private long A;
        private long B;
        private long sign;
        private Rational k;
        private Rational b;

        public Line(Point p1, Point p2, long A, long B) {
            this.p1 = p1;
            this.p2 = p2;
            this.A = A;
            this.B = B;

            if (p1.x != p2.x) {
                this.sign = p2.x > p1.x ? 1 : -1;
                this.k = new Rational(p2.y - p1.y, p2.x - p1.x);
                this.b = new Rational(p1.y).substract(k.mul(p1.x));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                long p = Long.parseLong(tkn2.nextToken());
                long q = Long.parseLong(tkn2.nextToken());
                long r = Long.parseLong(tkn2.nextToken());
                long s = Long.parseLong(tkn2.nextToken());

                Rational S = new Rational(p * s - q * r, 2).abs();
                List<Long> pointX = new ArrayList<>();
                Map<Long, List<Line>> pointXLines = new HashMap<>();

                for (int i = 0; i < n; i++) {
                    StringTokenizer tkn3 = new StringTokenizer(br.readLine());
                    long x = Long.parseLong(tkn3.nextToken());
                    long y = Long.parseLong(tkn3.nextToken());
                    long a = Long.parseLong(tkn3.nextToken());
                    long b = Long.parseLong(tkn3.nextToken());

                    Point p1 = new Point(x, y);
                    Point p2 = new Point(x + p, y + q);
                    Point p3 = new Point(x + r, y + s);

                    if (!pointXLines.containsKey(p1.x)) {
                        pointX.add(p1.x);
                        pointXLines.put(p1.x, new ArrayList<>());
                    }
                    if (!pointXLines.containsKey(p2.x)) {
                        pointX.add(p2.x);
                        pointXLines.put(p2.x, new ArrayList<>());
                    }
                    if (!pointXLines.containsKey(p3.x)) {
                        pointX.add(p3.x);
                        pointXLines.put(p3.x, new ArrayList<>());
                    }

                    Line line1 = new Line(p1, p2, a, b);
                    Line line2 = new Line(p2, p3, a, b);
                    Line line3 = new Line(p3, p1, a, b);

                    if (line1.p1.x != line1.p2.x) {
                        pointXLines.get(p1.x).add(line1);
                        pointXLines.get(p2.x).add(line1);
                    }

                    if (line2.p1.x != line2.p2.x) {
                        pointXLines.get(p2.x).add(line2);
                        pointXLines.get(p3.x).add(line2);
                    }

                    if (line3.p1.x != line3.p2.x) {
                        pointXLines.get(p1.x).add(line3);
                        pointXLines.get(p3.x).add(line3);
                    }
                }

                pointX.sort(Comparator.comparingLong(l -> l));
                List<Rational> segmentA = new ArrayList<>();
                List<Rational> segmentB = new ArrayList<>();
                Set<Line> currentLines = new HashSet<>();

                for (int i = 0; i < pointX.size() - 1; i++) {
                    long x1 = pointX.get(i);
                    long x2 = pointX.get(i + 1);
                    for (Line line : pointXLines.get(x1)) {
                        if (currentLines.contains(line)) {
                            currentLines.remove(line);
                        } else {
                            currentLines.add(line);
                        }
                    }

                    Rational segmentASum = Rational.ZERO;
                    Rational segmentBSum = Rational.ZERO;

                    for (Line line : currentLines) {
                        segmentASum = segmentASum.add(line.k.mul(x1 + x2).add(line.b.mul(Rational.TWO)).mul(line.sign).mul(line.A).mul(x2 - x1));
                        segmentBSum = segmentBSum.add(line.k.mul(x1 + x2).add(line.b.mul(Rational.TWO)).mul(line.sign).mul(line.B).mul(x2 - x1));
                    }

                    segmentASum = segmentASum.divide(S.mul(Rational.TWO));
                    segmentBSum = segmentBSum.divide(S.mul(Rational.TWO));

                    segmentA.add(segmentASum);
                    segmentB.add(segmentBSum);
                }

                List<Rational> leftSums = new ArrayList<>();
                leftSums.add(Rational.ZERO);
                for (Rational seg : segmentA) {
                    leftSums.add(leftSums.get(leftSums.size() - 1).add(seg));
                }

                List<Rational> rightSums = new ArrayList<>();
                for (int i = 0; i < segmentB.size(); i++) {
                    rightSums.add(null);
                }
                rightSums.add(Rational.ZERO);

                for (int i = segmentB.size() - 1; i >= 0; i--) {
                    rightSums.set(i, rightSums.get(i + 1).add(segmentB.get(i)));
                }

                List<Rational> candidates = new ArrayList<>();

                for (int i = 0; i < pointX.size(); i++) {
                    candidates.add(rightSums.get(i).substract(leftSums.get(i)).abs());
                }

                currentLines.clear();

                for (int i = 0; i < pointX.size() - 1; i++) {
                    long x1 = pointX.get(i);
                    long x2 = pointX.get(i + 1);
                    for (Line line : pointXLines.get(x1)) {
                        if (currentLines.contains(line)) {
                            currentLines.remove(line);
                        } else {
                            currentLines.add(line);
                        }
                    }

                    Rational x1Val = leftSums.get(i).substract(rightSums.get(i));
                    Rational x2Val = leftSums.get(i + 1).substract(rightSums.get(i + 1));

                    if (x1Val.mul(x2Val).compareTo(Rational.ZERO) < 1) {
                        candidates.add(Rational.ZERO);
                    }

                    Rational chisl = Rational.ZERO;
                    Rational znam = Rational.ZERO;

                    for (Line line : currentLines) {
                        chisl = chisl.substract(line.b.mul(line.sign).mul(line.A + line.B));
                        znam = znam.add(line.k.mul(line.sign).mul(line.A + line.B));
                    }

                    if (!znam.equals(Rational.ZERO)) {
                        Rational extremum = chisl.divide(znam);
                        extremum.normalSigns();
                        if (extremum.compareTo(new Rational(x1)) == 1 && extremum.compareTo(new Rational(x2)) == -1) {
                            Rational extremumValue = Rational.ZERO;

                            for (Line line : currentLines) {
                                extremumValue = extremumValue.add(line.k.mul(extremum.add(x1)).add(line.b.mul(Rational.TWO)).mul(line.sign).mul(line.A).mul(extremum.substract(x1)));
                                extremumValue = extremumValue.substract(line.k.mul(extremum.add(x2)).add(line.b.mul(Rational.TWO)).mul(line.sign).mul(line.B).mul(extremum.neg().add(x2)));
                            }

                            extremumValue = extremumValue.divide(S.mul(Rational.TWO));
                            extremumValue = extremumValue.add(leftSums.get(i));
                            extremumValue = extremumValue.substract(rightSums.get(i + 1));

                            candidates.add(extremumValue.abs());

                            if (x1Val.mul(extremumValue).compareTo(Rational.ZERO) < 1) {
                                candidates.add(Rational.ZERO);
                            }
                        }
                    }
                }
                candidates.sort(Rational::compareTo);
                System.out.printf("Case #%s: %s\n", t, candidates.get(0));
            }
        }
    }
}
