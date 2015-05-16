package core.primes;

import java.math.BigInteger;

import static core.primes.PrimeTest.TWO;
import static core.primes.PrimeTest.THREE;
import static core.primes.PrimeTest.FOUR;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

/**
 * Class provides prime generator algorithms.
 *
 * @author vadym
 * @since 05.05.15 19:23
 */
public class PrimeGenerator {
    /**
     * The initial constellation of the table of small primes.
     */
    private static int[] PRIMES = {
            2,   3,   5,   7,   11,  13,  17,  19,  23,  29,
            31,  37,  41,  43,  47,  53,  59,  61,  67,  71,
            73,  79,  83,  89,  97,  101, 103, 107, 109, 113,
            127, 131, 137, 139, 149, 151, 157, 163, 167, 173,
            179, 181, 191, 193, 197, 199, 211, 223, 227, 229,
            233, 239, 241, 251, 257, 263, 269, 271, 277, 281,
            283, 293, 307, 311, 313, 317, 331, 337, 347, 349,
            353, 359, 367, 373, 379, 383, 389, 397, 401, 409,
            419, 421, 431, 433, 439, 443, 449, 457, 461, 463,
            467, 479, 487, 491, 499, 503, 509, 521, 523, 541,
            547, 557, 563, 569, 571, 577, 587, 593, 599, 601,
            607, 613, 617, 619, 631, 641, 643, 647, 653, 659,
            661, 673, 677, 683, 691, 701, 709, 719, 727, 733,
            739, 743, 751, 757, 761, 769, 773, 787, 797, 809,
            811, 821, 823, 827, 829, 839, 853, 857, 859, 863,
            877, 881, 883, 887, 907, 911, 919, 929, 937, 941,
            947, 953, 967, 971, 977, 983, 991, 997,
    };

    /**
     * Provides division by a set of small prime numbers.
     *
     * @param n number for testing
     * @param bound limit value
     * @return <code>true</code> if was divided, <code>false</code> otherwise
     */
    private static boolean isDivisibleByPrimes(final BigInteger n, int bound) {
        for (int i = 0; i < PRIMES.length && PRIMES[i] <= bound; i++) {
            if (n.mod(BigInteger.valueOf(PRIMES[i])).equals(ZERO)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Generates big prime number by Maurer’s algorithm.
     *
     * @param k number of bits in generated prime number
     * @return prime number
     * @see "Handbook of Applied Cryptography" – A. Menezes, P van Oorschot.; 4.62 Algorithm; p.153
     */
    public static BigInteger Maurer(final int k) {
        if (k < 20) {
            // Maurer algorithm for small numbers
            int n;

            do {
                n = Chaos.getInstance().random.nextInt((int)Math.pow(2, k));

            } while (!PrimeTest.primality(n));

            return BigInteger.valueOf(n);
        }

        // Maurer algorithm for big numbers

        int m = 20;
        double c = 0.1;
        double r = 0.5;
        int B = (int) (c * k * k);

        if (k > 2*m) {
            double s;

            do {
                s = Chaos.getInstance().random.nextDouble();
                r = Math.pow(2, s - 1);

            } while (k - k * r <= m);
        }

        BigInteger q = Maurer((int) Math.floor(r * k) + 1);
        BigInteger I = TWO.pow(k - 1).divide(TWO.multiply(q));
        BigInteger R, n, d;

        while (true) {
            R = Chaos.getInstance().getBigInteger(I.add(ONE), TWO.multiply(I));
            n = TWO.multiply(R).multiply(q).add(ONE);

            if (isDivisibleByPrimes(n, B)) continue;

            BigInteger a = Chaos.getInstance().getBigInteger(2, n.subtract(TWO));
            BigInteger b = a.modPow(n.subtract(ONE), n);

            if (b.equals(ONE)) {
                b = a.modPow(TWO.multiply(R), n);
                d = n.gcd(b.subtract(ONE));

                if (d.equals(ONE)) break;
            }
        }

        return n;
    }


    /**
     * Generates big prime number by Blum-Micali's algorithm.
     *
     * @param seed initial seed
     * @param length length of generated sequence
     * @return
     * @see <a href="http://en.wikipedia.org/wiki/Blum%E2%80%93Micali_algorithm">Blum-Micali algorithm</a>
     */
    public static BigInteger BlumMicali(final long seed, final int length) {
        BigInteger p,q;

        // generate random primes p,q
        do {
            p = Maurer(64);
            q = Maurer(64);
        } while (p.equals(q));

        StringBuilder result = new StringBuilder();
        BigInteger x = BigInteger.valueOf(seed);

        for (int i = 0; i < length; i++) {
            x = q.modPow(x, p);

            result.append(x.compareTo(p.subtract(ONE).divide(TWO)) < 0 ? 1 : 0);
        }

        return new BigInteger(result.toString(), 2);
    }


    /**
     * Generates big prime number by BBS algorithm.
     *
     * @param seed initial seed
     * @param length length of generated sequence
     * @return
     * @see <a href="http://en.wikipedia.org/wiki/Blum_Blum_Shub">Blum Blum Shub algorithm</a>
     */
    public static BigInteger BBS(final long seed, final int length) {
        BigInteger p,q;

        // generate random primes p and q such that: p mod 3 = 4, q mod 3 = 4
        do {
            p = Maurer(64);
            q = Maurer(64);
        } while (p.mod(THREE).equals(FOUR) && q.mod(THREE).equals(FOUR));


        final BigInteger mod = p.multiply(q);

        StringBuilder result = new StringBuilder();
        BigInteger x = BigInteger.valueOf(seed);

        for (int i = 0; i < length; i++) {
            x = x.modPow(TWO, mod);
            result.append(x.mod(TWO));
        }

        return new BigInteger(result.toString(), 2);
    }
}
