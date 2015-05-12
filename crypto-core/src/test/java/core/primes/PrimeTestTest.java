package core.primes;

import org.junit.Test;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
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

        assertComplex(PrimeTest.primality(6241));
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
        assertPrime(PrimeTest.SolovayStrassen(BigInteger.valueOf(23), 2));
        assertPrime(PrimeTest.SolovayStrassen(BigInteger.valueOf(19), 2));
        assertPrime(PrimeTest.SolovayStrassen(BigInteger.valueOf(89), 2));

        assertPrime(PrimeTest.SolovayStrassen(BigInteger.valueOf(3571), 2));
        assertPrime(PrimeTest.SolovayStrassen(BigInteger.valueOf(28657), 2));
        assertPrime(PrimeTest.SolovayStrassen(BigInteger.valueOf(514229), 2));

        assertComplex(PrimeTest.SolovayStrassen(BigInteger.valueOf(5142292), 2));
        assertComplex(PrimeTest.SolovayStrassen(BigInteger.valueOf(433494439), 2));
        assertComplex(PrimeTest.SolovayStrassen(BigInteger.valueOf(1599), 2));
    }

    @Test
    public void shouldTestMillerRabin() throws Exception {
        assertPrime(PrimeTest.MillerRabin(BigInteger.valueOf(23), 2));
        assertPrime(PrimeTest.MillerRabin(BigInteger.valueOf(19), 2));
        assertPrime(PrimeTest.MillerRabin(BigInteger.valueOf(89), 2));

        assertPrime(PrimeTest.MillerRabin(BigInteger.valueOf(3571), 2));
        assertPrime(PrimeTest.MillerRabin(BigInteger.valueOf(28657), 2));
        assertPrime(PrimeTest.MillerRabin(BigInteger.valueOf(514229), 2));

        assertComplex(PrimeTest.MillerRabin(BigInteger.valueOf(5142292), 2));
        assertComplex(PrimeTest.MillerRabin(BigInteger.valueOf(433494439), 2));
        assertComplex(PrimeTest.MillerRabin(BigInteger.valueOf(1599), 2));
    }

    @Test
    public void shouldTestJacobiSymbol() throws Exception {
        assertEquals(PrimeTest.Jacobi(BigInteger.valueOf(6278), BigInteger.valueOf(9975)), ONE.negate());
        assertEquals(PrimeTest.Jacobi(BigInteger.valueOf(1001), BigInteger.valueOf(9907)), ONE.negate());
        assertEquals(PrimeTest.Jacobi(BigInteger.valueOf(610), BigInteger.valueOf(987)), ONE.negate());
        assertEquals(PrimeTest.Jacobi(BigInteger.valueOf(19), BigInteger.valueOf(45)), ONE);
        assertEquals(PrimeTest.Jacobi(BigInteger.valueOf(8), BigInteger.valueOf(21)), ONE.negate());
    }

    public static void assertPrime(boolean condition) {
        assertTrue("Should be prime", condition);
    }

    public static void assertComplex(boolean condition) {
        assertFalse("Should be complex", condition);
    }
}