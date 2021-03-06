package codejam.year2019.pt1;

import java.util.Objects;

public class Point {
    Rational x;
    Rational y;

    int index;

    public Point() {
    }

    public Point(Rational x, Rational y) {
        this.x = x;
        this.y = y;
    }

    public Point(int index, Rational x, Rational y) {
        this.x = x;
        this.y = y;
        this.index = index;
    }

    public Point setIndex(int index) {
        this.index = index;
        return this;
    }

    public static Point random(long xLimit, long yLimit) {
        Rational x = Rational.random(xLimit);
        Rational y = Rational.random(yLimit);
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

    public Point flipPoint(Point origin) {
        return new Point(Rational.TWO.mul(origin.x).substract(x), Rational.TWO.mul(origin.y).substract(y));
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", index=" + index +
                '}';
    }
}
