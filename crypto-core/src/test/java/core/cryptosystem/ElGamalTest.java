package core.cryptosystem;

import core.pkcs.Keys;
import core.pkcs.Token;
import core.primes.Chaos;
import core.primes.PrimeGenerator;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ElGamalTest {
    private Token.Mechanism algorithm = Token.Mechanism.ELGAMAL;
    private String password = "P@ssw0rd";
    private BigInteger salt = PrimeGenerator.BlumMicali(Chaos.getInstance().random.nextLong(), 512);

    @Test
    public void shouldEncrypt() throws Exception {
        final ElGamal elGamal = new ElGamal(
                new ElGamal.PublicKey(
                        BigInteger.valueOf(11),     // p = 11
                        BigInteger.valueOf(2),      // g = 2
                        BigInteger.valueOf(3)       // y = 3
                ),
                Keys.encrypt(new Keys.PrivateKey(BigInteger.valueOf(8)), password, salt)  // x = 8
        );

        final ElGamal.Ciphertext ciphertext = elGamal.encrypt(algorithm, BigInteger.valueOf(5));

        assertTrue(ciphertext.a.compareTo(BigInteger.valueOf(11)) <= 0);
        assertTrue(ciphertext.b.compareTo(BigInteger.valueOf(11)) <= 0);
    }


    @Test
    public void shouldDecrypt() throws Exception {
        final ElGamal elGamal = new ElGamal(
                new ElGamal.PublicKey(
                        BigInteger.valueOf(11),     // p = 11
                        BigInteger.valueOf(2),      // g = 2
                        BigInteger.valueOf(3)       // y = 3
                ),
                Keys.encrypt(new Keys.PrivateKey(BigInteger.valueOf(8)), password, salt)  // x = 8
        );

        final ElGamal.Ciphertext ciphertext = new ElGamal.Ciphertext(
                BigInteger.valueOf(6),              // a = 6
                BigInteger.valueOf(9)               // b = 9
        );

        final BigInteger actualM = elGamal.decrypt(algorithm, ciphertext, password, salt);

        assertEquals(BigInteger.valueOf(5), actualM);
    }


    @Test
    public void shouldEncryptAndDecrypt() throws Exception {
        final ElGamal elGamal = new ElGamal(BigInteger.valueOf(11), password, salt);

        final BigInteger expected = BigInteger.valueOf(5);
        final ElGamal.Ciphertext ciphertext = elGamal.encrypt(algorithm, expected);
        final BigInteger actual = elGamal.decrypt(algorithm, ciphertext, password, salt);

        assertEquals(expected, actual);
    }


    @Test
    public void shouldSign() throws Exception {
        final ElGamal elGamal = new ElGamal(
                new ElGamal.PublicKey(
                        BigInteger.valueOf(23),     // p = 23
                        BigInteger.valueOf(5),      // g = 5
                        BigInteger.valueOf(17)      // y = 17
                ),
                Keys.encrypt(new Keys.PrivateKey(BigInteger.valueOf(7)), password, salt)  // x = 7
        );

        final ElGamal.Signature actualSignature = elGamal.sign(algorithm, "baaqab", password, salt);

        assertTrue(actualSignature.r.compareTo(BigInteger.valueOf(23)) <= 0);
        assertTrue(actualSignature.s.compareTo(BigInteger.valueOf(23)) <= 0);
    }


    @Test
    public void shouldVerify() throws Exception {
        final ElGamal elGamal = new ElGamal(
                new ElGamal.PublicKey(
                        BigInteger.valueOf(23),     // p = 23
                        BigInteger.valueOf(5),      // g = 5
                        BigInteger.valueOf(17)      // y = 17
                ),
                Keys.encrypt(new Keys.PrivateKey(BigInteger.valueOf(7)), password, salt)  // x = 7
        );

        final ElGamal.Signature actualSignature = new ElGamal.Signature(
                BigInteger.valueOf(14),             // r = 14
                BigInteger.valueOf(4)               // s = 4
        );

        final boolean actualResult = elGamal.verify(algorithm, actualSignature, "baaqab");

        assertTrue(actualResult);
    }


    @Test
    public void shouldSignAndVerify() throws Exception {
        final ElGamal elGamal = new ElGamal(BigInteger.valueOf(23), password, salt);

        final String message = "baaqab";
        final ElGamal.Signature signature = elGamal.sign(algorithm, message, password, salt);

        assertTrue(elGamal.verify(algorithm, signature, message));
        assertFalse(elGamal.verify(algorithm, signature, "zxcvm"));

        signature.r = signature.r.flipBit(3);
        assertFalse(elGamal.verify(algorithm, signature, message));
    }
}