package core.pkcs;

import core.primes.Chaos;
import core.primes.PrimeGenerator;
import org.junit.Test;

import java.math.BigInteger;

/**
 * @author vadym
 * @since 12.06.15 0:20
 */
public class PasswordBasedCryptoTest {
    @Test
    public void shouldPKCS5() throws Exception {
        BigInteger salt1 = PrimeGenerator.BlumMicali(Chaos.getInstance().random.nextLong(), 512);
        BigInteger salt2 = PrimeGenerator.BlumMicali(Chaos.getInstance().random.nextLong(), 512);

        PasswordBasedCrypto pkcs5 = new PasswordBasedCrypto("P@ssw0rd");

        System.out.println(pkcs5.deriveKey(salt1));
        System.out.println(pkcs5.deriveKey(salt2));
    }
}
