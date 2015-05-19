package core.cryptosystem;

import core.primes.Chaos;
import core.primes.PrimeGenerator;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;

/**
 * RSA cryptosystem implementation
 *
 * @author vadym
 * @since 19.05.15 21:13
 */
public class RSA {
    /**
     * Class for storing RSA's public key.
     * Public key is a pair of (<i>e</i>, <i>n</i>), where:
     * <ul>
     *      <li><i>e</i> - is a public exponent</li>
     *      <li><i>n</i> - is a product of two primes <i>p</i> and <i>q</i></li>
     * </ul>
     *
     * @see #RSA(PublicKey, PrivateKey)
     * @see #generateKeys(int)
     * @see PrivateKey
     */
    public static class PublicKey {
        // made public for easier access, read-only because of final and immutable BigInteger
        public final BigInteger e;
        public final BigInteger n;

        public PublicKey(final BigInteger e, final BigInteger n) {
            if (e.compareTo(BigInteger.valueOf(5)) <= 0) throw
                    new NullPointerException("It is not safe to choose too small public exponent");

            this.e = e;
            this.n = n;
        }

        @Override
        public String toString() {
            return "(e=" + e + ", n=" + n + ')';
        }
    }

    /**
     * Class for storing RSA's private key.
     * Public key is a pair of (<i>d</i>, <i>n</i>), where:
     * <ul>
     *      <li><i>e</i> - is a private exponent</li>
     *      <li><i>n</i> - is a product of two primes <i>p</i> and <i>q</i></li>
     * </ul>
     *
     * @see #RSA(PublicKey, PrivateKey)
     * @see #generateKeys(int)
     * @see PublicKey
     */
    public static class PrivateKey {
        // made public for easier access, read-only because of final and immutable BigInteger
        public final BigInteger d;
        public final BigInteger n;

        public PrivateKey(final BigInteger d, final BigInteger n) {
            this.d = d;
            this.n = n;
        }

        @Override
        public String toString() {
            return "(d=" + d + ", n=" + n + ')';
        }
    }

    /**
     * Public key of RSA cryptosystem. The pair of <i>(e, n)</i>
     */
    protected PublicKey publicKey;

    /**
     * Private key of RSA cryptosystem. The pair of <i>(d, n)</i>
     */
    protected PrivateKey privateKey;


    /**
     * Constructor with keys auto-generation.
     *
     * @param bits key length
     */
    public RSA(final int bits) {
        generateKeys(bits);
    }


    /**
     * Constructor with keys initialization.
     *
     * @param publicKey public key
     * @param privateKey private key
     */
    public RSA(final PublicKey publicKey, final PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }


    /**
     * Returns RSA public key.
     *
     * @return public key
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Returns RSA private key.
     *
     * @return private key
     */
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * Generates private and public keys for RSA cryptosystem.
     * The <b>public key</b> is the pair of (<i>e</i>, <i>n</i>), and the
     * <b>private key</b> is the pair of (<i>d</i>, <i>n</i>).
     *
     * @param bits key's length
     */
    public void generateKeys(int bits) {
        final BigInteger p = PrimeGenerator.Maurer(bits);
        final BigInteger q = PrimeGenerator.Maurer(bits);
        final BigInteger n = p.multiply(q);

        final BigInteger phi = p.subtract(ONE).multiply(q.subtract(ONE));
        final BigInteger e = Chaos.getInstance().getMutuallyPrimeBigInteger(BigInteger.TEN, phi, phi);
        final BigInteger d = e.modInverse(phi);

        publicKey = new PublicKey(e, n);
        privateKey = new PrivateKey(d, n);
    }


    /**
     * Encrypts message using generated keys
     *
     * @param m message (plaintext), the number less than <i>n</i>
     * @return ciphertext <i>c</i>
     */
    public BigInteger encrypt(final BigInteger m) {
        if (publicKey == null) throw new NullPointerException("Public key is not set");

        // E(m) = m^e mod n = c
        return m.modPow(publicKey.e, publicKey.n);
    }


    /**
     * Decrypts ciphertext using generated keys
     *
     * @param c ciphertext
     * @return decrypted message <i>m</i>
     */
    public BigInteger decrypt(final BigInteger c) {
        if (privateKey == null) throw new NullPointerException("Public key is not set");

        // D(c) = c^d mod n = m
        return c.modPow(privateKey.d, privateKey.n);
    }


    /**
     * Make digital signature of the message
     *
     * @param m message (plaintext)
     * @return digital signature <i>(s)</i>
     */
    public BigInteger sign(final BigInteger m) {
        // S(m) = m^d mod n
        return decrypt(m);
    }


    /**
     * Verify digital signature
     *
     * @param signature digital signature <i>(s)</i> of the message <code>m</code>
     * @param m message (plaintext)
     * @return <i>true</i> if verification is success, <i>false</i> otherwise
     */
    public boolean verify(final BigInteger signature, final BigInteger m) {
        // V(s) = true if s^e mod n == m, false otherwise

        final BigInteger actualM = encrypt(signature);
        return m.equals(actualM);
    }
}
