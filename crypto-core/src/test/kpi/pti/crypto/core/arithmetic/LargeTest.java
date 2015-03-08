package kpi.pti.crypto.core.arithmetic;

import org.junit.Test;

import java.math.BigInteger;

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
        String x_str = "9234013274012419836418634983459547689126439817263478157836453178654";
        String y_str = "2934097831972391728347612783641927841983569834695";

        Large x_large = new Large(x_str);
        Large y_large = new Large(y_str);

        BigInteger x_bi = new BigInteger(x_str);
        BigInteger y_bi = new BigInteger(y_str);


        assertEquals(x_bi.add(y_bi).toString(), x_large.add(y_large).toString());
    }

    @Test
    public void testAddNegative() throws Exception {
        String x_str = "-9234013274012419836418634983459547689126439817263478157836453178654";
        String y_str = "-2934097831972391728347612783641927841983569834695";

        Large x_large = new Large(x_str);
        Large y_large = new Large(y_str);

        BigInteger x_bi = new BigInteger(x_str);
        BigInteger y_bi = new BigInteger(y_str);


        assertEquals(x_bi.add(y_bi).toString(), x_large.add(y_large).toString());
    }

    @Test
    public void testSubtract() throws Exception {
        String x_str = "9234013274012419836418634983459547689126439817263478157836453178654";
        String y_str = "2934097831972391728347612783641927841983569834695";

        Large x_large = new Large(x_str);
        Large y_large = new Large(y_str);

        BigInteger x_bi = new BigInteger(x_str);
        BigInteger y_bi = new BigInteger(y_str);


        assertEquals(x_bi.subtract(y_bi).toString(), x_large.subtract(y_large).toString());
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
        String x_str = "9234013274012419836418634983459547689126439817263478157836453178654";
        String y_str = "2934097831972391728347612783641927841983569834695";

        Large x_large = new Large(x_str);
        Large y_large = new Large(y_str);

        BigInteger x_bi = new BigInteger(x_str);
        BigInteger y_bi = new BigInteger(y_str);


        assertEquals(x_bi.compareTo(y_bi), x_large.compareTo(y_large));
    }

    @Test
    public void testCompareToPositiveFalse() throws Exception {
        String x_str = "9234013274012419836418634983459547689126439817263478157836453178654";
        String y_str = "2934097831972391728347612783641927841983569834695";

        Large x_large = new Large(x_str);
        Large y_large = new Large(y_str);

        BigInteger x_bi = new BigInteger(x_str);
        BigInteger y_bi = new BigInteger(y_str);


        assertEquals(y_bi.compareTo(x_bi), y_large.compareTo(x_large));
    }

    @Test
    public void testCompareToNegativeTrue() throws Exception {
        String x_str = "-9234013274012419836418634983459547689126439817263478157836453178654";
        String y_str = "-2934097831972391728347612783641927841983569834695";

        Large x_large = new Large(x_str);
        Large y_large = new Large(y_str);

        BigInteger x_bi = new BigInteger(x_str);
        BigInteger y_bi = new BigInteger(y_str);


        assertEquals(y_bi.compareTo(x_bi), y_large.compareTo(x_large));
    }

    @Test
    public void testCompareToNegativeFalse() throws Exception {
        String x_str = "-9234013274012419836418634983459547689126439817263478157836453178654";
        String y_str = "-2934097831972391728347612783641927841983569834695";

        Large x_large = new Large(x_str);
        Large y_large = new Large(y_str);

        BigInteger x_bi = new BigInteger(x_str);
        BigInteger y_bi = new BigInteger(y_str);


        assertEquals(x_bi.compareTo(y_bi), x_large.compareTo(y_large));
    }

    @Test
    public void testCompareToEqualsPositive() throws Exception {
        String val = "2934097831972391728347612783641927841983569834695";

        Large x = new Large(val);
        Large y = new Large(val);

        BigInteger x_bi = new BigInteger(val);
        BigInteger y_bi = new BigInteger(val);

        assertEquals(x_bi.compareTo(y_bi), x.compareTo(y));
    }

    @Test
    public void testCompareToEqualsNegative() throws Exception {
        String val = "-2934097831972391728347612783641927841983569834695";

        Large x = new Large(val);
        Large y = new Large(val);

        BigInteger x_bi = new BigInteger(val);
        BigInteger y_bi = new BigInteger(val);

        assertEquals(x_bi.compareTo(y_bi), x.compareTo(y));
    }

    @Test
    public void testToString() throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}