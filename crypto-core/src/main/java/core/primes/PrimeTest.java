package core.primes;

import core.Zip;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

/**
 * Class provides probabilistic algorithms for primality testing.
 *
 * @author vadym
 * @see <a href="http://cryptowiki.net/index.php?title=%D0%90%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC%D1%8B,_%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D1%83%D0%B5%D0%BC%D1%8B%D0%B5_%D0%BF%D1%80%D0%B8_%D1%80%D0%B5%D0%B0%D0%BB%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D0%B8_%D0%B0%D1%81%D0%B8%D0%BC%D0%BC%D0%B5%D1%82%D1%80%D0%B8%D1%87%D0%BD%D1%8B%D1%85_%D0%BA%D1%80%D0%B8%D0%BF%D1%82%D0%BE%D1%81%D1%85%D0%B5%D0%BC">Алгоритмы, используемые при реализации асимметричных криптосхем</a>
 * @since 05.05.15 18:31
 */
public class PrimeTest {
    /**
     * Useful constants
     */
    public static final BigInteger TWO = BigInteger.valueOf(2);
    public static final BigInteger THREE = BigInteger.valueOf(3);
    public static final BigInteger FOUR = BigInteger.valueOf(4);
    public static final BigInteger EIGHT = BigInteger.valueOf(8);


    /**
     * Provides simple primality test by division on every number to <i>square(<code>n</code>)</i>.
     *
     * @param n integer number for testing
     * @return <code>true</code> if <code>n</code> is prime number, <code>false</code> otherwise
     * @see <a href="https://en.wikipedia.org/wiki/Primality_test#Pseudocode">Primality test</a>
     */
    public static boolean primality(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;

        int square = (int) Math.ceil(Math.sqrt(n));
        for (int i = 5; i <= square; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }

        return true;
    }


    /**
     * Provides Fermat prime test.
     *
     * @param n integer number for testing
     * @param t security parameter (iteration counter)
     * @return <code>false</code> if <code>n</code> is complex number, and
     * <code>true</code> if <code>n</code> is <b>probably</b> prime
     * (no guarantee that <code>n</code> is real prime).
     */
    public static boolean Fermat(final BigInteger n, int t) {
        // check trivial primes
        if (n.compareTo(ONE) < 0) return false;
        if (n.compareTo(THREE) < 0) return true;
        if (n.mod(TWO).equals(ZERO) || n.mod(THREE).equals(ZERO)) return false;

        BigInteger a, r;

        for (int i = 0; i < t; i++) {
            // a = random [2; n-2]
            a = Chaos.getInstance().getBigInteger(2, n.subtract(TWO));

            // r = a ^ (n-1) mod n
            r = a.modPow(n.subtract(ONE), n);

            // if (r != 1) then, complex number
            if (r.compareTo(ONE) != 0) return false;
        }

        // .. else, probably prime
        return true;
    }


    /**
     * Provides Solovay-Strassen prime test.
     * Instead of {@link #Fermat} test, able to recognize Carmichael number as a complex number.
     *
     * @param n integer number for testing
     * @param k security parameter (iteration counter)
     * @return <code>false</code> if <code>n</code> is complex number, and
     * <code>true</code> if <code>n</code> is <b>probably</b> prime
     * (no guarantee that <code>n</code> is real prime).
     * @see <a href="http://en.wikipedia.org/wiki/Carmichael_number">Carmichael number</a>
     */
    public static boolean SolovayStrassen(final BigInteger n, int k) {
        // check trivial primes
        if (n.compareTo(ONE) < 0) return false;
        if (n.compareTo(THREE) < 0) return true;
        if (n.mod(TWO).equals(ZERO) || n.mod(THREE).equals(ZERO)) return false;

        BigInteger a, condition, jacobi;

        for (int i = 0; i < k; ++i) {
            // a = random [2; n-1]
            a = Chaos.getInstance().getBigInteger(2, n.subtract(ONE));

            // if gcd(a,n ) > 1 then, complex number
            if (!a.gcd(n).equals(ONE)) {
                return false;
            }

            // if ( a^(n-1)/2 != Jacobi(a,n) mod n ) then, complex number
            jacobi = Jacobi(a, n);
            condition = a.modPow((n.subtract(ONE)).divide(TWO), n);

            if (!jacobi.equals(condition) && !jacobi.add(n).equals(condition)) {
                return false;
            }
        }

        // .. else, probably prime
        return true;
    }


