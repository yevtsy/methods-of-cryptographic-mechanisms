package core.cryptosystem;

import core.primes.Chaos;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RSATest {

    @Test
    public void shouldEncrypt() throws Exception {
        final RSA rsa = new RSA(
                new RSA.PublicKey(
                        BigInteger.valueOf(3),
                        BigInteger.valueOf(9173503)
                ),
                new RSA.PrivateKey(
                        BigInteger.valueOf(6111579),
                        BigInteger.valueOf(9173503)
                )
        );

        assertEquals(BigInteger.valueOf(4051753), rsa.encrypt(BigInteger.valueOf(111111)));
    }


    @Test
    public void shouldDecrypt() throws Exception {
        final RSA rsa = new RSA(
                new RSA.PublicKey(
                        BigInteger.valueOf(3),
                        BigInteger.valueOf(9173503)
                ),
                new RSA.PrivateKey(
                        BigInteger.valueOf(6111579),
                        BigInteger.valueOf(9173503)
                )
        );

        assertEquals(BigInteger.valueOf(111111), rsa.decrypt(BigInteger.valueOf(4051753)));
    }


    @Test
    public void shouldEncryptAndDecrypt() throws Exception {
        final RSA rsa = new RSA(32);

        final BigInteger m = Chaos.getInstance().getBigInteger(BigInteger.ONE, BigInteger.valueOf(Integer.MAX_VALUE));
        final BigInteger c = rsa.encrypt(m);
        final BigInteger actualM = rsa.decrypt(c);

        assertEquals(m, actualM);
    }


    @Test
    public void shouldSign() throws Exception {
        final RSA rsa = new RSA(
                new RSA.PublicKey(
                        BigInteger.valueOf(3),
                        BigInteger.valueOf(9173503)
                ),
                new RSA.PrivateKey(
                        BigInteger.valueOf(6111579),
                        BigInteger.valueOf(9173503)
                )
        );

        assertEquals(BigInteger.valueOf(1161470), rsa.sign(BigInteger.valueOf(111111)));
    }


    @Test
    public void shouldVerify() throws Exception {
        final RSA rsa = new RSA(
                new RSA.PublicKey(
                        BigInteger.valueOf(3),
                        BigInteger.valueOf(9173503)
                ),
                new RSA.PrivateKey(
                        BigInteger.valueOf(6111579),
                        BigInteger.valueOf(9173503)
                )
        );

        assertTrue(rsa.verify(BigInteger.valueOf(1161470), BigInteger.valueOf(111111)));
        assertFalse(rsa.verify(BigInteger.valueOf(1161470), BigInteger.valueOf(111211)));
        assertFalse(rsa.verify(BigInteger.valueOf(1199970), BigInteger.valueOf(111111)));
    }

    @Test
    public void shouldSignAndVerify() throws Exception {
        final RSA rsa = new RSA(32);

        final BigInteger m = Chaos.getInstance().getBigInteger(BigInteger.ONE, BigInteger.valueOf(Integer.MAX_VALUE));
        final BigInteger s = rsa.sign(m);

        assertTrue(rsa.verify(s, m));
        assertFalse(rsa.verify(s, m.add(BigInteger.ONE)));
        assertFalse(rsa.verify(s.add(BigInteger.ONE), m));
    }
}