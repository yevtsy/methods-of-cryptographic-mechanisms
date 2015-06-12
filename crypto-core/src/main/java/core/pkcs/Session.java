package core.pkcs;

import java.math.BigInteger;

/**
 * Created by yevhen.tsyba on 10.06.2015.
 */
public interface Session extends AutoCloseable {
    public interface Ciphertext {}
    public interface Signature {}

    /**
     * Generates private and public keys for El-Gamal cryptosystem.
     * The <b>public key</b> is the triple of (<i>p</i>, <i>g</i>, <i>y</i>), and the
     * <b>private key</b> is <i>x</i>.
     *
     * <p><b>&lt!&gt Warning:</b> Actually,<i>g</i> should be a primitive root of <i>p</i>, but
     * according to Bruce Schneier, it's more practical to choose any random number
     * between <i>1</i> and <i>p</i>.</p>
     *
     * @param keydata large random prime number
     */
    public void generateKeyPair(Token.Mechanism mechanism, BigInteger keydata, String password, BigInteger salt);

    /**
     * Encrypts message using generated keys
     *
     * @param data message (plaintext), the number less than <i>p</i>
     * @return ciphertext <i>(a,b)</i>
     */
    public Ciphertext encrypt(Token.Mechanism mechanism,  BigInteger data);


    /**
     * Decrypts ciphertext using generated keys
     *
     * @param data ciphertext <i>(a,b)</i>
     * @return decrypted message <i>m</i>
     */
    public BigInteger decrypt(Token.Mechanism mechanism, Ciphertext data, String password, BigInteger salt);

    /**
     * Make digital signature of the message
     *
     * @param data message (plaintext)
     * @return digital signature <i>(r, s)</i>
     */
    public Signature sign(Token.Mechanism mechanism, String data, String password, BigInteger salt);

    /**
     * Verify digital signature
     *
     * @param signature digital signature <i>(r, s)</i> of the message <code>m</code>
     * @param data message (plaintext)
     * @return <i>true</i> if verification is success, <i>false</i> otherwise
     */
    public boolean verify(Token.Mechanism mechanism, Signature signature, String data);
}