    /**
     * Provides Miller-Rabin prime test.
     *
     * @param n integer number for testing
     * @return <code>false</code> if <code>n</code> is complex number, and
     * <code>true</code> if <code>n</code> is <b>probably</b> prime
     * (no guarantee that <code>n</code> is real prime).
     */
    public static boolean MillerRabin(final BigInteger n) {
        // check trivial primes
        if (n.compareTo(ONE) < 0) return false;
        if (n.compareTo(THREE) < 0) return true;
        if (n.mod(TWO).equals(ZERO) || n.mod(THREE).equals(ZERO)) return false;

        // recommended r is about log n base 2
        int r = n.bitCount();
        return MillerRabin(n, r);
    }

    /**
     * Provides Miller-Rabin prime test.
     *
     * @param n integer number for testing
     * @return <code>false</code> if <code>n</code> is complex number, and
     * <code>true</code> if <code>n</code> is <b>probably</b> prime
     * (no guarantee that <code>n</code> is real prime).
     * @see <a href="https://en.wikipedia.org/wiki/Miller–Rabin_primality_test">Miller-Rabin test</a>
     */
    public static boolean MillerRabin(final BigInteger n, int r) {
        // check trivial primes
        if (n.compareTo(ONE) < 0) return false;
        if (n.compareTo(THREE) < 0) return true;
        if (n.mod(TWO).equals(ZERO) || n.mod(THREE).equals(ZERO)) return false;

        BigInteger a, x;

        // compute s and t such that n-1 = 2^s * t, where t is odd
        Zip<Integer, BigInteger> zip = factorByBaseTwo(n.subtract(ONE));
        BigInteger t = zip.two;
        int s = zip.one;

        mainLoop:
        for (int i = 0; i < r; i++) {
            // a = random [2; n-2]
            a = Chaos.getInstance().getBigInteger(2, n.subtract(TWO));

            // x = a^t mod n
            x = a.modPow(t, n);

            // if (x = 1 || x = n-1) then, skip
            if (x.equals(ONE) || x.equals(n.subtract(ONE))) continue;

            for (int j = 0; j <= s-1; j++) {
                // x = a^2 mod n
                x = x.modPow(TWO, n);

                // if (x = 1) then, complex number
                if (x.equals(ONE)) return false;

                // if (x = n-1) then, skip
                if (x.equals(n.subtract(ONE))) continue mainLoop;
            }

            //  then, complex number
            return false;
        }

        // .. else, probably prime
        return true;
    }

    /**
     * Computes <i>s</i> and <i>t</i> such that <i>n = 2^s * t</i>, where <i>t</i> is odd
     * @param n number to be factorized
     * @return pair of <i>s</i> and <i>t</i>
     */
    private static Zip<Integer, BigInteger> factorByBaseTwo(final BigInteger n) {
        BigInteger m = n;
        int s = 0;

        while (m.mod(TWO).equals(ZERO)) {
            m = m.divide(TWO);
            s++;
        }

        return new Zip<>(s, m);
    }


    /**
     * Computes Jacobi Symbol.
     *
     * @param m positive integer
     * @param n odd integer
     * @return Jacobi symbol (<code>a</code>/<code>n</code>)
     * @see <a href="http://en.wikipedia.org/wiki/Jacobi_symbol">Jacobi Symbol</a>
     */
    public static BigInteger Jacobi(final BigInteger m, final BigInteger n) {

        BigInteger ans = ZERO;

        if (m.equals(ZERO)) {
            ans = n.equals(ONE) ? ONE : ZERO;
        } else if (m.equals(TWO)) {
            switch (n.remainder(EIGHT).intValue()) {
                case 1:
                case 7:
                    ans = ONE;
                    break;
                case 3:
                case 5:
                    ans = ONE.negate();
                    break;
            }
        } else if (m.compareTo(n) >= 0) {
            ans = Jacobi(m.remainder(n), n);
        } else if (m.remainder(TWO).equals(ZERO)) {
            ans = Jacobi(TWO, n).multiply(Jacobi(m.divide(TWO), n));
        } else {
            ans = (m.remainder(FOUR).equals(THREE) && n.remainder(FOUR).equals(THREE)) ?
                    Jacobi(n, m).negate() : Jacobi(n, m);
        }
        return ans;
    }
}
