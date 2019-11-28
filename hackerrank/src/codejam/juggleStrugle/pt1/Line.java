package codejam.juggleStrugle.pt1;

public class Line {
    Point p1;
    Point p2;

    Rational A;
    Rational B;
    Rational C;

    public Line(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;

        calcCoeficients();
    }

    private void calcCoeficients() {
        this.A = p2.y.substract(p1.y);
        this.B = p1.x.substract(p2.x);
        this.C = p1.y.mul(p2.x).substract(p1.x.mul(p2.y));
    }

    public void setP1(Point p1) {
        this.p1 = p1;
        calcCoeficients();
    }

    public void setP2(Point p2) {
        this.p2 = p2;
        calcCoeficients();
    }

    public boolean isParallel(Line l) {
        return A.divide(l.A).equals(B.divide(l.B)) && !A.divide(l.A).equals(C.divide(l.C));
    }

    public boolean pointBelongs(Point p) {
        return A.mul(p.x).add(B.mul(p.y)).add(C).equals(Rational.ZERO);
    }

    public Point getIntersection(Line l) {
        Rational znamX = A.mul(l.B).substract(l.A.mul(B));
        Rational znamY = B.mul(l.A).substract(l.B.mul(A));

        Rational x = l.C.mul(B).substract(C.mul(l.B)).divide(znamX);
        Rational y = l.C.mul(A).substract(C.mul(l.A)).divide(znamY);
        return new Point(x, y);
    }

    public int getPointSide(Point p) {
        return A.mul(p.x).add(B.mul(p.y)).add(C).compareTo(Rational.ZERO);
    }

    public boolean isPointInSegment(Point p) {
        return ((p1.x.compareTo(p.x) >= 0 && p2.x.compareTo(p.x) <= 0) || (p2.x.compareTo(p.x) >= 0 && p1.x.compareTo(p.x) <= 0)) &&
                ((p1.y.compareTo(p.y) >= 0 && p2.y.compareTo(p.y) <= 0) || (p2.y.compareTo(p.y) >= 0 && p1.y.compareTo(p.y) <= 0));
    }

    public Point getNearestPoint(Point p) {
        Rational dist1 = p.getSqrDist(p1);
        Rational dist2 = p.getSqrDist(p2);

        return dist1.compareTo(dist2) == -1 ? p1 : p2;
    }
}
