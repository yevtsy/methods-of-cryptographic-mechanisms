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
    public void testAddPositive() throws Exception {
        Large x = new Large("9234013274012419836418634983459547689126439817263478157836453178654");
        Large y = new Large("2934097831972391728347612783641927841983569834695");

        assertEquals("9234013274012419839352732815431939417474052600905405999820023013349", x.add(y).toString());
    }

    @Test
    public void testAddNegative() throws Exception {
        Large x = new Large("-9234013274012419836418634983459547689126439817263478157836453178654");
        Large y = new Large("-2934097831972391728347612783641927841983569834695");

        assertEquals("-9234013274012419839352732815431939417474052600905405999820023013349", x.add(y).toString());
    }

    @Test
    public void testSubtract() throws Exception {
        // TODO yevhen.tsyba: bring back to life the test
        Large x = new Large("1000");
        Large y = new Large("999");

        assertEquals("1", x.subtract(y).toString());
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
    public void testCompareToPositiveTrue() throws Exception {
        Large x = new Large("9234013274012419836418634983459547689126439817263478157836453178654");
        Large y = new Large("2934097831972391728347612783641927841983569834695");

        assertEquals(1, x.compareTo(y));
    }

    @Test
         public void testCompareToPositiveFalse() throws Exception {
        Large x = new Large("9234013274012419836418634983459547689126439817263478157836453178654");
        Large y = new Large("2934097831972391728347612783641927841983569834695");

        assertEquals(-1, y.compareTo(x));
    }

    @Test
    public void testCompareToNegativeTrue() throws Exception {
        Large x = new Large("-9234013274012419836418634983459547689126439817263478157836453178654");
        Large y = new Large("-2934097831972391728347612783641927841983569834695");

        assertEquals(1, y.compareTo(x));
    }

    @Test
    public void testCompareToNegativeFalse() throws Exception {
        Large x = new Large("-9234013274012419836418634983459547689126439817263478157836453178654");
        Large y = new Large("-2934097831972391728347612783641927841983569834695");

        assertEquals(-1, x.compareTo(y));
    }

    @Test
    public void testCompareToEqualsPositive() throws Exception {
        Large x = new Large("2934097831972391728347612783641927841983569834695");
        Large y = new Large("2934097831972391728347612783641927841983569834695");

        assertEquals(0, x.compareTo(y));
    }

    @Test
    public void testCompareToEqualsNegative() throws Exception {
        Large x = new Large("-2934097831972391728347612783641927841983569834695");
        Large y = new Large("-2934097831972391728347612783641927841983569834695");

        assertEquals(0, x.compareTo(y));
    }

    @Test
    public void testToString() throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}