package core.primes;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author vadym
 * @since 05.05.15 20:57
 */
public class Chaos {
    private static Chaos instance;
    public final Random random;

    public Chaos() {
        // TO-DO; Make seed more unpredictable
        final long seed = 0xcafebabe ^ 0xdeadbeaf;
        random = new Random(seed);
    }

    public static Chaos getInstance() {
        if (instance == null) {
            instance = new Chaos();
        }
        return instance;
    }

    public BigInteger getBigInteger(final BigInteger from, final BigInteger to) {
        BigInteger result;

        int len = to.bitLength();

        do {
            result = new BigInteger(len, random);
        } while (!(result.compareTo(from) > 0 && result.compareTo(to) < 0));

        return result;
    }

    public BigInteger getMutuallyPrimeBigInteger(final BigInteger from, final BigInteger to, final BigInteger mod) {
        BigInteger result;

        do {
            result = getBigInteger(from, to);
        } while (!mod.gcd(result).equals(BigInteger.ONE));

        return result;
    }

    public BigInteger getBigInteger(int from, final BigInteger to) {
        return getBigInteger(BigInteger.valueOf(from), to);
    }
}
