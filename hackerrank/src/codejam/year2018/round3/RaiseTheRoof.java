package codejam.year2018.round3;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RaiseTheRoof {

    private static final Random RND = new Random();
    private static int N = 15;

    private static class Point {
        private long x;
        private long y;
        private long z;

        public Point() {
            this.x = RND.nextInt(2000) - 1000;
            this.y = RND.nextInt(2000) - 1000;
            this.z = 1 + RND.nextInt(1000);
        }

        public Point(long x, long y, long z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }

    private static class Plane {
        private BigInteger a;
        private BigInteger b;
        private BigInteger c;
        private BigInteger d;

        public Plane(Point p1, Point p2, Point p3) {
            long d21 = p2.x - p1.x;
            long d22 = p2.y - p1.y;
            long d23 = p2.z - p1.z;
            long d31 = p3.x - p1.x;
            long d32 = p3.y - p1.y;
            long d33 = p3.z - p1.z;

            long aLong = d22 * d33 - d23 * d32;
            long bLong = d31 * d23 - d21 * d33;
            long cLong = d21 * d32 - d31 * d22;
            long dLong = - (aLong * p1.x + bLong * p1.y + cLong * p1.z);

            this.a = BigInteger.valueOf(aLong);
            this.b = BigInteger.valueOf(bLong);
            this.c = BigInteger.valueOf(cLong);
            this.d = BigInteger.valueOf(dLong);

            BigInteger gcd = this.a.gcd(this.b).gcd(this.c).gcd(this.d);
            this.a = this.a.divide(gcd);
            this.b = this.b.divide(gcd);
            this.c = this.c.divide(gcd);
            this.d = this.d.divide(gcd);
        }

        public Rational getRelativeAngle(Plane pln) {
            BigInteger chisl = this.a.multiply(pln.a).add(this.b.multiply(pln.b)).add(this.c.multiply(pln.c));
            BigInteger chisl2 = chisl.multiply(chisl);
            return new Rational(chisl2, pln.getNormalSquare().multiply(this.getNormalSquare()));
        }

        public BigInteger getNormalSquare() {
            return this.a.multiply(this.a).add(this.b.multiply(this.b)).add(this.c.multiply(this.c));
        }

        public Rational getZ(BigInteger x, BigInteger y) {
            return new Rational(this.d.add(this.a.multiply(x)).add(this.b.multiply(y)).negate(), this.c);
        }

        public Rational getZ(long x, long y) {
            return new Rational(this.d.add(this.a.multiply(BigInteger.valueOf(x))).add(this.b.multiply(BigInteger.valueOf(y))).negate(), this.c);
        }
    }

    public static void main_(String[] args) {
        Point p1 = new Point(95, -988, 90);
        Point p2 = new Point(-57, 584, 135);
        Point p3 = new Point(-191, -495, 125);

        Point p4 = new Point(697, 272, 360);

        Plane pln1 = new Plane(p1, p2, p3);
        Plane pln2 = new Plane(p2, p3, p4);
        Plane pln11 = new Plane(p2, p3, p1);
        Plane pln21 = new Plane(p3, p1, p4);

        System.out.println(pln1.getRelativeAngle(pln2));
        System.out.println(pln11.getRelativeAngle(pln21));
    }

    public static void main(String[] args) {
        List<Point> points = IntStream.range(0, N).mapToObj(i -> new Point()).collect(Collectors.toList());

        for (Point p1_ : points) {
            for (Point p2_ : points) {
                if (p1_.equals(p2_)) {
                    continue;
                }
                for (Point p3_ : points) {
                    if (p1_.equals(p3_) || p2_.equals(p3_)) {
                        continue;
                    }

                    Plane candidatePlane = new Plane(p1_, p2_, p3_);
                    boolean isValid = true;
                    for (Point p : points) {
                        if (p1_.equals(p) || p2_.equals(p) || p3_.equals(p)) {
                            continue;
                        }
                        if (candidatePlane.getZ(p.x, p.y).compareTo(new Rational(p.z)) >= 0) {
                            isValid = false;
                            break;
                        }
                    }

                    if (isValid) {
                        List<Point> processed = new ArrayList<>();
                        Set<Point> notProcessed = new HashSet<>(points);
                        processed.add(p1_);
                        processed.add(p2_);
                        processed.add(p3_);

                        notProcessed.remove(p1_);
                        notProcessed.remove(p2_);
                        notProcessed.remove(p3_);

                        Point p1 = p1_;
                        Point p2 = p2_;
                        Point p3 = p3_;

                        while (notProcessed.size() > 0) {
                            Point forReplace = p1;
                            Point p1_tmp = p2;
                            Point p2_tmp = p3;
                            Plane roof = new Plane(p1_tmp, p2_tmp, forReplace);

                            Comparator<Point> comparing = Comparator.comparing(p -> roof.getRelativeAngle(new Plane(p1_tmp, p2_tmp, p)));
                            Optional<Point> candidateOpt = notProcessed.stream()
                                    .filter(p -> new Plane(p1_tmp, p2_tmp, p).getZ(forReplace.x, forReplace.y).compareTo(new Rational(forReplace.z)) > 0)
                                    .sorted(comparing.reversed())
                                    .findFirst();

                            if (candidateOpt.isPresent()) {
                                Point candidate = candidateOpt.get();
                                notProcessed.remove(candidate);

                                candidatePlane = new Plane(p1_tmp, p2_tmp, candidate);

                                for (Point p : notProcessed) {
                                    if (candidatePlane.getZ(p.x, p.y).compareTo(new Rational(p.z)) > 0) {
                                        isValid = false;
                                    }
                                }

                                for (Point p : processed) {
                                    if (candidatePlane.getZ(p.x, p.y).compareTo(new Rational(p.z)) < 0) {
                                        isValid = false;
                                    }
                                }

                                processed.add(candidate);

                                if (!isValid) {
                                    break;
                                }

                                p1 = p2;
                                p2 = p3;
                                p3 = candidate;
                            } else {
                                isValid = false;
                                break;
                            }
                        }

                        if (isValid) {
                            System.out.println("SUCESS!!!!!!!!!!!");
                            System.out.println(p1_ + "\n" + p2_ + "\n" + p3_);
                            System.out.println(processed);
                            break;
                        } else {
                            //System.out.println("FAIL:(((((((((");
                            //System.out.println(notProcessed.size());
                        }
                    }
                }
            }
        }

        System.out.println("=======================");

        Point p1_ = points.stream().sorted(Comparator.comparing(p -> p.z)).findFirst().get();
        Comparator<Point> comparing2 = Comparator.comparing(p -> new Rational((p.z - p1_.z) * (p.z - p1_.z), (p.x - p1_.x) * (p.x - p1_.x) + (p.y - p1_.y) * (p.y - p1_.y) + (p.z - p1_.z) * (p.z - p1_.z)));
        Point p2_ = points.stream().filter(p -> !p.equals(p1_)).sorted(comparing2).findFirst().get();
        Point p3_ = null;

        for (Point p3_tmp : points) {
            if (p1_.equals(p3_tmp) || p2_.equals(p3_tmp)) {
                continue;
            }

            Plane candidatePlane = new Plane(p1_, p2_, p3_tmp);
            boolean isValid = true;
            for (Point p : points) {
                if (p1_.equals(p) || p2_.equals(p) || p3_tmp.equals(p)) {
                    continue;
                }
                if (candidatePlane.getZ(p.x, p.y).compareTo(new Rational(p.z)) >= 0) {
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                p3_ = p3_tmp;
                break;
            }
        }

        System.out.println(p1_ + "\n" + p2_ + "\n" + p3_);

        Set<Point> processed = new HashSet<>();
        Set<Point> notProcessed = new HashSet<>(points);

        notProcessed.remove(p1_);
        notProcessed.remove(p2_);
        notProcessed.remove(p3_);
        processed.add(p1_);
        processed.add(p2_);
        processed.add(p3_);

        Point p1 = p1_;
        Point p2 = p2_;
        Point p3 = p3_;

        boolean isValid = true;

        while (notProcessed.size() > 0) {
            Point forReplace = p1;
            Point p1_tmp = p2;
            Point p2_tmp = p3;
            Plane roof = new Plane(p1_tmp, p2_tmp, forReplace);

            Comparator<Point> comparing = Comparator.comparing(p -> roof.getRelativeAngle(new Plane(p1_tmp, p2_tmp, p)));
            Optional<Point> candidateOpt = notProcessed.stream()
                    .filter(p -> new Plane(p1_tmp, p2_tmp, p).getZ(forReplace.x, forReplace.y).compareTo(new Rational(forReplace.z)) > 0)
                    .sorted(comparing.reversed())
                    .findFirst();

            if (candidateOpt.isPresent()) {
                Point candidate = candidateOpt.get();
                notProcessed.remove(candidate);

                Plane candidatePlane = new Plane(p1_tmp, p2_tmp, candidate);

                for (Point p : notProcessed) {
                    if (candidatePlane.getZ(p.x, p.y).compareTo(new Rational(p.z)) > 0) {
                        isValid = false;
                    }
                }

                for (Point p : processed) {
                    if (candidatePlane.getZ(p.x, p.y).compareTo(new Rational(p.z)) < 0) {
                        isValid = false;
                    }
                }

                processed.add(candidate);

                if (!isValid) {
                    break;
                }

                p1 = p2;
                p2 = p3;
                p3 = candidate;
            } else {
                isValid = false;
                break;
            }
        }

        if (isValid) {
            System.out.println("SUCESS!!!!!!!!!!!");
        } else {
            System.out.println("FAIL:(((((((((");
        }
    }

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
            /*if (znam.equals(BigInteger.ZERO)) {
                throw new RuntimeException("Znam can't be zero!!!");
            }*/

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
}
