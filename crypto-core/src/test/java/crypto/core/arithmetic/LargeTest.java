package crypto.core.arithmetic;

import core.arithmetic.Large;
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
        assertEquals("123", (new Large("-123")).abs().toString());
        assertEquals("0", (new Large("0")).abs().toString());
        assertEquals("123", (new Large("123")).abs().toString());
    }

    @Test
    public void testSign() throws Exception {
        assertEquals(-1, (new Large("-123")).sign());
        assertEquals(0, (new Large("0")).sign());
        assertEquals(1, (new Large("123")).sign());
    }

    @Test
    public void testShiftLeft() throws Exception {
        assertEquals("123000", (new Large("123")).shiftLeft(3).toString());
        assertEquals("0", (new Large("0")).shiftLeft(3).toString());
    }

    @Test
    public void testMultiplyBySimpleValue() throws Exception {
        assertEquals("369", (new Large("123")).multiply(3).toString());
        assertEquals("1272", (new Large("424")).multiply(3).toString());
        assertEquals("3190203", (new Large("354467")).multiply(9).toString());
        assertEquals("0", (new Large("0")).multiply(3).toString());
        assertEquals("3", (new Large("1")).multiply(3).toString());
    }


    @Test
    public void testMultiply() throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Test
    public void testDivide() throws Exception {
        assertEquals("41", (new Large("124")).divide(new Large("3")).toString());
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