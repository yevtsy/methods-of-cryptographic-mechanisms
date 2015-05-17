package core.cryptosystem;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class NumberTheoryUtilsTest {

    @Test
    public void testPhi24() throws Exception {
        final BigInteger actualPhi = NumberTheoryUtils.phi(BigInteger.valueOf(24));

        final BigInteger expectedPhi = BigInteger.valueOf(8);

        assertEquals(expectedPhi, actualPhi);
    }

    @Test
    public void testPhi4() throws Exception {
        final BigInteger actualPhi = NumberTheoryUtils.phi(BigInteger.valueOf(4));

        final BigInteger expectedPhi = BigInteger.valueOf(2);

        assertEquals(expectedPhi, actualPhi);
    }

    @Test
    public void testPhi5() throws Exception {
        final BigInteger actualPhi = NumberTheoryUtils.phi(BigInteger.valueOf(5));

        final BigInteger expectedPhi = BigInteger.valueOf(4);

        assertEquals(expectedPhi, actualPhi);
    }

    @Test
    public void testPrimeRootBy7() throws Exception {
        final BigInteger primitiveRoot = NumberTheoryUtils.getPrimitiveRoot(BigInteger.valueOf(7));

        assertTrue(BigInteger.valueOf(3).equals(primitiveRoot) || BigInteger.valueOf(6).equals(primitiveRoot));

    }
}