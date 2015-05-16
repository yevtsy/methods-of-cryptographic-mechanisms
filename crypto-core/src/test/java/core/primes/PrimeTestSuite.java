package core.primes;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author vadym
 * @since 05.05.15 20:02
 */
@RunWith(Suite.class)
@SuiteClasses({
        PrimeTestTest.class,
        PrimeGeneratorTest.class
})
public class PrimeTestSuite {
}
