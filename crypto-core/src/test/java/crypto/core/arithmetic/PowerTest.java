package crypto.core.arithmetic;

import core.arithmetic.Large;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Created by yevhen.tsyba on 3/23/2015.
 */
@RunWith(Parameterized.class)
public class PowerTest {
    private BigInteger expectedX;
    private Large actualX;
    private int expectedY, actualY;

    public PowerTest(String x, String y) {
        expectedX = new BigInteger(x);
        expectedY = Integer.parseInt(y);

        actualX = new Large(x);
        actualY = Integer.parseInt(y);
    }

    @Parameterized.Parameters
    public static Collection numbers() {
        return Arrays.asList(new Object[][]{
                {"0", "0"},
                {"2", "1"},
                {
                        "923401327923401327923401327",
                        "212"
                }, {
                "-923401327923401327923401327",
                "67"
        }
        });
    }

    @Test
    public void shouldPower() throws Exception {
        assertEquals("should provide correct power calculations",
                expectedX.pow(expectedY).toString(),
                actualX.power(actualY).toString()
        );
    }
}
