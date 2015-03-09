package core.arithmetic;

import java.util.List;

/**
 * Implementation of arbitrary-precision arithmetic operations on large integer numbers.
 *
 * @author vadym
 * @author yevhen.tsyba
 * @since 07.03.15.
 */
public class Large implements Comparable<Large>, Cloneable {
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


    private Large(final List<Integer> initial) {
        a = new ExtendedArrayList<>();
        a.addAll(initial);
    }

    public Large(final List<Integer> initial, boolean isNegative) {
        this(initial);
        this.isNegative = isNegative;
    }

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
        for (int i = x.length() - 1; i >= 0; i--) {
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
        final Large result = this.clone();
        result.isNegative = false;
        return result;
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
    public Large add(final Large x) {
        final Large result = this.clone();
        int n = Math.max(a.size(), x.a.size());

        int carry = 0;
        int sum;

        for (int i = 0; i <= n; i++) {
            sum = a.get(i, 0) + x.a.get(i, 0) + carry;
            carry = sum / BASE;

            result.a.set(i, sum % BASE, 0);
        }

        result.a.trim(0);
        return result;
    }


    /**
     * Provides subtraction operation.
     *
     * @param x a large number to be subtracted.
     * @return large number decreased by value of the argument.
     */
    public Large subtract(final Large x) {
        final Large result = this.clone();
        int carry = 0;
        int diff;

        for (int i = 0; i < a.size(); ++i) {
            diff = a.get(i, 0) - x.a.get(i, 0) + carry;
            carry = (diff < 0) ? -1 : 0;

            result.a.set(i, (diff + BASE) % BASE);
        }

        result.a.trim(0);
        return result;
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

    private Large karatsubaMultiplication(final Large x, final Large y)  {
        // x is a small number?
        if (x.a.size() == 1) return y.multiply(x.a.get(0));

        // y is a small number?
        if (y.a.size() == 1) return x.multiply(y.a.get(0));

        int mid = Math.min(x.a.size(), y.a.size()) - 1;

        Large low1 = x.getPartialNumber(0, mid);
        Large high1 = x.getPartialNumber(mid, x.a.size());

        Large low2 = y.getPartialNumber(0, mid);
        Large high2 = y.getPartialNumber(mid, y.a.size());

        Large z0 = karatsubaMultiplication(low1, low2);
        Large z1 = karatsubaMultiplication(low1.add(high1), low2.add(high2));
        Large z2 = karatsubaMultiplication(high1, high2);

        Large result = z2.shiftLeft(2 * mid).add((z1.subtract(z2).subtract(z0)).shiftLeft(mid).add(z0));
        return result;
    }


    private Large getPartialNumber(int startIndex, int endIndex) {
        return new Large(a.subList(startIndex, endIndex));
    }


    /**
     * Returns a Large whose value is shifted left.
     *
     * @param n shift distance, in orders.
     * @return new large value shifted left by <cone>n</cone> orders
     */
    public Large shiftLeft(int n) {
        final Large result = this.clone();

        for (int i = 0; i < n; i++) {
            result.a.add(i, 0);
        }

        result.a.trim(0);
        return result;
    }

    /**
     * Provides multiplication large {@link Large} number by small like {@link Integer}
     *
     * @param x small number to multiply with large {@link Large}
     * @return new large {@link core.arithmetic.Large} number
     */
    public Large multiply(final Integer x) {
        if (x < 0 || x >= BASE)
            throw new IllegalArgumentException(String.format(
                    "For this method argument should be in [0..%d), but was %d",
                    BASE, x
            ));

        final Large res = this.abs();
        int carry = 0;
        int mul;

        for (int i = 0; i <= a.size(); i++) {
            mul = a.get(i, 0) * x + carry;
            carry = mul / BASE;

            res.a.set(i, mul % BASE, 0);
        }

        res.a.trim(0);
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


    @Override
    protected Large clone() {
        return new Large(this.a, this.isNegative);
    }
}
