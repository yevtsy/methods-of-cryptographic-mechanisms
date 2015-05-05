package core.primes;

import core.arithmetic.Large;

/**
 * Class provides probabilistic algorithms for primality testing.
 *
 * @author vadym
 * @since 05.05.15 18:31
 * @see <a href="http://cryptowiki.net/index.php?title=%D0%90%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC%D1%8B,_%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D1%83%D0%B5%D0%BC%D1%8B%D0%B5_%D0%BF%D1%80%D0%B8_%D1%80%D0%B5%D0%B0%D0%BB%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D0%B8_%D0%B0%D1%81%D0%B8%D0%BC%D0%BC%D0%B5%D1%82%D1%80%D0%B8%D1%87%D0%BD%D1%8B%D1%85_%D0%BA%D1%80%D0%B8%D0%BF%D1%82%D0%BE%D1%81%D1%85%D0%B5%D0%BC">Алгоритмы, используемые при реализации асимметричных криптосхем</a>
 */
public class PrimeTest {
    /**
     * Provides simple primality test by division on every number
     * from 2 to <i>square(<code>n</code>)</i>.
     *
     * @param n integer number for testing
     * @return <code>false</code> if <code>n</code> is complex number, and
     *         <code>true</code> if <code>n</code> is <b>probably</b> prime
     * @see <a href="https://en.wikipedia.org/wiki/Primality_test#Pseudocode">Primality test</a>
     */
    public static boolean primality(final Large n) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }


    /**
     * Provides Fermat prime test.
     *
     * @param n integer number for testing
     * @param t security parameter (iteration counter)
     * @return <code>false</code> if <code>n</code> is complex number, and
     *         <code>true</code> if <code>n</code> is <b>probably</b> prime
     *         (no guarantee that <code>n</code> is real prime).
     */
    public static boolean Fermat(final Large n, int t) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }


    /**
     * Provides Solovay-Strassen prime test.
     * Instead of {@link #Fermat} test, able to recognize Carmichael number as a complex number.
     *
     * @param n integer number for testing
     * @param k security parameter (iteration counter)
     * @return <code>false</code> if <code>n</code> is complex number, and
     *         <code>true</code> if <code>n</code> is <b>probably</b> prime
     *         (no guarantee that <code>n</code> is real prime).
     * @see <a href="http://en.wikipedia.org/wiki/Carmichael_number">Carmichael number</a>
     */
    public static boolean SolovayStrassen(final Large n, int k) {
        throw new UnsupportedOperationException("Not implemented yet.");
        // implementation needs Jacobi symbol
    }


    /**
     * Provides Miller-Rabin prime test.
     *
     * @param n integer number for testing
     * @param r security parameter (iteration counter)
     * @return <code>false</code> if <code>n</code> is complex number, and
     *         <code>true</code> if <code>n</code> is <b>probably</b> prime
     *         (no guarantee that <code>n</code> is real prime).
     */
    public static boolean MillerRabin(final Large n, int r) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }


    /**
     * Computes Jacobi Symbol.
     *
     * @param a positive integer
     * @param n odd integer
     * @return Jacobi symbol (<code>a</code>/<code>n</code>)
     * @see <a href="http://en.wikipedia.org/wiki/Jacobi_symbol">Jacobi Symbol</a>
     */
    private int Jacobi(final Large a, final Large n) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
