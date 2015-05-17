package showcase;

import core.Benchmark;
import core.cryptosystem.ElGamal;
import core.primes.PrimeGenerator;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.math.BigInteger;

/**
 * Created by yevhen.tsyba on 17.05.2015.
 */
public class Lab3 {
    private static final int BITS = 128;
    private static Benchmark benchmark = new Benchmark();
    private static String message = "we don't need no education";


    public static void main(String[] args) {
        double time[] = new double[4];
        final BigInteger prime = PrimeGenerator.Maurer(BITS);

        ElGamal cryptoSystem = new ElGamal();
        cryptoSystem.generateKeys(prime);

        final BigInteger plain = BigInteger.valueOf(123456789);

        benchmark.start();
        final ImmutablePair<BigInteger, BigInteger> cipher = cryptoSystem.encrypt(plain);
        time[3] = benchmark.stop();

        benchmark.start();
        final BigInteger decrypted = cryptoSystem.decrypt(cipher);
        time[2] = benchmark.stop();

        System.out.println("Plain data: " + plain);
        System.out.println("Decrypted data: " + decrypted);

        benchmark.start();
        final ImmutablePair<BigInteger, BigInteger> signature = cryptoSystem.makeSignature(message);
        time[1] = benchmark.stop();

        benchmark.start();
        final boolean verificationResult = cryptoSystem.verifySignature(signature, message);
        time[0] = benchmark.stop();

        System.out.println(String.format("%s\t %f\t%s | %f\t%s | %f\t%s | %f\t%s |",
                prime,
                time[3], "cryptoSystem.encrypt",
                time[2], "cryptoSystem.decrypt",
                time[1], "cryptoSystem.makeSignature",
                time[0], "cryptoSystem.verifySignature = " + verificationResult
        ));
    }
}
