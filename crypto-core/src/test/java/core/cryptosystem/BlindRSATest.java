package core.cryptosystem;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BlindRSATest {

    @Test
    public void shouldSignBlinded() throws Exception {
        final BlindRSA rsa = new BlindRSA(32);

        final BigInteger m = BigInteger.valueOf(111111);

        final BigInteger blinded = rsa.blind(m);
        final BigInteger signed = rsa.sign(blinded);
        final BigInteger s = rsa.unblind(signed);

        assertTrue(rsa.verify(s, m));
        assertFalse(rsa.verify(s, m.add(BigInteger.ONE)));
        assertFalse(rsa.verify(s.add(BigInteger.ONE), m));
    }
}