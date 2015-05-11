package core.primes;

import java.math.BigInteger;
import java.util.Random;

import static java.math.BigInteger.ONE;

/**
 * Class provides probabilistic algorithms for primality testing.
 *
 * @author vadym
 * @see <a href="http://cryptowiki.net/index.php?title=%D0%90%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC%D1%8B,_%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D1%83%D0%B5%D0%BC%D1%8B%D0%B5_%D0%BF%D1%80%D0%B8_%D1%80%D0%B5%D0%B0%D0%BB%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D0%B8_%D0%B0%D1%81%D0%B8%D0%BC%D0%BC%D0%B5%D1%82%D1%80%D0%B8%D1%87%D0%BD%D1%8B%D1%85_%D0%BA%D1%80%D0%B8%D0%BF%D1%82%D0%BE%D1%81%D1%85%D0%B5%D0%BC">Алгоритмы, используемые при реализации асимметричных криптосхем</a>
 * @since 05.05.15 18:31
 */
public class PrimeTest {
    /**
     * Useful constant
     */
    private static final BigInteger TWO = new BigInteger("2");

    /**
     * Provides simple primality test by division on every number
     * from 2 to <i>square(<code>n</code>)</i>.
     *
     * @param n integer number for testing
     * @return <code>false</code> if <code>n</code> is complex number, and
     * <code>true</code> if <code>n</code> is <b>probably</b> prime
     * @see <a href="https://en.wikipedia.org/wiki/Primality_test#Pseudocode">Primality test</a>
     */
    public static boolean primality(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;

        if (n % 2 == 0 || n % 3 == 0) return false;

        for (int i = 5; i < Math.sqrt(n); i += 6) {
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

        BigInteger a, r;

        for (int i = 0; i < t; i++) {
            // a = random [2; n-2]
            a = Chaos.getInstance().getBigInteger(2, n.subtract(BigInteger.valueOf(2)));

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

        BigInteger a, condition;
        String randStr;
        Random random = new Random();

        for (int i = 0; i < k; ++i) {

            randStr = String.valueOf(random.nextInt(n.bitLength() * n.bitLength() * n.bitLength()));
            while (n.compareTo(new BigInteger(randStr)) <= 0) {
                randStr = String.valueOf(random.nextInt(n.bitLength() * n.bitLength() * n.bitLength()));
            }
            a = new BigInteger(randStr);

            if (!a.gcd(n).equals(BigInteger.ONE)) {
                return false;
            }

            BigInteger jacobi = Jacobi(a, n);
            condition = a.modPow((n.subtract(BigInteger.ONE)).divide(TWO), n);

            if (!jacobi.equals(condition) && !jacobi.add(n).equals(condition)) {
                return false;
            }
        }

        return true;
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
    public static boolean MillerRabin(final BigInteger n) {

        int certainty = n.bitLength() * n.bitLength();

        return n.isProbablePrime(certainty);
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

        BigInteger ans = BigInteger.ZERO, eight = new BigInteger("8"), four = new BigInteger("4"),
                three = new BigInteger("3");

        if (m.equals(BigInteger.ZERO)) {
            ans = n.equals(BigInteger.ONE) ? BigInteger.ONE : BigInteger.ZERO;
        } else if (m.equals(TWO)) {
            switch (n.remainder(eight).intValue()) {
                case 1:
                case 7:
                    ans = BigInteger.ONE;
                    break;
                case 3:
                case 5:
                    ans = BigInteger.ONE.negate();
                    break;
            }
        } else if (m.compareTo(n) >= 0) {
            ans = Jacobi(m.remainder(n), n);
        } else if (m.remainder(TWO).equals(BigInteger.ZERO)) {
            ans = Jacobi(TWO, n).multiply(Jacobi(m.divide(TWO), n));
        } else {
            ans = (m.remainder(four).equals(three) && n.remainder(four).equals(three)) ?
                    Jacobi(n, m).negate() : Jacobi(n, m);
        }
        return ans;
    }
}
