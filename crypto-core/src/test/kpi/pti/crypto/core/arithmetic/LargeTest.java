package kpi.pti.crypto.core.arithmetic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LargeTest {

    @Test
    public void testConstructor() throws Exception {
        Large x = new Large("123");
        System.out.println(x);
    }

    @Test
    public void testAbs() throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Test
    public void testSign() throws Exception {
        assertEquals(-1, (new Large("-123")).sign());
        assertEquals(0, (new Large("0")).sign());
        assertEquals(1, (new Large("123")).sign());
    }


    @Test
    public void testMultiply() throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Test
    public void testDivide() throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Test
    public void testModulo() throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Test
    public void testPower() throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Test
    public void testToString() throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}