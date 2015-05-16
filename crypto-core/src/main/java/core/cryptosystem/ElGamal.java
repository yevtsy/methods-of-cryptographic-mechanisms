package core.cryptosystem;

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
        throw new RuntimeException("not implemented yet");
    }

    /**
     * Encrypt input data using generated keys
     *
     * @param m - input data, number less than {@value p}
     * @return {a, b} - cipher text
     */
    public ImmutablePair<BigInteger, BigInteger> encrypt(BigInteger m) {
        throw new RuntimeException("not implemented yet");
    }

    /**
     * Decrypt input data using generated keys
     *
     * @param cipher - encrypted pair
     * @return decrypted data
     */
    public BigInteger decrypt(ImmutablePair<BigInteger, BigInteger> cipher) {
        throw new RuntimeException("not implemented yet");
    }

    /**
     * Make digital signature of input data
     *
     * @param m - input data
     * @return {r, s} - digital signature
     */
    public ImmutablePair<BigInteger, BigInteger> makeSignature(String m) {
        throw new RuntimeException("not implemented yet");
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
        throw new RuntimeException("not implemented yet");
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
