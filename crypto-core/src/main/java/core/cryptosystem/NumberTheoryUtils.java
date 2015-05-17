package core.cryptosystem;

import core.primes.Chaos;

import java.math.BigInteger;

/**
 * @author yevhen.tsyba
 * @author vadym
 * @since 17.05.2015.
 */
public class NumberTheoryUtils {
    /**
     * Calculates Euler's function
     *
     * @param n - large prime number
     * @return phi(n)
     * @see <a href="http://e-maxx.ru/algo/export_euler_function"/>
     */
    public static BigInteger phi(BigInteger n) {

        if (n.isProbablePrime(15)) {
            return n.subtract(BigInteger.ONE);
        }

        BigInteger result = n;

        for (BigInteger i = BigInteger.valueOf(2); i.multiply(i).compareTo(n) <= 0; i = i.add(BigInteger.ONE))
            if (n.remainder(i).equals(BigInteger.ZERO)) {
                while (n.remainder(i).equals(BigInteger.ZERO))
                    n = n.divide(i);
                result = result.subtract(result.divide(i));
            }
        if (n.compareTo(BigInteger.ONE) > 0)
            result = result.subtract(result.divide(n));

        return result;
    }

    /**
     * Calculates Primitive root by modulo
     *
     * @param p - large prime number
     * @return primitive root of {@code p}
     * @see <a href="https://en.wikipedia.org/wiki/Primitive_root_modulo_n"/>
     */
    public static BigInteger getPrimitiveRoot(BigInteger p) {
        final BigInteger phi = phi(p);

        BigInteger g;
        // g^phi(p) = 1 mod(p) - prime root of p
        do {
            g = Chaos.getInstance().getBigInteger(BigInteger.ONE, p);
        } while (!g.modPow(phi, p).equals(BigInteger.ONE));

        return g;
    }
}
