package core.primes;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        assertComplex(PrimeTest.primality(37*16));
        assertComplex(PrimeTest.primality(3557*1601));
    }

    @Test
    public void shouldTestSolovayStrassen() throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Test
    public void shouldTestMillerRabin() throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    private static void assertPrime(boolean condition) {
        assertTrue("Should be prime", condition);
    }

    private static void assertComplex(boolean condition) {
        assertFalse("Should be complex", condition);
    }
}