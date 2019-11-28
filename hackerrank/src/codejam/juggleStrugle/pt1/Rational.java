package codejam.juggleStrugle.pt1;

import java.math.BigInteger;
import java.util.Objects;

public class Rational implements Comparable<Rational> {

    public static final BigInteger NEGATIVE_ONE = BigInteger.valueOf(-1);
    public static final Rational ZERO = new Rational(BigInteger.ZERO);
    public static final Rational MINUS_ONE = new Rational(NEGATIVE_ONE);

    private BigInteger chisl;
    private BigInteger znam;

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
            return new Rational(znam.multiply(NEGATIVE_ONE), chisl.multiply(NEGATIVE_ONE));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rational rational = (Rational) o;
        return Objects.equals(chisl, rational.chisl) &&
                Objects.equals(znam, rational.znam);
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
