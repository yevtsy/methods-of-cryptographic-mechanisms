package kpi.pti.crypto.core.arithmetic;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author vadym
 * @since 08.03.15 20:42
 */
@RunWith(Suite.class)
@SuiteClasses({
        AdditionTest.class,
        CompareTest.class,
        SubtractionTest.class,
        LargeTest.class
})
public class LargeTestsSuite {
}
