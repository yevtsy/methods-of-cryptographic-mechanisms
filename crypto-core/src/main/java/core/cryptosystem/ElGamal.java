package core.cryptosystem;

import core.primes.Chaos;
import java.math.BigInteger;

import static java.math.BigInteger.ONE;

/**
 * El-Gamal cryptosystem implementation
 *
 * @author yevhen.tsyba
 * @author vadym
 * @since 16.05.2015
 */
public class ElGamal {
    /**
     * El-Gamal's public key definition
     */
    public static class PublicKey {
        public final BigInteger p;
        public final BigInteger g;
        public final BigInteger y;

        public PublicKey(final BigInteger p, final BigInteger g, final BigInteger y) {
            this.p = p;
            this.g = g;
            this.y = y;
        }
    }

    /**
     * El-Gamal's private key definition
     */
    public static class PrivateKey {
        public final BigInteger x;

        public PrivateKey(BigInteger x) {
            this.x = x;
        }
    }

    /**
     * El-Gamal's ciphertext definition
     */
    public static class Ciphertext {
        public BigInteger a;
        public BigInteger b;
    }

    /**
     * El-Gamal's message signature definition
     */
    public static class Signature {
        public BigInteger r;
        public BigInteger s;
    }

    /**
     * Public key of El-Gamal cryptosystem {p, q, y}
     */
    private PublicKey publicKey;

    /**
     * Private key of El-Gamal cryptosystem {x}
     */
    private PrivateKey privateKey;


    /**
     * Constructor with auto key generation.
     *
     * @param p random prime
     */
    public ElGamal(final BigInteger p) {
        generateKeys(p);
    }

    /**
     * Constructor with public key initialization.
     *
     * @param publicKey public key
     */
    public ElGamal(final PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Constructor with keys initialization.
     *
     * @param publicKey public key
     * @param privateKey private key
     */
    public ElGamal(final PublicKey publicKey, final PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /**
     * Generate private and public keys to El-Gamal cryptosystem
     *
     * @param p - large random prime number
     * @see <a href="https://ru.wikipedia.org/wiki/Схема_Эль-Гамаля"/>
     */
    public void generateKeys(final BigInteger p) {
        final BigInteger g = NumberTheoryUtils.getPrimitiveRoot(p);
        final BigInteger x = Chaos.getInstance().getBigInteger(ONE, p);

        // y = g^x mod(p)
        final BigInteger y = g.modPow(x, p);

        privateKey = new PrivateKey(x);
        publicKey = new PublicKey(p, g, y);
    }

    /**
     * Encrypt input data using generated keys
     *
     * @param m - input data, number less than p
     * @return {a, b} - cipher text
     */
    public Ciphertext encrypt(final BigInteger m) {
        if (publicKey == null) throw new NullPointerException("Public key is not set");

        if (publicKey.p.compareTo(m) < 0) {
            throw new IllegalArgumentException("Message M should be less than P");
        }

        // choose session key k, such that: 1 < k < p-1
        final BigInteger sessionKey = Chaos.getInstance().getBigInteger(ONE, publicKey.p.subtract(ONE));
        final Ciphertext c = new Ciphertext();

        // compute a = g^k mod(p)
        c.a = publicKey.g.modPow(sessionKey, publicKey.p);

        // compute b = y^k*M mod(p)
        c.b = publicKey.y.modPow(sessionKey, publicKey.p).multiply(m).remainder(publicKey.p);

        return c;
    }

    /**
     * Decrypt input data using generated keys
     *
     * @param c - encrypted pair
     * @return decrypted data
     */
    public BigInteger decrypt(final Ciphertext c) {
        if (publicKey == null) throw new NullPointerException("Public key is not set");
        if (privateKey == null) throw new NullPointerException("Private key is not set");

        // M = b*(a^x)^-1 mod(p)

        // compute (a^x)^-1
        BigInteger inversed = c.a.modPow(privateKey.x, publicKey.p).modInverse(publicKey.p);

        // compute M = b * inversed mod(p)
        BigInteger m = c.b.multiply(inversed).remainder(publicKey.p);

        return m;
    }

    /**
     * Make digital signature of input data
     *
     * @param m - input data
     * @return {r, s} - digital signature
     */
    public Signature makeSignature(String m) {
        if (publicKey == null) throw new NullPointerException("Public key is not set");
        if (privateKey == null) throw new NullPointerException("Private key is not set");

        // choose Object.hashCode as hash function

        //compute m = h(M)
        BigInteger hashCode = BigInteger.valueOf(m.hashCode());
        if (hashCode.signum() < 0) {
            hashCode = hashCode.negate();
        }

        // choose random k, where 1 < k < p-1 and GCD(k, p-1) == 1
        final BigInteger module = publicKey.p.subtract(ONE);
        BigInteger k = Chaos.getInstance().getMutuallyPrimeBigInteger(ONE, module, module);

        final Signature signature = new Signature();

        // compute r = g^k mod(p)
        signature.r = publicKey.g.modPow(k, publicKey.p);

        // compute s = (m - xr)k^-1  mod(p-1)

        BigInteger mxr = hashCode.subtract(privateKey.x.multiply(signature.r)).remainder(module);

        if (mxr.signum() < 0) {
            mxr = mxr.add(module);
        }

        BigInteger inversed = k.modInverse(module);

        signature.s = mxr.multiply(inversed).remainder(module);

        return signature;
    }

    /**
     * Verify digital signature input data
     *
     * @param signature - {r, s} - digital signature
     * @param m         - input data
     * @return <i>true</i>, if verification is success
     * <i>false</i> if verification fails
     */
    public boolean verifySignature(final Signature signature, String m) {
        if (publicKey == null) throw new NullPointerException("Public key is not set");

        // if 0 < r < p  or 0 < s < p-1  are false -> verification fails

        if (signature.r.signum() <= 0 || signature.r.compareTo(publicKey.p) >= 0) {
            return false;
        }

        if (signature.s.signum() <= 0 || signature.s.compareTo(publicKey.p.subtract(ONE)) >= 0) {
            return false;
        }

        // compute m = h(M)
        BigInteger hashCode = BigInteger.valueOf(m.hashCode());
        if (hashCode.signum() < 0) {
            hashCode = hashCode.negate();
        }

        // verification is success in case y^r * r^s = g^m mod(p)

        final BigInteger leftSide = publicKey.y.modPow(signature.r, publicKey.p)
                .multiply(signature.r.modPow(signature.s, publicKey.p))
                .remainder(publicKey.p);

        final BigInteger rightSide = publicKey.g.modPow(hashCode, publicKey.p);

        return leftSide.equals(rightSide);
    }
}
