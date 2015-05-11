package core.primes;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

public class PrimeGeneratorTest {

    @Test
    public void shouldGenerateLightMaurer() throws Exception {
        BigInteger maurer = PrimeGenerator.Maurer(19);
        System.out.println("Maurer prime number generator : " + maurer);

        assertTrue("Generator makes not prime values", PrimeTest.MillerRabin(maurer));
    }

    @Test
    public void shouldGenerateComplexMaurer() throws Exception {
        BigInteger maurer = PrimeGenerator.Maurer(128);
        System.out.println("Maurer prime number generator : " + maurer);

        assertTrue("Generator makes not prime values", PrimeTest.MillerRabin(maurer));
    }

    @Test
    public void shouldGenerateBlumMicali() throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Test
    public void shouldGenerateBBS() throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}