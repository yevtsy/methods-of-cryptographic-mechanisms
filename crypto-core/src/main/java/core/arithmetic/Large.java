package core.arithmetic;

import org.apache.log4j.Logger;

/**
 * Implementation of arbitrary-precision arithmetic operations on large integer numbers.
 *
 * @author vadym
 * @author yevhen.tsyba
 * @since 07.03.15.
 */
public class Large implements Comparable<Large>, Cloneable {

    /**
     * Logger
     */
    final static Logger log = Logger.getLogger(Large.class);

    /**
     * Number base.
     */
    private static final byte BASE = 10;

    /**
     * Every number could be represented as:
     * <i>x = a<sub>n</sub> * BASE<sup>n</sup> + a<sub>n-1</sub> * BASE<sup>n-1</sup> + ... +
     * a<sub>1</sub> * BASE + a<sub>0</sub></i>,
     * where <i>a<sub>i</sub> &isin {0..BASE}</i>, <i>i = n..0</i>
     */
    private ExtendedArrayList<Integer> a;

    /**
     * Stores a sign of number
     */
    private boolean isNegative;


    public Large(String x) {
        // check input number format
        if (!x.matches("^-?\\d+$"))
            throw new IllegalArgumentException(String.format("Invalid number '%s'", x));
        // trim leading zeros
        x = x.replaceFirst("^0+(?!$)", "");

        // case x = 0
        if (x.equals("0")) {
            a = new ExtendedArrayList<>();
            a.add(0);
            return;
        }

        // case x < 0
        if (x.charAt(0) == '-') {
            isNegative = true;
            x = x.substring(1);
        }

        // fill the coefficients
        a = new ExtendedArrayList<>(x.length());
        for (int i = x.length() - 1; i > -1; i--) {
            a.add(Character.getNumericValue(x.charAt(i)));
        }
    }


