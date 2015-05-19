package core.cryptosystem;

import core.primes.Chaos;

import java.math.BigInteger;

/**
 * Extended RSA cryptosystem with blind signature implementation.
 *
 * @author vadym
 * @since 19.05.15 22:59
 */
public class BlindRSA extends RSA {
    /**
     * Random blinding factor
     */
    private BigInteger r;


    /**
     * Constructor with keys auto-generation.
     *
     * @param bits key length
     */
    public BlindRSA(int bits) {
        super(bits);
    }


    /**
     * Constructor with keys initialization.
     *
     * @param publicKey public key
     * @param privateKey private key
     */
    public BlindRSA(PublicKey publicKey, PrivateKey privateKey) {
        super(publicKey, privateKey);
    }


    /**
     * Blinds message for signing.
     * Should be called before sending to the signing authority.
     *
     * @param m message
     * @return blinded message <i>m'</i>
     */
    public BigInteger blind(final BigInteger m) {
        if (publicKey == null) throw new NullPointerException("Public key is not set");

        // choose random r, such that 1 < r < n and r is relative prime to n
        r = Chaos.getInstance().getMutuallyPrimeBigInteger(BigInteger.ONE, publicKey.n, publicKey.n);

        // compute blind factor b: b = r^e mod n
        final BigInteger b = encrypt(r);

        // compute blinded message: m' = m * b mod n = m * r^e mod n
        return m.multiply(b).remainder(publicKey.n);
    }


    /**
     * Unblinds signed message.
     *
     * @param signed blind signed message <i>s'</i>
     * @return signature <i>s</i> of ublinded message <i>m</i>
     */
    public BigInteger unblind(final BigInteger signed) {
        if (publicKey == null) throw new NullPointerException("Public key is not set");

        // compute signature: s = s' * r^-1 mod n
        return signed.multiply(r.modInverse(publicKey.n)).remainder(publicKey.n);
    }
}
