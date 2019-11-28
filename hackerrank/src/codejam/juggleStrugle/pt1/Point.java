package codejam.juggleStrugle.pt1;

import java.math.BigInteger;
import java.util.Objects;

public class Point {
    Rational x;
    Rational y;

    public Point() {
    }

    public Point(Rational x, Rational y) {
        this.x = x;
        this.y = y;
    }

    public static Point random(long xLimit, long yLimit) {
        Rational x = new Rational(BigInteger.valueOf((long)(Math.random() * xLimit)));
        Rational y = new Rational(BigInteger.valueOf((long)(Math.random() * yLimit)));
        return new Point(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return Objects.equals(x, point.x) &&
                Objects.equals(y, point.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Rational getSqrDist(Point p) {
        return x.substract(p.x).mul(x.substract(p.x)).add(y.substract(p.y).mul(y.substract(p.y)));
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
