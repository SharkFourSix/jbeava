package lib.gintec_rdl.jbeava.validation.filters;

import junit.framework.TestCase;
import lib.gintec_rdl.jbeava.validation.filters.integral.IntFilter;
import lib.gintec_rdl.jbeava.validation.filters.integral.NumberFilter;

import java.util.List;

public class NumberFilterTest extends TestCase {

    public void testFilter() {
        int actual;
        int expected;

        NumberFilter<Integer> intFilter;

        expected = 25;

        intFilter = new IntFilter();
        actual = intFilter.filter("age", "${value} is not a valid integer", "25", List.of());
        assertEquals("Expected and actual values do not match", expected, actual);
    }
}