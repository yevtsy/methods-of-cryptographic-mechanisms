package kpi.pti.crypto.core.arithmetic;

/**
 * Implementation of arbitrary-precision arithmetic operations on large integer numbers.
 *
 * @author vadym
 * @author yevhen.tsyba
 * @since 07.03.15.
 */
public class Large implements Comparable<Large> {
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
    public Large add(final Large x) {
        int n = Math.max(a.size(), x.a.size());

        int carry = 0;
        int sum;

        for (int i = 0; i < n; i++) {
            sum = a.get(i, 0) + x.a.get(i, 0) + carry;
            carry = sum / BASE;

            a.set(i, sum % BASE, 0);
        }

        return this;
    }


    /**
     * Provides subtraction operation.
     *
     * @param x a large number to be subtracted.
     * @return large number decreased by value of the argument.
     */
    public Large subtract(final Large x) {
        int carry = 0;
        int diff;

        for (int i = 0; i < a.size(); ++i) {
            diff = a.get(i, 0) - x.a.get(i, 0) + carry;
            carry = diff >= 0 ? 0 : -1;

            a.set(i, (diff + BASE) % BASE);
        }

        a.trim(0);
        return this;
    }


    /**
     * Provides multiplication operation.
     *
     * @param x a large number to be multiplied.
     * @return large number multiplied by value of the argument.
     * @see <a href="http://en.wikipedia.org/wiki/Karatsuba_algorithm">Karatsuba algorithm</a>
     */
    public Large multiply(final Large x) {
        throw new UnsupportedOperationException("Not implemented yet.");
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
        if (this.a.size() > o.a.size()){
            if(!this.isNegative && !o.isNegative)
                return 1;
            if(this.isNegative && o.isNegative){
                return -1;
            }
        }

        if (this.a.size() < o.a.size()){
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
}
