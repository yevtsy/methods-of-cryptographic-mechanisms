package core.primes;

import core.arithmetic.Large;

import java.math.BigInteger;

/**
 * Class provides prime generator algorithms.
 *
 * @author vadym
 * @since 05.05.15 19:23
 */
public class PrimeGenerator {

    /**
     * Length of the sequence
     */
    private static int length = 512;

    /**
     * Useful constant
     */
    private static final BigInteger TWO = new BigInteger("2");

    /**
     * Base of calculations
     */
    private static final int radix = 2;

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
    public static BigInteger BlumMicali(final BigInteger x0, final BigInteger p, final BigInteger q) {
        StringBuilder result = new StringBuilder();

        BigInteger x = q.modPow(x0, p);

        for (int i = 0; i < length*8; ++i) {
            x = q.modPow(x, p);

            x = x.compareTo(p.subtract(BigInteger.ONE).divide(TWO)) == -1 ? BigInteger.ONE : BigInteger.ZERO;
            result.append(x);
        }

        return new BigInteger(result.toString(), radix);
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
    public static BigInteger BBS(final BigInteger x0, final BigInteger p, final BigInteger q) {

        if (!isValidBbsPrime(p) || !isValidBbsPrime(q)){
            throw new IllegalArgumentException("Provided P or Q are not quadratic residue");
        }

        BigInteger mod = p.multiply(q);
        BigInteger exp = TWO;

        StringBuilder result = new StringBuilder();
        BigInteger x = x0.modPow(exp, mod);

        for (int i = 0; i < length*8; ++i) {
            x = x.modPow(exp, mod);
            result.append(x.mod(TWO));
        }

        return new BigInteger(result.toString(), radix);
    }

    private static boolean isValidBbsPrime(final BigInteger prime) {
        BigInteger THREE = new BigInteger("3");
        BigInteger FOUR = new BigInteger("4");

        return prime.mod(THREE).equals(FOUR);
    }
}
