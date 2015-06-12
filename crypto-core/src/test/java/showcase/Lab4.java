package showcase;

import core.cryptosystem.ElGamal;
import core.pkcs.Keys;
import core.pkcs.Session;
import core.pkcs.Token;
import core.primes.Chaos;
import core.primes.PrimeGenerator;

import java.math.BigInteger;

import static core.pkcs.Token.Mechanism.ELGAMAL;

/**
 * @author yevhen.tsyba
 * @author vadym
 * @since 17.05.2015
 */
public class Lab4 {
    public static void main(String[] args) {
        final Token token = new Token("1234", "token1");

        System.out.println("Token label = " + token.getLabel());
        System.out.println("Token Mechanisms = " + token.getMechanismList());

        String password = "P@ssw0rd";
        BigInteger salt = PrimeGenerator.BlumMicali(Chaos.getInstance().random.nextLong(), 512);

        try (Session session = token.getSession()) {

            final BigInteger p = PrimeGenerator.Maurer(128);
            final BigInteger plain = BigInteger.valueOf(123456789);

            session.generateKeyPair(ELGAMAL, p, password, salt);
            System.out.println("p = " + p);
            System.out.println("public key = " + ((ElGamal) session).getPublicKey());
            System.out.println("encrypted private key = " + ((ElGamal) session).getEncryptedPrivateKey());

//            password = "foooo";

            System.out.println("private key = " +
                    Keys.decrypt(((ElGamal) session).getEncryptedPrivateKey(), password, salt).getKey()
            );

            final Session.Ciphertext c = session.encrypt(ELGAMAL, plain);
            System.out.println("E(" + plain + ") = " + c);

            final BigInteger d = session.decrypt(ELGAMAL, c, password, salt);
            System.out.println("D(" + c + ") = " + d);

            final Session.Signature s = session.sign(ELGAMAL, plain.toString(), password, salt);
            System.out.println("S("+ plain + ") = " + s);

            final boolean verify = session.verify(ELGAMAL, s, plain.toString());
//            final boolean verify = session.verify(ELGAMAL, s, "fooo");
            System.out.println("V("+ s + ", " + plain + ") = " + verify);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            password = null;
            salt = null;
            System.gc();
        }
    }
}
