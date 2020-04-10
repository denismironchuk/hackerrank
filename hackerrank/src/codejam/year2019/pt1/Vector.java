package codejam.year2019.pt1;

public class Vector {
    Rational x;
    Rational y;

    public Vector(Line line) {
        this.x = line.p2.x.substract(line.p1.x);
        this.y = line.p2.y.substract(line.p1.y);
    }

    public Rational scalarMul(Vector v) {
        return x.mul(v.x).add(y.mul(v.y));
    }

    public Rational sqrLen() {
        return x.mul(x).add(y.mul(y));
    }
}
