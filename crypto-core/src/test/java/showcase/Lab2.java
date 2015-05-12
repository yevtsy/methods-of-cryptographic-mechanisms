package showcase;

import core.Benchmark;
import core.primes.PrimeGenerator;
import core.primes.PrimeTest;

import java.math.BigInteger;

/**
 * @author vadym
 * @since 07.03.15.
 */
public class Lab2 {
    private static final int BITS = 128;
    private static final int rounds = 3;
    private static Benchmark benchmark = new Benchmark();

    public static void main(String[] args) {
        BigInteger p;
        double time[] = new double[4];
        boolean isPrimes[] = new boolean[3];

        while (true) {
            benchmark.start();
            p = PrimeGenerator.Maurer(BITS);
//            p = PrimeGenerator.BlumMicali(0xcafebabe, BITS);
//            p = PrimeGenerator.BBS(0xcafebabe, BITS);
            time[3] = benchmark.stop();

            benchmark.start();
            isPrimes[0] = PrimeTest.Fermat(p, rounds);
            time[0] = benchmark.stop();

            benchmark.start();
            isPrimes[1] = PrimeTest.SolovayStrassen(p, rounds);
            time[1] = benchmark.stop();

            benchmark.start();
            isPrimes[2] = PrimeTest.MillerRabin(p, rounds);
            time[2] = benchmark.stop();

            System.out.println(String.format("%s\t %f\t | %f\t%s | %f\t%s | %f\t%s |",
                    p,
                    time[3],
                    time[0], isPrimes[0],
                    time[1], isPrimes[1],
                    time[2], isPrimes[2]
            ));
        }
    }
}
