package core.cryptosystem;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

public class ElGamalTest {

    @Test
    public void testSimpleEncryption() throws Exception {
        BigInteger m = BigInteger.valueOf(5);
        ImmutableTriple<BigInteger, BigInteger, BigInteger> publicKey = new ImmutableTriple<>(BigInteger.valueOf(11),
                BigInteger.valueOf(2), BigInteger.valueOf(3));

        ElGamal elGamal = new ElGamal();
        elGamal.setPublicKey(publicKey);

        final ImmutablePair<BigInteger, BigInteger> actualCipher = elGamal.encrypt(m);
        final ImmutablePair<BigInteger, BigInteger> expectedCipher = new ImmutablePair<>(BigInteger.valueOf(6), BigInteger.valueOf(9));

        assertTrue(expectedCipher.equals(actualCipher));
    }

    @Test
    public void testSimpleDecryprion() throws Exception {
        BigInteger privateKey = BigInteger.valueOf(8);
        ImmutableTriple<BigInteger, BigInteger, BigInteger> publicKey = new ImmutableTriple<>(BigInteger.valueOf(11),
                BigInteger.valueOf(2), BigInteger.valueOf(3));

        ElGamal elGamal = new ElGamal();
        elGamal.setPrivateKey(privateKey);
        elGamal.setPublicKey(publicKey);

        final BigInteger actualM = elGamal.decrypt(ImmutablePair.of(BigInteger.valueOf(6), BigInteger.valueOf(9)));
        BigInteger expectedM = BigInteger.valueOf(5);

        assertTrue(expectedM.equals(actualM));
    }

    @Test
    public void testSimpleSignature() throws Exception {
        String m = "baaqab";
        BigInteger privateKey = BigInteger.valueOf(7);
        ImmutableTriple<BigInteger, BigInteger, BigInteger> publicKey = new ImmutableTriple<>(BigInteger.valueOf(23),
                BigInteger.valueOf(5), BigInteger.valueOf(17));

        ElGamal elGamal = new ElGamal();
        elGamal.setPrivateKey(privateKey);
        elGamal.setPublicKey(publicKey);

        final ImmutablePair<BigInteger, BigInteger> actualSignature = elGamal.makeSignature(m);
        final ImmutablePair<BigInteger, BigInteger> expectedSignature = ImmutablePair.of(BigInteger.valueOf(20), BigInteger.valueOf(21));

        assertTrue(expectedSignature.equals(actualSignature));
    }

    @Test
    public void testSimpleSignatureVerification() throws Exception {
        String m = "baaqab";
        BigInteger privateKey = BigInteger.valueOf(7);
        ImmutableTriple<BigInteger, BigInteger, BigInteger> publicKey = new ImmutableTriple<>(BigInteger.valueOf(11),
                BigInteger.valueOf(2), BigInteger.valueOf(3));

        ElGamal elGamal = new ElGamal();
        elGamal.setPrivateKey(privateKey);
        elGamal.setPublicKey(publicKey);

        final boolean actualResult = elGamal.verifySignature(ImmutablePair.of(BigInteger.valueOf(20), BigInteger.valueOf(21)), m);

        assertTrue(actualResult);
    }
}