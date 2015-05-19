package core.primes;

import java.math.BigInteger;
import java.util.Random;

/**
 * Singleton class that provides randomness.
 *
 * @author vadym
 * @since 05.05.15 20:57
 */
public class Chaos {
    /**
     * Single instance of the class
     */
    private static Chaos instance;

    /**
     * Main pseudo-random generator
     */
    public final Random random;

    /**
     * Constructor that initializes randomness via generating the seed
     */
    private Chaos() {
        // TO-DO; Make seed more unpredictable
        final long seed = 0xcafebabe ^ 0xdeadbeaf;
        random = new Random(seed);
    }

    /**
     * Returns singleton instance
     * @return singleton
     */
    public static Chaos getInstance() {
        if (instance == null) {
            instance = new Chaos();
        }
        return instance;
    }

    /**
     * Chooses random number in range (<code>from</code>, <code>to</code>)
     *
     * @param from chosen number will be greater than that value
     * @param to chosen number will be lower than that value
     * @return chosen number
     * @see #getBigInteger(int, BigInteger)
     */
    public BigInteger getBigInteger(final BigInteger from, final BigInteger to) {
        BigInteger result;

        int len = to.bitLength();

        do {
            result = new BigInteger(len, random);
        } while (!(result.compareTo(from) > 0 && result.compareTo(to) < 0));

        return result;
    }


    /**
     * Chooses random number in range (<code>from</code>, <code>to</code>)
     *
     * @param from chosen number will be greater than that value
     * @param to chosen number will be lower than that value
     * @return chosen number
     * @see #getBigInteger(BigInteger, BigInteger)
     */
    public BigInteger getBigInteger(int from, final BigInteger to) {
        return getBigInteger(BigInteger.valueOf(from), to);
    }


    /**
     * Chooses random number in range (<code>from</code>, <code>to</code>),
     * such that chosen number is relatively prime to <code>mod</code>
     *
     * @param from chosen number will be greater than that value
     * @param to chosen number will be lower than that value
     * @param mod chosen number will be relatively prime to that value
     * @return chosen number
     */
    public BigInteger getMutuallyPrimeBigInteger(final BigInteger from, final BigInteger to, final BigInteger mod) {
        BigInteger result;

        do {
            result = getBigInteger(from, to);
        } while (!mod.gcd(result).equals(BigInteger.ONE));

        return result;
    }
}
