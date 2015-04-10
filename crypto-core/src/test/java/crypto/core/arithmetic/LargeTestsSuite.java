package crypto.core.arithmetic;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author vadym
 * @since 08.03.15 20:42
 */
@RunWith(Suite.class)
@SuiteClasses({
        LargeTest.class,
        AdditionTest.class,
        CompareTest.class,
        DivisionTest.class,
        MultiplicationTest.class,
        PowerTest.class,
        SubtractionTest.class
})
public class LargeTestsSuite {
}
