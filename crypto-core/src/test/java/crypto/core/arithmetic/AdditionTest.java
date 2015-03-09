package crypto.core.arithmetic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author vadym
 * @since 08.03.15 20:06
 */
@RunWith(Parameterized.class)
public class AdditionTest extends AbstractTest {

    public AdditionTest(String x, String y) {
        super(x, y);
    }

    @Parameterized.Parameters
    public static Collection numbers() {
        return Arrays.asList(new Object[][]{
                {"0", "0"},
                {"2", "1"},
                {
                        "9234013274012419836418634983459547689126439817263478157836453178654",
                        "2934097831972391728347612783641927841983569834695"
                }, {
                        "-9234013274012419836418634983459547689126439817263478157836453178654",
                        "-2934097831972391728347612783641927841983569834695"
                }
        });
    }


    @Test
    public void shouldAdd() throws Exception {
        assertEquals("should provide correct addition",
                expectedX.add(expectedY).toString(),
                actualX.add(actualY).toString()
        );
    }
}
