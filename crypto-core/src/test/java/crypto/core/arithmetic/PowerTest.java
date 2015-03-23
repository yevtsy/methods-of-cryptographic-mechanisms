package crypto.core.arithmetic;

import core.arithmetic.Large;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

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
                        "9234013274012419836418634983459547689126439817263478157836453178654",
                        "213"
                }, {
                "-9234013274012419836418634983459547689126439817263478157836453178654",
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
