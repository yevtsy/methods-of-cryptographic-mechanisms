package core.cryptosystem;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ElGamalImplTest {
    private String algorithm = "ElGamal";

    @Test
    public void shouldEncrypt() throws Exception {
        final ElGamalImpl elGamalImpl = new ElGamalImpl(
                new ElGamalImpl.PublicKey(
                        BigInteger.valueOf(11),     // p = 11
                        BigInteger.valueOf(2),      // g = 2
                        BigInteger.valueOf(3)       // y = 3
                ),
                new ElGamalImpl.PrivateKey(
                        BigInteger.valueOf(8)       // x = 8
                )
        );

        final ElGamalImpl.Ciphertext ciphertext = elGamalImpl.encrypt(algorithm, BigInteger.valueOf(5));

        assertTrue(ciphertext.a.compareTo(BigInteger.valueOf(11)) <= 0);
        assertTrue(ciphertext.b.compareTo(BigInteger.valueOf(11)) <= 0);
    }


    @Test
    public void shouldDecrypt() throws Exception {
        final ElGamalImpl elGamalImpl = new ElGamalImpl(
                new ElGamalImpl.PublicKey(
                        BigInteger.valueOf(11),     // p = 11
                        BigInteger.valueOf(2),      // g = 2
                        BigInteger.valueOf(3)       // y = 3
                ),
                new ElGamalImpl.PrivateKey(
                        BigInteger.valueOf(8)       // x = 8
                )
        );

        final ElGamalImpl.Ciphertext ciphertext = new ElGamalImpl.Ciphertext(
                BigInteger.valueOf(6),              // a = 6
                BigInteger.valueOf(9)               // b = 9
        );

        final BigInteger actualM = elGamalImpl.decrypt(algorithm, ciphertext);

        assertEquals(BigInteger.valueOf(5), actualM);
    }


    @Test
    public void shouldEncryptAndDecrypt() throws Exception {
        final ElGamalImpl elGamalImpl = new ElGamalImpl(BigInteger.valueOf(11));

        final BigInteger expected = BigInteger.valueOf(5);
        final ElGamalImpl.Ciphertext ciphertext = elGamalImpl.encrypt(algorithm, expected);
        final BigInteger actual = elGamalImpl.decrypt(algorithm, ciphertext);

        assertEquals(expected, actual);
    }


    @Test
    public void shouldSign() throws Exception {
        final ElGamalImpl elGamalImpl = new ElGamalImpl(
                new ElGamalImpl.PublicKey(
                        BigInteger.valueOf(23),     // p = 23
                        BigInteger.valueOf(5),      // g = 5
                        BigInteger.valueOf(17)      // y = 17
                ),
                new ElGamalImpl.PrivateKey(
                        BigInteger.valueOf(7)       // x = 7
                )
        );

        final ElGamalImpl.Signature actualSignature = elGamalImpl.sign(algorithm, "baaqab");

        assertTrue(actualSignature.r.compareTo(BigInteger.valueOf(23)) <= 0);
        assertTrue(actualSignature.s.compareTo(BigInteger.valueOf(23)) <= 0);
    }


    @Test
    public void shouldVerify() throws Exception {
        final ElGamalImpl elGamalImpl = new ElGamalImpl(
                new ElGamalImpl.PublicKey(
                        BigInteger.valueOf(23),     // p = 23
                        BigInteger.valueOf(5),      // g = 5
                        BigInteger.valueOf(17)      // y = 17
                ),
                new ElGamalImpl.PrivateKey(
                        BigInteger.valueOf(7)       // x = 7
                )
        );

        final ElGamalImpl.Signature actualSignature = new ElGamalImpl.Signature(
                BigInteger.valueOf(14),             // r = 14
                BigInteger.valueOf(4)               // s = 4
        );

        final boolean actualResult = elGamalImpl.verify(algorithm, actualSignature, "baaqab");

        assertTrue(actualResult);
    }


    @Test
    public void shouldSignAndVerify() throws Exception {
        final ElGamalImpl elGamalImpl = new ElGamalImpl(BigInteger.valueOf(23));

        final String message = "baaqab";
        final ElGamalImpl.Signature signature = elGamalImpl.sign(algorithm, message);

        assertTrue(elGamalImpl.verify(algorithm, signature, message));
        assertFalse(elGamalImpl.verify(algorithm, signature, "zxcvm"));

        signature.r = signature.r.flipBit(3);
        assertFalse(elGamalImpl.verify(algorithm, signature, message));
    }
}