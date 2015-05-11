package core.primes;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class PrimeTestTest {

    @Test
    public void shouldTestPrimality() throws Exception {
        assertPrime(PrimeTest.primality(2));
        assertPrime(PrimeTest.primality(3));
        assertPrime(PrimeTest.primality(5));
        assertPrime(PrimeTest.primality(89));
        assertPrime(PrimeTest.primality(3571));

        assertComplex(PrimeTest.primality(1));
        assertComplex(PrimeTest.primality(4));
        assertComplex(PrimeTest.primality(6));
        assertComplex(PrimeTest.primality(100));
        assertComplex(PrimeTest.primality(3556));
    }

    @Test
    public void shouldTestFermat() throws Exception {
        assertPrime(PrimeTest.Fermat(BigInteger.valueOf(991), 2));
        assertPrime(PrimeTest.Fermat(BigInteger.valueOf(3571), 2));

        assertComplex(PrimeTest.primality(37 * 16));
        assertComplex(PrimeTest.primality(3557 * 1601));
    }

    @Test
    public void shouldTestSolovayStrassen() throws Exception {
        assertPrime(PrimeTest.SolovayStrassen(new BigInteger("23"), 2));
        assertPrime(PrimeTest.SolovayStrassen(new BigInteger("19"), 2));
        assertPrime(PrimeTest.SolovayStrassen(new BigInteger("89"), 2));

        assertPrime(PrimeTest.SolovayStrassen(new BigInteger("3571"), 2));
        assertPrime(PrimeTest.SolovayStrassen(new BigInteger("28657"), 2));
        assertPrime(PrimeTest.SolovayStrassen(new BigInteger("514229"), 2));

        assertComplex(PrimeTest.SolovayStrassen(new BigInteger("5142292"), 2));
        assertComplex(PrimeTest.SolovayStrassen(new BigInteger("433494439"), 2));
        assertComplex(PrimeTest.SolovayStrassen(new BigInteger("1599"), 2));
    }

    @Test
    public void shouldTestMillerRabin() throws Exception {
        assertPrime(PrimeTest.MillerRabin(new BigInteger("23")));
        assertPrime(PrimeTest.MillerRabin(new BigInteger("19")));
        assertPrime(PrimeTest.MillerRabin(new BigInteger("89")));

        assertPrime(PrimeTest.MillerRabin(new BigInteger("3571")));
        assertPrime(PrimeTest.MillerRabin(new BigInteger("28657")));
        assertPrime(PrimeTest.MillerRabin(new BigInteger("514229")));

        assertComplex(PrimeTest.MillerRabin(new BigInteger("5142292")));
        assertComplex(PrimeTest.MillerRabin(new BigInteger("433494439")));
        assertComplex(PrimeTest.MillerRabin(new BigInteger("1599")));
    }

    @Test
    public void shouldTestJacobiSymbol() throws Exception {
        assertEquals(PrimeTest.Jacobi(BigInteger.valueOf(6278), BigInteger.valueOf(9975)), new BigInteger("-1"));
        assertEquals(PrimeTest.Jacobi(BigInteger.valueOf(1001), BigInteger.valueOf(9907)), new BigInteger("-1"));
        assertEquals(PrimeTest.Jacobi(BigInteger.valueOf(610), BigInteger.valueOf(987)), new BigInteger("-1"));
        assertEquals(PrimeTest.Jacobi(BigInteger.valueOf(19), BigInteger.valueOf(45)), BigInteger.ONE);
        assertEquals(PrimeTest.Jacobi(BigInteger.valueOf(8), BigInteger.valueOf(21)), new BigInteger("-1"));
    }

    private static void assertPrime(boolean condition) {
        assertTrue("Should be prime", condition);
    }

    private static void assertComplex(boolean condition) {
        assertFalse("Should be complex", condition);
    }
}