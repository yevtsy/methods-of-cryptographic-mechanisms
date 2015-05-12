package core.primes;

import org.junit.Test;

import static core.primes.PrimeTestTest.assertPrime;
import static org.junit.Assert.assertNotNull;

public class PrimeGeneratorTest {
    private static int seed = Chaos.getInstance().random.nextInt();

    @Test
    public void shouldGenerateMaurer() throws Exception {
        assertPrime(PrimeTest.MillerRabin(PrimeGenerator.Maurer(256)));
        assertPrime(PrimeTest.MillerRabin(PrimeGenerator.Maurer(128)));
        assertPrime(PrimeTest.MillerRabin(PrimeGenerator.Maurer(64)));
        assertPrime(PrimeTest.MillerRabin(PrimeGenerator.Maurer(32)));
        assertPrime(PrimeTest.MillerRabin(PrimeGenerator.Maurer(16)));
        assertPrime(PrimeTest.MillerRabin(PrimeGenerator.Maurer(8)));
    }

    @Test
    public void shouldGenerateBlumMicali() throws Exception {
        assertNotNull(PrimeGenerator.BlumMicali(seed, 32));
    }

    @Test
    public void shouldGenerateBBS() throws Exception {
        assertNotNull(PrimeGenerator.BBS(seed, 32));
    }
}