package core.cryptosystem;

import core.primes.Chaos;
import core.primes.PrimeTest;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;

/**
 * El-Gamal cryptosystem implementation
 *
 * @author yevhen.tsyba
 * @author vadym
 * @since 16.05.2015
 * @see <a href="https://ru.wikipedia.org/wiki/Схема_Эль-Гамаля">El-Gamal Cryptosystem Wiki</a>
 */
public class ElGamal {
    /**
     * Class for storing El-Gamal's public key.
     * Public key is the triple of (<i>p</i>, <i>g</i>, <i>y</i>), where:
     * <ul>
     *      <li><i>p</i> - is a large random prime</li>
     *      <li><i>g</i> - is a primitive root of <i>p</i></li>
     *      <li><i>y</i> - is a discrete logarithm</li>
     * </ul>
     *
     * @see #ElGamal(PublicKey, PrivateKey)
     * @see #generateKeys(BigInteger)
     * @see PrivateKey
     */
    public static class PublicKey {
        // made public for easier access, read-only because of final and immutable BigInteger
        public final BigInteger p;
        public final BigInteger g;
        public final BigInteger y;

        public PublicKey(final BigInteger p, final BigInteger g, final BigInteger y) {
            if (!PrimeTest.MillerRabin(p)) throw new IllegalArgumentException("p should be prime");

            this.p = p;
            this.g = g;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(p=" + p + ", g=" + g + ", y=" + y + ')';
        }
    }

    /**
     * Class for storing El-Gamal's private key.
     * Private key is the single <i>x</i>.
     *
     * @see #ElGamal(PublicKey, PrivateKey)
     * @see #generateKeys(BigInteger)
     * @see PublicKey
     */
    public static class PrivateKey {
        // made public for easier access, read-only because of final and immutable BigInteger
        public final BigInteger x;

        public PrivateKey(final BigInteger x) {
            this.x = x;
        }

        @Override
        public String toString() {
            return "(x=" + x + ')';
        }
    }

    /**
     * Class for storing El-Gamal's ciphertext.
     * Ciphertext is the pair of (<i>a</i>, <i>b</i>).
     *
     * @see #encrypt(BigInteger)
     * @see #decrypt(Ciphertext)
     */
    public static class Ciphertext {
        // made public for easier access
        public BigInteger a;
        public BigInteger b;

        public Ciphertext(final BigInteger a, final BigInteger b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public String toString() {
            return "(a=" + a + ", b=" + b + ')';
        }
    }

    /**
     * Class for storing El-Gamal's message signature.
     * Signature is the pair of (<i>r</i>, <i>s</i>).
     *
     * @see #sign(String)
     * @see #verify(Signature, String)
     */
    public static class Signature {
        // made public for easier access
        public BigInteger r;
        public BigInteger s;

        public Signature(final BigInteger r, final BigInteger s) {
            this.r = r;
            this.s = s;
        }

        @Override
        public String toString() {
            return "(r=" + r + ", s=" + s + ')';
        }
    }

    /**
     * Public key of El-Gamal cryptosystem. The triple of <i>(p, q, y)</i>
     */
    private PublicKey publicKey;

    /**
     * Private key of El-Gamal cryptosystem. Single <i>(x)</i>
     */
    private PrivateKey privateKey;


