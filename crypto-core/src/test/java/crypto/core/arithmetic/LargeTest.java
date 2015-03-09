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

//    @Test
//    public void testNonSignNumber() throws Exception {
//        assertEquals("123", (new Large("-123")).getNonSignNumber());
//        assertEquals("0", (new Large("0")).getNonSignNumber());
//        assertEquals("123", (new Large("123")).getNonSignNumber());
//    }

    @Test
    public void testMultiplyByOrders() throws Exception {
        assertEquals("123000", (new Large("123")).multiplyByOrder(3).toString());
        assertEquals("0", (new Large("0")).multiplyByOrder(3).toString());
    }

    @Test
    public void testMultiplyBySimpleValue() throws Exception {
        assertEquals("369", (new Large("123")).multiplyBySimpleValue(3).toString());
        assertEquals("31488", (new Large("123")).multiplyBySimpleValue(256).toString());
        assertEquals("1272", (new Large("424")).multiplyBySimpleValue(3).toString());
        assertEquals("0", (new Large("0")).multiplyBySimpleValue(3).toString());
        assertEquals("3", (new Large("1")).multiplyBySimpleValue(3).toString());
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