    /**
     * Returns the absolute value of a large number.
     * If negative, the negation of a large number is returned.
     *
     * @return the absolute value of of a large number.
     */
    public Large abs() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }


    /**
     * Returns the signum function of a large number.
     *
     * @return 0 if number is equal zero;
     * 1 if number is greater than zero;
     * -1 if number is less than zero;
     */
    public int sign() {
        return (a.size() == 1 && a.get(0) == 0) ? 0 : isNegative ? -1 : 1;
    }


    /**
     * Provides additional operation.
     *
     * @param x a large number to be added.
     * @return large number increased by value of the argument.
     */
    public Large add(final Large x) throws CloneNotSupportedException {
        Large res = (Large) this.clone();
        int n = Math.max(a.size(), x.a.size());

        int carry = 0;
        int sum;

        for (int i = 0; i <= n; i++) {
            sum = res.a.get(i, 0) + x.a.get(i, 0) + carry;
            carry = sum / BASE;

            res.a.set(i, sum % BASE, 0);
        }

        res.a.trim(0);
        return res;
    }


    /**
     * Provides subtraction operation.
     *
     * @param x a large number to be subtracted.
     * @return large number decreased by value of the argument.
     */
    public Large subtract(final Large x) throws CloneNotSupportedException {
        Large res = (Large) this.clone();
        int carry = 0;
        int diff;

        for (int i = 0; i < res.a.size(); ++i) {
            diff = res.a.get(i, 0) - x.a.get(i, 0) + carry;
            carry = diff >= 0 ? 0 : -1;

            res.a.set(i, (diff + BASE) % BASE);
        }

        res.a.trim(0);
        return res;
    }


    /**
     * Provides multiplication operation.
     *
     * @param x a large number to be multiplied.
     * @return large number multiplied by value of the argument.
     * @see <a href="http://en.wikipedia.org/wiki/Karatsuba_algorithm">Karatsuba algorithm</a>
     */
    public Large multiply(final Large x) throws CloneNotSupportedException {
        return karatsubaMultiplication(this, x);
    }

    private Large karatsubaMultiplication(final Large x, final Large y) throws CloneNotSupportedException {
        if (isSmallNumber(x)) {
            Large result = y.multiplyBySimpleValue(getSmallValue(x));
            log.info("karatsuba return for values < " + x.getNonSignNumber() + " > and < " + y.getNonSignNumber() + " > result :" + result.toString());
            return result;
        }
        if (isSmallNumber(y)) {
            Large result = x.multiplyBySimpleValue(getSmallValue(y));
            log.info("karatsuba return for values < " + x.getNonSignNumber() + " > and < " + y.getNonSignNumber() + " > result :" + result.toString());
            return result;
        }

        int mid = Math.min(x.a.size(), y.a.size()) - 1;

        Large high1 = getPartialNumber(x, 0, x.getNonSignNumber().length() - mid);
        Large low1 = getPartialNumber(x, x.getNonSignNumber().length() - mid, x.getNonSignNumber().length());

        Large high2 = getPartialNumber(y, 0, y.getNonSignNumber().length() - mid);
        Large low2 = getPartialNumber(y, y.getNonSignNumber().length() - mid, y.getNonSignNumber().length());

        Large z0 = karatsubaMultiplication(low1, low2);
        Large z1 = karatsubaMultiplication(low1.add(high1), low2.add(high2));
        Large z2 = karatsubaMultiplication(high1, high2);

        Large result = z2.multiplyByOrder(2 * mid).add((z1.subtract(z2).subtract(z0)).multiplyByOrder(mid).add(z0));
        log.info("karatsuba return for values < " + x.getNonSignNumber() + " > and < " + y.getNonSignNumber() + " > result :" + result.toString());
        return result;
    }

    private boolean isSmallNumber(final Large x) {
        return x.getNonSignNumber().length() == 1;
    }

    private Integer getSmallValue(final Large x) {
        return Character.getNumericValue(x.getNonSignNumber().charAt(0));
    }

    private Large getPartialNumber(Large x, int startIndex, int endIndex) {
        return new Large(x.getNonSignNumber().substring(startIndex, endIndex));
    }

    /**
     * Simple multiplication by orders.
     * For example, number '123' with n = 3 returns number 123000
     *
     * @param n number of order
     * @return new large value multiplied by orders
     */
    public Large multiplyByOrder(int n) {
        StringBuilder str = new StringBuilder(this.getNonSignNumber());

        for (int i = 0; i < n; ++i) {
            str.append("0");
        }

        return new Large(str.toString());
    }

    /**
     * Provides multiplication large {@link core.arithmetic.Large} number by small like {@link java.lang.Integer}
     *
     * @param val - small number to multiply with large {@link core.arithmetic.Large}
     * @return new large {@link core.arithmetic.Large} number
     */
    public Large multiplyBySimpleValue(Integer val) {
        Large res = new Large(this.getNonSignNumber());
        int carry = 0;
        int len = this.a.size();
        int mul;

        for (int i = 0; i <= len; i++) {
            mul = res.a.get(i, 0) * val + carry;
            carry = mul / BASE;

            res.a.set(i, mul % BASE, 0);
        }

        return res;
    }


    /**
     * Provides division operation.
     *
     * @param x a large number to be divided.
     * @return large number divided by value of the argument.
     * @see <a href="http://en.wikipedia.org/wiki/Division_algorithm">Division algorithm</a>
     */
    public Large divide(final Large x) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }


    /**
     * Provides modulo operation.
     *
     * @param n a modulo value.
     * @return large number modulo by the argument.
     * @see <a href="http://en.wikipedia.org/wiki/Barrett_reduction">Barrett reduction algorithm</a>
     */
    public Large modulo(final Large n) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }


    /**
     * Provides power operation.
     *
     * @param x a power value.
     * @return large number powered to value of the argument.
     * @see <a href="http://en.wikipedia.org/wiki/Exponentiation_by_squaring#2k-ary_method">2<sup>k</sup>-ary method</a>
     */
    public Large power(final Large x) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }


    /**
     * Provides power operation by modulo.
     *
     * @param x a power value.
     * @param n a modulo value.
     * @return large number powered to value of the argument by modulo.
     * @see <a href="http://en.wikipedia.org/wiki/Modular_exponentiation">Modular exponentiation methods</a>
     */
    public Large power(final Large x, final Large n) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }


    @Override
    public int compareTo(final Large o) {
        // compare numbers by signs
        if (!this.isNegative && o.isNegative) {
            return 1;
        } else if (this.isNegative && !o.isNegative) {
            return -1;
        }

        // check numbers for equality
        if (this.a.equals(o.a)) {
            return 0;
        }

        // compare numbers by their sizes and signs
        if (this.a.size() > o.a.size()) {
            if (!this.isNegative && !o.isNegative)
                return 1;
            if (this.isNegative && o.isNegative) {
                return -1;
            }
        }

        if (this.a.size() < o.a.size()) {
            if (!this.isNegative && !o.isNegative)
                return -1;
            if (this.isNegative && o.isNegative)
                return 1;
        }

        // compare numbers by items in case sizes and signs are equal
        for (int i = 0; i < this.a.size(); ++i) {
            if (this.a.get(i) > o.a.get(i)) {
                return 1;
            } else if (this.a.get(i) < o.a.get(i)) {
                return -1;
            }
        }
        return 0;
    }


    /**
     * Represents a large number in pretty-format.
     *
     * @return string representation of a large number.
     */
    @Override
    public String toString() {
        if (sign() == 0) return "0";

        StringBuilder s = new StringBuilder();
        if (isNegative) s.append("-");

        for (int i = a.size() - 1; i >= 0; i--) {
            s.append(a.get(i));
        }

        return s.toString();
    }

    /**
     * Represents value without sign
     * <p>
     * For example, number '-123456' will returns as '123456'
     * Any positive values return as same as {@code toString} method
     *
     * @return string representation of number without sign
     */
    public String getNonSignNumber() {
        return this.isNegative ? this.toString().substring(1) : this.toString();
    }

    private String prettyPrint() {
        if (sign() == 0) return "0";

        StringBuilder s = new StringBuilder();
        if (isNegative) s.append("-(");

        int x;
        for (int i = a.size() - 1; i >= 0; i--) {
            x = a.get(i);
            if (x != 0) {
                s.append(x)
                        .append("*")
                        .append(BASE)
                        .append('^')
                        .append(i)
                        .append(" + ");
            }
        }
        s.delete(s.length() - 3, s.length());
        if (isNegative) s.append(")");

        return s.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
