package codejam.juggleStrugle.pt1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

public class Rational implements Comparable<Rational> {

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

    public Rational mul(Rational r) {
        return new Rational(chisl.multiply(r.chisl), znam.multiply(r.znam));
    }

    public Rational substract(Rational r) {
        return add(r.mul(MINUS_ONE));
    }

    public Rational inverse() {
        if (chisl.compareTo(BigInteger.ZERO) == -1) {
            return new Rational(znam.negate(), chisl.negate());
        } else {
            return new Rational(znam, chisl);
        }
    }

    public Rational divide(Rational r) {
        return mul(r.inverse());
    }

    public int getValue() {
        return (chisl.divide(znam).intValue());
    }

    public double getDoubleValue() {
        return new BigDecimal(chisl, 6).divide(new BigDecimal(znam, 6), 6, RoundingMode.HALF_EVEN).doubleValue();
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
        /*return Objects.equals(chisl, rational.chisl) &&
                Objects.equals(znam, rational.znam);*/
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
