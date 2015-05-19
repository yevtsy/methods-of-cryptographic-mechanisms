package showcase;

import core.Benchmark;
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
    private static Benchmark benchmark = new Benchmark();


    public static void main(String[] args) {
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
}
