package crypto.core.arithmetic;

import core.arithmetic.Large;

import java.math.BigInteger;

/**
 * @author vadym
 * @since 08.03.15 20:31
 */
public abstract class AbstractTest {
    protected String X, Y;
    protected BigInteger expectedX, expectedY;
    protected Large actualX, actualY;

    public AbstractTest(String x, String y) {
        X = x;
        Y = y;

        expectedX = new BigInteger(X);
        expectedY = new BigInteger(Y);

        actualX = new Large(X);
        actualY = new Large(Y);
    }
}
