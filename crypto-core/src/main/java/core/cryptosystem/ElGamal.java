package core.cryptosystem;

import core.primes.Chaos;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.math.BigInteger;

/**
 * Created by yevhen.tsyba on 16.05.2015.
 */
public class ElGamal {

    /**
     * Public key of El-Gamal cryptosystem {p, q, y}
     */
    private ImmutableTriple<BigInteger, BigInteger, BigInteger> publicKey;

    /**
     * Private key of El-Gamal cryptosystem {x}
     */
    private BigInteger privateKey;

    /**
     * Generate private & public keys to El-Gamal cryptosystem
     *
     * @param p - large random prime number
     * @see <a href="https://ru.wikipedia.org/wiki/Схема_Эль-Гамаля"/>
     */
    public void generateKeys(final BigInteger p) {
        Chaos chaos = new Chaos();
        final BigInteger g = NumberTheoryUtils.getPrimitiveRoot(p);
        final BigInteger x = chaos.getBigInteger(BigInteger.ONE, p);

        // y = g^x mod(p)
        final BigInteger y = g.modPow(x, p);

        privateKey = x;
        publicKey = ImmutableTriple.of(p, g, y);
    }

    /**
     * Encrypt input data using generated keys
     *
     * @param m - input data, number less than {@value p}
     * @return {a, b} - cipher text
     */
    public ImmutablePair<BigInteger, BigInteger> encrypt(BigInteger m) {

        if (publicKey.getLeft().compareTo(m) <= 0) {
            throw new IllegalArgumentException("Message M should be less than P");
        }

        Chaos chaos = new Chaos();
        // choose random k, where 1 < k < p-1
        BigInteger sessionKey = chaos.getBigInteger(BigInteger.ONE, publicKey.getLeft().subtract(BigInteger.ONE));

        // compute a = g^k mod(p)
        BigInteger a = publicKey.getMiddle().modPow(sessionKey, publicKey.getLeft());

        // compute b = y^k*M mod(p)
        BigInteger b = publicKey.getRight().modPow(sessionKey, publicKey.getLeft()).multiply(m).remainder(publicKey.getLeft());

        return ImmutablePair.of(a, b);
    }

    /**
     * Decrypt input data using generated keys
     *
     * @param cipher - encrypted pair
     * @return decrypted data
     */
    public BigInteger decrypt(ImmutablePair<BigInteger, BigInteger> cipher) {
        // M = b*(a^x)^-1 mod(p)

        // compute (a^x)^-1
        BigInteger inversed = cipher.getLeft().modPow(privateKey, publicKey.getLeft()).modInverse(publicKey.getLeft());

        // compute M = b * inversed mod(p)
        BigInteger m = cipher.getRight().multiply(inversed).remainder(publicKey.getLeft());

        return m;
    }

    /**
     * Make digital signature of input data
     *
     * @param m - input data
     * @return {r, s} - digital signature
     */
    public ImmutablePair<BigInteger, BigInteger> makeSignature(String m) {
        // choose Object.hashCode as hash function

        //compute m = h(M)
        BigInteger hashCode = BigInteger.valueOf(m.hashCode());
        if (hashCode.compareTo(BigInteger.ZERO) < 0) {
            hashCode = hashCode.negate();
        }

        // choose random k, where 1 < k < p-1 and GCD(k, p-1) == 1
        Chaos chaos = new Chaos();
        final BigInteger module = publicKey.getLeft().subtract(BigInteger.ONE);
        BigInteger k = chaos.getMutuallyPrimeBigInteger(BigInteger.ONE, module, module);

        // compute r = g^k mod(p)
        BigInteger r = publicKey.getMiddle().modPow(k, publicKey.getLeft());

        // compute s = (m - xr)k^-1  mod(p-1)

        BigInteger mxr = hashCode.subtract(privateKey.multiply(r)).remainder(module);

        if (mxr.compareTo(BigInteger.ZERO) < 0) {
            mxr = mxr.add(module);
        }

        BigInteger inversed = k.modInverse(module);

        BigInteger s = mxr.multiply(inversed).remainder(module);

        return ImmutablePair.of(r, s);
    }

    /**
     * Verify digital signature input data
     *
     * @param signature - {r, s} - digital signature
     * @param m         - input data
     * @return <i>true</i>, if verification is success
     * <i>false</i> if verification fails
     */
    public boolean verifySignature(ImmutablePair<BigInteger, BigInteger> signature, String m) {

        BigInteger r = signature.getLeft();
        BigInteger s = signature.getRight();

        // if 0 < r < p  or 0 < s < p-1  are false -> verification fails

        if (r.compareTo(BigInteger.ZERO) <= 0 || r.compareTo(publicKey.getLeft()) >= 0) {
            return false;
        }

        if (s.compareTo(BigInteger.ZERO) <= 0 || s.compareTo(publicKey.getLeft()) >= 0) {
            return false;
        }

        // compute m = h(M)
        BigInteger hashCode = BigInteger.valueOf(m.hashCode());
        if (hashCode.compareTo(BigInteger.ZERO) < 0) {
            hashCode = hashCode.negate();
        }

        // verification is success in case y^r * r^s = g^m mod(p)

        final BigInteger leftSide = publicKey.getRight().modPow(r, publicKey.getLeft())
                .multiply(r.modPow(s, publicKey.getLeft()))
                .remainder(publicKey.getLeft());

        final BigInteger rightSide = publicKey.getMiddle().modPow(hashCode, publicKey.getLeft());

        return leftSide.equals(rightSide);
    }

    public void showElGamalParams() {
        System.out.println("************* Private key *************");
        System.out.println("x = " + privateKey);
        System.out.println();
        System.out.println("************* Public key *************");
        System.out.println("p = " + publicKey.getLeft());
        System.out.println("q = " + publicKey.getMiddle());
        System.out.println("y = " + publicKey.getRight());
        System.out.println();
    }

    public void setPublicKey(final ImmutableTriple<BigInteger, BigInteger, BigInteger> publicKey) {
        this.publicKey = publicKey;
    }

    public void setPrivateKey(final BigInteger privateKey) {
        this.privateKey = privateKey;
    }
}