    /**
     * Constructor with keys auto-generation.
     *
     * @param p random prime
     */
    public ElGamal(final BigInteger p) {
        generateKeys(p);
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
     * Returns El-Gamal's public key.
     *
     * @return public key
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Returns El-Gamal's private key.
     *
     * @return private key
     */
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * Generate private and public keys to El-Gamal cryptosystem.
     * The <b>public key</b> is the triple of (<i>p</i>, <i>g</i>, <i>y</i>), and the
     * <b>private key</b> is <i>x</i>.
     *
     * <p><b>&lt!&gt Warning:</b> Actually,<i>g</i> should be a primitive root of <i>p</i>, but 
     * according to Bruce Schneier, it's more practical to choose any random number 
     * between <i>1</i> and <i>p</i>.</p>
     *
     * @param p large random prime number
     */
    public void generateKeys(final BigInteger p) {
        if (!PrimeTest.MillerRabin(p)) throw new IllegalArgumentException("p should be prime");

        final BigInteger g = Chaos.getInstance().getBigInteger(ONE, p);
        final BigInteger x = Chaos.getInstance().getBigInteger(ONE, p);

        // y = g^x mod(p)
        final BigInteger y = g.modPow(x, p);

        privateKey = new PrivateKey(x);
        publicKey = new PublicKey(p, g, y);
    }

    
    /**
     * Encrypt message using generated keys
     *
     * @param m message (plaintext), the number less than <i>p</i>
     * @return ciphertext <i>(a,b)</i>
     */
    public Ciphertext encrypt(final BigInteger m) {
        if (publicKey == null) throw new NullPointerException("Public key is not set");

        if (publicKey.p.compareTo(m) < 0) {
            throw new IllegalArgumentException("Message M should be less than P");
        }

        // choose session key k, such that: 1 < k < p-1
        final BigInteger k = Chaos.getInstance().getBigInteger(ONE, publicKey.p.subtract(ONE));
        BigInteger a, b;

        // compute a = g^k mod(p)
        a = publicKey.g.modPow(k, publicKey.p);

        // compute b = y^k*M mod(p)
        b = publicKey.y.modPow(k, publicKey.p).multiply(m).remainder(publicKey.p);

        return new Ciphertext(a, b);
    }


    /**
     * Decrypt ciphertext using generated keys
     *
     * @param c ciphertext <i>(a,b)</i>
     * @return decrypted message
     */
    public BigInteger decrypt(final Ciphertext c) {
        if (publicKey == null) throw new NullPointerException("Public key is not set");
        if (privateKey == null) throw new NullPointerException("Private key is not set");

        // M = b*((a^x)^-1) mod(p)
        
        // compute temp = (a^x)^-1 mod p
        BigInteger temp = c.a.modPow(privateKey.x, publicKey.p).modInverse(publicKey.p);

        // compute M = b * temp mod(p)
        BigInteger m = c.b.multiply(temp).remainder(publicKey.p);

        return m;
    }


    /**
     * Make digital signature of the message
     *
     * @param m message (plaintext)
     * @return digital signature <i>(r, s)</i>
     */
    public Signature sign(String m) {
        if (publicKey == null) throw new NullPointerException("Public key is not set");
        if (privateKey == null) throw new NullPointerException("Private key is not set");

        //compute m = hash(M)
        final BigInteger hashCode = HASH(m);

        // choose random k, where 1 < k < p-1 and GCD(k, p-1) == 1
        final BigInteger module = publicKey.p.subtract(ONE);
        BigInteger k = Chaos.getInstance().getMutuallyPrimeBigInteger(ONE, module, module);

        BigInteger r, s;

        // compute r = g^k mod(p)
        r = publicKey.g.modPow(k, publicKey.p);

        // compute s = (m - xr)k^-1  mod(p-1)

        BigInteger mxr = hashCode.subtract(privateKey.x.multiply(r)).remainder(module);

        if (mxr.signum() < 0) {
            mxr = mxr.add(module);
        }

        s = mxr.multiply(k.modInverse(module)).remainder(module);

        return new Signature(r, s);
    }


    /**
     * Verify digital signature
     *
     * @param signature digital signature <i>(r, s)</i> of the message <code>m</code>
     * @param m message (plaintext)
     * @return <i>true</i> if verification is success, <i>false</i> otherwise
     */
    public boolean verify(final Signature signature, String m) {
        if (publicKey == null) throw new NullPointerException("Public key is not set");

        // if 0 < r < p  or 0 < s < p-1 then verification fails
        if (signature.r.signum() <= 0 ||
                signature.s.signum() <= 0 ||
                signature.r.compareTo(publicKey.p) >= 0 ||
                signature.s.compareTo(publicKey.p.subtract(ONE)) >= 0) {
            return false;
        }

        // compute m = h(M)
        final BigInteger hashCode = HASH(m);

        // verification is success in case y^r * r^s = g^m mod(p)

        final BigInteger leftSide = publicKey.y.modPow(signature.r, publicKey.p)
                .multiply(signature.r.modPow(signature.s, publicKey.p))
                .remainder(publicKey.p);

        final BigInteger rightSide = publicKey.g.modPow(hashCode, publicKey.p);

        return leftSide.equals(rightSide);
    }

    /**
     * Provides a <b>non-cryptographically secure</b> hash-function.
     * Used for creating and verifying the signature.
     * Based on {@link String#hashCode()}.
     *
     * @param m string message
     * @return positive hash-function of the message
     * @see #sign(String)
     * @see #verify(Signature, String)
     */
    private static BigInteger HASH(final String m) {
        BigInteger hashCode = BigInteger.valueOf(m.hashCode());
        if (hashCode.signum() < 0) {
            hashCode = hashCode.negate();
        }
        return hashCode;
    }
}
