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
        boolean isPrimes[] = new boolean[3];

        while (true) {
            benchmark.start();
            p = PrimeGenerator.Maurer(BITS);
//            p = PrimeGenerator.BlumMicali(0xcafebabe, BITS);
//            p = PrimeGenerator.BBS(0xcafebabe, BITS);
            benchmark.stop("generator");

            benchmark.start();
            isPrimes[0] = PrimeTest.Fermat(p, rounds);
            benchmark.stop("Fermat");

            benchmark.start();
            isPrimes[1] = PrimeTest.SolovayStrassen(p, rounds);
            benchmark.stop("SolovayStrassen");

            benchmark.start();
            isPrimes[2] = PrimeTest.MillerRabin(p, rounds);
            benchmark.stop("MillerRabin");

            System.out.println(String.format("%40s    %8.3fms  |  %8.3fms  %s  |  %8.3fms  %s  |  %8.3fms  %s",
                    p, benchmark.getTime("generator"),
                    benchmark.getTime("Fermat"), isPrimes[0],
                    benchmark.getTime("SolovayStrassen"), isPrimes[1],
                    benchmark.getTime("MillerRabin"), isPrimes[2]
            ));
        }
    }
}
