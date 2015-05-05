package core.primes;

import core.arithmetic.Large;

/**
 * Class provides prime generator algorithms.
 *
 * @author vadym
 * @since 05.05.15 19:23
 */
public class PrimeGenerator {
    /**
     * Generates big prime number by Maurer’s algorithm.
     *
     * @param k number of bits in generated prime number
     * @return prime number
     * @see <a href="http://en.wikipedia.org/wiki/Carmichael_number">Carmichael number</a>
     */
    public static Large Maurer(int k) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }


    /**
     * Generates big prime number by Blum-Micali's algorithm.
     *
     * @param x0 seed
     * @param p odd prime
     * @param q prime
     * @return
     * @see <a href="http://en.wikipedia.org/wiki/Blum%E2%80%93Micali_algorithm">Blum-Micali algorithm</a>
     */
    public static Large BlumMicali(final Large x0, final Large p, final Large q) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }


    /**
     * Generates big prime number by Blum-Micali's algorithm.
     *
     * @param x0 seed
     * @param p odd prime
     * @param q prime
     * @return
     * @see <a href="http://en.wikipedia.org/wiki/Blum_Blum_Shub">Blum Blum Shub algorithm</a>
     */
    public static Large BBS(final Large x0, final Large p, final Large q) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
