package showcase;

import core.Benchmark;
import core.cryptosystem.BlindRSA;
import core.cryptosystem.ElGamal;
import core.primes.PrimeGenerator;

import java.math.BigInteger;

/**
 * @author yevhen.tsyba
 * @author vadym
 * @since 17.05.2015
 */
public class Lab3 {
    private static ElGamal elGamal;
    private static BlindRSA rsa;
    private static Benchmark benchmark = new Benchmark();


    public static void main(String[] args) {
//        ElGamalShowcase();
        BlindedRSAShowcase();
    }

    public static void ElGamalShowcase() {
        final BigInteger prime = PrimeGenerator.Maurer(128);
        final BigInteger plain = BigInteger.valueOf(123456789);
        String message = "we don't need no education";
        boolean verify;

        benchmark.start();
        elGamal = new ElGamal(prime);
        benchmark.stop();
        System.out.println("Key generation: " + benchmark.getTime());
        System.out.println("\tp = " + prime);
        System.out.println("\tpublic key = " + elGamal.getPublicKey());
        System.out.println("\tprivate key = " + elGamal.getPublicKey());

        benchmark.start();
        final ElGamal.Ciphertext cipher = elGamal.encrypt(plain);
        benchmark.stop();
        System.out.println("Encryption: " + benchmark.getTime());
        System.out.println("\tE("+ plain + ") = " + cipher);

        benchmark.start();
        final BigInteger decrypted = elGamal.decrypt(cipher);
        benchmark.stop();
        System.out.println("Decryption: " + benchmark.getTime());
        System.out.println("\tD("+ cipher + ") = " + decrypted);

        benchmark.start();
        final ElGamal.Signature signature = elGamal.sign(message);
        benchmark.stop();
        System.out.println("Sign: " + benchmark.getTime());
        System.out.println("\tS(\""+ message + "\") = " + signature);

        benchmark.start();
        verify = elGamal.verify(signature, message);
        benchmark.stop();
        System.out.println("Verify: " + benchmark.getTime());
        System.out.println("\tV("+ signature + ", \"" + message + "\") = " + verify);

        message = message.toUpperCase();
        benchmark.start();
        verify = elGamal.verify(signature, message);
        benchmark.stop();
        System.out.println("Verify: " + benchmark.getTime());
        System.out.println("\tV("+ signature + ", \"" + message + "\") = " + verify);
    }

    public static void BlindedRSAShowcase() {
        BigInteger m = BigInteger.valueOf(111111);
        boolean verify;

        benchmark.start();
        rsa = new BlindRSA(128);
        benchmark.stop();
        System.out.println("Key generation: " + benchmark.getTime());
        System.out.println("\tpublic key = " + rsa.getPublicKey());
        System.out.println("\tprivate key = " + rsa.getPublicKey());

        benchmark.start();
        final BigInteger c = rsa.encrypt(m);
        benchmark.stop();
        System.out.println("Encryption: " + benchmark.getTime());
        System.out.println("\tE("+ m + ") = " + c);

        benchmark.start();
        final BigInteger decrypted = rsa.decrypt(c);
        benchmark.stop();
        System.out.println("Decryption: " + benchmark.getTime());
        System.out.println("\tD("+ c + ") = " + decrypted);

        benchmark.start();
        final BigInteger signature = rsa.sign(m);
        benchmark.stop();
        System.out.println("Sign: " + benchmark.getTime());
        System.out.println("\tS("+ m + ") = " + signature);

        benchmark.start();
        verify = rsa.verify(signature, m);
        benchmark.stop();
        System.out.println("Verify: " + benchmark.getTime());
        System.out.println("\tV("+ signature + ", " + m + ") = " + verify);

        m = BigInteger.valueOf(119911);
        benchmark.start();
        verify = rsa.verify(signature, m);
        benchmark.stop();
        System.out.println("Verify: " + benchmark.getTime());
        System.out.println("\tV("+ signature + ", " + m + ") = " + verify);

        m = BigInteger.valueOf(111111);
        benchmark.start();
        final BigInteger blinded = rsa.blind(m);
        benchmark.stop();
        System.out.println("Blind: " + benchmark.getTime());
        System.out.println("\tB("+ m + ") = " + blinded);

        benchmark.start();
        final BigInteger signed = rsa.sign(blinded);
        benchmark.stop();
        System.out.println("Blinded sign: " + benchmark.getTime());
        System.out.println("\tS("+ blinded + ") = " + signed);

        benchmark.start();
        final BigInteger unblinded = rsa.unblind(signed);
        benchmark.stop();
        System.out.println("Unblind: " + benchmark.getTime());
        System.out.println("\tU("+ signed + ") = " + unblinded);
    }
}
