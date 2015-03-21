package core.arithmetic;

import core.Zip;

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
    private static final int BASE = 10_000;
    /**
     * NUmber of digits in base.
     */
    private static final int PACK = (int) Math.floor(Math.log10(BASE));

    /**
     * Every number could be represented as:<br/>
     *      <i>x = a<sub>n</sub> * BASE<sup>n</sup> + a<sub>n-1</sub> * BASE<sup>n-1</sup> + &hellip; +
     *      a<sub>1</sub> * BASE + a<sub>0</sub></i>,
     * where <i>a<sub>i</sub> &isin [0..BASE)</i>, <i>i = n&hellip;0</i><br/>
     * Coefficients are stored in little-endian format i.e.
     *      <i>[a<sub>0</sub>, &hellip; ,a<sub>n-1</sub>, a<sub>n</sub>]</i>
     */
    private Digits digits;

    /**
     * Stores a sign of number.
     */
    private boolean isNegative;


    /**
     * Helper constructor.
     */
    public Large() {
        digits = new Digits();
    }

    /**
     * Helper constructor.
     *
     * @param initial a list of internal coefficients whose represents a number in base {@value #BASE}
     */
    private Large(final List<Integer> initial) {
        this();
        digits.addAll(initial);
    }

    /**
     * Helper constructor.
     *
     * @param initial a list of internal coefficients whose represents a number in base {@value #BASE}
     * @param isNegative if <code>true</code> number considered as below zero.
     */
    private Large(final List<Integer> initial, boolean isNegative) {
        this(initial);
        this.isNegative = isNegative;
    }

    /**
     * Constructor of large number.
     *
     * @param x string representation of number.
     *          Could starts with '-' if number is negative.
     *          Leading zeros will be ignored.
     */
    public Large(String x) {
        // check input number format
        if (!x.matches("^-?\\d+$")) throw new IllegalArgumentException(String.format("Invalid number '%s'", x));

        // trim leading zeros
        x = x.replaceFirst("^0+(?!$)", "");

        // case x = 0
        if ("0".equals(x)) {
            digits = new Digits();
            digits.add(0);
            return;
        }

        // case x < 0
        if (x.charAt(0) == '-') {
            isNegative = true;
            x = x.substring(1);
        }

        // fill the coefficients in little-endian format
        digits = new Digits();
        for (int i = x.length(); i > 0; i -= PACK) {
            digits.add(Integer.parseInt(
                    (i < PACK) ? x.substring(0, i) : x.substring(i - PACK, i)
            ));
        }
    }


    /**
     * Creates a new copy of instance
     */
    @Override
    protected Large clone() {
        return new Large(this.digits, this.isNegative);
    }


    /**
     * Returns the absolute value of a {@link Large} number.
     * If negative, the negation is returned.
     *
     * @return the absolute value of of a large number.
     */
    public Large abs() {
        final Large result = this.clone();
        result.isNegative = false;
        return result;
    }

    /**
     * Returns the negation value of a {@link Large} number.
     *
     * @return the negation value of of a large number.
     */
    public Large negation() {
        final Large result = this.clone();
        if (sign() != 0) result.isNegative = true;
        return result;
    }


    /**
     * Returns the signum function of a {@link Large} number.
     *
     * @return 0 if number is equal zero;
     *         1 if number is greater than zero;
     *        -1 if number is less than zero;
     */
    public int sign() {
        return (digits.isEmpty() ||
                digits.size() == 1 && digits.getLSB() == 0) ? 0 :
                isNegative ? -1 : 1;
    }


    /**
     * Provides additional operation between two {@link Large} numbers.
     * Knuth's algorithm.
     *
     * @param other a large number to be added.
     * @return new instance of large number increased by value of the argument.
     */
    public Large add(final Large other) {
        // speed up with trivial values
        if (other.sign() == 0) return clone();

        // dealing with negative numbers
        if (isNegative) {
            // (-A) + (-B) = -(A + B)
            if (other.isNegative) return (abs().add(other.abs())).negation();
            // (-A) + B = B - A
            else return other.subtract(abs());
        }
        // A + (-B) = A - B
        else if (other.isNegative) return subtract(other.abs());

        // main algorithm: A + B

        final Large result = clone();
        int n = Math.max(digits.size(), other.digits.size());

        int carry = 0;
        int sum;        // < 2 * BASE

        for (int i = 0; i < n || carry != 0; i++) {
            sum = digits.get(i)
                    + other.digits.get(i)
                    + carry;
            carry = sum / BASE;

            result.digits.set(i, sum % BASE);
        }

        result.digits.trim();
        return result;
    }

    /**
     * Provides additional operation.
     *
     * @param x a number to be added.
     * @return new instance of large number increased by value of the argument.
     */
    public Large add(int x) {
        // speed up with trivial values
        if (x == 0) return clone();

        // dealing with negative numbers
        if (x < 0) throw new UnsupportedOperationException("Not implemented yet.");

        // main algorithm

        final Large result = clone();

        int carry = x;
        int sum;        // < 2 * BASE

        for (int i = 0; i < digits.size() && carry != 0; i++) {
            sum = digits.get(i) + carry;
            carry = sum / BASE;

            result.digits.set(i, sum % BASE);
        }

        result.digits.trim();
        return result;
    }


    /**
     * Provides subtraction operation. Knuth's algorithm.
     *
     * @param other a large number to be subtracted.
     * @return large number decreased by value of the argument.
     */
    public Large subtract(final Large other) {
        // speed up with trivial values
        if (other.sign() == 0) return clone();

        // dealing with negative numbers
        // A - (-B) = A + B
        if (other.isNegative) return add(other.abs());
        // (-A) - B = -(A + B)
        else if (isNegative) return abs().add(other).negation();
        // A - B, A < B = -(B - A)
        else if (compareTo(other) == -1) return other.subtract(this).negation();

        // main algorithm: A - B, A > B

        final Large result = clone();
        int borrow = 0;
        int diff;

        for (int i = 0; i < digits.size() || borrow != 0; i++) {
            diff = digits.get(i) - other.digits.get(i) + borrow;
            borrow = (diff < 0) ? -1 : 0;

            result.digits.set(i, (diff + BASE) % BASE);
        }

        result.digits.trim();
        return result;
    }


    /**
     * Provides subtraction operation.
     *
     * @param x a number to be subtracted.
     * @return new instance of large number decreased by value of the argument.
     */
    public Large subtract(int x) {
        // speed up with trivial values
        if (x == 0) return clone();

        // dealing with negative numbers
        if (x < 0) throw new UnsupportedOperationException("Not implemented yet.");

        // main algorithm

        final Large result = clone();
        int borrow = -x;
        int diff;

        for (int i = 0; i < digits.size() && borrow != 0; i++) {
            diff = digits.get(i) + borrow;
            borrow = (diff < 0) ? -1 : 0;

            result.digits.set(i, (diff + BASE) % BASE);
        }

        result.digits.trim();
        return result;
    }


    /**
     * Provides multiplication operation. Knuth's algorithm.
     * The complexity of computation is &Theta;(n<sup>2</sup>).
     *
     * @param other a large number to be multiplied.
     * @return large number multiplied by value of the argument.
     */
    public Large multiply(final Large other) {
        // speed up with trivial values
        if (other.sign() == 0) return new Large();

        // main algorithm: A * B

        final Large result = new Large();
        result.digits.set(digits.size() + other.digits.size() - 1, 0);

        int carry;
        int mul;        // < BASE^2

        for (int i = 0; i < digits.size(); i++) {
            if (digits.get(i) == 0) continue;

            carry = 0;

            for (int j = 0; j < other.digits.size() || carry != 0; j++) {
                mul = digits.get(i) * other.digits.get(j)
                        + result.digits.get(i+j)
                        + carry;
                carry = mul / BASE;

                result.digits.set(i+j, mul % BASE);
            }
        }

        result.isNegative = isNegative != other.isNegative;
        result.digits.trim();
        return result;
    }


    /**
     * Provides multiplication operation.
     *
     * @param x a number to be multiplied.
     * @return new instance of large number multiplied by value of the argument.
     */
    public Large multiply(int x) {
        // speed up with trivial values
        if (x == 0) return new Large();
        if (x == 1) return clone();

        // main algorithm

        final Large result = clone();

        int carry = 0;
        int mul;        // < BASE^2

        for (int i = 0; i < digits.size() || carry != 0; i++) {
            mul = digits.get(i) * x + carry;
            carry = mul / BASE;

            result.digits.set(i, mul % BASE);
        }

        result.isNegative = isNegative != (x < 0);
        result.digits.trim();
        return result;
    }

    /**
     * Implementation of Karatsuba multiplication algorithm.
     * The complexity of computation is &Theta;(n<sup>log&#8322;3</sup>).
     *
     * @param x first number to be multiplied
     * @param y second number to be multiplied
     * @return new instance of large number that is a result of multiplication
     * @see <a href="http://en.wikipedia.org/wiki/Karatsuba_algorithm">Karatsuba algorithm</a>
     */
    private Large karatsuba(final Large x, final Large y)  {
        // is x or y a "small" number?
        if (x.digits.size() == 1) return y.multiply(x.digits.getLSB());
        if (y.digits.size() == 1) return x.multiply(y.digits.getLSB());

        int mid = Math.max(x.digits.size(), y.digits.size()) / 2;

        final Zip<Large,Large> zipX = x.split(mid);
        final Zip<Large,Large> zipY = y.split(mid);

        Large z0 = karatsuba(zipX.one, zipY.one);
        Large z1 = karatsuba(zipX.one.add(zipX.two), zipY.one.add(zipY.two));
        Large z2 = karatsuba(zipX.two, zipY.two);

        return z2.shiftLeft(2 * mid)
                .add(
                        ( z1.subtract(z2).subtract(z0) )
                        .shiftLeft(mid)
                        .add(z0)
                );
    }


    /**
     * Returns a Large whose value is shifted left.
     * Equivalent as multiplying in a power of BASE<sup>n</sup>.
     *
     * @param n shift distance, in orders.
     * @return new instance of large number shifted left by <cone>n</cone> orders
     */
    public Large shiftLeft(int n) {
        final Large result = this.clone();

        for (int i = 0; i < n; i++) {
            result.digits.add(i, 0);
        }

        result.digits.trim();
        return result;
    }


    /**
     * Provides division operation with a reminder. Knuth's algorithm.
     *
     * @param other a large number to be divided.
     * @return new instance of large number divided by value of the argument and a reminder.
     */
    public Zip<Large, Large> divideAndModulo(final Large other) {
        // speed up with trivial values
        if (other.sign() == 0) throw new ArithmeticException("Division by zero");

        // main algorithm

        Large r = new Large();
        final Large q = new Large();

        final int norm = BASE / (other.digits.getMSB() + 1);

        final Large a = abs().multiply(norm);
        final Large b = other.abs().multiply(norm);

        for (int i = a.digits.size() - 1; i >= 0; i--) {
            r = r.shiftLeft(1);
            r = r.add(a.digits.get(i));

            int s1 = r.digits.get(b.digits.size());
            int s2 = r.digits.get(b.digits.size() - 1);
            int guess = (s1*BASE + s2) / b.digits.getMSB();

            r = r.subtract(b.multiply(guess));

            while (r.sign() < 0) {
                r = r.add(b);
                guess--;
            }

            q.digits.set(i, guess);
        }

        r = r.divide(norm);
        r.digits.trim();

        q.digits.trim();
        q.isNegative = isNegative != other.isNegative;

        return new Zip<>(q, r);
    }


    /**
     * Provides division operation.
     *
     * @param other a large number to be divided.
     * @return new instance of large number divided by value of the argument.
     */
    public Large divide(final Large other) {
        return divideAndModulo(other).one;
    }


    /**
     * Provides division operation.
     *
     * @param x a number to be divided.
     * @return new instance of large number divided by value of the argument.
     */
    public Large divide(int x) {
        // speed up with trivial values
        if (x == 0) throw new ArithmeticException("Division by zero");
        if (x == 1) return clone();

        // main algorithm

        final Large result = clone();

        int r = 0;
        int div;

        for (int i = digits.size() - 1; i >= 0; i--) {
            div = digits.get(i) + r * BASE;
            r = div % x;

            result.digits.set(i, div / x);
        }

        result.digits.trim();
        result.isNegative = isNegative != (x < 0);
        return result;
    }


    /**
     * Provides modulo operation.
     *
     * @param other a modulo value.
     * @return large number modulo by the argument.
     */
    public Large modulo(final Large other) {
        return divideAndModulo(other).two;
    }

    /**
     * Provides modulo operation.
     *
     * @param x a modulo value.
     * @return large number modulo by the argument.
     */
    public int modulo(int x) {
        if (x < 0) x = -x;

        int mod = 0;

        for (int i = digits.size() - 1; i >= 0; i--) {
            mod = (digits.get(i) + mod * BASE) % x;
        }

        return mod;
    }

    /**
     * Provides modulo operation. Barret's algorithm.
     *
     * @param other a modulo value.
     * @return large number modulo by the argument.
     * @see <a href="http://en.wikipedia.org/wiki/Barrett_reduction">Barrett reduction algorithm</a>
     */
    public Large barret(final Large other) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /**
     * Provides power operation.
     *
     * @param other a power value.
     * @return large number powered to value of the argument.
     * @see <a href="http://en.wikipedia.org/wiki/Exponentiation_by_squaring#2k-ary_method">2<sup>k</sup>-ary method</a>
     */
    public Large power(final Large other) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }


    /**
     * Provides power operation by modulo.
     *
     * @param other a power value.
     * @param n a modulo value.
     * @return large number powered to value of the argument by modulo.
     * @see <a href="http://en.wikipedia.org/wiki/Modular_exponentiation">Modular exponentiation methods</a>
     */
    public Large power(final Large other, final Large n) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }


    @Override
    public int compareTo(final Large other) {
        // compare numbers by signs
        if (!isNegative && other.isNegative) return 1;
        if (isNegative && !other.isNegative) return -1;

        // compare numbers by their sizes and signs
        if (digits.size() > other.digits.size()) {
            if (!isNegative && !other.isNegative) return 1;
            if (isNegative && other.isNegative) return -1;
        }

        if (digits.size() < other.digits.size()) {
            if (!isNegative && !other.isNegative) return -1;
            if (isNegative && other.isNegative) return 1;
        }

        // compare numbers by items in case sizes and signs are equal
        for (int i = digits.size() - 1; i >= 0; i--) {
            if (digits.get(i) > other.digits.get(i)) return 1;
            if (digits.get(i) < other.digits.get(i)) return -1;
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

        int i = digits.size() - 1;
        s.append(digits.get(i));

        final String pattern = "%0"+PACK+"d";

        for (i--; i >= 0; i--) {
            s.append(String.format(pattern, digits.get(i)));
        }

        return s.toString();
    }


    /**
     * Helper method. Splits number <i>x</i> into two separate numbers <i>(low, high)</i>, as follows:<br/>
     *      x = high * BASE<sup>index</sup> + low
     *
     * @param index point on splitting.
     * @return a pair (an {@link Zip}) of numbers
     */
    private Zip<Large,Large> split(int index) {
        return new Zip<>(
                new Large(digits.subList(0, index)),              // low part
                new Large(digits.subList(index, digits.size()))   // high part
        );
    }
}
