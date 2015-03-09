package crypto.core.arithmetic;

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
                {"12345", "6789"},
        });
    }


    @Test
    public void shouldMultiplication() throws Exception {
        assertEquals("should provide correct multiplication",
                expectedX.multiply(expectedY).toString(),
                actualX.multiply(actualY).toString()
        );
    }
}
