package crypto.core.arithmetic;

import core.arithmetic.Large;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LargeTest {

    @Test
    public void testConstructor() throws Exception {
        assertEquals("0", (new Large()).toString());
        assertEquals("123", (new Large("123")).toString());
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
        assertEquals("123000000000000", (new Large("123")).shiftLeft(3).toString());
        assertEquals("0", (new Large("0")).shiftLeft(3).toString());
    }

    @Test
    public void testAddition() throws Exception {
        assertEquals("21", (new Large("12")).add(9).toString());
        assertEquals("22", (new Large("12")).add(10).toString());
        assertEquals("5243", (new Large("5234")).add(9).toString());
    }

    @Test
    public void testSubtraction() throws Exception {
        assertEquals("5234", (new Large("5234")).subtract(0).toString());
        assertEquals("5225", (new Large("5234")).subtract(9).toString());
    }

    @Test
    public void testMultiply() throws Exception {
        assertEquals("369", (new Large("123")).multiply(3).toString());
        assertEquals("1272", (new Large("424")).multiply(3).toString());
        assertEquals("3190203", (new Large("354467")).multiply(9).toString());
        assertEquals("0", (new Large("0")).multiply(3).toString());
        assertEquals("3", (new Large("1")).multiply(3).toString());
    }

    @Test
    public void testDivide() throws Exception {
        assertEquals("41", (new Large("124")).divide(3).toString());
    }

    @Test
    public void testModulo() throws Exception {
        assertEquals("1", (new Large("124")).modulo(new Large("3")).toString());
        assertEquals("0", (new Large("123")).modulo(new Large("3")).toString());

        assertEquals(1, (new Large("124")).modulo(3));
        assertEquals(0, (new Large("123")).modulo(3));
    }


    @Test
    public void testToString() throws Exception {
        assertEquals("14627333968688430767", (new Large("14627333968688430767")).toString());
    }
}