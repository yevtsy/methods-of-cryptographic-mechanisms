package core.cryptosystem;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ElGamalTest {
    private static final ElGamal.PublicKey publicKey1 = new ElGamal.PublicKey(
            BigInteger.valueOf(11),
            BigInteger.valueOf(2),
            BigInteger.valueOf(3)
    );
    private static final ElGamal.PrivateKey privateKey1 = new ElGamal.PrivateKey(BigInteger.valueOf(8));

    private static final ElGamal.PublicKey publicKey2 = new ElGamal.PublicKey(
            BigInteger.valueOf(23),
            BigInteger.valueOf(5),
            BigInteger.valueOf(17)
    );
    private static final ElGamal.PrivateKey privateKey2 = new ElGamal.PrivateKey(BigInteger.valueOf(7));


    @Test
    public void testSimpleEncryption() throws Exception {
        BigInteger m = BigInteger.valueOf(5);

        final ElGamal elGamal = new ElGamal(publicKey1);

        final ElGamal.Ciphertext ciphertext = elGamal.encrypt(m);

        assertTrue(ciphertext.a.compareTo(publicKey1.p) <= 0);
        assertTrue(ciphertext.b.compareTo(publicKey1.p) <= 0);
    }

    @Test
    public void testSimpleDecryption() throws Exception {
        final ElGamal elGamal = new ElGamal(publicKey1, privateKey1);

        final ElGamal.Ciphertext ciphertext = new ElGamal.Ciphertext();
        ciphertext.a = BigInteger.valueOf(6);
        ciphertext.b = BigInteger.valueOf(9);

        final BigInteger actualM = elGamal.decrypt(ciphertext);

        assertEquals(BigInteger.valueOf(5), actualM);
    }

    @Test
    public void testSimpleSignature() throws Exception {
        String m = "baaqab";

        final ElGamal elGamal = new ElGamal(publicKey2, privateKey2);

        final ElGamal.Signature actualSignature = elGamal.makeSignature(m);

        assertTrue(actualSignature.r.compareTo(publicKey2.p) <= 0);
        assertTrue(actualSignature.s.compareTo(publicKey2.p) <= 0);
    }

    @Test
    public void testSimpleSignatureVerification() throws Exception {
        String m = "baaqab";

        final ElGamal elGamal = new ElGamal(publicKey2, privateKey2);

        final ElGamal.Signature actualSignature = new ElGamal.Signature();
        actualSignature.r = BigInteger.valueOf(14);
        actualSignature.s = BigInteger.valueOf(4);

        final boolean actualResult = elGamal.verifySignature(actualSignature, m);

        assertTrue(actualResult);
    }
}