package crypto.core.arithmetic;

import core.arithmetic.Large;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Created by yevhen.tsyba on 09.03.2015.
 */
@RunWith(Parameterized.class)
public class MultiplicationTest extends AbstractTest {
    public MultiplicationTest(final String x, final String y) {
        super(x, y);
    }

    @Parameterized.Parameters
    public static Collection numbers() {
        return Arrays.asList(new Object[][]{
                {"12", "9"},
                {"12345", "6789"},
                {
                        "9234013274012419836",
                        "29340978319"
                },
                {
                        "9234013274012419836418634983459547689126439817263478157836453178654",
                        "2934097831972391728347612783641927841983569834695"
                },
                {
                        "92340132740124198364186349834595476891264398172634781578364531786549234013274012419836418634983459547689126439817263478157836453178654",
                        "29340978319723917283476127836419278419835698346952934097831972391728347612783641927841983569834695"
                },
                {
                        "9234013274012419836418634983459547689126439817263478157836453178654923401327401241983641863498345954768912643981726347815783645317865492340132740124198364186349834595476891264398172634781578364531786549234013274012419836418634983459547689126439817263478157836453178654",
                        "2934097831972391728347612783641927841983569834695293409783197239172834761278364192784198356983469529340978319723917283476127836419278419835698346952934097831972391728347612783641927841983569834695"
                }
        });
    }


    @Test
    public void shouldMultiplication() throws Exception {
        final String expected = expectedX.multiply(expectedY).toString();


        assertEquals("should provide correct multiplication",
                expected,
                actualX.multiply(actualY).toString()
        );

        assertEquals("should provide correct Karatsyba multiplication",
                expected,
                Large.karatsuba(actualX, actualY).toString()
        );
    }
}
