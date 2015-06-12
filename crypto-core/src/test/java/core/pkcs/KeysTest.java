package core.pkcs;

import core.primes.Chaos;
import core.primes.PrimeGenerator;
import org.junit.Test;

import java.math.BigInteger;

public class KeysTest {
    @Test
    public void should() throws Exception {
        BigInteger salt = PrimeGenerator.BlumMicali(Chaos.getInstance().random.nextLong(), 512);

        Keys.PrivateKey privateKey = new Keys.PrivateKey(BigInteger.valueOf(123456789));
        Keys.EncryptedPrivateKey encryptedKey = Keys.encrypt(privateKey, "P@ssw0rd", salt);

        System.out.println(encryptedKey.getEncrypted());
        System.out.println(Keys.decrypt(encryptedKey, "P@ssw0rd", salt).getKey());
    }
}