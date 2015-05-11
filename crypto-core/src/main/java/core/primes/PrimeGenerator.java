package core.primes;

import java.math.BigInteger;
import java.util.Random;

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
     * The initial constellation of the table of small primes.
     */
    private static int[] ptab = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71};

    /**
     * Generates big prime number by Maurer’s algorithm.
     *
     * @param k number of bits in generated prime number
     * @return prime number
     * @see <a href="http://en.wikipedia.org/wiki/Carmichael_number">Carmichael number</a>
     * @see <a href="http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.89.4426&rep=rep1&type=pdf"/>
     * @see <a href="http://s13.zetaboards.com/Crypto/topic/7234475/1/"/>
     */
    public static BigInteger Maurer(int k) {
        Random rand = new Random();
        boolean isPrime;

        if (k < 20) {

            while (true) {
                int n = rand.nextInt(k);

                if (n % 2 == 0 || n < 3) {
                    continue;
                }

                final double sqrt = Math.sqrt(n);

                for (int p : ptab) {
                    if (p > sqrt) {
                        return BigInteger.valueOf(n);
                    }

                    if (n % p == 0) {
                        break;
                    }
                }
            }
        } else {
            /*
             We use c=0.005 which has been experimentally found to be optimal in processing
             time for common PC under MS Windows and values of k of practical interest.
             A different c value may be desirable for use in different computing
             environments.
             */

            double c = 0.005;
            int m = 20;

            double B = c * k * k, r;

            if (k > 2 * m) {

                while (true) {
                    int s = rand.nextInt();

                    r = Math.pow(2, s - 1);

                    if (k - k * r > m) {
                        break;
                    }
                }
            } else {
                r = 0.5;
            }

            BigInteger q = Maurer((int) r * k);
            BigInteger I = BigInteger.valueOf(2 ^ (k - 1)).divide(q.multiply(TWO));
            boolean success = false;
            BigInteger rBigInt, nBigInt = BigInteger.ZERO;

            while (success == false) {
                rBigInt = generateRandomBigInt(I.add(BigInteger.ONE), I.multiply(TWO));
                nBigInt = TWO.multiply(rBigInt).multiply(q).add(BigInteger.ONE);

                success = true;

                for (int p : ptab) {
                    if (p > B) {
                        break;
                    }

                    if (nBigInt.remainder(BigInteger.valueOf(p)).equals(BigInteger.ZERO)) {
                        success = false;
                        break;
                    }
                }

                if (!success) {
                    continue;
                }

                int a = generateRandomIntInRange(2, nBigInt.subtract(TWO));

                BigInteger b = BigInteger.valueOf(a).modPow(nBigInt.subtract(BigInteger.ONE), nBigInt);

                if (b.equals(BigInteger.ONE)) {
                    b = TWO.modPow(TWO.multiply(rBigInt), nBigInt);
                    if (nBigInt.gcd(b.subtract(BigInteger.ONE)).equals(BigInteger.ONE)) {
                        success = true;
                    }
                }
            }

            return nBigInt;
        }
    }

    private static int generateRandomIntInRange(final int low, final BigInteger high) {
        Random rand = new Random();

        int result = rand.nextInt(Integer.MAX_VALUE);

        while (result < low || BigInteger.valueOf(result).compareTo(high) >= 0) {
            result = rand.nextInt(Integer.MAX_VALUE);
        }

        return result;
    }

    private static BigInteger generateRandomBigInt(final BigInteger low, final BigInteger high) {
        Random rand = new Random();

        BigInteger result = new BigInteger(high.bitLength(), rand);
        while (result.compareTo(high) >= 0 || result.compareTo(low) <= 0) {
            result = new BigInteger(high.bitLength(), rand);
        }
        return result;
    }


    /**
     * Generates big prime number by Blum-Micali's algorithm.
     *
     * @param x0 seed
     * @param p  odd prime
     * @param q  prime
     * @return
     * @see <a href="http://en.wikipedia.org/wiki/Blum%E2%80%93Micali_algorithm">Blum-Micali algorithm</a>
     */
    public static BigInteger BlumMicali(final BigInteger x0, final BigInteger p, final BigInteger q) {
        StringBuilder result = new StringBuilder();

        BigInteger x = q.modPow(x0, p);

        for (int i = 0; i < length * 8; ++i) {
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
     * @param p  odd prime
     * @param q  prime
     * @return
     * @see <a href="http://en.wikipedia.org/wiki/Blum_Blum_Shub">Blum Blum Shub algorithm</a>
     */
    public static BigInteger BBS(final BigInteger x0, final BigInteger p, final BigInteger q) {

        if (!isValidBbsPrime(p) || !isValidBbsPrime(q)) {
            throw new IllegalArgumentException("Provided P or Q are not quadratic residue");
        }

        BigInteger mod = p.multiply(q);
        BigInteger exp = TWO;

        StringBuilder result = new StringBuilder();
        BigInteger x = x0.modPow(exp, mod);

        for (int i = 0; i < length * 8; ++i) {
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
