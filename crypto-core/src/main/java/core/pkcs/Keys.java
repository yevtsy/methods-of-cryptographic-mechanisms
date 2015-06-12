package core.pkcs;

import java.math.BigInteger;

/**
 * @author vadym
 * @since 12.06.15 0:27
 */
public class Keys {
    private static final String ALGORITHM = "XOR";

    public interface Key {
    }

    public static class PrivateKey implements Key {
        public static final float version = 1.2f;
        private BigInteger key;

        public PrivateKey(BigInteger key) {
            this.key = key;
        }

        public BigInteger getKey() {
            return key;
        }

        @Override
        public String toString() {
            return "PrivateKey{" +
                    "key=" + key +
                    '}';
        }
    }

    public static class EncryptedPrivateKey implements Key {
        public static final String algorithm = ALGORITHM;
        private BigInteger encrypted;

        public EncryptedPrivateKey(BigInteger encrypted) {
            this.encrypted = encrypted;
        }

        public BigInteger getEncrypted() {
            return encrypted;
        }

        @Override
        public String toString() {
            return "(encrypted=" + encrypted + ')';
        }
    }

    public static class PublicKey implements Key {
    }

    public static EncryptedPrivateKey encrypt(final PrivateKey privateKey, String password, BigInteger salt) {
        final BigInteger key = (new PasswordBasedCrypto(password)).deriveKey(salt);

        return new EncryptedPrivateKey(privateKey.key.xor(key));
    }

    public static PrivateKey decrypt(final EncryptedPrivateKey encryptedKey, String password,  BigInteger salt) {
        final BigInteger key = (new PasswordBasedCrypto(password)).deriveKey(salt);

        return new PrivateKey(encryptedKey.encrypted.xor(key));
    }
}
